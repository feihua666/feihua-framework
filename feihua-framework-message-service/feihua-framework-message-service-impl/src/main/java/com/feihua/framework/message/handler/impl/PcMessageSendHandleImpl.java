package com.feihua.framework.message.handler.impl;

import com.feihua.framework.base.modules.user.po.BaseUserPo;
import com.feihua.framework.message.api.ApiBaseMessageTargetClientPoService;
import com.feihua.framework.message.dto.BaseMessageSendParamsDto;
import com.feihua.framework.message.dto.BaseMessageTargetClientParamsDto;
import com.feihua.framework.message.po.BaseMessagePo;
import com.feihua.framework.message.po.BaseMessageTargetClientPo;
import com.feihua.framework.constants.DictEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by yangwei
 * Created at 2018/11/1 18:24
 */
public class PcMessageSendHandleImpl extends ApiMessageSendHandlerAbstractImpl {

    @Autowired
    private ApiBaseMessageTargetClientPoService apiBaseMessageTargetClientPoService;

    public PcMessageSendHandleImpl() {
        super(DictEnum.LoginClient.pc);
    }


    @Override
    public void doMessageSend(BaseMessageTargetClientParamsDto dto, BaseMessagePo baseMessagePo, BaseUserPo userPo) {
        //暂时不需要处理
    }

}
