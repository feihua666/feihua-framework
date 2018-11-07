package com.feihua.framework.message.handler.impl;

import com.feihua.framework.base.modules.user.po.BaseUserPo;
import com.feihua.framework.constants.DictEnum;
import com.feihua.framework.message.handler.ApiMessageUsersHandler;
import com.feihua.framework.message.handler.BaseUsersByMessageTargetsIterator;
import feihua.jdbc.api.service.ApiPageIterator;

import java.util.List;

/**
 * Created by yangwei
 * Created at 2018/11/2 10:38
 */
public class MessageUsersHandlerImpl implements ApiMessageUsersHandler {
    @Override
    public ApiPageIterator<BaseUserPo> findUsersByMessageTargets(int pageNo, int pageSize,String targets, List<String> targetsValue) {
        if (pageNo <= 0){
            pageNo = 1;
        }
        return new BaseUsersByMessageTargetsIterator(pageNo,pageSize,
                targets,targetsValue);
    }

    @Override
    public boolean support(String targets) {
        for (DictEnum.MessageTargets messageTargets : DictEnum.MessageTargets.values()) {
            if(messageTargets.name().equals(targets)){
                return true;
            }
        }
        return false;
    }
}
