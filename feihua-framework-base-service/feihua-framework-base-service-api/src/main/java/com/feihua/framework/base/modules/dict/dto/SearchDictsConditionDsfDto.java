package com.feihua.framework.base.modules.dict.dto;

/**
 * Created by yangwei
 * Created at 2018/3/21 9:58
 */
public class SearchDictsConditionDsfDto extends SearchDictsConditionDto {

    private String selfCondition;
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSelfCondition() {
        return selfCondition;
    }

    public void setSelfCondition(String selfCondition) {
        this.selfCondition = selfCondition;
    }
}
