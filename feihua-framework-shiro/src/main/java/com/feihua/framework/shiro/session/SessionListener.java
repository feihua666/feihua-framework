package com.feihua.framework.shiro.session;

import org.apache.shiro.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * shiro session 监听
 * Created by yangwei
 * Created at 2017/7/25 9:21
 */
public class SessionListener implements org.apache.shiro.session.SessionListener {

    private static final Logger logger = LoggerFactory.getLogger(SessionListener.class);
    @Override
    public void onStart(Session session) {
        logger.debug("session id={} is created", session.getId());
    }

    @Override
    public void onStop(Session session) {
        logger.debug("session id={} is destroyed", session.getId());
    }

    @Override
    public void onExpiration(Session session) {
        logger.debug("session id={} is expired", session.getId());
    }
}
