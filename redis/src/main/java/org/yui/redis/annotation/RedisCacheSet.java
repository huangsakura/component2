package org.yui.redis.annotation;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Min;
import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * @author huangjinlong
 * @time 2019-03-24 11:26
 * @description
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface RedisCacheSet {

    /**
     * key的前缀
     * @return
     */
    @NotBlank
    String keyPrefix();

    /**
     * 盐
     * spel表达式
     * @return
     */
    @NotBlank
    String salt();

    /**
     * @return
     */
    @Min(1)
    long ttl();

    /**
     * 时间单位
     * @return
     */
    TimeUnit timeUnit() default TimeUnit.SECONDS;

    /**
     * 结果为null时，是否放入redis
     * @return
     */
    boolean allowNull() default false;


    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.METHOD})
    @Documented
    public @interface List {
        RedisCacheSet[] value();
    }
}
