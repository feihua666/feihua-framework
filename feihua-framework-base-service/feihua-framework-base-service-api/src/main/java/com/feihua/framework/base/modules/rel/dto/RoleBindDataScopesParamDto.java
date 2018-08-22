package com.feihua.framework.base.modules.rel.dto;

import feihua.jdbc.api.pojo.BaseAddParamDto;

import java.util.List;

/**
 * 角色绑定数据范围参数
 * Created by yangwei
 * Created at 2018/1/31 19:31
 */
public class RoleBindDataScopesParamDto extends BaseAddParamDto {

    private String roleId;
    private List<String> dataScopeIds;

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public List<String> getDataScopeIds() {
        return dataScopeIds;
    }

    public void setDataScopeIds(List<String> dataScopeIds) {
        this.dataScopeIds = dataScopeIds;
    }
}
