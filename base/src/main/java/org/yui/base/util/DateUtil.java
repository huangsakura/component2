package org.yui.base.util;

import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAccessor;

/**
 *
 * @author huangjinlong
 *
 * 日期时间工具类
 *
 * 请使用jdk8的时间，不要再使用 Date Calendar 等类
 */
public abstract class DateUtil {

    /**
     * 东八区
     */
    private static final ZoneId CHINA_ZONE_ID = ZoneId.of("Asia/Shanghai");

    /**
     * java8的Period类的字符串正则
     */
    //private static final Pattern PERIOD_PATTERN = Pattern.compile("^P(-?\\d+Y)?(-?\\d+M)?-?\\d+D$");

    /**
     * java8的Duration类的字符串正则
     */
    //private static final Pattern DURATION_PATTERN = Pattern.compile("^PT(-?\\d+H)?(-?\\d+M)?(-?\\d+\\.?(\\d+)?S)?$");

    private DateUtil(){}

    @Getter
    public enum DateTimeFormatEnum {

        /**
         * 如果有其他格式化的需求，请联系黄金龙
         */
        DATE_TIME_FORMAT_1("yyMMddHHmmssSSS"),
        DATE_TIME_FORMAT_2("yyMMddHHmmss"),
        DATE_TIME_FORMAT_3("yyyyMMdd"),
        DATE_TIME_FORMAT_4("yyyy-MM-dd HH:mm:ss"),
        DATE_TIME_FORMAT_5("yyyy-MM-dd"),
        DATE_TIME_FORMAT_6("HH:mm:ss"),
        DATE_TIME_FORMAT_7("yyyy/MM/dd"),
        DATE_TIME_FORMAT_8("yyyy.MM.dd"),
        DATE_TIME_FORMAT_9("yyyyMMddHHmmssSSS"),
        DATE_TIME_FORMAT_10("yyyyMMddHHmmss"),
        DATE_TIME_FORMAT_11("yyyy"),
        DATE_TIME_FORMAT_12("MMdd"),
        DATE_TIME_FORMAT_13("MM-dd"),
        DATE_TIME_FORMAT_14("MM"),
        DATE_TIME_FORMAT_15("dd"),
        DATE_TIME_FORMAT_16("yyyyMM"),
        DATE_TIME_FORMAT_17("yyyy-MM"),
        DATE_TIME_FORMAT_18("yyyy年MM月dd日"),
        DATE_TIME_FORMAT_19("HHmmss");

        private final String formatString;

        private DateTimeFormatEnum(String formatString) {
            this.formatString = formatString;
        }
    }

    /**
     * 不太清楚是否是线程安全的
     */
    public static final DateTimeFormatter DATE_TIME_FORMATTER_1 =
            DateTimeFormatter.ofPattern(DateTimeFormatEnum.DATE_TIME_FORMAT_1.getFormatString());
    public static final DateTimeFormatter DATE_TIME_FORMATTER_2 =
            DateTimeFormatter.ofPattern(DateTimeFormatEnum.DATE_TIME_FORMAT_2.getFormatString());
    public static final DateTimeFormatter DATE_TIME_FORMATTER_3 =
            DateTimeFormatter.ofPattern(DateTimeFormatEnum.DATE_TIME_FORMAT_3.getFormatString());
    public static final DateTimeFormatter DATE_TIME_FORMATTER_4 =
            DateTimeFormatter.ofPattern(DateTimeFormatEnum.DATE_TIME_FORMAT_4.getFormatString());
    public static final DateTimeFormatter DATE_TIME_FORMATTER_5 =
            DateTimeFormatter.ofPattern(DateTimeFormatEnum.DATE_TIME_FORMAT_5.getFormatString());
    public static final DateTimeFormatter DATE_TIME_FORMATTER_6 =
            DateTimeFormatter.ofPattern(DateTimeFormatEnum.DATE_TIME_FORMAT_6.getFormatString());
    public static final DateTimeFormatter DATE_TIME_FORMATTER_7 =
            DateTimeFormatter.ofPattern(DateTimeFormatEnum.DATE_TIME_FORMAT_7.getFormatString());
    public static final DateTimeFormatter DATE_TIME_FORMATTER_8 =
            DateTimeFormatter.ofPattern(DateTimeFormatEnum.DATE_TIME_FORMAT_8.getFormatString());
    public static final DateTimeFormatter DATE_TIME_FORMATTER_9 =
            DateTimeFormatter.ofPattern(DateTimeFormatEnum.DATE_TIME_FORMAT_9.getFormatString());
    public static final DateTimeFormatter DATE_TIME_FORMATTER_10 =
            DateTimeFormatter.ofPattern(DateTimeFormatEnum.DATE_TIME_FORMAT_10.getFormatString());
    public static final DateTimeFormatter DATE_TIME_FORMATTER_11 =
            DateTimeFormatter.ofPattern(DateTimeFormatEnum.DATE_TIME_FORMAT_11.getFormatString());
    public static final DateTimeFormatter DATE_TIME_FORMATTER_12 =
            DateTimeFormatter.ofPattern(DateTimeFormatEnum.DATE_TIME_FORMAT_12.getFormatString());
    public static final DateTimeFormatter DATE_TIME_FORMATTER_13 =
            DateTimeFormatter.ofPattern(DateTimeFormatEnum.DATE_TIME_FORMAT_13.getFormatString());
    public static final DateTimeFormatter DATE_TIME_FORMATTER_14 =
            DateTimeFormatter.ofPattern(DateTimeFormatEnum.DATE_TIME_FORMAT_14.getFormatString());
    public static final DateTimeFormatter DATE_TIME_FORMATTER_15 =
            DateTimeFormatter.ofPattern(DateTimeFormatEnum.DATE_TIME_FORMAT_15.getFormatString());
    public static final DateTimeFormatter DATE_TIME_FORMATTER_16 =
            DateTimeFormatter.ofPattern(DateTimeFormatEnum.DATE_TIME_FORMAT_16.getFormatString());
    public static final DateTimeFormatter DATE_TIME_FORMATTER_17 =
            DateTimeFormatter.ofPattern(DateTimeFormatEnum.DATE_TIME_FORMAT_17.getFormatString());
    public static final DateTimeFormatter DATE_TIME_FORMATTER_18 =
            DateTimeFormatter.ofPattern(DateTimeFormatEnum.DATE_TIME_FORMAT_18.getFormatString());
    public static final DateTimeFormatter DATE_TIME_FORMATTER_19 =
            DateTimeFormatter.ofPattern(DateTimeFormatEnum.DATE_TIME_FORMAT_19.getFormatString());





    /**
     * 返回当前时间，年月日时分秒
     * @return
     */
    public static LocalDateTime currentDateTime() {
        /**
         * 返回上海时间
         */
        return LocalDateTime.now(CHINA_ZONE_ID);
    }

    /**
     * 返回当前时间，只有年月日，没有时分秒
     * @return
     */
    public static LocalDate currentDate() {
        /**
         * 返回上海时间
         */
        return LocalDate.now(CHINA_ZONE_ID);
    }

    /**
     * 返回当前时间，没有年月日，只有时分秒
     * @return
     */
    public static LocalTime currentTime() {
        /**
         * 返回上海时间
         */
        return LocalTime.now(CHINA_ZONE_ID);
    }

    /**
     * 当前年份
     * @return
     */
    public static Year currentYear() {
        return Year.now(CHINA_ZONE_ID);
    }

    /**
     * 当前月份和日期
     * @return
     */
    public static MonthDay currentMonthDay() {
        return MonthDay.now(CHINA_ZONE_ID);
    }

    /**
     * 当前年份和月份
     * @return
     */
    public static YearMonth currentYearMonth() {
        return YearMonth.now(CHINA_ZONE_ID);
    }


    /**
     * 格式化时间
     */
    public static String format(TemporalAccessor temporalAccessor, DateTimeFormatEnum dateTimeFormatEnum) {
        switch (dateTimeFormatEnum) {
            case DATE_TIME_FORMAT_1:{
                return DATE_TIME_FORMATTER_1.format(temporalAccessor);
            } case DATE_TIME_FORMAT_2:{
                return DATE_TIME_FORMATTER_2.format(temporalAccessor);
            } case DATE_TIME_FORMAT_3:{
                return DATE_TIME_FORMATTER_3.format(temporalAccessor);
            } case DATE_TIME_FORMAT_4:{
                return DATE_TIME_FORMATTER_4.format(temporalAccessor);
            } case DATE_TIME_FORMAT_5:{
                return DATE_TIME_FORMATTER_5.format(temporalAccessor);
            } case DATE_TIME_FORMAT_6:{
                return DATE_TIME_FORMATTER_6.format(temporalAccessor);
            } case DATE_TIME_FORMAT_7:{
                return DATE_TIME_FORMATTER_7.format(temporalAccessor);
            } case DATE_TIME_FORMAT_8:{
                return DATE_TIME_FORMATTER_8.format(temporalAccessor);
            } case DATE_TIME_FORMAT_9:{
                return DATE_TIME_FORMATTER_9.format(temporalAccessor);
            } case DATE_TIME_FORMAT_10:{
                return DATE_TIME_FORMATTER_10.format(temporalAccessor);
            } case DATE_TIME_FORMAT_11:{
                return DATE_TIME_FORMATTER_11.format(temporalAccessor);
            } case DATE_TIME_FORMAT_12:{
                return DATE_TIME_FORMATTER_12.format(temporalAccessor);
            } case DATE_TIME_FORMAT_13:{
                return DATE_TIME_FORMATTER_13.format(temporalAccessor);
            } case DATE_TIME_FORMAT_14:{
                return DATE_TIME_FORMATTER_14.format(temporalAccessor);
            } case DATE_TIME_FORMAT_15:{
                return DATE_TIME_FORMATTER_15.format(temporalAccessor);
            } case DATE_TIME_FORMAT_16:{
                return DATE_TIME_FORMATTER_16.format(temporalAccessor);
            } case DATE_TIME_FORMAT_17:{
                return DATE_TIME_FORMATTER_17.format(temporalAccessor);
            } case DATE_TIME_FORMAT_18:{
                return DATE_TIME_FORMATTER_18.format(temporalAccessor);
            } case DATE_TIME_FORMAT_19:{
                return DATE_TIME_FORMATTER_19.format(temporalAccessor);
            } default:{
                break;
            }
        }
        return null;
    }

    /**
     * 加减 日期
     * @param temporal
     * @param days
     * @return
     */
    public static Temporal addDay(Temporal temporal,int days) {
        return add(temporal,days,ChronoUnit.DAYS);
    }

    /**
     * 加减月份
     * @param temporal
     * @param months
     * @return
     */
    public static Temporal addMonth(Temporal temporal,int months) {
        return add(temporal,months,ChronoUnit.MONTHS);
    }

    /**
     * 加减年
     * @param temporal
     * @param years
     * @return
     */
    public static Temporal addYear(Temporal temporal,int years) {
        return add(temporal,years,ChronoUnit.YEARS);
    }

    /**
     * 加减小时
     * @param temporal
     * @param hours
     * @return
     */
    public static Temporal addHour(Temporal temporal,long hours) {
        return add(temporal,hours,ChronoUnit.HOURS);
    }

    /**
     * 加减分钟
     * @param temporal
     * @param minutes
     * @return
     */
    public static Temporal addMinute(Temporal temporal,long minutes) {
        return add(temporal,minutes,ChronoUnit.MINUTES);
    }

    /**
     * 加减秒数
     * @param temporal
     * @param seconds
     * @return
     */
    public static Temporal addSecond(Temporal temporal,long seconds) {
        return add(temporal,seconds,ChronoUnit.SECONDS);
    }

    /**
     * 加减时间
     *
     * 如果前面的 年月日时分秒 的加减不能满足你的要求，
     * 请使用这个方法
     * @param temporal
     * @param timeNumber
     * @param chronoUnit
     * @return
     */
    public static Temporal add(Temporal temporal,long timeNumber,ChronoUnit chronoUnit) {
        if (timeNumber > 0) {
            return temporal.plus(timeNumber, chronoUnit);
        } else if (timeNumber == 0) {
            return temporal;
        } else {
            return temporal.minus(Math.abs(timeNumber), chronoUnit);
        }
    }

    /**
     * 判断两个时间段是否有交叉
     *
     * 如果有交叉，返回true
     * @return
     */
    public static <T extends Comparable<T>> boolean cross(@NotNull T from1,@NotNull T to1,@NotNull T from2,@NotNull T to2,boolean containEquals) {
        if (containEquals) {
            return  (to1.compareTo(from2) > 0) && (to2.compareTo(from1) > 0);
        } else {
            return  (to1.compareTo(from2) >= 0) && (to2.compareTo(from1) >= 0);
        }
    }

    /**
     * 判断时间的前后顺序
     * @param t1
     * @param t2
     * @return
     */
    public static <T extends Comparable<T>> boolean judgeFromTo(@NotNull T t1,@NotNull T t2,boolean containEquals) {
        if (containEquals) {
            return (t1.compareTo(t2) <= 0);
        } else {
            return (t1.compareTo(t2) < 0);
        }
    }

    /**
     * 把毫秒级的时间戳转为LocalDateTime
     *
     * 北京时间
     * @param milliSecond
     * @return
     */
    public static LocalDateTime milliSecondStamp2DateTime(long milliSecond) {
        return LocalDateTime.ofEpochSecond(milliSecond / 1000,
                Integer.valueOf(String.valueOf(milliSecond % 1000)) * 100000,
                ZoneOffset.ofHours(8));
    }


    public static void main(String[] args) {

        /*



        //System.out.println(format(currentYearMonth(),DateTimeFormatEnum.DATE_TIME_FORMAT_17));
        //TemporalAccessor temporalAccessor = parse("2018-09-01 01:01:01",DateUtil.DATE_TIME_FORMATTER_4);
        //temporalAccessor.
        LocalDateTime localDateTime = LocalDateTime.parse("2018-09-01 01:01:01",DateUtil.DATE_TIME_FORMATTER_4);
        System.out.println(localDateTime);
        //P20Y10M9D
        //PT182831H40M36S
        //PT10.000534444S
        //PT-1.000005556S
        System.out.println(DateUtil.parsePeriod("P-20Y10M9D"));
        System.out.println(DateUtil.parseDuration("PT182831H40M36S"));


        LocalDateTime localDateTime1 = LocalDateTime.parse("20180901010112",DateUtil.DATE_TIME_FORMATTER_10);
        localDateTime1 = localDateTime1.withNano(12345);
        LocalDateTime localDateTime2 = LocalDateTime.parse("20180901010111",DateUtil.DATE_TIME_FORMATTER_10);
        localDateTime2 = localDateTime2.withNano(6789);
        System.out.println(Duration.between(localDateTime1,localDateTime2));

        System.out.println(DateUtil.format(DateUtil.currentDateTime(), DateTimeFormatEnum.DATE_TIME_FORMAT_9));
        //System.out.println(LocalDateTime.p);

        //LocalTime localTime = LocalTime.
        */

        System.out.println(format(currentDate(),DateTimeFormatEnum.DATE_TIME_FORMAT_18));

        LocalDate localDate = LocalDate.of(2012,Month.FEBRUARY,29);
        System.out.println(DateUtil.addYear(localDate,1));
    }
}
