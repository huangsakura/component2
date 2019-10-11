package org.yui.base.annotation.doc;

import org.yui.base.enums.ApiStateEnum;

import java.lang.annotation.*;

/**
 * api的信息，用于替代 swagger自带的@ApiOperation注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface ApiInfo {

    /**
     * API的名称
     * @return
     */
    String value();

    /**
     * 当前API的状态
     * @return
     */
    ApiStateEnum state() default ApiStateEnum.NOT_AVAILABLE;
}
