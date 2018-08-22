package com.feihua.framework.activity.api;

import com.feihua.framework.activity.dto.TaskDto;
import feihua.jdbc.api.pojo.Page;

import java.util.Date;
import java.util.List;

/**
 * Created by yangwei
 * Created at 2018/4/9 10:53
 */
public interface ApiActivitiTaskService {

    /**
     * 待办
     */
    public static String TASK_STATUS_TODO = "todo";
    /**
     * 待签收
     */
    public static String TASK_STATUS_CLAIM = "claim";
    /**
     * 获取待办列表
     * @param procDefKey 流程定义标识
     * @param startTime
     * @param endTime
     * @param page
     * @param userId
     * @return
     */
    public List<TaskDto> searchTaskTodoListNativeSql(String procDefKey, Date startTime, Date endTime, Page page, String userId);

    /**
     * 获取待办列表
     * @param procDefKey 流程定义标识
     * @param startTime
     * @param endTime
     * @param page
     * @param userId
     * @return
     */
    public List<TaskDto> searchTaskTodoList(String procDefKey, Date startTime, Date endTime, Page page, String userId);

    /**
     * 获取待签收列表
     * @param procDefKey 流程定义标识
     * @param startTime
     * @param endTime
     * @param page
     * @param userId
     * @return
     */
    public List<TaskDto> searchTaskToClaimList(String procDefKey, Date startTime, Date endTime, Page page, String userId);

    /**
     * 获取流程表单（首先获取任务节点表单KEY，如果没有则取流程开始节点表单KEY）
     * @param procDefId
     * @param taskDefKey
     * @return
     */
    public String getFormKey(String procDefId, String taskDefKey);
}
