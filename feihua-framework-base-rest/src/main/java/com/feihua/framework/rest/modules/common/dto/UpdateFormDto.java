package com.feihua.framework.rest.modules.common.dto;

import java.util.Date;

/**
 * 数据更新用基类，更新时间做条件更新，乐观锁
 * Created by yangwei
 * Created at 2017/12/27 15:13
 */
public class UpdateFormDto {
    private Date updateTime;

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
