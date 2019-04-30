package com.feihua.framework.scheduler.rest.vo;

import com.feihua.framework.scheduler.dto.SchedulerDto;
import com.feihua.framework.scheduler.dto.TriggerDetailDto;

/**
 * Created by yangwei
 * Created at 2019/4/16 11:04
 */
public class SchedulerVo {
    private SchedulerDto schedulerDto;
    /**
     * 字典，任务计划状态，脱机，任务计划中，暂停,已完成，阻塞
     */
    private String schedulerStatus;
    private TriggerDetailDto triggerDetailDto;

    public SchedulerVo(SchedulerDto dto) {
        this.schedulerDto = dto;
    }

    public SchedulerDto getSchedulerDto() {
        return schedulerDto;
    }

    public void setSchedulerDto(SchedulerDto schedulerDto) {
        this.schedulerDto = schedulerDto;
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
}
