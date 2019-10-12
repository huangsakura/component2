package org.yui.aop.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @author huangjinlong
 * @time 2019-09-24 09:36
 * @description
 */
@Configuration
@EnableAspectJAutoProxy(exposeProxy = true,proxyTargetClass = true)
public class AopConfig {
}
