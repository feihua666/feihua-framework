package com.feihua.wechat.publicplatform.dto;

/**
 * 关注/取消关注消息
 * Created by yw on 2016/3/14.
 */
public class RequestSubscribeMessage extends RequestMessage {
    private String event;

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

}
