package org.yui.base.enums.entity;

import lombok.Getter;

/**
 *
 */
/**
 * 是 或 否 的枚举，注意与com.yunhuakeji.component.base.enums.YesNoEnum的区别
 */
@Getter
public enum YesNoCodeEnum implements EntityEnum {

    YES("YES","是"),
    NO("NO","否");

    private final String code;
    private final String dbCode;
    private final String message;

    private YesNoCodeEnum(String code, String message) {
        this.code = code;
        this.dbCode = code;
        this.message = message;
    }
}
