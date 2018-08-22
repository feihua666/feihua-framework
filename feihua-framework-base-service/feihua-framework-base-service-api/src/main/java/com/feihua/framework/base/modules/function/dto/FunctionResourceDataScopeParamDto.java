package com.feihua.framework.base.modules.function.dto;

import feihua.jdbc.api.pojo.BaseUpdateParamDto;

import java.util.List;

/**
 * Created by yangwei
 * Created at 2018/3/19 14:41
 */
public class FunctionResourceDataScopeParamDto extends BaseUpdateParamDto {

    /**
     * 角色id
     */
    private String roleId;
    /**
     * 功能ids，自定义选择时用
     */
    private List<String> functionResourceIds;
    /**
     * 类型
     */
    private String type;

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public List<String> getFunctionResourceIds() {
        return functionResourceIds;
    }

    public void setFunctionResourceIds(List<String> functionResourceIds) {
        this.functionResourceIds = functionResourceIds;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
