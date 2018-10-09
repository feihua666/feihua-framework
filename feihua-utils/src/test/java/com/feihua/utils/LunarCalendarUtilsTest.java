package com.feihua.utils;

import com.feihua.utils.calendar.CalendarUtils;
import com.feihua.utils.calendar.LunarCalendarUtils;
import com.feihua.utils.calendar.LunarDate;

import java.util.Date;

import static com.feihua.utils.calendar.LunarCalendarUtils.getTraditionalFestival;
import static com.feihua.utils.calendar.LunarCalendarUtils.lunarDateToString;

/**
 * Created by yangwei
 * Created at 2018/9/14 13:22
 */
public class LunarCalendarUtilsTest {
    public static void main(String[] args) throws Exception {

        System.out.println("==========================分割线================================================");
        // 标准日期
        //String strDate = "2017-03-25";
        // 该工具类支持的最早日期为 1900-01-31
        //String strDate = "1900-01-31";
        // 该工具类支持的最晚日期为 2049-12-31
        //String strDate = "2049-12-31";
        String strDate = "2019-01-13 22:20";
        Date date = CalendarUtils.stringToDate(strDate);
        int year = CalendarUtils.getYear(date);
        int month = CalendarUtils.getMonth(date);
        int day = CalendarUtils.getDay(date);
        System.out.println("==========================分割线================================================");
        //转农历
        LunarDate lunarDate = LunarCalendarUtils.CalendarToLunar(date);
        int lunarYear = lunarDate.getYear();
        int lunarMonth = lunarDate.getMonth();
        int lunarDay = lunarDate.getDay();

        int lunarHour = lunarDate.getHour();
        int lunarMinute = lunarDate.getMinute();


        System.out.println("==========================分割线================================================");
        //闰月
        int leapMonth = LunarCalendarUtils.leapMonth(year);
        if (leapMonth == 0) {
            System.out.println("没有闰月");
        }else{
            System.out.println("闰"+ leapMonth +"月");

        }
        System.out.println("==========================分割线================================================");
        // 动物年
        LunarCalendarUtils.SymbolicAnimals animals = LunarCalendarUtils.symbolicAnimalsYear(lunarYear);
        System.out.println(animals.getChineseName() + "年");

        System.out.println("==========================分割线================================================");
        // 天干地支年
        LunarCalendarUtils.Tiangan tiangan = LunarCalendarUtils.tianganYear(lunarYear);
        LunarCalendarUtils.Dizhi dizhi = LunarCalendarUtils.dizhiYear(lunarYear);
        System.out.println(tiangan.getChineseName() + dizhi.getChineseName() + "年");
        System.out.println("==========================分割线================================================");
        // 天干地支月
        LunarCalendarUtils.Tiangan tianganMonth = LunarCalendarUtils.tianganMonth(lunarYear,lunarMonth);
        LunarCalendarUtils.Dizhi dizhiMonth = LunarCalendarUtils.dizhiMonth(lunarMonth);
        System.out.println(tianganMonth.getChineseName() + dizhiMonth.getChineseName() + "月");
        System.out.println("==========================分割线================================================");
        // 天干地支日
        LunarCalendarUtils.Tiangan tianganDay = LunarCalendarUtils.tianganDay(lunarYear,lunarMonth,lunarDay);
        LunarCalendarUtils.Dizhi dizhiDay = LunarCalendarUtils.dizhiDay(lunarYear,lunarMonth,lunarDay);
        System.out.println(tianganDay.getChineseName() + dizhiDay.getChineseName() + "日");
        System.out.println("==========================分割线================================================");
        // 天干地支时辰
        LunarCalendarUtils.Tiangan tianganHour = LunarCalendarUtils.tianganHour(lunarYear,lunarMonth,lunarDay,lunarHour,lunarMinute);
        LunarCalendarUtils.Dizhi dizhiHour = LunarCalendarUtils.dizhiHour(lunarHour,lunarMinute);
        System.out.println(tianganHour.getChineseName() + dizhiHour.getChineseName() + "时");
        System.out.println("==========================分割线================================================");

        // 日转汉字
        LunarCalendarUtils.ChinaBaseTen ten = LunarCalendarUtils.ChinaBaseTen.getByDay(lunarDay);
        LunarCalendarUtils.ChineseNumber number = LunarCalendarUtils.ChineseNumber.getByDay(lunarDay);

        System.out.println("日转汉字 " + (ten.getChineseName() + number.getChineseName()));

        System.out.println("==========================分割线================================================");
        // 公历转农历
        LunarDate lunar = LunarCalendarUtils.CalendarToLunar(date);
        System.out.println("农历 " + lunarDateToString(lunar, CalendarUtils.DateStyle.YYYY_MM_DD_CN));

        System.out.println("==========================分割线================================================");
        // 农历转公历

        Date todate = LunarCalendarUtils.LunarToCalendar(lunar);
        System.out.println("转公历 "+CalendarUtils.dateToString(todate, CalendarUtils.DateStyle.YYYY_MM_DD));

        System.out.println("==========================分割线================================================");
        // 24节气

        LunarCalendarUtils.SolarTerm24 solarTerm24 = LunarCalendarUtils.getSolarTerm24(date);
        System.out.println("节气 " + (solarTerm24==null?"无":solarTerm24.getChineseName()));


        System.out.println("==========================分割线================================================");
        // 星座
        LunarCalendarUtils.Constellation constellation = LunarCalendarUtils.getConstellation(date);
        System.out.println(constellation.getChineseName());

        System.out.println("==========================分割线================================================");
        //某月第几周星期几节日
        LunarCalendarUtils.WeekSolarHoliday weekSolarHoliday = (LunarCalendarUtils.getWeekHoliday(date));
        System.out.println("按星期的节日 " + (weekSolarHoliday==null ? "无" : weekSolarHoliday.getChineseName()));

        System.out.println("==========================分割线================================================");

        //公历节日
        LunarCalendarUtils.SolarHoliday solarHoliday = LunarCalendarUtils.getSolarHoliday(date);
        System.out.println("公历节日 " + (solarHoliday==null?"无" : solarHoliday.getChineseName()));
        System.out.println("==========================分割线================================================");


        LunarCalendarUtils.TraditionalFestival traditionalFestival = getTraditionalFestival( lunarYear, lunarMonth, lunarDay);
        System.out.println("农历节日 " + (traditionalFestival==null?"无" : traditionalFestival.getChineseName()));

        System.out.println("==========================分割线================================================");
        // 二十八星宿
        LunarCalendarUtils.ChinaConstellation chinaConstellation = LunarCalendarUtils.getChinaConstellation(lunarMonth, lunarDay);
        System.out.println("二十八星宿 " + chinaConstellation.getChineseName());

        System.out.println(CalendarUtils.getWeekOfYear(new Date()));
        System.out.println(CalendarUtils.getDayOfMonth(new Date()));
        System.out.println(CalendarUtils.getDayOfWeek(new Date()));
        System.out.println(CalendarUtils.getDayOfYear(new Date()));
        Date date1 = new Date(2018,2,29,0,0,0);
        date1 = CalendarUtils.stringToDate("2018-2-29 0:0:0", CalendarUtils.DateStyle.YYYY_MM_DD_HH_MM_SS);
        System.out.println(date1);

    }
}
