package com.feihua.framework.message.handler.impl;

import com.feihua.framework.base.modules.loginclient.dto.BaseLoginClientDto;
import com.feihua.framework.message.dto.BaseMessageClientDto;
import com.feihua.framework.message.handler.ApiClientMessageSender;
import com.feihua.framework.message.handler.ApiVClientMessageSender;
import com.feihua.utils.collection.CollectionUtils;
import feihua.jdbc.api.pojo.BasePo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by yangwei
 * Created at 2018/11/1 19:39
 */
public abstract class AbstractVClientMessageSenderImpl implements ApiVClientMessageSender {


    private static final Logger logger = LoggerFactory.getLogger(AbstractVClientMessageSenderImpl.class);

    // 为空表示全部支持
    private String clientType;
    // 为空表示全部支持
    private String messageClientType;

    public AbstractVClientMessageSenderImpl(String clientCode,String messageClientType){
        this.clientType = clientCode;
        this.messageClientType = messageClientType;
    }


    public boolean support(BaseLoginClientDto clientDto, List<BaseMessageClientDto> messageClientDtos){
        // 只支持虚拟客户端
        if (BasePo.YesNo.N.name().equals(clientDto.getIsVirtual())) {
            return false;
        }
        // 如果构造方法两个参数都为空表示都支持
        if (StringUtils.isEmpty(clientType) && StringUtils.isEmpty(messageClientType)) {
            return true;
        }
        // 都不为空
        if (StringUtils.isNotEmpty(clientType) && StringUtils.isNotEmpty(messageClientType)) {
            if ( clientType.equals(clientDto.getType()) && AbstractClientMessageSenderImpl.isContents(messageClientType,messageClientDtos)) {
                return true;
            }
        }
        if (StringUtils.isNotEmpty(clientType)) {
            if ( clientType.equals(clientDto.getType())) {
                return true;
            }
        }
        if (StringUtils.isNotEmpty(messageClientType)) {
            if (AbstractClientMessageSenderImpl.isContents(messageClientType,messageClientDtos)) {
                return true;
            }
        }

        return false;
    }

}
