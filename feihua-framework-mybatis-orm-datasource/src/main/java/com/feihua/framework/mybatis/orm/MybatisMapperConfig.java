package com.feihua.framework.mybatis.orm;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * mybatis各模块配置接口
 * Created by yangwei
 * Created at 2018/11/30 17:38
 */
public interface MybatisMapperConfig {

    public String getBasePackage();

    /**
     * 多表联查缓存配置，value为查询，key为增、删、改
     * 查询value配置到mapper的方法全限定名，value 增、删、改可配置到mapper的类全限定名也可配置到方法的全限定名
     * @return
     */
    public Map<String, Set<String>> getCacheConfig();
}
