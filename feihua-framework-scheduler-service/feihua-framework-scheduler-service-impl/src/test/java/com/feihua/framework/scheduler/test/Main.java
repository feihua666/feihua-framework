package com.feihua.framework.scheduler.test;

import com.feihua.framework.scheduler.api.ApiBaseQuartzJobManager;
import org.quartz.*;
import org.quartz.impl.JobDetailImpl;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.*;

/**
 * Created by yangwei
 * Created at 2018/12/18 17:44
 */
public class Main {
    public static void main(String[] args) throws SchedulerException {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext-scheduler-service-test.xml");
        //cronTriggerJob(applicationContext);
        scheduler(applicationContext);
    }
    public static void cronTriggerJob(ApplicationContext applicationContext) throws SchedulerException {

        ApiBaseQuartzJobManager apiBaseQuartzJobManager = applicationContext.getBean(ApiBaseQuartzJobManager.class);
        JobDetail jobDetail = apiBaseQuartzJobManager.newJob("test", QuartzJobExample.class);
        Trigger trigger = apiBaseQuartzJobManager.newCronTrigger("test","0/1 * * * * ?",true);
        apiBaseQuartzJobManager.schedulerStart(jobDetail,trigger);

        JobDetail jobDetail1 = apiBaseQuartzJobManager.newJob("test1", QuartzJobExample1.class);
        Trigger trigger1 = apiBaseQuartzJobManager.newCronTrigger("test1","0/1 * * * * ?",false);
        apiBaseQuartzJobManager.schedulerStart(jobDetail1,trigger1);
    }
    public static void simpleTriggerJob(ApplicationContext applicationContext) throws SchedulerException{
        ApiBaseQuartzJobManager apiBaseQuartzJobManager = applicationContext.getBean(ApiBaseQuartzJobManager.class);
        JobDetail jobDetail = apiBaseQuartzJobManager.newJob("test2", QuartzJobExample.class);
        Trigger trigger = apiBaseQuartzJobManager.newTrigger("test2",
                SimpleScheduleBuilder.simpleSchedule()
                        .withIntervalInSeconds(1) //时间间隔
                        .repeatForever() // 重复次数
                ,true);
        apiBaseQuartzJobManager.schedulerStart(jobDetail,trigger);
    }
    public static void scheduler(ApplicationContext applicationContext) throws SchedulerException{
        Scheduler scheduler = applicationContext.getBean(Scheduler.class);
        System.out.println(scheduler.getSchedulerName());
        System.out.println(scheduler.getCalendarNames());
        System.out.println(scheduler.getCurrentlyExecutingJobs());
        System.out.println(scheduler.getJobGroupNames());
        System.out.println(scheduler.getPausedTriggerGroups());
        System.out.println(scheduler.getTriggerGroupNames());
        List<String> triggerGroupNames = scheduler.getTriggerGroupNames();
        List<Map<String,Object>> list = new ArrayList<>();
        for (String groupName : triggerGroupNames) { //组装group的匹配，为了模糊获取所有的triggerKey或者jobKey
             GroupMatcher groupMatcher = GroupMatcher.groupEquals(groupName); //获取所有的triggerKey
            Set<TriggerKey> triggerKeySet = scheduler.getTriggerKeys(groupMatcher);
            for (TriggerKey triggerKey : triggerKeySet) { //通过triggerKey在scheduler中获取trigger对象
                 Trigger trigger = scheduler.getTrigger(triggerKey); //获取trigger拥有的Job
                JobKey jobKey = trigger.getJobKey();
                JobDetailImpl jobDetail = (JobDetailImpl) scheduler.getJobDetail(jobKey); //组装页面需要显示的数据
                 Map<String,Object> quartzJobsVO = new HashMap<>();
                 quartzJobsVO.put("groupName",groupName);
                 quartzJobsVO.put("getName",jobDetail.getName());
                list.add(quartzJobsVO);
            }
        }
        System.out.println(list);


    }
}
