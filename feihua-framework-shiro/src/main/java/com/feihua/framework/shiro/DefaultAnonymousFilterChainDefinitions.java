package com.feihua.framework.shiro;

import org.apache.shiro.config.Ini;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 匿名拦截定义
 * Created by yangwei
 * Created at 2019/4/1 15:58
 */
public class DefaultAnonymousFilterChainDefinitions implements AnonymousFilterChainDefinitionsInterface {


    private Logger logger = LoggerFactory.getLogger(DefaultAnonymousFilterChainDefinitions.class);


    private String filterChainDefinitions;

    public Ini.Section getSection() throws Exception {
        Ini ini = new Ini();
        //加载默认的url
        ini.load(filterChainDefinitions);
        Ini.Section section = ini.getSection("urls");
        if(section == null || section.size() == 0) {
            section = ini.getSection("");
        }
        return section;
    }

    public String getFilterChainDefinitions() {
        return filterChainDefinitions;
    }

    public void setFilterChainDefinitions(String filterChainDefinitions) {
        this.filterChainDefinitions = filterChainDefinitions;
    }
}