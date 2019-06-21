package com.feihua.framework.rest.modules.group.dto;

import com.feihua.framework.rest.modules.common.dto.UpdateFormDto;

import java.util.List;

/**
 * Created by yangwei
 * Created at 2018/3/19 16:11
 */
public class UserGroupDataScopeDefineFormDto extends UpdateFormDto {

    private String id;
    private String type;
    private List<String> userGroupIds;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getUserGroupIds() {
        return userGroupIds;
    }

    public void setUserGroupIds(List<String> userGroupIds) {
        this.userGroupIds = userGroupIds;
    }
}
