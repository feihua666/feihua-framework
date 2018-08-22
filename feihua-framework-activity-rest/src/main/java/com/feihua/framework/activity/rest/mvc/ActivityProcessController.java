package com.feihua.framework.activity.rest.mvc;

import com.feihua.framework.activity.api.ApiActivitiProcessService;
import com.feihua.framework.activity.dto.ProcessDefinitionDto;
import com.feihua.framework.rest.ResponseJsonRender;
import com.feihua.framework.rest.mvc.SuperController;
import com.feihua.utils.http.httpServletResponse.ResponseCode;
import feihua.jdbc.api.pojo.BasePo;
import feihua.jdbc.api.pojo.Page;
import feihua.jdbc.api.utils.PageUtils;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.ProcessInstance;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
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
import org.springframework.web.multipart.MultipartFile;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by yw on 2017/2/4.
 */
@RestController
@RequestMapping("/activity")
public class ActivityProcessController extends SuperController {
    private static Logger logger = LoggerFactory.getLogger(ActivityProcessController.class);

    @Autowired
    private ApiActivitiProcessService activityProcessService;
    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private RuntimeService runtimeService;
    /**
     * 流程定义列表查询
     * @param category
     * @return
     */
    @RequiresPermissions("activity:process:list")
    @RequestMapping(value = "/processDefinitions", method = RequestMethod.GET)
    public ResponseEntity list( String category) {
        ResponseJsonRender resultData=new ResponseJsonRender();

        Page page  = PageUtils.getPageFromThreadLocal();
        List<ProcessDefinitionDto> list = activityProcessService.searchProcessDefinition(category,page);
        if(list == null || list.isEmpty()){
            resultData.setCode(ResponseCode.E404_100001.getCode());
            resultData.setMsg(ResponseCode.E404_100001.getMsg());
            return new ResponseEntity(resultData, HttpStatus.NOT_FOUND);
        }
        resultData.setData(list);
        return new ResponseEntity(resultData, HttpStatus.OK);
    }

    /**
     * 激活流程定义
     * @param procDefId
     * @return
     */
    @RequiresPermissions("activity:process:active")
    @RequestMapping(value = "/processDefinition/{procDefId}/active",method = RequestMethod.POST)
    public ResponseEntity activateProcessDefinition(@PathVariable String procDefId) {
        ResponseJsonRender resultData=new ResponseJsonRender();
        repositoryService.activateProcessDefinitionById(procDefId, true, null);
        return new ResponseEntity(resultData, HttpStatus.OK);
    }
    /**
     * 挂起流程定义
     * @param procDefId
     * @return
     */
    @RequiresPermissions("activity:process:suspend")
    @RequestMapping(value = "/processDefinition/{procDefId}/suspend",method = RequestMethod.POST)
    public ResponseEntity suspendProcessDefinition(@PathVariable String procDefId) {
        ResponseJsonRender resultData=new ResponseJsonRender();
        repositoryService.suspendProcessDefinitionById(procDefId, true, null);
        return new ResponseEntity(resultData, HttpStatus.OK);
    }

    /**
     * 将部署的流程转换为模型
     * @param procDefId
     * @return
     */
    @RequiresPermissions("activity:process:convertToModel")
    @RequestMapping(value = "/processDefinition/{procDefId}/convertToModel",method = RequestMethod.POST)
    public ResponseEntity convertProcessDefinitionToModel(@PathVariable String procDefId) {
        ResponseJsonRender resultData=new ResponseJsonRender();
        try {
            org.activiti.engine.repository.Model modelData = activityProcessService.convertProcessDefinitionToModel(procDefId);
            resultData.setData(modelData.getId());
        } catch (XMLStreamException e) {
            resultData.setCode(ResponseCode.E500_100000.getCode());
            resultData.setMsg("invalid xmlstream");
            return  new ResponseEntity(resultData, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (UnsupportedEncodingException e) {
            resultData.setCode(ResponseCode.E500_100000.getCode());
            resultData.setMsg("unsupportedEncoding");
            return  new ResponseEntity(resultData, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity(resultData, HttpStatus.OK);
    }
    /**
     * 删除部署的流程
     * @param deploymentId
     * @return
     */
    @RequiresPermissions("activity:process:delete")
    @RequestMapping(value = "/processDefinition/deployment/{deploymentId}",method = RequestMethod.DELETE)
    public ResponseEntity deleteProcessDefinition(@PathVariable String deploymentId) {
        ResponseJsonRender resultData=new ResponseJsonRender();
        //级联删除流程实例
        repositoryService.deleteDeployment(deploymentId, true);
        return new ResponseEntity(resultData, HttpStatus.NO_CONTENT);
    }

    /**
     * 更新分类
     * @param procDefId
     * @param category
     * @return
     */
    @RequiresPermissions("activity:process:updateCategory")
    @RequestMapping(value = "/processDefinition/{procDefId}/category",method = RequestMethod.PUT)
    public ResponseEntity updateCategoryProcessDefinition(@PathVariable String procDefId, String category) {
        ResponseJsonRender resultData=new ResponseJsonRender();
        repositoryService.setProcessDefinitionCategory(procDefId, category);
        return new ResponseEntity(resultData, HttpStatus.NO_CONTENT);
    }
    /**
     * 部署流程
     * @param deploymentFile
     * @param category
     * @return
     */
    @RequiresPermissions("activity:process:deploy")
    @RequestMapping(value = "/processDefinition/deploy",method = RequestMethod.POST)
    public ResponseEntity processDeploy( String category,MultipartFile deploymentFile) throws IOException {
        ResponseJsonRender resultData=new ResponseJsonRender();
        String processDefinitionId = activityProcessService.processDeploy(category,deploymentFile);
        if(StringUtils.isNotEmpty(processDefinitionId)){
            resultData.setData(processDefinitionId);
            return new ResponseEntity(resultData, HttpStatus.OK);
        }else {
            resultData.setMsg(ResponseCode.E400_100000.getMsg());
            resultData.setCode(ResponseCode.E400_100000.getCode());
            return new ResponseEntity(resultData, HttpStatus.BAD_REQUEST);
        }

    }

    /**
     * 流程实例列表查询
     * @param procInsId
     * @param procDefKey
     * @return
     */
    @RequiresPermissions("activity:processInstance:list")
    @RequestMapping(value = "/processInstances",method = RequestMethod.GET)
    public ResponseEntity processInstanceList( String procInsId, String procDefKey) {
        ResponseJsonRender resultData=new ResponseJsonRender();

        Page page  = PageUtils.getPageFromThreadLocal();
        List<ProcessInstance> list = activityProcessService.searchProcessInstance(procInsId,procDefKey,page);
        List<Map<String,Object>> result = new ArrayList<>();
        if(list != null && !list.isEmpty()){
            for(ProcessInstance instance:list){
                Map<String,Object> map = new HashedMap();
                map.put("id",instance.getId());
                map.put("processInstanceId",instance.getProcessInstanceId());
                map.put("processDefinitionId",instance.getProcessDefinitionId());
                map.put("activityId",instance.getActivityId());
                map.put("suspended",instance.isSuspended()? BasePo.YesNo.Y.name() : BasePo.YesNo.N.name());
                result.add(map);
            }
            resultData.setData(result);
            return new ResponseEntity(resultData, HttpStatus.OK);
        }
        resultData.setCode(ResponseCode.E404_100001.getCode());
        resultData.setMsg(ResponseCode.E404_100001.getMsg());
        return new ResponseEntity(resultData, HttpStatus.NOT_FOUND);
    }

    /**
     * 流程实例删除
     * @param procInsId
     * @param deleteReason
     * @return
     */
    @RequiresPermissions("activity:processInstance:delete")
    @RequestMapping(value = "/processInstance/{procInsId}",method = RequestMethod.DELETE)
    public ResponseEntity processInstanceDelete(@PathVariable String procInsId, String deleteReason) {
        ResponseJsonRender resultData=new ResponseJsonRender();

        runtimeService.deleteProcessInstance(procInsId, deleteReason);
        return new ResponseEntity(resultData, HttpStatus.NO_CONTENT);
    }
}
