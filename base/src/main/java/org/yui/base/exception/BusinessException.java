package org.yui.base.exception;

import lombok.Getter;
import org.yui.base.enums.Enums;
import org.yui.base.constant.StringConstant;

/**
 * @author huangjinlong
 * 通用的业务异常
 */
@Getter
public final class BusinessException extends RuntimeException {

    private static final String DEFAULT_CODE = "SYSTEM";

    private static final String DEFAULT_MESSAGE = "程序抛出了异常";


    private final String code;

    private final String message;

    public BusinessException(String code,String message) {
        this(code,message,true);
    }

    public BusinessException(String message) {
        this(DEFAULT_CODE,message,true);
    }

    public BusinessException(Enums enums) {
        this(enums.getCode(),enums.getMessage(),true);
    }

    public BusinessException(Enums enums, boolean writableStackTrace) {
        this(enums.getCode(),enums.getMessage(),writableStackTrace);
    }

    public BusinessException(String code, String message,boolean writableStackTrace) {
        super(message, null, false, writableStackTrace);
        this.message = message;
        this.code = code;
    }


    @Override
    public String toString() {
        return this.code + StringConstant.COLON + this.message;
    }
}
