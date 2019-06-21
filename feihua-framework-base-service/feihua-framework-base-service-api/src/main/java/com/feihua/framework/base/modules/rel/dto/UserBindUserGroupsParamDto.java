package com.feihua.framework.base.modules.rel.dto;

import feihua.jdbc.api.pojo.BaseAddParamDto;

import java.util.List;

/**
 * 用户绑定角色参数
 * Created by yangwei
 * Created at 2018/1/31 19:31
 */
public class UserBindUserGroupsParamDto extends BaseAddParamDto {

    private String userId;
    private List<String> userGroupIds;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<String> getUserGroupIds() {
        return userGroupIds;
    }

    public void setUserGroupIds(List<String> userGroupIds) {
        this.userGroupIds = userGroupIds;
    }
}
