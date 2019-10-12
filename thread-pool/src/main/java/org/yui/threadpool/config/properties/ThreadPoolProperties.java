package org.yui.threadpool.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

/**
 * @author huangjinlong
 * @time 2019-08-30 10:48
 * @description
 */
@Validated
@Configuration
@Getter
@Setter
@ConfigurationProperties(prefix = "yunhua.component.thread-pool")
public class ThreadPoolProperties {

    /**
     * 核心线程数
     */
    @NotNull
    private Integer corePoolSize = 10;

    /**
     * 队列大小
     */
    @NotNull
    private Integer queueCapacity = 50;

    /**
     * 最大线程池数
     */
    @NotNull
    private Integer maxPoolSize = 100;
}
