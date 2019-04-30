package com.feihua.framework.scheduler;

import com.feihua.exception.DataNotFoundException;
import com.feihua.framework.constants.DictEnum;
import com.feihua.utils.string.StringUtils;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

/**
 * Created by yangwei
 * Created at 2018/12/18 16:51
 */
public class QuartzSchedulerUtils {

    private static Logger logger = LoggerFactory.getLogger(QuartzSchedulerUtils.class);

    public static String DEFAULT_JOB_GROUP_NAME = "default_job_group_name";
    public static String DEFAULT_TRIGGER_GROUP_NAME = "default_trigger_group_name";

    /**
     * 创建一个新job
     * @param jobName
     * @param cls
     * @return
     */
    public static JobDetail newJob( String jobName, Class cls) {
        return newJob(jobName,DEFAULT_JOB_GROUP_NAME,cls);
    }
    public static JobDetail newJob( String jobName,String jobGroupName, Class cls) {
        JobDetail job = JobBuilder.newJob(cls)
                .withIdentity(jobName, jobGroupName)
                .build();
        return job;
    }
    public static JobDetail newJob( String jobName, Class cls,JobDataMap jobDataMap) {
        return newJob(jobName,DEFAULT_JOB_GROUP_NAME,cls,jobDataMap);
    }
    public static JobDetail newJob( String jobName,String jobGroupName, Class cls,JobDataMap jobDataMap) {
        JobDetail job = newJob(jobName,jobGroupName,cls);
        job.getJobDataMap().putAll(jobDataMap);
        return job;
    }
    /**
     * 创建一个新触发器
     * @param triggerName
     * @param cron
     * @param startNow
     * @return
     */
    public static Trigger newCronTrigger(String triggerName, String cron, boolean startNow) {

        return newCronTrigger(triggerName,DEFAULT_JOB_GROUP_NAME,cron,startNow);
    }
    public static CronTrigger newCronTrigger(String triggerName, String triggerGroupName, String cron, boolean startNow) {

        return (CronTrigger) newTrigger(triggerName,triggerGroupName,CronScheduleBuilder.cronSchedule(cron),startNow);
    }
    public static Trigger newTrigger(String triggerName, String triggerGroupName, ScheduleBuilder scheduleBuilder, boolean startNow) {
        TriggerBuilder triggerBuilder = TriggerBuilder.newTrigger()
                .withIdentity(triggerName, triggerGroupName);
        if (startNow) {
            triggerBuilder.startNow();
        }
        triggerBuilder.withSchedule(scheduleBuilder);
        Trigger trigger = triggerBuilder.build();
        return trigger;
    }
    /**
     * 启动所有job
     * @param scheduler
     * @param job
     * @param trigger
     * @throws SchedulerException
     */
    public static void schedulerStart(Scheduler scheduler,JobDetail job,Trigger trigger) throws SchedulerException {
            scheduler.scheduleJob(job, trigger);
            // 启动
            if (!scheduler.isShutdown()) {
                scheduler.start();
            }
    }

    /**
     * 修改job触发时间
     * @param scheduler
     * @param triggerName
     * @param cron
     * @throws SchedulerException
     */
    public static void modifyJobTime(Scheduler scheduler,String triggerName, String cron, boolean startNow) throws SchedulerException {
        modifyJobTime(scheduler,triggerName,DEFAULT_TRIGGER_GROUP_NAME,cron,startNow);
    }
    public static void modifyJobTime(Scheduler scheduler,String triggerName,String triggerGroupName, String cron, boolean startNow) throws SchedulerException {
        TriggerKey triggerKey = getTriggerKey(triggerName, triggerGroupName);
        CronTrigger trigger = null;
        trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
        if (trigger == null) {
            throw new DataNotFoundException(StringUtils.messageFormat("cronTrigger not fount with triggerName={0} triggerGroupName={1}",triggerName,triggerGroupName));
        }
        String oldTime = trigger.getCronExpression();
        if (!oldTime.equalsIgnoreCase(cron)) {
            trigger = (CronTrigger) newCronTrigger(triggerName,triggerGroupName,cron,startNow);
            // 修改一个任务的触发时间
            scheduler.rescheduleJob(triggerKey, trigger);
        }
    }
    /**
     * 删除一个job
     * @param scheduler
     * @param jobName
     * @param triggerName
     * @throws SchedulerException
     */
    public static void removeJob(Scheduler scheduler,String jobName,
                                 String triggerName) throws SchedulerException {

        removeJob(scheduler,jobName,DEFAULT_TRIGGER_GROUP_NAME,triggerName,DEFAULT_TRIGGER_GROUP_NAME);
    }
    public static void removeJob(Scheduler scheduler,String jobName,String jobGroupName,
                                 String triggerName,String triggerGroupName) throws SchedulerException {
        TriggerKey triggerKey = getTriggerKey(triggerName, triggerGroupName);
        // 停止触发器
        scheduler.pauseTrigger(triggerKey);
        // 移除触发器
        scheduler.unscheduleJob(triggerKey);
        // 删除任务
        scheduler.deleteJob(getJobKey(jobName, jobGroupName));

    }
    public static void pauseJob(Scheduler scheduler,JobKey jobKey) throws SchedulerException {
        scheduler.pauseJob(jobKey);
    }
    public static void pauseTrigger(Scheduler scheduler,TriggerKey triggerKey) throws SchedulerException {
        scheduler.pauseTrigger(triggerKey);
    }
    public static void resumeJob(Scheduler scheduler,JobKey jobKey) throws SchedulerException {
        scheduler.resumeJob(jobKey);
    }
    public static void resumeTrigger(Scheduler scheduler,TriggerKey triggerKey) throws SchedulerException {
        scheduler.resumeTrigger(triggerKey);
    }
    /**
     * 直接关闭
     * @param scheduler
     * @throws SchedulerException
     */
    public static void shutdownJobs(Scheduler scheduler) throws SchedulerException {
        if (!scheduler.isShutdown()) {
            scheduler.shutdown();
        }
    }

    /**
     * 安全关闭
     * @param scheduler
     * @throws SchedulerException
     */
    public static void shutdownJobsSafety(Scheduler scheduler) throws SchedulerException {
        int executingJobSize = scheduler.getCurrentlyExecutingJobs().size();
        logger.info("shutdownJobsSafety current job size is " + executingJobSize + ".jobs will be shutdown until completed");
        scheduler.shutdown(true);

        logger.info("shutdownJobsSafety jobs have bean shutdown.");
    }

    public static JobKey getJobKey(String jobName,String jobGroupName){
        return JobKey.jobKey(jobName, jobGroupName);
    }

    public static TriggerKey getTriggerKey(String triggerName,String triggerGroupName){
        return TriggerKey.triggerKey(triggerName, triggerGroupName);
    }
    public static String triggerStateToSchedulerStatus(Trigger.TriggerState triggerState){
        if (triggerState == null) {
            return null;
        }
        String r = null;
        switch (triggerState){
            case NONE:
                break;
            case ERROR:
                r = DictEnum.SchedulerStatus.error.name();
                break;
            case NORMAL:
                r = DictEnum.SchedulerStatus.normal.name();
                break;
            case PAUSED:
                r = DictEnum.SchedulerStatus.pause.name();
                break;
            case BLOCKED:
                r = DictEnum.SchedulerStatus.blocked.name();
                break;
            case COMPLETE:
                r = DictEnum.SchedulerStatus.complete.name();
                break;
        }
        return r;
    }

    public static String getBatchNo(JobExecutionContext context,boolean nullCreateNew){
        String batchNo = context.getJobDetail().getJobDataMap().getString("batchNo");
        if (nullCreateNew && org.apache.commons.lang3.StringUtils.isEmpty(batchNo) ) {
            batchNo = UUID.randomUUID().toString();
            context.getJobDetail().getJobDataMap().put("batchNo",batchNo);
        }
        return batchNo;
    }
}
