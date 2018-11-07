package com.feihua.framework.message.handler;

import com.feihua.framework.base.modules.user.po.BaseUserPo;
import com.feihua.framework.message.dto.BaseMessageSendParamsDto;
import com.feihua.framework.message.dto.BaseMessageTargetClientParamsDto;
import com.feihua.framework.message.po.BaseMessagePo;
import com.feihua.framework.message.po.BaseMessageTargetClientPo;
import com.feihua.framework.constants.DictEnum;

import java.util.List;

/**
 * 消息发送处理
 * Created by yangwei
 * Created at 2018/11/1 17:13
 */
public interface ApiMessageSendHandler {
    /**
     * 真正的发送消息处理
     * @param dto
     * @param baseMessagePo
     */
    public void doMessageSend(BaseMessageTargetClientParamsDto dto, BaseMessagePo baseMessagePo, List<BaseUserPo> userPos);

    /**
     * 是否支持该消息的处理
     * @param dto
     * @return
     */
    public boolean support(BaseMessageTargetClientParamsDto dto);

    /**
     * 添加消息
     * @param dto
     * @param baseMessagePo
     * @param messageState
     * @return
     */
    public BaseMessageTargetClientPo addBaseMessageTargetClientPo(BaseMessageTargetClientParamsDto dto,BaseMessagePo baseMessagePo, DictEnum.MessageState messageState);

    public int updateMessageTargetClientState(BaseMessageTargetClientParamsDto dto,BaseMessagePo baseMessagePo, DictEnum.MessageState messageState);

    public int updateMessageTargetClientState(BaseMessageTargetClientPo targetClientPo, DictEnum.MessageState messageState);
}
