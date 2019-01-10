package com.feihua.framework.spider.test;

import com.feihua.framework.spider.JsoupUtils;
import com.feihua.framework.spider.api.ApiSpiderService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by yangwei
 * Created at 2018/12/15 17:24
 */
public class TestSpideSpring {
    public static void main(String[] args) {

        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext-spider-service-test.xml");
        ApiSpiderService apiSpiderService = applicationContext.getBean(ApiSpiderService.class);

        String url = "https://www.toutiao.com/";
        Set<String> urls = new HashSet<>();
        urls.add(url);
        apiSpiderService.spide(urls);

    }
}
