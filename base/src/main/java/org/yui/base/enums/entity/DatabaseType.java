package org.yui.base.enums.entity;

import lombok.Getter;

/**
 * 数据库类型
 */
/**
 * @author huangjinlong
 *
 */
@Getter
public enum DatabaseType implements EntityEnum {

    /**
     *
     */
    ORACLE("ORACLE",new String[]{"ORACLE"},"ORACLE数据库"),
    MYSQL("MYSQL",new String[]{"MYSQL","MARIADB"},"MYSQL数据库");

    private final String code;
    private final String[] alias;
    private final String dbCode;
    private final String message;

    private DatabaseType(String code, String[] alias,String message) {
        this.code = code;
        this.alias = alias;
        this.dbCode = code;
        this.message = message;
    }
}
