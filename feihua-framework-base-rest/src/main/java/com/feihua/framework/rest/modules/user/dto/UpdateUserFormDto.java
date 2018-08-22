package com.feihua.framework.rest.modules.user.dto;

import com.feihua.framework.rest.modules.common.dto.UpdateFormDto;

import java.util.Date;

/**
 * 更新用户用表单dto
 * Created by yangwei
 * Created at 2017/12/26 15:51
 */
public class UpdateUserFormDto extends UpdateFormDto {

    private String locked;

    // 基本信息
    private String serialNo;
    private String nickname;
    private String gender;
    private String dataOfficeId;

    public String getLocked() {
        return locked;
    }

    public void setLocked(String locked) {
        this.locked = locked;
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

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public String getDataOfficeId() {
        return dataOfficeId;
    }

    public void setDataOfficeId(String dataOfficeId) {
        this.dataOfficeId = dataOfficeId;
    }
}
