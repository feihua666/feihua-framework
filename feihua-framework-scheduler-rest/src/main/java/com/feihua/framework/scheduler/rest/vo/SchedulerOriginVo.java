package com.feihua.framework.scheduler.rest.vo;

import com.feihua.framework.scheduler.dto.SchedulerDto;
import com.feihua.framework.scheduler.dto.TriggerDetailDto;

/**
 * Created by yangwei
 * Created at 2019/4/16 11:04
 */
public class SchedulerOriginVo {

    private String schedulerName;
    private String schedulerId;

    private String jobName;

    private String jobClass;

    private String jobGroup;
    /**
     * 字典，任务计划状态，脱机，任务计划中，暂停,已完成，阻塞
     */
    private String schedulerStatus;
    private String triggerName;
    private String triggerGroup;
    private TriggerDetailDto triggerDetailDto;

    public String getSchedulerName() {
        return schedulerName;
    }

    public void setSchedulerName(String schedulerName) {
        this.schedulerName = schedulerName;
    }

    public String getSchedulerId() {
        return schedulerId;
    }

    public void setSchedulerId(String schedulerId) {
        this.schedulerId = schedulerId;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getJobClass() {
        return jobClass;
    }

    public void setJobClass(String jobClass) {
        this.jobClass = jobClass;
    }

    public String getJobGroup() {
        return jobGroup;
    }

    public void setJobGroup(String jobGroup) {
        this.jobGroup = jobGroup;
    }

    public String getSchedulerStatus() {
        return schedulerStatus;
    }

    public void setSchedulerStatus(String schedulerStatus) {
        this.schedulerStatus = schedulerStatus;
    }

    public TriggerDetailDto getTriggerDetailDto() {
        return triggerDetailDto;
    }

    public void setTriggerDetailDto(TriggerDetailDto triggerDetailDto) {
        this.triggerDetailDto = triggerDetailDto;
    }

    public String getTriggerName() {
        return triggerName;
    }

    public void setTriggerName(String triggerName) {
        this.triggerName = triggerName;
    }

    public String getTriggerGroup() {
        return triggerGroup;
    }

    public void setTriggerGroup(String triggerGroup) {
        this.triggerGroup = triggerGroup;
    }
}
