package com.feihua.utils;

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

        String aa = "{\n" +
                "\"content\":\"ccccccccc\",\n" +
                "\"title\":\"ttttttttt\",\n" +
                "\"profile\":\"pppppppp\"\n" +
                "}";
        Map ssss = JSONUtils.json2map(aa,String.class);
        System.out.println(ssss);
        System.out.println(BooleanUtils.toBoolean("true"));

    }
}
