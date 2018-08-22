package com.feihua.framework.base.modules.user.dto;

/**
 * Created by yangwei
 * Created at 2018/3/21 12:32
 */
public class SearchUsersConditionDsfDto extends SearchBaseUsersConditionDto {
    private String selfCondition;

    public String getSelfCondition() {
        return selfCondition;
    }

    public void setSelfCondition(String selfCondition) {
        this.selfCondition = selfCondition;
    }
}
