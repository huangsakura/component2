package org.yui.web.bean.formatter;

import org.yui.base.util.DateUtil;
import org.springframework.format.Formatter;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.Locale;

public final class LocalDateTimeFormatter implements Formatter<LocalDateTime> {

    /**
     * 静态
     */
    public static final LocalDateTimeFormatter INSTANCE = new LocalDateTimeFormatter();

    @Override
    public LocalDateTime parse(String s, Locale locale) throws ParseException {
        return LocalDateTime.parse(s, DateUtil.DATE_TIME_FORMATTER_4);
    }

    @Override
    public String print(LocalDateTime localDateTime, Locale locale) {
        return DateUtil.format(localDateTime,DateUtil.DateTimeFormatEnum.DATE_TIME_FORMAT_4);
    }
}
