package com.feihua.framework.base.modules.datascope.dto;

import feihua.jdbc.api.pojo.BaseUpdateParamDto;

import java.util.List;

/**
 * Created by yangwei
 * Created at 2018/3/19 14:41
 */
public class DataScopeDataScopeParamDto extends BaseUpdateParamDto {

    /**
     * 数据范围id
     */
    private String dataScopeId;
    /**
     * 数据范围ids，自定义选择时用
     */
    private List<String> dataScopeIds;
    /**
     * 类型
     */
    private String type;

    public String getDataScopeId() {
        return dataScopeId;
    }

    public void setDataScopeId(String dataScopeId) {
        this.dataScopeId = dataScopeId;
    }

    public List<String> getDataScopeIds() {
        return dataScopeIds;
    }

    public void setDataScopeIds(List<String> dataScopeIds) {
        this.dataScopeIds = dataScopeIds;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
