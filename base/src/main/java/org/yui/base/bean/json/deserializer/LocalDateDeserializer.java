package org.yui.base.bean.json.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.yui.base.util.DateUtil;

import java.io.IOException;
import java.time.LocalDate;

public final class LocalDateDeserializer extends JsonDeserializer<LocalDate>  {

    /**
     * 静态常量
     */
    public static final LocalDateDeserializer INSTANCE = new LocalDateDeserializer();

    @Override
    public LocalDate deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        String text = jsonParser.getText().trim();
        return LocalDate.parse(text, DateUtil.DATE_TIME_FORMATTER_5);
    }
}
