package com.feihua.framework.message.impl;

import com.feihua.exception.BaseException;
import com.feihua.exception.ParamInvalidException;
import com.feihua.framework.base.modules.loginclient.api.ApiBaseLoginClientChannelBindPoService;
import com.feihua.framework.base.modules.loginclient.api.ApiBaseLoginClientPoService;
import com.feihua.framework.base.modules.loginclient.dto.BaseLoginClientDto;
import com.feihua.framework.base.modules.loginclient.po.BaseLoginClientChannelBindPo;
import com.feihua.framework.base.modules.loginclient.po.BaseLoginClientPo;
import com.feihua.framework.base.modules.user.api.ApiBaseUserPoService;
import com.feihua.framework.base.modules.user.po.BaseUserPo;
import com.feihua.framework.constants.DictEnum;
import com.feihua.framework.message.MsgTemplateUtils;
import com.feihua.framework.message.api.*;
import com.feihua.framework.message.dto.BaseMessageSendParamsDto;
import com.feihua.framework.message.dto.ClientMessageSendParamDto;
import com.feihua.framework.message.dto.MsgTemplateThirdParamDto;
import com.feihua.framework.message.handler.ApiClientMessageSender;
import com.feihua.framework.message.handler.ApiFindMessageUsersHandler;
import com.feihua.framework.message.po.*;
import com.feihua.utils.collection.CollectionUtils;
import com.feihua.utils.spring.SpringContextHolder;
import feihua.jdbc.api.pojo.BasePo;
import feihua.jdbc.api.service.ApiPageIterator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.Executor;

/**
 * Created by yangwei
 * Created at 2019/4/25 17:33
 */
@Service
public class ApiMessageServiceImpl implements ApiMessageService {


    @Autowired
    private List<ApiClientMessageSender> apiClientMessageSenders;
    @Autowired
    private List<ApiFindMessageUsersHandler> apiMessageUsersHandlers;
    @Autowired
    private ApiBaseMessageUserPoService apiBaseMessageUserPoService;
    @Autowired
    private ApiBaseMessagePoService apiBaseMessagePoService;
    @Autowired
    private ApiBaseLoginClientPoService apiBaseLoginClientPoService;
    @Autowired
    private ApiBaseMessageClientPoService apiBaseMessageClientPoService;
    @Autowired
    private ApiBaseUserPoService apiBaseUserPoService;
    @Autowired
    private ApiBaseMessageSendPoService apiBaseMessageSendPoService;
    @Autowired
    private ApiBaseMessageTemplatePoService apiBaseMessageTemplatePoService;

    @Autowired
    private ApiBaseMessageTemplateThirdBindPoService apiBaseMessageTemplateThirdBindPoService;

    @Autowired
    private ApiBaseLoginClientChannelBindPoService apiBaseLoginClientChannelBindPoService;

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
        BaseMessageTemplatePo templatePo = apiBaseMessageTemplatePoService.selectByPrimaryKeySimple(baseMessagePo.getMessageTemplateId(),false);


        // 待发送消息进行发送处理
        if (baseMessagePo != null && DictEnum.MessageState.to_be_sended.name().equals(baseMessagePo.getMsgState())) {
            BaseMessagePo baseMessagePoForUpdate = new BaseMessagePo();
            // 发送状态变更为发送中
            baseMessagePoForUpdate.setId(baseMessagePo.getId());
            baseMessagePoForUpdate.setMsgState(DictEnum.MessageState.sending.name());
            apiBaseMessagePoService.updateByPrimaryKeySelective(baseMessagePoForUpdate);
            // 插入消息发送表信息
            BaseMessageSendPo baseMessageSendPo = new BaseMessageSendPo();
            baseMessageSendPo.setMessageId(baseMessagePo.getId());
            BaseUserPo userPo = apiBaseUserPoService.selectByPrimaryKeySimple(dto.getCurrentUserId());
            baseMessageSendPo.setSendUserId(dto.getCurrentUserId());
            if (userPo != null) {
                baseMessageSendPo.setSendUserNickname(userPo.getNickname());
            }
            baseMessageSendPo.setTargetType(dto.getTargetType());
            baseMessageSendPo.setTargetValues(CollectionUtils.listToString(dto.getTargetValues(),","));
            if (dto.getClients() != null) {
                List<String> clientIds = new ArrayList<>(dto.getClients().size());
                List<String> clientNames = new ArrayList<>(dto.getClients().size());
                for (BaseLoginClientDto client : dto.getClients()) {
                    clientIds.add(client.getId());
                    clientNames.add(client.getClientName());
                }
                baseMessageSendPo.setClientIds(CollectionUtils.listToString(clientIds,","));
                baseMessageSendPo.setClientNames(CollectionUtils.listToString(clientNames,","));
            }
            if (dto.getTemplateParam() != null && !dto.getTemplateParam().isEmpty()) {
                baseMessageSendPo.setTemplateParams(dto.getTemplateParam().toString());
            }

            baseMessageSendPo = apiBaseMessageSendPoService.preInsert(baseMessageSendPo,dto.getCurrentUserId());
            apiBaseMessageSendPoService.insert(baseMessageSendPo);

            // 查找目标人员
            ApiFindMessageUsersHandler messageUsersHandler = resolveMessageUserHandler(dto.getTargetType(),dto.getTargetValues());
            ApiPageIterator<BaseUserPo> userIterator =  messageUsersHandler.findUsersByMessageTargets(1,20,dto.getTargetType(),dto.getTargetValues());
            List<BaseUserPo> userPos = null;
            while ((userPos = userIterator.next()) != null  && !userPos.isEmpty()){
                // 插入用户关联数据
                this.insertMessageUser(dto,userPos,baseMessagePo);
                // 插入消息客户端关联数据
                this.insertMessageClient(dto,baseMessagePo);
                if(isMq){
                    // 发送客户端消息
                    this.sendClientMessageMq(dto,userPos,baseMessagePo,templatePo);
                }else {
                    // 发送客户端消息
                    this.sendClientMessage(dto,userPos,baseMessagePo,templatePo);
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

            updateMessageClientStateToSended(dto,baseMessagePo);
        }
    }

    private BaseMessagePo getMessage(BaseMessageSendParamsDto dto){
        BaseMessagePo baseMessagePo = null;
        if (StringUtils.isNotEmpty(dto.getMessageId())){
            baseMessagePo = apiBaseMessagePoService.selectByPrimaryKeySimple(dto.getMessageId(),false);

            return baseMessagePo;

        }else {
            baseMessagePo = new BaseMessagePo();
        }
        if (StringUtils.isNotEmpty(dto.getMsgTemplateCode())){
            BaseMessageTemplatePo templatePo = apiBaseMessageTemplatePoService.selectByTemplateCode(dto.getMsgTemplateCode());
            if (templatePo != null) {
                baseMessagePo.setMsgState(DictEnum.MessageState.to_be_sended.name());
                Map<String,String> templateParam = dto.getTemplateParam();
                // 如果为空生成一个新的，以免空指针
                if (templateParam == null) {
                    templateParam = new HashMap<>();
                }
                baseMessagePo.setTitle(MsgTemplateUtils.replace(templatePo.getTitle(),templateParam));
                baseMessagePo.setProfile(MsgTemplateUtils.replace(templatePo.getProfile(),templateParam));
                baseMessagePo.setContent(MsgTemplateUtils.replace(templatePo.getContent(),templateParam));
                baseMessagePo.setMsgType(MsgTemplateUtils.replace(templatePo.getMsgType(),templateParam));
                baseMessagePo.setMsgLevel(MsgTemplateUtils.replace(templatePo.getMsgLevel(),templateParam));
                baseMessagePo.setMessageTemplateId(templatePo.getId());
                baseMessagePo = apiBaseMessagePoService.preInsert(baseMessagePo,dto.getCurrentUserId());
                baseMessagePo = apiBaseMessagePoService.insertSimple(baseMessagePo);
            }
        }
        return baseMessagePo;
    }

    /**
     * 将消息客户端发送状态变更为已发送
     * 注意该方法与insertMessageClient 相似度极高，请注意同时修改
     * @param dto
     * @param baseMessagePo
     */
    private void updateMessageClientStateToSended(BaseMessageSendParamsDto dto,BaseMessagePo baseMessagePo){

        BaseMessageClientPo messageClientPo = new BaseMessageClientPo();
        messageClientPo.setMessageState(DictEnum.MessageState.sended.name());

        BaseMessageClientPo messageClientConditionPo = null;
        // 客户端
        List<BaseLoginClientDto> clients = dto.getClients();
        if (clients != null && !clients.isEmpty()) {
            for (BaseLoginClientDto client : clients) {
                messageClientConditionPo = new BaseMessageClientPo();
                messageClientConditionPo.setClientId(client.getId());
                messageClientConditionPo.setMessageId(baseMessagePo.getId());

                messageClientConditionPo.setMessageState(DictEnum.MessageState.sended.name());
                apiBaseMessageClientPoService.updateSelective(messageClientPo,messageClientConditionPo);
            }
        }
    }
    private void insertMessageClient(BaseMessageSendParamsDto dto,BaseMessagePo baseMessagePo){

        BaseMessageClientPo messageClientPo = null;
        List<BaseMessageClientPo> messageClientPos = new ArrayList<>();
        // 客户端
        List<BaseLoginClientDto> clients = dto.getClients();
        if (clients != null && !clients.isEmpty()) {

            for (BaseLoginClientDto client : clients) {
                messageClientPo = new BaseMessageClientPo();
                messageClientPo.setClientId(client.getId());
                messageClientPo.setMessageId(baseMessagePo.getId());

                messageClientPo.setMessageState(DictEnum.MessageState.sending.name());
                messageClientPo = apiBaseMessageClientPoService.preInsert(messageClientPo,dto.getCurrentUserId());
                messageClientPos.add(messageClientPo);
            }
        }

        if (!messageClientPos.isEmpty()) {

            apiBaseMessageClientPoService.insertBatch(messageClientPos);
        }

    }

    private void insertMessageUser(BaseMessageSendParamsDto dto,List<BaseUserPo> userPos,BaseMessagePo baseMessagePo){
        BaseMessageUserPo messageUserPo = null;
        if (userPos != null && !userPos.isEmpty()) {
            List<BaseMessageUserPo> userStatePos = new ArrayList<>(userPos.size());
            for (BaseUserPo userPo : userPos) {
                messageUserPo = new BaseMessageUserPo();
                messageUserPo.setMessageId(baseMessagePo.getId());
                messageUserPo.setIsCanRead(BasePo.YesNo.Y.name());
                messageUserPo.setUserId(userPo.getId());
                messageUserPo.setIsRead(BasePo.YesNo.N.name());
                messageUserPo = apiBaseMessageUserPoService.preInsert(messageUserPo,dto.getCurrentUserId());
                userStatePos.add(messageUserPo);
            }
            apiBaseMessageUserPoService.insertBatch(userStatePos);
        }

    }

    /**
     * mq方式发送
     * @param dto
     * @param userPos
     * @param baseMessagePo
     */
    private void sendClientMessageMq(BaseMessageSendParamsDto dto,List<BaseUserPo> userPos,BaseMessagePo baseMessagePo,BaseMessageTemplatePo templatePo){
        // 暂未实现
    }
    private void sendClientMessage(BaseMessageSendParamsDto dto,List<BaseUserPo> userPos,BaseMessagePo baseMessagePo,BaseMessageTemplatePo templatePo) {
        if (dto.getClients() != null) {
            for (BaseLoginClientDto clientDto : dto.getClients()) {
                for (ApiClientMessageSender clientMessageSender : apiClientMessageSenders) {
                    if (clientMessageSender.support(clientDto)){
                        Map<String,Object> ThirdBindPo = getClientSupportMsgTemplateThirdBind(clientDto,templatePo);
                        ClientMessageSendParamDto clientMessageSendParamDto = new ClientMessageSendParamDto();
                        clientMessageSendParamDto.setBaseMessagePo(baseMessagePo);
                        clientMessageSendParamDto.setBaseMessageSendParamsDto(dto);
                        clientMessageSendParamDto.setClientDto(clientDto);
                        clientMessageSendParamDto.setTemplatePo(templatePo);
                        clientMessageSendParamDto.setUserPos(userPos);
                        if (ThirdBindPo != null) {
                            BaseMessageTemplateThirdBindPo messageTemplateThirdBindPo = (BaseMessageTemplateThirdBindPo) ThirdBindPo.get("MsgTemplateThirdBind");
                            BaseLoginClientChannelBindPo clientChannelBindPo = (BaseLoginClientChannelBindPo) ThirdBindPo.get("channelBind");
                            MsgTemplateThirdParamDto msgTemplateThirdParamDto = new MsgTemplateThirdParamDto();
                            msgTemplateThirdParamDto.setTemplateThirdBindPo(messageTemplateThirdBindPo);
                            msgTemplateThirdParamDto.setThirdTemplateContent(MsgTemplateUtils.replace(messageTemplateThirdBindPo.getThirdTemplateContent(),dto.getTemplateParam()));
                            clientMessageSendParamDto.setTemplateThirdParamDto(msgTemplateThirdParamDto);
                            clientMessageSendParamDto.setClientChannelBindPo(clientChannelBindPo);
                        }
                        clientMessageSender.doMessageSend(clientMessageSendParamDto);
                    }
                }
            }
        }
    }

    /**
     * 获取客户端所支持的三方模板
     * @param clientDto
     * @param templatePo
     * @return
     */
    private Map<String,Object> getClientSupportMsgTemplateThirdBind(BaseLoginClientDto clientDto,BaseMessageTemplatePo templatePo){
        if (clientDto == null || templatePo == null) {
            return null;
        }
        List<BaseLoginClientChannelBindPo> clientChannelBindPos = apiBaseLoginClientChannelBindPoService.selectByClientId(clientDto.getId());
        if (clientChannelBindPos == null || clientChannelBindPos.isEmpty()) {
            return null;
        }
        List<BaseMessageTemplateThirdBindPo> messageTemplateThirdBindPos = apiBaseMessageTemplateThirdBindPoService.selectByTemplateId(templatePo.getId());
        if (messageTemplateThirdBindPos == null || messageTemplateThirdBindPos.isEmpty()) {
            return null;
        }
        Map<String,Object> r = new HashMap<>(2);
        for (BaseLoginClientChannelBindPo clientChannelBindPo : clientChannelBindPos) {
            for (BaseMessageTemplateThirdBindPo messageTemplateThirdBindPo : messageTemplateThirdBindPos) {
                if (StringUtils.equals(clientChannelBindPo.getChannelId(),messageTemplateThirdBindPo.getThirdId())
                && StringUtils.equals(clientChannelBindPo.getChannelType(),messageTemplateThirdBindPo.getThirdType())) {
                    r.put("channelBind",clientChannelBindPo);
                    r.put("MsgTemplateThirdBind",messageTemplateThirdBindPo);
                    return r;

                }
            }
        }
        return null;
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
}
