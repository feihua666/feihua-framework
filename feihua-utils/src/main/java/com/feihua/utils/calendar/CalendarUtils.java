package com.feihua.utils.calendar;

/**
 * 日期工具操作类
 * 2014年2月24日 16:41:11
 * 飞华
 */

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CalendarUtils {

    private static final ThreadLocal<SimpleDateFormat> threadLocal = new ThreadLocal<SimpleDateFormat>();
    private static final ThreadLocal<SimpleDateFormat> threadLocalLenient = new ThreadLocal<SimpleDateFormat>();
    private static final Calendar calendar = Calendar.getInstance();
    private static final Object object = new Object();
    private static final String time_zero = "00:00:00";
    private static final String time_last = "23:59:59";

    /**
     * 获取SimpleDateFormat 
     * @param pattern 日期格式 
     * @return SimpleDateFormat对象
     * @throws RuntimeException 异常：非法日期格式 
     */
    private static SimpleDateFormat getDateFormat(String pattern) throws RuntimeException {
        SimpleDateFormat dateFormat = threadLocal.get();
        if (dateFormat == null) {
            synchronized (object) {
                if (dateFormat == null) {
                    dateFormat = new SimpleDateFormat(pattern);
                    dateFormat.setLenient(false);
                    threadLocal.set(dateFormat);
                }
            }
        }
        dateFormat.applyPattern(pattern);
        return dateFormat;
    }
    /**
     * 获取SimpleDateFormat
     * @param pattern 日期格式
     * @param lenient 是否宽松
     * @return SimpleDateFormat对象
     * @throws RuntimeException 异常：非法日期格式
     */
    private static SimpleDateFormat getDateFormat(String pattern,boolean lenient) throws RuntimeException {
        if(lenient){
            SimpleDateFormat dateFormat = threadLocalLenient.get();
            if (dateFormat == null) {
                synchronized (object) {
                    if (dateFormat == null) {
                        dateFormat = new SimpleDateFormat(pattern);
                        dateFormat.setLenient(lenient);
                        threadLocalLenient.set(dateFormat);
                    }
                }
            }
            dateFormat.applyPattern(pattern);
            return dateFormat;
        }else {
            return getDateFormat(pattern);
        }

    }

    /**
     * 判断是否为闰年
     * @param year
     * @return
     */
    public static boolean isLeap(int year){
        if(year % 4 == 0 && year % 100 != 0 || year%400 == 0){
            return true;
        }else{
            return false;
        }
    }
    /**
     * 获取日期中的某数值。如获取月份 
     * @param date 日期 
     * @param dateType 日期格式 
     * @return 数值
     */
    private static int getInteger(Date date, int dateType) {
        int num = 0;
        if (date != null) {
            calendar.setTime(date);
            num = calendar.get(dateType);
        }
        return num;
    }

    /**
     * 增加日期中某类型的某数值。如增加日期 
     * @param date 日期字符串 
     * @param dateType 类型 
     * @param amount 数值 
     * @return 计算后日期字符串
     */
    public static String addInteger(String date, int dateType, int amount) {
        String dateString = null;
        DateStyle dateStyle = getDateStyle(date);
        if (dateStyle != null) {
            Date myDate = stringToDate(date, dateStyle);
            myDate = addInteger(myDate, dateType, amount);
            dateString = dateToString(myDate, dateStyle);
        }
        return dateString;
    }

    /**
     * 增加日期中某类型的某数值。如增加日期
     * @param date 日期
     * @param dateType 类型
     * @param amount 数值
     * @return 计算后日期
     */
    public static Date addInteger(Date date, int dateType, int amount) {
        Date myDate = null;
        if (date != null) {
            calendar.setTime(date);
            calendar.add(dateType, amount);
            myDate = calendar.getTime();
        }
        return myDate;
    }

    /**
     * 获取精确的日期
     * @param timestamps 时间long集合
     * @return 日期
     */
    private static Date getAccurateDate(List<Long> timestamps) {
        Date date = null;
        long timestamp = 0;
        Map<Long, long[]> map = new HashMap<Long, long[]>();
        List<Long> absoluteValues = new ArrayList<Long>();

        if (timestamps != null && timestamps.size() > 0) {
            if (timestamps.size() > 1) {
                for (int i = 0; i < timestamps.size(); i++) {
                    for (int j = i + 1; j < timestamps.size(); j++) {
                        long absoluteValue = Math.abs(timestamps.get(i) - timestamps.get(j));
                        absoluteValues.add(absoluteValue);
                        long[] timestampTmp = { timestamps.get(i), timestamps.get(j) };
                        map.put(absoluteValue, timestampTmp);
                    }
                }

                // 有可能有相等的情况。如2012-11和2012-11-01。时间戳是相等的。此时minAbsoluteValue为0
                // 因此不能将minAbsoluteValue取默认值0
                long minAbsoluteValue = -1;
                if (!absoluteValues.isEmpty()) {
                    minAbsoluteValue = absoluteValues.get(0);
                    for (int i = 1; i < absoluteValues.size(); i++) {
                        if (minAbsoluteValue > absoluteValues.get(i)) {
                            minAbsoluteValue = absoluteValues.get(i);
                        }
                    }
                }

                if (minAbsoluteValue != -1) {
                    long[] timestampsLastTmp = map.get(minAbsoluteValue);

                    long dateOne = timestampsLastTmp[0];
                    long dateTwo = timestampsLastTmp[1];
                    if (absoluteValues.size() > 1) {
                        timestamp = Math.abs(dateOne) > Math.abs(dateTwo) ? dateOne : dateTwo;
                    }
                }
            } else {
                timestamp = timestamps.get(0);
            }
        }

        if (timestamp != 0) {
            date = new Date(timestamp);
        }
        return date;
    }

    /**
     * 判断字符串是否为日期字符串
     * @param date 日期字符串
     * @return true or false
     */
    public static boolean isDate(String date) {
        boolean isDate = false;
        if (date != null) {
            if (getDateStyle(date) != null) {
                isDate = true;
            }
        }
        return isDate;
    }
    /**
     * 获取当前的日期对象
     * @return
     */
    public static Date getCurrentDate(){
        return new Date();
    }
    /**
     * 获取日期字符串的日期风格。失敗返回null。
     * @param date 日期字符串
     * @return 日期风格
     */
    public static DateStyle getDateStyle(String date){
        return getDateStyle(date,false);
    }
    /**
     * 获取日期字符串的日期风格。失敗返回null。
     * @param date 日期字符串
     * @return 日期风格
     */
    public static DateStyle getDateStyle(String date,boolean lenient) {
        DateStyle dateStyle = null;
        Map<Long, DateStyle> map = new HashMap<Long, DateStyle>();
        List<Long> timestamps = new ArrayList<Long>();
        for (DateStyle style : DateStyle.values()) {
            if (style.isShowOnly()) {
                continue;
            }
            Date dateTmp = null;
            if (date != null) {
                try {
                    ParsePosition pos = new ParsePosition(0);
                    dateTmp = getDateFormat(style.getValue(),lenient).parse(date, pos);
                    if (pos.getIndex() != date.length()) {
                        dateTmp = null;
                    }
                } catch (Exception e) {
                }
            }
            if (dateTmp != null) {
                timestamps.add(dateTmp.getTime());
                map.put(dateTmp.getTime(), style);
            }
        }
        Date accurateDate = getAccurateDate(timestamps);
        if (accurateDate != null) {
            dateStyle = map.get(accurateDate.getTime());
        }
        return dateStyle;
    }

    /**
     * 将日期字符串转化为日期。失败返回null。
     * @param date 日期字符串
     * @return 日期
     */
    public static Date stringToDate(String date) {
        DateStyle dateStyle = getDateStyle(date);
        return stringToDate(date, dateStyle);
    }
    /**
     * 将日期字符串转化为日期。失败返回null。
     * @param date 日期字符串
     * @return 日期
     */
    public static Date stringToDate(String date, boolean lenient) {
        DateStyle dateStyle = getDateStyle(date,lenient);
        return stringToDate(date, dateStyle,lenient);
    }
    /**
     * 将日期字符串转化为日期。失败返回null。
     * @param date 日期字符串
     * @param pattern 日期格式
     * @return 日期
     */
    public static Date stringToDate(String date, String pattern) {
        return stringToDate(date,pattern,false);
    }
    /**
     * 将日期字符串转化为日期。失败返回null。
     * @param date 日期字符串
     * @param pattern 日期格式
     * @return 日期
     */
    public static Date stringToDate(String date, String pattern, boolean lenient) {
        Date myDate = null;
        if (date != null) {
            try {
                myDate = getDateFormat(pattern,lenient).parse(date);
            } catch (Exception e) {
            }
        }
        return myDate;
    }

    /**
     * 将日期字符串转化为日期。失败返回null。
     * @param date 日期字符串
     * @param dateStyle 日期风格
     * @return 日期
     */
    public static Date stringToDate(String date, DateStyle dateStyle) {
        return stringToDate(date,dateStyle,false);
    }
    /**
     * 将日期字符串转化为日期。失败返回null。
     * @param date 日期字符串
     * @param dateStyle 日期风格
     * @return 日期
     */
    public static Date stringToDate(String date, DateStyle dateStyle,boolean lenient) {
        Date myDate = null;
        if (dateStyle != null) {
            myDate = stringToDate(date, dateStyle.getValue(),lenient);
        }
        return myDate;
    }
    /**
     * 将日期转化为日期字符串。失败返回null。
     * @param date 日期
     * @param pattern 日期格式
     * @return 日期字符串
     */
    public static String dateToString(Date date, String pattern) {
        return dateToString(date,pattern,false);
    }
    /**
     * 将日期转化为日期字符串。失败返回null。
     * @param date 日期
     * @param pattern 日期格式
     * @return 日期字符串
     */
    public static String dateToString(Date date, String pattern,boolean lenient) {
        String dateString = null;
        if (date != null) {
            try {
                dateString = getDateFormat(pattern,lenient).format(date);
            } catch (Exception e) {
            }
        }
        return dateString;
    }

    /**
     * 将日期转化为日期字符串。失败返回null。
     * @param date 日期
     * @return 日期字符串 格式为@See com.feihua.utils.calendar.CalendarUtils.DateStyle#YYYY_MM_DD
     */
    public static String dateToString(Date date) {
        return dateToString(date,DateStyle.YYYY_MM_DD);
    }

    /**
     * 将日期转化为日期时间字符串。失败返回null。
     * @param date 日期
     * @return 日期字符串 格式为@See com.feihua.utils.calendar.CalendarUtils.DateStyle#YYYY_MM_DD_HH_MM_SS
     */
    public static String dateTimeToString(Date date) {
        return dateToString(date,DateStyle.YYYY_MM_DD_HH_MM_SS);
    }
    /**
     * 将日期转化为日期字符串。失败返回null。
     * @param date 日期
     * @param dateStyle 日期风格
     * @return 日期字符串
     */
    public static String dateToString(Date date, DateStyle dateStyle) {
        return dateToString(date,dateStyle,false);
    }
    /**
     * 将日期转化为日期字符串。失败返回null。
     * @param date 日期
     * @param dateStyle 日期风格
     * @return 日期字符串
     */
    public static String dateToString(Date date, DateStyle dateStyle,boolean lenient) {
        String dateString = null;
        if (dateStyle != null) {
            dateString = dateToString(date, dateStyle.getValue(),lenient);
        }
        return dateString;
    }
    /**
     * 将日期字符串转化为另一日期字符串。失败返回null。
     * @param date 旧日期字符串
     * @param newPattern 新日期格式
     * @return 新日期字符串
     */
    public static String stringToString(String date, String newPattern) {
        DateStyle oldDateStyle = getDateStyle(date);
        return stringToString(date, oldDateStyle, newPattern);
    }

    /**
     * 将日期字符串转化为另一日期字符串。失败返回null。
     * @param date 旧日期字符串
     * @param newDateStyle 新日期风格
     * @return 新日期字符串
     */
    public static String stringToString(String date, DateStyle newDateStyle) {
        DateStyle oldDateStyle = getDateStyle(date);
        return stringToString(date, oldDateStyle, newDateStyle);
    }

    /**
     * 将日期字符串转化为另一日期字符串。失败返回null。
     * @param date 旧日期字符串
     * @param olddPattern 旧日期格式
     * @param newPattern 新日期格式
     * @return 新日期字符串
     */
    public static String stringToString(String date, String olddPattern, String newPattern) {
        return dateToString(stringToDate(date, olddPattern), newPattern);
    }

    /**
     * 将日期字符串转化为另一日期字符串。失败返回null。
     * @param date 旧日期字符串
     * @param olddDteStyle 旧日期风格
     * @param newParttern 新日期格式
     * @return 新日期字符串
     */
    public static String stringToString(String date, DateStyle olddDteStyle, String newParttern) {
        String dateString = null;
        if (olddDteStyle != null) {
            dateString = stringToString(date, olddDteStyle.getValue(), newParttern);
        }
        return dateString;
    }

    /**
     * 将日期字符串转化为另一日期字符串。失败返回null。
     * @param date 旧日期字符串
     * @param olddPattern 旧日期格式
     * @param newDateStyle 新日期风格
     * @return 新日期字符串
     */
    public static String stringToString(String date, String olddPattern, DateStyle newDateStyle) {
        String dateString = null;
        if (newDateStyle != null) {
            dateString = stringToString(date, olddPattern, newDateStyle.getValue());
        }
        return dateString;
    }

    /**
     * 将日期字符串转化为另一日期字符串。失败返回null。
     * @param date 旧日期字符串
     * @param olddDteStyle 旧日期风格
     * @param newDateStyle 新日期风格
     * @return 新日期字符串
     */
    public static String stringToString(String date, DateStyle olddDteStyle, DateStyle newDateStyle) {
        String dateString = null;
        if (olddDteStyle != null && newDateStyle != null) {
            dateString = stringToString(date, olddDteStyle.getValue(), newDateStyle.getValue());
        }
        return dateString;
    }

    /**
     * 增加日期的年份。失败返回null。
     * @param date 日期
     * @param yearAmount 增加数量。可为负数
     * @return 增加年份后的日期字符串
     */
    public static String addYear(String date, int yearAmount) {
        return addInteger(date, Calendar.YEAR, yearAmount);
    }

    /**
     * 增加日期的年份。失败返回null。
     * @param date 日期
     * @param yearAmount 增加数量。可为负数
     * @return 增加年份后的日期
     */
    public static Date addYear(Date date, int yearAmount) {
        return addInteger(date, Calendar.YEAR, yearAmount);
    }

    /**
     * 增加日期的月份。失败返回null。
     * @param date 日期
     * @param monthAmount 增加数量。可为负数
     * @return 增加月份后的日期字符串
     */
    public static String addMonth(String date, int monthAmount) {
        return addInteger(date, Calendar.MONTH, monthAmount);
    }

    /**
     * 增加日期的月份。失败返回null。
     * @param date 日期
     * @param monthAmount 增加数量。可为负数
     * @return 增加月份后的日期
     */
    public static Date addMonth(Date date, int monthAmount) {
        return addInteger(date, Calendar.MONTH, monthAmount);
    }

    /**
     * 增加日期的天数。失败返回null。
     * @param date 日期字符串
     * @param dayAmount 增加数量。可为负数
     * @return 增加天数后的日期字符串
     */
    public static String addDay(String date, int dayAmount) {
        return addInteger(date, Calendar.DATE, dayAmount);
    }

    /**
     * 增加日期的天数。失败返回null。
     * @param date 日期
     * @param dayAmount 增加数量。可为负数
     * @return 增加天数后的日期
     */
    public static Date addDay(Date date, int dayAmount) {
        return addInteger(date, Calendar.DATE, dayAmount);
    }

    /**
     * 增加日期的小时。失败返回null。
     * @param date 日期字符串
     * @param hourAmount 增加数量。可为负数
     * @return 增加小时后的日期字符串
     */
    public static String addHour(String date, int hourAmount) {
        return addInteger(date, Calendar.HOUR_OF_DAY, hourAmount);
    }

    /**
     * 增加日期的小时。失败返回null。
     * @param date 日期
     * @param hourAmount 增加数量。可为负数
     * @return 增加小时后的日期
     */
    public static Date addHour(Date date, int hourAmount) {
        return addInteger(date, Calendar.HOUR_OF_DAY, hourAmount);
    }

    /**
     * 增加日期的分钟。失败返回null。
     * @param date 日期字符串
     * @param minuteAmount 增加数量。可为负数
     * @return 增加分钟后的日期字符串
     */
    public static String addMinute(String date, int minuteAmount) {
        return addInteger(date, Calendar.MINUTE, minuteAmount);
    }

    /**
     * 增加日期的分钟。失败返回null。
     * @param date 日期
     * @param minuteAmount 增加数量。可为负数
     * @return 增加分钟后的日期
     */
    public static Date addMinute(Date date, int minuteAmount) {
        return addInteger(date, Calendar.MINUTE, minuteAmount);
    }

    /**
     * 增加日期的秒钟。失败返回null。
     * @param date 日期字符串
     * @param secondAmount 增加数量。可为负数
     * @return 增加秒钟后的日期字符串
     */
    public static String addSecond(String date, int secondAmount) {
        return addInteger(date, Calendar.SECOND, secondAmount);
    }

    /**
     * 增加日期的秒钟。失败返回null。
     * @param date 日期
     * @param secondAmount 增加数量。可为负数
     * @return 增加秒钟后的日期
     */
    public static Date addSecond(Date date, int secondAmount) {
        return addInteger(date, Calendar.SECOND, secondAmount);
    }

    /**
     * 获取日期的年份。失败返回0。
     * @param date 日期字符串
     * @return 年份
     */
    public static int getYear(String date) {
        return getYear(stringToDate(date));
    }

    /**
     * 获取日期的年份。失败返回0。
     * @param date 日期
     * @return 年份
     */
    public static int getYear(Date date) {
        return getInteger(date, Calendar.YEAR);
    }

    /**
     * 获取日期的月份。失败返回0。
     * @param date 日期字符串
     * @return 月份
     */
    public static int getMonth(String date) {
        return getMonth(stringToDate(date));
    }

    /**
     * 获取日期的月份。失败返回0。
     * @param date 日期
     * @return 月份
     */
    public static int getMonth(Date date) {
        return getInteger(date, Calendar.MONTH) + 1;
    }

    /**
     * 获取日期的天数。失败返回0。
     * @param date 日期字符串
     * @return 天
     */
    public static int getDay(String date) {
        return getDay(stringToDate(date));
    }

    /**
     * 获取日期的天数。失败返回0。
     * @param date 日期
     * @return 天
     */
    public static int getDay(Date date) {
        return getInteger(date, Calendar.DATE);
    }

    /**
     * 获取日期的小时。失败返回0。
     * @param date 日期字符串
     * @return 小时
     */
    public static int getHour(String date) {
        return getHour(stringToDate(date));
    }

    /**
     * 获取日期的小时。失败返回0。
     * @param date 日期
     * @return 小时
     */
    public static int getHour(Date date) {
        return getInteger(date, Calendar.HOUR_OF_DAY);
    }

    /**
     * 获取日期的分钟。失败返回0。
     * @param date 日期字符串
     * @return 分钟
     */
    public static int getMinute(String date) {
        return getMinute(stringToDate(date));
    }

    /**
     * 获取日期的分钟。失败返回0。
     * @param date 日期
     * @return 分钟
     */
    public static int getMinute(Date date) {
        return getInteger(date, Calendar.MINUTE);
    }

    /**
     * 获取日期的秒钟。失败返回0。
     * @param date 日期字符串
     * @return 秒钟
     */
    public static int getSecond(String date) {
        return getSecond(stringToDate(date));
    }

    /**
     * 获取日期的秒钟。失败返回0。
     * @param date 日期
     * @return 秒钟
     */
    public static int getSecond(Date date) {
        return getInteger(date, Calendar.SECOND);
    }

    /**
     * 获取日期 。默认yyyy-MM-dd格式。失败返回null。
     * @param date 日期字符串
     * @return 日期
     */
    public static String getDate(String date) {
        return stringToString(date, DateStyle.YYYY_MM_DD);
    }

    /**
     * 获取日期。默认yyyy-MM-dd格式。失败返回null。
     * @param date 日期
     * @return 日期
     */
    public static String getDate(Date date) {
        return dateToString(date, DateStyle.YYYY_MM_DD);
    }

    /**
     * 获取日期的时间。默认HH:mm:ss格式。失败返回null。
     * @param date 日期字符串
     * @return 时间
     */
    public static String getTime(String date) {
        return stringToString(date, DateStyle.HH_MM_SS);
    }

    /**
     * 获取日期的时间。默认HH:mm:ss格式。失败返回null。
     * @param date 日期
     * @return 时间
     */
    public static String getTime(Date date) {
        return dateToString(date, DateStyle.HH_MM_SS);
    }

    /**
     * 将日期时间设置到0点
     * @param date
     * @return
     */
    public static Date resetTimeToZero(Date date){
        return stringToDate(dateToString(date, DateStyle.YYYY_MM_DD) + " " + time_zero);
    }

    /**
     * 将日期时间设置到23:59:59
     * @param date
     * @return
     */
    public static Date resetTimeToLast(Date date){
        return stringToDate(dateToString(date, DateStyle.YYYY_MM_DD) + " " + time_last);
    }

    /**
     * 获取日期的星期。失败返回null。
     * @param date 日期字符串
     * @return 星期
     */
    public static Week getWeek(String date) {
        Week week = null;
        DateStyle dateStyle = getDateStyle(date);
        if (dateStyle != null) {
            Date myDate = stringToDate(date, dateStyle);
            week = getWeek(myDate);
        }
        return week;
    }

    /**
     * 获取日期的星期。失败返回null。
     * @param date 日期
     * @return 星期
     */
    public static Week getWeek(Date date) {
        Week week = null;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int weekNumber = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        switch (weekNumber) {
            case 0:
                week = Week.SUNDAY;
                break;
            case 1:
                week = Week.MONDAY;
                break;
            case 2:
                week = Week.TUESDAY;
                break;
            case 3:
                week = Week.WEDNESDAY;
                break;
            case 4:
                week = Week.THURSDAY;
                break;
            case 5:
                week = Week.FRIDAY;
                break;
            case 6:
                week = Week.SATURDAY;
                break;
        }
        return week;
    }

    /**
     * 获取两个日期相差的天数
     * @param date 日期字符串
     * @param otherDate 另一个日期字符串
     * @return 相差天数。如果失败则返回-1
     */
    public static int getIntervalDays(String date, String otherDate) {
        return getIntervalDays(stringToDate(date), stringToDate(otherDate));
    }

    /**
     * @param date 日期
     * @param otherDate 另一个日期
     * @return 相差天数。如果失败则返回-1
     */
    public static int getIntervalDays(Date date, Date otherDate) {
        int num = -1;
        Date dateTmp = CalendarUtils.stringToDate(CalendarUtils.getDate(date), DateStyle.YYYY_MM_DD);
        Date otherDateTmp = CalendarUtils.stringToDate(CalendarUtils.getDate(otherDate), DateStyle.YYYY_MM_DD);
        if (dateTmp != null && otherDateTmp != null) {
            long time = (dateTmp.getTime() - otherDateTmp.getTime());
            num = (int) (time / (24 * 60 * 60 * 1000));
        }
        return num;
    }
    /**
     *
     * @param date 日期
     * @param otherDate 另一个日期
     * @return dateStyle格式的字符串时间差
     * displayAll 是否全部显示，默认显示全部,如果为true，如：0年0月9天，为false则为：9天，前边的0不显示
     */
    public static String getIntervalTime(Date date,Date otherDate,DateStyle dateStyle,boolean displayAll){
        String result = dateStyle.getValue();
        int year = 0,month = 0,day = 0,hour = 0,minute = 0,second = 0;
        Date bigDate = null;
        Date smallDate = null;
        if(date.compareTo(otherDate)>0){
            bigDate = date;
            smallDate = otherDate;
        }else if(date.compareTo(otherDate)<0){
            bigDate = otherDate;
            smallDate = date;
        }
        int startYear = getYear(smallDate);
        int startMonth = getMonth(smallDate);
        int startDay = getDay(smallDate);
        calendar.setTime(smallDate);
        int startDayOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        int startHour = getHour(smallDate);
        int startMinute = getMinute(smallDate);
        int startSecond = getSecond(smallDate);

        int endYear = getYear(bigDate);
        int endMonth = getMonth(bigDate);
        int endDay = getDay(bigDate);
        calendar.setTime(bigDate);
        int endDayOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        int endHour = getHour(bigDate);
        int endMinute = getMinute(bigDate);
        int endSecond = getSecond(bigDate);

        second = endSecond-startSecond;
        if(second<0){
            endMinute = endMinute-1;
            second = second+60;
        }
        minute = endMinute-startMinute;
        if(minute<0){
            endHour = endHour-1;
            minute = minute+60;
        }
        hour = endHour -startHour;
        if(hour<0){
            endDay = endDay-1;
            hour = hour+24;
        }
        day = endDay-startDay;
        if(day<0){
            endMonth = endMonth-1;
            day = startDayOfMonth+day;
        }
        month = endMonth-startMonth;
        if(month<0){
            endYear = endYear-1;
            month = month + 12;
        }
        year = endYear - startYear;
            if(!displayAll){
                if(year==0){
                    if(result.indexOf("MM")>0)
                        result = result.substring(result.indexOf("MM"));
                }
                if(month == 0){
                    result = (result.indexOf("MM")>0?result.substring(0, result.indexOf("MM")):result)+(result.indexOf("dd")>0?result.substring(result.indexOf("dd")):result);
                }
                if(day ==0){
                    result = result.indexOf("dd")>0?result.substring(0, result.indexOf("dd")):result+(result.indexOf("HH")>0?result.substring(result.indexOf("HH")):result);
                }
                if(hour == 0){
                    result = result.indexOf("HH")>0?result.substring(0, result.indexOf("HH")):result+(result.indexOf("mm")>0?result.substring(result.indexOf("mm")):result);
                }
                if(minute == 0){
                    result = result.indexOf("mm")>0?result.substring(0, result.indexOf("mm")):result+(result.indexOf("ss")>0?result.substring(result.indexOf("ss")):result);
                }
                if(second == 0){
                    if(result.indexOf("ss")>0)
                        result = result.substring(0, result.indexOf("ss"));
                }
            }

        return result.replace("yyyy", ""+year).replace("MM", ""+month).replace("dd", ""+day)
                .replace("HH", ""+hour).replace("mm", ""+minute).replace("ss", ""+second);
    }
    public enum DateStyle {

        YYYY_MM("yyyy-MM", false),
        YYYY_MM_DD("yyyy-MM-dd", false),
        YYYY_MM_DD_HH_MM("yyyy-MM-dd HH:mm", false),
        YYYY_MM_DD_HH_MM_SS("yyyy-MM-dd HH:mm:ss", false),

        YYYY_MM_EN("yyyy/MM", false),
        YYYY_MM_DD_EN("yyyy/MM/dd", false),
        YYYY_MM_DD_HH_MM_EN("yyyy/MM/dd HH:mm", false),
        YYYY_MM_DD_HH_MM_SS_EN("yyyy/MM/dd HH:mm:ss", false),

        YYYY_MM_CN("yyyy年MM月", false),
        YYYY_MM_DD_CN("yyyy年MM月dd日", false),
        YYYY_MM_DD_HH_MM_CN("yyyy年MM月dd日 HH:mm", false),
        YYYY_MM_DD_HH_MM_SS_CN("yyyy年MM月dd日 HH:mm:ss", false),

        HH_MM("HH:mm", true),
        HH_MM_SS("HH:mm:ss", true),

        MM_DD("MM-dd", true),
        MM_DD_HH_MM("MM-dd HH:mm", true),
        MM_DD_HH_MM_SS("MM-dd HH:mm:ss", true),

        MM_DD_EN("MM/dd", true),
        MM_DD_HH_MM_EN("MM/dd HH:mm", true),
        MM_DD_HH_MM_SS_EN("MM/dd HH:mm:ss", true),

        MM_DD_CN("MM月dd日", true),
        MM_DD_HH_MM_CN("MM月dd日 HH:mm", true),
        MM_DD_HH_MM_SS_CN("MM月dd日 HH:mm:ss", true),


        YY_MM_CN("yyyy年MM个月",true),
        YY_MM_DD_CN("yyyy年MM个月dd天",true),
        YY_MM_DD_HH_CN("yyyy年MM个月dd天HH小时",true),
        YY_MM_DD_HH_MM_CN("yyyy年MM个月dd天HH小时mm分",true),
        YY_MM_DD_HH_MM_SS_CN("yyyy年MM个月dd天HH小时mm分ss秒",true);

        private String value;

        private boolean isShowOnly;

        DateStyle(String value, boolean isShowOnly) {
            this.value = value;
            this.isShowOnly = isShowOnly;
        }

        public String getValue() {
            return value;
        }

        public boolean isShowOnly() {
            return isShowOnly;
        }
    }
    public enum Week {

        MONDAY("星期一","一", "Monday", "Mon.", 1),
        TUESDAY("星期二","二", "Tuesday", "Tues.", 2),
        WEDNESDAY("星期三","三", "Wednesday", "Wed.", 3),
        THURSDAY("星期四","四", "Thursday", "Thur.", 4),
        FRIDAY("星期五","五", "Friday", "Fri.", 5),
        SATURDAY("星期六","六", "Saturday", "Sat.", 6),
        SUNDAY("星期日","日", "Sunday", "Sun.", 7);

        String name_cn;
        String name_cnShort;
        String name_en;
        String name_enShort;
        int number;

        Week(String name_cn,String name_cnShort, String name_en, String name_enShort, int number) {
            this.name_cn = name_cn;
            this.name_cnShort = name_cnShort;
            this.name_en = name_en;
            this.name_enShort = name_enShort;
            this.number = number;
        }

        public String getChineseName() {
            return name_cn;
        }
        public String getChineseShortName() {
            return name_cnShort;
        }

        public String getName() {
            return name_en;
        }

        public String getShortName() {
            return name_enShort;
        }

        public int getNumber() {
            return number;
        }
    }

    /**
     * 获取日期所在月的第一天的日期
     * @param date
     * @return
     */
    public static Date getFirstDayOfMonth(Date date){
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        return calendar.getTime();
    }

    /**
     * 获取日期所在月的最后一天的日期
     * @param date
     * @return
     */
    public static Date getEndDayOfMonth(Date date){
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(calendar.DAY_OF_MONTH));
        return calendar.getTime();
    }

    /**
     * 获取日期所在周的第一天的日期
     * @param date
     * @return
     */
    public static Date getFirstDayOfWeek(Date date){
        //设置一周的第一天是星期几
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        return calendar.getTime();
    }
    /**
     * 获取日期所在周的最后一天的日期
     * @param date
     * @return
     */
    public static Date getEndDayOfWeek(Date date){
        //设置一周的第一天是星期几
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);

        return calendar.getTime();
    }
    /**
     * 获取日期所在年的第一天的日期
     * @param date
     * @return
     */
    public static Date getFirstDayOfYear(Date date){
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_YEAR, 1);
        return calendar.getTime();
    }

    /**
     * 获取日期是星期中第几天
     * @param date
     * @return
     */
    public static int getDayOfWeek(Date date){
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    /**
     * 获取日期是月中第几天
     * @param date
     * @return
     */
    public static int getDayOfMonth(Date date){
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 获取日期是年中第几天
     * @param date
     * @return
     */
    public static int getDayOfYear(Date date){
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_YEAR);
    }

    /**
     * 获取该日期当月有几天
     * @param date
     * @return
     */
    public static int getDaysOfMonth(Date date){
        calendar.setTime(date);
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    /**
     * 获取日期是月中第几周
     * @param date
     * @return
     */
    public static int getWeekOfMonth(Date date){
        calendar.setTime(date);
        //设置一周的第一天是星期几
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        return calendar.get(Calendar.WEEK_OF_MONTH);
    }

    /**
     * 获取日期是年中第几周
     * @param date
     * @return
     */
    public static int getWeekOfYear(Date date){
        calendar.setTime(date);
        //设置一周的第一天是星期几
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        return calendar.get(Calendar.WEEK_OF_YEAR);
    }
}
