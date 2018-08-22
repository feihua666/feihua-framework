package com.feihua.framework.base.modules.role.dto;

/**
 * Created by yangwei
 * Created at 2018/3/21 16:42
 */
public class SearchRolesConditionDsfDto extends SearchRolesConditionDto {

    private String selfCondition;

    public String getSelfCondition() {
        return selfCondition;
    }

    public void setSelfCondition(String selfCondition) {
        this.selfCondition = selfCondition;
    }
}
