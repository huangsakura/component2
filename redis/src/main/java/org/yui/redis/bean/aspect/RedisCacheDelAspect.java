package org.yui.redis.bean.aspect;

import org.yui.aop.util.AopUtil;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.redisson.api.RKeys;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.yui.expression.util.SpelUtil;
import org.yui.redis.annotation.RedisCacheDel;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * @author huangjinlong
 * @time 2019-03-25 18:47
 * @description
 */
@Log4j2
@Aspect
@Component
public class RedisCacheDelAspect {

    @Autowired
    private RedissonClient redissonClient;

    /**
     * 定义切面
     */
    @Pointcut("@annotation(org.yui.redis.annotation.RedisCacheDel) || " +
            "@annotation(org.yui.redis.annotation.RedisCacheDel.List)")
    public void redisCacheDel() {}

    /**
     *
     * @param proceedingJoinPoint
     * @return
     * @throws Throwable
     */
    @Around("redisCacheDel()")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

        Object[] args = proceedingJoinPoint.getArgs();

        Method method = AopUtil.around2Method(proceedingJoinPoint);

        List<RedisCacheDel> redisCacheDelList = new ArrayList<>();
        Optional.ofNullable(method.getAnnotation(RedisCacheDel.List.class)).ifPresent((x) -> {
            redisCacheDelList.addAll(Arrays.asList(x.value()));
        });
        Optional.ofNullable(method.getAnnotation(RedisCacheDel.class)).ifPresent(redisCacheDelList::add);

        RKeys rKeys = redissonClient.getKeys();

        redisCacheDelList.forEach((redisCacheDel) -> {
            String salt = null;
            if (args.length > 0) {
                /**
                 * 用spel解析盐
                 */
                salt = SpelUtil.parse(redisCacheDel.salt(),method,args);
            } else {
                salt = redisCacheDel.salt();
            }

            String keyPattern = redisCacheDel.keyPatternPrefix() + salt;
            log.info("redis删除key的数量:{},pattern:{}",
                    rKeys.deleteByPattern(keyPattern),keyPattern);
        });
        return proceedingJoinPoint.proceed();
    }
}
