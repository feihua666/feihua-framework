package com.feihua.framework.message.dto;

import feihua.jdbc.api.pojo.BaseConditionDto;

import java.util.Map;

/**
 * Created by yangwei
 * Created at 2019/5/14 15:59
 */
public class MessageSendForUserParamsDto extends BaseConditionDto {
    private String templateCode;
    private String clientCode;
    private String userId;
    private Map<String, String> templateParams;

    public String getTemplateCode() {
        return templateCode;
    }

    public void setTemplateCode(String templateCode) {
        this.templateCode = templateCode;
    }

    public String getClientCode() {
        return clientCode;
    }

    public void setClientCode(String clientCode) {
        this.clientCode = clientCode;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Map<String, String> getTemplateParams() {
        return templateParams;
    }

    public void setTemplateParams(Map<String, String> templateParams) {
        this.templateParams = templateParams;
    }
}
