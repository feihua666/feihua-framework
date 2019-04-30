package com.feihua.framework.message.handler.impl;

import com.feihua.framework.base.modules.user.po.BaseUserPo;
import com.feihua.framework.constants.DictEnum;
import com.feihua.framework.message.handler.ApiFindMessageUsersHandler;
import com.feihua.framework.message.handler.BaseUsersByMessageTargetsIterator;
import feihua.jdbc.api.service.ApiPageIterator;

import java.util.List;

/**
 * Created by yangwei
 * Created at 2018/11/2 10:38
 */
public class DefaultFindMessageUsersHandlerImpl implements ApiFindMessageUsersHandler {
    @Override
    public ApiPageIterator<BaseUserPo> findUsersByMessageTargets(int pageNo, int pageSize,String targetType, List<String> targetValues) {
        if (pageNo <= 0){
            pageNo = 1;
        }
        return new BaseUsersByMessageTargetsIterator(pageNo,pageSize,
                targetType,targetValues);
    }

    @Override
    public boolean support(String targetType, List<String> targetValues) {
        for (DictEnum.MessageTargetType messageTargets : DictEnum.MessageTargetType.values()) {
            if(messageTargets.name().equals(targetType)){
                return true;
            }
        }
        return false;
    }
}
