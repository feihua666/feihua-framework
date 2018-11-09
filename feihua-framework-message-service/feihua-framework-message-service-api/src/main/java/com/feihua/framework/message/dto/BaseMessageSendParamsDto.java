package com.feihua.framework.message.dto;

import feihua.jdbc.api.pojo.BaseConditionDto;

import java.util.List;

/**
 * Created by yangwei
 * Created at 2018/10/31 12:52
 */
public class BaseMessageSendParamsDto extends BaseConditionDto {

    /**
     * 如果要发送一条已经存在的消息只需要指定id
     */
    private String messageId;
    /**
     * messageId和以下其它属性选其一
     */
    private String title;
    private String profile;
    private String content;
    private String msgType;
    private String msgLevel;

    /**
     * 必填
     */
    private String targets;
    /**
     * 必填
     */
    private List<String> targetsValue;

    private List<BaseMessageTargetClientParamsDto> targetClients;

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

    public String getMsgLevel() {
        return msgLevel;
    }

    public void setMsgLevel(String msgLevel) {
        this.msgLevel = msgLevel;
    }

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
