package com.feihua.wechat.miniprogram.dto;

import java.util.HashMap;
import java.util.Map;

/**
 * 图片消息
 * Created by yangwei
 * Created at 2018/8/16 15:49
 */
public class ResponseImageMessage extends ResponseMessage {

    private Map<String,String> image;
    public ResponseImageMessage(){
        super("image");
    }
    public ResponseImageMessage(String mediaId){
        this();
        this.image = new HashMap<>();
        image.put("media_id",mediaId);
    }

    public Map<String, String> getImage() {
        return image;
    }

    public void setImage(Map<String, String> image) {
        this.image = image;
    }
}
