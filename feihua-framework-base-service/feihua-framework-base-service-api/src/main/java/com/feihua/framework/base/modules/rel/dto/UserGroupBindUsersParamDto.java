package com.feihua.framework.base.modules.rel.dto;

import feihua.jdbc.api.pojo.BaseAddParamDto;

import java.util.List;

/**
 * 角色绑定用户参数
 * Created by yangwei
 * Created at 2018/1/31 19:31
 */
public class UserGroupBindUsersParamDto extends BaseAddParamDto {

    private String userGroupId;
    private List<String> userIds;

    public String getUserGroupId() {
        return userGroupId;
    }

    public void setUserGroupId(String userGroupId) {
        this.userGroupId = userGroupId;
    }

    public List<String> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<String> userIds) {
        this.userIds = userIds;
    }
}
