package com.feihua.framework.shiro.pojo.token;

import org.apache.shiro.authc.UsernamePasswordToken;

/**
 * 用户帐号，密码 token
 * Created by yw on 2016/1/15.
 */
public class AccountPasswordToken extends UsernamePasswordToken {

    public AccountPasswordToken(String account,String password){
        this.account = account;
        setPassword((password != null?password.toCharArray():null));
    }
    public AccountPasswordToken(String account, String password, boolean rememberMe, String host){
        this.account = account;
        setPassword((password != null?password.toCharArray():null));
        setRememberMe(rememberMe);
        setHost(host);
    }
    private String account;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }
}
