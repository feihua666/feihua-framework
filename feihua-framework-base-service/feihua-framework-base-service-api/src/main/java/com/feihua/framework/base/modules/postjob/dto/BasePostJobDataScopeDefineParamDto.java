package com.feihua.framework.base.modules.postjob.dto;

import feihua.jdbc.api.pojo.BaseUpdateParamDto;

/**
 * Created by yangwei
 * Created at 2018/3/26 12:53
 */
public class BasePostJobDataScopeDefineParamDto extends BaseUpdateParamDto {

    private String dataScopeId;
    private String type;

    public String getDataScopeId() {
        return dataScopeId;
    }

    public void setDataScopeId(String dataScopeId) {
        this.dataScopeId = dataScopeId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
