package com.feihua.framework.base.modules.postjob.dto;

import com.feihua.framework.base.modules.dict.dto.SearchDictsConditionDto;

/**
 * Created by yangwei
 * Created at 2018/3/21 9:58
 */
public class SearchPostJobsConditionDsfDto extends SearchBasePostJobsConditionDto {

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
