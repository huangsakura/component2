package org.yui.base.bean.json.keydeserializer;

import org.yui.base.util.DateUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.KeyDeserializer;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.time.LocalDateTime;

public final class LocalDateTimeKeyDeserializer extends KeyDeserializer {

    /**
     * 静态常量
     */
    public static final LocalDateTimeKeyDeserializer INSTANCE = new LocalDateTimeKeyDeserializer();

    @Override
    public Object deserializeKey(String s, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        if (StringUtils.isBlank(s)) {
            return null;
        }
        return LocalDateTime.parse(s, DateUtil.DATE_TIME_FORMATTER_4);
    }
}
