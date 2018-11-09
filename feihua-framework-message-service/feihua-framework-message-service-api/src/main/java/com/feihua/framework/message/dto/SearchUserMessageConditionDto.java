package com.feihua.framework.message.dto;

import feihua.jdbc.api.pojo.BaseConditionDto;

/**
 *
 *
 *
 * @mbg.generated 2018-10-29 19:58:54
*/
public class SearchUserMessageConditionDto extends BaseConditionDto {

    private String title;

    private String msgType;

    private String isRead;

    private String msgLevel;
    private String userId;

    BaseMessageTargetClientParamsDto targetClientParamsDto;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public BaseMessageTargetClientParamsDto getTargetClientParamsDto() {
        return targetClientParamsDto;
    }

    public void setTargetClientParamsDto(BaseMessageTargetClientParamsDto targetClientParamsDto) {
        this.targetClientParamsDto = targetClientParamsDto;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}