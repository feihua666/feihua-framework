package com.feihua.framework.rest.modules.datascope.dto;

import com.feihua.framework.rest.modules.common.dto.UpdateFormDto;

import java.util.List;

/**
 * Created by yangwei
 * Created at 2018/3/19 16:11
 */
public class DataScopeDataScopeDefineFormDto extends UpdateFormDto {

    private String id;
    private String type;
    private List<String> dataScopeIds;

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

    public List<String> getDataScopeIds() {
        return dataScopeIds;
    }

    public void setDataScopeIds(List<String> dataScopeIds) {
        this.dataScopeIds = dataScopeIds;
    }
}
