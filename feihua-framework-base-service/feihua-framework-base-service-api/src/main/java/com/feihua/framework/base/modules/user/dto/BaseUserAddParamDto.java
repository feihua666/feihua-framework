package com.feihua.framework.base.modules.user.dto;

import feihua.jdbc.api.pojo.BaseAddParamDto;

/**
 * Created by yangwei
 * Created at 2018/4/19 16:00
 */
public class BaseUserAddParamDto extends BaseAddParamDto {

    // 帐号信息
    private String identifier;
    private String locked;
    // 密码信息
    private String password;
    // 基本信息
    private String serialNo;
    private String nickname;
    private String gender;
    private String dataOfficeId;
    // 头像
    private String photo;

    private String identityType;

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getLocked() {
        return locked;
    }

    public void setLocked(String locked) {
        this.locked = locked;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDataOfficeId() {
        return dataOfficeId;
    }

    public void setDataOfficeId(String dataOfficeId) {
        this.dataOfficeId = dataOfficeId;
    }

    public String getIdentityType() {
        return identityType;
    }

    public void setIdentityType(String identityType) {
        this.identityType = identityType;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
