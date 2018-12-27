package com.feihua.framework.spider.test;

import com.feihua.framework.spider.JsoupUtils;
import com.feihua.framework.spider.api.ApiSpiderService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by yangwei
 * Created at 2018/12/15 17:24
 */
public class TestSeleniumSpiderSpring {
    public static void main(String[] args) {

        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext-spider-service.xml");
        ApiSpiderService apiSpiderService = applicationContext.getBean(ApiSpiderService.class);

        String url = "https://www.toutiao.com/a6626127654925894158/";
        String r = apiSpiderService.fetchDynamic(url,5);

        Document doc = Jsoup.parse(r);
        r = JsoupUtils.selectOuterHtml(".article-content",doc);
        System.out.println(r);
    }
}
