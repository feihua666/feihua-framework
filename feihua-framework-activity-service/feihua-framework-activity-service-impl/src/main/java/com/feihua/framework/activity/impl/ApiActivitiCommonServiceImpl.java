package com.feihua.framework.activity.impl;

import com.feihua.framework.activity.ActUtils;
import com.feihua.framework.activity.api.ApiActivitiCommonService;
import com.feihua.framework.mybatis.orm.mapper.NativeSqlMapper;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * Created by yw on 2017/3/21.
 */
@Service
public class ApiActivitiCommonServiceImpl implements ApiActivitiCommonService {

    @Autowired
    private IdentityService identityService;

    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private NativeSqlMapper nativeSqlMapper;

    public ProcessInstance startProcess(String procDefKey,String businessId, String title, Map<String, Object> vars,String userId){

        // 用来设置启动流程的人员ID，引擎会自动把用户ID保存到activiti:initiator中
        identityService.setAuthenticatedUserId(userId+"");

        // 设置流程变量
        if (vars == null){
            vars = new HashedMap();
        }

        // 设置流程标题
        if (StringUtils.isNotBlank(title)){
            vars.put("title", title);
        }

        // 启动流程
        ProcessInstance procIns = runtimeService.startProcessInstanceByKey(procDefKey, businessId + "", vars);
        return procIns;
    }

    /**
     * 提交任务, 并保存意见
     * @param taskId 任务ID
     * @param procInsId 流程实例ID，如果为空，则不保存任务提交意见
     * @param comment 任务提交意见的内容
     * @param title			流程标题，显示在待办任务标题
     * @param vars 任务变量
     */
    @Transactional(readOnly = false)
    public void complete(String taskId, String procInsId, String comment, String title, Map<String, Object> vars){
        // 添加意见
        if (StringUtils.isNotBlank(procInsId) && StringUtils.isNotBlank(comment)){
            taskService.addComment(taskId, procInsId, comment);
        }
        // 设置流程变量
        if (vars == null){
            vars = new HashedMap();
        }
        // 设置流程标题
        if (StringUtils.isNotBlank(title)){
            vars.put("title", title);
        }
        // 提交任务
        taskService.complete(taskId, vars);

        //清除
        deleteNgIdentitylink();
    }

    /**
     * 完成第一个任务
     * @param procInsId
     */
    public void completeFirstTask(String userId,String procInsId){
        completeFirstTask(userId,procInsId, null, null, null);
    }

    /**
     * 完成第一个任务
     * @param procInsId
     * @param comment
     * @param title
     * @param vars
     */
    public void completeFirstTask(String userId,String procInsId, String comment, String title, Map<String, Object> vars){
        Task task = taskService.createTaskQuery().taskAssignee(userId ).processInstanceId(procInsId).active().singleResult();
        if (task != null){
            complete(task.getId(), procInsId, comment, title, vars);
        }
    }
    private void deleteNgIdentitylink(){
        nativeSqlMapper.deleteByNativeSql("update act_hi_actinst set ASSIGNEE_ = null where ASSIGNEE_='" + ActUtils.VARIABLE_VALUE_NG+"'");
        nativeSqlMapper.deleteByNativeSql("update act_hi_taskinst set ASSIGNEE_ = null where ASSIGNEE_='" + ActUtils.VARIABLE_VALUE_NG+"'");
        nativeSqlMapper.deleteByNativeSql("update act_ru_task set ASSIGNEE_ = null where ASSIGNEE_='" + ActUtils.VARIABLE_VALUE_NG+"'");
        nativeSqlMapper.deleteByNativeSql("delete from act_hi_varinst where text_ ='" + ActUtils.VARIABLE_VALUE_NG+"'");
        nativeSqlMapper.deleteByNativeSql("delete from act_ru_variable where text_ ='" + ActUtils.VARIABLE_VALUE_NG+"'");
        nativeSqlMapper.deleteByNativeSql("delete from act_hi_identitylink where group_id_ ='" + ActUtils.VARIABLE_VALUE_NG+"' or user_id_='"+ ActUtils.VARIABLE_VALUE_NG+"'");
        nativeSqlMapper.deleteByNativeSql("delete from act_ru_identitylink where group_id_ ='" + ActUtils.VARIABLE_VALUE_NG+"' or user_id_='"+ActUtils.VARIABLE_VALUE_NG+"'");
    }

}
