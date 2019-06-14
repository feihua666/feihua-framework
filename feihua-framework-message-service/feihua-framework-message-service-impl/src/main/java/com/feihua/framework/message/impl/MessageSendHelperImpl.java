package com.feihua.framework.message.impl;

import com.feihua.framework.base.modules.loginclient.api.ApiBaseLoginClientPoService;
import com.feihua.framework.base.modules.loginclient.dto.BaseLoginClientDto;
import com.feihua.framework.constants.DictEnum;
import com.feihua.framework.message.api.ApiBaseMessagePoService;
import com.feihua.framework.message.api.ApiBaseMessageTemplatePoService;
import com.feihua.framework.message.api.ApiMessageService;
import com.feihua.framework.message.api.MessageSendHelper;
import com.feihua.framework.message.dto.BaseMessageSendClientParamDto;
import com.feihua.framework.message.dto.BaseMessageSendParamsDto;
import com.feihua.framework.message.dto.CreateMessageParamsDto;
import com.feihua.framework.message.dto.MessageSendForUserParamsDto;
import com.feihua.framework.message.po.BaseMessagePo;
import com.feihua.framework.message.po.BaseMessageTemplatePo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangwei
 * Created at 2019/5/14 15:54
 */
@Service
public class MessageSendHelperImpl implements MessageSendHelper {
    @Autowired
    private ApiBaseMessageTemplatePoService apiBaseMessageTemplatePoService;

    @Autowired
    private ApiBaseMessagePoService apiBaseMessagePoService;
    @Autowired
    private ApiMessageService apiMessageService;
    @Autowired
    private ApiBaseLoginClientPoService apiBaseLoginClientPoService;
    @Override
    public void messageSendForUser(MessageSendForUserParamsDto paramsDto) {
        BaseMessageTemplatePo messageTemplatePo = apiBaseMessageTemplatePoService.selectByTemplateCode(paramsDto.getTemplateCode());

        // 先添加一条消息
        CreateMessageParamsDto createMessageParamsDto = new CreateMessageParamsDto();
        createMessageParamsDto.setTitle(messageTemplatePo.getTitle());
        createMessageParamsDto.setProfile(messageTemplatePo.getProfile());
        createMessageParamsDto.setContent(messageTemplatePo.getContent());
        createMessageParamsDto.setMsgType(messageTemplatePo.getMsgType());
        createMessageParamsDto.setMsgLevel(messageTemplatePo.getMsgLevel());
        createMessageParamsDto.setMsgState(DictEnum.MessageState.to_be_sended.name());
        createMessageParamsDto.setMessageTemplateId(messageTemplatePo.getId());
        createMessageParamsDto.setTemplateParams(paramsDto.getTemplateParams());

        createMessageParamsDto.setCurrentUserId(paramsDto.getCurrentUserId());
        createMessageParamsDto.setCurrentRoleId(paramsDto.getCurrentRoleId());
        createMessageParamsDto.setCurrentPostId(paramsDto.getCurrentPostId());
        BaseMessagePo r = apiBaseMessagePoService.addMessage(createMessageParamsDto);

        // 设置发送参数
        BaseMessageSendClientParamDto clientParamDto = new BaseMessageSendClientParamDto();

        List<BaseLoginClientDto> clientDtos = new ArrayList<>(1);
        BaseLoginClientDto loginClientDto = apiBaseLoginClientPoService.wrapDto(apiBaseLoginClientPoService.selectByClientCode(paramsDto.getClientCode()));
        clientDtos.add(loginClientDto);
        clientParamDto.setClients(clientDtos);

        clientParamDto.setTargetType(DictEnum.MessageTargetType.multi_people.name());
        List<String> userIds = new ArrayList<>(1);
        userIds.add(paramsDto.getUserId());
        clientParamDto.setTargetValues(userIds);


        BaseMessageSendParamsDto baseMessageSendParamsDto = new BaseMessageSendParamsDto();
        baseMessageSendParamsDto.setMessageId(r.getId());
        baseMessageSendParamsDto.setClientParamDto(clientParamDto);
        baseMessageSendParamsDto.setCurrentUserId(paramsDto.getCurrentUserId());
        baseMessageSendParamsDto.setCurrentRoleId(paramsDto.getCurrentRoleId());
        baseMessageSendParamsDto.setCurrentPostId(paramsDto.getCurrentPostId());
        apiMessageService.messageSend(baseMessageSendParamsDto,true);
    }
}
