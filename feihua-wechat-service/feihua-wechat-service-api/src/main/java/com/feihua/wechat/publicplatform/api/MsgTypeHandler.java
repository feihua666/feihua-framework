package com.feihua.wechat.publicplatform.api;

import com.feihua.wechat.publicplatform.dto.MsgType;

/**
 * 微信消息处理，注入的类请带wx-public-前缀
 * Created by yangwei
 * Created at 2018/7/20 11:35
 */
public interface MsgTypeHandler {
    /**
     * 处理消息
     * @param postXmlData
     * @param which
     * @return
     */
    public String handleMsg(String postXmlData, final String which);
}
