package com.feihua.framework.scheduler.impl;

import com.feihua.framework.scheduler.QuartzSchedulerUtils;
import com.feihua.framework.scheduler.api.ApiBaseQuartzJobManager;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by yangwei
 * Created at 2018/12/18 16:42
 */
@Service
public class ApiBaseQuartzJobManagerImpl implements ApiBaseQuartzJobManager {

    private static final Logger logger = LoggerFactory.getLogger(ApiBaseQuartzJobManagerImpl.class);
    private static String DEFAULT_JOB_GROUP_NAME = "manager_default_job_group_name";
    private static String DEFAULT_TRIGGER_GROUP_NAME = "manager_default_trigger_group_name";
    
    @Autowired
    private Scheduler scheduler;

    @Override
    public JobDetail newJob(String jobName, Class cls) {
        return QuartzSchedulerUtils.newJob(jobName,DEFAULT_JOB_GROUP_NAME,cls);
    }

    @Override
    public JobDetail newJob(String jobName, String jobGroupName, Class cls) {
        return QuartzSchedulerUtils.newJob(jobName,jobGroupName,cls);
    }

    @Override
    public Trigger newCronTrigger(String triggerName, String cron, boolean startNow) {
        return QuartzSchedulerUtils.newCronTrigger(triggerName,DEFAULT_TRIGGER_GROUP_NAME,cron,startNow);
    }

    @Override
    public Trigger newCronTrigger(String triggerName, String triggerGroupName, String cron, boolean startNow) {
        return QuartzSchedulerUtils.newCronTrigger(triggerName,triggerGroupName,cron,startNow);
    }

    @Override
    public Trigger newTrigger(String triggerName, ScheduleBuilder scheduleBuilder, boolean startNow) {
        return QuartzSchedulerUtils.newTrigger(triggerName,DEFAULT_TRIGGER_GROUP_NAME,scheduleBuilder,startNow);
    }

    @Override
    public Trigger newTrigger(String triggerName, String triggerGroupName, ScheduleBuilder scheduleBuilder, boolean startNow) {
        return QuartzSchedulerUtils.newTrigger(triggerName,triggerGroupName,scheduleBuilder,startNow);

    }

    @Override
    public void schedulerStart( JobDetail job, Trigger trigger) throws SchedulerException  {
        QuartzSchedulerUtils.schedulerStart(this.scheduler,job,trigger);
    }

    @Override
    public void modifyJobTime( String triggerName, String cron) throws SchedulerException {
        QuartzSchedulerUtils.modifyJobTime(this.scheduler,triggerName,DEFAULT_TRIGGER_GROUP_NAME,cron);
    }

    @Override
    public void modifyJobTime( String triggerName, String triggerGroupName, String cron) throws SchedulerException {
        QuartzSchedulerUtils.modifyJobTime(this.scheduler,triggerName,triggerGroupName,cron);

    }

    @Override
    public void removeJob( String jobName, String triggerName) throws SchedulerException {
        QuartzSchedulerUtils.removeJob(this.scheduler,jobName,DEFAULT_JOB_GROUP_NAME,triggerName,DEFAULT_TRIGGER_GROUP_NAME);
    }

    @Override
    public void removeJob( String jobName, String jobGroupName, String triggerName, String triggerGroupName) throws SchedulerException {
        QuartzSchedulerUtils.removeJob(this.scheduler,jobName,jobGroupName,triggerName,triggerGroupName);

    }

    @Override
    public void shutdownJobs(Scheduler scheduler) throws SchedulerException {
        QuartzSchedulerUtils.shutdownJobs(this.scheduler);
    }

    @Override
    public void shutdownJobsSafety(Scheduler scheduler) throws SchedulerException {
        QuartzSchedulerUtils.shutdownJobsSafety(this.scheduler);
    }
}
