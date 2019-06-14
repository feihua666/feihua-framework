package feihua.jdbc.api.pojo;

import java.io.Serializable;

/**
 * 条件搜索的基类，可以传用户id
 * Created by yangwei
 * Created at 2017/12/21 13:46
 */
public class BaseConditionDto implements Serializable{
    /**
     * 用户id,操作人的用户id
     */
    private String currentUserId;

    /**
     * 当前使用的角色id
     */
    private String currentRoleId;
    /**
     * 当前使用的岗位id
     */
    private String currentPostId;

    public String getCurrentUserId() {
        return currentUserId;
    }

    public void setCurrentUserId(String currentUserId) {
        this.currentUserId = currentUserId;
    }

    public String getCurrentRoleId() {
        return currentRoleId;
    }

    public void setCurrentRoleId(String currentRoleId) {
        this.currentRoleId = currentRoleId;
    }

    public String getCurrentPostId() {
        return currentPostId;
    }

    public void setCurrentPostId(String currentPostId) {
        this.currentPostId = currentPostId;
    }
}
