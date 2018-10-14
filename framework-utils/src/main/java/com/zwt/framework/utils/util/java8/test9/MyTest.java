package com.zwt.framework.utils.util.java8.test9;

import java.time.*;
import java.time.temporal.ChronoField;
import java.util.TimeZone;

/**
 * @author zwt
 * @detail
 * @date 2018/10/12
 * @since 1.0
 */
public class MyTest {
    public static void main(String[] args) {
        //创建一个日期
        LocalDate date=LocalDate.of(2014,3,18);
        //获取日期年份
        int year=date.getYear();
        //获取日期月份
        Month month=date.getMonth();
        //获取当月第几天
        int day=date.getDayOfMonth();
        //获取星期几
        DayOfWeek dow=date.getDayOfWeek();
        //获取该月有几天
        int len=date.lengthOfMonth();
        //是不是闰年
        boolean leap=date.isLeapYear();
        //获取当前日期
        LocalDate today=LocalDate.now();
        //获取年月日
        int year1=date.get(ChronoField.YEAR);
        int month1=date.get(ChronoField.MONTH_OF_YEAR);
        int day1=date.get(ChronoField.DAY_OF_MONTH);

        //时分秒
        LocalTime time=LocalTime.of(13,45,20);
        int hour=time.getHour();
        int minute=time.getMinute();
        int second=time.getSecond();

        //字符串转时间
        LocalDate date1=LocalDate.parse("2014-03-18");
        LocalTime time1=LocalTime.parse("13:45:20");

        //LocalDateTime对象
        LocalDateTime dt1=LocalDateTime.of(2014,Month.MARCH,18,13,45,20);
        LocalDateTime dt2=LocalDateTime.of(date,time);
        LocalDateTime dt3=date.atTime(13,45,20);
        LocalDateTime dt4=date.atTime(time);
        LocalDateTime dt5=time.atDate(date);

        LocalDate date2=dt1.toLocalDate();
        LocalTime time2=dt2.toLocalTime();

        //机器时间
        Instant.ofEpochSecond(3);
        Instant.ofEpochSecond(3,0);
        Instant.now();
        Instant.ofEpochMilli(0);
        Instant.ofEpochSecond(2,1000000);
        Instant.ofEpochSecond(4,-1000000);

        Duration d1=Duration.between(time1,time2);
        Duration d2=Duration.between(date1,date2);

        //时间间隔
        Period tenDays=Period.between(LocalDate.of(2014,3,8),LocalDate.of(2014,3,18));

        //更改日期时间，返回新的对象，原对象不会变化
        LocalDate date3=date1.with(ChronoField.MONTH_OF_YEAR,9);

        ZoneId romeZone= TimeZone.getDefault().toZoneId();
        //datetime转换为instant
        Instant instantFormDateTime=dt1.toInstant(ZoneOffset.UTC);
        //instant转datetime
        Instant instant1=Instant.now();
        LocalDateTime timeFromInstant=LocalDateTime.ofInstant(instant1,romeZone);



    }
}
