package com.feihua.framework.message.dto;

import feihua.jdbc.api.pojo.BaseConditionDto;

import java.util.Map;

/**
 * Created by yangwei
 * Created at 2019/5/7 12:51
 */
public class CreateMessageParamsDto extends BaseConditionDto {



    private String messageId;

    private String title;

    private String profile;

    private String content;

    private String msgType;

    private String msgState;

    private String msgLevel;

    private String messageTemplateId;

    private Map<String,String> templateParams;

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public String getMsgState() {
        return msgState;
    }

    public void setMsgState(String msgState) {
        this.msgState = msgState;
    }

    public String getMsgLevel() {
        return msgLevel;
    }

    public void setMsgLevel(String msgLevel) {
        this.msgLevel = msgLevel;
    }

    public String getMessageTemplateId() {
        return messageTemplateId;
    }

    public void setMessageTemplateId(String messageTemplateId) {
        this.messageTemplateId = messageTemplateId;
    }

    public Map<String, String> getTemplateParams() {
        return templateParams;
    }

    public void setTemplateParams(Map<String, String> templateParams) {
        this.templateParams = templateParams;
    }
}
