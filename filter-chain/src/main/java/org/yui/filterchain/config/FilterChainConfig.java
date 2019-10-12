package org.yui.filterchain.config;

import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.yui.filterchain.bean.filter.*;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.yui.filterchain.config.properties.FilterChainProperties;

/**
 * @author huangjinlong
 * @time 2019-10-11 17:23
 * @description
 */
@Configuration
public class FilterChainConfig {

    @Autowired
    private RedissonClient redissonClient;
    @Autowired
    private Environment environment;
    @Autowired
    private FilterChainProperties filterChainProperties;
    /**
     *
     * @return
     */
    @Bean
    public FilterRegistrationBean mdcFilter() {
        FilterRegistrationBean<MdcFilter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new MdcFilter());
        filterRegistrationBean.setOrder(20);
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.setName("mdcFilter");
        return filterRegistrationBean;
    }

    /**
     *
     * @return
     */
    @Bean
    public FilterRegistrationBean printParameterFilter() {
        FilterRegistrationBean<PrintParameterFilter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new PrintParameterFilter(redissonClient));
        filterRegistrationBean.setOrder(30);
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.setName("printParameterFilter");
        return filterRegistrationBean;
    }

    /**
     *
     * @return
     */
    @Bean
    public FilterRegistrationBean checkApiSignFilter() {
        FilterRegistrationBean<CheckApiSignFilter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new CheckApiSignFilter(redissonClient,filterChainProperties,environment));
        filterRegistrationBean.setOrder(40);
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.setName("checkApiSignFilter");
        return filterRegistrationBean;
    }

    /**
     * 
     * @return
     */
    @Bean
    public FilterRegistrationBean decryptionFilter() {
        FilterRegistrationBean<DecryptionFilter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new DecryptionFilter());
        filterRegistrationBean.setOrder(50);
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.setName("decryptionFilter");
        return filterRegistrationBean;
    }

    /**
     *
     * @return
     */
    @Bean
    public FilterRegistrationBean urlDecodeBodyFilter() {
        FilterRegistrationBean<UrlDecodeBodyFilter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new UrlDecodeBodyFilter());
        filterRegistrationBean.setOrder(60);
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.setName("urlDecodeBodyFilter");
        return filterRegistrationBean;
    }

    @Bean
    public FilterRegistrationBean xssFilter() {
        FilterRegistrationBean<XssFilter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new XssFilter());
        filterRegistrationBean.setOrder(70);
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.setName("xssFilter");
        return filterRegistrationBean;
    }
}
