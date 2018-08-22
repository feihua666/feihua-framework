package com.feihua.wechat.miniprogram.dto;

/**
 * Created by yangwei
 * Created at 2018/8/16 15:25
 */
public class RequestImageMessage extends RequestMessage {

    public RequestImageMessage(){
        super.setMsgType("image");
    }
    public RequestImageMessage(String picUrl,String mediaId){
        this();
        this.picUrl = picUrl;
        this.mediaId = mediaId;
    }
    private String picUrl;
    private String mediaId;

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getMediaId() {
        return mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }
}
