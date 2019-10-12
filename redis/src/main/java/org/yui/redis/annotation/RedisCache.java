package org.yui.redis.annotation;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Min;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * @author huangjinlong
 * @time 2019-03-21 09:13
 * @description
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface RedisCache {

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
    String salt() default "'0'";

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
}
