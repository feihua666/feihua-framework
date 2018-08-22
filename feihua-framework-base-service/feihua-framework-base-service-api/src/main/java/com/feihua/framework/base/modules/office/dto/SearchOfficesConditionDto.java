package com.feihua.framework.base.modules.office.dto;

import feihua.jdbc.api.pojo.BaseConditionDto;

/**
 * Created by yangwei
 * Created at 2017/12/21 13:23
 */
public class SearchOfficesConditionDto extends BaseConditionDto {

    /**
     * 类型
     */
    private String type;
    /**
     * 名称
     */
    private String name;
    /**
     * 父级id
     */
    private String parentId;
    /**
     * 区域
     */
    private String areaId;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }
}
