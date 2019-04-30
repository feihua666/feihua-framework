package com.feihua.framework.scheduler;

import org.quartz.JobExecutionContext;

/**
 * 全局默认的mq触发调用任务
 * Created by yangwei
 * Created at 2019/4/24 9:41
 */
public class GlobalMqQuartzJob extends BaseQuartzJob {
    @Override
    public void doExecute(JobExecutionContext jobExecutionContext) {
        
    }
}
