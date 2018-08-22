package com.feihua.framework.base.modules.rel.dto;

import feihua.jdbc.api.pojo.BaseAddParamDto;

import java.util.List;

/**
 * 用户绑定数据范围参数
 * Created by yangwei
 * Created at 2018/1/31 19:31
 */
public class UserBindDataScopesParamDto extends BaseAddParamDto {

    private String userId;
    private List<String> dataScopeIds;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<String> getDataScopeIds() {
        return dataScopeIds;
    }

    public void setDataScopeIds(List<String> dataScopeIds) {
        this.dataScopeIds = dataScopeIds;
    }
}
