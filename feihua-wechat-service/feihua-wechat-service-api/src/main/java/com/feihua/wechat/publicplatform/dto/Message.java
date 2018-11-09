package com.feihua.wechat.publicplatform.dto;

import java.io.Serializable;

/**
 * 微信消息
 * Created by yw on 2016/3/14.
 */
public abstract class Message implements Serializable {
    private String toUserName;
    private String fromUserName;
    private long createTime;
    private String msgType;

    public Message(){
        //由于微信服务端需要的时间整形是以秒为单位的，故需要除以1000L
        this.createTime = System.currentTimeMillis() / 1000L;
    }
    public String getToUserName() {
        return toUserName;
    }

    public void setToUserName(String toUserName) {
        this.toUserName = toUserName;
    }

    public String getFromUserName() {
        return fromUserName;
    }

    public void setFromUserName(String fromUserName) {
        this.fromUserName = fromUserName;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }
}
