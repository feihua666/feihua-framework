package com.feihua.utils;

import com.feihua.utils.calendar.CalendarUtils;
import com.feihua.utils.calendar.LunarCalendarUtils;
import com.feihua.utils.calendar.LunarDate;
import com.feihua.utils.http.httpServletRequest.RequestUtils;
import com.feihua.utils.io.FileUtils;
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
    public static void main(String[] args) {
        System.out.println(FileUtils.getCanonicalPath("context/news/main/../xx.html"));

        System.out.println(RequestUtils.convertToSlash("\\WEB-INF\\template-cms\\default\\index.html"));
    }
}
