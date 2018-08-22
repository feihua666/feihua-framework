package com.feihua.wechat.publicplatform.api;

/**
 * 公众号api
 * Created by yangwei
 * Created at 2018/7/20 9:54
 */
public interface ApiPublicPlatformService {

    /**
     * 处理微信平台发来的消息
     * @param postXmlData
     * @param which
     * @return
     */
    public String processMsg(String postXmlData, final String which);
}
