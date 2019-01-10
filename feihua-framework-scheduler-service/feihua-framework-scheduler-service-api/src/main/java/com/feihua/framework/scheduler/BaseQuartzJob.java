package com.feihua.framework.scheduler;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by yangwei
 * Created at 2018/12/18 16:42
 */
public abstract class BaseQuartzJob implements Job {

    private static final Logger logger = LoggerFactory.getLogger(BaseQuartzJob.class);

    public void beforeExecute(JobExecutionContext jobExecutionContext){}
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        beforeExecute(jobExecutionContext);

        try {
            doExecute(jobExecutionContext);
            afterExecute(jobExecutionContext);
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
        } finally {
            finalyExecute(jobExecutionContext);
        }
    }
    public void afterExecute(JobExecutionContext jobExecutionContext){}
    public void finalyExecute(JobExecutionContext jobExecutionContext){}

    public abstract void doExecute(JobExecutionContext jobExecutionContext);

}
