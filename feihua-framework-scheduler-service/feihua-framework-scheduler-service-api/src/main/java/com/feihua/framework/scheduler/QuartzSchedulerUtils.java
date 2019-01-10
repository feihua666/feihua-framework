package com.feihua.framework.scheduler;

import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by yangwei
 * Created at 2018/12/18 16:51
 */
public class QuartzSchedulerUtils {

    private static Logger logger = LoggerFactory.getLogger(QuartzSchedulerUtils.class);

    private static String DEFAULT_JOB_GROUP_NAME = "default_job_group_name";
    private static String DEFAULT_TRIGGER_GROUP_NAME = "default_trigger_group_name";

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
    public static Trigger newCronTrigger(String triggerName, String triggerGroupName, String cron, boolean startNow) {

        return newTrigger(triggerName,triggerGroupName,CronScheduleBuilder.cronSchedule(cron),startNow);
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
    public static void modifyJobTime(Scheduler scheduler,String triggerName, String cron) throws SchedulerException {
        modifyJobTime(scheduler,triggerName,DEFAULT_TRIGGER_GROUP_NAME,cron);
    }
    public static void modifyJobTime(Scheduler scheduler,String triggerName,String triggerGroupName, String cron) throws SchedulerException {
        TriggerKey triggerKey = TriggerKey.triggerKey(triggerName, triggerGroupName);
        CronTrigger trigger = null;
        trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
        if (trigger == null) {
            return;
        }
        String oldTime = trigger.getCronExpression();
        if (!oldTime.equalsIgnoreCase(cron)) {
            /** 方式一 ：调用 rescheduleJob 开始 */
            // 触发器
            TriggerBuilder<Trigger> triggerBuilder = TriggerBuilder.newTrigger();
            // 触发器名,触发器组
            triggerBuilder.withIdentity(triggerName, triggerGroupName);
            triggerBuilder.startNow();
            // 触发器时间设定
            triggerBuilder.withSchedule(CronScheduleBuilder.cronSchedule(cron));
            // 创建Trigger对象
            trigger = (CronTrigger) triggerBuilder.build();
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
        TriggerKey triggerKey = TriggerKey.triggerKey(triggerName, jobGroupName);
        // 停止触发器
        scheduler.pauseTrigger(triggerKey);
        // 移除触发器
        scheduler.unscheduleJob(triggerKey);
        // 删除任务
        scheduler.deleteJob(JobKey.jobKey(jobName, triggerGroupName));

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
}
