package com.feihua.framework.shiro.cache;

import org.apache.shiro.cache.MemoryConstrainedCacheManager;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.FactoryBean;

/**
 * shiro 缓存管理 工厂
 * Created by yangwei
 * Created at 2017/7/26 9:03
 */
public class ShiroCacheManagerFactoryBean implements FactoryBean {

    private static Logger logger = LoggerFactory.getLogger(ShiroCacheManagerFactoryBean.class);

    private static String CACHE_MANAGER_TYPE_REDIS = "redis";
    private static String CACHE_MANAGER_TYPE_eh = "eh_shiro";
    private static String CACHE_MANAGER_TYPE_memory = "memory_shiro";

    private String cacheManagerType = CACHE_MANAGER_TYPE_REDIS;
    @Override
    public Object getObject() throws Exception {
        if(isRedis()){
            return new ShiroRedisCacheManager();
        }else if(isEh()){
            return new EhCacheManager();
        }if(isMemory()){
            return new MemoryConstrainedCacheManager();
        }else {
            logger.error("cacheManagerType {} is not supported. use [{},{},{}] instead",cacheManagerType,CACHE_MANAGER_TYPE_REDIS,CACHE_MANAGER_TYPE_eh,CACHE_MANAGER_TYPE_memory);
        }


        return null;
    }

    @Override
    public Class<?> getObjectType() {
        if(isRedis()){
            return ShiroRedisCacheManager.class;
        }else if(isEh()){
            return EhCacheManager.class;
        }if(isMemory()){
            return MemoryConstrainedCacheManager.class;
        }else {
            logger.error("cacheManagerType {} is not supported. use [{},{},{}] instead",cacheManagerType,CACHE_MANAGER_TYPE_REDIS,CACHE_MANAGER_TYPE_eh,CACHE_MANAGER_TYPE_memory);
        }
        return null;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    private boolean isRedis(){
        return CACHE_MANAGER_TYPE_REDIS.equals(cacheManagerType);
    }
    private boolean isEh(){
        return CACHE_MANAGER_TYPE_eh.equals(cacheManagerType);
    }
    private boolean isMemory(){
        return CACHE_MANAGER_TYPE_memory.equals(cacheManagerType);
    }


    public String getCacheManagerType() {
        return cacheManagerType;
    }

    public void setCacheManagerType(String cacheManagerType) {
        this.cacheManagerType = cacheManagerType;
    }
}
