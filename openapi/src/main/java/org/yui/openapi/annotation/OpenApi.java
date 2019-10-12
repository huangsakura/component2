package org.yui.openapi.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * @author huangjinlong
 * @time 2019-10-11 20:05
 * @description
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Component
public @interface OpenApi {

    String value();
}
