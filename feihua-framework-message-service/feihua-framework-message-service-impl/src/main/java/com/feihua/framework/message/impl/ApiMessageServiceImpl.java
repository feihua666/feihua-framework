package com.feihua.framework.message.impl;

import com.feihua.exception.BaseException;
import com.feihua.exception.ParamInvalidException;
import com.feihua.framework.base.modules.loginclient.api.ApiBaseLoginClientPoService;
import com.feihua.framework.base.modules.loginclient.dto.BaseLoginClientDto;
import com.feihua.framework.base.modules.loginclient.po.BaseLoginClientPo;
import com.feihua.framework.base.modules.user.api.ApiBaseUserPoService;
import com.feihua.framework.base.modules.user.po.BaseUserPo;
import com.feihua.framework.constants.DictEnum;
import com.feihua.framework.message.api.*;
import com.feihua.framework.message.dto.*;
import com.feihua.framework.message.handler.ApiClientMessageSender;
import com.feihua.framework.message.handler.ApiFindMessageUsersHandler;
import com.feihua.framework.message.handler.ApiFindMessageVUsersHandler;
import com.feihua.framework.message.handler.ApiVClientMessageSender;
import com.feihua.framework.message.po.*;
import com.feihua.utils.collection.CollectionUtils;
import com.feihua.utils.spring.SpringContextHolder;
import feihua.jdbc.api.pojo.BasePo;
import feihua.jdbc.api.service.ApiPageIterator;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;

/**
 * Created by yangwei
 * Created at 2019/4/25 17:33
 */
@Service
public class ApiMessageServiceImpl implements ApiMessageService {

    private static Logger logger = LoggerFactory.getLogger(ApiMessageServiceImpl.class);

    @Autowired
    private List<ApiClientMessageSender> apiClientMessageSenders;
    @Autowired
    private List<ApiVClientMessageSender> apiVClientMessageSenders;
    @Autowired
    private List<ApiFindMessageUsersHandler> apiMessageUsersHandlers;
    @Autowired(required = false)
    private List<ApiFindMessageVUsersHandler> apiMessageVUsersHandlers;
    @Autowired
    private ApiBaseMessageUserPoService apiBaseMessageUserPoService;
    @Autowired
    private ApiBaseMessagePoService apiBaseMessagePoService;
    @Autowired
    private ApiBaseLoginClientPoService apiBaseLoginClientPoService;
    @Autowired
    private ApiBaseMessageSendClientPoService apiBaseMessageSendClientPoService;
    @Autowired
    private ApiBaseUserPoService apiBaseUserPoService;
    @Autowired
    private ApiBaseMessageSendPoService apiBaseMessageSendPoService;

    @Autowired
    private ApiBaseMessageClientPoService apiBaseMessageClientPoService;
    @Autowired
    private ApiBaseMessageVUserPoService apiBaseMessageVUserPoService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void messageSend(final BaseMessageSendParamsDto dto, boolean asyn) {
        if (asyn) {
            Executor executor = SpringContextHolder.getBean("taskExecutor");
            if (executor != null) {
                executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        doSend(dto,false);
                    }
                });
            }else{
                throw new BaseException("bean name taskExecutor can not be found");
            }
        }else {
            doSend(dto,false);
        }
    }

    @Override
    public void messageSendByMq(BaseMessageSendParamsDto dto) {
        doSend(dto,true);
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void readMessage(String messageId, String userId,String readClientId) {
        if(StringUtils.isEmpty(readClientId)){
            throw new ParamInvalidException("readClientId can not be empty");
        }
        if (StringUtils.isAnyEmpty(messageId,userId)){
            return;
        }

        BaseMessageUserPo condition = new BaseMessageUserPo();
        condition.setMessageId(messageId);
        condition.setUserId(userId);
        condition.setDelFlag(BasePo.YesNo.N.name());

        BaseMessageUserPo param = new BaseMessageUserPo();
        param.setIsRead(BasePo.YesNo.Y.name());
        param.setReadTime(new Date());
        param.setReadClientId(readClientId);
        apiBaseMessageUserPoService.updateSelective(param, condition);
    }

    @Override
    public void readMessageWithClientCode(String messageId, String userId, String clientCode) {
        BaseLoginClientPo loginClientPo = apiBaseLoginClientPoService.selectByClientCode(clientCode);
        if (loginClientPo == null) {
            throw new ParamInvalidException("can not find  loginClient by clientCode=" + clientCode);
        }
        readMessage(messageId,userId,loginClientPo.getId());
    }


    /**
     * 实际发送
     * @param dto
     */
    private void doSend(BaseMessageSendParamsDto dto,boolean isMq){

        // 确保消息存在并在待发送状态，如果没有消息id则新添加一条消息
        BaseMessagePo baseMessagePo = getMessage(dto);

        // 待发送消息进行发送处理
        if (baseMessagePo != null && DictEnum.MessageState.to_be_sended.name().equals(baseMessagePo.getMsgState())) {
            BaseMessagePo baseMessagePoForUpdate = new BaseMessagePo();
            // 发送状态变更为发送中
            baseMessagePoForUpdate.setId(baseMessagePo.getId());
            baseMessagePoForUpdate.setMsgState(DictEnum.MessageState.sending.name());
            apiBaseMessagePoService.updateByPrimaryKeySelective(baseMessagePoForUpdate);

            // 插入消息发送参数表信息
            BaseMessageSendPo baseMessageSendPo = new BaseMessageSendPo();
            baseMessageSendPo.setMessageId(baseMessagePo.getId());
            BaseUserPo userPo = apiBaseUserPoService.selectByPrimaryKeySimple(dto.getCurrentUserId());
            baseMessageSendPo.setSendUserId(dto.getCurrentUserId());
            if (userPo != null) {
                baseMessageSendPo.setSendUserNickname(userPo.getNickname());
            }


            baseMessageSendPo = apiBaseMessageSendPoService.preInsert(baseMessageSendPo,dto.getCurrentUserId());
            apiBaseMessageSendPoService.insert(baseMessageSendPo);
            // 插入消息发送参数表信息 结束


            BaseMessageSendClientParamDto sendClientParamDto = dto.getClientParamDto();
            if (sendClientParamDto != null) {

                // 查找目标人员
                ApiFindMessageUsersHandler messageUsersHandler = resolveMessageUserHandler(sendClientParamDto.getTargetType(),sendClientParamDto.getTargetValues());
                if (messageUsersHandler != null) {
                    ApiPageIterator<BaseUserPo> userIterator =  messageUsersHandler.findUsersByMessageTargets(1,20,sendClientParamDto.getTargetType(),sendClientParamDto.getTargetValues());
                    List<BaseUserPo> userPos = null;
                    while ((userPos = userIterator.next()) != null  && !userPos.isEmpty()){
                        // 插入用户关联数据
                        this.insertMessageUser(userPos,baseMessagePo,dto.getCurrentUserId());
                        // 插入消息发送客户端关联数据
                        this.insertMessageSendClient(dto.getClientParamDto(),baseMessagePo,dto.getCurrentUserId());
                        if(isMq){
                            // 发送客户端消息
                            this.sendClientMessageMq(dto.getClientParamDto(),userPos,baseMessagePo);
                        }else {
                            // 发送客户端消息
                            this.sendClientMessage(dto.getClientParamDto(),userPos,baseMessagePo);
                        }
                    }
                }

            }

            // 虚拟客户端发送
            List<BaseMessageSendVClientParamDto> vClientParamDtos = dto.getvClientParamDtos();
            if (!CollectionUtils.isNullOrEmpty(vClientParamDtos)) {
                for (BaseMessageSendVClientParamDto vClientParamDto : vClientParamDtos) {
                    ApiFindMessageVUsersHandler messageVUsersHandler = resolveMessageVUserHandler(vClientParamDto.getVtargetType(),vClientParamDto.getVtargetValues());
                    if (messageVUsersHandler != null) {
                        ApiPageIterator<String> userIterator =  messageVUsersHandler.findUsersByMessageTargets(1,20,vClientParamDto.getVtargetType(),vClientParamDto.getVtargetValues());
                        List<String> userIdentifiers = null;
                        while ((userIdentifiers = userIterator.next()) != null  && !userIdentifiers.isEmpty()){
                            // 插入用户关联数据
                            this.insertMessageVUser(vClientParamDto.getVclient(),userIdentifiers,baseMessagePo,dto.getCurrentUserId());
                            // 插入消息发送客户端关联数据
                            this.insertMessageSendVClient(vClientParamDto,baseMessagePo,dto.getCurrentUserId());
                            if(isMq){
                                // 发送客户端消息
                                this.sendVClientMessageMq(vClientParamDto,userIdentifiers,baseMessagePo);
                            }else {
                                // 发送客户端消息
                                this.sendVClientMessage(vClientParamDto,userIdentifiers,baseMessagePo);
                            }
                        }
                    }

                }
            }


            // 变更消息状态
            // 注意： 以下消息状态变更为已发送只是标识消息实体已传递到消息处理函数中，并不能真正表明消息已送达，可能消息实体还是发送队列中，消息实体的发送还需要时间，如短信、邮件或推送
            // 如果对消息状态要求就高，这就够了，不太影响，否则建议将以下消息状态变更动作迁移到各消息实际发送处理函数中去
            // 变更消息客户端状态为已发送

            // 变更消息发送状态为已发送
            baseMessagePoForUpdate.setId(baseMessagePo.getId());
            baseMessagePoForUpdate.setMsgState(DictEnum.MessageState.sended.name());
            apiBaseMessagePoService.updateByPrimaryKeySelective(baseMessagePoForUpdate);

            // 更新客户端发送状态
            updateMessageClientStateToSended(dto.getClientParamDto(),baseMessagePo);

            // 更新虚拟客户端发送状态
            updateMessageVClientStateToSended(dto.getvClientParamDtos(),baseMessagePo);
        }
    }

    private BaseMessagePo getMessage(BaseMessageSendParamsDto dto){
        BaseMessagePo baseMessagePo = null;
        if (StringUtils.isNotEmpty(dto.getMessageId())){
            baseMessagePo = apiBaseMessagePoService.selectByPrimaryKeySimple(dto.getMessageId(),false);

            return baseMessagePo;

        }
        return baseMessagePo;
    }

    /**
     * 将消息客户端发送状态变更为已发送
     * 注意该方法与insertMessageClient 相似度极高，请注意同时修改
     * @param dto
     * @param baseMessagePo
     */
    private void updateMessageClientStateToSended(BaseMessageSendClientParamDto dto,BaseMessagePo baseMessagePo){

        BaseMessageSendClientPo messageClientPo = new BaseMessageSendClientPo();
        messageClientPo.setMessageState(DictEnum.MessageState.sended.name());

        BaseMessageSendClientPo messageClientConditionPo = null;
        // 客户端
        List<BaseLoginClientDto> clients = dto.getClients();
        if (clients != null && !clients.isEmpty()) {
            for (BaseLoginClientDto client : clients) {
                messageClientConditionPo = new BaseMessageSendClientPo();
                messageClientConditionPo.setClientId(client.getId());
                messageClientConditionPo.setMessageId(baseMessagePo.getId());
                messageClientConditionPo.setMessageState(DictEnum.MessageState.sended.name());
                apiBaseMessageSendClientPoService.updateSelective(messageClientPo,messageClientConditionPo);
            }
        }
    }
    private void updateMessageVClientStateToSended(List<BaseMessageSendVClientParamDto> dtos,BaseMessagePo baseMessagePo){

        BaseMessageSendClientPo messageClientPo = new BaseMessageSendClientPo();
        messageClientPo.setMessageState(DictEnum.MessageState.sended.name());

        BaseMessageSendClientPo messageClientConditionPo = null;
        // 客户端
        if (dtos != null && !dtos.isEmpty()) {
            for (BaseMessageSendVClientParamDto client : dtos) {
                messageClientConditionPo = new BaseMessageSendClientPo();
                messageClientConditionPo.setClientId(client.getVclient().getId());
                messageClientConditionPo.setMessageId(baseMessagePo.getId());
                messageClientConditionPo.setMessageState(DictEnum.MessageState.sended.name());
                apiBaseMessageSendClientPoService.updateSelective(messageClientPo,messageClientConditionPo);
            }
        }
    }
    private void insertMessageSendClient(BaseMessageSendClientParamDto dto, BaseMessagePo baseMessagePo,String currentUserId){

        BaseMessageSendClientPo messageClientPo = null;
        List<BaseMessageSendClientPo> messageClientPos = new ArrayList<>();
        // 客户端
        List<BaseLoginClientDto> clients = dto.getClients();
        if (clients != null && !clients.isEmpty()) {

            for (BaseLoginClientDto client : clients) {
                messageClientPo = new BaseMessageSendClientPo();
                messageClientPo.setClientId(client.getId());
                messageClientPo.setMessageId(baseMessagePo.getId());
                messageClientPo.setTargetType(dto.getTargetType());
                messageClientPo.setTargetValues(CollectionUtils.listToString(dto.getTargetValues(),","));
                messageClientPo.setMessageState(DictEnum.MessageState.sending.name());
                messageClientPo = apiBaseMessageSendClientPoService.preInsert(messageClientPo,currentUserId);
                messageClientPos.add(messageClientPo);
            }
        }

        if (!messageClientPos.isEmpty()) {

            apiBaseMessageSendClientPoService.insertBatch(messageClientPos);
        }

    }
    private void insertMessageSendVClient(BaseMessageSendVClientParamDto dto, BaseMessagePo baseMessagePo,String currentUserId){

        BaseMessageSendClientPo messageClientPo = null;
        // 客户端
        BaseLoginClientDto clients = dto.getVclient();
        messageClientPo = new BaseMessageSendClientPo();
        messageClientPo.setClientId(dto.getVclient().getId());
        messageClientPo.setMessageId(baseMessagePo.getId());
        messageClientPo.setTargetType(dto.getVtargetType());
        messageClientPo.setTargetValues(CollectionUtils.listToString(dto.getVtargetValues(),","));
        messageClientPo.setMessageState(DictEnum.MessageState.sending.name());
        messageClientPo = apiBaseMessageSendClientPoService.preInsert(messageClientPo,currentUserId);
        apiBaseMessageSendClientPoService.insert(messageClientPo);

    }
    private void insertMessageUser(List<BaseUserPo> userPos,BaseMessagePo baseMessagePo,String currentUserId){
        BaseMessageUserPo messageUserPo = null;
        if (userPos != null && !userPos.isEmpty()) {
            List<BaseMessageUserPo> userStatePos = new ArrayList<>(userPos.size());
            for (BaseUserPo userPo : userPos) {
                messageUserPo = new BaseMessageUserPo();
                messageUserPo.setMessageId(baseMessagePo.getId());
                messageUserPo.setIsCanRead(BasePo.YesNo.Y.name());
                messageUserPo.setUserId(userPo.getId());
                messageUserPo.setIsRead(BasePo.YesNo.N.name());
                messageUserPo = apiBaseMessageUserPoService.preInsert(messageUserPo,currentUserId);
                userStatePos.add(messageUserPo);
            }
            apiBaseMessageUserPoService.insertBatch(userStatePos);
        }

    }
    private void insertMessageVUser(BaseLoginClientDto clientDto,List<String> userIdentifiers,BaseMessagePo baseMessagePo,String currentUserId){
        BaseMessageVUserPo messagVeUserPo = null;
        if (userIdentifiers != null && !userIdentifiers.isEmpty()) {
            List<BaseMessageVUserPo> userStatePos = new ArrayList<>(userIdentifiers.size());
            for (String userIdentifier : userIdentifiers) {
                messagVeUserPo = new BaseMessageVUserPo();
                messagVeUserPo.setMessageId(baseMessagePo.getId());
                messagVeUserPo.setIdentifier(userIdentifier);
                messagVeUserPo.setClientId(clientDto.getId());
                messagVeUserPo.setMessageState(DictEnum.MessageState.sending.name());
                messagVeUserPo = apiBaseMessageVUserPoService.preInsert(messagVeUserPo,currentUserId);
                userStatePos.add(messagVeUserPo);
            }
            apiBaseMessageVUserPoService.insertBatch(userStatePos);
        }

    }
    /**
     * mq方式发送
     * @param dto
     * @param userPos
     * @param baseMessagePo
     */
    private void sendClientMessageMq(BaseMessageSendClientParamDto dto,List<BaseUserPo> userPos,BaseMessagePo baseMessagePo){
        // 暂未实现
    }
    private void sendVClientMessageMq(BaseMessageSendVClientParamDto dto,List<String> userIdentifiers,BaseMessagePo baseMessagePo){
        // 暂未实现
    }
    private void sendClientMessage(BaseMessageSendClientParamDto dto,List<BaseUserPo> userPos,BaseMessagePo baseMessagePo) {
        if (dto.getClients() != null) {
            for (BaseLoginClientDto clientDto : dto.getClients()) {
                List<BaseMessageClientDto> messageClientDtos = apiBaseMessageClientPoService.selectByClientIdAndIsEnable(clientDto.getId(),BasePo.YesNo.Y.name());
                for (ApiClientMessageSender clientMessageSender : apiClientMessageSenders) {
                    if (clientMessageSender.support(clientDto,messageClientDtos)){
                        ClientMessageSendParamDto clientMessageSendParamDto = new ClientMessageSendParamDto();
                        clientMessageSendParamDto.setBaseMessagePo(baseMessagePo);
                        clientMessageSendParamDto.setClientDto(clientDto);
                        clientMessageSendParamDto.setUserPos(userPos);
                        try {
                            clientMessageSender.doMessageSend(clientMessageSendParamDto);
                        }catch (Exception e) {
                            logger.error(e.getMessage() + " 客户端名称={}",e,clientDto.getName());
                        }
                    }
                }
            }
        }
    }
    private void sendVClientMessage(BaseMessageSendVClientParamDto dto,List<String> userIdentifiers,BaseMessagePo baseMessagePo) {
        List<BaseMessageClientDto> messageClientDtos = apiBaseMessageClientPoService.selectByClientIdAndIsEnable(dto.getVclient().getId(),BasePo.YesNo.Y.name());

        for (ApiVClientMessageSender clientMessageSender : apiVClientMessageSenders) {
            if (clientMessageSender.support(dto.getVclient(),messageClientDtos)){
                VClientMessageSendParamDto clientMessageSendParamDto = new VClientMessageSendParamDto();
                clientMessageSendParamDto.setBaseMessagePo(baseMessagePo);
                clientMessageSendParamDto.setClientDto(dto.getVclient());
                clientMessageSendParamDto.setUserIdentifiers(userIdentifiers);
                try {

                    clientMessageSender.doMessageSend(clientMessageSendParamDto);
                }catch (Exception e) {
                    logger.error(e.getMessage() + " 客户端名称={}",e,dto.getVclient().getName());
                }
            }
        }


    }
    /**
     * 获取发送目标处理服务
     * @param targetType
     * @param targetValues
     * @return
     */
    private ApiFindMessageUsersHandler resolveMessageUserHandler(String targetType, List<String> targetValues){
        // 查找目标人
        for (ApiFindMessageUsersHandler apiMessageUsersHandler : apiMessageUsersHandlers) {
            if(apiMessageUsersHandler.support(targetType, targetValues)){
                return apiMessageUsersHandler;
            }
        }
        return null;
    }
    private ApiFindMessageVUsersHandler resolveMessageVUserHandler(String vtargetType, List<String> vtargetValues){
        // 查找目标人
        for (ApiFindMessageVUsersHandler apiMessageVUsersHandler : apiMessageVUsersHandlers) {
            if(apiMessageVUsersHandler.support(vtargetType, vtargetValues)){
                return apiMessageVUsersHandler;
            }
        }
        return null;
    }
}
