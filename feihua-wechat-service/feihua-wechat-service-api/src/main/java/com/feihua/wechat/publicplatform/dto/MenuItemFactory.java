package com.feihua.wechat.publicplatform.dto;

import com.feihua.framework.constants.DictEnum;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by yangwei
 * Created at 2018/8/17 9:14
 */
public class MenuItemFactory {

    public static MenuItem createMenuItem(WeixinMenuDto weixinMenuDto){
        MenuItem menuItem = null;
        if(StringUtils.isEmpty(weixinMenuDto.getType())){
            menuItem = new MenuItem();
            menuItem.setName(weixinMenuDto.getName());
        }else if(DictEnum.WeixinMenuType.click.name().equals(weixinMenuDto.getType())){
            menuItem = new MenuItemClick(weixinMenuDto);
        }else if(DictEnum.WeixinMenuType.view.name().equals(weixinMenuDto.getType())){
            menuItem = new MenuItemView(weixinMenuDto);
        }else if(DictEnum.WeixinMenuType.scancode_push.name().equals(weixinMenuDto.getType())){
            menuItem = new MenuItemScancodePush(weixinMenuDto);
        }else if(DictEnum.WeixinMenuType.scancode_waitmsg.name().equals(weixinMenuDto.getType())){
            menuItem = new MenuItemScancodeWaitmsg(weixinMenuDto);
        }else if(DictEnum.WeixinMenuType.pic_sysphoto.name().equals(weixinMenuDto.getType())){
            menuItem = new MenuItemPicSysPhoto(weixinMenuDto);
        }else if(DictEnum.WeixinMenuType.pic_photo_or_album.name().equals(weixinMenuDto.getType())){
            menuItem = new MenuItemPicPhotoOrAlbum(weixinMenuDto);
        }else if(DictEnum.WeixinMenuType.pic_weixin.name().equals(weixinMenuDto.getType())){
            menuItem = new MenuItemPicWeixin(weixinMenuDto);
        }else if(DictEnum.WeixinMenuType.location_select.name().equals(weixinMenuDto.getType())){
            menuItem = new MenuItemLocationSelect(weixinMenuDto);
        }else if(DictEnum.WeixinMenuType.media_id.name().equals(weixinMenuDto.getType())){
            menuItem = new MenuItemMediaId(weixinMenuDto);
        }else if(DictEnum.WeixinMenuType.view_limited.name().equals(weixinMenuDto.getType())){
            menuItem = new MenuItemViewLimited(weixinMenuDto);
        }else if(DictEnum.WeixinMenuType.miniprogram.name().equals(weixinMenuDto.getType())){
            menuItem = new MenuItemMiniprogram(weixinMenuDto);
        }

        return menuItem;
    }
}
