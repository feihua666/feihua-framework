package com.feihua.framework.message.dto;

import com.feihua.framework.base.modules.loginclient.dto.BaseLoginClientDto;
import com.feihua.framework.base.modules.user.po.BaseUserPo;
import com.feihua.framework.message.po.BaseMessagePo;
import feihua.jdbc.api.pojo.BaseConditionDto;

import java.util.List;

/**
 * Created by yangwei
 * Created at 2019/4/30 10:04
 */
public class VClientMessageSendParamDto extends BaseConditionDto {

    /**
     * 发送的消息实体
     */
    private BaseMessagePo baseMessagePo;

    /**
     * 目标用户
     */
    private List<String> userIdentifiers;
    /**
     * 发送的目标客户端
     */
    private BaseLoginClientDto clientDto;

    public BaseMessagePo getBaseMessagePo() {
        return baseMessagePo;
    }

    public void setBaseMessagePo(BaseMessagePo baseMessagePo) {
        this.baseMessagePo = baseMessagePo;
    }

    public List<String> getUserIdentifiers() {
        return userIdentifiers;
    }

    public void setUserIdentifiers(List<String> userIdentifiers) {
        this.userIdentifiers = userIdentifiers;
    }

    public BaseLoginClientDto getClientDto() {
        return clientDto;
    }

    public void setClientDto(BaseLoginClientDto clientDto) {
        this.clientDto = clientDto;
    }
}
