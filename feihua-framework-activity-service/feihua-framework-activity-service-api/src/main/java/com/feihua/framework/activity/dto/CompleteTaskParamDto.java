package com.feihua.framework.activity.dto;

import feihua.jdbc.api.pojo.BaseConditionDto;

import java.util.Map;

/**
 * Created by yangwei
 * Created at 2018/4/11 16:42
 */
public class CompleteTaskParamDto extends BaseConditionDto{
    private String comment;
    private String flag;
    private String taskId;
    private String taskName;
    private String taskDefKey;
    private String procInsId;
    private String procDefId;
    private String status;
    /**
     * 业务id
     */
    private String businessId;
    /**
     * 扩展参数
     */
    private Map<String,Object> addtional;

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskDefKey() {
        return taskDefKey;
    }

    public void setTaskDefKey(String taskDefKey) {
        this.taskDefKey = taskDefKey;
    }

    public String getProcInsId() {
        return procInsId;
    }

    public void setProcInsId(String procInsId) {
        this.procInsId = procInsId;
    }

    public String getProcDefId() {
        return procDefId;
    }

    public void setProcDefId(String procDefId) {
        this.procDefId = procDefId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public Map<String, Object> getAddtional() {
        return addtional;
    }

    public void setAddtional(Map<String, Object> addtional) {
        this.addtional = addtional;
    }
}
