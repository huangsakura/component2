package org.yui.base.enums;

import lombok.Getter;

/**
 * 标记api接口的状态，会显示到swagger的页面上
 */
@Getter
public enum ApiStateEnum implements Enums {

    /**
     *
     */
    NOT_AVAILABLE("NOT_AVAILABLE","不可用"),
    DEFINED_PARAMETER("DEFINED_PARAMETER","已定义入参出参结构,但未实现逻辑"),
    IMPLEMENTED("IMPLEMENTED","已实现逻辑,APP和前端请调用"),
    INTEGRATED_TESTED("INTEGRATED_TESTED","已完成联调并成功"),
    TO_BE_ABANDONED("TO_BE_ABANDONED","即将被弃用"),
    DEAD("DEAD","已弃用");

    private final String code;
    private final String message;

    private ApiStateEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
