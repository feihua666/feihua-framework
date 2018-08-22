package com.feihua.wechat.publicplatform.dto;


import java.io.Serializable;

public class JsapiTicket implements Serializable{
    private String ticket;
    /**
     * 有效时间，秒，7200秒
     */
    private int expiresIn;
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
        //减60s也就是1分钟,提前获取
        if(System.currentTimeMillis()/1000 - createTime -60> expiresIn){
            return true;
        }
        return result;
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
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
}
