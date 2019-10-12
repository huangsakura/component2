package org.yui.mybatis.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import com.alibaba.druid.spring.boot.autoconfigure.properties.DruidStatProperties;
import com.alibaba.druid.spring.boot.autoconfigure.stat.DruidFilterConfiguration;
import com.alibaba.druid.spring.boot.autoconfigure.stat.DruidSpringAopConfiguration;
import com.alibaba.druid.spring.boot.autoconfigure.stat.DruidStatViewServletConfiguration;
import com.alibaba.druid.spring.boot.autoconfigure.stat.DruidWebStatFilterConfiguration;
import com.alibaba.druid.util.JdbcConstants;
import org.yui.mybatis.config.properties.CustomMybatisProperties;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.yui.base.enums.entity.DatabaseType;
import org.yui.base.exception.BusinessException;

import javax.annotation.Resource;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @author huangjinlong
 * @time 2019-03-05 15:40
 * @description
 */
@Log4j2
@Configuration
@ConditionalOnClass(DruidDataSource.class)
@AutoConfigureBefore(DruidDataSourceAutoConfigure.class)
@EnableConfigurationProperties({DruidStatProperties.class, DataSourceProperties.class})
@Import({DruidSpringAopConfiguration.class,
        DruidStatViewServletConfiguration.class,
        DruidWebStatFilterConfiguration.class,
        DruidFilterConfiguration.class})
public class CustomMybatisConfig implements ApplicationContextAware  {

    @Resource
    private CustomMybatisProperties customMybatisProperties;

    private DataSourceProperties dataSourceProperties;

    public static final String DRUID_DATA_SOURCE_BEAN_NAME = "druidDataSource";

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        dataSourceProperties = applicationContext.getBean(DataSourceProperties.class);
        dataSourceProperties.setType(DruidDataSource.class);
    }

    /**
     *
     * @return
     */
    @Bean(name = DRUID_DATA_SOURCE_BEAN_NAME,destroyMethod = "close")
    public DruidDataSource druidDataSource() throws SQLException {
        /**
         * 启用非公平锁
         */
        DruidDataSource druidDataSource = new DruidDataSource(true);

        druidDataSource.setUsername(dataSourceProperties.getUsername());
        druidDataSource.setPassword(dataSourceProperties.getPassword());
        druidDataSource.setUrl(dataSourceProperties.getUrl());
        druidDataSource.setDriverClassName(dataSourceProperties.getDriverClassName());
        druidDataSource.setInitialSize(customMybatisProperties.getInitSize());
        druidDataSource.setMaxActive(customMybatisProperties.getMaxActive());
        druidDataSource.setMaxWait(customMybatisProperties.getMaxWait());
        /**
         * 执行超时的秒数
         */
        druidDataSource.setQueryTimeout(5);
        druidDataSource.setTransactionQueryTimeout(5);
        /**
         * 在小于minIdle连接数的时候执行保活操作，防止防火墙断开连接
         */
        druidDataSource.setKeepAlive(true);
        /**
         * 打开PSCache，并且指定每个连接上PSCache的大小
         */
        druidDataSource.setPoolPreparedStatements(true);
        druidDataSource.setMaxPoolPreparedStatementPerConnectionSize(30);

        druidDataSource.setMinIdle(customMybatisProperties.getMinIdle());

        /**
         * 参考 https://github.com/alibaba/druid/wiki/%E5%B8%B8%E8%A7%81%E9%97%AE%E9%A2%98 33
         */
        druidDataSource.setTestOnBorrow(false);
        druidDataSource.setTestOnReturn(false);
        druidDataSource.setTestWhileIdle(true);
        /**
         * 配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒
         */
        druidDataSource.setTimeBetweenEvictionRunsMillis(300000L);
        /**
         * 配置一个连接在池中最小生存的时间，单位是毫秒
         */
        druidDataSource.setMinEvictableIdleTimeMillis(3600000L);
        if (customMybatisProperties.getLeakDetection()) {
            druidDataSource.setRemoveAbandoned(true);
            /**
             * 30分钟
             */
            druidDataSource.setRemoveAbandonedTimeout(1800);
            druidDataSource.setLogAbandoned(true);
        }

        /**
         * 记录慢sql
         */
        Properties properties = new Properties();
        properties.put("druid.stat.slowSqlMillis",2000);
        properties.put("druid.stat.logSlowSql",true);
        druidDataSource.setConnectProperties(properties);

        String urlUpper = dataSourceProperties.getUrl().toUpperCase();
        if (urlUpper.contains(DatabaseType.MYSQL.getCode())) {
            druidDataSource.setOracle(false);
            druidDataSource.setValidationQuery("SELECT 1");
            druidDataSource.setDbType(JdbcConstants.MYSQL);
        } else if (urlUpper.contains(DatabaseType.ORACLE.getCode())) {
            druidDataSource.setOracle(true);
            druidDataSource.setValidationQuery("SELECT 1 FROM dual");
            druidDataSource.setDbType(JdbcConstants.ORACLE);
        } else {
            throw new BusinessException("UNSUPPORTED_DATABASE","不支持的数据库类型",false);
        }
        druidDataSource.setValidationQueryTimeout(1);
        /**
         * 设置过滤器
         */
        druidDataSource.setFilters("stat,log4j2,wall,mergeStat");
        /**
         * 初始化
         */
        druidDataSource.init();
        return druidDataSource;
    }
}
