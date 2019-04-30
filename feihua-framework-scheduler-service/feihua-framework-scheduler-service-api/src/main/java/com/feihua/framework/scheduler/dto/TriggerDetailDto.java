package com.feihua.framework.scheduler.dto;

import feihua.jdbc.api.pojo.BaseDto;
import feihua.jdbc.api.pojo.BasePo;
import org.quartz.CronTrigger;
import org.quartz.Trigger;

import java.util.Date;

/**
 * Created by yangwei
 * Created at 2019/4/16 16:01
 */
public class TriggerDetailDto extends BaseDto {

    private String description;
    private String mayFireAgain;
    private Date startTime;
    private Date endTime;
    private Date nextFireTime;
    private Date previousFireTime;
    private Date finalFireTime;
    private int misfireInstruction;
    private String cronExpression;

    public TriggerDetailDto(Trigger trigger) {
        this.description = trigger.getDescription();
        this.mayFireAgain = trigger.mayFireAgain()? BasePo.YesNo.Y.name() : BasePo.YesNo.N.name();
        this.startTime = trigger.getStartTime();
        this.endTime = trigger.getEndTime();
        this.nextFireTime = trigger.getNextFireTime();
        this.previousFireTime = trigger.getPreviousFireTime();
        this.finalFireTime = trigger.getFinalFireTime();
        this.misfireInstruction = trigger.getMisfireInstruction();
        if(trigger instanceof CronTrigger){
            this.cronExpression = ((CronTrigger) trigger).getCronExpression();
        }
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMayFireAgain() {
        return mayFireAgain;
    }

    public void setMayFireAgain(String mayFireAgain) {
        this.mayFireAgain = mayFireAgain;
    }

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

    public Date getNextFireTime() {
        return nextFireTime;
    }

    public void setNextFireTime(Date nextFireTime) {
        this.nextFireTime = nextFireTime;
    }

    public Date getPreviousFireTime() {
        return previousFireTime;
    }

    public void setPreviousFireTime(Date previousFireTime) {
        this.previousFireTime = previousFireTime;
    }

    public Date getFinalFireTime() {
        return finalFireTime;
    }

    public void setFinalFireTime(Date finalFireTime) {
        this.finalFireTime = finalFireTime;
    }

    public int getMisfireInstruction() {
        return misfireInstruction;
    }

    public void setMisfireInstruction(int misfireInstruction) {
        this.misfireInstruction = misfireInstruction;
    }

    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }
}
