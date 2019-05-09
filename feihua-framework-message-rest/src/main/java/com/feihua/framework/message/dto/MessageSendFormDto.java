package com.feihua.framework.message.dto;

import java.util.List;
import java.util.Map;

/**
 * Created by yangwei
 * Created at 2018/11/6 16:40
 */
public class MessageSendFormDto {


    private String targetType;
    private List<String> targetValues;
    // 只接收非虚拟客户端的id
    private java.util.List<String> clientIds;
    private List<MessageVSendFormDto> vSendFormDtos;
    private String templateParams;

    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    public List<String> getTargetValues() {
        return targetValues;
    }

    public void setTargetValues(List<String> targetValues) {
        this.targetValues = targetValues;
    }

    public List<String> getClientIds() {
        return clientIds;
    }

    public void setClientIds(List<String> clientIds) {
        this.clientIds = clientIds;
    }

    public List<MessageVSendFormDto> getvSendFormDtos() {
        return vSendFormDtos;
    }

    public void setvSendFormDtos(List<MessageVSendFormDto> vSendFormDtos) {
        this.vSendFormDtos = vSendFormDtos;
    }

    public String getTemplateParams() {
        return templateParams;
    }

    public void setTemplateParams(String templateParams) {
        this.templateParams = templateParams;
    }
}
