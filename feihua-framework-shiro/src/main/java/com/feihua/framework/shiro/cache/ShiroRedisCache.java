package com.feihua.framework.shiro.cache;

import com.feihua.framework.jedis.utils.JedisUtils;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * shiro jedis cache
 * Created by yangwei
 * Created at 2017/7/25 21:40
 */
public class ShiroRedisCache<K, V> implements Cache<K, V> {

    private Logger logger = LoggerFactory.getLogger(ShiroRedisCache.class);

    private String cacheKeyName = null;

    public ShiroRedisCache(String cacheKeyName) {
        this.cacheKeyName = JedisUtils.wrapKeyPrefix(cacheKeyName);
    }


    @Override
    public V get(K key) throws CacheException {
        if (key == null){
            return null;
        }

        V value = null;
        Jedis jedis = null;
        try {
            jedis = JedisUtils.getResource();
            value = (V)JedisUtils.toObject(jedis.hget(JedisUtils.getBytesKey(cacheKeyName), JedisUtils.getBytesKey(key)));
            logger.debug("get cacheKeyName={} key={} value={}", cacheKeyName, key, value);
        } catch (Exception e) {
            logger.error("get cacheKeyName={} key={} {}", cacheKeyName, key, e);
        } finally {
            JedisUtils.returnResource(jedis);
        }
        return value;
    }

    @Override
    public V put(K key, V value) throws CacheException {
        if (key == null){
            return null;
        }

        Jedis jedis = null;
        try {
            jedis = JedisUtils.getResource();
            jedis.hset(JedisUtils.getBytesKey(cacheKeyName), JedisUtils.getBytesKey(key), JedisUtils.toBytes(value));
            logger.debug("put {} {} = {}", cacheKeyName, key, value);
        } catch (Exception e) {
            logger.error("put {} {}", cacheKeyName, key, e);
        } finally {
            JedisUtils.returnResource(jedis);
        }
        return value;
    }

    @SuppressWarnings("unchecked")
    @Override
    public V remove(K key) throws CacheException {
        V value = null;
        Jedis jedis = null;
        try {
            jedis = JedisUtils.getResource();
            value = (V)JedisUtils.toObject(jedis.hget(JedisUtils.getBytesKey(cacheKeyName), JedisUtils.getBytesKey(key)));
            Long delNum = jedis.hdel(JedisUtils.getBytesKey(cacheKeyName), JedisUtils.getBytesKey(key));
            logger.debug("remove {} key={} value={} delNum={}", cacheKeyName, key, value, delNum);
        } catch (Exception e) {
            logger.warn("remove {} {}", cacheKeyName, key, e);
        } finally {
            JedisUtils.returnResource(jedis);
        }
        return value;
    }

    @Override
    public void clear() throws CacheException {
        Jedis jedis = null;
        try {
            jedis = JedisUtils.getResource();
            jedis.hdel(JedisUtils.getBytesKey(cacheKeyName));
            logger.debug("clear {}", cacheKeyName);
        } catch (Exception e) {
            logger.error("clear {}", cacheKeyName, e);
        } finally {
            JedisUtils.returnResource(jedis);
        }
    }

    @Override
    public int size() {
        int size = 0;
        Jedis jedis = null;
        try {
            jedis = JedisUtils.getResource();
            size = jedis.hlen(JedisUtils.getBytesKey(cacheKeyName)).intValue();
            logger.debug("size {} {} ", cacheKeyName, size);
            return size;
        } catch (Exception e) {
            logger.error("clear {}",  cacheKeyName, e);
        } finally {
            JedisUtils.returnResource(jedis);
        }
        return size;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Set<K> keys() {
        Set<K> keys = new HashSet<>();
        Jedis jedis = null;
        try {
            jedis = JedisUtils.getResource();
            Set<byte[]> set = jedis.hkeys(JedisUtils.getBytesKey(cacheKeyName));
            for(byte[] key : set){
                keys.add((K)key);
            }
            logger.debug("keys {} {} ", cacheKeyName, keys);
            return keys;
        } catch (Exception e) {
            logger.error("keys {}", cacheKeyName, e);
        } finally {
            JedisUtils.returnResource(jedis);
        }
        return keys;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Collection<V> values() {
        Collection<V> vals = Collections.emptyList();;
        Jedis jedis = null;
        try {
            jedis = JedisUtils.getResource();
            Collection<byte[]> col = jedis.hvals(JedisUtils.getBytesKey(cacheKeyName));
            for(byte[] val : col){
                vals.add((V)val);
            }
            logger.debug("values {} {} ", cacheKeyName, vals);
            return vals;
        } catch (Exception e) {
            logger.error("values {}",  cacheKeyName, e);
        } finally {
            JedisUtils.returnResource(jedis);
        }
        return vals;
    }
}