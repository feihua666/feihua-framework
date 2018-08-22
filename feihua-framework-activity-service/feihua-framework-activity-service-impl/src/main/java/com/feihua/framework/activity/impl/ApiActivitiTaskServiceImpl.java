package com.feihua.framework.activity.impl;

import com.feihua.framework.activity.ActUtils;
import com.feihua.framework.activity.api.ApiActivitiTaskService;
import com.feihua.framework.activity.dto.ProcessDefinitionDto;
import com.feihua.framework.activity.dto.TaskDto;
import feihua.jdbc.api.pojo.Page;
import feihua.jdbc.api.utils.PageUtils;
import org.activiti.engine.FormService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.TaskService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.impl.persistence.entity.GroupEntityManager;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.NativeTaskQuery;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by yw on 2017/3/15.
 */
@Service
@Transactional(readOnly = true)
public class ApiActivitiTaskServiceImpl implements ApiActivitiTaskService{
    /**
     * 日志对象
     */
    protected Logger logger = LoggerFactory.getLogger(ApiActivitiTaskServiceImpl.class);
    @Autowired
    private GroupEntityManager customGroupEntityManager;

    @Autowired
    private TaskService taskService;
    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private FormService formService;
    /**
     * 获取待办列表
     * @param procDefKey 流程定义标识
     * @return
     */
    public List<TaskDto> searchTaskTodoListNativeSql(String procDefKey, Date startTime, Date endTime,Page page,String userId){


        String sql = "SELECT\n" +
                "\tRES.*, VAR.ID_ AS VAR_ID_,\n" +
                "\tVAR.NAME_ AS VAR_NAME_,\n" +
                "\tVAR.TYPE_ AS VAR_TYPE_,\n" +
                "\tVAR.REV_ AS VAR_REV_,\n" +
                "\tVAR.PROC_INST_ID_ AS VAR_PROC_INST_ID_,\n" +
                "\tVAR.EXECUTION_ID_ AS VAR_EXECUTION_ID_,\n" +
                "\tVAR.TASK_ID_ AS VAR_TASK_ID_,\n" +
                "\tVAR.BYTEARRAY_ID_ AS VAR_BYTEARRAY_ID_,\n" +
                "\tVAR.DOUBLE_ AS VAR_DOUBLE_,\n" +
                "\tVAR.TEXT_ AS VAR_TEXT_,\n" +
                "\tVAR.TEXT2_ AS VAR_TEXT2_,\n" +
                "\tVAR.LONG_ AS VAR_LONG_\n" +
                "FROM\n" +
                "\tACT_RU_TASK RES\n" +
                "LEFT OUTER JOIN ACT_RU_VARIABLE VAR ON RES.PROC_INST_ID_ = VAR.EXECUTION_ID_\n" +
                "AND VAR.TASK_ID_ IS NULL\n" +
                "left JOIN ACT_RU_IDENTITYLINK I_OR0 ON I_OR0.TASK_ID_ = RES.ID_\n" +
                "#{processDefinitionjoinTable}\n" +
                "WHERE\n" +
                "\tRES.SUSPENSION_STATE_ = 1\n" +
                "\t#{processDefinitionjoinCondition}\n" +
                "AND (\n" +
                "\tRES.ASSIGNEE_ = #{userId}\n" +
                "\tOR (\n" +
                "\t\tRES.ASSIGNEE_ IS NULL\n" +
                "\t\tAND I_OR0.TYPE_ = 'candidate'\n" +
                "\t\tAND (\n" +
                "\t\t\tI_OR0.USER_ID_ = #{userId}\n" +
                "\t\t\t#{groupIdsIn}\n" +
                "\t\t)\n" +
                "\t)\n" +
                ")\n" +
                "#{startTime}\n" +
                "#{endTime}\n" +
                "ORDER BY\n" +
                "\tRES.CREATE_TIME_ DESC";


        NativeTaskQuery todoTaskQuery = taskService.createNativeTaskQuery();
        NativeTaskQuery todoTaskQuerycount = taskService.createNativeTaskQuery();


        // 设置查询条件
        //用户及候选人条件
        sql = sql.replace("#{userId}",userId+"");
        //角色

        List<Group> groups = customGroupEntityManager.findGroupsByUser(userId);
        if(groups != null && !groups.isEmpty()){
            StringBuffer sb = new StringBuffer(" OR I_OR0.GROUP_ID_ IN (");
            for (int i = 0; i < groups.size(); i++) {
                Group group = groups.get(i);
                sb.append("'" + group.getId() + "'");
                if(i < groups.size()-1){
                    sb.append(",");
                }
            }

            sb.append(")");
            sql = sql.replace("#{groupIdsIn}",sb.toString());
        }
        if (StringUtils.isNotBlank(procDefKey)){
            sql = sql.replace("#{processDefinitionjoinTable}"," INNER JOIN ACT_RE_PROCDEF D ON RES.PROC_DEF_ID_ = D.ID_");
            sql = sql.replace("#{processDefinitionjoinCondition}"," and D.KEY_ = #{procDefKey}");
            todoTaskQuery.parameter("procDefKey",procDefKey);
            todoTaskQuerycount.parameter("procDefKey",procDefKey);
        }else {
            sql = sql.replace("#{processDefinitionjoinTable}","");
            sql = sql.replace("#{processDefinitionjoinCondition}","");
        }
        if (startTime != null){
            sql = sql.replace("#{startTime}"," and RES.CREATE_TIME_ >= #{startTime}");
            todoTaskQuery.parameter("startTime",startTime);
            todoTaskQuerycount.parameter("startTime",startTime);
        }else {
            sql = sql.replace("#{startTime}","");
        }
        if (endTime != null){
            sql = sql.replace("#{endTime}"," and RES.CREATE_TIME_ <= #{endTime}");
            todoTaskQuery.parameter("endTime",endTime);
            todoTaskQuerycount.parameter("endTime",endTime);
        }else {
            sql = sql.replace("#{endTime}","");
        }
        todoTaskQuery.sql(sql);
        todoTaskQuerycount.sql("select count(distinct id_) from ("+sql+") t");
        List<Task> taskList = null;
        // =============== 已经签收或待签收的任务  ===============
        /*TaskQuery todoTaskQuery = taskService.createTaskQuery().or().taskAssignee(su.getId()+"").taskCandidateUser(su.getId()+"").taskCandidateGroupIn(roleIds).endOr()
                .active().includeProcessVariables().orderByTaskCreateTime().desc();*/

        taskList = ActUtils.queryListPage(todoTaskQuery,page,todoTaskQuerycount);

        return wrapTaskList(taskList,"");
    }
    /**
     * 获取待办列表
     * @param procDefKey 流程定义标识
     * @return
     */
    public List<TaskDto> searchTaskTodoList(String procDefKey, Date startTime, Date endTime,Page page,String userId){

        List<Task> taskList = null;


        // =============== 已经签收的任务  ===============
        TaskQuery todoTaskQuery = taskService.createTaskQuery().taskAssignee(userId+"").active()
                .includeProcessVariables().orderByTaskCreateTime().desc();

        // 设置查询条件
        if (StringUtils.isNotBlank(procDefKey)){
            todoTaskQuery.processDefinitionKey(procDefKey);
        }
        if (startTime != null){
            todoTaskQuery.taskCreatedAfter(startTime);
        }
        if (endTime != null){
            todoTaskQuery.taskCreatedBefore(endTime);
        }
        if(page == null || !page.isPageable()){
            taskList = todoTaskQuery.list();
        }else {
            page.setDataNum((int) todoTaskQuery.count());
            taskList = todoTaskQuery.listPage(PageUtils.getFirstOffset(page), page.getPageSize());
        }

        return wrapTaskList(taskList,TASK_STATUS_TODO);
    }

    /**
     * 获取待签收列表
     * @param procDefKey 流程定义标识
     * @return
     */
    public List<TaskDto> searchTaskToClaimList(String procDefKey, Date startTime, Date endTime,Page page,String userId){

        List<Task> taskList = null;
        //角色
        List<String> roleIds = new ArrayList<>();
        List<Group> groups = customGroupEntityManager.findGroupsByUser(userId);
        if(groups != null && !groups.isEmpty()){
            for (Group group : groups) {
                roleIds.add(group.getId());
            }
        }

        // =============== 等待签收的任务  ===============
        TaskQuery toClaimQuery = taskService.createTaskQuery().or().taskCandidateUser(userId+"").taskCandidateGroupIn(roleIds).endOr()
                .includeProcessVariables().active().orderByTaskCreateTime().desc();
        // 设置查询条件
        if (StringUtils.isNotBlank(procDefKey)){
            toClaimQuery.processDefinitionKey(procDefKey);
        }
        if (startTime != null){
            toClaimQuery.taskCreatedAfter(startTime);
        }
        if (endTime != null){
            toClaimQuery.taskCreatedBefore(endTime);
        }
        if(page == null || !page.isPageable()){
            taskList = toClaimQuery.list();
        }else {
            page.setDataNum((int) toClaimQuery.count());
            taskList = toClaimQuery.listPage(PageUtils.getFirstOffset(page), page.getPageSize());
        }


        return wrapTaskList(taskList,TASK_STATUS_CLAIM);
    }

    /**
     * 获取流程表单（首先获取任务节点表单KEY，如果没有则取流程开始节点表单KEY）
     * @return
     */
    public String getFormKey(String procDefId, String taskDefKey){
        String formKey = "";
        if (StringUtils.isNotBlank(procDefId)){
            if (StringUtils.isNotBlank(taskDefKey)){
                try{
                    formKey = formService.getTaskFormKey(procDefId, taskDefKey);
                }catch (Exception e) {
                    formKey = "";
                }
            }
            if (StringUtils.isBlank(formKey)){
                formKey = formService.getStartFormKey(procDefId);
            }
        }
        logger.debug("getFormKey: {}", formKey);
        return formKey;
    }

    private List<TaskDto>  wrapTaskList(List<Task> taskList,String status){
        List<TaskDto> result = new ArrayList<>();
        for (Task task : taskList) {
            TaskDto taskDto = new TaskDto();
            ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(task.getProcessDefinitionId()).singleResult();
            taskDto.setProcessVariables(task.getProcessVariables());

            ProcessDefinitionDto processDefinitionDto = new ProcessDefinitionDto();
            processDefinitionDto.setId(processDefinition.getId());
            processDefinitionDto.setName(processDefinition.getName());
            processDefinitionDto.setVersion(processDefinition.getVersion());
            taskDto.setProcessDefinition(processDefinitionDto);

            taskDto.setProcessDefinitionId(processDefinition.getId());
            taskDto.setId(task.getId());
            taskDto.setName(task.getName());
            taskDto.setTaskDefinitionKey(task.getTaskDefinitionKey());
            taskDto.setProcessInstanceId(task.getProcessInstanceId());
            taskDto.setAssignee(task.getAssignee());
            taskDto.setExecutionId(task.getExecutionId());
            taskDto.setCreateTime(task.getCreateTime());
            taskDto.setStatus(status);
            result.add(taskDto);
        }
        return result;
    }
}
