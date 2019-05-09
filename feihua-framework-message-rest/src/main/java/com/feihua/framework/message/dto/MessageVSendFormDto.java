package com.feihua.framework.message.dto;

/**
 * Created by yangwei
 * Created at 2019/5/7 16:28
 */
public class MessageVSendFormDto {
    private String clientId;
    private String vTargetType;
    private String vTargetValues;

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getvTargetType() {
        return vTargetType;
    }

    public void setvTargetType(String vTargetType) {
        this.vTargetType = vTargetType;
    }

    public String getvTargetValues() {
        return vTargetValues;
    }

    public void setvTargetValues(String vTargetValues) {
        this.vTargetValues = vTargetValues;
    }
}
