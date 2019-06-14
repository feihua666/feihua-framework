package com.feihua.framework.base.modules.rel.dto;

import feihua.jdbc.api.pojo.BaseAddParamDto;

import java.util.List;

/**
 * 岗位绑定角色参数
 * Created by yangwei
 * Created at 2019/6/12 19:31
 */
public class PostBindRolesParamDto extends BaseAddParamDto {

    private String postId;
    private List<String> roleIds;

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public List<String> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(List<String> roleIds) {
        this.roleIds = roleIds;
    }
}
