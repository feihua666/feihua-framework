package com.feihua.framework.activity.rest.mvc;

import com.feihua.framework.activity.api.ApiActivitiProcessService;
import com.feihua.framework.activity.api.ApiActivitiTaskService;
import com.feihua.framework.activity.dto.TaskDto;
import com.feihua.framework.rest.ResponseJsonRender;
import com.feihua.framework.rest.mvc.SuperController;
import feihua.jdbc.api.pojo.Page;
import feihua.jdbc.api.utils.PageUtils;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by yw on 2017/2/4.
 */
@Controller
@RequestMapping("/activity")
public class ActivityTaskController extends SuperController {
    private static Logger logger = LoggerFactory.getLogger(ActivityTaskController.class);

    @Autowired
    private ApiActivitiProcessService activityProcessService;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private ApiActivitiTaskService activityTaskService;
    @Autowired
    private TaskService taskService;
    /**
     * 待办任务列表查询
     * @param activityType
     * @return
     */
    @RequiresPermissions("activity:task:list")
    @RequestMapping(value = "/tasks", method = RequestMethod.GET)
    public ResponseEntity taskTodolist( String activityType,Date startTime,Date endTime,String taskStatus) {
        ResponseJsonRender resultData=new ResponseJsonRender();

        Page page  = PageUtils.getPageFromThreadLocal();
        List<TaskDto> list = null;
        if(ApiActivitiTaskService.TASK_STATUS_TODO.equals(taskStatus)){
            list = activityTaskService.searchTaskTodoList(activityType,startTime,endTime,page,getLoginUser().getId());
        }else if(ApiActivitiTaskService.TASK_STATUS_CLAIM.equals(taskStatus)){
            list = activityTaskService.searchTaskToClaimList(activityType,startTime,endTime,page,getLoginUser().getId());
        }
        resultData.setData(list);
        return new ResponseEntity(resultData, HttpStatus.OK);
    }
    /**
     * 签收任务
     * @param taskId
     * @return
     */
    @RequiresPermissions("activity:task:claim")
    @RequestMapping(value = "/task/{taskId}/claim", method = RequestMethod.POST)
    public ResponseEntity taskClaim(@PathVariable String taskId) {
        ResponseJsonRender resultData=new ResponseJsonRender();

        taskService.claim(taskId, getLoginUser().getId());
        return new ResponseEntity(resultData, HttpStatus.OK);
    }
    /**
     * 作用办理，实际是获取业务url的过程
     * @param formUrl
     * @param taskId
     * @param taskName
     * @param taskDefKey
     * @param procInsId
     * @param procDefId
     * @param status
     * @return
     */
    @RequiresPermissions("activity:task:redirectbusinessform")
    @RequestMapping(value = "/task/{taskId}/redirectbusinessform", method = RequestMethod.GET)
    public ResponseEntity businessFormUri(String formUrl,@PathVariable String taskId,String taskName,String taskDefKey,String procInsId,String procDefId,String status){
        ResponseJsonRender resultData=new ResponseJsonRender("获取成功");
        StringBuilder formUri = new StringBuilder();

        if(StringUtils.isEmpty(formUrl)){
            // 获取流程XML上的表单KEY
            formUrl = activityTaskService.getFormKey(procDefId, taskDefKey);
        }

        formUri.append(formUrl).append(formUri.indexOf("?") == -1 ? "?" : "&");
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(procInsId).singleResult();

        Map<String,String> m = new HashedMap();
        m.put("formUrl",formUrl);
        m.put("taskId",taskId);
        m.put("taskName",taskName);
        m.put("taskDefKey",taskDefKey);
        m.put("procInsId",procInsId);
        m.put("procDefId",procDefId);
        m.put("status",status);
        m.put("businessId",processInstance.getBusinessKey());

        for(Map.Entry<String,String> entry:m.entrySet()){
            if(!"formUrl".equals(entry.getKey())){
                formUri.append(formUri.indexOf("?") == -1 ? "?" : "&").append(entry.getKey()).append("=").append(entry.getValue() != null?entry.getValue():"");
            }
        }
        m.put("businessFormUri",formUri.toString());
        resultData.setData(m);
        return new ResponseEntity(resultData, HttpStatus.OK);
    }

    /**
     * 删除任务
     * @param taskId
     * @return
     */
    @RequiresPermissions("activity:task:delete")
    @RequestMapping(value = "/task/{taskId}", method = RequestMethod.DELETE)
    public ResponseEntity taskDelete(@PathVariable String taskId,String deleteReason) {
        ResponseJsonRender resultData=new ResponseJsonRender();
        taskService.deleteTask(taskId, deleteReason);
        return new ResponseEntity(resultData, HttpStatus.OK);
    }

}
