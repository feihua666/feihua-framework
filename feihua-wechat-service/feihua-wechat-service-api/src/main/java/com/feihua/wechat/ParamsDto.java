package com.feihua.wechat;

import java.io.Serializable;

/**
 * Created by yangwei
 * Created at 2018/4/28 14:06
 */
public class ParamsDto implements Serializable{

    private String appId;
    private String secret;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }
}
