package com.zerosx.common.core.utils;

import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
public class DateTimeUtil {


    public static final String FORMAT_1 = "yyyy-MM-dd HH:mm:ss";

    public static final String FORMAT_2 = "yyyyMMddHHmmss";

    public static final String FORMAT_3 = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    public static final String FORMAT_4 = "yyyy-MM-dd'T'HH:mm:ss'Z'";

    public static final String FORMAT_5 = "HH:mm";

    public static final String FORMAT_6 = "yyyy-MM-dd";

    public static final String FORMAT_7 = "yyyy-MM-dd'T'HH:mm:ss";

    public static final String FORMAT_8 = "yyyyMMdd";

    public static final String TIME_ZONE_GMT8 = "GMT+8";


    /**
     * 获取几天后的开始时刻
     *
     * @param days 0：当天 负数:前多少天 正数:后多少天
     * @return
     */
    public static String getBeginDayStr(int days) {
        return getBeginDayStr(days, FORMAT_1);
    }

    /**
     * 获取几天后的结束时刻
     *
     * @param days 0：当天 负数:前多少天 正数:后多少天
     * @return yyyy-MM-dd HH:mm:ss
     */
    public static String getEndDayStr(int days) {
        return getEndDayStr(days, FORMAT_1);
    }

    /**
     * 获取几天后的开始时刻
     *
     * @param days 0：当天 负数:前多少天 正数:后多少天
     * @return yyyy-MM-dd HH:mm:ss
     */
    public static String getBeginDayStr(int days, String format) {
        return LocalDateTime.of(LocalDate.now().plusDays(days), LocalTime.MIN).format(DateTimeFormatter.ofPattern(FORMAT_1));
    }

    /**
     * 获取几天后的结束时刻
     *
     * @param days 0：当天 负数:前多少天 正数:后多少天
     * @return 指定格式
     */
    public static String getEndDayStr(int days, String format) {
        return LocalDateTime.of(LocalDate.now().plusDays(days), LocalTime.MAX).format(DateTimeFormatter.ofPattern(FORMAT_1));
    }

    /**
     * 获取几天后的开始时刻
     *
     * @param days 0：当天 负数:前多少天 正数:后多少天
     * @return 指定格式
     */
    public static Date getBeginDate(int days) {
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
    public static Date getEndDate(int days) {
        LocalDateTime todayStart = LocalDateTime.of(LocalDate.now().plusDays(days), LocalTime.MAX);
        Instant instant = todayStart.atZone(ZoneId.systemDefault()).toInstant();
        return Date.from(instant);
    }

    /**
     * 日期转换
     *
     * @param dateStr yyyy-MM-dd HH:mm:ss
     * @return date
     */
    public static Date asDate(String dateStr) {
        return Date.from(LocalDateTime.parse(dateStr, DateTimeFormatter.ofPattern(FORMAT_1)).atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * 日期转换
     *
     * @param dateStr yyyyMMddHHmmss
     * @return date
     */
    public static Date toDate(String dateStr) {
        return Date.from(LocalDateTime.parse(dateStr, DateTimeFormatter.ofPattern(FORMAT_2)).atZone(ZoneId.systemDefault()).toInstant());
    }


    public static String now() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern(FORMAT_1));
    }

    public static String now_2() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern(FORMAT_2));
    }

    public static String now(String format) {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern(format));
    }

    /**
     * 将LocalDateTime转字符串日期
     *
     * @param dateTime
     * @param format
     * @return
     */
    public static String localDateTimeStr(LocalDateTime dateTime, String format) {
        return dateTime.format(DateTimeFormatter.ofPattern(format));
    }

    /**
     * 将LocalDateTime转字符串日期
     *
     * @param dateTime
     * @param dateTime yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String localDateTimeStr(LocalDateTime dateTime) {
        return localDateTimeStr(dateTime, FORMAT_1);
    }




    /**
     * 将yyyyMMddHHmmss转换成yyyy-MM-dd HH:mm:ss
     *
     * @param dateStr yyyyMMddHHmmss
     * @return 解析出错时原样放回
     */
    public static String toDateStr(String dateStr) {
        if (StringUtils.isBlank(dateStr)) {
            return dateStr;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_2);
        SimpleDateFormat dateFormat = new SimpleDateFormat(FORMAT_1);
        try {
            return dateFormat.format(sdf.parse(dateStr));
        } catch (ParseException e) {
            return dateStr;
        }
    }

    /**
     * 将yyyy-MM-dd HH:mm:ss转换成yyyyMMddHHmmss
     *
     * @param dateStr yyyy-MM-dd HH:mm:ss
     * @return 解析出错时原样放回
     */
    public static String toDateStrLe(String dateStr) {
        if (StringUtils.isBlank(dateStr)) {
            return dateStr;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_1);
        SimpleDateFormat dateFormat = new SimpleDateFormat(FORMAT_2);
        try {
            return dateFormat.format(sdf.parse(dateStr));
        } catch (ParseException e) {
            return dateStr;
        }
    }


    /**
     * 将时间格式转换为对应
     *
     * @param date
     * @param dateStr
     * @return
     */
    public static String toDateStrLe(Date date, String dateStr) {
        if (StringUtils.isBlank(dateStr) || date == null) {
            return dateStr;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(dateStr);
        return sdf.format(date);
    }

    /**
     * 将秒转换成--天--时--分
     * Common.dayhourminute = {}天{}小时{}分钟
     * Common.hourminute = {}小时{}分钟
     * common.minute = {}分钟
     *
     * @param chargeTimes 单位：秒
     * @return
     */
    public static String transTime(Integer chargeTimes) {
        int days = chargeTimes / (24 * 60 * 60);//天
        int hours = (chargeTimes % (24 * 60 * 60)) / (60 * 60);//小时
        int minutes = (chargeTimes % (60 * 60)) / (60);//分钟
        if (days > 0) {
            //{}天{}小时{}分钟
            return days + "天" + hours + "小时" + minutes + "分钟";
        }
        if (hours > 0) {
            return hours + "小时" + minutes + "分钟";

        }
        return minutes + "分钟";
    }

    public static String getLocalDateStr(Date date, String formatter) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(formatter);
        Instant instant = date.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zoneId);
        return dateTimeFormatter.format(localDateTime);
    }



}
