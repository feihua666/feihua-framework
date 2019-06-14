package com.feihua.framework.base.modules.rel.dto;

import feihua.jdbc.api.pojo.BaseAddParamDto;

import java.util.List;

/**
 * 岗位绑定用户参数
 * Created by yangwei
 * Created at 2018/1/31 19:31
 */
public class PostBindUsersParamDto extends BaseAddParamDto {

    private String postId;
    private List<String> userIds;

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public List<String> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<String> userIds) {
        this.userIds = userIds;
    }
}
