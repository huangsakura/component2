package org.yui.aop.util;

import org.yui.base.util.JsonUtil;
import org.yui.base.util.RandomUtil;
import org.yui.logger.annotation.PrintLog;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.Validate;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.MDC;
import org.yui.base.bean.constant.LogConstant;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Map;

/**
 * @author huangjinlong
 * @time 2019-03-24 12:01
 * @description
 */
@Log4j2
public abstract class AopUtil {

    /**
     *
     * @param proceedingJoinPoint
     * @return
     */
    public static Method around2Method(ProceedingJoinPoint proceedingJoinPoint) {
        Signature signature = proceedingJoinPoint.getSignature();
        Validate.isTrue(signature instanceof MethodSignature);

        MethodSignature methodSignature = (MethodSignature) signature;
        return methodSignature.getMethod();
    }

    /**
     *
     * @param proceedingJoinPoint
     * @return
     * @throws Throwable
     */
    public static Object proceedAsync(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

        Object[] args = proceedingJoinPoint.getArgs();

        Map<String, String> contextMap = MDC.getCopyOfContextMap();
        MDC.put(LogConstant.MDC_REQUEST_ID_NAME, RandomUtil.did());

        Method method = around2Method(proceedingJoinPoint);
        Parameter[] parameters = method.getParameters();
        if ((null != args) && (null != parameters)) {
            for (int i = 0;i < parameters.length; i ++) {
                Parameter parameter = parameters[i];
                PrintLog printLog = parameter.getAnnotation(PrintLog.class);

                boolean printLogValue = true;
                if ((null != printLog) && (!printLog.value())) {
                    printLogValue = false;
                }
                if (printLogValue) {
                    log.info("异步参数{}:{}",i, JsonUtil.toJsonStringQuietly(args[i]));
                }
            }
        }

        try {
            return proceedingJoinPoint.proceed();
        } finally {
            MDC.clear();
            if (null != contextMap) {
                MDC.setContextMap(contextMap);
            }
        }
    }
}
