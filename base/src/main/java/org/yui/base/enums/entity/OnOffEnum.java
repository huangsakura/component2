package org.yui.base.enums.entity;

import lombok.Getter;

/**
 * @author huangjinlong
 */
@Getter
public enum OnOffEnum implements EntityEnum {

    /**
     *
     */
    ON("ON","启用"),
    OFF("OFF","禁用");

    private final String code;
    private final String dbCode;
    private final String message;

    private OnOffEnum(String code, String message) {
        this.code = code;
        this.dbCode = code;
        this.message = message;
    }

    /**
     *
     * @param code
     * @return
     */
    public static OnOffEnum codeOf(String code) {
        for (OnOffEnum onOffEnum : OnOffEnum.values()) {
            if (onOffEnum.getCode().equals(code)) {
                return onOffEnum;
            }
        }
        return null;
    }
}
