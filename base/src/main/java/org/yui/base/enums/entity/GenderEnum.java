package org.yui.base.enums.entity;

import lombok.Getter;

/**
 *
 */
@Getter
public enum GenderEnum implements EntityEnum {

    MALE(GenderCodeEnum.MALE,""),
    FEMALE(GenderCodeEnum.FEMALE,""),
    UNKNOWN(GenderCodeEnum.UNKNOWN,"");

    private final String code;
    private final String dbCode;
    private final String message;

    private GenderEnum(GenderCodeEnum genderCodeEnum, String nationalCode) {
        this.code = genderCodeEnum.getCode();
        this.message = genderCodeEnum.getMessage();
        this.dbCode = nationalCode;
    }
}
