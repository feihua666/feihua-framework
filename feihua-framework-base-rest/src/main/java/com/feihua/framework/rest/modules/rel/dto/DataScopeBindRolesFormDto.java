package com.feihua.framework.rest.modules.rel.dto;

import java.util.List;

/**
 * Created by yangwei
 * Created at 2018/2/1 18:48
 */
public class DataScopeBindRolesFormDto {

    List<String> roleIds;

    public List<String> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(List<String> roleIds) {
        this.roleIds = roleIds;
    }
}
