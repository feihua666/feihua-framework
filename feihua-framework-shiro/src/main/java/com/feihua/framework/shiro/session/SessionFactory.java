package com.feihua.framework.shiro.session;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.SessionContext;

/**
 * session factory 生成 session
 * Created by yangwei
 * Created at 2017/7/25 12:50
 */
public class SessionFactory implements org.apache.shiro.session.mgt.SessionFactory {
    @Override
    public Session createSession(SessionContext sessionContext) {
        String host = sessionContext.getHost();
        ShiroSession shiroSession = new ShiroSession();
        shiroSession.setHost(host);
        return shiroSession;
    }
}
