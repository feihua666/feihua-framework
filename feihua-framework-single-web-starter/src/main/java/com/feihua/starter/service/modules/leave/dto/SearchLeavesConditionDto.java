package com.feihua.starter.service.modules.leave.dto;

import feihua.jdbc.api.pojo.BaseConditionDto;

/**
 * Created by yangwei
 * Created at 2018/4/11 16:05
 */
public class SearchLeavesConditionDto extends BaseConditionDto {
    private String leaveType;

    public String getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(String leaveType) {
        this.leaveType = leaveType;
    }
}
