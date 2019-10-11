package org.yui.base.enums.entity;

import lombok.Getter;

import javax.annotation.Nullable;

/**
 * @author huangjinlong
 */
@Getter
public enum ClientCategory implements EntityEnum {

    /**
     *
     */
    ANDROID("ANDROID","安卓",true,true),
    IOS("IOS","苹果",true,true),
    PC("PC","PC",false,false),
    H5("H5","H5",false,false),
    TENCENT_LITTLE_PROGRAM("TENCENT_LITTLE_PROGRAM","腾讯小程序",false,false);

    private final String code;
    private final String dbCode;
    private final String message;
    private final boolean pushable;
    private final boolean needAppVersion;

    private ClientCategory(String code, String message,boolean pushable,boolean needAppVersion) {
        this.code = code;
        this.dbCode = code;
        this.message = message;
        this.pushable = pushable;
        this.needAppVersion = needAppVersion;
    }

    /**
     *
     * @param code
     * @return
     */
    @Nullable
    public static ClientCategory of(String code) {
        for (ClientCategory clientCategory : values()) {
            if (clientCategory.getCode().equals(code)) {
                return clientCategory;
            }
        }
        return null;
    }
}
