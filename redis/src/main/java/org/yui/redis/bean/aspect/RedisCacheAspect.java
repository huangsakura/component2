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
import org.yui.redis.annotation.RedisCache;

import java.lang.reflect.Method;
import java.util.Optional;

/**
 * @author huangjinlong
 * @time 2019-03-24 11:44
 * @description
 */
@Log4j2
@Aspect
@Component
public class RedisCacheAspect {

    @Autowired
    private RedissonClient redissonClient;

    /**
     * 定义切面
     */
    @Pointcut("@annotation(org.yui.redis.annotation.RedisCache)")
    public void redisCache() {}

    /**
     *
     * @param proceedingJoinPoint
     * @return
     * @throws Throwable
     */
    @Around("redisCache()")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

        Object[] args = proceedingJoinPoint.getArgs();

        Method method = AopUtil.around2Method(proceedingJoinPoint);

        RedisCache redisCache = Optional.of(method.getAnnotation(RedisCache.class)).get();

        String salt = null;
        if (args.length > 0) {
            /**
             * 用spel解析盐
             */
            salt = SpelUtil.parse(redisCache.salt(),method,args);
        } else {
            salt = redisCache.salt();
        }

        String key = redisCache.keyPrefix() + salt;
        RBucket<Object> rBucket = redissonClient.getBucket(key);

        Object result = rBucket.get();
        if (null == result) {
            result = proceedingJoinPoint.proceed();
            if (redisCache.allowNull() || (null != result)) {
                rBucket.set(result,redisCache.ttl(), redisCache.timeUnit());
                log.info("结果放入redis成功,key:{}",key);
            }
        } else {
            log.info("命中redis缓存,key:{}",key);
        }
        return result;
    }
}
