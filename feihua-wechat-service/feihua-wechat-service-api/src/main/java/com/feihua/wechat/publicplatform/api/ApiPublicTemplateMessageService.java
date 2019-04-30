package com.feihua.wechat.publicplatform.api;

import com.feihua.wechat.publicplatform.dto.WxPublicTemplateParam;

/**
 * 微信模板消息处理，该服务不支持被动回复消息，如果发回复被动消息请参见 com.feihua.wechat.publicplatform.api.MsgTypeHandler
 * Created by yangwei
 * Created at 2019/4/28 17:13
 */
public interface ApiPublicTemplateMessageService {
    /**
     * 发送模板消息
     * @param wxPublicTemplate
     * @param which
     */
    void send(WxPublicTemplateParam wxPublicTemplate, String which);
}
