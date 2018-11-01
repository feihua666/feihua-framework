package com.feihua.framework.base.modules.message.dto;

import feihua.jdbc.api.pojo.BaseConditionDto;

import java.util.List;

/**
 * Created by yangwei
 * Created at 2018/10/31 12:52
 */
public class BaseMessageSendParamsDto extends BaseConditionDto {

    private String messageId;
    private String targets;
    private String targetsValue;
    /**
     * 如果有子客户端以冒号分隔
     */
    private List<String> targetClient;

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getTargets() {
        return targets;
    }

    public void setTargets(String targets) {
        this.targets = targets;
    }

    public String getTargetsValue() {
        return targetsValue;
    }

    public void setTargetsValue(String targetsValue) {
        this.targetsValue = targetsValue;
    }

    public List<String> getTargetClient() {
        return targetClient;
    }

    public void setTargetClient(List<String> targetClient) {
        this.targetClient = targetClient;
    }
}
