package com.feihua.framework.message.dto;

import com.feihua.framework.base.modules.loginclient.dto.BaseLoginClientDto;
import feihua.jdbc.api.pojo.BaseConditionDto;

import java.util.List;
import java.util.Map;

/**
 * Created by yangwei
 * Created at 2018/10/31 12:52
 */
public class BaseMessageSendParamsDto extends BaseConditionDto {

    /**
     * 如果要发送一条已经存在的消息只需要指定id
     */
    private String messageId;

    private List<BaseMessageSendVClientParamDto> vClientParamDtos;
    private BaseMessageSendClientParamDto clientParamDto;


    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public List<BaseMessageSendVClientParamDto> getvClientParamDtos() {
        return vClientParamDtos;
    }

    public void setvClientParamDtos(List<BaseMessageSendVClientParamDto> vClientParamDtos) {
        this.vClientParamDtos = vClientParamDtos;
    }

    public BaseMessageSendClientParamDto getClientParamDto() {
        return clientParamDto;
    }

    public void setClientParamDto(BaseMessageSendClientParamDto clientParamDto) {
        this.clientParamDto = clientParamDto;
    }
}
