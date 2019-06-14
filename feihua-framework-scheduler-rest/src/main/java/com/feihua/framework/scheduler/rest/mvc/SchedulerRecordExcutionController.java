package com.feihua.framework.scheduler.rest.mvc;

import com.feihua.framework.base.modules.role.dto.BaseRoleDto;
import com.feihua.framework.rest.ResponseJsonRender;
import com.feihua.framework.rest.interceptor.RepeatFormValidator;
import com.feihua.framework.rest.mvc.SuperController;
import com.feihua.framework.scheduler.api.ApiSchedulerRecordExcutionPoService;
import com.feihua.framework.scheduler.dto.SchedulerRecordExcutionDto;
import com.feihua.framework.scheduler.dto.SearchSchedulerRecordExcutionsConditionDto;
import com.feihua.utils.http.httpServletResponse.ResponseCode;
import feihua.jdbc.api.pojo.PageAndOrderbyParamDto;
import feihua.jdbc.api.pojo.PageResultDto;
import feihua.jdbc.api.utils.OrderbyUtils;
import feihua.jdbc.api.utils.PageUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
/**
 * 任务计划记录
 * Created by yangwei
 */
@RestController
@RequestMapping("/scheduler")
public class SchedulerRecordExcutionController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(SchedulerRecordExcutionController.class);

    @Autowired
    private ApiSchedulerRecordExcutionPoService apiSchedulerRecordExcutionPoService;

    /**
     * 复数资源，搜索任务计划记录
     * @param dto
     * @return
     */
    @RequiresPermissions("scheduler:excuteRecord")
    @RequestMapping(value = "/scheduler/{schedulerId}/excuteRecords",method = RequestMethod.GET)
    public ResponseEntity search(@PathVariable String schedulerId, SearchSchedulerRecordExcutionsConditionDto dto){

        ResponseJsonRender resultData=new ResponseJsonRender();
        dto.setSchedulerId(schedulerId);
        PageAndOrderbyParamDto pageAndOrderbyParamDto = new PageAndOrderbyParamDto(PageUtils.getPageFromThreadLocal(), OrderbyUtils.getOrderbyFromThreadLocal());
        // 设置当前登录用户id
        dto.setCurrentUserId(getLoginUser().getId());
        dto.setCurrentRoleId(getLoginUserRoleId());
        dto.setCurrentPostId(getLoginUserPostId());
        PageResultDto<SchedulerRecordExcutionDto> list = apiSchedulerRecordExcutionPoService.searchSchedulerRecordExcutionsDsf(dto,pageAndOrderbyParamDto);

        if(CollectionUtils.isNotEmpty(list.getData())){
            resultData.setData(list.getData());
            resultData.setPage(list.getPage());
            return new ResponseEntity(resultData, HttpStatus.OK);
        }else{
            resultData.setCode(ResponseCode.E404_100001.getCode());
            resultData.setMsg(ResponseCode.E404_100001.getMsg());
            return new ResponseEntity(resultData,HttpStatus.NOT_FOUND);
        }
    }
}
