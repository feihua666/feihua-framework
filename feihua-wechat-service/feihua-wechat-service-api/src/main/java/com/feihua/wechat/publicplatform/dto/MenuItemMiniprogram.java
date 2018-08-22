package com.feihua.wechat.publicplatform.dto;

/**
 * 小程序入口
 * Created by yangwei
 * Created at 2018/8/16 17:59
 */
public class MenuItemMiniprogram extends MenuItem {

    public MenuItemMiniprogram(){
        super("miniprogram");
    }
    public MenuItemMiniprogram(WeixinMenuDto menuDto){
        this();
        super.setName(menuDto.getName());
        this.url = menuDto.getUrl();
        this.appid = menuDto.getAppid();
        this.pagepath = menuDto.getPagepath();
    }
    private String url;
    private String appid;
    private String pagepath;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getPagepath() {
        return pagepath;
    }

    public void setPagepath(String pagepath) {
        this.pagepath = pagepath;
    }
}
