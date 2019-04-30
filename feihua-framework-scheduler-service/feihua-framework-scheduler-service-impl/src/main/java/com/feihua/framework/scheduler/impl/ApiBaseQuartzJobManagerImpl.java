package com.feihua.framework.scheduler.impl;

import com.feihua.framework.scheduler.QuartzSchedulerUtils;
import com.feihua.framework.scheduler.api.ApiBaseQuartzJobManager;
import com.feihua.framework.scheduler.dto.JobAndTriggerWrapperDto;
import org.apache.commons.lang3.StringUtils;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.quartz.impl.matchers.KeyMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.feihua.framework.scheduler.QuartzSchedulerUtils.DEFAULT_JOB_GROUP_NAME;
import static com.feihua.framework.scheduler.QuartzSchedulerUtils.DEFAULT_TRIGGER_GROUP_NAME;

/**
 * Created by yangwei
 * Created at 2018/12/18 16:42
 */
@Service
public class ApiBaseQuartzJobManagerImpl implements ApiBaseQuartzJobManager {

    private static final Logger logger = LoggerFactory.getLogger(ApiBaseQuartzJobManagerImpl.class);

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
    public JobDetail newJob(String jobName, String jobGroupName, String cls) throws ClassNotFoundException {
        Class clazz = Class.forName(cls);
        return QuartzSchedulerUtils.newJob(jobName,jobGroupName,clazz);
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
    public void modifyJobTime( String triggerName, String cron, boolean startNow) throws SchedulerException {
        QuartzSchedulerUtils.modifyJobTime(this.scheduler,triggerName,DEFAULT_TRIGGER_GROUP_NAME, startNow);
    }

    @Override
    public void modifyJobTime( String triggerName, String triggerGroupName, String cron, boolean startNow) throws SchedulerException {
        QuartzSchedulerUtils.modifyJobTime(this.scheduler,triggerName,triggerGroupName,cron,startNow);

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


    @Override
    public Trigger.TriggerState getTriggerState(String triggerName, String triggerGroupName) throws SchedulerException {
        if(StringUtils.isEmpty(triggerGroupName)){
            triggerGroupName = DEFAULT_TRIGGER_GROUP_NAME;
        }

        TriggerKey triggerKey = QuartzSchedulerUtils.getTriggerKey(triggerName, triggerGroupName);
        Trigger.TriggerState triggerState = getTriggerState(triggerKey);

        return triggerState;
    }

    @Override
    public Trigger.TriggerState getTriggerState(Trigger trigger) throws SchedulerException {
        Trigger.TriggerState triggerState = getTriggerState(trigger.getKey());

        return triggerState;
    }

    @Override
    public Trigger.TriggerState getTriggerState(TriggerKey triggerKey) throws SchedulerException {
        Trigger.TriggerState triggerState = this.scheduler.getTriggerState(triggerKey);

        return triggerState;
    }

    @Override
    public Trigger getTrigger(String triggerName, String triggerGroupName) throws SchedulerException {
        if(StringUtils.isEmpty(triggerGroupName)){
            triggerGroupName = DEFAULT_TRIGGER_GROUP_NAME;
        }

        TriggerKey triggerKey = QuartzSchedulerUtils.getTriggerKey(triggerName, triggerGroupName);
        return this.scheduler.getTrigger(triggerKey);
    }

    @Override
    public JobDetail getJobDetail(String jobName, String jobGroupName) throws SchedulerException {
        if(StringUtils.isEmpty(jobGroupName)){
            jobGroupName = DEFAULT_JOB_GROUP_NAME;
        }
        JobKey jobKey = QuartzSchedulerUtils.getJobKey(jobName,jobGroupName);
        return this.scheduler.getJobDetail(jobKey);
    }

    @Override
    public List<JobAndTriggerWrapperDto> getAllJobs() throws SchedulerException {
        List<String> triggerGroupNames = scheduler.getTriggerGroupNames();
        if(triggerGroupNames != null && !triggerGroupNames.isEmpty()){
            List<JobAndTriggerWrapperDto> jobAndTriggerWrapperDtos = new ArrayList<>(triggerGroupNames.size());
            JobAndTriggerWrapperDto jobAndTriggerWrapperDto = null;
            for (String groupName : triggerGroupNames) {
                //组装group的匹配，为了模糊获取所有的triggerKey或者jobKey
                GroupMatcher groupMatcher = GroupMatcher.groupEquals(groupName);
                //获取所有的triggerKey
                Set<TriggerKey> triggerKeySet = scheduler.getTriggerKeys(groupMatcher);
                for (TriggerKey triggerKey : triggerKeySet) {
                    //通过triggerKey在scheduler中获取trigger对象
                    Trigger trigger = scheduler.getTrigger(triggerKey);
                    //获取trigger拥有的Job
                    JobKey jobKey = trigger.getJobKey();
                    JobDetail jobDetail = scheduler.getJobDetail(jobKey);
                    //组装页面需要显示的数据

                    jobAndTriggerWrapperDto = new JobAndTriggerWrapperDto();
                    jobAndTriggerWrapperDto.setJobDetail(jobDetail);
                    jobAndTriggerWrapperDto.setTrigger(trigger);
                    jobAndTriggerWrapperDto.setTriggerState(getTriggerState(trigger));
                    jobAndTriggerWrapperDtos.add(jobAndTriggerWrapperDto);

                }
            }
            return jobAndTriggerWrapperDtos;
        }

        return null;
    }
    public void addJobListener(JobListener jobListener,JobKey jobKey) throws SchedulerException {
        KeyMatcher keyMatcher = KeyMatcher.keyEquals(jobKey);
        this.scheduler.getListenerManager().addJobListener(jobListener,keyMatcher);
    }
    public void addTriggerListener(TriggerListener triggerListener,TriggerKey triggerKey) throws SchedulerException {
        KeyMatcher keyMatcher = KeyMatcher.keyEquals(triggerKey);
        this.scheduler.getListenerManager().addTriggerListener(triggerListener,keyMatcher);
    }
    @Override
    public void pauseJob(JobKey jobKey) throws SchedulerException {
        QuartzSchedulerUtils.pauseJob(this.scheduler,jobKey);
    }

    @Override
    public void pauseTrigger(TriggerKey triggerKey) throws SchedulerException {
        QuartzSchedulerUtils.pauseTrigger(this.scheduler,triggerKey);
    }

    @Override
    public void resumeJob(JobKey jobKey) throws SchedulerException {
        QuartzSchedulerUtils.resumeJob(this.scheduler,jobKey);
    }

    @Override
    public void resumeTrigger(TriggerKey triggerKey) throws SchedulerException {
        QuartzSchedulerUtils.resumeTrigger(this.scheduler,triggerKey);
    }
}
