package org.yui.base.bean.json.deserializer;

import org.yui.base.util.DateUtil;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.MonthDay;

public final class MonthDayDeserializer extends JsonDeserializer<MonthDay> {

    /**
     * 静态常量
     */
    public static final MonthDayDeserializer INSTANCE = new MonthDayDeserializer();

    @Override
    public MonthDay deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        String text = jsonParser.getText().trim();
        return MonthDay.parse(text, DateUtil.DATE_TIME_FORMATTER_13);
    }
}
