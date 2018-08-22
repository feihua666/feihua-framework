package com.feihua.framework.base.modules.rel.dto;

import feihua.jdbc.api.pojo.BaseAddParamDto;

import java.util.List;

/**
 * 角色绑定用户参数
 * Created by yangwei
 * Created at 2018/1/31 19:31
 */
public class RoleBindUsersParamDto extends BaseAddParamDto {

    private String roleId;
    private List<String> userIds;

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public List<String> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<String> userIds) {
        this.userIds = userIds;
    }
}
