package com.feihua.framework.base.modules.group.dto;

import com.feihua.framework.base.modules.postjob.dto.SearchBasePostsConditionDto;

/**
 * Created by yangwei
 * Created at 2018/3/21 9:58
 */
public class SearchUserGroupsConditionDsfDto extends SearchBaseUserGroupsConditionDto {

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
