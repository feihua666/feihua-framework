package com.feihua.framework.rest.modules.rel.dto;

import java.util.List;

/**
 * Created by yangwei
 * Created at 2018/2/1 18:48
 */
public class RoleBindDataScopesFormDto {

    List<String> dataScopeIds;

    public List<String> getDataScopeIds() {
        return dataScopeIds;
    }

    public void setDataScopeIds(List<String> dataScopeIds) {
        this.dataScopeIds = dataScopeIds;
    }
}
