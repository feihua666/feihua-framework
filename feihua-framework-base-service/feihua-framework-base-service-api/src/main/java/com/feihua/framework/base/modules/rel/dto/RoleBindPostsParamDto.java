package com.feihua.framework.base.modules.rel.dto;

import feihua.jdbc.api.pojo.BaseAddParamDto;

import java.util.List;

/**
 * 角色绑定岗位参数
 * Created by yangwei
 * Created at 2018/1/31 19:31
 */
public class RoleBindPostsParamDto extends BaseAddParamDto {

    private String roleId;
    private List<String> postIds;

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public List<String> getPostIds() {
        return postIds;
    }

    public void setPostIds(List<String> postIds) {
        this.postIds = postIds;
    }
}
