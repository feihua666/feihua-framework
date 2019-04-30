package com.feihua.framework.scheduler.dto;

import feihua.jdbc.api.pojo.BaseDto;
import org.quartz.JobDetail;
import org.quartz.Trigger;

/**
 * Created by yangwei
 * Created at 2019/4/16 16:21
 */
public class JobAndTriggerWrapperDto extends BaseDto {
    private JobDetail jobDetail;
    private Trigger trigger;
    private Trigger.TriggerState triggerState;

    public JobDetail getJobDetail() {
        return jobDetail;
    }

    public void setJobDetail(JobDetail jobDetail) {
        this.jobDetail = jobDetail;
    }

    public Trigger getTrigger() {
        return trigger;
    }

    public void setTrigger(Trigger trigger) {
        this.trigger = trigger;
    }

    public Trigger.TriggerState getTriggerState() {
        return triggerState;
    }

    public void setTriggerState(Trigger.TriggerState triggerState) {
        this.triggerState = triggerState;
    }
}
