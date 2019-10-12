package org.yui.safety.md5;

import org.apache.commons.codec.binary.Hex;
import org.yui.base.bean.constant.StringConstant;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;

/**
 * 由原来阙海林的工程中剥离迁移出来 by zhanglin 20180927
 *
 * 再新增了加16位随机盐值的MD5方法
 */
public final class MD5Util {

    private static final char[] HEX_DIGITS =
            new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    /**
     * 产生随机数
     */
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    /**
     * MD5常量
     */
    private static final String MD5_STRING = "MD5";

    private MD5Util() {}

    public static String getMD5(String s) {
        try {
            byte[] btInput = s.getBytes(StandardCharsets.UTF_8);
            MessageDigest mdInst = MessageDigest.getInstance(MD5_STRING);
            mdInst.update(btInput);
            byte[] md = mdInst.digest();
            int j = md.length;
            char[] str = new char[j * 2];
            int k = 0;

            for (int i = 0; i < j; ++i) {
                byte byte0 = md[i];
                str[k++] = HEX_DIGITS[byte0 >>> 4 & 15];
                str[k++] = HEX_DIGITS[byte0 & 15];
            }

            return new String(str);
        } catch (Exception var9) {
            var9.printStackTrace();
            return null;
        }
    }

    /**
     * 使用加盐的MD5
     * @param password
     * @return
     */
    public static String getSaltMD5(String password) {
        /**
         * 生成一个16位的随机数
         */
        StringBuilder sBuilder = new StringBuilder(16);
        /**
         * random没有提供nextLong(bound)函数
         */
        sBuilder.append(SECURE_RANDOM.nextInt(99999999)).append(SECURE_RANDOM.nextInt(99999999));
        int len = sBuilder.length();
        if (len < 16) {
            for (int i = 0; i < 16 - len; i++) {
                sBuilder.append(StringConstant.ZERO);
            }
        }
        /**
         * 生成最终的加密盐
         */
        String Salt = sBuilder.toString();
        password = md5Hex(password + Salt);
        char[] cs = new char[48];
        for (int i = 0; i < 48; i += 3) {
            cs[i] = password.charAt(i / 3 * 2);
            char c = Salt.charAt(i / 3);
            cs[i + 1] = c;
            cs[i + 2] = password.charAt(i / 3 * 2 + 1);
        }
        return String.valueOf(cs);
    }

    /**
     * 使用Apache的Hex类实现Hex(16进制字符串和)和字节数组的互转
     * @param str
     * @return
     */
    private static String md5Hex(String str) {
        try {
            MessageDigest md = MessageDigest.getInstance(MD5_STRING);
            byte[] digest = md.digest(str.getBytes(StandardCharsets.UTF_8));
            return new String(new Hex().encode(digest),StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.toString());
            return "";
        }
    }

    public static void main(String[] a) {
        System.out.print(getMD5("123456"));
    }
}