package org.yui.base.enums.entity;

import lombok.Getter;

/**
 *
 */
/**
 * 状态枚举
 */
@Getter
public enum StateEnum implements EntityEnum {

    VALID("VALID","VALID","有效"),
    INVALID("INVALID","INVALID","无效");

    private final String code;
    private final String dbCode;
    private final String message;

    private StateEnum(String code, String dbCode, String message) {
        this.code = code;
        this.message = message;
        this.dbCode = dbCode;
    }
}
