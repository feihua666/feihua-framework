package com.feihua.starter.service.modules.leave.dto;


import com.feihua.framework.activity.dto.ApplyParamDto;

/**
 * Created by yangwei
 * Created at 2018/4/11 16:46
 */
public class LeaveApplyParamDto extends ApplyParamDto {
    private String leaveId;

    public String getLeaveId() {
        return leaveId;
    }

    public void setLeaveId(String leaveId) {
        this.leaveId = leaveId;
    }
}
