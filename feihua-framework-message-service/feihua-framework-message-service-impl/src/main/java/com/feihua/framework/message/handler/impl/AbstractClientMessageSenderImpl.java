package com.feihua.framework.message.handler.impl;

import com.feihua.framework.base.modules.loginclient.api.ApiBaseLoginClientPoService;
import com.feihua.framework.base.modules.loginclient.dto.BaseLoginClientDto;
import com.feihua.framework.base.modules.loginclient.po.BaseLoginClientPo;
import com.feihua.framework.message.handler.ApiClientMessageSender;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by yangwei
 * Created at 2018/11/1 19:39
 */
public abstract class AbstractClientMessageSenderImpl implements ApiClientMessageSender {


    private static final Logger logger = LoggerFactory.getLogger(AbstractClientMessageSenderImpl.class);

    private String clientCode;
    public AbstractClientMessageSenderImpl(){}
    public AbstractClientMessageSenderImpl(String clientCode){
        this.clientCode = clientCode;
    }


    public boolean support(BaseLoginClientDto clientDto){
        if (StringUtils.isNotEmpty(clientCode) && clientCode.equals(clientDto.getClientCode())) {
            return true;
        }
        return false;
    }
}
