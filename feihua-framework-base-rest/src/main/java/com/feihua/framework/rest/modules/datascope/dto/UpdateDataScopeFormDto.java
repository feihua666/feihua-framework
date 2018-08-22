package com.feihua.framework.rest.modules.datascope.dto;

import com.feihua.framework.rest.modules.common.dto.UpdateFormDto;

/**
 * Created by yangwei
 * Created at 2018/1/9 14:13
 */
public class UpdateDataScopeFormDto extends UpdateFormDto {
    private String name;
    private String type;
    private String dataOfficeId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDataOfficeId() {
        return dataOfficeId;
    }

    public void setDataOfficeId(String dataOfficeId) {
        this.dataOfficeId = dataOfficeId;
    }
}
