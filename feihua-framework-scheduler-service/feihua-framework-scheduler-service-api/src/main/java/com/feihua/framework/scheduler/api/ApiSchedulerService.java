package com.feihua.framework.scheduler.api;

import com.feihua.exception.DataNotFoundException;
import com.feihua.framework.scheduler.dto.JobAndTriggerWrapperDto;
import com.feihua.framework.scheduler.dto.TriggerDetailDto;
import org.quartz.SchedulerException;

import java.util.List;

/**
 * Created by yangwei
 * Created at 2019/4/19 10:52
 */
public interface ApiSchedulerService {
    /**
     * 发布任务到计划任务中
     * @param schedulerId
     * @param startNow
     */
    public void publish(String schedulerId,boolean startNow) throws ClassNotFoundException, SchedulerException, DataNotFoundException;

    /**
     * 暂停任务
     * @param schedulerId
     * @throws ClassNotFoundException
     * @throws SchedulerException
     * @throws DataNotFoundException
     */
    public void pauseTrigger(String schedulerId) throws SchedulerException, DataNotFoundException;

    /**
     * 恢复暂停的任务
     * @param schedulerId
     * @throws ClassNotFoundException
     * @throws SchedulerException
     * @throws DataNotFoundException
     */
    public void resumeTrigger(String schedulerId) throws SchedulerException, DataNotFoundException;

    /**
     *
     * @param schedulerId
     * @throws SchedulerException
     * @throws DataNotFoundException
     */
    public void removeJob(String schedulerId) throws SchedulerException, DataNotFoundException;

    /**
     * 修改时间
     * @param schedulerId
     * @throws SchedulerException
     * @throws DataNotFoundException
     */
    public void modifyTriggerTime(String schedulerId,String cron,String cronDesc,boolean startNow,String currentUserId) throws SchedulerException, DataNotFoundException;

    /**
     * 获取触发详情
     * @param triggerName
     * @param triggerGroupName
     * @return
     * @throws SchedulerException
     */
    public TriggerDetailDto getTriggerDetail(String triggerName,String triggerGroupName) throws SchedulerException;

    /**
     * 获取任务状态
     * @param triggerName
     * @param triggerGroupName
     * @return
     */
    public String getSchedulerStatus(String triggerName,String triggerGroupName) throws SchedulerException;

    /**
     *
     * @return
     * @throws SchedulerException
     */
    List<JobAndTriggerWrapperDto> getAllJobs() throws SchedulerException;
}
