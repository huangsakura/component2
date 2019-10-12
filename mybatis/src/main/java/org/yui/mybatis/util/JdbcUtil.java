package org.yui.mybatis.util;

import org.yui.base.enums.entity.DatabaseType;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.SQLException;

/**
 * @author huangjinlong
 * 工具类
 */
@Component
public class JdbcUtil implements InitializingBean {

    private static volatile DatabaseType DATABASE_TYPE;

    @Value("${spring.datasource.url}")
    private String url;

    @Override
    public void afterPropertiesSet() throws Exception {
        updateDatabaseType(url);
    }

    /**
     *
     * @param url
     */
    private static void updateDatabaseType(String url) {
        if (null == DATABASE_TYPE) {
            synchronized (JdbcUtil.class) {
                if (null == DATABASE_TYPE) {

                    String urlUpper = url.toUpperCase();
                    if (urlUpper.contains(DatabaseType.MYSQL.getCode())) {
                        DATABASE_TYPE = DatabaseType.MYSQL;
                    } else if (urlUpper.contains(DatabaseType.ORACLE.getCode())) {
                        DATABASE_TYPE = DatabaseType.ORACLE;
                    } else {
                        throw new IllegalArgumentException("不支持的数据库类型");
                    }
                }
            }
        }
    }

    /**
     *
     * @return
     * @throws SQLException
     */
    public static DatabaseType getDatabaseType() {
        return DATABASE_TYPE;
    }
}
