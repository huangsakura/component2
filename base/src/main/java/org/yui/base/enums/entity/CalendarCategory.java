package org.yui.base.enums.entity;

import lombok.Getter;

/**
 *
 */
@Getter
public enum CalendarCategory implements EntityEnum {

    /**
     *
     */
    GREGORIAN("GREGORIAN","公历"),
    LUNAR("LUNAR","农历");

    private final String code;
    private final String dbCode;
    private final String message;

    private CalendarCategory(String code, String message) {
        this.code = code;
        this.dbCode = code;
        this.message = message;
    }
}
