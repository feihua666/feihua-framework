package com.feihua.framework.activity.api;

import org.activiti.engine.runtime.ProcessInstance;

import java.util.Map;

/**
 * Created by yangwei
 * Created at 2018/4/9 8:51
 */
public interface ApiActivitiCommonService {
    /**
     * 启动流程
     * @param procDefKey
     * @param businessId
     * @param title
     * @param vars
     * @param userId
     * @return
     */
    public ProcessInstance startProcess(String procDefKey, String businessId, String title, Map<String, Object> vars, String userId);
    /**
     * 提交任务, 并保存意见
     * @param taskId 任务ID
     * @param procInsId 流程实例ID，如果为空，则不保存任务提交意见
     * @param comment 任务提交意见的内容
     * @param title			流程标题，显示在待办任务标题
     * @param vars 任务变量
     */
    public void complete(String taskId, String procInsId, String comment, String title, Map<String, Object> vars);
    /**
     * 完成第一个任务
     * @param procInsId
     * @param comment
     * @param title
     * @param vars
     */
    public void completeFirstTask(String userId, String procInsId, String comment, String title, Map<String, Object> vars);
    /**
     * 完成第一个任务
     * @param procInsId
     */
    public void completeFirstTask(String userId, String procInsId);
}
