package org.yui.redis.annotation;

import org.yui.redis.bean.constant.RedisConstant;
import org.springframework.transaction.annotation.Transactional;
import org.yui.base.bean.constant.StringConstant;
import org.yui.base.bean.entity.Entitys;

import java.lang.annotation.*;

/**
 * @author huangjinlong
 * @time 2019-03-06 17:06
 * @description
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
@Transactional(rollbackFor = {RuntimeException.class})
@Repeatable(value = RedisRokku.List.class)
public @interface RedisRokku {

    /**
     * 和 fixedPrefix 性质一样，只不过更友好而已
     * @return
     */
    Class<? extends Entitys> entity() default Entitys.class;

    /**
     * 支持 spel 表达式
     * 盐
     * @return
     */
    String salt() default "'0'";

    /**
     * redis key的固定的前缀
     */
    String fixedPrefix() default StringConstant.BLANK;

    /**
     * 获取锁，等待时间。单位秒
     * @return
     */
    long waitTime() default RedisConstant.DEFAULT_WAIT_TIME;

    /**
     * 锁的自动释放时间，单位秒
     * @return
     */
    long leaseTime() default RedisConstant.DEFAULT_LEASE_TIME;

    /**
     * 允许一个方法上同时放多个同样的注解
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.METHOD})
    @Documented
    @Transactional(rollbackFor = {RuntimeException.class})
    public @interface List {
        RedisRokku[] value();
    }
}
