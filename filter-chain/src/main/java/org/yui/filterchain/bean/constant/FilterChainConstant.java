package org.yui.filterchain.bean.constant;

import com.google.common.collect.Sets;

import java.util.Collections;
import java.util.Set;

/**
 * @author huangjinlong
 * @time 2019-10-11 17:57
 * @description
 */
public interface FilterChainConstant {

    String SIGN = "sign";

    String TIMESTAMP = "timestamp";

    String NONCE = "nonce";

    String APP_KEY = "appKey";

    String APP_SECRET = "appSecret";

    /**
     * 关联app_key和app_secret
     */
    String ABUTMENT_APP_SECRET_REDIS_KEY = "ABUTMENT_APP_SECRET";

    Set<String> BYPASS_URI_OF_FILTER = Collections.unmodifiableSet(Sets.newHashSet(
            "/**/swagger-ui.html",
            "/**/swagger-resources",
            "/**/v2/**",
            "/**/swagger-resources/**",
            "/**/webjars/**",
            "/**/favicon.ico",
            "/**/timer/**",
            "/**/druid/**",
            "/**/file/upload"
    ));
}
