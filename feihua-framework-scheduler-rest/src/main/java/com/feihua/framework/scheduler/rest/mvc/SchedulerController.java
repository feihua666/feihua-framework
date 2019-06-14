package com.feihua.framework.scheduler.rest.mvc;

import com.feihua.exception.DataNotFoundException;
import com.feihua.framework.base.modules.role.dto.BaseRoleDto;
import com.feihua.framework.constants.DictEnum;
import com.feihua.framework.rest.ResponseJsonRender;
import com.feihua.framework.rest.interceptor.RepeatFormValidator;
import com.feihua.framework.rest.mvc.SuperController;
import com.feihua.framework.scheduler.QuartzSchedulerUtils;
import com.feihua.framework.scheduler.api.ApiBaseQuartzJobManager;
import com.feihua.framework.scheduler.api.ApiSchedulerService;
import com.feihua.framework.scheduler.dto.*;
import com.feihua.framework.scheduler.rest.vo.SchedulerOriginVo;
import com.feihua.framework.scheduler.rest.vo.SchedulerVo;
import com.feihua.utils.collection.CollectionUtils;
import com.feihua.utils.http.httpServletResponse.ResponseCode;
import feihua.jdbc.api.pojo.BasePo;
import feihua.jdbc.api.pojo.PageAndOrderbyParamDto;
import feihua.jdbc.api.pojo.PageResultDto;
import feihua.jdbc.api.utils.OrderbyUtils;
import feihua.jdbc.api.utils.PageUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.quartz.JobDetail;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.feihua.framework.scheduler.api.ApiSchedulerPoService;
import com.feihua.framework.scheduler.rest.dto.AddSchedulerFormDto;
import com.feihua.framework.scheduler.rest.dto.UpdateSchedulerFormDto;
import com.feihua.framework.scheduler.po.SchedulerPo;

import java.util.ArrayList;
import java.util.List;

/**
 * 任务计划管理
 * Created by yangwei
 */
@RestController
@RequestMapping("/scheduler")
public class SchedulerController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(SchedulerController.class);

    @Autowired
    private ApiSchedulerPoService apiSchedulerPoService;
    @Autowired
    private ApiSchedulerService apiSchedulerService;

    /**
     * 单资源，添加
     * @param dto
     * @return
     */
    @RepeatFormValidator
    @RequiresPermissions("scheduler:add")
    @RequestMapping(value = "/scheduler",method = RequestMethod.POST)
    public ResponseEntity add(AddSchedulerFormDto dto){
        logger.info("添加任务计划开始");
        logger.info("当前登录用户id:{}",getLoginUser().getId());
        ResponseJsonRender resultData=new ResponseJsonRender();
        // 表单值设置
        SchedulerPo basePo = new SchedulerPo();
        basePo.setName(dto.getName());
        basePo.setJobName(dto.getJobName());
        basePo.setJobClass(dto.getJobClass());
        basePo.setJobGroup(dto.getJobGroup());
        basePo.setTriggerName(dto.getTriggerName());
        basePo.setTriggerGroup(dto.getTriggerGroup());
        basePo.setTriggerCronExpression(dto.getTriggerCronExpression());
        basePo.setTriggerCronExpressionDesc(dto.getTriggerCronExpressionDesc());
        basePo.setExcuteNum(0);
        basePo.setExcuteAvgTime("0");

        basePo = apiSchedulerPoService.preInsert(basePo,getLoginUser().getId());
        SchedulerDto r = apiSchedulerPoService.insert(basePo);
        if (r == null) {
            // 添加失败
            resultData.setCode(ResponseCode.E404_100001.getCode());
            resultData.setMsg(ResponseCode.E404_100001.getMsg());
            logger.info("code:{},msg:{}",resultData.getCode(),resultData.getMsg());
            logger.info("添加任务计划结束，失败");
            return new ResponseEntity(resultData,HttpStatus.NOT_FOUND);
        }else{
            // 添加成功，返回添加的数据
            resultData.setData(r);
            logger.info("添加任务计划id:{}",r.getId());
            logger.info("添加任务计划结束，成功");
            return new ResponseEntity(resultData, HttpStatus.CREATED);
        }
    }

    /**
     * 单资源，删除
     * @param id
     * @return
     */
    @RepeatFormValidator
    @RequiresPermissions("scheduler:delete")
    @RequestMapping(value = "/scheduler/{id}",method = RequestMethod.DELETE)
    public ResponseEntity delete(@PathVariable String id){
        logger.info("删除任务计划开始");
        logger.info("用户id:{}",getLoginUser().getId());
        logger.info("任务计划id:{}",id);
        ResponseJsonRender resultData=new ResponseJsonRender();

            int r = apiSchedulerPoService.deleteFlagByPrimaryKeyWithUpdateUser(id,getLoginUser().getId());
            if (r <= 0) {
                // 删除失败，可能没有找到资源
                resultData.setCode(ResponseCode.E404_100001.getCode());
                resultData.setMsg(ResponseCode.E404_100001.getMsg());
                logger.info("code:{},msg:{}",resultData.getCode(),resultData.getMsg());
                logger.info("删除任务计划结束，失败");
                return new ResponseEntity(resultData,HttpStatus.NOT_FOUND);
            }else{
                // 删除成功
                logger.info("删除的任务计划id:{}",id);
                logger.info("删除任务计划结束，成功");
                return new ResponseEntity(resultData,HttpStatus.NO_CONTENT);
            }
    }

    /**
     * 单资源，更新
     * @param id
     * @param dto
     * @return
     */
    @RepeatFormValidator
    @RequiresPermissions("scheduler:update")
    @RequestMapping(value = "/scheduler/{id}",method = RequestMethod.PUT)
    public ResponseEntity update(@PathVariable String id, UpdateSchedulerFormDto dto){
        logger.info("更新任务计划开始");
        logger.info("当前登录用户id:{}",getLoginUser().getId());
        logger.info("任务计划id:{}",id);
        ResponseJsonRender resultData=new ResponseJsonRender();
        // 表单值设置
        SchedulerPo basePo = new SchedulerPo();
        // id
        basePo.setId(id);
        basePo.setName(dto.getName());
        basePo.setJobName(dto.getJobName());
        basePo.setJobClass(dto.getJobClass());
        basePo.setJobGroup(dto.getJobGroup());
        basePo.setTriggerName(dto.getTriggerName());
        basePo.setTriggerGroup(dto.getTriggerGroup());
        basePo.setTriggerCronExpression(dto.getTriggerCronExpression());
        basePo.setTriggerCronExpressionDesc(dto.getTriggerCronExpressionDesc());

        // 用条件更新，乐观锁机制
        SchedulerPo basePoCondition = new SchedulerPo();
        basePoCondition.setId(id);
        basePoCondition.setDelFlag(BasePo.YesNo.N.name());
        basePoCondition.setUpdateAt(dto.getUpdateTime());
        basePo = apiSchedulerPoService.preUpdate(basePo,getLoginUser().getId());
        int r = apiSchedulerPoService.updateSelective(basePo,basePoCondition);
        if (r <= 0) {
            // 更新失败，资源不存在
            resultData.setCode(ResponseCode.E404_100001.getCode());
            resultData.setMsg(ResponseCode.E404_100001.getMsg());
            logger.info("code:{},msg:{}",resultData.getCode(),resultData.getMsg());
            logger.info("更新任务计划结束，失败");
            return new ResponseEntity(resultData,HttpStatus.NOT_FOUND);
        }else{
            // 更新成功，已被成功创建
            logger.info("更新的任务计划id:{}",id);
            logger.info("更新任务计划结束，成功");
            return new ResponseEntity(resultData, HttpStatus.CREATED);
        }
    }

    /**
     * 单资源，获取id任务计划
     * @param id
     * @return
     */
    @RequiresPermissions("scheduler:getById")
    @RequestMapping(value = "/scheduler/{id}",method = RequestMethod.GET)
    public ResponseEntity getById(@PathVariable String id){

        ResponseJsonRender resultData=new ResponseJsonRender();
        SchedulerDto baseDataScopeDto = apiSchedulerPoService.selectByPrimaryKey(id,false);
        if(baseDataScopeDto != null){
            resultData.setData(baseDataScopeDto);
            return new ResponseEntity(resultData, HttpStatus.OK);
        }else{
            resultData.setCode(ResponseCode.E404_100001.getCode());
            resultData.setMsg(ResponseCode.E404_100001.getMsg());
            return new ResponseEntity(resultData,HttpStatus.NOT_FOUND);
        }
    }

    /**
     * 复数资源，搜索任务计划
     * @param dto
     * @return
     */
    @RequiresPermissions("scheduler:search")
    @RequestMapping(value = "/schedulers",method = RequestMethod.GET)
    public ResponseEntity search(SearchSchedulersConditionDto dto){

        ResponseJsonRender resultData=new ResponseJsonRender();
        PageAndOrderbyParamDto pageAndOrderbyParamDto = new PageAndOrderbyParamDto(PageUtils.getPageFromThreadLocal(), OrderbyUtils.getOrderbyFromThreadLocal());
        // 设置当前登录用户id
        dto.setCurrentUserId(getLoginUser().getId());
        dto.setCurrentRoleId(getLoginUserRoleId());
        dto.setCurrentPostId(getLoginUserPostId());
        PageResultDto<SchedulerDto> list = apiSchedulerPoService.searchSchedulersDsf(dto,pageAndOrderbyParamDto);

        List<SchedulerDto> data = list.getData();
        if(data != null && !data.isEmpty()){
            List<SchedulerVo> schedulerVos = new ArrayList<>(data.size());
            SchedulerVo schedulerVo = null;
            for (SchedulerDto schedulerDto : data) {
                schedulerVo = new SchedulerVo(schedulerDto);
                try {
                    TriggerDetailDto triggerDetailDto = apiSchedulerService.getTriggerDetail(schedulerDto.getTriggerName(),schedulerDto.getTriggerGroup());
                    schedulerVo.setTriggerDetailDto(triggerDetailDto);
                } catch (SchedulerException e) {
                    e.printStackTrace();
                }
                try {
                    String schedulerStatus = apiSchedulerService.getSchedulerStatus(schedulerDto.getTriggerName(),schedulerDto.getTriggerGroup());
                    schedulerVo.setSchedulerStatus(schedulerStatus);
                } catch (SchedulerException e) {
                    logger.error("can not getTriggerState by triggerName={} and triggerGroupName={}",schedulerDto.getTriggerName(),schedulerDto.getTriggerGroup(),e);

                }
                if(StringUtils.isEmpty(schedulerVo.getSchedulerStatus())){
                    schedulerVo.setSchedulerStatus(DictEnum.SchedulerStatus.offline.name());
                }
                schedulerVos.add(schedulerVo);
            }
            resultData.setData(schedulerVos);
            resultData.setPage(list.getPage());
            return new ResponseEntity(resultData, HttpStatus.OK);
        }else{
            resultData.setCode(ResponseCode.E404_100001.getCode());
            resultData.setMsg(ResponseCode.E404_100001.getMsg());
            return new ResponseEntity(resultData,HttpStatus.NOT_FOUND);
        }
    }



    /**
     * 复数资源，搜索任务计划
     * @param dto
     * @return
     */
    @RequiresPermissions("scheduler:search:origin")
    @RequestMapping(value = "/origin/schedulers",method = RequestMethod.GET)
    public ResponseEntity origin(final SearchSchedulersOriginConditionDto dto){

        ResponseJsonRender resultData=new ResponseJsonRender();
        List<JobAndTriggerWrapperDto> wrapperDtos = null;
        try {
            wrapperDtos = apiSchedulerService.getAllJobs();
        } catch (SchedulerException e) {
            logger.error(e.getMessage(),e);
            return returnDto(null,resultData);
        }

        if (wrapperDtos != null && !wrapperDtos.isEmpty()) {
            CollectionUtils.filter(wrapperDtos, new CollectionUtils.Filter<JobAndTriggerWrapperDto>() {
                @Override
                public boolean filter(JobAndTriggerWrapperDto obj) {
                    String jobName = StringUtils.isEmpty(dto.getJobName()) ? dto.getJobName() : obj.getJobDetail().getKey().getName();
                    String jobGroup = StringUtils.isEmpty(dto.getJobGroup()) ? dto.getJobGroup() : obj.getJobDetail().getKey().getGroup();
                    String triggerName = StringUtils.isEmpty(dto.getTriggerName()) ? dto.getTriggerName() : obj.getTrigger().getKey().getName();
                    String triggerGroup = StringUtils.isEmpty(dto.getTriggerGroup()) ? dto.getTriggerGroup() : obj.getTrigger().getKey().getGroup();
                    return (StringUtils.equals(jobName + jobGroup + triggerName + triggerGroup,
                            dto.getJobName() + dto.getJobGroup() + dto.getTriggerName() + dto.getTriggerGroup()));
                }
            });
        }

        if (wrapperDtos != null && !wrapperDtos.isEmpty()) {
            List<SchedulerOriginVo> schedulerOriginVos = new ArrayList<>(wrapperDtos.size());
            SchedulerOriginVo schedulerOriginVo = null;
            SchedulerPo schedulerConditionPo = null;

            for (JobAndTriggerWrapperDto wrapperDto : wrapperDtos) {
                schedulerOriginVo = new SchedulerOriginVo();
                // 查询是否在维护中
                schedulerConditionPo = new SchedulerPo();
                schedulerConditionPo.setJobName(wrapperDto.getJobDetail().getKey().getName());
                schedulerConditionPo.setJobGroup(wrapperDto.getJobDetail().getKey().getGroup());
                schedulerConditionPo.setTriggerName(wrapperDto.getTrigger().getKey().getName());
                schedulerConditionPo.setTriggerGroup(wrapperDto.getTrigger().getKey().getGroup());
                SchedulerDto schedulerDto = apiSchedulerPoService.selectOne(schedulerConditionPo);
                schedulerOriginVo.setJobName(wrapperDto.getJobDetail().getKey().getName());
                schedulerOriginVo.setJobGroup(wrapperDto.getJobDetail().getKey().getGroup());
                schedulerOriginVo.setJobClass(wrapperDto.getJobDetail().getJobClass().getCanonicalName());
                schedulerOriginVo.setTriggerName(wrapperDto.getTrigger().getKey().getName());
                schedulerOriginVo.setTriggerGroup(wrapperDto.getTrigger().getKey().getGroup());
                if (schedulerDto != null) {
                    schedulerOriginVo.setSchedulerId(schedulerDto.getId());
                    schedulerOriginVo.setSchedulerName(schedulerDto.getName());
                }
                if (wrapperDto.getTrigger() != null) {
                    schedulerOriginVo.setTriggerDetailDto(new TriggerDetailDto(wrapperDto.getTrigger()));
                }
                if(wrapperDto.getTriggerState() != null ) {
                    schedulerOriginVo.setSchedulerStatus(QuartzSchedulerUtils.triggerStateToSchedulerStatus(wrapperDto.getTriggerState()));
                }
                schedulerOriginVos.add(schedulerOriginVo);
            }
            return returnList(schedulerOriginVos,resultData);
        }else{
            return returnDto(null,resultData);
        }
    }

    // 以下是任务操作

    /**
     * 发布任务
     * @param id schedulerId
     * @return
     */
    @RepeatFormValidator
    @RequiresPermissions("scheduler:publish")
    @RequestMapping(value = "/scheduler/{id}/publish",method = RequestMethod.POST)
    public ResponseEntity publish(@PathVariable String id,boolean startNow){
        ResponseJsonRender resultData=new ResponseJsonRender();
        try {
            apiSchedulerService.publish(id,startNow);
        } catch (ClassNotFoundException e) {
            // 类路径不对
            resultData.setCode("ClassNotFound");
            resultData.setMsg("ClassNotFound");
            return new ResponseEntity(resultData,HttpStatus.BAD_REQUEST);
        } catch (SchedulerException e) {
            logger.error(e.getMessage(),e);
            // 类路径不对
            resultData.setCode("schedulerStartError");
            resultData.setMsg("schedulerStartError");
            return new ResponseEntity(resultData,HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (DataNotFoundException e) {
            return returnDto(null,resultData);
        }
        return new ResponseEntity(resultData,HttpStatus.CREATED);
    }
    /**
     * 暂停任务
     * @param id schedulerId
     * @return
     */
    @RepeatFormValidator
    @RequiresPermissions("scheduler:pause")
    @RequestMapping(value = "/scheduler/{id}/pause",method = RequestMethod.POST)
    public ResponseEntity pause(@PathVariable String id){
        ResponseJsonRender resultData=new ResponseJsonRender();
        try {
            apiSchedulerService.pauseTrigger(id);
        } catch (DataNotFoundException e) {
            return returnDto(null,resultData);
        } catch (SchedulerException e) {
            logger.error(e.getMessage(),e);
            resultData.setCode("schedulerError");
            resultData.setMsg("schedulerError");
            return new ResponseEntity(resultData,HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity(resultData,HttpStatus.CREATED);
    }
    /**
     * 恢复暂停的任务
     * @param id schedulerId
     * @return
     */
    @RepeatFormValidator
    @RequiresPermissions("scheduler:resume")
    @RequestMapping(value = "/scheduler/{id}/resume",method = RequestMethod.POST)
    public ResponseEntity resume(@PathVariable String id){
        ResponseJsonRender resultData=new ResponseJsonRender();
        try {
            apiSchedulerService.resumeTrigger(id);
        } catch (DataNotFoundException e) {
            return returnDto(null,resultData);
        } catch (SchedulerException e) {
            logger.error(e.getMessage(),e);
            resultData.setCode("schedulerError");
            resultData.setMsg("schedulerError");
            return new ResponseEntity(resultData,HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity(resultData,HttpStatus.CREATED);
    }
    /**
     * 删除任务,取消发布任务
     * @param id schedulerId
     * @return
     */
    @RepeatFormValidator
    @RequiresPermissions("scheduler:remove")
    @RequestMapping(value = "/scheduler/{id}/remove",method = RequestMethod.POST)
    public ResponseEntity remove(@PathVariable String id){
        ResponseJsonRender resultData=new ResponseJsonRender();
        try {
            apiSchedulerService.removeJob(id);
        } catch (DataNotFoundException e) {
            return returnDto(null,resultData);
        } catch (SchedulerException e) {
            logger.error(e.getMessage(),e);
            resultData.setCode("schedulerError");
            resultData.setMsg("schedulerError");
            return new ResponseEntity(resultData,HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity(resultData,HttpStatus.CREATED);
    }
    /**
     * 修改执行时间
     * @param id schedulerId
     * @return
     */
    @RepeatFormValidator
    @RequiresPermissions("scheduler:modifyTriggerTime")
    @RequestMapping(value = "/scheduler/{id}/modifyTriggerTime",method = RequestMethod.POST)
    public ResponseEntity modifyTriggerTime(@PathVariable String id,String cron,String cronDesc,boolean startNow){
        ResponseJsonRender resultData=new ResponseJsonRender();
        try {
            apiSchedulerService.modifyTriggerTime(id,cron,cronDesc,startNow,getLoginUserId());
        } catch (DataNotFoundException e) {
            return returnDto(null,resultData);
        } catch (SchedulerException e) {
            logger.error(e.getMessage(),e);
            resultData.setCode("schedulerError");
            resultData.setMsg("schedulerError");
            return new ResponseEntity(resultData,HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity(resultData,HttpStatus.CREATED);
    }
}
