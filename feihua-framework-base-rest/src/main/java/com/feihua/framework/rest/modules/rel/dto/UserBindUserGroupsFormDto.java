package com.feihua.framework.rest.modules.rel.dto;

import java.util.List;

/**
 * Created by yangwei
 * Created at 2018/2/1 18:48
 */
public class UserBindUserGroupsFormDto {

    List<String> userGroupIds;

    public List<String> getUserGroupIds() {
        return userGroupIds;
    }

    public void setUserGroupIds(List<String> userGroupIds) {
        this.userGroupIds = userGroupIds;
    }
}
