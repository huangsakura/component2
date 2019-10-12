package org.yui.openapi.config;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.BeansException;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.yui.base.util.GenericUtil;
import org.yui.openapi.annotation.OpenApi;
import org.yui.openapi.bean.*;

import java.util.*;

/**
 * @author huangjinlong
 * @time 2019-10-11 20:01
 * @description
 */
@Log4j2
@Configuration
public class OpenApiConfig implements ApplicationContextAware {

    private Set<OpenApiInfo> openApiInfoSet;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String,AbstractOpenApi> openApiMap = applicationContext.getBeansOfType(AbstractOpenApi.class);

        Set<OpenApiInfo> openApiInfoSet0 = new HashSet<>();

        openApiMap.forEach((key0,value0) -> {

            Class<? extends AbstractOpenApi> openApiClass = value0.getClass();

            Class<?>[] classes = GenericUtil.getGenericClasses(openApiClass);
            Validate.notNull(classes);
            Validate.isTrue(classes.length == 2);
            Validate.isTrue(AbstractOpenApiRequest.class.isAssignableFrom(classes[0]));
            Validate.isTrue(AbstractOpenApiResponse.class.isAssignableFrom(classes[1]));

            Optional.ofNullable(value0.getClass().getAnnotation(OpenApi.class))
                    .ifPresent(value1 -> {
                        OpenApiInfo openApiInfo = new OpenApiInfo();
                        openApiInfo.setName(value1.value());
                        openApiInfo.setOpenApi(value0);
                        openApiInfo.setRequest((Class<? extends AbstractOpenApiRequest>)classes[0]);
                        openApiInfo.setResponse((Class<? extends AbstractOpenApiResponse>)classes[1]);
                        openApiInfoSet0.add(openApiInfo);
                        log.info("注册{}成为OPENAPI成功",value1.value());
                    });
        });
        openApiInfoSet = Collections.unmodifiableSet(openApiInfoSet0);
    }

    @Bean
    public ServletRegistrationBean openApiServlet() {
        return new ServletRegistrationBean<>(new OpenApiServlet(openApiInfoSet),OpenApiServlet.PATH);
    }
}
