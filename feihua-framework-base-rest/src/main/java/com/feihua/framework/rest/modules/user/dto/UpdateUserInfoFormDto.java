package com.feihua.framework.rest.modules.user.dto;

import com.feihua.framework.rest.modules.common.dto.UpdateFormDto;

/**
 * 用户信息修改用，表单dto
 * Created by yangwei
 * Created at 2017/12/26 15:51
 */
public class UpdateUserInfoFormDto extends UpdateFormDto {

    private String serialNo;
    private String nickname;
    private String gender;

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
}
