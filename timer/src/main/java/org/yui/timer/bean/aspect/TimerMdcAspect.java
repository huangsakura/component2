package org.yui.timer.bean.aspect;

import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.yui.aop.util.AopUtil;

/**
 * @author huangjinlong
 * @time 2019-05-10 10:03
 * @description
 */
@Log4j2
@Aspect
@Component
public class TimerMdcAspect {

    /**
     * 定义切面
     */
    @Pointcut("@annotation(org.springframework.scheduling.annotation.Scheduled)")
    public void aspect() {}

    /**
     *
     * @param proceedingJoinPoint
     * @return
     * @throws Throwable
     */
    @Around("aspect()")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        return AopUtil.proceedAsync(proceedingJoinPoint);
    }
}
