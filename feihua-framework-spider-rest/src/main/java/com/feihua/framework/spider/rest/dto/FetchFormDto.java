package com.feihua.framework.spider.rest.dto;

import java.util.Map;

/**
 * Created by yangwei
 * Created at 2018/12/17 20:05
 */
public class FetchFormDto {
    /**
     * key 键值
     * value css选择器
     */
    private Map<String,String> selector;
    private boolean dynamic;
    private String url;
    private int dynamicWaitSecond = 3;


    public Map<String, String> getSelector() {
        return selector;
    }

    public void setSelector(Map<String, String> selector) {
        this.selector = selector;
    }

    public boolean isDynamic() {
        return dynamic;
    }

    public void setDynamic(boolean dynamic) {
        this.dynamic = dynamic;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getDynamicWaitSecond() {
        return dynamicWaitSecond;
    }

    public void setDynamicWaitSecond(int dynamicWaitSecond) {
        this.dynamicWaitSecond = dynamicWaitSecond;
    }
}
