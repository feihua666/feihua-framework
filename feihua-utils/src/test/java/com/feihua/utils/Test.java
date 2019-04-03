package com.feihua.utils;

import com.feihua.utils.calendar.CalendarUtils;
import com.feihua.utils.calendar.LunarCalendarUtils;
import com.feihua.utils.calendar.LunarDate;
import com.feihua.utils.http.httpServletRequest.RequestUtils;
import com.feihua.utils.http.httpclient.HttpClientUtils;
import com.feihua.utils.io.FileUtils;
import com.feihua.utils.pdf.PdfUtils;
import com.feihua.utils.pdf.SwfToolUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.feihua.utils.calendar.LunarCalendarUtils.getChinaConstellation;

/**
 * Created by yangwei
 * Created at 2018/3/21 10:59
 */
public class Test {
    public static void main(String[] args) throws IOException {
        //System.out.println(FileUtils.getCanonicalPath("context/news/main/../xx.html"));

        //System.out.println(RequestUtils.convertToSlash("\\WEB-INF\\template-cms\\default\\index.html"));

        //System.out.println(HttpClientUtils.httpGet("https://www.toutiao.com/a6634127568062644744/"));
        System.out.println(CalendarUtils.getIntervalDays("2019-04-03","2019-04-03"));
        System.out.println(CalendarUtils.getIntervalDays("2019-04-03","2019-04-02"));
        System.out.println(Math.abs(CalendarUtils.getIntervalDays("2019-04-02","2019-04-03")));
        System.out.println( Math.abs(CalendarUtils.getIntervalDays(new Date(),new Date())));

    }
}
