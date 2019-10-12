package org.yui.web.config;

import org.yui.base.util.JsonUtil;
import org.yui.spring.util.EnvUtil;
import org.yui.web.bean.formatter.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeansException;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.yui.base.bean.constant.StringConstant;
import org.yui.web.bean.formatter.*;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author huangjinlong
 * 开启定时任务
 * 开启多线程
 */
@Order(value = Integer.MIN_VALUE)
@Log4j2
@Configuration
public class WebConfig implements WebMvcConfigurer,ApplicationContextAware {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resource/**")
                .addResourceLocations("classpath:/resource/");
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addFormatter(LocalDateTimeFormatter.INSTANCE);
        registry.addFormatter(LocalDateFormatter.INSTANCE);
        registry.addFormatter(LocalTimeFormatter.INSTANCE);
        registry.addFormatter(YearFormatter.INSTANCE);
        registry.addFormatter(MonthDayFormatter.INSTANCE);
        registry.addFormatter(YearMonthFormatter.INSTANCE);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

        ObjectMapper objectMapper = applicationContext.getBean(ObjectMapper.class);
        JsonUtil.userDefineObjectMapper(objectMapper,true);
    }
    /**
     * 解决  生成的json字符串 汉字是问号的问题
     * @return
     */
    @Bean
    public HttpMessageConverter<String> stringHttpMessageConverter() {
        return new StringHttpMessageConverter(StandardCharsets.UTF_8);
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(stringHttpMessageConverter());
    }

    @Bean
    public HiddenHttpMethodFilter hiddenHttpMethodFilter() {
        return new HiddenHttpMethodFilter();
    }


    /**
     *
     * dev和test环境允许跨域
     * 这个过滤器一定要排在第一
     * @return
     */
    @Conditional(value = {EnvUtil.DevOrTestCondition.class})
    @Bean
    public FilterRegistrationBean corsFilter() {
        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource =
                new UrlBasedCorsConfigurationSource();

        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin(StringConstant.ASTERISK);
        corsConfiguration.addAllowedHeader(StringConstant.ASTERISK);
        corsConfiguration.addAllowedMethod(StringConstant.ASTERISK);
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);

        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setOrder(10);
        filterRegistrationBean.setFilter(new CorsFilter(urlBasedCorsConfigurationSource));
        filterRegistrationBean.setName("corsFilter");
        filterRegistrationBean.addUrlPatterns("/*");
        return filterRegistrationBean;
    }


}
