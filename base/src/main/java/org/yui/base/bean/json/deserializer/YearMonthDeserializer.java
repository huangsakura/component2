package org.yui.base.bean.json.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.yui.base.util.DateUtil;

import java.io.IOException;
import java.time.YearMonth;

public final class YearMonthDeserializer extends JsonDeserializer<YearMonth> {

    /**
     * 静态常量
     */
    public static final YearMonthDeserializer INSTANCE = new YearMonthDeserializer();

    @Override
    public YearMonth deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        String text = jsonParser.getText().trim();
        return YearMonth.parse(text, DateUtil.DATE_TIME_FORMATTER_17);
    }
}
