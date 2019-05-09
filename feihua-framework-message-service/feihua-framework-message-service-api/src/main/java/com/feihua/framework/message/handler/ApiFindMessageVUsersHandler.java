package com.feihua.framework.message.handler;

import com.feihua.framework.base.modules.user.po.BaseUserPo;
import feihua.jdbc.api.service.ApiPageIterator;

import java.util.List;

/**
 * 根据消息目标获取人员
 * 类似策略模式，只会调用一个实现，按spring注册顺序
 * Created by yangwei
 * Created at 2018/11/1 19:10
 */
public interface ApiFindMessageVUsersHandler {
    /**
     * 根据消息目标获取人员
     * @param pageNo 从1开始，指定从哪一页开始
     * @param pageSize 每一页的条数
     * @param targetType 目标人类型，如office=机构下的人等，self=自定义人
     * @param targetValues 自定义目标人的值，比如userId集合
     * @return
     */
    public ApiPageIterator<String> findUsersByMessageTargets(int pageNo, int pageSize, String targetType, List<String> targetValues);

    /**
     * 是否支持该处理
     * @param vtargetType 目标人类型，如sms=短信
     * @param vtargetValues 自定义目标人的值，13521452589
     * @return
     */
    public boolean support(String vtargetType, List<String> vtargetValues);
}
