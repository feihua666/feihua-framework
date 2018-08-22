package com.feihua.wechat.publicplatform.dto;

/**
 * Created by yangwei
 * Created at 2018/8/16 17:59
 */
public class MenuItemViewLimited extends MenuItem {

    public MenuItemViewLimited(){
        super("view_limited");
    }
    public MenuItemViewLimited(WeixinMenuDto menuDto){
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
