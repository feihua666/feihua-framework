package com.feihua.framework.message;

import com.feihua.utils.calendar.CalendarUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.Map;

/**
 * 消息模板标签替换工具
 * Created by yangwei
 * Created at 2019/4/29 16:27
 */
public class MsgTemplateUtils {
    public static final String label_pattern = "\\{\\{(.*?)\\}\\}";
    public static final String label_prefix = "{{";
    public static final String label_suffix = "}}";
    public enum TemplateDefaultKey{
        nowDate,      // 当前日期
        nowTime,      // 当前时间
        nowDateTime   // 当前日期时间
    }

    public static String replace(String label, Map<String,String> params){
        String r = label;
        if (StringUtils.isEmpty(label) || params == null  || params.isEmpty()) return label;
        for (String key : params.keySet()) {
            r = r.replace(label_prefix + key + label_suffix,params.get(key));
        }
        // 默认key添加
        String nowDateKey = label_prefix + TemplateDefaultKey.nowDate.name() + label_suffix;
        String nowTimeKey = label_prefix + TemplateDefaultKey.nowTime.name() + label_suffix;
        String nowDateTimeKey = label_prefix + TemplateDefaultKey.nowDateTime.name() + label_suffix;
        Date now = new Date();
        if(r.contains(nowDateKey)){
            r = r.replace(nowDateKey, CalendarUtils.dateToString(now, CalendarUtils.DateStyle.YYYY));
        }
        if(r.contains(nowTimeKey)){
            r = r.replace(nowTimeKey, CalendarUtils.dateToString(now, CalendarUtils.DateStyle.HH_MM_SS));
        }
        if(r.contains(nowDateTimeKey)){
            r = r.replace(nowDateTimeKey, CalendarUtils.dateToString(now, CalendarUtils.DateStyle.YYYY_MM_DD_HH_MM_SS));
        }
        return r;
    }
}
