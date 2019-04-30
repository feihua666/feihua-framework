package com.feihua.framework.message.handler.impl;

import com.feihua.framework.base.modules.loginclient.dto.BaseLoginClientDto;
import com.feihua.framework.base.modules.user.po.BaseUserPo;
import com.feihua.framework.message.dto.BaseMessageSendParamsDto;
import com.feihua.framework.message.dto.ClientMessageSendParamDto;
import com.feihua.framework.message.dto.MsgTemplateThirdParamDto;
import com.feihua.framework.message.po.BaseMessagePo;
import com.feihua.framework.message.po.BaseMessageTemplatePo;
import com.feihua.framework.message.po.BaseMessageTemplateThirdBindPo;

import java.util.List;

/**
 * Created by yangwei
 * Created at 2018/11/1 18:24
 */
public class PcMessageSenderImpl extends AbstractClientMessageSenderImpl {

    public PcMessageSenderImpl() {
        super("pc");
    }


    @Override
    public void doMessageSend(ClientMessageSendParamDto dto) {
        // 无需处理
    }
}
