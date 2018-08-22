package com.feihua.framework.shiro.pojo;


import com.feihua.framework.shiro.pojo.PasswordAndSalt;

import java.io.Serializable;

/**
 * Created by yangwei
 * Created at 2017/7/24 15:56
 */
public class AuthenticationInfo implements Serializable{

    private String userId;
    private PasswordAndSalt passwordAndSalt;
    private boolean locked = false;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public PasswordAndSalt getPasswordAndSalt() {
        return passwordAndSalt;
    }

    public void setPasswordAndSalt(PasswordAndSalt passwordAndSalt) {
        this.passwordAndSalt = passwordAndSalt;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }
}
