package org.yui.base.util;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.Validate;
import org.yui.base.constant.StringConstant;
import org.yui.base.exception.BusinessException;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.UUID;

/**
 * 生成随机字符串的工具类
 */
@Log4j2
public abstract class RandomUtil {

    private static final char[] CHAR_STRING =
            {'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f','g','h','i','j',
                    'k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z','A','B',
                    'C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T',
                    'U','V','W','X','Y','Z'};

    /**
     * CHAR_STRING 的长度
     */
    private static final int CHAR_STRING_LENGTH = 62;

    /**
     * CHAR_STRING 里面的数字的长度
     */
    private static final int CHAR_STRING_NUMBER_LENGTH = 10;

    private static SecureRandom SECURE_RANDOM;
    static {
        try {
            SECURE_RANDOM = SecureRandom.getInstance("SHA1PRNG");
            /**
             * 由于产生第一个随机数较慢，所以在这里就产生一个无用的随机数
             */
            SECURE_RANDOM.nextInt(CHAR_STRING_LENGTH);
        } catch (NoSuchAlgorithmException e) {
            throw new BusinessException("INIT_RANDOM_UTIL_ERROR","初始化随机数工具类出错",false);
        }
    }

    /**
     *
     * @return
     */
    public static String uuid() {
        return UUID.randomUUID().toString();
    }

    /**
     *
     * @return
     */
    public static String uuidWithoutHyphen() {
        return uuid().replace(StringConstant.SHORT_LINE,StringConstant.BLANK);
    }

    /**
     * 此方法不保证全局唯一
     * @return
     */
    public static String did() {
        return System.currentTimeMillis() + randomCode(7);
    }

    /**
     * 产生 number 位的随机数，包括 大写英语字母，小写英语字母和阿拉伯数字
     * @param number
     * @return
     */
    public static String randomCode(int number) {
        Validate.isTrue(number > 0, "数字必须大于0");
        StringBuilder stringBuilder = new StringBuilder();
        for (int i=0;i<number;i++) {
            stringBuilder.append(CHAR_STRING[SECURE_RANDOM.nextInt(CHAR_STRING_LENGTH)]);
        }
        return stringBuilder.toString();
    }

    /**
     * 产生 number 位的随机数，只包括阿拉伯数字
     * @param number
     * @return
     */
    public static String randomNumber(int number) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i=0;i<number;i++) {
            stringBuilder.append(CHAR_STRING[SECURE_RANDOM.nextInt(CHAR_STRING_NUMBER_LENGTH)]);
        }
        return stringBuilder.toString();
    }

    /**
     * 生成随机的常见汉字
     * 参考 https://blog.csdn.net/HardWorkingAnt/article/details/69189731
     * @param number
     * @return
     */
    public static String randomChinese(int number) {

        Validate.isTrue(number > 0,"入参必须大于零");

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0;i < number;i++) {
            int highCode = (176 + Math.abs(SECURE_RANDOM.nextInt(39)));
            int lowCode = (161 + Math.abs(SECURE_RANDOM.nextInt(93)));

            byte[] bytes = new byte[2];
            bytes[0] = (Integer.valueOf(highCode)).byteValue();
            bytes[1] = (Integer.valueOf(lowCode)).byteValue();

            try {
                stringBuilder.append(new String(bytes, StringConstant.GBK));
            } catch (UnsupportedEncodingException e) {
                log.error("不支持的编码:{}",e.getMessage());
                stringBuilder.append(StringConstant.BLANK);
            }
        }
        return stringBuilder.toString();
    }

    public static void main(String[] args) {

//        ExecutorService executorService = Executors.newCachedThreadPool();
//
//        for (int i=0;i<CHAR_STRING_LENGTH;i++) {
//            //System.out.println(CHAR_STRING[i]);
//        }
//        //System.out.println(randomNumber(23));
//        //System.out.println(did());
//        //System.out.println(did());
//
//        for (int i=0;i<100;i++) {
//            executorService.execute(() -> {
//                //System.out.println(did());
//            });
//        }
//        executorService.shutdown();
//
//        System.out.println(randomChinese(8));
    }

}
