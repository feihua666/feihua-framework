package com.feihua.framework.base.modules.dict.dto;

import feihua.jdbc.api.pojo.BaseConditionDto;

/**
 * Created by yangwei
 * Created at 2017/12/21 13:23
 */
public class SearchDictsConditionDto extends BaseConditionDto {

    /**
     * 字典值，
     */
    private String value;
    /**
     * 字典名
     */
    private String name;
    /**
     * 类型
     */
    private String type;
    /**
     * 是否系统
     */
    private String isSystem;
    /**
     * 父级id
     */
    private String parentId;

    private String isPublic;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

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

    public String getIsSystem() {
        return isSystem;
    }

    public void setIsSystem(String isSystem) {
        this.isSystem = isSystem;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(String isPublic) {
        this.isPublic = isPublic;
    }
}
