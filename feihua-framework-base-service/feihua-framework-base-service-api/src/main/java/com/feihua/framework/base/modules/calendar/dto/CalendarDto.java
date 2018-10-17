package com.feihua.framework.base.modules.calendar.dto;

import com.feihua.utils.calendar.LunarCalendarUtils;
import com.feihua.utils.calendar.LunarDate;

import java.util.Date;

/**
 * Created by yangwei
 * Created at 2018/9/20 15:17
 */
public class CalendarDto {
    private Date date;
    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;
    private int second;
    private LunarDate lunarDate;
    private int lunarYear;
    private int lunarMonth;
    private int lunarDay;
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
    private LunarCalendarUtils.SymbolicAnimals animal;
    private LunarCalendarUtils.Tiangan tianganYear;
    private LunarCalendarUtils.Dizhi dizhiYear;

    private LunarCalendarUtils.Tiangan tianganMonth;
    private LunarCalendarUtils.Dizhi dizhiMonth;

    private LunarCalendarUtils.Tiangan tianganDay;
    private LunarCalendarUtils.Dizhi dizhiDay;

    private LunarCalendarUtils.Tiangan tianganHour;
    private LunarCalendarUtils.Dizhi dizhiHour;

    private LunarCalendarUtils.SolarTerm24 solarTerm24;

    private LunarCalendarUtils.Constellation constellation;

    private LunarCalendarUtils.WeekSolarHoliday weekSolarHoliday;

    private LunarCalendarUtils.SolarHoliday solarHoliday;

    private  LunarCalendarUtils.TraditionalFestival traditionalFestival;

    private LunarCalendarUtils.ChinaConstellation chinaConstellation;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public LunarDate getLunarDate() {
        return lunarDate;
    }

    public void setLunarDate(LunarDate lunarDate) {
        this.lunarDate = lunarDate;
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

    public LunarCalendarUtils.SymbolicAnimals getAnimal() {
        return animal;
    }

    public void setAnimal(LunarCalendarUtils.SymbolicAnimals animal) {
        this.animal = animal;
    }

    public LunarCalendarUtils.Tiangan getTianganYear() {
        return tianganYear;
    }

    public void setTianganYear(LunarCalendarUtils.Tiangan tianganYear) {
        this.tianganYear = tianganYear;
    }

    public LunarCalendarUtils.Dizhi getDizhiYear() {
        return dizhiYear;
    }

    public void setDizhiYear(LunarCalendarUtils.Dizhi dizhiYear) {
        this.dizhiYear = dizhiYear;
    }

    public LunarCalendarUtils.Tiangan getTianganMonth() {
        return tianganMonth;
    }

    public void setTianganMonth(LunarCalendarUtils.Tiangan tianganMonth) {
        this.tianganMonth = tianganMonth;
    }

    public LunarCalendarUtils.Dizhi getDizhiMonth() {
        return dizhiMonth;
    }

    public void setDizhiMonth(LunarCalendarUtils.Dizhi dizhiMonth) {
        this.dizhiMonth = dizhiMonth;
    }

    public LunarCalendarUtils.Tiangan getTianganDay() {
        return tianganDay;
    }

    public void setTianganDay(LunarCalendarUtils.Tiangan tianganDay) {
        this.tianganDay = tianganDay;
    }

    public LunarCalendarUtils.Dizhi getDizhiDay() {
        return dizhiDay;
    }

    public void setDizhiDay(LunarCalendarUtils.Dizhi dizhiDay) {
        this.dizhiDay = dizhiDay;
    }

    public LunarCalendarUtils.Tiangan getTianganHour() {
        return tianganHour;
    }

    public void setTianganHour(LunarCalendarUtils.Tiangan tianganHour) {
        this.tianganHour = tianganHour;
    }

    public LunarCalendarUtils.Dizhi getDizhiHour() {
        return dizhiHour;
    }

    public void setDizhiHour(LunarCalendarUtils.Dizhi dizhiHour) {
        this.dizhiHour = dizhiHour;
    }

    public LunarCalendarUtils.SolarTerm24 getSolarTerm24() {
        return solarTerm24;
    }

    public void setSolarTerm24(LunarCalendarUtils.SolarTerm24 solarTerm24) {
        this.solarTerm24 = solarTerm24;
    }

    public LunarCalendarUtils.Constellation getConstellation() {
        return constellation;
    }

    public void setConstellation(LunarCalendarUtils.Constellation constellation) {
        this.constellation = constellation;
    }

    public LunarCalendarUtils.WeekSolarHoliday getWeekSolarHoliday() {
        return weekSolarHoliday;
    }

    public void setWeekSolarHoliday(LunarCalendarUtils.WeekSolarHoliday weekSolarHoliday) {
        this.weekSolarHoliday = weekSolarHoliday;
    }

    public LunarCalendarUtils.SolarHoliday getSolarHoliday() {
        return solarHoliday;
    }

    public void setSolarHoliday(LunarCalendarUtils.SolarHoliday solarHoliday) {
        this.solarHoliday = solarHoliday;
    }

    public LunarCalendarUtils.TraditionalFestival getTraditionalFestival() {
        return traditionalFestival;
    }

    public void setTraditionalFestival(LunarCalendarUtils.TraditionalFestival traditionalFestival) {
        this.traditionalFestival = traditionalFestival;
    }

    public LunarCalendarUtils.ChinaConstellation getChinaConstellation() {
        return chinaConstellation;
    }

    public void setChinaConstellation(LunarCalendarUtils.ChinaConstellation chinaConstellation) {
        this.chinaConstellation = chinaConstellation;
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
}
