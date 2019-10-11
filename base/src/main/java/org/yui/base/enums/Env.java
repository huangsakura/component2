package org.yui.base.enums;

import lombok.Getter;

/**
 * @author huangjinlong
 */
@Getter
public enum Env implements Enums {

    /**
     *
     */
    DEV("DEV","开发环境"),
    TEST("TEST","测试环境"),
    SHOW("SHOW","演示环境"),
    ONLINE("ONLINE","线上环境");

    private final String code;
    private final String lowerCode;
    private final String message;

    private Env(String code,String message) {
        this.code = code;
        this.lowerCode = code.toLowerCase();
        this.message = message;
    }
}
