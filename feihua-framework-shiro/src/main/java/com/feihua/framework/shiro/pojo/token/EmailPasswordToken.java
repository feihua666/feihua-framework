package com.feihua.framework.shiro.pojo.token;

import org.apache.shiro.authc.UsernamePasswordToken;

/**
 * 邮箱，密码 token
 * Created by yw on 2016/1/15.
 */
public class EmailPasswordToken extends UsernamePasswordToken {

    public EmailPasswordToken(String email,String password){
        this.email = email;
        setPassword((password != null?password.toCharArray():null));
    }
    public EmailPasswordToken(String email, String password, boolean rememberMe, String host){
        this.email = email;
        setPassword((password != null?password.toCharArray():null));
        setRememberMe(rememberMe);
        setHost(host);
    }

    /**
     * 邮箱
     */
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
