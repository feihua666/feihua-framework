package com.feihua.framework.base.modules.rel.dto;

import feihua.jdbc.api.pojo.BaseAddParamDto;

import java.util.List;

/**
 * 用户绑定角色参数
 * Created by yangwei
 * Created at 2018/1/31 19:31
 */
public class UserBindRolesParamDto extends BaseAddParamDto {

    /**
     * 因为父级冲突，变量有改动成现在这个样子，用户id
     */
    private String userId;
    private List<String> roleIds;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<String> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(List<String> roleIds) {
        this.roleIds = roleIds;
    }
}
