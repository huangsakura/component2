package org.yui.base.util;

import org.apache.commons.lang3.StringUtils;
import org.yui.base.constant.StringConstant;

import java.util.regex.Pattern;

/**
 * 字符串处理的工具类
 *
 * 请优先使用 org.apache.commons.lang3.StringUtils
 */
public abstract class StringUtil {

    private static final Pattern PATTERN_UNDERLINE = Pattern.compile("^[A-Za-z][a-zA-Z_]*$");

    private static final Pattern PATTERN_CAMEL = Pattern.compile("^[a-z]+$");

    /**
     * 把带有下划线的字符串转为驼峰命名规则
     */
    public static String transferUnderline2Camel(String originalText) {
        if (StringUtils.isBlank(originalText) || !originalText.contains(StringConstant.UNDERLINE)) {
            return originalText;
        }

        if (PATTERN_UNDERLINE.matcher(originalText).matches()) {

            originalText = originalText.toLowerCase();

            StringBuilder stringBuilder = new StringBuilder();
            String[] originalTexts = originalText.split(StringConstant.UNDERLINE);
            int length = originalTexts.length;

            for (int i = 0; i < length; i++) {
                String x = originalTexts[i];
                if (StringUtils.isBlank(x)) {
                    continue;
                }
                if (i == 0) {
                    stringBuilder.append(x);
                } else {
                    char firstCharOfX = x.charAt(0);
                    stringBuilder.append(String.valueOf(firstCharOfX).toUpperCase());
                    if (x.length() > 1) {
                        stringBuilder.append(x.substring(1));
                    }
                }
            }
            return stringBuilder.toString();
        } else {
            throw new IllegalArgumentException("参数错误");
        }
    }

    public static String transferUnderline2Entity(String originalText) {
        if (StringUtils.isBlank(originalText) || !originalText.contains(StringConstant.UNDERLINE)) {
            return originalText;
        }

        if (PATTERN_UNDERLINE.matcher(originalText).matches()) {

            originalText = originalText.toLowerCase();

            StringBuilder stringBuilder = new StringBuilder();
            String[] originalTexts = originalText.split(StringConstant.UNDERLINE);
            int length = originalTexts.length;

            for (int i = 0; i < length; i++) {
                String x = originalTexts[i];
                if (StringUtils.isBlank(x)) {
                    continue;
                }
                char firstCharOfX = x.charAt(0);
                stringBuilder.append(String.valueOf(firstCharOfX).toUpperCase());
                if (x.length() > 1) {
                    stringBuilder.append(x.substring(1));
                }
            }
            return stringBuilder.toString();
        } else {
            throw new IllegalArgumentException("参数错误");
        }
    }

    /**
     * 把驼峰命名法的字符串改为 下划线的
     * @param originalText
     * @return
     */
    public static String transferCamel2Underline(String originalText) {
        if (StringUtils.isBlank(originalText)) {
            return originalText;
        }
        if (PATTERN_CAMEL.matcher(originalText).matches()) {

            StringBuilder stringBuilder = new StringBuilder();
            for (char x : originalText.toCharArray()) {
                if ((x >= 'A') && (x <= 'Z')) {
                    stringBuilder.append(StringConstant.UNDERLINE);
                    stringBuilder.append(String.valueOf(x).toLowerCase());
                } else {
                    stringBuilder.append(x);
                }
            }
            return stringBuilder.toString();
        } else {
            throw new IllegalArgumentException("参数错误");
        }
    }

    public static void main(String[] a) {
        System.out.println(transferUnderline2Camel("awe_as_ddd_s"));
        System.out.println(transferCamel2Underline("aweAsDddS"));
    }
}
