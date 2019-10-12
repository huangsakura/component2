package org.yui.web.bean.formatter;

import org.yui.base.util.DateUtil;
import org.springframework.format.Formatter;

import java.text.ParseException;
import java.time.YearMonth;
import java.util.Locale;

public final class YearMonthFormatter implements Formatter<YearMonth> {

    /**
     * 静态
     */
    public static final YearMonthFormatter INSTANCE = new YearMonthFormatter();

    @Override
    public YearMonth parse(String s, Locale locale) throws ParseException {
        return YearMonth.parse(s, DateUtil.DATE_TIME_FORMATTER_17);
    }

    @Override
    public String print(YearMonth yearMonth, Locale locale) {
        return DateUtil.format(yearMonth,DateUtil.DateTimeFormatEnum.DATE_TIME_FORMAT_17);
    }
}
