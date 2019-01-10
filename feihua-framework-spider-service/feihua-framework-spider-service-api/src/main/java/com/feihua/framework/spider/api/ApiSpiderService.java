package com.feihua.framework.spider.api;

import com.feihua.framework.spider.po.SpiderResultUrlsPo;

import java.util.List;
import java.util.Set;

/**
 * Created by yangwei
 * Created at 2018/12/17 16:00
 */
public interface ApiSpiderService {
    /**
     * 获取一个网页，并返回网页内容
     * @param url
     * @return
     */
    String fetchStatic(String url);

    /**
     * 动态网页获取
     * @param url
     * @param wait 秒
     * @return
     */
    String fetchDynamic(String url,int wait);

    void spide(Set<String> urls);
    void spide(List<SpiderResultUrlsPo> spiderResultUrlsPos);
}
