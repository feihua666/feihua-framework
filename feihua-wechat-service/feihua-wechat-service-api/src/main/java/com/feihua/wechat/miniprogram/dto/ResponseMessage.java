package com.feihua.wechat.miniprogram.dto;

/**
 * 响应回复消息
 * Created by yangwei
 * Created at 2018/8/16 15:48
 */
public abstract class  ResponseMessage {

    private String touser;
    private String msgtype;

    public ResponseMessage(String msgtype){
        this.msgtype = msgtype;
    }

    public String getTouser() {
        return touser;
    }

    public void setTouser(String touser) {
        this.touser = touser;
    }

    public String getMsgtype() {
        return msgtype;
    }

    public void setMsgtype(String msgtype) {
        this.msgtype = msgtype;
    }
}
