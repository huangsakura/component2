package org.yui.base.bean.json.keydeserializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.KeyDeserializer;
import org.apache.commons.lang3.StringUtils;
import org.yui.base.util.DateUtil;

import java.io.IOException;
import java.time.LocalDate;

public final class LocalDateKeyDeserializer extends KeyDeserializer {

    /**
     * 静态常量
     */
    public static final LocalDateKeyDeserializer INSTANCE = new LocalDateKeyDeserializer();

    @Override
    public Object deserializeKey(String s, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        if (StringUtils.isBlank(s)) {
            return null;
        }
        return LocalDate.parse(s, DateUtil.DATE_TIME_FORMATTER_5);
    }
}
