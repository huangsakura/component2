package org.yui.base.bean.json.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.yui.base.util.DateUtil;

import java.io.IOException;
import java.time.LocalTime;

public final class LocalTimeDeserializer extends JsonDeserializer<LocalTime> {

    /**
     * 静态常量
     */
    public static final LocalTimeDeserializer INSTANCE = new LocalTimeDeserializer();

    @Override
    public LocalTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        String text = jsonParser.getText().trim();
        return LocalTime.parse(text, DateUtil.DATE_TIME_FORMATTER_6);
    }
}
