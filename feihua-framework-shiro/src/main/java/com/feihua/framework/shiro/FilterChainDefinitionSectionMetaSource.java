package com.feihua.framework.shiro;

import org.apache.shiro.config.Ini;
import org.apache.shiro.util.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.FactoryBean;

/**
 * 权限配置资源
 *
 * Created by yw on 2016/1/20.
 */
public class FilterChainDefinitionSectionMetaSource implements FactoryBean<Ini.Section> {

    private Logger logger = LoggerFactory.getLogger(FilterChainDefinitionSectionMetaSource.class);


    private String filterChainDefinitions;

    @Override
    public Ini.Section getObject() throws Exception {
        Ini ini = new Ini();
        //加载默认的url
        ini.load(filterChainDefinitions);
        Ini.Section section = ini.getSection("urls");
        if(CollectionUtils.isEmpty(section)) {
            section = ini.getSection("");
        }
        return section;
    }

    /**
     * 资源配置
     * @return
     * @throws Exception
     */





    @Override
    public Class<?> getObjectType() {
        return this.getClass();
    }

    @Override
    public boolean isSingleton() {
        return false;
    }

    public String getFilterChainDefinitions() {
        return filterChainDefinitions;
    }

    public void setFilterChainDefinitions(String filterChainDefinitions) {
        this.filterChainDefinitions = filterChainDefinitions;
    }
}
