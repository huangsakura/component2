package org.yui.base.util;

import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.NotBlank;
import org.yui.base.constant.StringConstant;
import org.yui.base.enums.HttpValidatorEnum;
import org.yui.base.exception.BusinessException;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.groups.Default;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * @author huangjinlong
 * 校验工具类
 */
public abstract class ValidationUtil {

    /**
     * 校验http的正则
     */
    public static final Pattern HTTP_PATTERN = Pattern.compile("^http://.+");

    /**
     * 校验https的正则
     */
    public static final Pattern HTTPS_PATTERN = Pattern.compile("^https://.+");

    /**
     * 校验http或https的正则
     */
    public static final Pattern HTTP_OR_HTTPS_PATTERN = Pattern.compile("^https?://.+");

    /**
     * 校验手机号的正则
     */
    public static final Pattern MOBILE_PATTERN = Pattern.compile("^1[3-9]\\d{9}$");

    /**
     * 只能是数字
     */
    public static final Pattern ONLY_INTEGER = Pattern.compile("^\\d+$");

    /**
     * 最多两位小数的数字
     */
    public static final Pattern NUMBER_WITH_MAX_2_DIGIT = Pattern.compile("^\\d+(\\.\\d{1,2})?$");

    /**
     * 校验 中国大陆的身份证号
     */
    public static final Pattern CERT_PATTERN_18 = Pattern.compile(
            "^(1[1-5]|2[1-3]|3[1-7]|4[1-3]|5[0-4]|6[1-5])\\d{4}(19|20)\\d{2}(0[1-9]|1[0-2])([12][1-9]|3[01])\\d{3}[\\dxX]$");


    /**
     * 中国大陆地区的座机号码
     */
    public static final Pattern PHONE_PATTERN = Pattern.compile(
            "^(((((010)|(02\\d))-?)?\\d{8})|((0[3-9]\\d{2})-?)?\\d{7})$");

    /**
     * 定义校验器
     */
    private static Validator VALIDATOR;
    static {
        VALIDATOR = Validation.buildDefaultValidatorFactory().getValidator();
    }

    /**
     * 校验失败的code
     */
    private static final String VALIDATE_FAIL_CODE = "VALIDATE_NOT_PASS";

    /**
     * 校验
     */
    public static void validate(Object object) {

        Set<ConstraintViolation<Object>> constraintViolationSet
                = VALIDATOR.validate(object, Default.class);
        if ((null != constraintViolationSet) && !constraintViolationSet.isEmpty()) {

            List<ConstraintViolation<Object>> constraintViolationList =
                    Lists.newArrayList(constraintViolationSet);

            throw new BusinessException(VALIDATE_FAIL_CODE,
                    constraintViolationList.get(0).getPropertyPath().toString()
                            + StringConstant.ONE_BLANK_SPACE
                            + constraintViolationList.get(0).getMessage());
        }
    }

    /**
     * 校验是不是http（https）链接
     */
    public static boolean isHttp(String value, HttpValidatorEnum httpValidatorEnum) {

        if (StringUtils.isBlank(value)) {
            return false;
        }

        switch (httpValidatorEnum) {
            case ONLY_VALIDATE_HTTP:{
                return HTTP_PATTERN.matcher(value).matches();
            } case HTTP_OR_HTTPS:{
                return HTTP_OR_HTTPS_PATTERN.matcher(value).matches();
            } case ONLY_VALIDATE_HTTPS:{
                return HTTPS_PATTERN.matcher(value).matches();
            } default:{
                return false;
            }
        }
    }

    /**
     * 校验是不是手机号
     */
    public static boolean isMobile(String value) {
        if (StringUtils.isBlank(value)) {
            return false;
        }

        return MOBILE_PATTERN.matcher(value).matches();
    }

    /**
     * 校验是不是中国大陆身份证
     *
     * 暂时不考虑15位数的身份证号
     */
    public static boolean isCert(String value) {
        if (StringUtils.isBlank(value)) {
            return false;
        }

        return CERT_PATTERN_18.matcher(value).matches();
    }

    /**
     * 校验是不是中国大陆的座机号码
     * @param value
     * @return
     */
    public static boolean isPhone(String value) {
        if (StringUtils.isBlank(value)) {
            return false;
        }
        return PHONE_PATTERN.matcher(value).matches();
    }

    /**
     * 判断当前字符是否是汉字或者是汉字的标点符号
     * @param c
     * @return
     */
    public static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        return (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION);
    }

    /**
     * 判断当前字符是否是汉字（注意：这里所说的汉字是不包括汉字的标点符号的）
     * @param c
     * @return
     */
    public static boolean isChineseByScript(char c) {
        return Character.UnicodeScript.HAN == Character.UnicodeScript.of(c);
    }

    /**
     * 判断当前字符串是否包含汉字或标点符号
     * @param text
     * @return
     */
    public static boolean containChinese(String text) {

        char[] chars = text.toCharArray();
        for (char c : chars) {
            if (isChinese(c)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断当前字符串是否包含非汉字或标点符号
     * @param text
     * @return
     */
    public static boolean containUnChinese(String text) {

        char[] chars = text.toCharArray();
        for (char c : chars) {
            if (!isChinese(c)) {
                return true;
            }
        }
        return false;
    }


    public static void main(String[] args) {

        /*
        System.out.println(isChinese('き'));
        System.out.println(isChineseByScript('き'));
        System.out.println(isChineseByScript('你'));
        System.out.println(isChineseByScript('，'));
        System.out.println(isChinese('，'));
         */
        /*
        System.out.println(isPhone("023-09090909"));
        System.out.println(isPhone("02309090909"));
        System.out.println(isPhone("09090909"));
        System.out.println("----------------");
        System.out.println(isPhone("033-09090909"));
        System.out.println(isPhone("03309090909"));
        System.out.println(isPhone("01009090909"));
        System.out.println(isPhone("01109090909"));
        */

        A a = new A();
        ValidationUtil.validate(a);
    }


    @Setter
    @Getter
    private static class A {
        @NotBlank
        private String x;
    }
}
