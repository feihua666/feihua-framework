package com.feihua.framework.base.modules.rel.dto;

import feihua.jdbc.api.pojo.BaseAddParamDto;

import java.util.List;

/**
 * 数据范围绑定角色参数
 * Created by yangwei
 * Created at 2018/1/31 19:31
 */
public class DataScopeBindRolesParamDto extends BaseAddParamDto {

    private String dataScopeId;
    private List<String> roleIds;

    public String getDataScopeId() {
        return dataScopeId;
    }

    public void setDataScopeId(String dataScopeId) {
        this.dataScopeId = dataScopeId;
    }

    public List<String> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(List<String> roleIds) {
        this.roleIds = roleIds;
    }
}
