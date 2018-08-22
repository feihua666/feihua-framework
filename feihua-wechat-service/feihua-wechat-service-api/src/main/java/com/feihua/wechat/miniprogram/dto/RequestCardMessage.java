package com.feihua.wechat.miniprogram.dto;

/**
 * Created by yangwei
 * Created at 2018/8/16 15:25
 */
public class RequestCardMessage extends RequestMessage {

    public RequestCardMessage(){
        super.setMsgType("miniprogrampage");
    }

    private String title;
    private String appId;
    private String pagePath;
    private String thumbUrl;
    private String thumbMediaId;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getPagePath() {
        return pagePath;
    }

    public void setPagePath(String pagePath) {
        this.pagePath = pagePath;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }

    public String getThumbMediaId() {
        return thumbMediaId;
    }

    public void setThumbMediaId(String thumbMediaId) {
        this.thumbMediaId = thumbMediaId;
    }
}
