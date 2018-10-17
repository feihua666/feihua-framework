package com.feihua.utils;

import com.feihua.utils.calendar.CalendarUtils;
import com.feihua.utils.calendar.LunarCalendarUtils;
import com.feihua.utils.calendar.LunarDate;

import java.util.Date;

import static com.feihua.utils.calendar.LunarCalendarUtils.getChinaConstellation;

/**
 * Created by yangwei
 * Created at 2018/3/21 10:59
 */
public class Test {
    public static void main(String[] args) throws Exception {

        LunarDate lunarDate = LunarCalendarUtils.stringTolunarDate("2018-02-29 12:12");
        System.out.println(lunarDate);
    }
}
