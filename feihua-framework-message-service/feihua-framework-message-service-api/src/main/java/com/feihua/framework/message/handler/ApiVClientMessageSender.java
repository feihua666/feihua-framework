package com.feihua.framework.message.handler;

import com.feihua.framework.base.modules.loginclient.dto.BaseLoginClientDto;
import com.feihua.framework.message.dto.BaseMessageClientDto;
import com.feihua.framework.message.dto.VClientMessageSendParamDto;

import java.util.List;

/**
 * 虚拟客户端消息发送处理,主要负责三方渠道发送
 * 可以做不同的实现以实现不同的发送功能，如推送等发送处理
 * 请发送完成后更新更新 base_message_v_user表中发送状态
 * Created by yangwei
 * Created at 2018/11/1 17:13
 */
public interface ApiVClientMessageSender {
    /**
     * 真正的发送消息处理
     * @param dto
     */
    public void doMessageSend(VClientMessageSendParamDto dto);

    /**
     * 是否支持该客户端的处理
     * @param clientDto 客户端
     * @return 是否支持客户的发送
     */
    public boolean support(BaseLoginClientDto clientDto, List<BaseMessageClientDto> messageClientDtos);

}
