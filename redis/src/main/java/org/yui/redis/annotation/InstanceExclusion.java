package org.yui.redis.annotation;

import java.lang.annotation.*;

/**
 * @author huangjinlong
 * @time 2019-06-21 09:32
 * @description
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface InstanceExclusion {

    /**
     * 等待时间，单位毫秒
     * @return
     */
    long waitTime() default 1000;

    /**
     * 失效时间，单位毫秒
     * @return
     */
    long leaseTime() default 2000;

    /**
     *
     * @return
     */
    String key();
}
