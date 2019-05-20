package com.feihua.wechat.common.api;

import com.feihua.wechat.common.dto.WeixinUserDto;

/**
 * Created by yangwei
 * Created at 2018/7/24 18:14
 */
public interface ApiWeixinUserListener {
    /**
     * 完成用户插入监听
     * @param weixinUserDto
     * @param fromClientId 用户来源客户端
     */
    public void onAddWexinUser(WeixinUserDto weixinUserDto,String fromClientId);
    public void onUpdateWexinUser(WeixinUserDto weixinUserDto,String fromClientId);
}
