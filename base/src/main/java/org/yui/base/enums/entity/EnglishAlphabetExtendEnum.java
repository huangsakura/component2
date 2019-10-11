package org.yui.base.enums.entity;

import lombok.Getter;

/**
 *
 */
@Getter
public enum EnglishAlphabetExtendEnum implements EntityEnum {

    A(EnglishAlphabetEnum.A),
    B(EnglishAlphabetEnum.B),
    C(EnglishAlphabetEnum.C),
    D(EnglishAlphabetEnum.D),
    E(EnglishAlphabetEnum.E),
    F(EnglishAlphabetEnum.F),
    G(EnglishAlphabetEnum.G),
    H(EnglishAlphabetEnum.H),
    I(EnglishAlphabetEnum.I),
    J(EnglishAlphabetEnum.J),
    K(EnglishAlphabetEnum.K),
    L(EnglishAlphabetEnum.L),
    M(EnglishAlphabetEnum.M),
    N(EnglishAlphabetEnum.N),
    O(EnglishAlphabetEnum.O),
    P(EnglishAlphabetEnum.P),
    Q(EnglishAlphabetEnum.Q),
    R(EnglishAlphabetEnum.R),
    S(EnglishAlphabetEnum.S),
    T(EnglishAlphabetEnum.T),
    U(EnglishAlphabetEnum.U),
    V(EnglishAlphabetEnum.V),
    W(EnglishAlphabetEnum.W),
    X(EnglishAlphabetEnum.X),
    Y(EnglishAlphabetEnum.Y),
    Z(EnglishAlphabetEnum.Z),

    UNKNOWN("UNKNOWN","unknown","未知");

    private final String code;
    private final String dbCode;
    private final String lowerCaseCode;
    private final String message;

    private EnglishAlphabetExtendEnum(String code, String lowerCaseCode, String message) {
        this.code = code;
        this.message = message;
        this.dbCode = code;
        this.lowerCaseCode = lowerCaseCode;
    }

    private EnglishAlphabetExtendEnum(EnglishAlphabetEnum englishAlphabetEnum) {
        this.code = englishAlphabetEnum.getCode();
        this.message = englishAlphabetEnum.getMessage();
        this.dbCode = englishAlphabetEnum.getDbCode();
        this.lowerCaseCode = englishAlphabetEnum.getDbCode();
    }


    /**
     *
     * @param code
     * @return
     */
    public static EnglishAlphabetExtendEnum getByCode(String code) {

        for (EnglishAlphabetExtendEnum englishAlphabetExtendEnum : values()) {
            if (englishAlphabetExtendEnum.getCode().equals(code)) {
                return englishAlphabetExtendEnum;
            }
        }
        return EnglishAlphabetExtendEnum.UNKNOWN;
    }

    /**
     *
     * @param lowerCaseCode
     * @return
     */
    public static EnglishAlphabetExtendEnum getByLowerCaseCode(String lowerCaseCode) {

        for (org.yui.base.enums.entity.EnglishAlphabetExtendEnum EnglishAlphabetExtendEnum : values()) {
            if (EnglishAlphabetExtendEnum.getLowerCaseCode().equals(lowerCaseCode)) {
                return EnglishAlphabetExtendEnum;
            }
        }
        return EnglishAlphabetExtendEnum.UNKNOWN;
    }
}
