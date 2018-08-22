package com.feihua.wechat.publicplatform.dto;

/**
 * Created by yangwei
 * Created at 2018/8/16 17:59
 */
public class MenuItemScancodePush extends MenuItem {

    public MenuItemScancodePush(){
        super("scancode_push");
    }
    public MenuItemScancodePush(WeixinMenuDto menuDto){
        this();
        super.setName(menuDto.getName());
        this.key = menuDto.getKey();
    }
    private String key;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
