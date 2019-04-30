package com.feihua.utils.string;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by yangwei
 * Created at 2019/4/30 16:35
 */
public class RegexUtils {
    /**
     * 获取正则匹配的字符串
     * @param regularPattern
     * @param content
     * @return
     */
    public static List<String> getMatchedString(String regularPattern,String content){
        Pattern pattern = Pattern.compile(regularPattern);
        Matcher matcher = pattern.matcher(content);
        List<String> r = new ArrayList<>();
        while (matcher.find())
        {
            String src = matcher.group(1);
            r.add(src);
        }
        return r;
    }
}
