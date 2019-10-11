package org.yui.base.bean.json.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.yui.base.util.DateUtil;

import java.io.IOException;
import java.time.LocalTime;

public final class LocalTimeSerializer extends JsonSerializer<LocalTime> {

    /**
     * 静态常量
     */
    public static final LocalTimeSerializer INSTANCE = new LocalTimeSerializer();

    @Override
    public void serialize(LocalTime localTime, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
        jsonGenerator.writeString(DateUtil.format(localTime,DateUtil.DateTimeFormatEnum.DATE_TIME_FORMAT_6));
    }
}
