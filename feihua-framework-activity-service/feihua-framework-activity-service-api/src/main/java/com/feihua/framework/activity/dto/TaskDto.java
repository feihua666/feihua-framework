package com.feihua.framework.activity.dto;

import java.util.Date;
import java.util.Map;

/**
 * 任务dto
 * Created by yangwei
 * Created at 2018/4/9 9:38
 */
public class TaskDto {
    private Map<String,Object> processVariables;
    private ProcessDefinitionDto processDefinition;
    private String processDefinitionId;
    private String id;
    private String name;
    private String taskDefinitionKey;
    private String processInstanceId;
    private String assignee;
    private String executionId;
    private Date createTime;
    private String status;

    public Map<String, Object> getProcessVariables() {
        return processVariables;
    }

    public void setProcessVariables(Map<String, Object> processVariables) {
        this.processVariables = processVariables;
    }

    public ProcessDefinitionDto getProcessDefinition() {
        return processDefinition;
    }

    public void setProcessDefinition(ProcessDefinitionDto processDefinition) {
        this.processDefinition = processDefinition;
    }

    public String getProcessDefinitionId() {
        return processDefinitionId;
    }

    public void setProcessDefinitionId(String processDefinitionId) {
        this.processDefinitionId = processDefinitionId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTaskDefinitionKey() {
        return taskDefinitionKey;
    }

    public void setTaskDefinitionKey(String taskDefinitionKey) {
        this.taskDefinitionKey = taskDefinitionKey;
    }

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public String getExecutionId() {
        return executionId;
    }

    public void setExecutionId(String executionId) {
        this.executionId = executionId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
