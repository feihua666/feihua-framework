package com.feihua.framework.mybatis.orm.cache;

import com.feihua.framework.jedis.utils.JedisUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * mybatis整合redis，缓存
 * Created by lyy on 2015/9/6.
 */
public class RedisCache implements MybatisOrmCache {

    private static Logger logger = LoggerFactory.getLogger(RedisCache.class);


    /** The ReadWriteLock */
    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    private String id;

    public RedisCache(String id){
        if(StringUtils.isBlank(id)){
            throw new IllegalArgumentException("Cache instances require an ID");
        }
        logger.debug("------------>RedisCache : id={}",id);
        this.id=id;
    }

    public String getId() {
        return id;
    }

    public void putObject(Object key, Object value) {

        Jedis jedis = null;
        try {
            jedis = JedisUtils.getResource();
            jedis.hset(KEY_PREFIX_SINGLE_CACHE + id, key.toString(), "");
            jedis.set(getBytesKey(key),JedisUtils.toBytes(value));
            logger.debug("putObject {} {}", KEY_PREFIX_SINGLE_CACHE + id, key.toString());
        } catch (Exception e) {
            logger.error("putObject {} {}", KEY_PREFIX_SINGLE_CACHE + id, key.toString(), e);
        } finally {
            JedisUtils.returnResource(jedis);
        }
    }

    public Object getObject(Object key) {
        Jedis jedis = null;
        try {
            jedis = JedisUtils.getResource();
            byte[] result = jedis.get(getBytesKey(key));
            int length = 0;
            if(result != null){
                length = result.length;
            }
            logger.debug("getObject {} {} {}", KEY_PREFIX_SINGLE_CACHE + id, key.toString(), length);
            return JedisUtils.toObject(result);
        } catch (Exception e) {
            logger.error("getObject {} {}", KEY_PREFIX_SINGLE_CACHE + id, key.toString(), e);
        } finally {
            JedisUtils.returnResource(jedis);
        }
        return null;
    }

    public Object removeObject(Object key) {
        Long result = new Long(0);
        Jedis jedis = null;
        try {
            jedis = JedisUtils.getResource();
            result = jedis.hdel(KEY_PREFIX_SINGLE_CACHE + id, key.toString());
            jedis.del(getBytesKey(key));
            logger.debug("removeObject {} {}", KEY_PREFIX_SINGLE_CACHE + id, key.toString());
        } catch (Exception e) {
            logger.error("removeObject {} {}", KEY_PREFIX_SINGLE_CACHE + id, key.toString(), e);
        } finally {
            JedisUtils.returnResource(jedis);
        }
        return result;
    }

    public void clear() {
        Jedis jedis = null;
        try {
            int count = 0;
            jedis = JedisUtils.getResource();
            Map<String,String> map =  jedis.hgetAll(KEY_PREFIX_SINGLE_CACHE + id);
            if(map != null) {
                for (Map.Entry<String,String> entry : map.entrySet()) {
                    Long result = jedis.hdel(KEY_PREFIX_SINGLE_CACHE + id, entry.getKey());
                    jedis.del(getBytesKey(entry.getKey()));
                    count += result;
                }
            }
            logger.debug("clear {} count:{}", KEY_PREFIX_SINGLE_CACHE + id, count);
        } catch (Exception e) {
            logger.error("clear {} {}", KEY_PREFIX_SINGLE_CACHE + id, e);
        } finally {
            JedisUtils.returnResource(jedis);
        }
    }

    private byte[] getBytesKey(Object key){
        return JedisUtils.getBytesKey(KEY_PREFIX_SINGLE_CACHE + id + key.toString());
    }
    public int getSize() {
        Jedis jedis = null;
        int count = 0;
        try {
            jedis = JedisUtils.getResource();
            Map<String,String> map =  jedis.hgetAll(KEY_PREFIX_SINGLE_CACHE + id);
            if(map != null) {
                count = map.size();
            }
            logger.debug("getSize {} count:{}", KEY_PREFIX_SINGLE_CACHE + id, count);
        } catch (Exception e) {
            logger.error("getSize {} {}", KEY_PREFIX_SINGLE_CACHE + id, e);
        } finally {
            JedisUtils.returnResource(jedis);
        }
        return count;
    }

    @Override
    public ReadWriteLock getReadWriteLock() {
        return readWriteLock;
    }
}
