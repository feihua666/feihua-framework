package com.feihua.framework.message.handler.impl;

import com.feihua.exception.BaseException;
import com.feihua.framework.base.modules.user.po.BaseUserPo;
import com.feihua.framework.message.api.ApiBaseMessageTargetClientPoService;
import com.feihua.framework.message.api.ApiBaseMessageTargetClientUserRelPoService;
import com.feihua.framework.message.dto.BaseMessageSendParamsDto;
import com.feihua.framework.message.dto.BaseMessageTargetClientParamsDto;
import com.feihua.framework.message.handler.ApiMessageSendHandler;
import com.feihua.framework.constants.DictEnum;
import com.feihua.framework.message.po.BaseMessagePo;
import com.feihua.framework.message.po.BaseMessageTargetClientPo;
import com.feihua.framework.message.po.BaseMessageTargetClientUserRelPo;
import feihua.jdbc.api.pojo.BasePo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by yangwei
 * Created at 2018/11/1 19:39
 */
public abstract class ApiMessageSendHandlerAbstractImpl  implements ApiMessageSendHandler {


    private static final Logger logger = LoggerFactory.getLogger(ApiMessageSendHandlerAbstractImpl.class);

    private DictEnum.LoginClient loginClient;
    @Autowired
    private ApiBaseMessageTargetClientPoService apiBaseMessageTargetClientPoService;
    @Autowired
    private ApiBaseMessageTargetClientUserRelPoService apiBaseMessageTargetClientUserRelPoService;

    public ApiMessageSendHandlerAbstractImpl(DictEnum.LoginClient loginClient){
        this.loginClient = loginClient;
    }


    public boolean support(BaseMessageTargetClientParamsDto dto){
        if(loginClient == null){
            return false;
        }
        return loginClient.name().equals(dto.getTargetClient()) ? true : false;
    }


    @Transactional(rollbackFor = Exception.class)
    public BaseMessageTargetClientPo addBaseMessageTargetClientPo(BaseMessageTargetClientParamsDto dto, BaseMessagePo baseMessagePo, DictEnum.MessageState messageState) {
        // 检查是否存在
        BaseMessageTargetClientPo baseMessageTargetClientPoCheck = new BaseMessageTargetClientPo();
        baseMessageTargetClientPoCheck.setMessageId(baseMessagePo.getId());
        baseMessageTargetClientPoCheck.setDelFlag(BasePo.YesNo.N.name());
        baseMessageTargetClientPoCheck.setTargetClient(dto.getTargetClient());
        baseMessageTargetClientPoCheck.setSubTargetClient(dto.getSubTargetClient());
        BaseMessageTargetClientPo baseMessageTargetClientPoDb = apiBaseMessageTargetClientPoService.selectOneSimple(baseMessageTargetClientPoCheck);
        if (baseMessageTargetClientPoDb != null) {
            return baseMessageTargetClientPoDb;
        }


        BaseMessageTargetClientPo baseMessageTargetClientPo = new BaseMessageTargetClientPo();
        baseMessageTargetClientPo.setMessageId(baseMessagePo.getId());
        baseMessageTargetClientPo.setMessageState(messageState.name());

        baseMessageTargetClientPo.setTargetClient(dto.getTargetClient());
        baseMessageTargetClientPo.setSubTargetClient(dto.getSubTargetClient());
        apiBaseMessageTargetClientPoService.preInsert(baseMessageTargetClientPo,dto.getCurrentUserId());
        baseMessageTargetClientPo = apiBaseMessageTargetClientPoService.insertSelectiveSimple(baseMessageTargetClientPo);
        return baseMessageTargetClientPo;
    }
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int updateMessageTargetClientState(BaseMessageTargetClientParamsDto dto, BaseMessagePo baseMessagePo, DictEnum.MessageState messageState) {
        // 检查是否存在
        BaseMessageTargetClientPo baseMessageTargetClientPoCheck = new BaseMessageTargetClientPo();
        baseMessageTargetClientPoCheck.setMessageId(baseMessagePo.getId());
        baseMessageTargetClientPoCheck.setDelFlag(BasePo.YesNo.N.name());
        baseMessageTargetClientPoCheck.setTargetClient(dto.getTargetClient());
        baseMessageTargetClientPoCheck.setSubTargetClient(dto.getSubTargetClient());
        BaseMessageTargetClientPo baseMessageTargetClientPoDb = apiBaseMessageTargetClientPoService.selectOneSimple(baseMessageTargetClientPoCheck);
        if (baseMessageTargetClientPoDb != null) {
            return updateMessageTargetClientState(baseMessageTargetClientPoDb,messageState);
        }
        return 0;
    }
    @Transactional(rollbackFor = Exception.class)
    public int updateMessageTargetClientState(BaseMessageTargetClientPo targetClientPo, DictEnum.MessageState messageState){
        if (targetClientPo != null) {
            BaseMessageTargetClientPo targetClientPoForUpdate = new BaseMessageTargetClientPo();
            targetClientPoForUpdate.setId(targetClientPo.getId());
            targetClientPoForUpdate.setMessageState(messageState.name());
            return apiBaseMessageTargetClientPoService.updateByPrimaryKeySelective(targetClientPoForUpdate);
        }
        return 0;
    }
    @Override
    public void doMessageSend(BaseMessageTargetClientParamsDto dto, BaseMessagePo baseMessagePo, List<BaseUserPo> userPos) {

        BaseMessageTargetClientPo baseMessageTargetClientPoCheck = new BaseMessageTargetClientPo();
        baseMessageTargetClientPoCheck.setMessageId(baseMessagePo.getId());
        baseMessageTargetClientPoCheck.setDelFlag(BasePo.YesNo.N.name());
        baseMessageTargetClientPoCheck.setTargetClient(dto.getTargetClient());
        baseMessageTargetClientPoCheck.setSubTargetClient(dto.getSubTargetClient());
        BaseMessageTargetClientPo baseMessageTargetClientPoDb = apiBaseMessageTargetClientPoService.selectOneSimple(baseMessageTargetClientPoCheck);
        if (baseMessageTargetClientPoDb == null) {
            throw new BaseException("can not find targetClient data from database by targetClien=" + dto.getTargetClient() + " subTargetClient=" + dto.getSubTargetClient());
        }
        BaseMessageTargetClientUserRelPo baseMessageTargetClientUserRelPo = null;


        for (BaseUserPo userPo : userPos) {

            baseMessageTargetClientUserRelPo = new BaseMessageTargetClientUserRelPo();
            baseMessageTargetClientUserRelPo.setIsRead(BasePo.YesNo.N.name());
            baseMessageTargetClientUserRelPo.setTargetClientId(baseMessageTargetClientPoDb.getId());
            baseMessageTargetClientUserRelPo.setUserId(userPo.getId());
            apiBaseMessageTargetClientUserRelPoService.preInsert(baseMessageTargetClientUserRelPo,dto.getCurrentUserId());
            apiBaseMessageTargetClientUserRelPoService.insert(baseMessageTargetClientUserRelPo);

            doMessageSend(dto,baseMessagePo,userPo);
        }

    }
    public abstract void doMessageSend(BaseMessageTargetClientParamsDto dto, BaseMessagePo baseMessagePo, BaseUserPo userPo);
    public DictEnum.LoginClient getLoginClient() {
        return loginClient;
    }
}
