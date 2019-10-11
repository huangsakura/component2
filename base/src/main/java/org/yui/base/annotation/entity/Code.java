package org.yui.base.annotation.entity;

import java.lang.annotation.*;

/**
 * 每一张表，除了必须 给其中一个字段加上 @Id 之外，
 * 还应该给这张表的其他字段加上这个注解，标记 这个字段是这张表的code字段。
 *
 * code字段的作用是替代主键的外键功能，即
 * 假设 A表要关联B表，传统的方式是将B表的主键放到A表中去，但由于引入了code字段，所以
 * A表只需要引入B表的code字段即可
 *
 * //这个注解使用到的地方
 * //com.yunhuakeji.component.mybatis.bean.interceptor.UpdateInterceptor#intercept(org.apache.ibatis.plugin.Invocation)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Documented
public @interface Code {
}
