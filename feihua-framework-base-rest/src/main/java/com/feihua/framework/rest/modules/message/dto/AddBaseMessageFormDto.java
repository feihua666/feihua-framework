package com.feihua.framework.rest.modules.message.dto;

import java.util.Date;

/**
 * This class was generated by MyBatis Generator.
 * This class corresponds to the database table base_message
 *
 * @mbg.generated 2018-10-29 19:58:54
*/
public class AddBaseMessageFormDto {

    private String title;

    private String profile;

    private String content;

    private String targets;

    private String targetsValue;

    private Integer predictNum;

    private String msgType;

    private String msgState;

    private String msgLevel;

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

    public Integer getPredictNum() {
        return predictNum;
    }

    public void setPredictNum(Integer predictNum) {
        this.predictNum = predictNum;
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
}