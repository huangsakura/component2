package org.yui.base.bean.json.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.yui.base.util.DateUtil;

import java.io.IOException;
import java.time.Year;

public final class YearDeserializer extends JsonDeserializer<Year> {

    /**
     * 静态常量
     */
    public static final YearDeserializer INSTANCE = new YearDeserializer();

    @Override
    public Year deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        String text = jsonParser.getText().trim();
        return Year.parse(text, DateUtil.DATE_TIME_FORMATTER_11);
    }
}
