package com.zerosx.utils;

import org.apache.commons.lang3.StringUtils;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @author javacctvnews
 * @description 日期工具类
 * DateUtil和DateUtils的命名实在太多了
 * @date Created in 2020/12/23 14:30
 * @modify by
 */
public class DateUtils2 {

    public static final String FORMAT_1 = "yyyy-MM-dd HH:mm:ss";

    public static final String FORMAT_2 = "yyyyMMddHHmmss";

    public static final String FORMAT_3 = "yyyy-MM-dd";

    public static final String FORMAT_4 = "yyyyMMdd";

    public static final String TIME_ZONE_GMT8 = "GMT+8";

    /**
     * Date类型格式化为`yyyy-MM-dd HH:mm:ss`格式
     *
     * @param date date
     * @return yyyy-MM-dd HH:mm:ss格式
     */
    public static String formatDate(Date date) {
        return formatDate(date, FORMAT_1);
    }

    /**
     * 将Date类型格式化为指定格式
     *
     * @param date      date
     * @param formatter 格式
     * @return
     */
    public static String formatDate(Date date, String formatter) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(formatter);
        Instant instant = date.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zoneId);
        return dateTimeFormatter.format(localDateTime);
    }

    /**
     * 获取当前时间，格式是`yyyy-MM-dd HH:mm:ss`
     *
     * @return
     */
    public static String now() {
        return now(FORMAT_1);
    }

    /**
     * 获取当前时间，并格式化指定格式
     *
     * @param formatter
     * @return
     */
    public static String now(String formatter) {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern(formatter));
    }

    /**
     * 当前时间或前(整数)或后（负数）多少天的开始时刻，格式是`yyyy-MM-dd HH:mm:ss`
     *
     * @param days 负数是多少天前，正数是多少天后
     * @return
     */
    public static String daysMin(int days) {
        return daysMin(days, FORMAT_1);
    }

    /**
     * 当前时间或前(整数)或后（负数）多少天的开始时刻，指定格式
     *
     * @param days 负数是多少天前，正数是多少天后
     * @return
     */
    public static String daysMin(int days, String formatter) {
        return LocalDateTime.of(LocalDate.now().plusDays(days), LocalTime.MIN).format(DateTimeFormatter.ofPattern(FORMAT_1));
    }

    /**
     * 当前时间或前(整数)或后（负数）多少天的最后时刻，格式是`yyyy-MM-dd HH:mm:ss`
     *
     * @param days 负数是多少天前，正数是多少天后
     * @return
     */
    public static String daysMax(int days) {
        return dayMax(days, FORMAT_1);
    }

    /**
     * 获取几天后的结束时刻
     *
     * @param days 0：当天 负数:前多少天 正数:后多少天
     * @return 指定格式
     */
    public static String dayMax(int days, String formatter) {
        return LocalDateTime.of(LocalDate.now().plusDays(days), LocalTime.MAX).format(DateTimeFormatter.ofPattern(FORMAT_1));
    }


    /**
     * 获取几天后的开始时刻
     *
     * @param days 0：当天 负数:前多少天 正数:后多少天
     * @return 指定格式的Date
     */
    public static Date dateMin(int days) {
        LocalDateTime todayStart = LocalDateTime.of(LocalDate.now().plusDays(days), LocalTime.MIN);
        Instant instant = todayStart.atZone(ZoneId.systemDefault()).toInstant();
        return Date.from(instant);
    }

    /**
     * 获取几天后的结束时刻
     *
     * @param days 0：当天 负数:前多少天 正数:后多少天
     * @return Date
     */
    public static Date dateMax(int days) {
        LocalDateTime todayStart = LocalDateTime.of(LocalDate.now().plusDays(days), LocalTime.MAX);
        Instant instant = todayStart.atZone(ZoneId.systemDefault()).toInstant();
        return Date.from(instant);
    }

    /**
     * 日期转换
     *
     * @param date `yyyy-MM-dd HH:mm:ss`格式的日期
     * @return date
     */
    public static Date date(String date) {
        return date(date, FORMAT_1);
    }

    /**
     * 日期转换
     *
     * @param date `yyyyMMddHHmmss`的日期
     * @return date
     */
    public static Date date(String date, String formatter) {
        return Date.from(LocalDateTime.parse(date, DateTimeFormatter.ofPattern(formatter)).atZone(ZoneId.systemDefault()).toInstant());
    }


    /**
     * 将LocalDateTime转字符串日期
     *
     * @param dateTime LocalDateTime类型日期
     * @param formatter 日期格式
     * @return
     */
    public static String fmtLocalDate(LocalDateTime dateTime, String formatter) {
        return dateTime.format(DateTimeFormatter.ofPattern(formatter));
    }

    /**
     * 将LocalDateTime转字符串日期
     *
     * @param dateTime LocalDateTime类型日期
     * @param dateTime yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String fmtLocalDate(LocalDateTime dateTime) {
        return fmtLocalDate(dateTime, FORMAT_1);
    }

    /**
     * 将yyyyMMddHHmmss转换成yyyy-MM-dd HH:mm:ss
     *
     * @param date yyyyMMddHHmmss
     * @return date
     */
    public static String fmtDate(String date) {
        if (StringUtils.isBlank(date)) {
            return date;
        }
        return formatDate(date(date), FORMAT_2);
    }

    /**
     * 将`yyyy-MM-dd HH:mm:ss` 转换成 `yyyyMMddHHmmss`
     *
     * @param date yyyy-MM-dd HH:mm:ss的日期
     * @return date
     */
    public static String fmtDate2(String date) {
        if (StringUtils.isBlank(date)) {
            return date;
        }
        return formatDate(date(date, FORMAT_2), FORMAT_1);
    }

    /**
     * 将 秒数 转换成 {}天{}小时{}分钟
     *
     * @param seconds 秒数
     * @return {}天{}小时{}分钟
     */
    public static String formatSeconds(Integer seconds) {
        int days = seconds / (24 * 60 * 60);//天
        int hours = (seconds % (24 * 60 * 60)) / (60 * 60);//小时
        int minutes = (seconds % (60 * 60)) / (60);//分钟
        if (days > 0) {
            //{}天{}小时{}分钟
            return days + "天" + hours + "小时" + minutes + "分钟";
        }
        if (hours > 0) {
            return hours + "小时" + minutes + "分钟";

        }
        return minutes + "分钟";
    }

    /*public static void main(String[] args) {
        String fmted = fmtDate("2023-02-01 12:12:32");
        System.out.println("fmted = " + fmted);
        String fmted1 = fmtDate2(fmted);
        System.out.println("fmted1 = " + fmted1);
    }*/

}
