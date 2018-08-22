package com.feihua.framework.shiro.session;

import org.apache.shiro.session.mgt.SimpleSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * 自定义shiro session
 * Created by yangwei
 * Created at 2017/7/25 12:47
 */
public class ShiroSession extends SimpleSession {
    private static final Logger logger = LoggerFactory.getLogger(ShiroSession.class);
    private static final long serialVersionUID = 1L;
    private boolean isChanged = false;

    public ShiroSession() {
        logger.debug("call ShiroSession method");
        this.setChanged(true);
    }

    public ShiroSession(String host) {
        super(host);
        logger.debug("call ShiroSession[host] method");
        this.setChanged(true);
    }

    public void setId(Serializable id) {
        logger.debug("call setId method");
        super.setId(id);
        this.setChanged(true);
    }

    public void setStopTimestamp(Date stopTimestamp) {
        logger.debug("call setStopTimestamp method");
        super.setStopTimestamp(stopTimestamp);
        this.setChanged(true);
    }

    public void setExpired(boolean expired) {
        logger.debug("call setExpired method");
        super.setExpired(expired);
        this.setChanged(true);
    }

    public void setTimeout(long timeout) {
        logger.debug("call setTimeout method");
        super.setTimeout(timeout);
        this.setChanged(true);
    }

    public void setHost(String host) {
        logger.debug("call setHost method");
        super.setHost(host);
        this.setChanged(true);
    }

    public void setAttributes(Map<Object, Object> attributes) {
        logger.debug("call setAttributes method");
        super.setAttributes(attributes);
        this.setChanged(true);
    }

    public void setAttribute(Object key, Object value) {
        logger.debug("call setAttribute method");
        super.setAttribute(key, value);
        this.setChanged(true);
    }

    public Object removeAttribute(Object key) {
        logger.debug("call removeAttribute method");
        this.setChanged(true);
        return super.removeAttribute(key);
    }

    public void stop() {
        logger.debug("call stop method");
        super.stop();
        this.setChanged(true);
    }

    protected void expire() {
        logger.debug("call expire method");
        this.stop();
        this.setExpired(true);
    }

    public boolean isChanged() {
        return this.isChanged;
    }

    public void setChanged(boolean isChanged) {
        this.isChanged = isChanged;
    }

    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    protected boolean onEquals(SimpleSession ss) {
        return super.onEquals(ss);
    }

    public int hashCode() {
        return super.hashCode();
    }

    public String toString() {
        return super.toString();
    }
}
