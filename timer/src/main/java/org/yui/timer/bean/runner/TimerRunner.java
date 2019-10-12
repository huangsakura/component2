package org.yui.timer.bean.runner;

import org.yui.timer.bean.constant.TimerConstant;
import org.yui.timer.config.properties.TimerProperties;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.yui.base.bean.constant.StringConstant;
import org.yui.timer.bean.ITimer;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Optional;

/**
 * @author huangjinlong
 * @time 2019-05-10 13:28
 * @description
 */
@Component
@Log4j2
public class TimerRunner implements ApplicationRunner {

    @Autowired
    private RequestMappingHandlerMapping requestMappingHandlerMapping;
    @Autowired
    private TimerProperties timerProperties;
    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        Map<String, ITimer> timerTaskMap = applicationContext.getBeansOfType(ITimer.class);

        timerTaskMap.forEach((key,value) -> {

            Class<?> clazz = value.getClass();
            Method goMethod = Optional.of(MethodUtils.getAccessibleMethod(clazz,"go")).get();

            Object object = null;
            try {
                object = MethodUtils.invokeMethod(value,"mapping");
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                log.warn("动态注入mapping失败:{}",e.getMessage());
                return;
            }

            String uri = Optional.of(object).get().toString();
            if (!uri.startsWith(StringConstant.SLASH)) {
                uri = StringConstant.SLASH + uri;
            }
            uri = TimerConstant.TIMER_TASK_URI_PREFIX + uri;

            RequestMappingInfo requestMappingInfo = RequestMappingInfo
                    .paths(uri).methods(RequestMethod.GET).build();

            requestMappingHandlerMapping.registerMapping(requestMappingInfo,key,goMethod);
        });

        log.info("定时任务的手动调用的key:{}",timerProperties.getInvokeKey());
    }
}
