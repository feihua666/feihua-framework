package com.feihua.framework.message.dto;

import java.util.List;

/**
 * Created by yangwei
 * Created at 2018/11/6 16:40
 */
public class MessageSendFormDto {

    private String targets;
    private List<String> targetsValue;
    private java.util.List<BaseMessageTargetClientParamsDto> targetClients;

    public String getTargets() {
        return targets;
    }

    public void setTargets(String targets) {
        this.targets = targets;
    }

    public List<String> getTargetsValue() {
        return targetsValue;
    }

    public void setTargetsValue(List<String> targetsValue) {
        this.targetsValue = targetsValue;
    }

    public List<BaseMessageTargetClientParamsDto> getTargetClients() {
        return targetClients;
    }

    public void setTargetClients(List<BaseMessageTargetClientParamsDto> targetClients) {
        this.targetClients = targetClients;
    }
}
