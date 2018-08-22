package com.feihua.framework.mybatis.orm.interceptor;

import com.feihua.framework.jedis.utils.JedisUtils;
import com.feihua.framework.mybatis.orm.cache.MybatisOrmCache;
import com.feihua.framework.mybatis.orm.cache.RedisCache;
import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.parsing.XNode;
import org.apache.ibatis.parsing.XPathParser;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class CacheManager {

	//每一个statementId 更新依赖的statementId集合
	private Map<String,Set<String>> statements = new ConcurrentHashMap<String,Set<String>>();
	private static int cacheSeconds =  MybatisOrmCache.cacheSeconds;
	/**
	 * 标识是否已经初始化配置
	 */
	private boolean initialized = false;
	/**
	 * 是否开启缓存
	 */
	private boolean cacheEnabled = false;


	private static CacheManager cacheManager;

	private CacheManager(){}
	public static CacheManager getInstance()
	{
		return cacheManager ==null ? (cacheManager =new CacheManager()): cacheManager;
	}

	public boolean isInitialized() {
		return initialized;
	}
	
	public void initialize(Properties properties) {
		//cacheEnabled
		String cacheEnabled = properties.getProperty("cacheEnabled", "true");
		if("true".equals(cacheEnabled))
		{
			this.cacheEnabled = true;
		}
		//依赖关系
		String dependency = properties.getProperty("dependency");
		if(!("".equals(dependency) || dependency==null))
		{
			InputStream inputStream;
			try
			{
				inputStream = Resources.getResourceAsStream(dependency);
				XPathParser parser = new XPathParser(inputStream);
				List<XNode> statements = parser.evalNodes("/dependencies/statements/statement");
				for(XNode node :statements)
				{
					Set<String> temp = new HashSet<>();
					List<XNode> obs = node.evalNodes("observer");
					for(XNode observer:obs)
					{
						temp.add(observer.getStringAttribute("id"));
					}
					this.statements.put(node.getStringAttribute("id"), temp);
				}
				initialized = true;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * 记录一个statementid对应的cachekey
	 * @param queryMappedStatementId
	 * @param cacheKey
	 */
	public void saveCachekeyOfStatmentId(String queryMappedStatementId, CacheKey cacheKey){

		Set<String> set = JedisUtils.getSet(MybatisOrmCache.KEY_PREFIX+ queryMappedStatementId);
		if(set == null){
			set = new HashSet<>();
		}
		if(!set.contains(cacheKey.toString())){
			set.add(cacheKey.toString());
			JedisUtils.setSet(MybatisOrmCache.KEY_PREFIX+ queryMappedStatementId, set, cacheSeconds);
		}

	}
	/**
	 * 记录一个statementid对应的cachekeys
	 * @param queryMappedStatementId
	 * @param cacheKeys
	 */
	public void saveCachekeyOfStatmentId(String queryMappedStatementId, Set<CacheKey> cacheKeys){

		Set<String> set = JedisUtils.getSet(MybatisOrmCache.KEY_PREFIX+ queryMappedStatementId);
		if(set == null){
			set = new HashSet<>();
		}
		if(cacheKeys != null){
			for(CacheKey cacheKey:cacheKeys){
				set.add(cacheKey.toString());
			}
		}

		JedisUtils.setSet(MybatisOrmCache.KEY_PREFIX+ queryMappedStatementId, set, cacheSeconds);

	}
	/**
	 * 清空保存的cacheKey
	 * @param queryMappedStatementId
	 */
	private void clearCachekeyOfStatmentId(String queryMappedStatementId){

		JedisUtils.del(MybatisOrmCache.KEY_PREFIX + queryMappedStatementId);
	}

	/**
	 * 清空保存的cacheKey
	 * @param queryMappedStatementId
	 * @param cacheKey
	 */
	private void clearCachekeyOfStatmentId(String queryMappedStatementId, CacheKey cacheKey){

		Map<String,Object> map = JedisUtils.getObjectMap(MybatisOrmCache.KEY_PREFIX + queryMappedStatementId);
		if(map == null){
			return;
		}
		map.remove(cacheKey.toString());
		if(map.isEmpty()){
			clearCachekeyOfStatmentId(queryMappedStatementId);
		}else{
			JedisUtils.setObjectMap(MybatisOrmCache.KEY_PREFIX + queryMappedStatementId,map,cacheSeconds);
		}

	}

	/**
	 * 删除缓存数据
	 * @param updateMappedStatementId
	 * @param cache
	 */
	public void removeCache(String updateMappedStatementId, Cache cache){

		// cacheId,即RedisCache的id
		String updateMapper = updateMappedStatementId.substring(0,updateMappedStatementId.lastIndexOf("."));

		// 查检有没有配置，如果没有配置，setMapping为obsevers 也就是query查询的statementId
		Set<String> queryStatementIds = this.statements.get(updateMappedStatementId);
		if(queryStatementIds == null){
			queryStatementIds = this.statements.get(updateMapper);
			if(queryStatementIds == null){
				return;
			}
		}
		for(String mapping : queryStatementIds){
			String cacheId = mapping.substring(0,mapping.lastIndexOf("."));
			Set<String> setCacheKey = JedisUtils.getSet(MybatisOrmCache.KEY_PREFIX + mapping);
			if(setCacheKey == null){
				continue;
			}
			for(String cacheKey:setCacheKey){
				if(cache == null){
					//这里写死了，留待以后灵活配置
					new RedisCache(cacheId).removeObject(cacheKey);
				}else {
					cache.removeObject(cacheKey);
				}
			}
			this.clearCachekeyOfStatmentId(mapping);
		}
	}

	/**
	 * 判断某一个查询id是否存在
	 * @param queryMappedStatementId
	 * @return
	 */
	public boolean isQueryStatementIdConfig(String queryMappedStatementId){
        for (String key : this.statements.keySet()) {
            Set<String> queryStatementIds = this.statements.get(key);
            if(queryStatementIds != null && !queryStatementIds.isEmpty()){
                for (String queryStatementId : queryStatementIds) {
                    if(queryStatementId.equals(queryMappedStatementId)){
                        return true;
                    }
                }
            }
        }

        return false;
	}

    /**
     * 判断某一个更新id是否存在
     * @param updateMappedStatementId
     * @return
     */
    public boolean isUpdateStatementIdConfig(String updateMappedStatementId){
        String mapperStr = updateMappedStatementId.substring(0,updateMappedStatementId.lastIndexOf("."));
        for (String updateStatementId : this.statements.keySet()) {

            if(updateStatementId.equals(updateMappedStatementId) || updateStatementId.equals(mapperStr)){
                return true;
            }
        }

        return false;
    }

	public boolean isCacheEnabled() {
		return cacheEnabled;
	}

	public Map<String, Set<String>> getStatements() {
		return statements;
	}
}

