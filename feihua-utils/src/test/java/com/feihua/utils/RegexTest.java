package com.feihua.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by yangwei
 * Created at 2019/4/30 16:32
 */
public class RegexTest {
    public static void main(String[] args) {
        String reg = "\\{\\{(.*?)\\}\\}";
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher("ssdsf{{111}}sssss{{333}}");
        while (matcher.find())
        {
            String src = matcher.group(1);
            System.out.println(src);
        }
    }
}
