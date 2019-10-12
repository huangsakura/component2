package org.yui.mybatis.bean;

import com.alibaba.fastjson.JSON;
import org.yui.mybatis.config.properties.CustomMybatisProperties;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.Validate;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.yui.base.bean.constant.StringConstant;

import java.util.Properties;

/**
 * @author huangjinlong
 * @time 2019-09-20 16:18
 * @description
 */
@Component
@Log4j2
@Intercepts({@Signature(type = Executor.class, method = "query", args = { MappedStatement.class, Object.class,
        RowBounds.class, ResultHandler.class , CacheKey.class, BoundSql.class}) })
public class QueryStatisticsInterceptor implements Interceptor, InitializingBean {

    @Autowired
    private CustomMybatisProperties customMybatisProperties;

    private long limit;

    @Override
    public void afterPropertiesSet() throws Exception {
        limit = customMybatisProperties.getSlowSqlLimitSecond() * 1000;
        Validate.isTrue(limit > 0);
    }

    @Override
    public Object intercept(Invocation invocation) throws Throwable {

        long start = System.currentTimeMillis();
        Object result = invocation.proceed();
        long end = System.currentTimeMillis();

        long time = end - start;
        if (time > limit) {
            Object[] objects = invocation.getArgs();
            BoundSql boundSql = (BoundSql) objects[5];
            Object object = boundSql.getParameterObject();
            String parameterText = StringConstant.BLANK;
            if (null != object) {
                try {
                    parameterText = JSON.toJSONString(object);
                } catch (Exception e) {
                    parameterText = object.toString();
                }
            }
            /**
             * "慢sql"一定要和 com.yunhuakeji.component.logger.plugin.SlowSqlPlugin
             * 保持一致
             *
             * 一定是warn级别
             */
            log.warn("慢sql:{},执行时长:{}毫秒,参数列表:{}", boundSql.getSql(),time,parameterText);
        }
        return result;
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
    }
}
