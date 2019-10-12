package org.yui.threadpool.config;

import org.yui.base.util.RandomUtil;
import org.yui.threadpool.config.properties.ThreadPoolProperties;
import lombok.extern.log4j.Log4j2;
import org.slf4j.MDC;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskDecorator;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.yui.base.bean.constant.LogConstant;
import org.yui.threadpool.bean.constant.ThreadPoolConstant;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author huangjinlong
 * @time 2019-01-09 13:19
 * @description
 */
@Log4j2
@EnableAsync
@Configuration
public class ThreadPoolConfig implements AsyncConfigurer  {

    @Autowired
    private ThreadPoolProperties threadPoolProperties;

    @Override
    public Executor getAsyncExecutor() {
        return threadPoolTaskExecutor();
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return (throwable, method, params) -> {
            log.error("方法:{}抛出了异常:{},入参为:{}",
                    method.getDeclaringClass().getName() + "#" + method.getName(),
                    throwable.getMessage(),params);
        };
    }

    /**
     * 线程池配置
     */
    @Bean(name = ThreadPoolConstant.GENERAL_THREAD_POOL,destroyMethod="shutdown")
    public ThreadPoolTaskExecutor threadPoolTaskExecutor() {

        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setTaskDecorator(MdcTaskDecorator.INSTANCE);
        /**
         * 设置核心线程数
         */
        threadPoolTaskExecutor.setCorePoolSize(threadPoolProperties.getCorePoolSize());
        /**
         * 设置队列容量
         */
        threadPoolTaskExecutor.setQueueCapacity(threadPoolProperties.getQueueCapacity());
        /**
         * 设置最大线程数
         */
        threadPoolTaskExecutor.setMaxPoolSize(threadPoolProperties.getMaxPoolSize());
        /**
         * 设置线程活跃时间（秒）
         */
        threadPoolTaskExecutor.setKeepAliveSeconds(300);
        /**
         * 设置默认线程名称
         */
        threadPoolTaskExecutor.setThreadNamePrefix(ThreadPoolConstant.GENERAL_TASK_EXECUTOR_PREFIX);
        /**
         * 设置拒绝策略
         */
        threadPoolTaskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
        /**
         * 等待所有任务结束后再关闭线程池
         */
        threadPoolTaskExecutor.setWaitForTasksToCompleteOnShutdown(true);
        /**
         * 设置线程池中任务的等待时间，如果超过这个时候还没有销毁就强制销毁，以确保应用最后能够被关闭，而不是阻塞住
         */
        threadPoolTaskExecutor.setAwaitTerminationSeconds(0);
        /**
         * 允许核心线程超时
         */
        threadPoolTaskExecutor.setAllowCoreThreadTimeOut(true);
        /**
         * 守护线程
         */
        threadPoolTaskExecutor.setDaemon(true);
        threadPoolTaskExecutor.initialize();
        return threadPoolTaskExecutor;
    }

    /**
     * https://stackoverflow.com/questions/6073019/how-to-use-mdc-with-thread-pools
     */
    private static class MdcTaskDecorator implements TaskDecorator {

        static final MdcTaskDecorator INSTANCE = new MdcTaskDecorator();

        @Override
        public Runnable decorate(Runnable runnable) {
            Map<String, String> contextMap = MDC.getCopyOfContextMap();
            return () -> {
                try {
                    MDC.put(LogConstant.MDC_REQUEST_ID_NAME, RandomUtil.did());
                    runnable.run();
                } finally {
                    MDC.clear();
                    Optional.ofNullable(contextMap).ifPresent(MDC::setContextMap);
                }
            };
        }
    }
}
