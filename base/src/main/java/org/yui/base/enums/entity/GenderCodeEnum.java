package org.yui.base.enums.entity;

import lombok.Getter;
import org.yui.base.exception.BusinessException;

import javax.annotation.Nullable;

/**
 *
 */
@Getter
public enum GenderCodeEnum implements EntityEnum {

    MALE("MALE", "男"),
    FEMALE("FEMALE", "女"),
    UNKNOWN("UNKNOWN", "未知");

    private final String code;
    private final String dbCode;
    private final String message;

    private GenderCodeEnum(String code, String message) {
        this.code = code;
        this.message = message;
        this.dbCode = code;
    }

    /**
     * 根据message取出性别
     *
     * @param message
     * @return
     */
    @Nullable
    public static GenderCodeEnum parseMessage(String message) {
        for (GenderCodeEnum genderCodeEnum : values()) {
            if (genderCodeEnum.getMessage().equals(message)) {
                return genderCodeEnum;
            }
        }
        return null;
    }

    /**
     * 根据Code取出性别
     *
     * @return
     */
    @Nullable
    public static GenderCodeEnum getGenderCodeByCode(String code) {
        if (null == code) {
            throw new BusinessException("GENDER_TEXT_CANNOT_BE_NULL", "性别不能为空", false);
        }
        for (GenderCodeEnum genderCodeEnum : values()) {
            if (code.contains(genderCodeEnum.getCode())) {
                return genderCodeEnum;
            }
        }
        return null;
    }

    /**
     * 根据message取出性别，模糊
     *
     * @param message
     * @return
     */
    @Nullable
    public static GenderCodeEnum parseMessageVaguely(String message) {
        if (null == message) {
            throw new BusinessException("GENDER_TEXT_CANNOT_BE_NULL", "性别不能为空", false);
        }
        for (GenderCodeEnum genderCodeEnum : values()) {
            if (message.contains(genderCodeEnum.getMessage())) {
                return genderCodeEnum;
            }
        }
        return null;
    }


    public static void main(String[] args) {

        GenderCodeEnum genderCodeEnum = GenderCodeEnum.parseMessageVaguely("男性");
        System.out.println(genderCodeEnum);
    }
}
