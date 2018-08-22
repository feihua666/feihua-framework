package com.feihua.wechat.publicplatform.dto;

import java.io.Serializable;

/**
 * Created by yangwei
 * Created at 2018/7/23 16:09
 */
public class AuthorizeAccessToken implements Serializable{

    private String accessToken;
    /**
     * 有效时间，秒,两个小时
     */
    private int expiresIn;
    private String refreshToken;
    private String openid;
    private String scope;
    private String which;
    /**
     * 生成token时间戳，秒
     */
    private long createTime;
    /**
     * 是否过期
     * @return
     */
    public boolean isExpires(){
        boolean result = false;
        //减600s也就是10分钟,提前获取
        if(System.currentTimeMillis()/1000 - createTime -600> expiresIn){
            return true;
        }
        return result;
    }
    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public int getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(int expiresIn) {
        this.expiresIn = expiresIn;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getWhich() {
        return which;
    }

    public void setWhich(String which) {
        this.which = which;
    }
}
