package com.feihua.wechat.miniprogram.dto;

import com.feihua.wechat.publicplatform.dto.Message;

/**
 * Created by yangwei
 * Created at 2018/8/16 15:25
 */
public class RequestEnterSessionEventMessage extends Message {


    public RequestEnterSessionEventMessage(){
        super.setMsgType("event");
    }

    private String event;
    private String sessionFrom;

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getSessionFrom() {
        return sessionFrom;
    }

    public void setSessionFrom(String sessionFrom) {
        this.sessionFrom = sessionFrom;
    }
}
