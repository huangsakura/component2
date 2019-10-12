package org.yui.redis.bean.aspect;

import org.yui.aop.util.AopUtil;
import org.yui.base.exception.BusinessException;
import org.yui.redis.annotation.InstanceExclusion;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * @author huangjinlong
 * @time 2019-06-21 09:34
 * @description
 */
@Log4j2
@Aspect
@Component
public class InstanceExclusionAspect {

    @Autowired
    private RedissonClient redissonClient;

    /**
     * 定义切面
     */
    @Pointcut("@annotation(org.yui.redis.annotation.InstanceExclusion)")
    public void redisRokku() {}


    /**
     * around切面
     * @param proceedingJoinPoint
     * @return
     * @throws Throwable
     */
    @Around("redisRokku()")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

        Method method = AopUtil.around2Method(proceedingJoinPoint);

        InstanceExclusion instanceExclusion = method.getAnnotation(InstanceExclusion.class);
        if (null == instanceExclusion) {
            throw new BusinessException("NOT_EXIST_ANNOTATION","不存在InstanceExclusion注解",false);
        }

        /**
         * 方法路径
         */
        RLock rLock = redissonClient.getLock(instanceExclusion.key());
        try {
            if (rLock.tryLock(instanceExclusion.waitTime(),instanceExclusion.leaseTime(),TimeUnit.MILLISECONDS)) {
                return proceedingJoinPoint.proceed();
            }
        } catch (InterruptedException e) {
            String methodPath = method.getDeclaringClass().getName() + "#" + method.getName();
            log.error("获取实例排他redis锁出错:{},方法路径为:{}",e.getMessage(),methodPath);
        }
        return null;
    }

    public static void main(String[] args) throws NoSuchMethodException, SecurityException {
        Method method = InstanceExclusionAspect.class.getMethod("around",ProceedingJoinPoint.class);
        System.out.println(method.getDeclaringClass().getName());
        System.out.println(method.getName());
    }
}
