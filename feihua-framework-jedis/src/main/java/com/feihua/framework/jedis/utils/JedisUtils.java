package com.feihua.framework.jedis.utils;

import com.feihua.utils.serialize.SerializeUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisException;

import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * Jedis Cache 工具类
 */
public class JedisUtils {

	private static Logger logger = LoggerFactory.getLogger(JedisUtils.class);
	
	private static JedisPool jedisPool = null;

	private static String keyPrefix = "";

	public static String wrapKeyPrefix(String originalKey){

		return keyPrefix + originalKey;
	}

	/**
	 * 获取缓存
	 * @param key 键
	 * @return 值
	 */
	public static String get(String key) {
		String value = null;
		Jedis jedis = null;
		String prefixKey = wrapKeyPrefix(key);
		try {
			jedis = getResource();
			if (jedis.exists(prefixKey)) {
				value = jedis.get(prefixKey);
				value = StringUtils.isNotBlank(value) && !"nil".equalsIgnoreCase(value) ? value : null;
				if(logger.isDebugEnabled()){
					logger.debug("get {} = {}", prefixKey, value);
				}
			}
		} catch (Exception e) {
			logger.error("get {} = {}", prefixKey, value, e);
		} finally {
			returnResource(jedis);
		}
		return value;
	}
	
	/**
	 * 获取缓存
	 * @param key 键
	 * @return 值
	 */
	public static Object getObject(String key) {
		Object value = null;
		Jedis jedis = null;
		String prefixKey = wrapKeyPrefix(key);
		try {
			jedis = getResource();
			if (jedis.exists(getBytesKey(prefixKey))) {
				value = toObject(jedis.get(getBytesKey(prefixKey)));
				if(logger.isDebugEnabled()){
					logger.debug("getObject {} = {}", prefixKey, value);
				}

			}
		} catch (Exception e) {
			logger.error("getObject {} = {}", prefixKey, value, e);
		} finally {
			returnResource(jedis);
		}
		return value;
	}
	
	/**
	 * 设置缓存
	 * @param key 键
	 * @param value 值
	 * @param cacheSeconds 超时时间，0为不超时
	 * @return
	 */
	public static String set(String key, String value, int cacheSeconds) {
		String result = null;
		Jedis jedis = null;
		String prefixKey = wrapKeyPrefix(key);
		try {
			jedis = getResource();
			result = jedis.set(prefixKey, value);
			if (cacheSeconds != 0) {
				jedis.expire(prefixKey, cacheSeconds);
			}
			if(logger.isDebugEnabled()){
				logger.debug("set {} = {}", prefixKey, value);
			}

		} catch (Exception e) {
			logger.error("set {} = {}", prefixKey, value, e);
		} finally {
			returnResource(jedis);
		}
		return result;
	}
	
	/**
	 * 设置缓存
	 * @param key 键
	 * @param value 值
	 * @param cacheSeconds 超时时间，0为不超时
	 * @return
	 */
	public static String setObject(String key, Object value, int cacheSeconds) {
		String result = null;
		Jedis jedis = null;
		String prefixKey = wrapKeyPrefix(key);
		try {
			jedis = getResource();
			result = jedis.set(getBytesKey(prefixKey), toBytes(value));
			if (cacheSeconds != 0) {
				jedis.expire(prefixKey, cacheSeconds);
			}
			if(logger.isDebugEnabled()){
				logger.debug("setObject {} = {}", prefixKey, value);
			}

		} catch (Exception e) {
			logger.error("setObject {} = {}", prefixKey, value, e);
		} finally {
			returnResource(jedis);
		}
		return result;
	}
	
	/**
	 * 获取List缓存
	 * @param key 键
	 * @return 值
	 */
	public static List<String> getList(String key) {
		List<String> value = null;
		Jedis jedis = null;
		String prefixKey = wrapKeyPrefix(key);
		try {
			jedis = getResource();
			if (jedis.exists(prefixKey)) {
				value = jedis.lrange(prefixKey, 0, -1);
				if(logger.isDebugEnabled()){
					logger.debug("getList {} = {}", prefixKey, value);
				}
			}
		} catch (Exception e) {
			logger.error("getList {} = {}", prefixKey, value, e);
		} finally {
			returnResource(jedis);
		}
		return value;
	}
	
	/**
	 * 获取List缓存
	 * @param key 键
	 * @return 值
	 */
	public static List<Object> getObjectList(String key) {
		List<Object> value = null;
		Jedis jedis = null;
		String prefixKey = wrapKeyPrefix(key);
		try {
			jedis = getResource();
			if (jedis.exists(getBytesKey(prefixKey))) {
				List<byte[]> list = jedis.lrange(getBytesKey(prefixKey), 0, -1);
				value = new ArrayList<>();
				for (byte[] bs : list){
					value.add(toObject(bs));
				}
				if(logger.isDebugEnabled()){
					logger.debug("getObjectList {} = {}", prefixKey, value);
				}

			}
		} catch (Exception e) {
			logger.error("getObjectList {} = {}", prefixKey, value, e);
		} finally {
			returnResource(jedis);
		}
		return value;
	}
	
	/**
	 * 设置List缓存
	 * @param key 键
	 * @param value 值
	 * @param cacheSeconds 超时时间，0为不超时
	 * @return
	 */
	public static long setList(String key, List<String> value, int cacheSeconds) {
		long result = 0;
		Jedis jedis = null;
		String prefixKey = wrapKeyPrefix(key);
		try {
			jedis = getResource();
			if (jedis.exists(prefixKey)) {
				jedis.del(prefixKey);
			}
			result = jedis.rpush(prefixKey, (String[]) value.toArray());
			if (cacheSeconds != 0) {
				jedis.expire(prefixKey, cacheSeconds);
			}
			if(logger.isDebugEnabled()){
				logger.debug("setList {} = {}", prefixKey, value);
			}
		} catch (Exception e) {
			logger.error("setList {} = {}", prefixKey, value, e);
		} finally {
			returnResource(jedis);
		}
		return result;
	}
	
	/**
	 * 设置List缓存
	 * @param key 键
	 * @param value 值
	 * @param cacheSeconds 超时时间，0为不超时
	 * @return
	 */
	public static long setObjectList(String key, List<Object> value, int cacheSeconds) {
		long result = 0;
		Jedis jedis = null;
		String prefixKey = wrapKeyPrefix(key);
		try {
			jedis = getResource();
			if (jedis.exists(getBytesKey(prefixKey))) {
				jedis.del(prefixKey);
			}
			List<byte[]> list = new ArrayList<>();
			for (Object o : value){
				list.add(toBytes(o));
			}
			result = jedis.rpush(getBytesKey(prefixKey), (byte[][]) list.toArray());
			if (cacheSeconds != 0) {
				jedis.expire(prefixKey, cacheSeconds);
			}
			if(logger.isDebugEnabled()){
				logger.debug("setObjectList {} = {}", prefixKey, value);
			}

		} catch (Exception e) {
			logger.error("setObjectList {} = {}", prefixKey, value, e);
		} finally {
			returnResource(jedis);
		}
		return result;
	}
	
	/**
	 * 向List缓存中添加值
	 * @param key 键
	 * @param value 值
	 * @return
	 */
	public static long listAdd(String key, String... value) {
		long result = 0;
		Jedis jedis = null;
		String prefixKey = wrapKeyPrefix(key);
		try {
			jedis = getResource();
			result = jedis.rpush(prefixKey, value);
			if(logger.isDebugEnabled()){
				logger.debug("listAdd {} = {}", prefixKey, value);
			}

		} catch (Exception e) {
			logger.error("listAdd {} = {}", prefixKey, value, e);
		} finally {
			returnResource(jedis);
		}
		return result;
	}
	
	/**
	 * 向List缓存中添加值
	 * @param key 键
	 * @param value 值
	 * @return
	 */
	public static long listObjectAdd(String key, Object... value) {
		long result = 0;
		Jedis jedis = null;
		String prefixKey = wrapKeyPrefix(key);
		try {
			jedis = getResource();
			List<byte[]> list = new ArrayList<>();
			for (Object o : value){
				list.add(toBytes(o));
			}
			result = jedis.rpush(getBytesKey(prefixKey), (byte[][]) list.toArray());
			if(logger.isDebugEnabled()){
				logger.debug("listObjectAdd {} = {}", prefixKey, value);
			}

		} catch (Exception e) {
			logger.error("listObjectAdd {} = {}", prefixKey, value, e);
		} finally {
			returnResource(jedis);
		}
		return result;
	}

	/**
	 * 获取缓存
	 * @param key 键
	 * @return 值
	 */
	public static Set<String> getSet(String key) {
		Set<String> value = null;
		Jedis jedis = null;
		String prefixKey = wrapKeyPrefix(key);
		try {
			jedis = getResource();
			if (jedis.exists(prefixKey)) {
				value = jedis.smembers(prefixKey);
				if(logger.isDebugEnabled()){
					logger.debug("getSet {} = {}", prefixKey, value);
				}

			}
		} catch (Exception e) {
			logger.error("getSet {} = {}", prefixKey, value, e);
		} finally {
			returnResource(jedis);
		}
		return value;
	}
	
	/**
	 * 获取缓存
	 * @param key 键
	 * @return 值
	 */
	public static Set<Object> getObjectSet(String key) {
		Set<Object> value = null;
		Jedis jedis = null;
		String prefixKey = wrapKeyPrefix(key);
		try {
			jedis = getResource();
			if (jedis.exists(getBytesKey(prefixKey))) {
				value = new HashSet<>();
				Set<byte[]> set = jedis.smembers(getBytesKey(prefixKey));
				for (byte[] bs : set){
					value.add(toObject(bs));
				}
				if(logger.isDebugEnabled()){
					logger.debug("getObjectSet {} = {}", prefixKey, value);
				}

			}
		} catch (Exception e) {
			logger.error("getObjectSet {} = {}", prefixKey, value, e);
		} finally {
			returnResource(jedis);
		}
		return value;
	}
	
	/**
	 * 设置Set缓存
	 * @param key 键
	 * @param value 值
	 * @param cacheSeconds 超时时间，0为不超时
	 * @return
	 */
	public static long setSet(String key, Set<String> value, int cacheSeconds) {
		long result = 0;
		Jedis jedis = null;
		String prefixKey = wrapKeyPrefix(key);
		try {
			jedis = getResource();
			if (jedis.exists(prefixKey)) {
				jedis.del(prefixKey);
			}
			String array[] = new String[value.size()];
			value.toArray(array);
			result = jedis.sadd(prefixKey,  array);
			if (cacheSeconds != 0) {
				jedis.expire(prefixKey, cacheSeconds);
			}
			if(logger.isDebugEnabled()){
				logger.debug("setSet {} = {}", prefixKey, value);
			}

		} catch (Exception e) {
			logger.error("setSet {} = {}", prefixKey, value, e);
		} finally {
			returnResource(jedis);
		}
		return result;
	}
	
	/**
	 * 设置Set缓存
	 * @param key 键
	 * @param value 值
	 * @param cacheSeconds 超时时间，0为不超时
	 * @return
	 */
	public static long setObjectSet(String key, Set<Object> value, int cacheSeconds) {
		long result = 0;
		Jedis jedis = null;
		String prefixKey = wrapKeyPrefix(key);
		try {
			jedis = getResource();
			if (jedis.exists(getBytesKey(prefixKey))) {
				jedis.del(prefixKey);
			}
			Set<byte[]> set = new HashSet();
			for (Object o : value){
				set.add(toBytes(o));
			}
			result = jedis.sadd(getBytesKey(prefixKey), (byte[][]) set.toArray());
			if (cacheSeconds != 0) {
				jedis.expire(prefixKey, cacheSeconds);
			}
			if(logger.isDebugEnabled()){
				logger.debug("setObjectSet {} = {}", prefixKey, value);
			}
		} catch (Exception e) {
			logger.error("setObjectSet {} = {}", prefixKey, value, e);
		} finally {
			returnResource(jedis);
		}
		return result;
	}
	
	/**
	 * 向Set缓存中添加值
	 * @param key 键
	 * @param value 值
	 * @return
	 */
	public static long setSetAdd(String key, String... value) {
		long result = 0;
		Jedis jedis = null;
		String prefixKey = wrapKeyPrefix(key);
		try {
			jedis = getResource();
			result = jedis.sadd(prefixKey, value);
			if(logger.isDebugEnabled()){
				logger.debug("setSetAdd {} = {}", prefixKey, value);
			}
		} catch (Exception e) {
			logger.error("setSetAdd {} = {}", prefixKey, value, e);
		} finally {
			returnResource(jedis);
		}
		return result;
	}

	/**
	 * 向Set缓存中添加值
	 * @param key 键
	 * @param value 值
	 * @return
	 */
	public static long setSetObjectAdd(String key, Object... value) {
		long result = 0;
		Jedis jedis = null;
		String prefixKey = wrapKeyPrefix(key);
		try {
			jedis = getResource();
			Set<byte[]> set = new HashSet();
			for (Object o : value){
				set.add(toBytes(o));
			}
			result = jedis.rpush(getBytesKey(prefixKey), (byte[][]) set.toArray());
			if(logger.isDebugEnabled()){
				logger.debug("setSetObjectAdd {} = {}", prefixKey, value);
			}
		} catch (Exception e) {
			logger.error("setSetObjectAdd {} = {}", prefixKey, value, e);
		} finally {
			returnResource(jedis);
		}
		return result;
	}
	
	/**
	 * 获取Map缓存
	 * @param key 键
	 * @return 值
	 */
	public static Map<String, String> getMap(String key) {
		Map<String, String> value = null;
		Jedis jedis = null;
		String prefixKey = wrapKeyPrefix(key);
		try {
			jedis = getResource();
			if (jedis.exists(prefixKey)) {
				value = jedis.hgetAll(prefixKey);
				if(logger.isDebugEnabled()){
					logger.debug("getMap {} = {}", prefixKey, value);
				}
			}
		} catch (Exception e) {
			logger.error("getMap {} = {}", prefixKey, value, e);
		} finally {
			returnResource(jedis);
		}
		return value;
	}
	
	/**
	 * 获取Map缓存
	 * @param key 键
	 * @return 值
	 */
	public static Map<String, Object> getObjectMap(String key) {
		Map<String, Object> value = null;
		Jedis jedis = null;
		String prefixKey = wrapKeyPrefix(key);
		try {
			jedis = getResource();
			if (jedis.exists(getBytesKey(prefixKey))) {
				value = new HashMap();
				Map<byte[], byte[]> map = jedis.hgetAll(getBytesKey(prefixKey));
				for (Map.Entry<byte[], byte[]> e : map.entrySet()){
					String s = null;
					try {
						s =  new String(e.getKey(), "utf-8");
					} catch (UnsupportedEncodingException uee) {
						s = "";
					}
					value.put(s, toObject(e.getValue()));
				}
				if(logger.isDebugEnabled()){
					logger.debug("getObjectMap {} = {}", prefixKey, value);
				}
			}
		} catch (Exception e) {
			logger.error("getObjectMap {} = {}", prefixKey, value, e);
		} finally {
			returnResource(jedis);
		}
		return value;
	}
	
	/**
	 * 设置Map缓存
	 * @param key 键
	 * @param value 值
	 * @param cacheSeconds 超时时间，0为不超时
	 * @return
	 */
	public static String setMap(String key, Map<String, String> value, int cacheSeconds) {
		String result = null;
		Jedis jedis = null;
		String prefixKey = wrapKeyPrefix(key);
		try {
			jedis = getResource();
			if (jedis.exists(prefixKey)) {
				jedis.del(prefixKey);
			}
			result = jedis.hmset(prefixKey, value);
			if (cacheSeconds != 0) {
				jedis.expire(prefixKey, cacheSeconds);
			}
			if(logger.isDebugEnabled()){
				logger.debug("setMap {} = {}", prefixKey, value);
			}
		} catch (Exception e) {
			logger.error("setMap {} = {}", prefixKey, value, e);
		} finally {
			returnResource(jedis);
		}
		return result;
	}
	
	/**
	 * 设置Map缓存
	 * @param key 键
	 * @param value 值
	 * @param cacheSeconds 超时时间，0为不超时
	 * @return
	 */
	public static String setObjectMap(String key, Map<String, Object> value, int cacheSeconds) {
		String result = null;
		Jedis jedis = null;
		String prefixKey = wrapKeyPrefix(key);
		try {
			jedis = getResource();
			if (jedis.exists(getBytesKey(prefixKey))) {
				jedis.del(prefixKey);
			}
			Map<byte[], byte[]> map = new HashMap();
			for (Map.Entry<String, Object> e : value.entrySet()){
				map.put(getBytesKey(e.getKey()), toBytes(e.getValue()));
			}
			result = jedis.hmset(getBytesKey(prefixKey), (Map<byte[], byte[]>)map);
			if (cacheSeconds != 0) {
				jedis.expire(prefixKey, cacheSeconds);
			}
			if(logger.isDebugEnabled()){
				logger.debug("setObjectMap {} = {}", prefixKey, value);
			}
		} catch (Exception e) {
			logger.error("setObjectMap {} = {}", prefixKey, value, e);
		} finally {
			returnResource(jedis);
		}
		return result;
	}
	
	/**
	 * 向Map缓存中添加值
	 * @param key 键
	 * @param value 值
	 * @return
	 */
	public static String mapPut(String key, Map<String, String> value) {
		String result = null;
		Jedis jedis = null;
		String prefixKey = wrapKeyPrefix(key);
		try {
			jedis = getResource();
			result = jedis.hmset(prefixKey, value);
			if(logger.isDebugEnabled()){
				logger.debug("mapPut {} = {}", prefixKey, value);
			}
		} catch (Exception e) {
			logger.error("mapPut {} = {}", prefixKey, value, e);
		} finally {
			returnResource(jedis);
		}
		return result;
	}
	
	/**
	 * 向Map缓存中添加值
	 * @param key 键
	 * @param value 值
	 * @return
	 */
	public static String mapObjectPut(String key, Map<String, Object> value) {
		String result = null;
		Jedis jedis = null;
		String prefixKey = wrapKeyPrefix(key);
		try {
			jedis = getResource();
			Map<byte[], byte[]> map = new HashMap();
			for (Map.Entry<String, Object> e : value.entrySet()){
				map.put(getBytesKey(e.getKey()), toBytes(e.getValue()));
			}
			result = jedis.hmset(getBytesKey(prefixKey), (Map<byte[], byte[]>) map);
			if(logger.isDebugEnabled()){
				logger.debug("mapObjectPut {} = {}", prefixKey, value);
			}
		} catch (Exception e) {
			logger.error("mapObjectPut {} = {}", prefixKey, value, e);
		} finally {
			returnResource(jedis);
		}
		return result;
	}
	
	/**
	 * 移除Map缓存中的值
	 * @param key 键
	 * @param mapKey 值
	 * @return
	 */
	public static long mapRemove(String key, String mapKey) {
		long result = 0;
		Jedis jedis = null;
		String prefixKey = wrapKeyPrefix(key);
		try {
			jedis = getResource();
			result = jedis.hdel(prefixKey, mapKey);
			if(logger.isDebugEnabled()){
				logger.debug("mapRemove {}  {}", prefixKey, mapKey);
			}
		} catch (Exception e) {
			logger.error("mapRemove {}  {}", prefixKey, mapKey, e);
		} finally {
			returnResource(jedis);
		}
		return result;
	}
	
	/**
	 * 移除Map缓存中的值
	 * @param key 键
	 * @param mapKey 值
	 * @return
	 */
	public static long mapObjectRemove(String key, String mapKey) {
		long result = 0;
		Jedis jedis = null;
		String prefixKey = wrapKeyPrefix(key);
		try {
			jedis = getResource();
			result = jedis.hdel(getBytesKey(prefixKey), getBytesKey(mapKey));
			if(logger.isDebugEnabled()){
				logger.debug("mapObjectRemove {}  {}", prefixKey, mapKey);
			}
		} catch (Exception e) {
			logger.error("mapObjectRemove {}  {}", prefixKey, mapKey, e);
		} finally {
			returnResource(jedis);
		}
		return result;
	}
	
	/**
	 * 判断Map缓存中的Key是否存在
	 * @param key 键
	 * @param mapKey 值
	 * @return
	 */
	public static boolean mapExists(String key, String mapKey) {
		boolean result = false;
		Jedis jedis = null;
		String prefixKey = wrapKeyPrefix(key);
		try {
			jedis = getResource();
			result = jedis.hexists(prefixKey, mapKey);
			if(logger.isDebugEnabled()){
				logger.debug("mapExists {}  {}", prefixKey, mapKey);
			}
		} catch (Exception e) {
			logger.error("mapExists {}  {}", prefixKey, mapKey, e);
		} finally {
			returnResource(jedis);
		}
		return result;
	}
	
	/**
	 * 判断Map缓存中的Key是否存在
	 * @param key 键
	 * @param mapKey 值
	 * @return
	 */
	public static boolean mapObjectExists(String key, String mapKey) {
		boolean result = false;
		Jedis jedis = null;
		String prefixKey = wrapKeyPrefix(key);
		try {
			jedis = getResource();
			result = jedis.hexists(getBytesKey(prefixKey), getBytesKey(mapKey));
			if(logger.isDebugEnabled()){
				logger.debug("mapObjectExists {}  {}", prefixKey, mapKey);
			}
		} catch (Exception e) {
			logger.error("mapObjectExists {}  {}", prefixKey, mapKey, e);
		} finally {
			returnResource(jedis);
		}
		return result;
	}
	
	/**
	 * 删除缓存
	 * @param key 键
	 * @return
	 */
	public static long del(String key) {
		long result = 0;
		Jedis jedis = null;
		String prefixKey = wrapKeyPrefix(key);
		try {
			jedis = getResource();
			if (jedis.exists(prefixKey)){
				result = jedis.del(prefixKey);
				if(logger.isDebugEnabled())
				logger.debug("del {}", prefixKey);
			}else{
				if(logger.isDebugEnabled())
				logger.debug("del {} not exists", prefixKey);
			}
		} catch (Exception e) {
			logger.error("del {}", prefixKey, e);
		} finally {
			returnResource(jedis);
		}
		return result;
	}

	/**
	 * 删除缓存
	 * @param key 键
	 * @return
	 */
	public static long delObject(String key) {
		long result = 0;
		Jedis jedis = null;
		String prefixKey = wrapKeyPrefix(key);
		try {
			jedis = getResource();
			if (jedis.exists(getBytesKey(prefixKey))){
				result = jedis.del(getBytesKey(prefixKey));
				if(logger.isDebugEnabled())
				logger.debug("delObject {}", prefixKey);
			}else{
				if(logger.isDebugEnabled())
				logger.debug("delObject {} not exists", prefixKey);
			}
		} catch (Exception e) {
			logger.error("delObject {}", prefixKey, e);
		} finally {
			returnResource(jedis);
		}
		return result;
	}
	
	/**
	 * 缓存是否存在
	 * @param key 键
	 * @return
	 */
	public static boolean exists(String key) {
		boolean result = false;
		Jedis jedis = null;
		String prefixKey = wrapKeyPrefix(key);
		try {
			jedis = getResource();
			result = jedis.exists(prefixKey);
			if(logger.isDebugEnabled())
			logger.debug("exists {}", prefixKey);
		} catch (Exception e) {
			logger.error("exists {}", prefixKey, e);
		} finally {
			returnResource(jedis);
		}
		return result;
	}
	
	/**
	 * 缓存是否存在
	 * @param key 键
	 * @return
	 */
	public static boolean existsObject(String key) {
		boolean result = false;
		Jedis jedis = null;
		String prefixKey = wrapKeyPrefix(key);
		try {
			jedis = getResource();
			result = jedis.exists(getBytesKey(prefixKey));
			if(logger.isDebugEnabled())
			logger.debug("existsObject {}", prefixKey);
		} catch (Exception e) {
			logger.error("existsObject {}", prefixKey, e);
		} finally {
			returnResource(jedis);
		}
		return result;
	}

	/**
	 * 获取资源
	 * @return
	 * @throws JedisException
	 */
	public static Jedis getResource() throws JedisException {
		Jedis jedis = null;
		if(jedisPool == null){
			throw new RuntimeException("jedisPool has not been initialized,use [new JedisUtils().setJedisPool() method to init jedisPool]");
		}
		try {
			jedis = jedisPool.getResource();
		} catch (JedisException e) {
			logger.error("getResource.", e);
			returnBrokenResource(jedis);
			throw e;
		}
		return jedis;
	}

	/**
	 * 归还资源
	 * @param jedis
	 */
	public static void returnBrokenResource(Jedis jedis) {
		if (jedis != null) {
			jedis.close();
		}
	}
	
	/**
	 * 释放资源
	 * @param jedis
	 */
	public static void returnResource(Jedis jedis) {
		if (jedis != null) {
			 jedis.close();
		}
	}

	/**
	 * 获取byte[]类型Key
	 * @return
	 */
	public static byte[] getBytesKey(Object object){
		if(object instanceof String){
			try {
				return ((String)object).getBytes("utf-8");
			} catch (UnsupportedEncodingException e) {
				return null;
			}
    	}else{
    		return SerializeUtils.serialize(object);
    	}
	}
	
	/**
	 * Object转换byte[]类型
	 * @param object
	 * @return
	 */
	public static byte[] toBytes(Object object){
    	return SerializeUtils.serialize(object);
	}

	/**
	 * byte[]型转换Object
	 * @param bytes
	 * @return
	 */
	public static Object toObject(byte[] bytes){
		return SerializeUtils.unserialize(bytes);
	}


	public static void setJedisPool(JedisPool jedisPool) {
		if(JedisUtils.jedisPool == null){
			JedisUtils.jedisPool = jedisPool;
		}else {
			//throw new RuntimeException("jedisPool has been initialized");
		}

	}

	public static void setKeyPrefix(String keyPrefix) {
		JedisUtils.keyPrefix = keyPrefix;

	}
}
