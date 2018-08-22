package com.feihua.utils.http.httpclient;

import com.feihua.utils.string.StringUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by yangwei
 * Created at 2018/4/28 10:19
 */
public class HttpClientUtils {
    // 建立链接超时时间,毫秒
    public static final int connTimeout = 10000;
    // 响应超时时间,毫秒
    public static final int readTimeout = 10000;
    public static final String charset = "UTF-8";
    private static HttpClient client = null;
    static {
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        cm.setMaxTotal(128);
        cm.setDefaultMaxPerRoute(128);
        client = HttpClients.custom().setConnectionManager(cm).build();
    }

    public static String httpGet(String url) throws IOException {
        HttpGet get = new HttpGet(url);
        HttpResponse res = null;
        String r = null;
        try{
            res = client.execute(get);
            r = HttpResponseContentToString(res);
        }finally {
            get.releaseConnection();
        }
        return r;
    }

    public static HttpClient getClient() {
        return client;
    }

    /**
     *
     * @param url
     * @param params
     * @return
     * @throws IOException
     */
    public static String httpPost(String url,Map<String,String> params) throws IOException {
        HttpPost post = new HttpPost(url);
        // 创建参数列表
        if (params != null) {
            List<NameValuePair> paramList = new ArrayList<>();
            for (String key : params.keySet()) {
                paramList.add(new BasicNameValuePair(key, params.get(key)));
            }
            // 模拟表单
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramList,charset);
            post.setEntity(entity);
        }
        HttpResponse res = null;
        String r = null;
        try{
            res = client.execute(post);
            r = HttpResponseContentToString(res);
        }finally {
            post.releaseConnection();
        }
        return r;
    }

    /**
     *
     * @param url
     * @param body
     * @return
     * @throws IOException
     */
    public static String httpPostJson(String url,String body) throws IOException {
        HttpPost post = new HttpPost(url);
        post.addHeader("Content-Type", "application/json");
        // 创建参数列表
        if(org.apache.commons.lang3.StringUtils.isNotEmpty(body)){
            post.setEntity(new StringEntity(body,"utf-8"));
        }
        HttpResponse res = null;
        String r = null;
        try{
            res = client.execute(post);
            r = HttpResponseContentToString(res);
        }finally {
            post.releaseConnection();
        }
        return r;
    }

    /**
     * 获取状态码
     * @param res
     * @return
     */
    public static int HttpResponseStatus(HttpResponse res){
        return res.getStatusLine().getStatusCode();
    }

    /**
     * 响应内容转为字符串
     * @param res
     * @return
     * @throws IOException
     */
    public static String HttpResponseContentToString(HttpResponse res) throws IOException {
        return IOUtils.toString(res.getEntity().getContent(),charset);
    }
}
