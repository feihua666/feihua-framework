package com.feihua.framework.mybatis.orm.interceptor;


import com.feihua.framework.mybatis.orm.MybatisMapperConfig;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


@Intercepts(value = {
		@Signature(args = {MappedStatement.class, Object.class, RowBounds.class,ResultHandler.class, CacheKey.class, BoundSql.class}, method = "query", type = Executor.class),
		@Signature(args = {MappedStatement.class, Object.class}, method = "update", type = Executor.class)
})
/**
 * 缓存记录简单类，支持只设置需要缓存的xxxmapper.xml，不用担心没有设置缓存的xxxmapper.xml更新会产生脏数据
 */
public  class CacheInterceptor implements Interceptor {
	private static Logger logger = LoggerFactory.getLogger(CacheInterceptor.class);

	private static Map<String,Set<CacheKey>> queryCacheOnCommit = new ConcurrentHashMap<>();
	private static Set<String>   updateStatementOnCommit = new HashSet<>();

	CacheManager cachingManager = CacheManager.getInstance();
	@Autowired
	private Map<String, MybatisMapperConfig> mybatisMapperConfigs;

	public Object intercept(Invocation invocation) throws Throwable {
		String name = invocation.getMethod().getName();
		Object result =null;
		if("query".equals(name))
		{
			result = this.processQuery(invocation);
		}
		else if("update".equals(name))
		{
			result = this.processUpdate(invocation);
		}
		return result;
	}

	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}

	/**
	 * when executing a query operation
	 * 1. record this statement's id and it's corresponding Cache Object into Global Caching Manager;
	 * 2. record this statement's id and  
	 * @param invocation
	 * @return
	 * @throws Throwable
	 */
	protected Object processQuery(Invocation invocation) throws Throwable {

		if(cachingManager.isCacheEnabled())
		{
			Object[] args = invocation.getArgs();
			MappedStatement mappedStatement = (MappedStatement)args[0];

			//记录本次查询所产生的CacheKey
			CacheKey cacheKey = (CacheKey)args[4];

			//如果本条statementId表示的查询语句配置了 flushCache=true，则清空querCacheOnCommit缓存
			if(mappedStatement.isFlushCacheRequired())
			{
				queryCacheOnCommit.clear();
			}
			//如果本条statementId表示的查询语句配置了使用缓存，并且二级缓存不为空，则将StatementId 和对应的二级缓存对象映射关系添加到全局缓存映射管理器中
			if(mappedStatement.isUseCache() && mappedStatement.getCache()!=null)
			{
				//cachingManager.saveCachekeyOfStatmentId(mappedStatement.getId(),cacheKey);
				//记录本次查询所产生的CacheKey

					if(cachingManager.isQueryStatementIdConfig(mappedStatement.getId())){
						if(queryCacheOnCommit.get(mappedStatement.getId())==null) {
							queryCacheOnCommit.put(mappedStatement.getId(), new HashSet<CacheKey>());
						}
						queryCacheOnCommit.get(mappedStatement.getId()).add(cacheKey);
					}


			}

		}
		Object result = invocation.proceed();
		return result;
	}

	/**
	 * 如果是更新，记录更新记录
	 * @param invocation
	 * @return
	 * @throws Throwable
	 */
	protected Object processUpdate(Invocation invocation) throws Throwable {

		Object result = invocation.proceed();
		MappedStatement mappedStatement = (MappedStatement)invocation.getArgs()[0];

		if(cachingManager.isUpdateStatementIdConfig(mappedStatement.getId())){
			updateStatementOnCommit.add(mappedStatement.getId());
			this.refreshCache(invocation);
		}
		return result;
	}
	
	

	/**
	 * when the sqlSession has been committed,rollbacked,or closed,
	 * session buffer query CacheKeys and update Statement collections should be cleared.
	 *
	 * 当SqlSession 执行了commit()、rollback()、close()方法，
	 * Session级别的查询语句产生的CacheKey集合以及  执行的更新语句集合应该被清空
	 */
	private synchronized void clearSessionData(Invocation invocation)
	{
		queryCacheOnCommit.clear();
		updateStatementOnCommit.clear();
	}
	


	/**
	 * refresh the session cache,there are two things have to do:
	 * 1. add this session scope query logs to global cache Manager
	 * 2. clear the related caches according to the update statements as configured in "dependency" file
	 * 3. clear the session data
	 * 虽然加了同步，但多个服务器是不能同步的
	 */
	private synchronized void refreshCache(Invocation invocation)
	{
		for(String queryS :queryCacheOnCommit.keySet()){
			cachingManager.saveCachekeyOfStatmentId(queryS,queryCacheOnCommit.get(queryS));
		}
		for(String  updateS:updateStatementOnCommit){
			cachingManager.removeCache(updateS,null);
		}

		clearSessionData(invocation);
	}

	/**
	 * 
	 * 
	 * Executor插件配置信息加载点
	 * properties中有 "dependency" 属性来指示 配置的缓存依赖配置信息，读取文件，初始化CacheManager
	 */
	public void setProperties(Properties properties) {
		
		if(!cachingManager.isInitialized())
		{
			cachingManager.initialize(properties,mybatisMapperConfigs);
		}
	}
}
