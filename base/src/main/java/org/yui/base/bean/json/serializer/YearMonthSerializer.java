package org.yui.base.bean.json.serializer;

import org.yui.base.util.DateUtil;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.YearMonth;

public final class YearMonthSerializer extends JsonSerializer<YearMonth> {

    /**
     * 静态常量
     */
    public static final YearMonthSerializer INSTANCE = new YearMonthSerializer();

    @Override
    public void serialize(YearMonth yearMonth, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
        jsonGenerator.writeString(DateUtil.format(yearMonth,DateUtil.DateTimeFormatEnum.DATE_TIME_FORMAT_17));
    }
}
