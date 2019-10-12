package org.yui.redis.bean.aspect;

import org.yui.aop.util.AopUtil;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.yui.expression.util.SpelUtil;
import org.yui.redis.annotation.RedisCacheSet;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * @author huangjinlong
 * @time 2019-03-25 18:44
 * @description
 */
@Log4j2
@Aspect
@Component
public class RedisCacheSetAspect {

    @Autowired
    private RedissonClient redissonClient;

    /**
     * 定义切面
     */
    @Pointcut("@annotation(org.yui.redis.annotation.RedisCacheSet)" +
            " || @annotation(org.yui.redis.annotation.RedisCacheSet.List)")
    public void redisCacheSet() {}

    /**
     *
     * @param proceedingJoinPoint
     * @return
     * @throws Throwable
     */
    @Around("redisCacheSet()")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

        Object[] args = proceedingJoinPoint.getArgs();

        Method method = AopUtil.around2Method(proceedingJoinPoint);

        List<RedisCacheSet> redisCacheSetList = new ArrayList<>();
        Optional.ofNullable(method.getAnnotation(RedisCacheSet.List.class)).ifPresent((x) -> {
            redisCacheSetList.addAll(Arrays.asList(x.value()));
        });
        Optional.ofNullable(method.getAnnotation(RedisCacheSet.class)).ifPresent(redisCacheSetList::add);

        Object result = proceedingJoinPoint.proceed();

        redisCacheSetList.forEach((redisCacheSet) -> {
            String salt = null;
            if (args.length > 0) {
                /**
                 * 用spel解析盐
                 */
                salt = SpelUtil.parse(redisCacheSet.salt(),method,args);
            } else {
                salt = redisCacheSet.salt();
            }

            String key = redisCacheSet.keyPrefix() + salt;
            RBucket<Object> rBucket = redissonClient.getBucket(key);

            if (redisCacheSet.allowNull() || (null != result)) {
                rBucket.set(result, redisCacheSet.ttl(), redisCacheSet.timeUnit());
                log.info("结果放入redis成功,key:{}",key);
            }
        });
        return result;
    }
}
