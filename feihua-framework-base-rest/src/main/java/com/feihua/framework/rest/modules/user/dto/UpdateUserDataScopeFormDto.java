package com.feihua.framework.rest.modules.user.dto;

import com.feihua.framework.rest.modules.common.dto.UpdateFormDto;

import java.util.Date;

/**
 * 更新用户数据范围用表单dto
 * Created by yangwei
 * Created at 2017/12/26 15:51
 */
public class UpdateUserDataScopeFormDto extends UpdateFormDto {
    private String id;
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
