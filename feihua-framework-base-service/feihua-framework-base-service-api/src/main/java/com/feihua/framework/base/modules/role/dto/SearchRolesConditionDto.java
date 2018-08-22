package com.feihua.framework.base.modules.role.dto;

import feihua.jdbc.api.pojo.BaseConditionDto;

/**
 * Created by yangwei
 * Created at 2017/11/18 15:52
 */
public class SearchRolesConditionDto extends BaseConditionDto {
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
