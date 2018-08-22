package com.feihua.framework.rest.modules.user.dto;

import com.feihua.framework.rest.modules.common.dto.UpdateFormDto;

/**
 * 用户手机号修改用，表单dto
 * Created by yangwei
 * Created at 2017/12/26 15:51
 */
public class UpdateUserMobileFormDto extends UpdateFormDto {

    private String mobile;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
