package com.feihua.wechat.miniprogram.dto;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yangwei
 * Created at 2018/8/16 15:49
 */
public class ResponseTextMessage extends ResponseMessage {

    private Map<String,String> text;
    public ResponseTextMessage(){
        super("text");
    }
    public ResponseTextMessage(String content){
        this();
        this.text = new HashMap<>();
        text.put("content",content);
    }

    public Map<String, String> getText() {
        return text;
    }

    public void setText(Map<String, String> text) {
        this.text = text;
    }
}
