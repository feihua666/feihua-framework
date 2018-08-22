package com.feihua.utils;

import com.feihua.utils.http.httpclient.HttpClientUtils;
import com.feihua.utils.json.JSONUtils;
import com.feihua.utils.string.StringUtils;
import org.apache.http.HttpResponse;

import java.io.IOException;
import java.util.Map;

import static com.feihua.utils.string.StringUtils.propertyToSetMethodName;

/**
 * Created by yangwei
 * Created at 2018/3/21 10:59
 */
public class Test {
    public static void main(String[] args) throws Exception {


        String url = "http://localhost:8080/wwd/users";
        System.out.println( url.substring(0,url.indexOf("/wwd")));

    }
}
