package com.feihua.wechat.miniprogram.dto;

import com.feihua.wechat.publicplatform.dto.Message;

/**
 * 接收微信消息
 * Created by yw on 2016/3/14.
 */
public  class RequestMessage extends Message{
    private String msgId;

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }
}
