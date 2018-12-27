package com.feihua.framework.scheduler.test;

import com.feihua.framework.scheduler.api.ApiBaseQuartzJobManager;
import org.quartz.JobDetail;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by yangwei
 * Created at 2018/12/18 17:44
 */
public class Main {
    public static void main(String[] args) throws SchedulerException {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext-scheduler-service-test.xml");

        simpleTriggerJob(applicationContext);
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
}
