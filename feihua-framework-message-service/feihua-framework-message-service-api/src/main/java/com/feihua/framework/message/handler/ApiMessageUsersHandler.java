package com.feihua.framework.message.handler;

import com.feihua.framework.base.modules.user.po.BaseUserPo;
import feihua.jdbc.api.service.ApiPageIterator;

import java.util.List;

/**
 * Created by yangwei
 * Created at 2018/11/1 19:10
 */
public interface ApiMessageUsersHandler {
    /**
     * 根据消息目标获取人员
     * @param pageNo 从1开始，指定从哪一页开始
     * @param pageSize 每一页的条数
     * @param targets
     * @param targetsValue
     * @return
     */
    public ApiPageIterator<BaseUserPo> findUsersByMessageTargets(int pageNo, int pageSize,String targets, List<String> targetsValue);

    /**
     * 是否支持该处理
     * @param targets
     * @return
     */
    public boolean support(String targets);
}
