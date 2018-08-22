package com.feihua.wechat.publicplatform.dto;

/**
 * Created by yangwei
 * Created at 2018/8/16 17:59
 */
public class MenuItemView extends MenuItem {

    public MenuItemView(){
        super("view");
    }
    public MenuItemView(WeixinMenuDto menuDto){
        this();
        super.setName(menuDto.getName());
        this.url = menuDto.getUrl();
    }
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
