package com.feihua.wechat.miniprogram.api;

/**
 * Created by yangwei
 * Created at 2018/8/16 15:00
 */
public interface MsgTypeHandler {
    /**
     * 处理xml消息
     * @param postXmlData
     * @param which
     * @return
     */
    public String handleXmlMsg(String postXmlData, final String which);

    /**
     * 处理json消息
     * @param postJsonData
     * @param which
     * @return
     */
    public String handleJsonMsg(String postJsonData, final String which);
}
