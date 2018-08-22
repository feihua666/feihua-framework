package com.feihua.wechat.miniprogram.dto;

/**
 * Created by yangwei
 * Created at 2018/8/16 15:25
 */
public class RequestTextMessage extends RequestMessage {

    public RequestTextMessage(){
        super.setMsgType("text");
    }
    public RequestTextMessage(String content){
        this();
        this.content = content;
    }

    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
