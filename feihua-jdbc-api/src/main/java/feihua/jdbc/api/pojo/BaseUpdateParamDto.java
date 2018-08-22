package feihua.jdbc.api.pojo;

import java.io.Serializable;
import java.util.Date;

/**
 * 更新数据的基类，可以传用户id
 * Created by yangwei
 * Created at 2017/12/21 13:46
 */
public class BaseUpdateParamDto implements Serializable{
    /**
     * 用户id,操作人的用户id
     */
    private String currentUserId;

    public String getCurrentUserId() {
        return currentUserId;
    }

    public void setCurrentUserId(String currentUserId) {
        this.currentUserId = currentUserId;
    }

    private Date updateTime;

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
