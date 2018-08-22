package com.feihua.wechat.publicplatform.dto;


import java.io.Serializable;

public class AccessToken implements Serializable {

    private String token;
    /**
     * 有效时间，秒,两个小时
     */
    private int expiresIn;
    /**
     * 生成token时间戳，秒
     */
    private long createTime;
    private String which;

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
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(int expiresIn) {
        this.expiresIn = expiresIn;
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
