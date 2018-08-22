package com.feihua.framework.shiro.pojo.token;

import org.apache.shiro.authc.UsernamePasswordToken;

/**
 * 微信小程序token
 * Created by yw on 2016/1/15.
 */
public class WxMiniProgramToken extends UsernamePasswordToken {
    /**
     * openid
     */
    private String openid;

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }


    @Override
    public Object getPrincipal() {
        return openid;
    }

    @Override
    public Object getCredentials() {
        return null;
    }
}
