package org.yui.filterchain.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Set;

/**
 * @author huangjinlong
 * @time 2019-10-11 17:59
 * @description
 */
@Configuration
@Getter
@Setter
@ConfigurationProperties(prefix = "yunhua.component.filter-chain")
public class FilterChainProperties {

    /**
     * 不校验签名的uri pattern，适用ant匹配规则
     */
    private Set<String> noSignPatterns;
}
