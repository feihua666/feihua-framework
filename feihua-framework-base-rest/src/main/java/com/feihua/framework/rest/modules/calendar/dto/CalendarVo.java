package com.feihua.framework.rest.modules.calendar.dto;

import com.feihua.framework.base.modules.calendar.dto.CalendarDto;
import com.feihua.utils.calendar.CalendarUtils;
import com.feihua.utils.calendar.LunarCalendarUtils;
import com.feihua.utils.calendar.LunarDate;

import java.util.Date;

/**
 * Created by yangwei
 * Created at 2018/9/20 17:58
 */
public class CalendarVo {

    private static final String shu = "属";
    private static final String nian = "年";
    private static final String yue = "月";
    private static final String ri = "日";
    private static final String shi = "时";

    public CalendarVo(){}
    public CalendarVo(CalendarDto calendarDto){
        this.date = calendarDto.getDate();
        this.year = calendarDto.getYear();
        this.month = calendarDto.getMonth();
        this.day = calendarDto.getDay();
        this.hour = calendarDto.getHour();
        this.minute = calendarDto.getMinute();
        this.second = calendarDto.getSecond();
        this.week = calendarDto.getWeek();
        this.lunarDate = calendarDto.getLunarDate();
        this.lunarDateStr = LunarCalendarUtils.lunarDateToString(lunarDate, CalendarUtils.DateStyle.YYYY_MM_DD_HH_MM_SS_CN);
        this.lunarYear = calendarDto.getLunarYear();
        this.lunarMonth = calendarDto.getLunarMonth();
        this.lunarMonthStr = LunarCalendarUtils.ChineseNumber.getByNumber(lunarMonth).getMonthName();
        this.lunarDay = calendarDto.getLunarDay();
        this.lunarDayStr = LunarCalendarUtils.ChinaBaseTen.getByDay(lunarDay).getChineseName() + LunarCalendarUtils.ChineseNumber.getByDay(lunarDay).getChineseName();
        this.lunarHour = calendarDto.getLunarHour();
        this.lunarMinute = calendarDto.getLunarMinute();
        this.lunarSecond = calendarDto.getLunarSecond();
        this.leap = calendarDto.isLeap();
        this.leapMonth = calendarDto.getLeapMonth();
        this.currentLeap = calendarDto.isCurrentLeap();

        this.weekOfYear = CalendarUtils.getWeekOfYear(calendarDto.getDate());
        this.dayOfYear = CalendarUtils.getDayOfYear(calendarDto.getDate());

        this.animal = shu + calendarDto.getAnimal().getChineseName();
        this.ganzhiYear = calendarDto.getTianganYear().getChineseName() + calendarDto.getDizhiYear().getChineseName() + nian;
        this.ganzhiMonth = calendarDto.getTianganMonth().getChineseName() + calendarDto.getDizhiMonth().getChineseName() + yue;
        this.ganzhiDay = calendarDto.getTianganDay().getChineseName() + calendarDto.getDizhiDay().getChineseName() + ri;
        this.ganzhiHour = calendarDto.getTianganHour().getChineseName() + calendarDto.getDizhiHour().getChineseName() + shi;
        if (calendarDto.getSolarTerm24() != null) {
            this.solarTerm24 = calendarDto.getSolarTerm24().getChineseName() ;
        }
        if (calendarDto.getConstellation() != null) {
            this.constellation = calendarDto.getConstellation().getChineseName();
        }
        if (calendarDto.getWeekSolarHoliday() != null) {
            this.weekSolarHoliday = calendarDto.getWeekSolarHoliday().getChineseName();
        }
        if (calendarDto.getSolarHoliday() != null) {
            this.solarHoliday = calendarDto.getSolarHoliday().getChineseName();
        }
        if (calendarDto.getTraditionalFestival() != null) {
            this.traditionalFestival = calendarDto.getTraditionalFestival().getChineseName();
        }
        if (calendarDto.getChinaConstellation() != null) {
            this.chinaConstellation = calendarDto.getChinaConstellation().getChineseName();
        }
    }
    private Date date;
    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;
    private int second;
    private int week;
    private LunarDate lunarDate;
    private String lunarDateStr;
    private int lunarYear;
    private int lunarMonth;
    private String lunarMonthStr;
    private int lunarDay;
    private String lunarDayStr;
    private int lunarHour;
    private int lunarMinute;
    private int lunarSecond;
    /**
     * 该年是否闰月
     */
    private boolean leap;
    /**
     * 如果闰月，闰的是几月
     */
    private int leapMonth;
    /**
     * 当前日期是否在闰月中
     */
    private boolean currentLeap;
    private int weekOfYear;
    private int dayOfYear;
    private String animal;
    private String ganzhiYear;
    private String ganzhiMonth;
    private String ganzhiDay;
    private String ganzhiHour;
    private String solarTerm24;
    private String constellation;
    private String weekSolarHoliday;
    private String solarHoliday;
    private String traditionalFestival;
    private String chinaConstellation;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public int getSecond() {
        return second;
    }

    public void setSecond(int second) {
        this.second = second;
    }

    public LunarDate getLunarDate() {
        return lunarDate;
    }

    public void setLunarDate(LunarDate lunarDate) {
        this.lunarDate = lunarDate;
    }

    public String getLunarDateStr() {
        return lunarDateStr;
    }

    public void setLunarDateStr(String lunarDateStr) {
        this.lunarDateStr = lunarDateStr;
    }

    public int getLunarYear() {
        return lunarYear;
    }

    public void setLunarYear(int lunarYear) {
        this.lunarYear = lunarYear;
    }

    public int getLunarMonth() {
        return lunarMonth;
    }

    public void setLunarMonth(int lunarMonth) {
        this.lunarMonth = lunarMonth;
    }

    public int getLunarDay() {
        return lunarDay;
    }

    public void setLunarDay(int lunarDay) {
        this.lunarDay = lunarDay;
    }

    public String getLunarDayStr() {
        return lunarDayStr;
    }

    public void setLunarDayStr(String lunarDayStr) {
        this.lunarDayStr = lunarDayStr;
    }

    public int getLunarHour() {
        return lunarHour;
    }

    public void setLunarHour(int lunarHour) {
        this.lunarHour = lunarHour;
    }

    public int getLunarMinute() {
        return lunarMinute;
    }

    public void setLunarMinute(int lunarMinute) {
        this.lunarMinute = lunarMinute;
    }

    public int getLunarSecond() {
        return lunarSecond;
    }

    public void setLunarSecond(int lunarSecond) {
        this.lunarSecond = lunarSecond;
    }

    public boolean isLeap() {
        return leap;
    }

    public void setLeap(boolean leap) {
        this.leap = leap;
    }

    public int getLeapMonth() {
        return leapMonth;
    }

    public void setLeapMonth(int leapMonth) {
        this.leapMonth = leapMonth;
    }

    public boolean isCurrentLeap() {
        return currentLeap;
    }

    public void setCurrentLeap(boolean currentLeap) {
        this.currentLeap = currentLeap;
    }

    public String getAnimal() {
        return animal;
    }

    public void setAnimal(String animal) {
        this.animal = animal;
    }

    public String getGanzhiYear() {
        return ganzhiYear;
    }

    public void setGanzhiYear(String ganzhiYear) {
        this.ganzhiYear = ganzhiYear;
    }

    public String getGanzhiMonth() {
        return ganzhiMonth;
    }

    public void setGanzhiMonth(String ganzhiMonth) {
        this.ganzhiMonth = ganzhiMonth;
    }

    public String getGanzhiDay() {
        return ganzhiDay;
    }

    public void setGanzhiDay(String ganzhiDay) {
        this.ganzhiDay = ganzhiDay;
    }

    public String getGanzhiHour() {
        return ganzhiHour;
    }

    public void setGanzhiHour(String ganzhiHour) {
        this.ganzhiHour = ganzhiHour;
    }

    public String getSolarTerm24() {
        return solarTerm24;
    }

    public void setSolarTerm24(String solarTerm24) {
        this.solarTerm24 = solarTerm24;
    }

    public String getConstellation() {
        return constellation;
    }

    public void setConstellation(String constellation) {
        this.constellation = constellation;
    }

    public String getWeekSolarHoliday() {
        return weekSolarHoliday;
    }

    public void setWeekSolarHoliday(String weekSolarHoliday) {
        this.weekSolarHoliday = weekSolarHoliday;
    }

    public String getSolarHoliday() {
        return solarHoliday;
    }

    public void setSolarHoliday(String solarHoliday) {
        this.solarHoliday = solarHoliday;
    }

    public String getTraditionalFestival() {
        return traditionalFestival;
    }

    public void setTraditionalFestival(String traditionalFestival) {
        this.traditionalFestival = traditionalFestival;
    }

    public String getChinaConstellation() {
        return chinaConstellation;
    }

    public void setChinaConstellation(String chinaConstellation) {
        this.chinaConstellation = chinaConstellation;
    }

    public int getWeekOfYear() {
        return weekOfYear;
    }

    public void setWeekOfYear(int weekOfYear) {
        this.weekOfYear = weekOfYear;
    }

    public int getDayOfYear() {
        return dayOfYear;
    }

    public void setDayOfYear(int dayOfYear) {
        this.dayOfYear = dayOfYear;
    }

    public int getWeek() {
        return week;
    }

    public void setWeek(int week) {
        this.week = week;
    }

    public String getLunarMonthStr() {
        return lunarMonthStr;
    }

    public void setLunarMonthStr(String lunarMonthStr) {
        this.lunarMonthStr = lunarMonthStr;
    }
}
