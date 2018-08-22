package com.feihua.wechat.publicplatform.dto;

/**
 * 微信消息类型
 * Created by yangwei
 * Created at 2018/7/20 11:36
 */
public class MsgType {
    /**
     * 消息事件
     */
    private MsgEvent msgEvent;

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MsgEvent getMsgEvent() {
        return msgEvent;
    }

    public void setMsgEvent(MsgEvent msgEvent) {
        this.msgEvent = msgEvent;
    }
}
