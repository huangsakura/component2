package org.yui.web.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


/**
 * @author huangjinlong
 */
@Configuration
@Getter
@Setter
@ConfigurationProperties(prefix = "yunhua.component.web")
public class WebProperties {
    /**
     * 当前项目的app_key，接口签名时会使用
     */
    private String appKey;

    /**
     * 当前项目的app_secret，接口签名时会使用
     */
    private String appSecret;
}
