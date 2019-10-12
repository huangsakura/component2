package org.yui.redis.bean.aspect;

import org.yui.aop.util.AopUtil;
import com.google.common.collect.Lists;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.yui.base.bean.constant.StringConstant;
import org.yui.expression.util.SpelUtil;
import org.yui.redis.annotation.RedisRokku;
import org.yui.redis.bean.service.RedisRokkuInfo;
import org.yui.redis.util.RedisUtil;

import java.lang.reflect.Method;
import java.util.*;

/**
 * @author huangjinlong
 * @time 2019-03-06 17:11
 * @description
 */
@Log4j2
@Aspect
@Component
public class RedisRokkuAspect {

    @Autowired
    private RedissonClient redissonClient;

    /**
     * 定义切面
     */
    @Pointcut("@annotation(org.yui.redis.annotation.RedisRokku)" +
            " || @annotation(org.yui.redis.annotation.RedisRokku.List)")
    public void redisRokku() {}

    /**
     * around切面
     * @param proceedingJoinPoint
     * @return
     * @throws Throwable
     */
    @Around("redisRokku()")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

        Object[] args = proceedingJoinPoint.getArgs();

        Method method = AopUtil.around2Method(proceedingJoinPoint);

        List<RedisRokkuInfo> redisRokkuInfoList = Lists.newArrayList(packageRedissonLockKey(method,args));

        /**
         * 加锁与解锁
         */
        int siz = redisRokkuInfoList.size();
        RLock[] rLocks = new RLock[redisRokkuInfoList.size()];
        try {
            for (int i = 0;i < siz;i ++) {
                RedisRokkuInfo redisRokkuInfo = redisRokkuInfoList.get(i);
                rLocks[i] = redissonClient.getFairLock(redisRokkuInfo.getKey());
                RedisUtil.lock(rLocks[i],redisRokkuInfo.getWaitTime(),redisRokkuInfo.getLeaseTime());
            }
            return proceedingJoinPoint.proceed();
        } finally {
            for (RLock rLock : rLocks) {
                RedisUtil.unlock(rLock);
            }
        }
    }

    /**
     *
     * @param method
     * @param args
     * @return
     */
    private static Set<RedisRokkuInfo> packageRedissonLockKey(Method method, Object[] args) {

        List<RedisRokku> redisRokkuList = null;
        /**
         * 取出方法的 @RedisRokku 注解的信息
         */
        RedisRokku.List rokkuList = method.getAnnotation(RedisRokku.List.class);
        if (null != rokkuList) {
            redisRokkuList = new ArrayList<>();
            Collections.addAll(redisRokkuList, rokkuList.value());
        } else {
            RedisRokku redisRokku = method.getAnnotation(RedisRokku.class);
            if (null != redisRokku) {
                redisRokkuList = new ArrayList<>();
                redisRokkuList.add(redisRokku);
            }
        }
        Validate.isTrue(!CollectionUtils.isEmpty(redisRokkuList));

        Set<RedisRokkuInfo> result = new LinkedHashSet<>();

        for (RedisRokku redisRokku : redisRokkuList) {

            RedisUtil.validateLockTime(redisRokku.waitTime(),redisRokku.leaseTime());

            /**
             * 锁key的前缀
             */
            String prefix = null;

            if (!redisRokku.entity().isInterface()) {
                prefix = redisRokku.entity().getName();
            } else {
                if (StringUtils.isNotBlank(redisRokku.fixedPrefix())) {
                    prefix = redisRokku.fixedPrefix();
                }
            }
            Validate.isTrue(StringUtils.isNotBlank(prefix));

            String[] salts = null;
            if (args.length > 0) {
                /**
                 * 用spel解析盐
                 */
                salts = SpelUtil.parse(redisRokku.salt(),method,args)
                        .split(StringConstant.GENERAL_COMMA_SPLIT);
            } else {
                salts = redisRokku.salt().split(StringConstant.GENERAL_COMMA_SPLIT);
            }

            for (String salt : salts) {
                RedisRokkuInfo redisRokkuInfo = new RedisRokkuInfo();
                redisRokkuInfo.setKey(RedisUtil.packageRedissonRokkuKey(prefix,salt));
                redisRokkuInfo.setWaitTime(redisRokku.waitTime());
                redisRokkuInfo.setLeaseTime(redisRokku.leaseTime());

                result.add(redisRokkuInfo);
            }
        }
        return result;
    }
}
