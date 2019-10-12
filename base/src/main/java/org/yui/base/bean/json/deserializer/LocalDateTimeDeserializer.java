package org.yui.base.bean.json.deserializer;

import org.yui.base.util.DateUtil;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.LocalDateTime;

public final class LocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {

    /**
     * 静态常量
     */
    public static final LocalDateTimeDeserializer INSTANCE = new LocalDateTimeDeserializer();

    @Override
    public LocalDateTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        String text = jsonParser.getText().trim();
        return LocalDateTime.parse(text, DateUtil.DATE_TIME_FORMATTER_4);
    }
}
