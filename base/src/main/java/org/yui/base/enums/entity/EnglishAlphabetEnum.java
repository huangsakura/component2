package org.yui.base.enums.entity;

import lombok.Getter;

/**
 *
 */
@Getter
public enum EnglishAlphabetEnum implements EntityEnum {

    A("A","a","A"),
    B("B","b","b"),
    C("C","c","C"),
    D("D","d","D"),
    E("E","e","E"),
    F("F","f","F"),
    G("G","g","G"),
    H("H","h","H"),
    I("I","i","I"),
    J("J","j","J"),
    K("K","k","K"),
    L("L","l","L"),
    M("M","m","M"),
    N("N","n","N"),
    O("O","o","O"),
    P("P","p","P"),
    Q("Q","q","Q"),
    R("R","r","R"),
    S("S","s","S"),
    T("T","t","T"),
    U("U","u","U"),
    V("V","v","V"),
    W("W","w","W"),
    X("X","x","X"),
    Y("Y","y","Y"),
    Z("Z","z","Z");

    private final String code;
    private final String dbCode;
    private final String lowerCaseCode;
    private final String message;

    private EnglishAlphabetEnum(String code, String lowerCaseCode, String message) {
        this.code = code;
        this.message = message;
        this.dbCode = code;
        this.lowerCaseCode = lowerCaseCode;
    }

    /**
     *
     * @param code
     * @return
     */
    public static EnglishAlphabetEnum getByCode(String code) {

        for (EnglishAlphabetEnum englishAlphabetEnum : values()) {
            if (englishAlphabetEnum.getCode().equals(code)) {
                return englishAlphabetEnum;
            }
        }
        return null;
    }

    /**
     *
     * @param lowerCaseCode
     * @return
     */
    public static EnglishAlphabetEnum getByLowerCaseCode(String lowerCaseCode) {

        for (EnglishAlphabetEnum englishAlphabetEnum : values()) {
            if (englishAlphabetEnum.getLowerCaseCode().equals(lowerCaseCode)) {
                return englishAlphabetEnum;
            }
        }
        return null;
    }
}
