package com.feihua.framework.rest.modules.role.dto;

import com.feihua.framework.rest.modules.common.dto.UpdateFormDto;

/**
 * 角色更新用表单dto
 * Created by yangwei
 * Created at 2017/12/21 17:39
 */
public class UpdateRoleFormDto extends UpdateFormDto {
    private String name;
    private String code;
    private String type;
    private String disabled;
    private String parentId;
    private String dataOfficeId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDisabled() {
        return disabled;
    }

    public void setDisabled(String disabled) {
        this.disabled = disabled;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getDataOfficeId() {
        return dataOfficeId;
    }

    public void setDataOfficeId(String dataOfficeId) {
        this.dataOfficeId = dataOfficeId;
    }
}
