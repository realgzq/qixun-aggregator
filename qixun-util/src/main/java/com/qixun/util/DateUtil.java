package com.qixun.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 *  Created by guozq on 2015/9/9.
 *
 *  主要包含以下几个方面的功能
 *  1. 字符串转日期， 日期转字符串
 *  2. 日期的截取，只得到固定单位上的值
 *  3. 日期的加减，在日期的某个单位上加上固定的值
 *  4. 得到本月的第一天，下月的第一天，本月的最后一天
 *  5. 两个时间之间在规定单位上的计算
 */
public class DateUtil {
    public static final String YEAR = "YEAR";
    public static final String MONTH = "MONTH";
    public static final String DAY = "DAY";
    public static final String WEEK = "WEEK";
    public static final String HOUR = "HOUR";
    public static final String MINUTE = "MINUTE";
    public static final String SECOND = "SECOND";
    public static final String QUARTER = "QUARTER";
    public static final String FRAC_SECOND ="FRAC_SECOND";


    public static final String YYYY_MM="yyyy-MM";
    public static final String YYYY_MM_DD="yyyy-MM-dd";
    public static final String YYYY_MM_DD_HH_MM="yyyy-MM-dd HH:mm";
    public static final String YYYY_MM_DD_HH_MM_SS="yyyy-MM-dd HH:mm:ss";
    public static final String YYYY_MM_EN="yyyy/MM";
    public static final String YYYY_MM_DD_EN="yyyy/MM/dd";
    public static final String YYYY_MM_DD_HH_MM_EN="yyyy/MM/dd HH:mm";
    public static final String YYYY_MM_DD_HH_MM_SS_EN="yyyy/MM/dd HH:mm:ss";
    public static final String YYYY_MM_CN="yyyy年MM月";
    public static final String YYYY_MM_DD_CN="yyyy年MM月dd日";
    public static final String YYYY_MM_DD_HH_MM_CN="yyyy年MM月dd日 HH:mm";
    public static final String YYYY_MM_DD_HH_MM_SS_CN="yyyy年MM月dd日 HH:mm:ss";
    public static final String HH_MM="HH:mm";
    public static final String HH_MM_SS="HH:mm:ss";
    public static final String MM_DD="MM-dd";
    public static final String MM_DD_HH_MM="MM-dd HH:mm";
    public static final String MM_DD_HH_MM_SS="MM-dd HH:mm:ss";
    public static final String MM_DD_EN="MM/dd";
    public static final String MM_DD_HH_MM_EN="MM/dd HH:mm";
    public static final String MM_DD_HH_MM_SS_EN="MM/dd HH:mm:ss";
    public static final String MM_DD_CN="MM月dd日";
    public static final String MM_DD_HH_MM_CN="MM月dd日 HH:mm";
    public static final String MM_DD_HH_MM_SS_CN="MM月dd日 HH:mm:ss";


    private static final Map<String, String> DATE_FORMAT_REGEXPS = new HashMap<String, String>() {{
        put("^\\d{8}$", "yyyyMMdd");
        put("^\\d{1,2}-\\d{1,2}-\\d{4}$", "dd-MM-yyyy");
        put("^\\d{4}-\\d{1,2}-\\d{1,2}$", "yyyy-MM-dd");
        put("^\\d{1,2}/\\d{1,2}/\\d{4}$", "MM/dd/yyyy");
        put("^\\d{4}/\\d{1,2}/\\d{1,2}$", "yyyy/MM/dd");
        put("^\\d{1,2}\\s[a-z]{3}\\s\\d{4}$", "dd MMM yyyy");
        put("^\\d{1,2}\\s[a-z]{4,}\\s\\d{4}$", "dd MMMM yyyy");
        put("^\\d{12}$", "yyyyMMddHHmm");
        put("^\\d{8}\\s\\d{4}$", "yyyyMMdd HHmm");
        put("^\\d{1,2}-\\d{1,2}-\\d{4}\\s\\d{1,2}:\\d{2}$", "dd-MM-yyyy HH:mm");
        put("^\\d{4}-\\d{1,2}-\\d{1,2}\\s\\d{1,2}:\\d{2}$", "yyyy-MM-dd HH:mm");
        put("^\\d{1,2}/\\d{1,2}/\\d{4}\\s\\d{1,2}:\\d{2}$", "MM/dd/yyyy HH:mm");
        put("^\\d{4}/\\d{1,2}/\\d{1,2}\\s\\d{1,2}:\\d{2}$", "yyyy/MM/dd HH:mm");
        put("^\\d{1,2}\\s[a-z]{3}\\s\\d{4}\\s\\d{1,2}:\\d{2}$", "dd MMM yyyy HH:mm");
        put("^\\d{1,2}\\s[a-z]{4,}\\s\\d{4}\\s\\d{1,2}:\\d{2}$", "dd MMMM yyyy HH:mm");
        put("^\\d{14}$", "yyyyMMddHHmmss");
        put("^\\d{8}\\s\\d{6}$", "yyyyMMdd HHmmss");
        put("^\\d{1,2}-\\d{1,2}-\\d{4}\\s\\d{1,2}:\\d{2}:\\d{2}$", "dd-MM-yyyy HH:mm:ss");
        put("^\\d{4}-\\d{1,2}-\\d{1,2}\\s\\d{1,2}:\\d{2}:\\d{2}$", "yyyy-MM-dd HH:mm:ss");
        put("^\\d{1,2}/\\d{1,2}/\\d{4}\\s\\d{1,2}:\\d{2}:\\d{2}$", "MM/dd/yyyy HH:mm:ss");
        put("^\\d{4}/\\d{1,2}/\\d{1,2}\\s\\d{1,2}:\\d{2}:\\d{2}$", "yyyy/MM/dd HH:mm:ss");
    }};


    /**
     * 根据提供日期的得到 SimpleDateFormat 格式 如果没有匹配成功返回空，如果返回成功，返回指定的格式
     * @param dateString 用来判断得到字符串格式的日期字符串
     * @return 日期字符串对应的格式字符换, 如果没有匹配成功返回空
     * @see SimpleDateFormat
     */
    public static String getDateFormat(String dateString) {
        for (String regexp : DATE_FORMAT_REGEXPS.keySet()) {
            if (dateString.toLowerCase().matches(regexp)) {
                return DATE_FORMAT_REGEXPS.get(regexp);
            }
        }
        return null;
    }



    /**
     * 字符串传换成Date
     *
     * @param dateString 将日期字符串转化为日期
     * @param pattern 传入转换字符串格式
     * @return 字符串转换后的日期
     */
    public static Date stringToDate(String dateString, String pattern) {
        if (dateString == null || dateString.trim().length() <= 0)
            return null;

        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        try {
            return sdf.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 字符串传换成Date，智能的匹配字符串的格式，匹配列表
     * @See DATE_FORMAT_REGEXPS
     *
     * @param dateString 将日期字符串转化为日期
     * @return 日期
     */
    public static Date stringToDate(String dateString) {
        if (dateString == null || dateString.trim().length() <= 0) {
            return null;
        }

        String pattern = getDateFormat(dateString);
        if (pattern == null) {
            return null;
        }

        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        try {
            return sdf.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }



    /**
     * 将Date转化为日期字符串
     *
     * @param date 需要转换的日期
     * @param pattern 返回日期字符串的格式
     * @return 字符串转为的date
     */
    public static String dateToString(Date date, String pattern) {
        if (date == null || pattern==null || pattern.trim().length() <= 0)
            return null;
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(date);
    }


    /**
     * 将日期指定元素而截去的日期值, 比如原始的日期为2015-9-18 16:34:00 pattern为 yyyy-MM-dd 则返回的日期为2015-9-18 00:00:00
     * @param date 指定的日期
     * @param pattern 返回日期的格式
     * @return 日期
     */
    public static Date dateToDate(Date date, String pattern){
        if (date == null|| pattern==null || pattern.trim().length() <= 0) {
            return null;
        }

        try {
            SimpleDateFormat sdf=new SimpleDateFormat(pattern);
            String s=sdf.format(date);
            return sdf.parse(s);
        } catch (ParseException e) {
            return  null;
        }
    }


    /**
     * 将日期字符串自动判断格式，并转化为另一个日期字符串
     * @param dateString 日期字符串
     * @param pattern 返回日期字符串的格式
     * @return 日期字符串
     */
    public static String stringToString(String dateString, String pattern) {
        if (dateString == null || dateString.trim().length() <= 0
                || pattern == null || pattern.trim().length() <= 0) {
            return null;
        }

        String oldPattern = getDateFormat(dateString);
        if (oldPattern == null) {
            return null;
        }

        Date date = stringToDate(dateString, oldPattern);
        if (date != null) {
            return dateToString(date, pattern);
        }

        return null;
    }


    /**
     * 将日期字符串转化为另一个日期字符串
     * @param dateString 日期字符串
     * @param oldPattern 传入日期字符的格式
     * @param newPattern 转出日期字符串的格式
     * @return 日期字符串
     */
    public static String stringToString(String dateString, String oldPattern, String newPattern) {
        if (dateString == null || dateString.trim().length() <= 0
                || oldPattern == null || oldPattern.trim().length() <= 0
                || newPattern == null || newPattern.trim().length() <= 0) {
            return null;
        }

        Date date = stringToDate(dateString, oldPattern);

        if (date != null) {
            return dateToString(date, newPattern);
        }

        return null;
    }

    /**
     * 在指定的时间单位上, 加上或减去指定的数值, 例如添在原来的日期date上添加5天
     * 可以通过dateAdd(date, Calendar.DAY_OF_MONTH,5)
     * @param dateString 日期
     * @param field   时间的单位, 用Calendar的单位定义
     * @param amount  在指定单位上相加的时间
     * @return 时间相加后的日期
     */
    public static String dateAdd(String dateString, int field, int amount){
        if (dateString == null || dateString.trim().length() <= 0) {
            return null;
        }

        String pattern = getDateFormat(dateString);
        if (pattern == null) {
            return null;
        }

        Date date = stringToDate(dateString,pattern);
        Date dateRet = dateAdd(date, field, amount);
        return dateToString(dateRet,pattern);
    }

    /**
     * 在指定的时间单位上, 加上或减去指定的数值, 例如添在原来的日期date上添加5天
     * 可以通过dateAdd(date, Calendar.DAY_OF_MONTH,5)
     * @param date 日期
     * @param field   时间的单位, 用Calendar的单位定义
     * @param amount  在指定单位上相加的时间
     * @return 时间相加后的日期
     */
    public static Date dateAdd(Date date, int field, int amount){
        if (date == null)
            return null;
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(field, amount);
        return cal.getTime();
    }

    /**
     * 增加日期的年份。失败返回null。
     *
     * @param dateString 日期
     * @param yearAmount  增加数量。可为负数
     * @return 增加年份后的日期字符串
     */
    public static String addYear(String dateString, int yearAmount) {
        return dateAdd(dateString, Calendar.YEAR, yearAmount);
    }

    /**
     * 增加日期的年份。失败返回null。
     *
     * @param date 日期
     * @param yearAmount 增加数量。可为负数
     * @return 增加年份后的日期
     */
    public static Date addYear(Date date, int yearAmount) {
        return dateAdd(date, Calendar.YEAR, yearAmount);
    }

    /**
     * 增加日期的月份。失败返回null。
     *
     * @param dateString 日期
     * @param monthAmount 增加数量。可为负数
     * @return 增加月份后的日期字符串
     */
    public static String addMonth(String dateString, int monthAmount) {
        return dateAdd(dateString, Calendar.MONTH, monthAmount);
    }

    /**
     * 增加日期的月份。失败返回null。
     *
     * @param date
     *            日期
     * @param monthAmount
     *            增加数量。可为负数
     * @return 增加月份后的日期
     */
    public static Date addMonth(Date date, int monthAmount) {
        return dateAdd(date, Calendar.MONTH, monthAmount);
    }

    /**
     * 增加日期的天数。失败返回null。
     *
     * @param dateString 日期字符串
     * @param dayAmount 增加数量。可为负数
     * @return 增加天数后的日期字符串
     */
    public static String addDay(String dateString, int dayAmount) {
        return dateAdd(dateString, Calendar.DATE, dayAmount);
    }

    /**
     * 增加日期的天数。失败返回null。
     *
     * @param date 日期
     * @param dayAmount 增加数量。可为负数
     * @return 增加天数后的日期
     */
    public static Date addDay(Date date, int dayAmount) {
        return dateAdd(date, Calendar.DATE, dayAmount);
    }

    /**
     * 增加日期的小时。失败返回null。
     *
     * @param dateString 日期字符串
     * @param hourAmount 增加数量。可为负数
     * @return 增加小时后的日期字符串
     */
    public static String addHour(String dateString, int hourAmount) {
        return dateAdd(dateString, Calendar.HOUR_OF_DAY, hourAmount);
    }

    /**
     * 增加日期的小时。失败返回null。
     *
     * @param date 日期
     * @param hourAmount 增加数量。可为负数
     * @return 增加小时后的日期
     */
    public static Date addHour(Date date, int hourAmount) {
        return dateAdd(date, Calendar.HOUR_OF_DAY, hourAmount);
    }

    /**
     * 增加日期的分钟。失败返回null。
     *
     * @param dateString 日期字符串
     * @param minuteAmount 增加数量。可为负数
     * @return 增加分钟后的日期字符串
     */
    public static String addMinute(String dateString, int minuteAmount) {
        return dateAdd(dateString, Calendar.MINUTE, minuteAmount);
    }

    /**
     * 增加日期的分钟。失败返回null。
     *
     * @param date 日期
     * @param minuteAmount 增加数量。可为负数
     * @return 增加分钟后的日期
     */
    public static Date addMinute(Date date, int minuteAmount) {
        return dateAdd(date, Calendar.MINUTE, minuteAmount);
    }

    /**
     * 增加日期的秒钟。失败返回null。
     *
     * @param dateString 日期字符串
     * @param secondAmount  增加数量。可为负数
     * @return 增加秒钟后的日期字符串
     */
    public static String addSecond(String dateString, int secondAmount) {
        return dateAdd(dateString, Calendar.SECOND, secondAmount);
    }

    /**
     * 增加日期的秒钟。失败返回null。
     *
     * @param date 日期
     * @param secondAmount 增加数量。可为负数
     * @return 增加秒钟后的日期
     */
    public static Date addSecond(Date date, int secondAmount) {
        return dateAdd(date, Calendar.SECOND, secondAmount);
    }


    /**
     * 得到日期的指定时间单位的数值, 如果没有指定日期返回-1
     * @param date 日期
     * @param field 时间的单位, 用Calendar的单位定义
     * @return 指定单位上的数值
     */
    public static int get(Date date, int field){
        if (date == null) {
            return -1;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(field);
    }


    /**
     * 得到指定日期的月份
     * @param date 在月中的天
     * @return 月份顺序
     */
    public static int getMonth(Date date){
        return get(date,Calendar.MONTH);
    }


    /**
     * 得到指定日期的，在月中的天
     * @param date 要截取的日期
     * @return 月份顺序
     */
    public static int getDayOfMonth(Date date){
        return get(date,Calendar.DAY_OF_MONTH);
    }

    /**
     * 得到星期几
     * @param date 要截取的日期
     * @return SUNDAY=1...SATURDAY=7
     */
    public static int getDayOfWeek(Date date){
        return get(date,Calendar.DAY_OF_WEEK);
    }

    /**
     * 得到改天是一年中的第几天
     * @param date 指定日期
     * @return 当年中的第几天
     */
    public static int getDayOfYear(Date date){
        return get(date,Calendar.DAY_OF_YEAR);
    }

    /**
     * 得到日期是当月的第几周
     * @param date 指定的日期
     * @return 是该月的第几周
     */
    public static int getWeekOfMonth(Date date){
        return get(date,Calendar.WEEK_OF_MONTH);
    }

    /**
     * 得到当前的小时数
     * @param date
     * @return
     */
    public static  int getHourOfDay(Date date){
        return get(date,Calendar.HOUR_OF_DAY);
    }

    /**
     * 得到当前的分钟
     * @param date
     * @return
     */
    public static int getMinute(Date date){
        return get(date,Calendar.MINUTE);
    }

    /**
     * 得到当前的秒
     * @param date
     * @return
     */
    public static int getSecond(Date date){
        return get(date,Calendar.SECOND);
    }

    /**
     * 得到当前的年
     * @param date
     * @return
     */
    public static int getWeekOfYear(Date date){
        return  get(date, Calendar.WEEK_OF_YEAR);
    }


    /**
     * 得到指定日期下一个月的一日
     * @param date 下个月1日 00:00:00
     * @return 下个月1日 00:00:00
     */
    public static Date getNextMonth1st(Date date){
        if (date == null) {
            return null;
        }
        Date nextMonth = dateAdd(date, Calendar.MONTH, 1);
        return stringToDate(dateToString(nextMonth, YYYY_MM_DD), YYYY_MM);
    }


    /**
     * 得到指定日期下一个月的一日
     * @param date 下个月1日 00:00:00
     * @param pattern 返回日期的格式
     * @return 下个月1日 00:00:00
     */
    public static String getNextMonth1st(Date date,String pattern){
        if (date == null) {
            return null;
        }

        if (pattern == null) {
            pattern = YYYY_MM_DD;
        }

        Date nextMonth1st = getNextMonth1st(date);
        return dateToString(nextMonth1st, pattern);
    }


    /**
     * 得到指定日期下一个月的一号
     * @param dateString 日期字符串
     * @param pattern 返回日期的格式
     * @return String 下个月1日 00:00:00
     */
    public static String getNextMonth1st(String dateString,String pattern){
        return getNextMonth1st(stringToDate(dateString), pattern);
    }

    /**
     * 得到指定日期下一个月的一号
     * @param dateString 日期字符串
     * @return String 下个月1日 00:00:00
     */
    public static String getNextMonth1st(String dateString){
        return getNextMonth1st(dateString, YYYY_MM_DD);
    }



    /**
     * 得到指定日期当月的1号对应的日期
     * @param date 指定的日之前
     * @return 日期为1号的日期
     */
    public static Date getCurMonth1st(Date date){
        if (date == null) {
            return null;
        }

        return stringToDate(dateToString(date, YYYY_MM_DD), YYYY_MM);
    }

    /**
     * 得到指定日期当月的1号对应的日期
     * @param date 指定的日期
     * @param pattern 返回日期的格式，如果格式非空，返回YYYY-MM-DD类型的格式
     * @return 日期为1号的日期，格式由pattern决定
     */
    public static String getCurMonth1st(Date date,String pattern){
        if (date == null ) {
            return null;
        }

        if (pattern == null) {
            pattern = YYYY_MM_DD;
        }

        Date curMonth1st = getCurMonth1st(date);
        return dateToString(curMonth1st, pattern);
    }


    /**
     * 得到指定日期当月的1号对应的日期
     * @param dateString 指定的日之前
     * @param pattern 返回日期的格式，如果格式非空，返回YYYY-MM-DD类型的格式
     * @return 日期为1号的日期，格式由pattern决定
     */
    public static String getCurMonth1st(String dateString,String pattern){
        if (dateString == null || dateString.trim().length() == 0) {
            return null;
        }

        if (pattern == null) {
            pattern = YYYY_MM_DD;
        }

        Date curMonth1st = getCurMonth1st(stringToDate(dateString));
        return dateToString(curMonth1st, pattern);
    }


    /**
     * 得到指定日期当月的一号
     * @param dateString 日期字符串
     * @return String 本月1日 例如:2016-05-01
     */
    public static String getCurMonth1st(String dateString){
        return getCurMonth1st(dateString, YYYY_MM_DD);
    }


    /**
     * 得到指定日期当前月的最后一天的日期
     * @param date 指定的日期
     * @return 最后一天的日期
     */
    public static Date getCurMonthLastDay(Date date)  {
        if (date == null) {
            return null;
        }

        return addDay(getNextMonth1st(date),-1);
    }


    /**
     * 得到指定日期当前月的最后一天的日期字符, 格式为yyyy-MM-dd
     * @param date 指定的日期
     * @return 最后一天的日期
     */
    public static String getCurMonthLastDay(Date date,String pattern)  {
        if (date == null) {
            return null;
        }

        return dateToString(getCurMonthLastDay(date),pattern);
    }


    /**
     * 得到指定日期当前月的最后一天的日期字符, 格式为yyyy-MM-dd
     * @param dateString 指定的日期
     * @return 最后一天的日期
     */
    public static String getCurMonthLastDay(String dateString,String pattern)  {
        if (dateString == null || dateString.trim().length()==0) {
            return null;
        }

        return dateToString(getCurMonthLastDay(stringToDate(dateString)),pattern);
    }

    /**
     * 得到指定日期当前月的最后一天的日期字符, 格式为yyyy-MM-dd
     * @param dateString 指定的日期
     * @return 最后一天的日期
     */
    public static String getCurMonthLastDay(String dateString)  {
        return getCurMonthLastDay(dateString,YYYY_MM_DD);
    }



    /**
     * 得到下一个指定的日期(DAY_OF_MONTH)
     * @param date 指定的日期
     * @param dayOfMonth 指定的 DAY_OF_MONTH
     * @return
     */
    public static Date getNextDayOfMonth(Date date, int dayOfMonth){
        if (date == null) {
            return null;
        }

        int curDayOfMonth = getDayOfMonth(date);
        if(curDayOfMonth <= dayOfMonth){
            return dateAdd(date, Calendar.DAY_OF_MONTH, dayOfMonth - curDayOfMonth);
        }else {
            return dateAdd(getNextMonth1st(date), Calendar.DAY_OF_MONTH, dayOfMonth-1);
        }
    }

    /**
     * 得到下一个日期, 比如今天是5月5号, 得到下一个10号为5月10号, 得到下一个2号为6月2号
     * @param dayOfMonth 指定的日期
     * @return
     */
    public static Date getNextDayOfMonth(int dayOfMonth){
        return getNextDayOfMonth(new Date(),dayOfMonth);
    }

    /**
     * 得到下一个指定的日期(DAY_OF_MONTH)
     * @param date 指定的日期
     * @param dayOfMonth 指定的 DAY_OF_MONTH
     * @return
     */
    public static String getNextDayOfMonthString(Date date, int dayOfMonth){
        if (date == null) {
            return null;
        }
       return  dateToString(getNextDayOfMonth(date,dayOfMonth),YYYY_MM_DD);
    }

    /**
     * 得到下一个指定的日期(DAY_OF_MONTH)
     * @param dayOfMonth 指定的 DAY_OF_MONTH
     * @return
     */
    public static String getNextDayOfMonthString(int dayOfMonth){
        return  getNextDayOfMonthString(new Date(),dayOfMonth);
    }


    /**
     * 得到下个周一的日期,周日为一周的结束
     * @param date
     * @return
     */
    public static Date getNextMonday(Date date){
        if (date == null) {
            return null;
        }

        int curWeedDay = getDayOfWeek(date);
        //日1 一2 二3 三4 四5 五6 六7
        return dateAdd(date, Calendar.DAY_OF_YEAR, 7 - (curWeedDay -2 + 7) % 7 );
    }

    /**
     * 得到下个周一的日期,周日为一周的结束
     * @return
     */
    public static Date getNextMonday(){
        return getNextMonday(new Date());
    }


    /**
     * 得到下一个周一的日期字符串,周日为一周的结束
     * @param date 指定的日期
     * @return
     */
    public static String getNextMondayString(Date date){
        if (date == null) {
            return null;
        }

        return dateToString(getNextMonday(date), YYYY_MM_DD);
    }

    /**
     * 得到相对于下一个周一的日期字符串,周日为一周的结束
     * @return
     */
    public static String getNextMondayString(){
        return getNextMondayString(new Date());
    }


    /**
     * 得到下一个周几, 如果指定的星期几,大于当前日期的星期, 则为本周的日期, 否则为下周的日期
     * 比如今天是周三, 下一个周三为下周三, 下一个周五为本周五, 下一个周一为下周一
     * @param date  要判断的日期
     * @param dayOfWeek 周几, 又Calendar.MONDAY/TUESDAY...指定
     * @return
     */
    public static Date getNextDayOfWeek(Date date, int dayOfWeek){
        if (date == null) {
            return null;
        }
        int curWeedDay = getDayOfWeek(date);
        if(dayOfWeek>curWeedDay){
            return dateAdd(date,Calendar.DAY_OF_YEAR, dayOfWeek-curWeedDay);
        }

        return dateAdd(getNextMonday(date),Calendar.DAY_OF_YEAR, dayOfWeek-Calendar.MONDAY);
    }

    /**
     * 得到下一个周几, 如果指定的星期几,大于当前日期的星期, 则为本周的日期, 否则为下周的日期
     * 比如今天是周三, 下一个周三为下周三, 下一个周五为本周五, 下一个周一为下周一
     * @param date  要判断的日期
     * @param dayOfWeek 周几, 又Calendar.MONDAY/TUESDAY...指定
     * @return
     */
    public static String getNextDayOfWeekString(Date date, int dayOfWeek){
        return dateToString(getNextDayOfWeek(date,dayOfWeek), YYYY_MM_DD);
    }

    /**
     * 得到下一个周几, 如果指定的星期几,大于当前日期的星期, 则为本周的日期, 否则为下周的日期
     * 比如今天是周三, 下一个周三为下周三, 下一个周五为本周五, 下一个周一为下周一
     * @param dayOfWeek 周几, 又Calendar.MONDAY/TUESDAY...指定
     * @return
     */
    public static Date getNextDayOfWeek(int dayOfWeek){
        return getNextDayOfWeek(new Date(), dayOfWeek);
    }

    /**
     * 得到下一个周几, 如果指定的星期几,大于当前日期的星期, 则为本周的日期, 否则为下周的日期
     * 比如今天是周三, 下一个周三为下周三, 下一个周五为本周五, 下一个周一为下周一
     * @param dayOfWeek 周几, 又Calendar.MONDAY/TUESDAY...指定, MONDAY->2 TUESDAY->3
     * @return
     */
    public static String getNextDayOfWeekString(int dayOfWeek){
        return dateToString(getNextDayOfWeek(new Date(), dayOfWeek), YYYY_MM_DD);
    }


    /**
     * 按指定日期单位计算两个日期间的间隔, 时间单位可以为YEAR,MONTH,DAY,WEEK,HOUR,MINUTE,SECOND,QUARTER,FRAC_SECOND
     * 用第一个时间减去第二个时间的数值
     *
     * @param timeInterval 时间间隔单位, 取值可以为YEAR,MONTH,DAY,WEEK,HOUR,MINUTE,SECOND,QUARTER,FRAC_SECOND 如果不在指定的单位内返回时间的毫秒查
     * @param subtrahend 被减的时间
     * @param extraction   减去的时间
     * @return 第一个参数指定时间减去第二个参数时间差的单位的数值
     */
    public static long dateDiff(String timeInterval, Date subtrahend, Date extraction ) {
        if (subtrahend == null || extraction == null) {
            return 0;
        }


        if (timeInterval.equals(YEAR)) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(subtrahend);
            int time = calendar.get(Calendar.YEAR);
            calendar.setTime(extraction );
            return time - calendar.get(Calendar.YEAR);
        }

        if (timeInterval.equals(QUARTER)) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(subtrahend);
            int time = calendar.get(Calendar.YEAR) * 4;
            calendar.setTime(extraction );
            time -= calendar.get(Calendar.YEAR) * 4;
            calendar.setTime(subtrahend);
            time += calendar.get(Calendar.MONTH) / 4;
            calendar.setTime(extraction );
            return time - calendar.get(Calendar.MONTH) / 4;
        }

        if (timeInterval.equals(MONTH)) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(subtrahend);
            int time = calendar.get(Calendar.YEAR) * 12;
            calendar.setTime(extraction );
            time -= calendar.get(Calendar.YEAR) * 12;
            calendar.setTime(subtrahend);
            time += calendar.get(Calendar.MONTH);
            calendar.setTime(extraction );
            return time - calendar.get(Calendar.MONTH);
        }

        if (timeInterval.equals(WEEK)) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(subtrahend);
            int time = calendar.get(Calendar.YEAR) * 52;
            calendar.setTime(extraction );
            time -= calendar.get(Calendar.YEAR) * 52;
            calendar.setTime(subtrahend);
            time += calendar.get(Calendar.WEEK_OF_YEAR);
            calendar.setTime(extraction );
            return time - calendar.get(Calendar.WEEK_OF_YEAR);
        }

        if (timeInterval.equals(DAY)) {
            long time = subtrahend.getTime() / 1000 / 60 / 60 / 24;
            return time - extraction .getTime() / 1000 / 60 / 60 / 24;
        }

        if (timeInterval.equals(HOUR)) {
            long time = subtrahend.getTime() / 1000 / 60 / 60;
            return time - extraction .getTime() / 1000 / 60 / 60;
        }

        if (timeInterval.equals(MINUTE)) {
            long time = subtrahend.getTime() / 1000 / 60;
            return time - extraction .getTime() / 1000 / 60;
        }

        if (timeInterval.equals(SECOND)) {
            long time = subtrahend.getTime() / 1000;
            return time - extraction .getTime() / 1000;
        }

        if (timeInterval.equals(FRAC_SECOND)) {
            return subtrahend.getTime() - extraction .getTime();
        }

        return subtrahend.getTime() - extraction .getTime();
    }


    /**
     * 获取两个日期相差的天数
     *
     * @param date 日期字符串
     * @param otherDate 另一个日期字符串
     * @return 相差天数。如果失败则返回-1
     */
    public static long getIntervalDays(Date date, Date otherDate) {
        return dateDiff(DateUtil.DAY, date, otherDate);
    }




    public static void main(String[] args) {
        System.out.println("getDateFormat(String dateString) ---->"+getDateFormat("2016-04-16") );
        System.out.println("getDateFormat(String dateString) ---->"+getDateFormat("2016-04-16 22") );
        System.out.println("getDateFormat(String dateString) ---->"+getDateFormat("2016-04-16 22:10") );
        System.out.println("getDateFormat(String dateString) ---->"+getDateFormat("2016-04-16 22:10:30") );
        System.out.println("stringToDate(String dateString, String pattern) ---->"+stringToDate("2016-04-16 22:10:30", YYYY_MM_DD_HH_MM_SS));
        System.out.println("stringToDate(String dateString) ---->"+stringToDate("2016-04-16 22:10:30" ));
        System.out.println("dateToString(Date date, String pattern) ---->"+dateToString(new Date(), YYYY_MM_DD_HH_MM_SS) );
        System.out.println("dateToDate(Date date, String pattern)---->"+dateToDate(new Date(), YYYY_MM_DD));
        System.out.println("stringToString(String dateString, String pattern) ---->"+stringToString("2016-04-16 22:10:30", YYYY_MM_DD) );
        System.out.println("stringToString(String dateString, String oldPattern, String newPattern) ---->"+stringToString("2016-04-16 22:10:30",YYYY_MM_DD_HH_MM_SS, YYYY_MM_DD) );
        System.out.println("dateAdd(String dateString, int field, int amount)---->"+dateAdd("2016-04-16 22:10:30", Calendar.DAY_OF_MONTH, 30));
        System.out.println("dateAdd(Date date, int field, int amount)---->"+dateAdd(new Date(), Calendar.DAY_OF_MONTH, 3));
        System.out.println("addYear(String dateString, int yearAmount) ---->"+addYear("2016-04-16", 3) );
        System.out.println("addYear(Date date, int yearAmount) ---->"+addYear(new Date(), 1) );
        System.out.println("addMonth(String dateString, int monthAmount) ---->"+addMonth("2016-04-16 22:10:30", 2) );
        System.out.println("addMonth(Date date, int monthAmount) ---->"+addMonth(new Date(), 3) );
        System.out.println("addDay(String dateString, int dayAmount) ---->"+addDay("2016-04-16 22:10:30", 4) );
        System.out.println("addDay(Date date, int dayAmount) ---->"+addDay(new Date(), 5) );
        System.out.println("addHour(String dateString, int hourAmount) ---->"+addHour("2016-04-16 22:10:30", 2) );
        System.out.println("addHour(Date date, int hourAmount) ---->"+addHour(new Date(), 5) );
        System.out.println("addMinute(String dateString, int minuteAmount) ---->"+addMinute("2016-04-16 22:10:30", 3) );
        System.out.println("addMinute(Date date, int minuteAmount) ---->"+addMinute(new Date(), 30) );
        System.out.println("addSecond(String dateString, int secondAmount) ---->"+addSecond("2016-04-16 22:10:30", 30) );
        System.out.println("addSecond(Date date, int secondAmount) ---->"+addSecond(new Date(), 10) );
        System.out.println("get(Date date, int field)---->"+get(new Date(), Calendar.DAY_OF_WEEK));
        System.out.println("getMonth(Date date)---->"+getMonth(new Date()));
        System.out.println("getDayOfMonth(Date date)---->"+getDayOfMonth(new Date()));
        System.out.println("getDayOfWeek(Date date)---->"+getDayOfWeek(new Date()));
        System.out.println("getDayOfYear(Date date)---->"+getDayOfYear(new Date()));
        System.out.println("getWeekOfMonth(Date date)---->"+getWeekOfMonth(new Date()));
        System.out.println("getHourOfDay(Date date)---->"+getHourOfDay(new Date()));
        System.out.println("getMinute(Date date)---->"+getMinute(new Date()));
        System.out.println("getSecond(Date date)---->"+getSecond(new Date()));
        System.out.println("getWeekOfYear(Date date)---->"+getWeekOfYear(new Date()));
        System.out.println("getNextMonth1st(Date date)---->"+getNextMonth1st(new Date()));
        System.out.println("getNextMonth1st(Date date,String pattern)---->"+getNextMonth1st(new Date(),YYYY_MM_DD));
        System.out.println("getNextMonth1st(String dateString,String pattern)---->"+getNextMonth1st("2016-04-16 22:10:30",YYYY_MM_DD));
        System.out.println("getNextMonth1st(String dateString)---->"+getNextMonth1st("2016-04-16 22:10:30"));
        System.out.println("getCurMonth1st(Date date)---->"+getCurMonth1st(new Date()));
        System.out.println("getCurMonth1st(Date date,String pattern)---->"+getCurMonth1st(new Date(),YYYY_MM_DD));
        System.out.println("getCurMonth1st(String dateString,String pattern)---->"+getCurMonth1st("2016-04-16 22:10:30",YYYY_MM_DD));
        System.out.println("getCurMonth1st(String dateString)---->"+getCurMonth1st("2016-04-16 22:10:30"));
        System.out.println("getCurMonthLastDay(Date date)  ---->"+getCurMonthLastDay(new Date())  );
        System.out.println("getCurMonthLastDay(Date date,String pattern)  ---->" + getCurMonthLastDay(new Date(), YYYY_MM_DD));
        System.out.println("getCurMonthLastDay(String dateString,String pattern)  ---->"+getCurMonthLastDay("2016-04-16 22:10:30",YYYY_MM_DD)  );
        System.out.println("getCurMonthLastDay(String dateString)  ---->"+getCurMonthLastDay("2016-04-16 22:10:30")  );
        System.out.println("getNextDayOfMonth(Date date, int dayOfMonth)---->"+getNextDayOfMonth(new Date(), 11));
        System.out.println("getNextDayOfMonth(int dayOfMonth)---->"+getNextDayOfMonth(1));
        System.out.println("getNextDayOfMonthString(Date date, int dayOfMonth)---->"+getNextDayOfMonthString(new Date(), 12));
        System.out.println("getNextDayOfMonthString(int dayOfMonth)---->"+getNextDayOfMonthString(11));
        System.out.println("getNextMonday(Date date)---->"+getNextMonday(new Date()));
        System.out.println("getNextMonday()---->"+getNextMonday());
        System.out.println("getNextMondayString(Date date)---->"+getNextMondayString(new Date()));
        System.out.println("getNextMondayString()---->"+getNextMondayString());
        System.out.println("getNextDayOfWeek(Date date, int dayOfWeek)---->"+getNextDayOfWeek(new Date(), Calendar.MONDAY));
        System.out.println("getNextDayOfWeekString(Date date, int dayOfWeek)---->"+getNextDayOfWeekString(new Date(), Calendar.FRIDAY));
        System.out.println("getNextDayOfWeek(int dayOfWeek)---->"+getNextDayOfWeek(Calendar.FRIDAY));
        System.out.println("getNextDayOfWeekString(int dayOfWeek)---->"+getNextDayOfWeekString(Calendar.FRIDAY));
        System.out.println("long dateDiff(String timeInterval, Date subtrahend, Date extraction ) ---->"+ dateDiff(DAY, new Date(), stringToDate("2016-04-13 23:10:30" )) );
        System.out.println("long getIntervalDays(Date date, Date otherDate) ---->"+ getIntervalDays(new Date(), stringToDate("2016-04-13 23:10:30") ));


    }
}