package com.feihua.wechat.publicplatform.dto;

/**
 * Created by yangwei
 * Created at 2018/8/16 17:59
 */
public class MenuItemMediaId extends MenuItem {

    public MenuItemMediaId(){
        super("media_id");
    }
    public MenuItemMediaId(WeixinMenuDto menuDto){
        this();
        super.setName(menuDto.getName());
        this.mediaId = menuDto.getMediaId();
    }
    private String mediaId;

    public String getMediaId() {
        return mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }
}
