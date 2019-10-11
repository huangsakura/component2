package org.yui.base.util;

import lombok.extern.log4j.Log4j2;

import javax.annotation.Nullable;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

/**
 * @author huangjinlong
 * @time 2019-02-22 16:28
 * @description
 */
@Log4j2
public abstract class ByteUtil {

    @Nullable
    public static Object readFromByteArray(byte[] bytes) {
        try {
            return new ObjectInputStream(new ByteArrayInputStream(bytes)).readObject();
        } catch (IOException | ClassNotFoundException e) {
            log.warn("byte[]转Object对象失败:{}",e.getMessage());
            return null;
        }
    }
}
