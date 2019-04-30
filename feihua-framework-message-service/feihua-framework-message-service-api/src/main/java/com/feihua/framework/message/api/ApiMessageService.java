package com.feihua.framework.message.api;

import com.feihua.framework.message.dto.BaseMessageSendParamsDto;

/**
 * 消息服务，主要消息管理
 * 发送消息主入口，读取消息
 * Created by yangwei
 * Created at 2019/4/25 17:32
 */
public interface ApiMessageService {


    /**
     * 消息发送
     * @param dto
     * @param asyn true=异步，false=同步
     */
    void messageSend(BaseMessageSendParamsDto dto, boolean asyn);
    /**
     * 消息发送,以mq的方式发送
     * @param dto
     */
    void messageSendByMq(BaseMessageSendParamsDto dto);

    /**
     * 读取消息
     * @param messageId 消息id
     * @param userId 用户id
     * @param readClientId 读取的客户端id
     */
    void readMessage(String messageId,String userId,String readClientId);

    /**
     * 读取消息
     * @param messageId
     * @param userId
     * @param clientCode
     */
    void readMessageWithClientCode(String messageId,String userId,String clientCode);
}
