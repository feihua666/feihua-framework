package com.feihua.framework.shiro;

import org.apache.shiro.config.Ini;

/**
 * 匿名拦截定义
 * Created by yangwei
 * Created at 2019/4/1 15:58
 */
public interface AnonymousFilterChainDefinitionsInterface {

    public Ini.Section getSection() throws Exception;
}