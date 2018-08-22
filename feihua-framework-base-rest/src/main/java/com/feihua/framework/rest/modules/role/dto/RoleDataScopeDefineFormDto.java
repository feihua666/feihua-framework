package com.feihua.framework.rest.modules.role.dto;

import com.feihua.framework.rest.modules.common.dto.UpdateFormDto;

import java.util.List;

/**
 * Created by yangwei
 * Created at 2018/3/19 16:11
 */
public class RoleDataScopeDefineFormDto extends UpdateFormDto {

    private String id;
    private String type;
    private List<String> roleIds;

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

    public List<String> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(List<String> roleIds) {
        this.roleIds = roleIds;
    }
}
