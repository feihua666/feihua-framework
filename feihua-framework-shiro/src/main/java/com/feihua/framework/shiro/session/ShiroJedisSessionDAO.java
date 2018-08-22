package com.feihua.framework.shiro.session;

import com.feihua.framework.jedis.utils.JedisUtils;
import com.feihua.framework.shiro.pojo.ShiroUser;
import com.feihua.framework.shiro.utils.ShiroUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

import java.io.Serializable;
import java.util.*;

/**
 * redis session dao
 * yangwei 2017年7月26日 12:30:56
 */
public class ShiroJedisSessionDAO extends AbstractSessionDAO {

	private Logger logger = LoggerFactory.getLogger(ShiroJedisSessionDAO.class);

	private String sessionKeyPrefix = "shiro_session_";
	private String sessionUserKeyPrefix = "shiro_session_user_";

	@Override
	public void update(Session session) throws UnknownSessionException {
		if (session == null || session.getId() == null) {  
            return;
        }

		Jedis jedis = null;
		try {
			jedis = JedisUtils.getResource();

			String userId = "";
			ShiroUser su = ShiroUtils.getShiroUser(session);
			if (su != null) {
				userId = su.getId();
			}
			jedis.hset(sessionKeyPrefix, session.getId().toString(),String.valueOf(session.getTimeout()) + "|" + userId);

			jedis.set(JedisUtils.getBytesKey(sessionKeyPrefix + session.getId()), JedisUtils.toBytes(session));
			// 设置超期时间
			int timeoutSeconds = (int) (session.getTimeout() / 1000);
			jedis.expire((sessionKeyPrefix + session.getId()), timeoutSeconds);
			// 记录同一个用户的会话id
			if (su != null) {
				jedis.hset(sessionUserKeyPrefix + su.getId(),session.getId().toString(), session.getId().toString());
			}

			logger.debug("update session {}", session.getId());
		} catch (Exception e) {
			logger.error("update session {}", session.getId(), e);
		} finally {
			JedisUtils.returnResource(jedis);
		}
	}

	@Override
	public void delete(Session session) {
		if (session == null || session.getId() == null) {
			return;
		}
		Jedis jedis = null;
		try {
			jedis = JedisUtils.getResource();
			jedis.hdel(sessionKeyPrefix, session.getId().toString());
			jedis.del(JedisUtils.getBytesKey(sessionKeyPrefix + session.getId()));
			//删除同一个用户登录的当前会话记录
			ShiroUser su = ShiroUtils.getShiroUser(session);
			if (su != null) {
				jedis.hdel(sessionUserKeyPrefix + su.getId(),session.getId().toString());
			}

			if(logger.isDebugEnabled()){
				logger.debug("delete session {} ", session.getId());
			}
		} catch (Exception e) {
			logger.error("delete session {} ", session.getId(), e);
		} finally {
			JedisUtils.returnResource(jedis);
		}
	}

	@Override
	public Collection<Session> getActiveSessions() {
		Set<Session> sessions = new HashSet();

		Jedis jedis = null;
		try {
			jedis = JedisUtils.getResource();
			Map<String, String> map = jedis.hgetAll(sessionKeyPrefix);
			for (Map.Entry<String, String> e : map.entrySet()){
				if (StringUtils.isNotBlank(e.getKey()) && StringUtils.isNotBlank(e.getValue())){
					String values[] = e.getValue().split("|");
					if (jedis.exists(sessionKeyPrefix + e.getKey())){
						Session session = (Session) JedisUtils.toObject(jedis.get(JedisUtils.getBytesKey(sessionKeyPrefix + e.getKey())));
						if(session == null){
							//session 不存在
							jedis.hdel(sessionKeyPrefix, e.getKey());

							//删除同一个用户登录的当前会话记录
							// values[1]为userId    e.getKey() 为sessionId
							if (values.length == 2 && StringUtils.isNotEmpty(values[1])) {
								jedis.hdel(sessionUserKeyPrefix + values[1],e.getKey());
							}

							continue;
						}
						sessions.add(session);
					}
					// 存储的SESSION不符合规则
					else{
						jedis.hdel(sessionKeyPrefix, e.getKey());
						//删除同一个用户登录的当前会话记录
						// values[1]为userId    e.getKey() 为sessionId
						if (values.length == 2 && StringUtils.isNotEmpty(values[1])) {
							jedis.hdel(sessionUserKeyPrefix + values[1],e.getKey());
						}
					}
				}
				// 存储的SESSION无Value
				else if (StringUtils.isNotBlank(e.getKey())){
					jedis.hdel(sessionKeyPrefix, e.getKey());
					// 删除同一个用户登录的当前会话记录
					if (jedis.exists(sessionKeyPrefix + e.getKey())){
						// 这个session相当于一个僵尸session，只能通过id从redis中拿到
						Session session = (Session) JedisUtils.toObject(jedis.get(JedisUtils.getBytesKey(sessionKeyPrefix + e.getKey())));
						ShiroUser su = ShiroUtils.getShiroUser(session);
						if (su != null) {
							jedis.hdel(sessionUserKeyPrefix + su.getId(),session.getId().toString());
						}
					}
				}
			}
			if(logger.isInfoEnabled())
				logger.info("getActiveSessions size: {} ", sessions.size());
		} catch (Exception e) {
			logger.error("getActiveSessions", e);
		} finally {
			JedisUtils.returnResource(jedis);
		}
		return sessions;
	}

	@Override
	protected Serializable doCreate(Session session) {
		Serializable sessionId = this.generateSessionId(session);
		this.assignSessionId(session, sessionId);
		this.update(session);
		logger.debug("create new session {}", sessionId);
		return sessionId;
	}

	@Override
	protected Session doReadSession(Serializable sessionId) {
		if(sessionId == null || StringUtils.isEmpty(sessionId.toString())){
			return null;
		}
		Session session = null;

		Jedis jedis = null;
		try {
			jedis = JedisUtils.getResource();
			session = (Session) JedisUtils.toObject(jedis.get(
					JedisUtils.getBytesKey(sessionKeyPrefix + sessionId)));
			if(session == null){
				logger.debug("doReadSession from jedis by sessionId {} session is null", sessionId);

			}else {
				logger.debug("doReadSession from jedis by sessionId {} success", sessionId);
			}

		} catch (Exception e) {
			logger.error("readSession from jedis by sessionId {} exception", sessionId, e);
		} finally {
			JedisUtils.returnResource(jedis);
		}

		return session;
	}



	public List<Session> getSessionsByUserId(String userId) {
		List<Session> sessions = new ArrayList<>();

		Jedis jedis = null;
		try {
			jedis = JedisUtils.getResource();
			Map<String, String> map = jedis.hgetAll(sessionUserKeyPrefix + userId);
			for (Map.Entry<String, String> e : map.entrySet()){
				if (StringUtils.isNotBlank(e.getKey())){
					if (jedis.exists(sessionKeyPrefix + e.getKey())){
						Session session = (Session) JedisUtils.toObject(jedis.get(JedisUtils.getBytesKey(sessionKeyPrefix + e.getKey())));
						if(session == null){
							continue;
						}
						sessions.add(session);
					}
				}
			}
			if(logger.isInfoEnabled())
				logger.info("getSessionsByUserId size={} userId={}", sessions.size(),userId);
		} catch (Exception e) {
			logger.error("getSessionsByUserId,userId={}", e,userId);
		} finally {
			JedisUtils.returnResource(jedis);
		}
		return sessions;
	}
	public String getSessionKeyPrefix() {
		return sessionKeyPrefix;
	}

	public void setSessionKeyPrefix(String sessionKeyPrefix) {
		this.sessionKeyPrefix = sessionKeyPrefix;
	}
}
