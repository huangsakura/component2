package org.yui.base.bean.json.serializer;

import org.yui.base.util.DateUtil;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.LocalDate;

public final class LocalDateSerializer extends JsonSerializer<LocalDate> {

    /**
     * 静态常量
     */
    public static final LocalDateSerializer INSTANCE = new LocalDateSerializer();

    @Override
    public void serialize(LocalDate localDate, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
        jsonGenerator.writeString(DateUtil.format(localDate,DateUtil.DateTimeFormatEnum.DATE_TIME_FORMAT_5));
    }
}
