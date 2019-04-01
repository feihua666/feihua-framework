package com.feihua.framework.shiro;

import org.apache.shiro.config.Ini;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * 权限配置资源
 *
 * Created by yw on 2016/1/20.
 */
public class FilterChainDefinitionSectionMetaSource implements FactoryBean<Ini.Section> {

    private Logger logger = LoggerFactory.getLogger(FilterChainDefinitionSectionMetaSource.class);


    private String filterChainDefinitions;
    @Autowired(required = false)
    private Map<String, AnonymousFilterChainDefinitionsInterface> anonymousFilterChainDefinitionsMap;

    @Override
    public Ini.Section getObject() throws Exception {
        Ini ini = new Ini();
        //加载默认的url
        ini.load(filterChainDefinitions);
        Ini.Section section = ini.getSection("urls");
        if(section == null || section.size() == 0) {
            section = ini.getSection("");
        }
        Ini.Section temp = null;
        if(section != null && anonymousFilterChainDefinitionsMap != null){
            for (String key : anonymousFilterChainDefinitionsMap.keySet()) {
                if(temp == null){
                    temp = anonymousFilterChainDefinitionsMap.get(key).getSection();
                }else {
                    if(anonymousFilterChainDefinitionsMap.get(key).getSection() != null){
                        temp.putAll(anonymousFilterChainDefinitionsMap.get(key).getSection());
                    }
                }
            }
        }
        if(temp != null){
            if(section != null){
                temp.putAll(section);

            }
            return temp;
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
