package com.feihua.wechat.publicplatform.dto;

/**
 * Created by yangwei
 * Created at 2018/8/16 17:59
 */
public class MenuItemPicPhotoOrAlbum extends MenuItem {

    public MenuItemPicPhotoOrAlbum(){
        super("pic_photo_or_album");
    }
    public MenuItemPicPhotoOrAlbum(WeixinMenuDto menuDto){
        this();
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
