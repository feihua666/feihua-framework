package com.feihua.wechat.publicplatform.dto;

/**
 * Created by yangwei
 * Created at 2018/8/16 17:59
 */
public class MenuItemLocationSelect extends MenuItem {

    public MenuItemLocationSelect(){
        super("location_select");
    }
    public MenuItemLocationSelect(WeixinMenuDto menuDto){
        this();
        this.key = menuDto.getKey();
        super.setName(menuDto.getName());
    }
    private String key;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
