package org.yui.redis.annotation;

import org.hibernate.validator.constraints.NotBlank;

import java.lang.annotation.*;

/**
 * @author huangjinlong
 * @time 2019-03-24 11:26
 * @description
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Repeatable(value = RedisCacheDel.List.class)
public @interface RedisCacheDel {

    /**
     * key pattern的前缀
     * @return
     */
    @NotBlank
    String keyPatternPrefix();

    /**
     * 盐
     * spel表达式
     * @return
     */
    @NotBlank
    String salt();


    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.METHOD})
    @Documented
    public @interface List {
        RedisCacheDel[] value();
    }
}
