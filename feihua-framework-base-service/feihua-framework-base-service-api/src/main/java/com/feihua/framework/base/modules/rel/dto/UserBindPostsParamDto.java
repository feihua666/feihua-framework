package com.feihua.framework.base.modules.rel.dto;

import feihua.jdbc.api.pojo.BaseAddParamDto;

import java.util.List;

/**
 * 用户绑定岗位参数
 * Created by yangwei
 * Created at 2018/1/31 19:31
 */
public class UserBindPostsParamDto extends BaseAddParamDto {

    private String userId;
    private List<String> postIds;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<String> getPostIds() {
        return postIds;
    }

    public void setPostIds(List<String> postIds) {
        this.postIds = postIds;
    }
}
