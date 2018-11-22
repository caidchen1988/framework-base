package com.zwt.framework.utils.util.java8.test9;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.TimeZone;

/**
 * @author zwt
 * @detail
 * @date 2018/10/12
 * @since 1.0
 */
public class MyTest {
    public static void main(String[] args) {
        //1. 第一部分
        //创建一个日期
        LocalDate date=LocalDate.of(2018,11,22);
        //时分秒
        LocalTime time=LocalTime.of(13,45,20);
        //LocalDateTime对象
        LocalDateTime dateTime=LocalDateTime.of(2018,Month.NOVEMBER,22,13,45,20);

        //2. 第二部分
        //使用localDate和localTime构造一个LocalDateTime
        LocalDateTime dateTime1=LocalDateTime.of(date,time);
        //使用LocalDate构造一个LocalDateTime
        LocalDateTime dateTime2=date.atStartOfDay();//这一天的00:00:00
        LocalDateTime dateTime3=date.atTime(LocalTime.of(12,12,12));//指定这一天的时间
        //使用LocalTime构造LocalDateTime
        LocalDateTime dateTime4=time.atDate(LocalDate.of(2018,11,22));//指定日期
        //通过LocalDateTime获取LocalDate和LocalTime
        LocalDate date1=dateTime.toLocalDate();
        LocalTime time1=dateTime.toLocalTime();

        //3. 第三部分
        //获取日期年份
        int year=dateTime.getYear();
        int year1=dateTime.get(ChronoField.YEAR);
        //获取日期月份
        Month month=dateTime.getMonth();
        int month1=month.getValue();
        int month2=dateTime.get(ChronoField.MONTH_OF_YEAR);
        //获取当月第几天
        int day=dateTime.getDayOfMonth();
        int day1=dateTime.get(ChronoField.DAY_OF_MONTH);
        //获取星期几
        DayOfWeek dow=dateTime.getDayOfWeek();
        //获取该月有几天
        int len=date.lengthOfMonth();
        //获取小时数
        int hour=dateTime.getHour();
        //获取分钟
        int minute=dateTime.getMinute();
        //获取秒数
        int second=dateTime.getSecond();

        //4. 第四部分
        //是不是闰年
        boolean leap=date.isLeapYear();
        //获取当前时间信息
        LocalDateTime localDateTimeNow=LocalDateTime.now();
        LocalDate localDateNow=LocalDate.now();
        LocalTime localTimeNow=LocalTime.now();
        //字符串转时间
        LocalDate localDate1=LocalDate.parse("2014-03-18");
        LocalTime localTime1=LocalTime.parse("13:45:20");
        LocalDateTime localDateTime1=LocalDateTime.parse("2018/11/22 11:22:33", DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
        //时间转换为字符串
        String localDateStr=localDate1.toString();
        String localTimeStr=localTime1.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        String localDateTimeStr=localDateTime1.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        //当前时间减10天
        LocalDateTime localDateTime2=localDateTime1.minusDays(10);
        //当前时间加1年
        LocalDateTime localDateTime3=localDateTime1.plusYears(1);
        //当前时间加1个月
        LocalDateTime localDateTime4=localDateTime1.minus(-1, ChronoUnit.MONTHS);
        //当前时间加1个月
        LocalDateTime localDateTime5=localDateTime1.plus(1, ChronoUnit.MONTHS);
        //更改日期时间，返回新的对象，原对象不会变化
        LocalDate localDate=date1.with(ChronoField.MONTH_OF_YEAR,9);
        //日期时间比较
        boolean flag=localDateTime2.isAfter(localDateTime3);
        boolean flag1=localDateTime2.isBefore(localDateTime3);
        boolean flag2=localDateTime2.isEqual(localDateTime3);

        //5. 第五部分
        //计算两个时间差
        Duration d1=Duration.between(localDateTime2,localDateTime3);
        long days1=d1.toDays();
        Duration d2=Duration.between(localDateTime4,localDateTime5);
        long hours=d2.toHours();
        //计算相差时间，结果10天
        Period period=Period.between(LocalDate.of(2014,3,8),LocalDate.of(2014,3,18));
        int days2=period.getDays();

        //6. 第六部分
        //机器时间
        //以下均表示3s时间
        Instant.ofEpochSecond(3);
        Instant.ofEpochSecond(3,0);
        Instant.ofEpochSecond(2,1000000);
        Instant.ofEpochSecond(4,-1000000);
        //当前时间的时间戳
        Instant.now();
        //localDateTime转换为instant
        Instant instantFormDateTime=localDateTime1.toInstant(ZoneOffset.UTC);
        //获取本地区的zoneId
        ZoneId romeZone= TimeZone.getDefault().toZoneId();
        //instant转localDateTime
        Instant instant1=Instant.now();
        LocalDateTime timeFromInstant=LocalDateTime.ofInstant(instant1,romeZone);
        //将时区设置为欧洲罗马城市。
        ZoneId romeZone1 = ZoneId.of("Europe/Rome");
    }
}
