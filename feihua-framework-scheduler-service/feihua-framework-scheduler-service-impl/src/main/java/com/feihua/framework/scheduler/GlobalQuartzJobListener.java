package com.feihua.framework.scheduler;

import com.feihua.framework.scheduler.api.ApiSchedulerPoService;
import com.feihua.framework.scheduler.api.ApiSchedulerRecordExcutionPoService;
import com.feihua.framework.scheduler.po.SchedulerPo;
import com.feihua.framework.scheduler.po.SchedulerRecordExcutionPo;
import com.feihua.utils.spring.SpringContextHolder;
import feihua.jdbc.api.pojo.BasePo;
import org.apache.commons.lang3.StringUtils;
import org.quartz.*;

import java.util.Date;
import java.util.UUID;

import static com.feihua.framework.scheduler.QuartzSchedulerUtils.getBatchNo;

/**
 * 全局监听器，用来记录任务的执行情况
 * Created by yangwei
 * Created at 2019/4/17 15:15
 */
public class GlobalQuartzJobListener implements JobListener {
    public static final String globalQuartzJobListenerName = "GlobalQuartzJobListenerName";
    private ApiSchedulerRecordExcutionPoService apiSchedulerRecordExcutionPoService;
    private ApiSchedulerPoService apiSchedulerPoService;
    @Override
    public String getName() {
        return globalQuartzJobListenerName;
    }

    @Override
    public void jobToBeExecuted(JobExecutionContext context) {
        initBean();
        String batchNo = getBatchNo(context,true);
        SchedulerPo schedulerPo = getSchedulerPo(context);
        if (schedulerPo != null) {
            // 插入执行记录
            SchedulerRecordExcutionPo schedulerRecordExcutionPo = new SchedulerRecordExcutionPo();
            schedulerRecordExcutionPo.setSchedulerId(schedulerPo.getId());
            schedulerRecordExcutionPo.setStartTime(new Date());
            schedulerRecordExcutionPo.setBatchNo(batchNo);
            schedulerRecordExcutionPo.setDuration("");
            schedulerRecordExcutionPo = apiSchedulerRecordExcutionPoService.preInsert(schedulerRecordExcutionPo,BasePo.DEFAULT_USER_ID);
            apiSchedulerRecordExcutionPoService.insert(schedulerRecordExcutionPo);
            context.getJobDetail().getJobDataMap().put("schedulerRecordId",schedulerRecordExcutionPo.getId());
        }
    }

    @Override
    public void jobExecutionVetoed(JobExecutionContext context) {
        initBean();
    }

    @Override
    public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {
        initBean();
        String schedulerRecordId = context.getJobDetail().getJobDataMap().getString("schedulerRecordId");
        String batchNo = getBatchNo(context,false);
        if (StringUtils.isNotEmpty(schedulerRecordId)) {
            SchedulerRecordExcutionPo schedulerRecordExcutionPo = apiSchedulerRecordExcutionPoService.selectByPrimaryKeySimple(schedulerRecordId);
            if (schedulerRecordExcutionPo != null) {
                Date endTime = new Date();
                Date startTime = schedulerRecordExcutionPo.getStartTime();

                schedulerRecordExcutionPo.setEndTime(endTime);
                String duration = (Long.toString( endTime.getTime() - startTime.getTime()));
                schedulerRecordExcutionPo.setDuration(duration);
                if (StringUtils.isNotEmpty(batchNo)) {
                    schedulerRecordExcutionPo.setBatchNo(batchNo);
                }
                schedulerRecordExcutionPo = apiSchedulerRecordExcutionPoService.preUpdate(schedulerRecordExcutionPo,BasePo.DEFAULT_USER_ID);
                apiSchedulerRecordExcutionPoService.updateByPrimaryKeySelective(schedulerRecordExcutionPo);

                // 执行次数
                SchedulerPo schedulerPo = getSchedulerPo(context);
                schedulerPo.setExcuteNum(schedulerPo.getExcuteNum() + 1);
                // 上次执行时间
                schedulerPo.setExcuteAvgTime(duration);
                apiSchedulerPoService.updateByPrimaryKeySelective(schedulerPo);
            }

        }
    }
    private SchedulerPo getSchedulerPo(JobExecutionContext context){
        JobKey jobKey = context.getJobDetail().getKey();
        TriggerKey triggerKey = context.getTrigger().getKey();
        SchedulerPo schedulerPo = new SchedulerPo();
        schedulerPo.setJobName(jobKey.getName());
        schedulerPo.setJobGroup(jobKey.getGroup());
        schedulerPo.setTriggerName(triggerKey.getName());
        schedulerPo.setTriggerGroup(triggerKey.getGroup());
        schedulerPo.setDelFlag(BasePo.YesNo.N.name());
        return apiSchedulerPoService.selectOneSimple(schedulerPo);
    }
    private void initBean(){
        if (apiSchedulerRecordExcutionPoService == null) {
            apiSchedulerRecordExcutionPoService = SpringContextHolder.getBean(ApiSchedulerRecordExcutionPoService.class);
        }
        if (apiSchedulerPoService == null) {
            apiSchedulerPoService = SpringContextHolder.getBean(ApiSchedulerPoService.class);
        }
    }
}
