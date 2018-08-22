package com.feihua.framework.base.modules.function.dto;

import feihua.jdbc.api.pojo.BaseConditionDto;

/**
 * Created by yangwei
 * Created at 2017/11/18 15:52
 */
public class SearchFunctionResourcesConditionDto extends BaseConditionDto {
    private String name;
    private String isShow;
    private String type;
    private String parentId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIsShow() {
        return isShow;
    }

    public void setIsShow(String isShow) {
        this.isShow = isShow;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }
}
