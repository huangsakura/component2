package org.yui.base.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.Validate;
import org.hibernate.validator.constraints.NotBlank;
import org.yui.base.bean.api.JsonResult;
import org.yui.base.bean.json.deserializer.*;
import org.yui.base.bean.json.keydeserializer.*;
import org.yui.base.bean.json.serializer.*;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.time.*;

/**
 * @author huangjinlong
 *
 * json工具类
 */
@Log4j2
public abstract class JsonUtil {

    private static volatile ObjectMapper OBJECT_MAPPER = null;
    /**
     * 把对象转为json字符串
     * @param o
     * @return
     */
    public static String toJsonString(Object o) {
        return JSON.toJSONString(o);
    }

    /**
     *
     * @param objectMapper
     * @param o
     * @return
     */
    @Nullable
    public static String toJsonStringQuietly(@NotNull ObjectMapper objectMapper, Object o) {
        try {
            return objectMapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            log.error("ObjectMapper序列化对象失败:{}",e.getMessage());
            return null;
        }
    }

    /**
     *
     * @param o
     * @return
     */
    @Nullable
    public static String toJsonStringQuietly(Object o) {
        Validate.notNull(OBJECT_MAPPER);
        try {
            return OBJECT_MAPPER.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            log.error("ObjectMapper序列化对象失败:{}",e.getMessage());
            return null;
        }
    }

    /**
     *
     * @param objectMapper
     * @param text
     * @param tClass
     * @param <T>
     * @return
     */
    @Nullable
    public static <T> T toObjectQuietly(@NotNull ObjectMapper objectMapper,@NotBlank String text, Class<T> tClass) {
        try {
            return objectMapper.readValue(text,tClass);
        } catch (IOException e) {
            log.error("ObjectMapper反序列化成对象失败:{}",e.getMessage());
            return null;
        }
    }

    /**
     * 把json字符串转为对象
     *
     * @param text
     * @param type
     * @param <T>
     * @return
     */
    public static <T> T toJsonObject(String text,Class<T> type) {
        return JSON.parseObject(text,type);
    }

    /**
     *
     * @param objectMapper
     */
    public static void userDefineObjectMapper(@NotNull ObjectMapper objectMapper,boolean forGeneralUse) {

        /**
         * 修改ObjectMapper的属性
         */
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addSerializer(LocalDateTime.class, LocalDateTimeSerializer.INSTANCE);
        simpleModule.addSerializer(LocalDate.class, LocalDateSerializer.INSTANCE);
        simpleModule.addSerializer(LocalTime.class, LocalTimeSerializer.INSTANCE);
        simpleModule.addSerializer(Year.class, YearSerializer.INSTANCE);
        simpleModule.addSerializer(YearMonth.class, YearMonthSerializer.INSTANCE);
        simpleModule.addSerializer(MonthDay.class, MonthDaySerializer.INSTANCE);

        simpleModule.addDeserializer(LocalDateTime.class, LocalDateTimeDeserializer.INSTANCE);
        simpleModule.addDeserializer(LocalDate.class, LocalDateDeserializer.INSTANCE);
        simpleModule.addDeserializer(LocalTime.class, LocalTimeDeserializer.INSTANCE);
        simpleModule.addDeserializer(YearMonth.class, YearMonthDeserializer.INSTANCE);
        simpleModule.addDeserializer(Year.class, YearDeserializer.INSTANCE);
        simpleModule.addDeserializer(MonthDay.class, MonthDayDeserializer.INSTANCE);

        simpleModule.addKeyDeserializer(LocalDateTime.class, LocalDateTimeKeyDeserializer.INSTANCE);
        simpleModule.addKeyDeserializer(LocalDate.class, LocalDateKeyDeserializer.INSTANCE);
        simpleModule.addKeyDeserializer(LocalTime.class, LocalTimeKeyDeserializer.INSTANCE);
        simpleModule.addKeyDeserializer(Year.class, YearKeyDeserializer.INSTANCE);
        simpleModule.addKeyDeserializer(YearMonth.class, YearMonthKeyDeserializer.INSTANCE);
        simpleModule.addKeyDeserializer(MonthDay.class, MonthDayKeyDeserializer.INSTANCE);
        objectMapper.registerModule(simpleModule);

        if (forGeneralUse && (null == OBJECT_MAPPER)) {
            synchronized (JsonUtil.class) {
                if (null == OBJECT_MAPPER) {
                    OBJECT_MAPPER = objectMapper;
                }
            }
        }
    }


    /**
     *
     * @param jsonObject
     * @return
     */
    public static boolean success(@NotNull JSONObject jsonObject) {
        return JsonResult.SUCCESS.equals(jsonObject.getString("code"));
    }
}
