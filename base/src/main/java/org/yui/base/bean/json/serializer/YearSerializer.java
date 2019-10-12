package org.yui.base.bean.json.serializer;

import org.yui.base.util.DateUtil;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.Year;

public final class YearSerializer extends JsonSerializer<Year> {

    /**
     * 静态常量
     */
    public static final YearSerializer INSTANCE = new YearSerializer();

    @Override
    public void serialize(Year year, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
        jsonGenerator.writeString(DateUtil.format(year,DateUtil.DateTimeFormatEnum.DATE_TIME_FORMAT_11));
    }
}
