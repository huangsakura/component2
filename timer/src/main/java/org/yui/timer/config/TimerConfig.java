package org.yui.timer.config;

import org.yui.timer.bean.constant.TimerConstant;
import org.yui.timer.bean.filter.ITimerFilter;
import org.yui.timer.config.properties.TimerProperties;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author huangjinlong
 * @time 2019-01-09 16:54
 * @description
 */
@Log4j2
@EnableScheduling
@Configuration
public class TimerConfig implements SchedulingConfigurer {

    @Autowired
    private TimerProperties timerProperties;

    /**
     * 开启多线程的定时任务，即并行执行定时任务
     * @param scheduledTaskRegistrar
     */
    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
        scheduledTaskRegistrar.setScheduler(threadPoolTaskScheduler());
    }

    /**
     * 定时任务的线程池
     * @return
     */
    @Bean(destroyMethod="shutdown",name = TimerConstant.TIMER_TASK_THREAD_POOL)
    public ThreadPoolTaskScheduler threadPoolTaskScheduler() {

        ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setDaemon(true);
        threadPoolTaskScheduler.setRemoveOnCancelPolicy(true);
        threadPoolTaskScheduler.setThreadNamePrefix(TimerConstant.TIMER_TASK_EXECUTOR_PREFIX);
        threadPoolTaskScheduler.setPoolSize(timerProperties.getPoolSize());
        threadPoolTaskScheduler.setWaitForTasksToCompleteOnShutdown(true);
        threadPoolTaskScheduler.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
        threadPoolTaskScheduler.initialize();
        return threadPoolTaskScheduler;
    }

    @Bean
    public FilterRegistrationBean iTimerFilter() {
        FilterRegistrationBean<ITimerFilter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new ITimerFilter(timerProperties));
        filterRegistrationBean.setOrder(90);
        filterRegistrationBean.addUrlPatterns(TimerConstant.TIMER_TASK_URI_PREFIX + "/*");
        filterRegistrationBean.setName("iTimerFilter");
        return filterRegistrationBean;
    }
}
