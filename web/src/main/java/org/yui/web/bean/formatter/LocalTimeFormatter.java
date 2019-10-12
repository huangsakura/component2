package org.yui.web.bean.formatter;

import org.yui.base.util.DateUtil;
import org.springframework.format.Formatter;

import java.text.ParseException;
import java.time.LocalTime;
import java.util.Locale;

public final class LocalTimeFormatter implements Formatter<LocalTime> {

    /**
     * 静态
     */
    public static final LocalTimeFormatter INSTANCE = new LocalTimeFormatter();

    @Override
    public LocalTime parse(String s, Locale locale) throws ParseException {
        return LocalTime.parse(s, DateUtil.DATE_TIME_FORMATTER_6);
    }

    @Override
    public String print(LocalTime localTime, Locale locale) {
        return DateUtil.format(localTime,DateUtil.DateTimeFormatEnum.DATE_TIME_FORMAT_6);
    }
}
