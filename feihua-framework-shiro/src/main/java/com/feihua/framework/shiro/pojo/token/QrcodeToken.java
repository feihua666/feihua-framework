package com.feihua.framework.shiro.pojo.token;

import org.apache.shiro.authc.UsernamePasswordToken;

/**
 * 扫码登录 token
 * Created by yw on 2016/1/15.
 */
public class QrcodeToken extends UsernamePasswordToken {

    String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
