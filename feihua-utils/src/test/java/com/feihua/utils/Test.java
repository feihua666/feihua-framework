package com.feihua.utils;

import com.feihua.utils.calendar.CalendarUtils;
import com.feihua.utils.calendar.LunarCalendarUtils;
import com.feihua.utils.calendar.LunarDate;
import com.feihua.utils.pdf.PdfUtils;
import com.feihua.utils.pdf.SwfToolUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.feihua.utils.calendar.LunarCalendarUtils.getChinaConstellation;

/**
 * Created by yangwei
 * Created at 2018/3/21 10:59
 */
public class Test {
    public static void main(String[] args) throws Exception {

        /*LunarDate lunarDate = LunarCalendarUtils.stringTolunarDate("2018-02-29 12:12");
        System.out.println(lunarDate);

        System.out.println(CalendarUtils.getDayOfWeek(CalendarUtils.stringToDate("2018-10-11")));
        System.out.println(CalendarUtils.getDayOfWeek(CalendarUtils.stringToDate("2018-10-12")));
        System.out.println(CalendarUtils.getDayOfWeek(CalendarUtils.stringToDate("2018-10-13")));
        System.out.println(CalendarUtils.getDayOfWeek(CalendarUtils.stringToDate("2018-10-14")));*/
        PdfUtils.pdf2Image("d:/test/3ed87ef8-b8f7-4bc1-aebe-b31593553954-pdf.pdf","d:/test/img",
                "png",100);
        List<String> pngPaths = new ArrayList<>();
        pngPaths.add("d:/test/img/*.png");
        SwfToolUtils.pngToSwf("C:\\Program Files (x86)\\SWFTools\\png2swf.exe",
                "d:/test/img/",
                "d:/test/swf.swf");
    }
}
