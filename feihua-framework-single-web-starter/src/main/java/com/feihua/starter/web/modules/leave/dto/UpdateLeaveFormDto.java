package com.feihua.starter.web.modules.leave.dto;

import com.feihua.framework.rest.modules.common.dto.UpdateFormDto;

import java.util.Date;

/**
 * 请假更新单
 * Created by yangwei
 * Created at 2018/4/11 15:08
 */
public class UpdateLeaveFormDto extends UpdateFormDto {


    private Date startTime;

    private Date endTime;

    private String leaveType;

    private String reason;

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(String leaveType) {
        this.leaveType = leaveType;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
