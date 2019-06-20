package com.feihua.utils;

import com.feihua.utils.calendar.CalendarUtils;
import com.feihua.utils.json.JSONUtils;
import org.apache.commons.lang3.BooleanUtils;

import java.util.*;

/**
 * Created by yangwei
 * Created at 2018/3/21 10:59
 */
public class Test {
    public static void main(String[] args) throws Exception {
        //System.out.println(FileUtils.getCanonicalPath("context/news/main/../xx.html"));

        //System.out.println(RequestUtils.convertToSlash("\\WEB-INF\\template-cms\\default\\index.html"));

        //System.out.println(HttpClientUtils.httpGet("https://www.toutiao.com/a6634127568062644744/"));
        //System.out.println(CalendarUtils.getIntervalDays("2019-04-03","2019-04-03"));
        //System.out.println(CalendarUtils.getIntervalDays("2019-04-03","2019-04-02"));
        //System.out.println(Math.abs(CalendarUtils.getIntervalDays("2019-04-02","2019-04-03")));
        //System.out.println( Math.abs(CalendarUtils.getIntervalDays(new Date(),new Date())));

        /*Map<String,String> map = new HashMap<>();
        map.put("loginType","ACCOUNT");
        map.put("loginClient","pc");
        map.put("principal","superAdmin");
        map.put("password","1q2w3e4r");

        String r = HttpClientUtils.httpPost("http://master.com/api/login",map);
        System.out.println(r);
        String rr = HttpClientUtils.httpGet("http://master.com/api/base/user/current");
        System.out.println(rr);*/


        System.out.println(CalendarUtils.showTime(CalendarUtils.getCurrentDate(),CalendarUtils.getCurrentDate()));;
        System.out.println(CalendarUtils.showTime(CalendarUtils.stringToDate("2019-06-18 09:06:00"),CalendarUtils.getCurrentDate()));;
        System.out.println(CalendarUtils.showTime(CalendarUtils.stringToDate("2019-06-18"),CalendarUtils.getCurrentDate()));;
        System.out.println(CalendarUtils.showTime(CalendarUtils.stringToDate("2019-06-16"),CalendarUtils.getCurrentDate()));;
        System.out.println(CalendarUtils.showTime(CalendarUtils.stringToDate("2019-05-16"),CalendarUtils.getCurrentDate()));;
        System.out.println(CalendarUtils.showTime(CalendarUtils.stringToDate("2018-06-16"),CalendarUtils.getCurrentDate()));;


        System.out.println(((Long) System.currentTimeMillis()));
    }
}
