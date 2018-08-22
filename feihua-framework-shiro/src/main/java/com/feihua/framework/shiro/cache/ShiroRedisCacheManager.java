package com.feihua.framework.shiro.cache;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.util.StringUtils;

/**
 * shiro 缓存管理
 * Created by yangwei
 * Created at 2017/7/25 21:19
 */
public class ShiroRedisCacheManager implements CacheManager {


    @Override
    public <K, V> Cache<K, V> getCache(String name) throws CacheException {
        if(!StringUtils.hasText(name)) {
            throw new IllegalArgumentException("Cache name cannot be null or empty.");
        }
        return new ShiroRedisCache(name);
    }
}
