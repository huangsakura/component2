package org.yui.base.annotation.doc;

import java.lang.annotation.*;

/**
 * 用于替代 swagger的@Api 注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
public @interface ApiGroup {

    String value();
}
