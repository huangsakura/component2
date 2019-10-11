package org.yui.base.bean.json.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.yui.base.util.DateUtil;

import java.io.IOException;
import java.time.LocalDateTime;

public final class LocalDateTimeSerializer extends JsonSerializer<LocalDateTime> {

    /**
     * 静态常量
     */
    public static final LocalDateTimeSerializer INSTANCE = new LocalDateTimeSerializer();

    @Override
    public void serialize(LocalDateTime localDateTime, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
        jsonGenerator.writeString(DateUtil.format(localDateTime,DateUtil.DateTimeFormatEnum.DATE_TIME_FORMAT_4));
    }
}
