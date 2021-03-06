package com.feihua.framework.base.modules.user.po;

import feihua.jdbc.api.pojo.BasePo;

/**
 * This class was generated by MyBatis Generator.
 * @author yangwei 2019-06-13 19:36:06
 * Database Table Remarks:
 *   用户角色岗位切换表
 *
 * This class corresponds to the database table base_user_role_post_switch
 * @mbg.generated do_not_delete_during_merge 2019-06-13 19:36:06
*/
public class BaseUserRolePostSwitchPo extends feihua.jdbc.api.pojo.BasePo<String> {
    /**
     * Database Column Remarks:
     *   用户id
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column base_user_role_post_switch.USER_ID
     *
     * @mbg.generated 2019-06-13 19:36:06
     */
    private String userId;

    /**
     * Database Column Remarks:
     *   角色id
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column base_user_role_post_switch.ROLE_ID
     *
     * @mbg.generated 2019-06-13 19:36:06
     */
    private String roleId;

    /**
     * Database Column Remarks:
     *   岗位id
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column base_user_role_post_switch.POST_ID
     *
     * @mbg.generated 2019-06-13 19:36:06
     */
    private String postId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public com.feihua.framework.base.modules.rel.api.ApiBaseUserRolePostSwitchPoService service() {
        return com.feihua.utils.spring.SpringContextHolder.getBean(com.feihua.framework.base.modules.rel.api.ApiBaseUserRolePostSwitchPoService.class);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", userId=").append(userId);
        sb.append(", roleId=").append(roleId);
        sb.append(", postId=").append(postId);
        sb.append("]");
        return sb.toString();
    }
}