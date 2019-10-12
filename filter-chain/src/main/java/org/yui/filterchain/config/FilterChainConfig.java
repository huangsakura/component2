package org.yui.filterchain.config;

import org.yui.filterchain.bean.filter.*;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author huangjinlong
 * @time 2019-10-11 17:23
 * @description
 */
@Configuration
public class FilterChainConfig {
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
        filterRegistrationBean.setFilter(new PrintParameterFilter());
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
    public FilterRegistrationBean urlDecodeBodyFilter() {
        FilterRegistrationBean<UrlDecodeBodyFilter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new UrlDecodeBodyFilter());
        filterRegistrationBean.setOrder(40);
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.setName("urlDecodeBodyFilter");
        return filterRegistrationBean;
    }

    /**
     *
     * @return
     */
    @Bean
    public FilterRegistrationBean trimParameterFilter() {
        FilterRegistrationBean<TrimParameterFilter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new TrimParameterFilter());
        filterRegistrationBean.setOrder(50);
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.setName("trimParameterFilter");
        return filterRegistrationBean;
    }

    /**
     *
     * @return
     */
    @Bean
    public FilterRegistrationBean httpMethodCheckFilter() {
        FilterRegistrationBean<HttpMethodCheckFilter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new HttpMethodCheckFilter());
        filterRegistrationBean.setOrder(60);
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.setName("httpMethodCheckFilter");
        return filterRegistrationBean;
    }

    @Bean
    public FilterRegistrationBean xssFilter() {
        FilterRegistrationBean<XssFilter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new XssFilter());
        filterRegistrationBean.setOrder(80);
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.setName("xssFilter");
        return filterRegistrationBean;
    }
}
