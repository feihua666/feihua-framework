package com.feihua.starter.web.modules.leave.mvc;



import com.feihua.framework.activity.api.ApiActivitiBusinessService;
import com.feihua.framework.activity.api.ApiActivitiTaskService;
import com.feihua.framework.activity.dto.CompleteTaskParamDto;
import com.feihua.framework.rest.ResponseJsonRender;
import com.feihua.framework.rest.interceptor.RepeatFormValidator;
import com.feihua.framework.rest.modules.common.mvc.BaseController;
import com.feihua.starter.service.modules.leave.api.ApiOaLeavePoService;
import com.feihua.starter.service.modules.leave.dto.LeaveApplyParamDto;
import com.feihua.starter.service.modules.leave.dto.OaLeaveDto;
import com.feihua.starter.service.modules.leave.dto.SearchLeavesConditionDto;
import com.feihua.starter.service.modules.leave.po.OaLeavePo;
import com.feihua.starter.web.modules.leave.dto.AddLeaveFormDto;
import com.feihua.starter.web.modules.leave.dto.UpdateLeaveFormDto;
import com.feihua.utils.http.httpServletResponse.ResponseCode;
import feihua.jdbc.api.pojo.BasePo;
import feihua.jdbc.api.pojo.PageAndOrderbyParamDto;
import feihua.jdbc.api.pojo.PageResultDto;
import feihua.jdbc.api.utils.OrderbyUtils;
import feihua.jdbc.api.utils.PageUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Date;

/**
   请假
 * Created by yw on 2015/9/23.
 */
@Controller
@RequestMapping("/oa/workattendance")
public class OaLeaveController extends BaseController {


    private static Logger logger = LoggerFactory.getLogger(OaLeaveController.class);
     @Autowired
     private ApiOaLeavePoService apiOaLeavePoService;

     @Autowired
     @Qualifier("apiOaLeavePoServiceImpl")
     private ApiActivitiBusinessService apiLeaveBusinessService;
     @Autowired
     private ApiActivitiTaskService activityTaskService;


    /**
     * 添加
     * @return
     */
    @RepeatFormValidator
    @RequiresPermissions("oa:leave:add")
    @RequestMapping(value = "/leave", method = RequestMethod.POST)
    public ResponseEntity addOaLeave(AddLeaveFormDto addLeaveFormDto){
        logger.info("添加请假单开始");
        logger.info("当前登录用户id:{}",getLoginUser().getId());
        ResponseJsonRender resultData=new ResponseJsonRender();
        // 属性设置
        OaLeavePo oaLeavePo = new OaLeavePo();
        oaLeavePo.setApplyTime(new Date());
        oaLeavePo.setStartTime(addLeaveFormDto.getStartTime());
        oaLeavePo.setEndTime(addLeaveFormDto.getEndTime());
        oaLeavePo.setLeaveType(addLeaveFormDto.getLeaveType());
        oaLeavePo.setReason(addLeaveFormDto.getReason());
        oaLeavePo.setDataUserId(getLoginUser().getId());

        apiOaLeavePoService.preInsert(oaLeavePo,getLoginUser().getId());
        OaLeaveDto result = apiOaLeavePoService.insertSelective(oaLeavePo);

        if (result == null) {
            // 添加失败
            resultData.setCode(ResponseCode.E404_100001.getCode());
            resultData.setMsg(ResponseCode.E404_100001.getMsg());
            logger.info("code:{},msg:{}",resultData.getCode(),resultData.getMsg());
            logger.info("添加请假单结束，失败");
            return new ResponseEntity(resultData,HttpStatus.NOT_FOUND);
        }else{
            // 添加成功，返回添加的数据
            resultData.setData(result);
            logger.info("添加请假单id:{}",result.getId());
            logger.info("添加请假单结束，成功");
            return new ResponseEntity(resultData, HttpStatus.CREATED);
        }
    }


    /**
     * 删除
     * @return
     */
    @RequiresPermissions("oa:leave:delete")
    @RequestMapping(value = "/leave/{id}", method = RequestMethod.DELETE)
    public ResponseEntity deleteOaLeave(@PathVariable String id){
        ResponseJsonRender resultData=new ResponseJsonRender();
        int flag = apiOaLeavePoService.deleteFlagByPrimaryKey(id);
        if(flag == 0){
            return new ResponseEntity(resultData,HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(resultData,HttpStatus.NO_CONTENT);
    }



    /**
     * 更新
     * @return
     */
    @RequiresPermissions("oa:leave:update")
    @RequestMapping(value = "/leave/{id}", method = RequestMethod.PUT)
    public ResponseEntity updateOaLeave(@PathVariable String id,UpdateLeaveFormDto updateLeaveFormDto){
        ResponseJsonRender resultData=new ResponseJsonRender();
        OaLeaveDto dbLeave = apiOaLeavePoService.selectByPrimaryKey(id);
        if (dbLeave != null && StringUtils.isNotEmpty(dbLeave.getProcessInstanceId())) {
            resultData.setMsg("can not update due have applied");
            resultData.setCode(ResponseCode.E400_100000.getCode());
            return new ResponseEntity(resultData,HttpStatus.BAD_REQUEST);
        }
        // 属性设置
        OaLeavePo oaLeavePo = new OaLeavePo();
        oaLeavePo.setApplyTime(new Date());
        oaLeavePo.setStartTime(updateLeaveFormDto.getStartTime());
        oaLeavePo.setEndTime(updateLeaveFormDto.getEndTime());
        oaLeavePo.setLeaveType(updateLeaveFormDto.getLeaveType());
        oaLeavePo.setReason(updateLeaveFormDto.getReason());
        apiOaLeavePoService.preUpdate(oaLeavePo,getLoginUser().getId());

        OaLeavePo oaLeavePoConditon = new OaLeavePo();
        oaLeavePoConditon.setId(id);
        oaLeavePoConditon.setDelFlag(BasePo.YesNo.N.name());
        oaLeavePoConditon.setUpdateAt(updateLeaveFormDto.getUpdateTime());
        int flag = apiOaLeavePoService.updateSelective(oaLeavePo,oaLeavePoConditon);
        if(flag == 0){
            resultData.setMsg(ResponseCode.E404_100001.getMsg());
            resultData.setCode(ResponseCode.E404_100001.getCode());
            return new ResponseEntity(resultData,HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(resultData,HttpStatus.NO_CONTENT);
    }
    /**
     * 查询
     *
     * @return
     */
    @RequiresPermissions("oa:leave:getbyId")
    @RequestMapping(value = "/leave/{id}", method = RequestMethod.GET)
    public ResponseEntity findOaLeave(@PathVariable String id) {
        ResponseJsonRender resultData = new ResponseJsonRender();
        OaLeaveDto entity = apiOaLeavePoService.selectByPrimaryKey(id);
        return super.returnDto(entity,resultData);
    }

    /**
     * 查询
     *
     * @return
     */
    @RequiresPermissions("oa:leave:list")
    @RequestMapping(value = "/leaves", method = RequestMethod.GET)
    public ResponseEntity listOaLeave(SearchLeavesConditionDto dto){
        ResponseJsonRender resultData = new ResponseJsonRender();
        PageAndOrderbyParamDto pageAndOrderbyParamDto = new PageAndOrderbyParamDto(PageUtils.getPageFromThreadLocal(), OrderbyUtils.getOrderbyFromThreadLocal());
        // 设置当前登录用户id
        dto.setCurrentUserId(getLoginUser().getId());
        dto.setCurrentRoleId(getLoginUserRoleId());
        PageResultDto<OaLeaveDto> list= apiOaLeavePoService.searchLeavesDsf(dto,pageAndOrderbyParamDto);

        return super.returnPageResultDto(list,resultData);
    }


    /**
     * 请假申请发起
     * @return
     */
    @RequiresPermissions("oa:leave:apply")
    @RequestMapping(value = "/leave/{id}/apply", method = RequestMethod.POST)
    public ResponseEntity apply(@PathVariable String id){
        ResponseJsonRender resultData=new ResponseJsonRender();
        LeaveApplyParamDto leaveApplyParamDto = new LeaveApplyParamDto();
        leaveApplyParamDto.setLeaveId(id);
        leaveApplyParamDto.setBusinessId(id);
        leaveApplyParamDto.setCurrentRoleId(getLoginUserRoleId());
        leaveApplyParamDto.setCurrentUserId(getLoginUser().getId());
        apiLeaveBusinessService.apply(leaveApplyParamDto);
        return new ResponseEntity(resultData,HttpStatus.OK);
    }

    /**
     * 审批
     * @param businessId
     * @return
     */
    @RequiresPermissions(value = {"activity:task:redirectbusinessform"})
    @RequestMapping(value = "/leave/{businessId}/aduit", method = RequestMethod.POST)
    public ResponseEntity aduit(@PathVariable String businessId,CompleteTaskParamDto completeTaskParamDto) {
        ResponseJsonRender resultData=new ResponseJsonRender();

        completeTaskParamDto.setBusinessId(businessId);
        completeTaskParamDto.setCurrentUserId(getLoginUser().getId());
        completeTaskParamDto.setCurrentRoleId(getLoginUserRoleId());

        apiLeaveBusinessService.completeTask( completeTaskParamDto);
        return  new ResponseEntity(resultData,HttpStatus.OK);
    }
}
