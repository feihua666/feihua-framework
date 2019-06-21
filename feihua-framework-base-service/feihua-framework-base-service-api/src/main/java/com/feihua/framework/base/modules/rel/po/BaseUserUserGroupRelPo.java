package com.feihua.framework.base.modules.rel.po;

import feihua.jdbc.api.pojo.BasePo;

/**
 * This class was generated by MyBatis Generator.
 * @author yangwei 2019-06-20 13:37:34
 * Database Table Remarks:
 *   用户-用户分组
 *
 * This class corresponds to the database table base_user_user_group_rel
 * @mbg.generated do_not_delete_during_merge 2019-06-20 13:37:34
*/
public class BaseUserUserGroupRelPo extends feihua.jdbc.api.pojo.BasePo<String> {
    /**
     * Database Column Remarks:
     *   用户ID
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column base_user_user_group_rel.USER_ID
     *
     * @mbg.generated 2019-06-20 13:37:34
     */
    private String userId;

    /**
     * Database Column Remarks:
     *   用户分组ID
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column base_user_user_group_rel.USER_GROUP_ID
     *
     * @mbg.generated 2019-06-20 13:37:34
     */
    private String userGroupId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserGroupId() {
        return userGroupId;
    }

    public void setUserGroupId(String userGroupId) {
        this.userGroupId = userGroupId;
    }

    public com.feihua.framework.base.modules.rel.api.ApiBaseUserUserGroupRelPoService service() {
        return com.feihua.utils.spring.SpringContextHolder.getBean(com.feihua.framework.base.modules.rel.api.ApiBaseUserUserGroupRelPoService.class);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", userId=").append(userId);
        sb.append(", userGroupId=").append(userGroupId);
        sb.append("]");
        return sb.toString();
    }
}