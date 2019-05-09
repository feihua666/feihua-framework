package com.feihua.framework.message.handler.impl;

import com.feihua.framework.base.modules.loginclient.dto.BaseLoginClientDto;
import com.feihua.framework.base.modules.user.api.ApiBaseUserAuthPoService;
import com.feihua.framework.base.modules.user.dto.BaseUserAuthDto;
import com.feihua.framework.base.modules.user.po.BaseUserPo;
import com.feihua.framework.constants.DictEnum;
import com.feihua.framework.message.dto.BaseMessageClientDto;
import com.feihua.framework.message.dto.ClientMessageSendParamDto;
import com.feihua.framework.message.dto.VClientMessageSendParamDto;
import com.feihua.framework.message.handler.ApiClientMessageSender;
import com.feihua.framework.message.handler.ApiVClientMessageSender;
import com.feihua.framework.message.po.BaseMessagePo;
import com.feihua.utils.collection.CollectionUtils;
import com.feihua.utils.spring.SpringContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 短信消息发送
 * Created by yangwei
 * Created at 2019/5/8 13:30
 */
public class SmsMessageSenderImpl implements ApiVClientMessageSender, ApiClientMessageSender {
    private static final Logger logger = LoggerFactory.getLogger(SmsMessageSenderImpl.class);

    ApiBaseUserAuthPoService apiBaseUserAuthPoService;


    @Override
    public void doMessageSend(ClientMessageSendParamDto dto) {
        send(dto.getBaseMessagePo(),dto.getClientDto(),null,dto.getUserPos());

    }

    @Override
    public void doMessageSend(VClientMessageSendParamDto dto) {
        send(dto.getBaseMessagePo(),dto.getClientDto(),dto.getUserIdentifiers(),null);

    }

    @Override
    public boolean support(BaseLoginClientDto clientDto, List<BaseMessageClientDto> messageClientDtos) {
        // todo
        // 现在没有具体实现，暂时不支持发送
        /*if(AbstractClientMessageSenderImpl.isContents(DictEnum.MessageClientType.sms.name(),messageClientDtos)){
            return true;
        }*/
        return false;
    }
    private void send(BaseMessagePo baseMessagePo, BaseLoginClientDto clientDto, List<String> userIdentifiers, List<BaseUserPo> userPos){

        if (!CollectionUtils.isNullOrEmpty(userIdentifiers)) {
            for (String userIdentifier : userIdentifiers) {
                doSend(userIdentifier,baseMessagePo);
            }
        }
        if (apiBaseUserAuthPoService == null) {
            apiBaseUserAuthPoService = SpringContextHolder.getBean(ApiBaseUserAuthPoService.class);
        }

        if (!CollectionUtils.isNullOrEmpty(userPos)) {
            for (BaseUserPo userPo : userPos) {
                BaseUserAuthDto userAuthDto = apiBaseUserAuthPoService.selectByUserIdAndType(userPo.getId(),DictEnum.LoginType.MOBILE.name());
                if (userAuthDto != null) {
                    doSend(userAuthDto.getIdentifier(),baseMessagePo);
                }
            }
        }
    }
    private void doSend(String mobile,BaseMessagePo messagePo){
        // todo
        // 发送短信
    }

}
