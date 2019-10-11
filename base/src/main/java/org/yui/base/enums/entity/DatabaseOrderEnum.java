package org.yui.base.enums.entity;

import lombok.Getter;

/**
 * @author huangjinlong
 * @time 2018-11-20 11:42
 * @description
 */
/**
 *
 */
@Getter
public enum DatabaseOrderEnum implements EntityEnum {

    /**
     *
     */
    ASC("ASC","正序"),
    DESC("DESC","倒序");

    private final String code;
    private final String dbCode;
    private final String message;

    private DatabaseOrderEnum(String code, String message) {
        this.code = code;
        this.dbCode = code;
        this.message = message;
    }
}
