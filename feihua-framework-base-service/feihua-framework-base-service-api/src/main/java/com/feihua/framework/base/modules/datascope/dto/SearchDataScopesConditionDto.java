package com.feihua.framework.base.modules.datascope.dto;

import feihua.jdbc.api.pojo.BaseConditionDto;

/**
 * Created by yangwei
 * Created at 2018/1/9 14:14
 */
public class SearchDataScopesConditionDto extends BaseConditionDto {
    private String name;
    private String type;

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
}
