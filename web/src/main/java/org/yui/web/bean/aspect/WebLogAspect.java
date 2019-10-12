package org.yui.web.bean.aspect;

import org.yui.aop.util.AopUtil;
import org.yui.base.util.JsonUtil;
import org.yui.logger.annotation.PrintLog;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @author huangjinlong
 */
@Log4j2
@Aspect
@Component
public class WebLogAspect {
    /**
     * 定义切面
     */
    @Pointcut("execution(public * org.yui..*.controller..*.*(..))")
    public void logger() {}

    /**
     *
     * @param proceedingJoinPoint
     * @return
     * @throws Throwable
     */
    @Around("logger()")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

        Method method = AopUtil.around2Method(proceedingJoinPoint);

        Object result = proceedingJoinPoint.proceed();

        boolean isPrintLog = true;
        PrintLog printLog = method.getAnnotation(PrintLog.class);
        if (printLog != null) {
            isPrintLog = printLog.value();
        }
        if (isPrintLog) {
            log.info("出参:"+ JsonUtil.toJsonStringQuietly(result));
        } else {
            log.info("已关闭打印出参");
        }
        return result;
    }
}