package com.feihua.wechat.publicplatform.dto;

import feihua.jdbc.api.pojo.BaseConditionDto;

import java.util.Date;

/**
 * This class was generated by MyBatis Generator.
 * This class corresponds to the database table weixin_menu
 *
 * @mbg.generated 2018-08-16 18:52:20
*/
public class SearchWeixinMenusConditionDto extends BaseConditionDto {

    private String name;

    private String type;
    private String parentId;

    private String which;

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

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getWhich() {
        return which;
    }

    public void setWhich(String which) {
        this.which = which;
    }
}