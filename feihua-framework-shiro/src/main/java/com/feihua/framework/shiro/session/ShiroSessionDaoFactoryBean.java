package com.feihua.framework.shiro.session;

import org.apache.shiro.session.mgt.eis.MemorySessionDAO;
import org.apache.shiro.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.FactoryBean;

/**
 * shiro session dao
 * Created by yangwei
 * Created at 2017/7/26 14:08
 */
public class ShiroSessionDaoFactoryBean implements FactoryBean {

    private Logger logger = LoggerFactory.getLogger(ShiroSessionDaoFactoryBean.class);

    private static String SESSION_DAO_TYPE_REDIS = "redis";
    private static String SESSION_DAO_TYPE_MEMORY = "memory_shiro";

    private String sessionDaoType = "redis";

    private String sessionRedisKeyPrefix;

    @Override
    public Object getObject() throws Exception {

        if(isRedis()){
            ShiroJedisSessionDAO jedisSessionDAO = new ShiroJedisSessionDAO();
            if(sessionRedisKeyPrefix != null && StringUtils.hasText(sessionRedisKeyPrefix)){
                jedisSessionDAO.setSessionKeyPrefix(sessionRedisKeyPrefix);
            }
            return jedisSessionDAO;
        }if(isMemoty()){
            return new MemorySessionDAO();
        }else {
            logger.error("sessionDaoType={} is not supported. use [{} {}] instead",sessionDaoType,SESSION_DAO_TYPE_REDIS,SESSION_DAO_TYPE_MEMORY);
        }

        return null;
    }

    @Override
    public Class<?> getObjectType() {
        if(isRedis()){
            return ShiroJedisSessionDAO.class;
        }if(isMemoty()){
            return MemorySessionDAO.class;
        }else {
            logger.error("sessionDaoType={} is not supported. use [{} {}] instead",sessionDaoType,SESSION_DAO_TYPE_REDIS,SESSION_DAO_TYPE_MEMORY);
        }
        return null;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    private boolean isRedis(){
        return SESSION_DAO_TYPE_REDIS.equals(sessionDaoType);
    }
    private boolean isMemoty(){
        return SESSION_DAO_TYPE_MEMORY.equals(sessionDaoType);
    }
    public String getSessionDaoType() {
        return sessionDaoType;
    }

    public void setSessionDaoType(String sessionDaoType) {
        this.sessionDaoType = sessionDaoType;
    }

    public String getSessionRedisKeyPrefix() {
        return sessionRedisKeyPrefix;
    }

    public void setSessionRedisKeyPrefix(String sessionRedisKeyPrefix) {
        this.sessionRedisKeyPrefix = sessionRedisKeyPrefix;
    }
}
