package com.feihua.framework.rest.modules.office.dto;

import com.feihua.framework.rest.modules.common.dto.UpdateFormDto;

import java.util.List;

/**
 * Created by yangwei
 * Created at 2018/3/19 16:11
 */
public class OfficeDataScopeDefineFormDto extends UpdateFormDto {

    private String id;
    private String type;
    private List<String> officeIds;

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

    public List<String> getOfficeIds() {
        return officeIds;
    }

    public void setOfficeIds(List<String> officeIds) {
        this.officeIds = officeIds;
    }
}
