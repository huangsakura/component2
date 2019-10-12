package org.yui.safety.aes;

import lombok.extern.log4j.Log4j2;
import org.hibernate.validator.constraints.NotBlank;

import javax.annotation.Nullable;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.validation.constraints.NotNull;

/**
 * @author huangjinlong
 * @time 2019-10-12 14:32
 * @description
 */
@Log4j2
public abstract class AesUtil {

    public static final String ALGORITHM0  = "AES/ECB/PKCS5Padding";

    public static final String ALGORITHM1 = "PKCS7Padding";

    private static final String ALGORITHM_AES = "AES";

    private static final String BC = "BC";

    /**
     * 本方法，参考自亿美短信的官方demo
     * @param content
     * @param password
     * @param algorithm
     * @return
     */
    @Nullable
    public static byte[] encrypt(@NotNull byte[] content, @NotNull byte[] password, @NotBlank String algorithm) {
        try {
            Cipher cipher = null;
            if (algorithm.endsWith(ALGORITHM1)) {
                cipher = Cipher.getInstance(algorithm, BC);
            } else {
                cipher = Cipher.getInstance(algorithm);
            }
            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(password, ALGORITHM_AES));
            return cipher.doFinal(content);
        } catch (Exception e) {
            log.error("aes加密出错:{}",e.getMessage());
            return null;
        }
    }

    /**
     * 本方法，参考自亿美短信的官方demo
     * @param content
     * @param password
     * @param algorithm
     * @return
     */
    @Nullable
    public static byte[] decrypt(@NotNull byte[] content,@NotNull byte[] password,@NotBlank String algorithm) {
        try {
            Cipher cipher = null;
            if (algorithm.endsWith(ALGORITHM1)) {
                cipher = Cipher.getInstance(algorithm, BC);
            } else {
                cipher = Cipher.getInstance(algorithm);
            }
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(password, ALGORITHM_AES));
            return cipher.doFinal(content);
        } catch (Exception e) {
            log.error("aes解密出错:{}",e.getMessage());
            return null;
        }
    }
}
