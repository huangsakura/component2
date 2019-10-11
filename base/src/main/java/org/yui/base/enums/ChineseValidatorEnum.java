package org.yui.base.enums;

import lombok.Getter;

/**
 * 汉字校验的枚举
 */
@Getter
public enum ChineseValidatorEnum implements Enums {

    /**
     *
     */
    ALL_CHINESE("ALL_CHINESE","必须全是汉字"),
    CONTAIN_CHINESE("CONTAIN_CHINESE","允许包含汉字"),
    NO_CHINESE("NO_CHINESE","不包含任何汉字");

    private final String code;
    private final String message;

    private ChineseValidatorEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
