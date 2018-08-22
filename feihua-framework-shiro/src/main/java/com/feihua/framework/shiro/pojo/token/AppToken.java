package com.feihua.framework.shiro.pojo.token;

import org.apache.shiro.authc.UsernamePasswordToken;

/**
 * app token
 * Created by yw on 2016年4月15日 14:57:55
 */
public class AppToken extends UsernamePasswordToken {
    /**
     * 分类，备用
     */
    private String type;
    /**
     * openid
     */
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
