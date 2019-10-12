package org.yui.web.bean.formatter;

import org.yui.base.util.DateUtil;
import org.springframework.format.Formatter;

import java.text.ParseException;
import java.time.Year;
import java.util.Locale;

public final class YearFormatter implements Formatter<Year> {

    /**
     * 静态
     */
    public static final YearFormatter INSTANCE = new YearFormatter();

    @Override
    public Year parse(String s, Locale locale) throws ParseException {
        return Year.parse(s, DateUtil.DATE_TIME_FORMATTER_11);
    }

    @Override
    public String print(Year year, Locale locale) {
        return DateUtil.format(year,DateUtil.DateTimeFormatEnum.DATE_TIME_FORMAT_11);
    }
}
