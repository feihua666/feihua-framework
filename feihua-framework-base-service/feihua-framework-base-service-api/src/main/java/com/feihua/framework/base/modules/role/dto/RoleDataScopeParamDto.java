package com.feihua.framework.base.modules.role.dto;

import feihua.jdbc.api.pojo.BaseUpdateParamDto;

import java.util.List;

/**
 * Created by yangwei
 * Created at 2018/3/19 14:41
 */
public class RoleDataScopeParamDto extends BaseUpdateParamDto {

    /**
     * 数据范围id
     */
    private String dataScopeId;
    /**
     * 机构ids，自定义选择时用
     */
    private List<String> roleIds;
    /**
     * 类型
     */
    private String type;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
