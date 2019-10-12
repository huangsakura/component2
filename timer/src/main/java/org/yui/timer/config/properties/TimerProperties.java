package org.yui.timer.config.properties;

import org.yui.base.util.RandomUtil;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author huangjinlong
 * @time 2019-03-26 11:21
 * @description
 */
@Configuration
@Getter
@Setter
@ConfigurationProperties(prefix = "yunhua.component.timer")
public class TimerProperties {
    /**
     * 手动触发定时任务，需要传入key
     */
    private String invokeKey = RandomUtil.did();

    /**
     * 线程池大小
     */
    private Integer poolSize = 10;
}
