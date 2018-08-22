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
     */
    public void onAddWexinUser(WeixinUserDto weixinUserDto);
}
