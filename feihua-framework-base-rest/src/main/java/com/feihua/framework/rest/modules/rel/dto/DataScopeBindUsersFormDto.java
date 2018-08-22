package com.feihua.framework.rest.modules.rel.dto;

import java.util.List;

/**
 * Created by yangwei
 * Created at 2018/2/1 18:48
 */
public class DataScopeBindUsersFormDto {

    List<String> userIds;

    public List<String> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<String> userIds) {
        this.userIds = userIds;
    }
}
