package com.feihua.framework.mybatis.orm;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by yangwei
 * Created at 2018/11/30 17:45
 *
 * 请使用以下方式配置
 <bean class="com.feihua.framework.mybatis.orm.DefaultMybatisMapperConfig">
 <property name="basePackage" value="com.feihua.framework.mybatis.orm.mapper"/>
 </bean>
 *
 */
public final class DefaultMybatisMapperConfig  implements MybatisMapperConfig {

    private String basePackage;
    private Map<String, Set<String>> cacheConfig;

    @Override
    public String getBasePackage() {
        return this.basePackage;
    }

    @Override
    public Map<String, Set<String>> getCacheConfig() {
        return this.cacheConfig;
    }

    public void setBasePackage(String basePackage) {
        this.basePackage = basePackage;
    }

    public void setCacheConfig(Map<String, Set<String>> cacheConfig) {
        this.cacheConfig = cacheConfig;
    }
}
