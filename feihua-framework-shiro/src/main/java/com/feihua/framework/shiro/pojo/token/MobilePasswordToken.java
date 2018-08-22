package com.feihua.framework.shiro.pojo.token;

import org.apache.shiro.authc.UsernamePasswordToken;

/**
 * 手机号，密码 token
 * Created by yw on 2016/1/15.
 */
public class MobilePasswordToken extends UsernamePasswordToken {

    public MobilePasswordToken(String mobile,String password){
        this.mobile = mobile;
        setPassword((password != null?password.toCharArray():null));
    }
    public MobilePasswordToken(String mobile, String password, boolean rememberMe, String host){
        this.mobile = mobile;
        setPassword((password != null?password.toCharArray():null));
        setRememberMe(rememberMe);
        setHost(host);
    }

    /**
     * 手机号
     */
    private String mobile;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
