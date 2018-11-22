package com.zwt.framework.utils.util.date;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @author zwt
 * @detail 日期转换
 * @date 2018/11/22
 * @since 1.0
 */
public class DateConverter {

    /**
     * Java8 LocalDate 对象转 Date对象
     * @param localDate
     * @return
     */
    public static Date localDate2Date(LocalDate localDate){
        //使用ZonedDateTime将LocalDate转换为Instant。
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zdt = localDate.atStartOfDay(zoneId);
        //使用from方法从Instant对象获取Date的实例
        return Date.from(zdt.toInstant());
    }

    /**
     * Date对象 转 Java8 LocalDate 对象
     * @param date
     * @return
     */
    public static LocalDate date2LocalDate(Date date){
        //将Date转换为ZonedDateTime。
        Instant instant = date.toInstant();
        ZoneId zone = ZoneId.systemDefault();
        //使用 ofInstant可以拿到LocalDateTime在转换成LocalDate
        //return LocalDateTime.ofInstant(instant, zone).toLocalDate();
        //或者使用 Instant的atZone()方法返回在指定时区从此Instant生成的ZonedDateTime。
        return instant.atZone(zone).toLocalDate();
    }

    /**
     * Java8 LocalDateTime对象转Date对象
     * @param localDateTime
     * @return
     */
    public static Date localDateTime2Date(LocalDateTime localDateTime){
        //获取zoneId
        ZoneId zone = ZoneId.systemDefault();
        //将localDateTime转换为Instant对象
        Instant instant = localDateTime.atZone(zone).toInstant();
        return Date.from(instant);
    }

    /**
     * Date对象 转 Java8 LocalDateTime对象
     * @param date
     * @return
     */
    public static LocalDateTime date2LocalDateTime(Date date){
        //根据date拿到Instant
        Instant instant = date.toInstant();
        ZoneId zone = ZoneId.systemDefault();
        //转换为LocalDateTime
        return LocalDateTime.ofInstant(instant, zone);
    }

    /**
     * Java 8 LocalTime对象 转Date对象
     * 由于Date对象本身的原因，故取日期为当前日期，时间部分为LocalTime的值
     * @param localTime
     * @return
     */
    public static Date localTime2Date(LocalTime localTime){
        //转换成LocalDateTime，由LocalDateTime进行转换
        LocalDate localDate = LocalDate.now();
        LocalDateTime localDateTime = LocalDateTime.of(localDate, localTime);
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = localDateTime.atZone(zone).toInstant();
        return Date.from(instant);
    }

    /**
     * Date对象 转 Java 8 LocalTime对象
     * @param date
     * @return
     */
    public static LocalTime date2LocalTime(Date date){
        //先转为LocalDateTime对象，在由其转为LocalTime对象
        Instant instant = date.toInstant();
        ZoneId zone = ZoneId.systemDefault();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zone);
        return localDateTime.toLocalTime();
    }

    /**
     * 获取两个日期的时间间隔
     * @param date1
     * @param date2
     * @return
     */
    public static long diffDays(Date date1,Date date2){
        LocalDateTime localDateTime1=date2LocalDateTime(date1);
        LocalDateTime localDateTime2=date2LocalDateTime(date2);
        return Duration.between(localDateTime1,localDateTime2).toDays();
    }

    /**
     * 当期日期增加多少天后的日期，负数为减少多少天
     * @param date
     * @param days
     * @return
     */
    public static Date addDays(Date date,int days){
        LocalDateTime localDateTime1=date2LocalDateTime(date);
        LocalDateTime localDateTime2=localDateTime1.minusDays(days);
        return localDateTime2Date(localDateTime2);
    }


    /**
     * 将日期格式化为指定的格式
     * @param date
     * @return
     */
    public static String localDateTime2String(Date date){
        LocalDateTime localDateTime=date2LocalDateTime(date);
        return DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(localDateTime);
    }

    /**
     * 将string时间格式转化为LocalDateTime
     * @param string
     * @return
     */
    public static LocalDateTime string2LocalDateTime(String string){
        DateTimeFormatter df=DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.parse(string,df);
    }

    /**
     * 判断是否是闰年
     * @param date
     * @return
     */
    public static boolean isLeapYear(Date date){
        LocalDateTime localDateTime=date2LocalDateTime(date);
        return localDateTime.toLocalDate().isLeapYear();
    }


    public static void main(String[] args) {
        System.out.println(localDate2Date(LocalDate.of(2018,11,22)));
        System.out.println(date2LocalDate(new Date()));
        System.out.println(localDateTime2Date(LocalDateTime.of(2018,Month.MARCH,22,14,20,45)));
        System.out.println(date2LocalDateTime(new Date()));
        System.out.println(localTime2Date(LocalTime.of(2,30,44)));
        System.out.println(date2LocalTime(new Date()));
        System.out.println(diffDays(localDateTime2Date(LocalDateTime.of(2018,Month.MARCH,21,14,20,45)),localDateTime2Date(LocalDateTime.of(2018,Month.MARCH,22,10,20,45))));
        System.out.println(localDateTime2String(new Date()));
    }
}
