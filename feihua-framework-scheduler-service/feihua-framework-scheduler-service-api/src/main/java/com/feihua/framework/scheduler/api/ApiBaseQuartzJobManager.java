package com.feihua.framework.scheduler.api;

import org.quartz.*;

/**
 * Created by yangwei
 * Created at 2018/12/18 16:40
 */
public interface ApiBaseQuartzJobManager{


    /**
     * 新建一个新job，group名默认
     * @param jobName
     * @param cls
     * @return
     */
    JobDetail newJob(String jobName, Class cls);

    /**
     * 新建一个新job
     * @param jobName
     * @param jobGroupName
     * @param cls
     * @return
     */
    JobDetail newJob( String jobName,String jobGroupName, Class cls);

    /**
     * 新建一个触发器，group名默认
     * @param triggerName
     * @param cron
     * @param startNow
     * @return
     */
    Trigger newCronTrigger(String triggerName, String cron, boolean startNow);

    /**
     * 新建一个触发器
     * @param triggerName
     * @param triggerGroupName
     * @param cron
     * @param startNow
     * @return
     */
    Trigger newCronTrigger(String triggerName, String triggerGroupName, String cron, boolean startNow);

    /**
     * 新建一个触发器，group名默认
     * @param triggerName
     * @param scheduleBuilder
     * @param startNow
     * @return
     */
    Trigger newTrigger(String triggerName, ScheduleBuilder scheduleBuilder, boolean startNow);

    /**
     * 新建一个触发器
     * @param triggerName
     * @param triggerGroupName
     * @param scheduleBuilder
     * @param startNow
     * @return
     */
    Trigger newTrigger(String triggerName, String triggerGroupName, ScheduleBuilder scheduleBuilder, boolean startNow);

    /**
     * 开启任务
     * @param job
     * @param trigger
     */
    void schedulerStart(JobDetail job,Trigger trigger) throws SchedulerException;

    /**
     * 修改任务时间，group名称默认
     * @param triggerName
     * @param cron
     * @throws SchedulerException
     */
    void modifyJobTime(String triggerName, String cron) throws SchedulerException;

    /**
     * 修改任务时间
     * @param triggerName
     * @param triggerGroupName
     * @param cron
     * @throws SchedulerException
     */
    void modifyJobTime(String triggerName,String triggerGroupName, String cron) throws SchedulerException;

    /**
     * 删除一个任务job，group名默认
     * @param jobName
     * @param triggerName
     * @throws SchedulerException
     */
    void removeJob(String jobName,
              String triggerName) throws SchedulerException;

    /**
     * 删除一个任务job
     * @param jobName
     * @param jobGroupName
     * @param triggerName
     * @param triggerGroupName
     * @throws SchedulerException
     */
    void removeJob(String jobName,String jobGroupName,
              String triggerName,String triggerGroupName) throws SchedulerException;

    /**
     * 关闭任务管理，所有任务停止
     * @param scheduler
     * @throws SchedulerException
     */
    void shutdownJobs(Scheduler scheduler) throws SchedulerException;

    /**
     * 关闭任务管理，所有任务招生完成后停止
     * @param scheduler
     * @throws SchedulerException
     */
    void shutdownJobsSafety(Scheduler scheduler) throws SchedulerException;
}
