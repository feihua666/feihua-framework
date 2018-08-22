package com.feihua.framework.mybatis.orm.cache;

import org.apache.ibatis.cache.Cache;

/**
 * Created by yangwei
 * Created at 2018/6/29 10:55
 */
public interface MybatisOrmCache extends Cache {

    public static int cacheSeconds =  0;
    public static final String KEY_PREFIX = "_mybatisCache";
    public static final String KEY_PREFIX_SINGLE_CACHE = "_mybatisCache_namespace";
}
