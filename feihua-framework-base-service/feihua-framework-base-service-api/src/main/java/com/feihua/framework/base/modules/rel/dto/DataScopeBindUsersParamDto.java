package com.feihua.framework.base.modules.rel.dto;

import feihua.jdbc.api.pojo.BaseAddParamDto;

import java.util.List;

/**
 * 数据范围绑定用户参数
 * Created by yangwei
 * Created at 2018/1/31 19:31
 */
public class DataScopeBindUsersParamDto extends BaseAddParamDto {

    private String dataScopeId;
    private List<String> userIds;

    public String getDataScopeId() {
        return dataScopeId;
    }

    public void setDataScopeId(String dataScopeId) {
        this.dataScopeId = dataScopeId;
    }

    public List<String> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<String> userIds) {
        this.userIds = userIds;
    }
}
