package org.yui.base.enums;

import lombok.Getter;

/**
 * @author huangjinlong
 * http校验枚举
 */
@Getter
public enum HttpValidatorEnum implements Enums {

    /**
     *
     */
    ONLY_VALIDATE_HTTP("ONLY_VALIDATE_HTTP","只校验HTTP"),
    ONLY_VALIDATE_HTTPS("ONLY_VALIDATE_HTTPS","只校验HTTPS"),
    HTTP_OR_HTTPS("HTTP_OR_HTTPS","校验HTTP或HTTPS");

    private final String code;
    private final String message;

    private HttpValidatorEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
