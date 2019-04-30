package com.feihua.framework.scheduler.impl;

import com.feihua.exception.DataNotFoundException;
import com.feihua.framework.scheduler.QuartzSchedulerUtils;
import com.feihua.framework.scheduler.api.ApiBaseQuartzJobManager;
import com.feihua.framework.scheduler.api.ApiSchedulerPoService;
import com.feihua.framework.scheduler.api.ApiSchedulerService;
import com.feihua.framework.scheduler.dto.JobAndTriggerWrapperDto;
import com.feihua.framework.scheduler.dto.SchedulerDto;
import com.feihua.framework.scheduler.dto.TriggerDetailDto;
import com.feihua.framework.scheduler.po.SchedulerPo;
import com.feihua.utils.string.StringUtils;
import org.quartz.JobDetail;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by yangwei
 * Created at 2019/4/19 10:52
 */
@Service
public class ApiSchedulerServiceImpl implements ApiSchedulerService {


    @Autowired
    private ApiSchedulerPoService apiSchedulerPoService;
    @Autowired
    private ApiBaseQuartzJobManager apiBaseQuartzJobManager;


    private SchedulerDto getScheduler(String schedulerId){
        SchedulerDto schedulerDto = apiSchedulerPoService.selectByPrimaryKey(schedulerId,false);
        if (schedulerDto == null) {
            throw new DataNotFoundException(StringUtils.messageFormat("schedulerNotFound with id={0}",schedulerId));
        }
        return schedulerDto;
    }
    private SchedulerPo getSchedulerPo(String schedulerId){
        SchedulerPo schedulerDto = apiSchedulerPoService.selectByPrimaryKeySimple(schedulerId,false);
        if (schedulerDto == null) {
            throw new DataNotFoundException(StringUtils.messageFormat("schedulerNotFound with id={0}",schedulerId));
        }
        return schedulerDto;
    }
    @Override
    public void publish(String schedulerId, boolean startNow) throws ClassNotFoundException, SchedulerException, DataNotFoundException {
        SchedulerDto schedulerDto = getScheduler(schedulerId);
        Trigger trigger = apiBaseQuartzJobManager.newCronTrigger(schedulerDto.getTriggerName(),schedulerDto.getTriggerGroup(),schedulerDto.getTriggerCronExpression(),startNow);
        JobDetail jobDetail = apiBaseQuartzJobManager.newJob(schedulerDto.getJobName(),schedulerDto.getJobGroup(),schedulerDto.getJobClass());
        apiBaseQuartzJobManager.schedulerStart(jobDetail,trigger);

    }

    @Override
    public void pauseTrigger(String schedulerId) throws  SchedulerException, DataNotFoundException {
        SchedulerDto schedulerDto = getScheduler(schedulerId);
        TriggerKey triggerKey = QuartzSchedulerUtils.getTriggerKey(schedulerDto.getTriggerName(),schedulerDto.getTriggerGroup());
        apiBaseQuartzJobManager.pauseTrigger(triggerKey);
    }

    @Override
    public void resumeTrigger(String schedulerId) throws  SchedulerException, DataNotFoundException {
        SchedulerDto schedulerDto = getScheduler(schedulerId);
        TriggerKey triggerKey = QuartzSchedulerUtils.getTriggerKey(schedulerDto.getTriggerName(),schedulerDto.getTriggerGroup());
        apiBaseQuartzJobManager.resumeTrigger(triggerKey);
    }

    @Override
    public void removeJob(String schedulerId) throws SchedulerException, DataNotFoundException {
        SchedulerDto schedulerDto = getScheduler(schedulerId);
        apiBaseQuartzJobManager.removeJob(schedulerDto.getJobName(),schedulerDto.getJobGroup(),schedulerDto.getTriggerName(),schedulerDto.getTriggerGroup());
    }

    @Override
    public void modifyTriggerTime(String schedulerId,String cron,String cronDesc,boolean startNow,String currentUserId) throws SchedulerException, DataNotFoundException {

        SchedulerPo schedulerDto = getSchedulerPo(schedulerId);
        schedulerDto.setTriggerCronExpression(cron);
        schedulerDto.setTriggerCronExpressionDesc(cronDesc);
        apiSchedulerPoService.preUpdate(schedulerDto,currentUserId);
        apiSchedulerPoService.updateByPrimaryKeySelective(schedulerDto);
        apiBaseQuartzJobManager.modifyJobTime(schedulerDto.getTriggerName(),schedulerDto.getTriggerGroup(),cron,startNow);
    }

    @Override
    public TriggerDetailDto getTriggerDetail(String triggerName, String triggerGroupName) throws SchedulerException {
        Trigger trigger = apiBaseQuartzJobManager.getTrigger(triggerName,triggerGroupName);
        if (trigger != null) {
            return new TriggerDetailDto(trigger);
        }
        return null;
    }


    @Override
    public String getSchedulerStatus(String triggerName, String triggerGroupName) throws SchedulerException {
        Trigger.TriggerState triggerState = apiBaseQuartzJobManager.getTriggerState(triggerName,triggerGroupName);
        return QuartzSchedulerUtils.triggerStateToSchedulerStatus(triggerState);
    }

    @Override
    public List<JobAndTriggerWrapperDto> getAllJobs() throws SchedulerException {
        return apiBaseQuartzJobManager.getAllJobs();
    }
}
