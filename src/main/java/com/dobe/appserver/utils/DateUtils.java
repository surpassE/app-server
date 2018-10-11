package com.dobe.appserver.utils;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 时间工具类jdk8+
 *
 * @author zc.ding
 * @create 2018/10/01
 */
public interface DateUtils {
    int DAY_TIME = 86400;
    String YYYY_MM_DD_HH_MM_SS_SSS = "yyyy-MM-dd HH:mm:ss SSS";
    String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    String YYYY_MM_DD = "yyyy-MM-dd";
    String HH_MM_SS = "HH:mm:ss";
    String HH_MM_SS_SSS = "HH:mm:ss SSS";
    
    /**
    * 北京：东8区
    */
    ZoneOffset ZONE_OFFSET = ZoneOffset.of("+8");
    String START = "START";
    String END = "END";

    /**
     *  获取最早时间
     *  @param date             ：日期
     *  @return java.time.Instant
     *  @date                   ：2018/9/29
     *  @author                 ：zc.ding@foxmail.com
     */
    static Instant getFirstTime(LocalDate date){
        return date.atTime(0, 0, 0).toInstant(ZONE_OFFSET);
    }

    /**
     *  获取最晚时间
     *  @param date             ：日期
     *  @return java.time.Instant
     *  @date                   ：2018/9/29
     *  @author                 ：zc.ding@foxmail.com
     */
    static Instant getLastTime(LocalDate date){
        return date.atTime(23, 59, 59).toInstant(ZONE_OFFSET).plusMillis(999);
    }

    /**
     *  java.util.date -> java.time.LocalDate
     *  @param date              ：日期
     *  @return java.time.LocalDate
     *  @date                    ：2018/9/29
     *  @author                  ：zc.ding@foxmail.com
     */
    static LocalDate toLocalDate(Date date){
        return date.toInstant().atOffset(ZONE_OFFSET).toLocalDate();
    }

    /**
     *  java.util.date -> java.time.LocalDate
     *  @param date              ：日期
     *  @return java.time.LocalDateTime
     *  @date                    ：2018/9/29
     *  @author                  ：zc.ding@foxmail.com
     */
    static LocalDateTime toLocalDateTime(Date date){
        return date.toInstant().atOffset(ZONE_OFFSET).toLocalDateTime();
    }
    
    /**
    *  当前时间默认格式  yyyy-MM-dd HH:mm:ss
    *  @return java.lang.String
    *  @date                    ：2018/9/21
    *  @author                  ：zc.ding@foxmail.com
    */
    static String format(){
        return format(YYYY_MM_DD_HH_MM_SS);
    }
    
    /**
    *  获取当前时间指定格式字符串
    *  @param pattern           ：时间格式
    *  @return java.lang.String
    *  @date                    ：2018/9/21
    *  @author                  ：zc.ding@foxmail.com
    */
    static String format(String pattern){
        return DateTimeFormatter.ofPattern(pattern).format(LocalDateTime.now());
    }

    /**
     *  获取指定时间默认字符串格式
     *  @param date             ：日期
     *  @return java.lang.String
     *  @date                   ：2018/9/21
     *  @author                  ：zc.ding@foxmail.com
     */
    static String format(Date date){
        return DateTimeFormatter.ofPattern(YYYY_MM_DD_HH_MM_SS).format(toLocalDateTime(date));
    }
    
    /**
    *  获取指定时间指定格式字符串
    *  @param date              ：日期
    *  @param pattern           ：时间格式
    *  @return java.lang.String
    *  @date                   ：2018/9/21
    *  @author                  ：zc.ding@foxmail.com
    */
    static String format(Date date, String pattern){
        return DateTimeFormatter.ofPattern(pattern).format(toLocalDateTime(date));
    }

    /**
    *  时间戳转为日期
    *  @param timestamp         ：时间错
    *  @return java.util.Date
    *  @date                    ：2018/10/3
    *  @author                  ：zc.ding@foxmail.com
    */
    static Date parse(long timestamp){
        return Date.from(Instant.ofEpochMilli(timestamp));
    }

    /**
     *  时间戳转为日期格式字符串
     *  @param timestamp         ：时间戳
     *  @return java.util.Date
     *  @date                    ：2018/10/3
     *  @author                  ：zc.ding@foxmail.com
     */
    static String parseString(long timestamp){
        return format(parse(timestamp));
    }

    /**
     *  时间戳转为日期格式字符串
     *  @param timestamp        ：时间戳
     *  @param pattern          ：时间格式
     *  @return java.util.Date
     *  @date                    ：2018/10/3
     *  @author                  ：zc.ding@foxmail.com
     */
    static String parseString(long timestamp, String pattern){
        return format(parse(timestamp), pattern);
    }
    
    /**
    *  字符串转日期, eg: 2000-01-01 00:00:00解析为Date类型
    *  @param date              ：日期
    *  @return java.util.Date
    *  @date                   ：2018/9/21
    *  @author                  ：zc.ding@foxmail.com
    */
    static Date parse(String date){
        return parse(date, YYYY_MM_DD_HH_MM_SS);
    }
    
    /**
    *  将时间转为指定pattern格式的Date数据
    *  @param date              ：日期
    *  @param pattern           ：日期格式
    *  @return java.util.Date
    *  @date Date               ：2018/9/21
    *  @author                  ：zc.ding@foxmail.com
    */
    static Date parse(String date, String pattern){
        return Date.from(LocalDateTime.parse(date, DateTimeFormatter.ofPattern(pattern)).toInstant(ZONE_OFFSET));
    }

    /**
     *  当前时间加减天数， days正负代表加减
     *  <p>
     *      2018-01-01 12:23:30 123-> +1天 2018-01-02 12:23:30 123
     *  </p>  
     *  @param days             ：天数
     *  @return java.util.Date
     *  @date                   ：2018/9/28
     *  @author                 ：zc.ding@foxmail.com
     */
    static Date addDays(Integer days){
        return Date.from(LocalDateTime.now().plusDays(days).toInstant(ZONE_OFFSET));
    }
    
    /**
    *  指定时间加减天数， days正负代表加减
    *  <p>
    *      2018-01-01 12:23:30 123-> +1天 2018-01-02 12:23:30 123
    *  </p>  
    *  @param date              ：日期
    *  @param days              ：天数
    *  @return java.util.Date
    *  @date                    ：2018/9/28
    *  @author                  ：zc.ding@foxmail.com
    */
    static Date addDays(Date date, Integer days){
        return Date.from(toLocalDateTime(date).plusDays(days).toInstant(ZONE_OFFSET));
    }
    
    /**
    *  指定时间加减天数，返回结果的最早时间，days正负代表加减
    *  <p>
    *     2018-01-01 12:23:30 -> +1天 2018-01-02 00:00:00
    *  </p>
    *  @param date
    *  @param days
    *  @return java.util.Date
    *  @date                    ：2018/9/29
    *  @author                  ：zc.ding@foxmail.com
    */
    static Date addDaysFirst(Date date, Integer days){
        return Date.from(getFirstTime(toLocalDate(date).plusDays(days)));
    }

    /**
     *  指定时间加减天数，返回结果的最晚时间，days正负代表加减
     *  <p>
     *     2018-01-01 12:23:30 -> +1天 2018-01-02 23:59:59 999
     *  </p>
     *  @param date
     *  @param days
     *  @return java.util.Date
     *  @date                    ：2018/9/29
     *  @author                  ：zc.ding@foxmail.com
     */
    static Date addDaysLast(Date date, Integer days){
        return Date.from(getLastTime(toLocalDate(date).plusDays(days)));
    }
    
    /**
    *  获取指定时间的最早时间
    *  @param date
    *  @return java.util.Date
    *  @date                    ：2018/9/29
    *  @author                  ：zc.ding@foxmail.com
    */
    static Date getDayFirst(Date date){
        return addDaysFirst(date, 0);
    }

    /**
    *  获取指定时间的最早时间 时间格式：yyyy-MM-dd HH:mm:ss
    *  @param date
    *  @return java.lang.String
    *  @date                    ：2018/9/29
    *  @author                  ：zc.ding@foxmail.com
    */
    static String getDayFirstString(Date date){
        return format(getDayFirst(date), YYYY_MM_DD_HH_MM_SS);
    }

    /**
     *  获取指定时间的最早时间, 指定时间格式
     *  @param date
     *  @param pattern
     *  @return java.lang.String
     *  @date                    ：2018/9/29
     *  @author                  ：zc.ding@foxmail.com
     */
    static String getDayFirst(Date date, String pattern){
        return format(getDayFirst(date), pattern);
    }

    /**
    *  获取指定时间的最晚时间
    *  @param date
    *  @return java.util.Date
    *  @date                    ：2018/9/29
    *  @author                  ：zc.ding@foxmail.com
    */
    static Date getDayLast(Date date){
        return addDaysLast(date, 0);
    }

    /**
     *  获取指定时间的最晚时间 时间格式：yyyy-MM-dd HH:mm:ss
     *  @param date
     *  @return java.util.Date
     *  @date                    ：2018/9/29
     *  @author                  ：zc.ding@foxmail.com
     */
    static String getDayLastString(Date date){
        return format(getDayLast(date), YYYY_MM_DD_HH_MM_SS);
    }

    /**
     *  获取指定时间的最晚时间，指定时间格式
     *  @param date
     *  @param pattern
     *  @return java.util.Date
     *  @date                    ：2018/9/29
     *  @author                  ：zc.ding@foxmail.com
     */
    static String getDayLast(Date date, String pattern){
        return format(getDayLast(date), pattern);
    }

    /**
    *  当前时间加减月数
    *  @param months
    *  @return java.util.Date
    *  @date                    ：2018/9/29
    *  @author                  ：zc.ding@foxmail.com
    */
    static Date addMonth(Integer months){
        return Date.from(LocalDateTime.now().plusMonths(months).toInstant(ZONE_OFFSET));
    }
    
    /**
    *  指定时间加减月数
    *  @param date
    *  @param months
    *  @return java.util.Date
    *  @date                    ：2018/9/29
    *  @author                  ：zc.ding@foxmail.com
    */
    static Date addMonth(Date date, Integer months){
        return Date.from(toLocalDateTime(date).plusMonths(months).toInstant(ZONE_OFFSET));
    }

    /**
     *  指定时间加减月数，并返回结果的当天最早时间
     *  <p>
     *      2018-01-01 10:10:10 -> +2月 2018-03-01 00:00:00
     *  </p>
     *  @param date
     *  @param months
     *  @return java.util.Date
     *  @date                    ：2018/9/29
     *  @author                  ：zc.ding@foxmail.com
     */
    static Date addMonthFirst(Date date, Integer months){
        return getDayFirst(addMonth(date, months));
    }

    /**
    *  指定时间加减月数，并返回结果的当天最早时间
    *  <p>
    *      2018-01-01 10:10:10 -> +2月 2018-03-01 23:59:59 999
    *  </p>
    *  @param date
    *  @param months
    *  @return java.util.Date
    *  @date                    ：2018/9/29
    *  @author                  ：zc.ding@foxmail.com
    */
    static Date addMonthLast(Date date, Integer months){
        return getDayLast(addMonth(date, months));
    }

    /**
    *  获得当前时间的本月第一天
    * 
    *  @return java.util.Date
    *  @date                    ：2018/9/29
    *  @author                  ：zc.ding@foxmail.com
    */
    static Date getMonthFirstDay(){
        return Date.from(getFirstTime(LocalDate.now().with(TemporalAdjusters.firstDayOfMonth())));
    }

    /**
     *  获得当前时间的本月第一天
     *
     *  @param date
     *  @return java.util.Date
     *  @date                    ：2018/9/29
     *  @author                  ：zc.ding@foxmail.com
     */
    static Date getMonthFirstDay(Date date) {
        return Date.from(getFirstTime(toLocalDate(date).with(TemporalAdjusters.firstDayOfMonth())));
    }

    /**
     *  获得当前时间的本月第一天
     *
     *  @return java.util.Date
     *  @date                    ：2018/9/29
     *  @author                  ：zc.ding@foxmail.com
     */
    static String getMonthFirstDayString(){
        return format(getMonthFirstDay(), YYYY_MM_DD_HH_MM_SS);
    }

    /**
     *  获得当前时间的本月第一天
     *
     *  @param date
     *  @return java.lang.String
     *  @date                    ：2018/9/29
     *  @author                  ：zc.ding@foxmail.com
     */
    static String getMonthFirstDayString(Date date){
        return format(getMonthFirstDay(date), YYYY_MM_DD_HH_MM_SS);
    }

    /**
     *  获得当前时间的本月第一天
     *  @param date
     *  @param pattern
     *  @return java.lang.String
     *  @date                    ：2018/9/29
     *  @author                  ：zc.ding@foxmail.com
     */
    static String getMonthFirstDayString(Date date, String pattern){
        return format(getMonthFirstDay(date), pattern);
    }

    /**
     *  获得当前时间月份最后一天
     *
     *  @return java.util.Date
     *  @date                    ：2018/9/29
     *  @author                  ：zc.ding@foxmail.com
     */
    static Date getMonthLastDay(){
        return Date.from(getLastTime(LocalDate.now().with(TemporalAdjusters.lastDayOfMonth())));
    }
    
    /**
    *  获得指定时间月份最后一天
    *  @param date
    *  @return java.util.Date
    *  @date                    ：2018/9/29
    *  @author                  ：zc.ding@foxmail.com
    */
    static Date getMonthLastDay(Date date){
        return Date.from(getLastTime(toLocalDate(date).with(TemporalAdjusters.lastDayOfMonth())));
    }
    
    /**
    *  获得当前时间月份最后一天
    *  @return java.lang.String
    *  @date                    ：2018/9/29
    *  @author                  ：zc.ding@foxmail.com
    */
    static String getMonthLastDayString(){
        return format(Date.from(getLastTime(LocalDate.now().with(TemporalAdjusters.lastDayOfMonth()))), YYYY_MM_DD_HH_MM_SS);
    }

    /**
    *  获得当前时间月份最后一天
    *  @param date
    *  @return java.lang.String
    *  @date                    ：2018/9/29
    *  @author                  ：zc.ding@foxmail.com
    */
    static String getMonthLastDayString(Date date){
        return format(Date.from(getLastTime(toLocalDate(date).with(TemporalAdjusters.lastDayOfMonth()))), YYYY_MM_DD_HH_MM_SS);
    }

    /**
    *  获得当前时间月份最后一天
    *  @param date
    *  @param pattern
    *  @return java.lang.String
    *  @date                    ：2018/9/29
    *  @author                  ：zc.ding@foxmail.com
    */
    static String getMonthLastDayString(Date date, String pattern){
        return format(Date.from(getLastTime(toLocalDate(date).with(TemporalAdjusters.lastDayOfMonth()))), pattern);
    }
    
    /**
    *  判断两个时间是不是同一天
    *  @param srcDate
    *  @param destDate
    *  @return boolean
    *  @date                    ：2018/10/2
    *  @author                  ：zc.ding@foxmail.com
    */
    static boolean isSameDay(Date srcDate, Date destDate) {
        return toLocalDate(srcDate).isEqual(toLocalDate(destDate));
    }
    
    /**
    *  判断两个时间是不是同一个月
    *  @param srcDate
    *  @param destDate
    *  @return boolean
    *  @date                    ：2018/10/2
    *  @author                  ：zc.ding@foxmail.com
    */
    static boolean isSameMonth(Date srcDate, Date destDate) {
        return toLocalDate(srcDate).getMonthValue() == toLocalDate(destDate).getMonthValue();
    }

    /**
     *  当前时间到指定时间天数, 含有正负标识，未包含当前天数, 过去的时间显示为负数
     *  <p>
     *      2018-09-30 -> 2018-09-20  间隔-10天
     *  </p>
     *  @param date
     *  @return int
     *  @date                    ：2018/10/2
     *  @author                  ：zc.ding@foxmail.com
     */
    static int getDays(Date date){
        return new Long(LocalDate.now().until(toLocalDate(date), ChronoUnit.DAYS)).intValue();
    }
    
    /**
    *  当前时间到指定时间天数, 返回正整数天数，未包含当前天数
    *  <p>
    *      2018-09-20 -> 2018-09-30  间隔10天
    *  </p>
    *  @param date
    *  @return int
    *  @date                    ：2018/10/2
    *  @author                  ：zc.ding@foxmail.com
    */
    static int getDaysAbs(Date date){
        return Math.abs(getDays(date));
    }

    /**
     *  当前时间到指定时间天数, 含有正负标识，包含当前天数, 过去的时间显示为负数
     *  <p>
     *      2018-09-20 -> 2018-09-30  间隔11天
     *  </p>
     *  @param date
     *  @return int
     *  @date                    ：2018/10/2
     *  @author                  ：zc.ding@foxmail.com
     */
    static int getDaysInclude(Date date){
        int days = getDays(date);
        return  days + (days < 0 ? -1 : 1);
    }
    
    /**
     *  当前时间到指定时间天数, 返回正整数天数，包含当前天数
     *  <p>
     *      2018-09-20 -> 2018-09-30  间隔11天
     *  </p>
     *  @param date
     *  @return int
     *  @date                    ：2018/10/2
     *  @author                  ：zc.ding@foxmail.com
     */
    static int getDaysAbsInclude(Date date){
        return getDaysAbs(date) + 1;
    }

    /**
     *  当前时间到指定时间天数, 含有正负标识，未包含当前天数, 过去的时间显示为负数
     *  <p>
     *      2018-09-20 -> 2018-09-30  间隔10天
     *  </p>  
     *  @param date
     *  @return int
     *  @date                    ：2018/10/2
     *  @author                  ：zc.ding@foxmail.com
     */
    static int getDays(String date) {
        return getDays(parse(date));
    }

    /**
     *  当前时间到指定时间天数, 返回正整数天数，未包含当前天数
     *  <p>
     *      2018-09-20 -> 2018-09-30  间隔10天
     *  </p>  
     *  @param date
     *  @return int
     *  @date                    ：2018/10/2
     *  @author                  ：zc.ding@foxmail.com
     */
    static int getAbsDays(String date) {
        return Math.abs(getDays(parse(date)));
    }

    /**
     *  当前时间到指定时间天数, 含有正负标识，包含当前天数, 过去的时间显示为负数
     *  <p>
     *      2018-09-20 -> 2018-09-30  间隔11天
     *  </p>  
     *  @param date
     *  @return int
     *  @date                    ：2018/10/2
     *  @author                  ：zc.ding@foxmail.com
     */
    static int getDaysInclude(String date) {
        return getDaysInclude(parse(date));
    }

    /**
     *  当前时间到指定时间天数, 返回正整数天数，包含当前天数
     *  <p>
     *      2018-09-20 -> 2018-09-30  间隔11天
     *  </p>  
     *  @param date
     *  @return int
     *  @date                    ：2018/10/2
     *  @author                  ：zc.ding@foxmail.com
     */
    static int getDaysAbsInclude(String date) {
        return getDaysAbsInclude(parse(date));
    }
    
    /**
    *  start时间到end时间间隔天数，含有正负标识， 未包含end当天, 过去的时间显示为负数
    *  @param start
    *  @param end
    *  @return int
    *  @date                    ：2018/10/2
    *  @author                  ：zc.ding@foxmail.com
    */
    static int getDays(Date start, Date end){
        return new Long(toLocalDate(start).until(toLocalDate(end), ChronoUnit.DAYS)).intValue();
    }

    /**
     *  start时间到end时间间隔天数，返回正整数， 未包含end当天
     *  @param start
     *  @param end
     *  @return int
     *  @date                    ：2018/10/2
     *  @author                  ：zc.ding@foxmail.com
     */
    static int getDaysAbs(Date start, Date end){
        return Math.abs(getDays(start, end));
    }

    /**
     *  start时间到end时间间隔天数，含有正负标识， 包含end当天, 过去的时间显示为负数
     *  @param start
     *  @param end
     *  @return int
     *  @date                    ：2018/10/2
     *  @author                  ：zc.ding@foxmail.com
     */
    static int getDaysInclude(Date start, Date end){
        int days = getDays(start, end);
        return days + (days < 0 ? -1 : 1);
    }

    /**
     *  start时间到end时间间隔天数，返回正整数， 包含end当天
     *  @param start
     *  @param end
     *  @return int
     *  @date                    ：2018/10/2
     *  @author                  ：zc.ding@foxmail.com
     */
    static int getDaysAbsInclude(Date start, Date end){
        return getDaysAbs(start, end) + 1;
    }

    /**
     *  start时间到end时间间隔天数，含有正负标识， 未包含end当天, 字符串类型入参
     *  @param start
     *  @param end
     *  @return int
     *  @date                    ：2018/10/2
     *  @author                  ：zc.ding@foxmail.com
     */
    static int getDays(String start, String end){
        return getDays(parse(start), parse(end));
    }

    /**
     *  start时间到end时间间隔天数，返回正整数， 未包含end当天, 字符串类型入参
     *  @param start
     *  @param end
     *  @return int
     *  @date                    ：2018/10/2
     *  @author                  ：zc.ding@foxmail.com
     */
    static int getDaysAbs(String start, String end){
        return Math.abs(getDays(start, end));
    }

    /**
     *  start时间到end时间间隔天数，含有正负标识， 包含end当天, 字符串类型入参
     *  @param start
     *  @param end
     *  @return int
     *  @date                    ：2018/10/2
     *  @author                  ：zc.ding@foxmail.com
     */
    static int getDaysInclude(String start, String end){
        return getDaysInclude(parse(start), parse(end));
    }

    /**
     *  start时间到end时间间隔天数，返回正整数， 包含end当天, 字符串类型入参
     *  @param start
     *  @param end
     *  @return int
     *  @date                    ：2018/10/2
     *  @author                  ：zc.ding@foxmail.com
     */
    static int getDaysAbsInclude(String start, String end){
        return Math.abs(getDaysInclude(start, end));
    }
    
    /**
    *  判断当前时间是否在start之后end之前
    *  @param start
    *  @param end
    *  @return boolean
    *  @date                    ：2018/10/2
    *  @author                  ：zc.ding@foxmail.com
    */
    static  boolean valid(Date start, Date end){
        return Instant.now().isBefore(end.toInstant()) && Instant.now().isAfter(start.toInstant());
    }

    /**
     *  判断当前时间是否在start之后end之前
     *  @param start
     *  @param end
     *  @return boolean
     *  @date                    ：2018/10/2
     *  @author                  ：zc.ding@foxmail.com
     */
    static boolean valid(String start, String end){
        return valid(parse(start), parse(end));
    }
    
    /**
    *  获取当前时间是第几季度
    *  @return int
    *  @date                    ：2018/10/2
    *  @author                  ：zc.ding@foxmail.com
    */
    static int getQuarter(){
        Integer month = LocalDate.now().getMonthValue();
        return (month / 3) + ((month % 3) > 0 ? 1 : 0);
    }
    
    /**
    *  计算周期是时间  {@link com.dobe.appserver.utils.DateUtils.PeriodEnum}
    *  @param periodEnum        : 周期类型
    *  @return java.util.Map<java.lang.String,java.util.Date>
    *  @date                    ：2018/10/2
    *  @author                  ：zc.ding@foxmail.com
    */
    static Map<String, Date> getPeriod(PeriodEnum periodEnum){
        Map<String, Date> map = new HashMap<>(2);
        Instant instant = Instant.now();
        LocalDate localDate = LocalDate.from(instant.atOffset(ZONE_OFFSET));
        Date date = Date.from(instant);
        int mod = 0;
        switch (periodEnum){
            case LATEST_1_DAYS:
                map.put(START, addDaysFirst(date, -1));
                map.put(END, date);
                break;
            case LATEST_3_DAYS:
                map.put(START, addDaysFirst(date, -3));
                map.put(END, date);
                break;
            case LATEST_7_DAYS:
                map.put(START, addDaysFirst(date, -7));
                map.put(END, date);
                break;
            case LATEST_30_DAYS:
                map.put(START, addDaysFirst(date, -30));
                map.put(END, date);
                break;
            case CURR_WEEK:
                map.put(START, addDaysFirst(date, -localDate.getDayOfWeek().getValue() + 1));
                map.put(END, date);
                break;
            case PRE_WEEK:
                map.put(START, addDaysFirst(date, -localDate.getDayOfWeek().getValue() - 6));
                map.put(END, addDaysLast(date, -localDate.getDayOfWeek().getValue()));
                break;
            case CURR_MONTH:
                map.put(START, addDaysFirst(date, -localDate.getDayOfMonth() + 1));
                map.put(END, date);
                break;
            case PRE_MONTH:
                LocalDate preLocalDate = localDate.plusMonths(-1);
                map.put(START, addDaysFirst(addMonth(date, -1), -preLocalDate.getDayOfMonth() + 1));
                map.put(END, addDaysLast(map.get(START), preLocalDate.getMonth().maxLength() - 1));
                break;
            case CURR_QUARTER:
                /*
                * 如果余数为0说明是在季度的最后一个月，例如 3,6,9,12月
                */
                mod = localDate.getMonthValue() % 3;
                map.put(START, getMonthFirstDay(addMonth(date, -(mod == 0 ? 3 : mod) + 1)));
                map.put(END, date);
                break;
            case PRE_QUARTER:
                mod = localDate.getMonthValue() % 3;
                map.put(START, addMonth(getMonthFirstDay(addMonth(date, -(mod == 0 ? 3 : mod) + 1)), -3));
                map.put(END, getMonthLastDay(addMonth(map.get(START), 2)));
                break;
            case CURR_YEAR:
                map.put(START, addDaysFirst(date, -localDate.getDayOfYear() + 1));
                map.put(END, date);
                break;
            default:break;
        }
        return map;
    }

    /**
     *  时间周期枚举
     *  @date                    ：2018/10/2
     *  @author                  ：zc.ding@foxmail.com
     */
    public enum PeriodEnum {
        /**
         * 昨天（最近1天）
         */
        LATEST_1_DAYS(1),
        /**
         * 最近3天
         */
        LATEST_3_DAYS(2),
        /**
         * 最近7天
         */
        LATEST_7_DAYS(3),
        /**
         * 最近30天
         */
        LATEST_30_DAYS(4),
        /**
         * 当前周
         */
        CURR_WEEK(11),
        /**
         * 上一周
         */
        PRE_WEEK(12),
        /**
         * 当天月
         */
        CURR_MONTH(21),
        /**
         * 上一月
         */
        PRE_MONTH(22),
        /**
         * 当前季度
         */
        CURR_QUARTER(31),
        /**
         * 上一季度
         */
        PRE_QUARTER(32),
        /**
         * 本年
         */
        CURR_YEAR(100);

        private int type;

        PeriodEnum(int type){
            this.type = type;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        /**
         *  获取时间周期类型
         *  @Method_Name             ：getPeriodEnum
         *  @param period
         *  @return com.yirun.framework.core.enums.PeriodEnum
         *  @date                    ：2018/10/2
         *  @author                  ：zc.ding@foxmail.com
         */
        public static PeriodEnum getPeriodEnum(Integer period){
            for(PeriodEnum  pe : PeriodEnum.values()){
                if(period.equals(pe.getType())){
                    return pe;
                }
            }
            return PeriodEnum.LATEST_1_DAYS;
        }
    }


    public static void main(String[] args) {
        LocalDate localDate = LocalDate.now();
        LocalDate now = LocalDate.now();
        System.out.println(localDate.until(now).getDays());
        System.out.println("========分隔符================");
        System.out.println(DateUtils.format());
        System.out.println(format(YYYY_MM_DD_HH_MM_SS_SSS));
        System.out.println(format(new Date(), YYYY_MM_DD_HH_MM_SS_SSS));
        System.out.println("=========== 添加天数 ========");
        System.out.println(format(addDays(new Date(), -10), YYYY_MM_DD_HH_MM_SS_SSS));
        System.out.println(format(addDaysFirst(new Date(), 1)));
        System.out.println(format(addDaysLast(new Date(), 1), YYYY_MM_DD_HH_MM_SS_SSS));

        System.out.println("=========== 获得指定时间的最早、最晚时间 ========");
        System.out.println(getDayFirst(new Date()));
        System.out.println(getDayFirstString(new Date()));
        System.out.println(getDayFirst(new Date(), YYYY_MM_DD_HH_MM_SS_SSS));
        System.out.println(getDayLast(new Date()));
        System.out.println(getDayLastString(new Date()));
        System.out.println(getDayLast(new Date(), YYYY_MM_DD_HH_MM_SS_SSS));

        System.out.println("=========== 计算月份 ========");
        System.out.println(format(addMonth(1)));
        System.out.println(getMonthLastDayString());
        System.out.println(getMonthFirstDayString(new Date(), YYYY_MM_DD_HH_MM_SS_SSS));
        System.out.println("是否为同一天" + isSameDay(new Date(), parse("2018-10-01 00:00:00")));
        System.out.println("是否为同一个月" + isSameMonth(new Date(), parse("2018-09-01 00:00:00")));

        System.out.println("=========== 计算天数 ========");
        System.out.println(getDays("2018-09-20 00:00:00"));
        System.out.println(getDaysInclude("2018-09-20 00:00:00"));
        System.out.println(getDaysAbsInclude("2018-09-20 00:00:00"));
        System.out.println(getDaysInclude("2018-09-20 00:00:00", "2018-09-24 00:00:00"));
        System.out.println(getDaysInclude("2018-09-24 00:00:00", "2018-09-20 00:00:00"));

        System.out.println("=========== 计算周期时间 ========");
        System.out.println(getQuarter());
        System.out.println(format(getPeriod(PeriodEnum.CURR_WEEK).get(START)));
        System.out.println(format(getPeriod(PeriodEnum.PRE_WEEK).get(START)));
        System.out.println(format(getPeriod(PeriodEnum.PRE_WEEK).get(END)));
        System.out.println(format(getPeriod(PeriodEnum.CURR_MONTH).get(START)));
        System.out.println(format(getPeriod(PeriodEnum.PRE_MONTH).get(START)));
        System.out.println(format(getPeriod(PeriodEnum.PRE_MONTH).get(END)));
        System.out.println(format(getPeriod(PeriodEnum.CURR_QUARTER).get(START)));
        System.out.println(format(getPeriod(PeriodEnum.CURR_QUARTER).get(END)));
        System.out.println(format(getPeriod(PeriodEnum.PRE_QUARTER).get(START)));
        System.out.println(format(getPeriod(PeriodEnum.PRE_QUARTER).get(END)));

        System.out.println(format(getPeriod(PeriodEnum.CURR_YEAR).get(START)));
        System.out.println(format(getPeriod(PeriodEnum.CURR_YEAR).get(END)));
    }
    
}
