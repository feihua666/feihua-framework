package com.feihua.framework.activity.api;

import com.feihua.framework.activity.dto.AdditionalParam;
import com.feihua.framework.activity.dto.ApplyParamDto;
import com.feihua.framework.activity.dto.CompleteTaskParamDto;

/**
 * 业务接口
 * Created by yangwei
 * Created at 2018/4/9 11:03
 */
public interface ApiActivitiBusinessService {

    /**
     * 流程启动后调用,更新业务的流程实例id
     * @param procInsId
     * @param BusinessId
     */
    public void updateProcInsIdByBusinessId(String procInsId, String BusinessId, AdditionalParam param);

    /**
     * 流程启动后调用，一般用户来设置审批人
     * @param procInsId
     * @param BusinessId
     */
    public void onStartProcessComplete(String procInsId, String BusinessId, AdditionalParam param);

    /**
     * 申请，启动流程
     * @param applyParamDto
     */
    public void apply(ApplyParamDto applyParamDto);

    /**
     * 完成任务节点，审核
     * @param completeTaskParamDto
     */
    public void completeTask(CompleteTaskParamDto completeTaskParamDto);
}
