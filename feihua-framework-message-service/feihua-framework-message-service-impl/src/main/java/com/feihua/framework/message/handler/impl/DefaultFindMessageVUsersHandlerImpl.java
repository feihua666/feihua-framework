package com.feihua.framework.message.handler.impl;

import com.feihua.framework.base.modules.user.po.BaseUserPo;
import com.feihua.framework.constants.DictEnum;
import com.feihua.framework.message.handler.ApiFindMessageUsersHandler;
import com.feihua.framework.message.handler.ApiFindMessageVUsersHandler;
import com.feihua.framework.message.handler.BaseUsersByMessageTargetsIterator;
import com.feihua.framework.message.handler.BaseVUsersByMessageTargetsIterator;
import feihua.jdbc.api.service.ApiPageIterator;

import java.util.List;

/**
 * Created by yangwei
 * Created at 2018/11/2 10:38
 */
public class DefaultFindMessageVUsersHandlerImpl implements ApiFindMessageVUsersHandler {
    @Override
    public ApiPageIterator<String> findUsersByMessageTargets(int pageNo, int pageSize,String targetType, List<String> targetValues) {
        if (pageNo <= 0){
            pageNo = 1;
        }
        return new BaseVUsersByMessageTargetsIterator(pageNo,pageSize,
                targetType,targetValues);
    }

    @Override
    public boolean support(String targetType, List<String> targetValues) {
        return true;
    }
}
