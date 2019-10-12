package org.yui.web.bean.formatter;

import org.yui.base.util.DateUtil;
import org.springframework.format.Formatter;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.Locale;

public final class LocalDateFormatter implements Formatter<LocalDate> {

    /**
     * 静态
     */
    public static final LocalDateFormatter INSTANCE = new LocalDateFormatter();

    @Override
    public LocalDate parse(String s, Locale locale) throws ParseException {
        return LocalDate.parse(s, DateUtil.DATE_TIME_FORMATTER_5);
    }

    @Override
    public String print(LocalDate localDate, Locale locale) {
        return DateUtil.format(localDate,DateUtil.DateTimeFormatEnum.DATE_TIME_FORMAT_5);
    }
}
