package org.yui.base.annotation.doc;

import org.yui.base.bean.constant.StringConstant;

import java.lang.annotation.*;

/**
 *
 * @author huangjinlong
 * 自定义的文档注解，用于替代 swagger2原生的注解。
 *
 *
 * swagger2原生的注解 有以下几个问题：
 * 1、注解的参数太多，搞不清楚具体含义是啥；
 * 2、经过测试发现，入参注解和出参注解不一样，不方便使用。
 * 3、不方便框架自定义参数。
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD,ElementType.PARAMETER})
@Documented
public @interface ApiField {

    /**
     * 字段的汉语描述
     * @return
     */
    String desc();

    /**
     * 默认值
     * 这个参数只对入参生效
     *
     * @return
     */
    String defaultValue() default StringConstant.BLANK;

    /**
     * 示例值
     * 这个参数只对出参生效
     * @return
     */
    String example() default StringConstant.BLANK;

    /**
     * 可能的参数，逗号分隔
     *
     * 如果字段的类型是枚举，则不需要专门设置这个值，框架会自动解析枚举的code和message
     */
    String allowableValues() default StringConstant.BLANK;
}
