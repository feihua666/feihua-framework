package com.feihua.framework.message.dto;

import com.feihua.framework.message.api.ApiBaseMessagePoService;
import feihua.jdbc.api.pojo.BaseDto;

import java.util.Date;

/**
 * 指定用户消息实体
 *
*/
public class UserMessageDto extends BaseDto {

    String id;

    private String title;

    private String profile;

    private String content;

    private String msgType;

    private String msgLevel;

    private String isRead;

    private Date readTime;

    private String readClientId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getIsRead() {
        return isRead;
    }

    public void setIsRead(String isRead) {
        this.isRead = isRead;
    }

    public Date getReadTime() {
        return readTime;
    }

    public void setReadTime(Date readTime) {
        this.readTime = readTime;
    }

    public String getReadClientId() {
        return readClientId;
    }

    public void setReadClientId(String readClientId) {
        this.readClientId = readClientId;
    }
}