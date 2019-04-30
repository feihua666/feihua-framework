package com.feihua.wechat.miniprogram.api;

import com.feihua.wechat.miniprogram.dto.MsgTemplateParamDto;

/**
 * Created by yangwei
 * Created at 2019/4/28 18:05
 */
public interface ApiMiniProgramTemplateMessageService {

    /**
     * 发送模板消息
     * @param dto
     */
    void send(MsgTemplateParamDto dto,String which);
}
