package org.yui.mybatis.bean;

import org.yui.base.bean.BinaryField;
import org.yui.base.util.ClassUtil;
import org.yui.mybatis.config.properties.CustomMybatisProperties;
import lombok.extern.log4j.Log4j2;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.yui.base.enums.entity.*;
import org.yui.mybatis.bean.handler.BinaryFieldHandler;
import org.yui.mybatis.bean.handler.EntityEnumHandler;
import org.yui.mybatis.bean.handler.MonthDayHandler;

import java.time.MonthDay;
import java.util.Set;

/**
 * 将自定义类的TypeHandler注册到mybatis
 */
@Log4j2
@Component
public final class CustomTypeHandlerParser implements ApplicationContextAware {

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

        CustomMybatisProperties customMybatisProperties = applicationContext.getBean(CustomMybatisProperties.class);

        SqlSessionFactory sqlSessionFactory = applicationContext.getBean(SqlSessionFactory.class);
        TypeHandlerRegistry typeHandlerRegistry = sqlSessionFactory.getConfiguration().getTypeHandlerRegistry();

        /**
         * base项目里面的枚举
         */
        typeHandlerRegistry.register(DatabaseType.class, EntityEnumHandler.class);
        typeHandlerRegistry.register(StateEnum.class, EntityEnumHandler.class);
        typeHandlerRegistry.register(YesNoCodeEnum.class, EntityEnumHandler.class);
        typeHandlerRegistry.register(OnOffEnum.class, EntityEnumHandler.class);
        typeHandlerRegistry.register(EnglishAlphabetEnum.class, EntityEnumHandler.class);
        typeHandlerRegistry.register(EnglishAlphabetExtendEnum.class, EntityEnumHandler.class);
        typeHandlerRegistry.register(FileExtensionEnum.class, EntityEnumHandler.class);
        typeHandlerRegistry.register(GenderCodeEnum.class, EntityEnumHandler.class);
        typeHandlerRegistry.register(GenderEnum.class, EntityEnumHandler.class);
        typeHandlerRegistry.register(ClientCategory.class, EntityEnumHandler.class);

        /**
         * java8
         */
        typeHandlerRegistry.register(MonthDay.class, MonthDayHandler.INSTANCE);

        if (!CollectionUtils.isEmpty(customMybatisProperties.getBinaryClassPackages())) {

            for (String x : customMybatisProperties.getBinaryClassPackages()) {
                Set<Class<?>> classSet = ClassUtil.listClass(x);
                if (null != classSet) {
                    for (Class clazz : classSet) {
                        if (BinaryField.class.isAssignableFrom(clazz)) {
                            typeHandlerRegistry.register(clazz, new BinaryFieldHandler(clazz));
                        }
                    }
                }
            }
        }
    }
}
