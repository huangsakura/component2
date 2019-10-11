package org.yui.base.bean.json.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.yui.base.util.DateUtil;

import java.io.IOException;
import java.time.MonthDay;

public final class MonthDaySerializer extends JsonSerializer<MonthDay> {

    /**
     * 静态常量
     */
    public static final MonthDaySerializer INSTANCE = new MonthDaySerializer();

    @Override
    public void serialize(MonthDay monthDay, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
        jsonGenerator.writeString(DateUtil.format(monthDay,DateUtil.DateTimeFormatEnum.DATE_TIME_FORMAT_13));
    }
}
