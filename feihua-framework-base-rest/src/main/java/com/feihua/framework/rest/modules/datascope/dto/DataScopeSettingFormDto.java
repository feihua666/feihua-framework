package com.feihua.framework.rest.modules.datascope.dto;

import com.feihua.framework.rest.modules.common.dto.UpdateFormDto;

/**
 * Created by yangwei
 * Created at 2018/1/9 14:13
 */
public class DataScopeSettingFormDto extends UpdateFormDto {
    private String dataScopeItemId;

    public String getDataScopeItemId() {
        return dataScopeItemId;
    }

    public void setDataScopeItemId(String dataScopeItemId) {
        this.dataScopeItemId = dataScopeItemId;
    }
}
