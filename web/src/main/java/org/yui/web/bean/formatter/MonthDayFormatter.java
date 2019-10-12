package org.yui.web.bean.formatter;

import org.yui.base.util.DateUtil;
import org.springframework.format.Formatter;

import java.text.ParseException;
import java.time.MonthDay;
import java.util.Locale;

public final class MonthDayFormatter implements Formatter<MonthDay> {

    /**
     * 静态
     */
    public static final MonthDayFormatter INSTANCE = new MonthDayFormatter();

    @Override
    public MonthDay parse(String s, Locale locale) throws ParseException {
        return MonthDay.parse(s, DateUtil.DATE_TIME_FORMATTER_13);
    }

    @Override
    public String print(MonthDay monthDay, Locale locale) {
        return DateUtil.format(monthDay,DateUtil.DateTimeFormatEnum.DATE_TIME_FORMAT_13);
    }
}
