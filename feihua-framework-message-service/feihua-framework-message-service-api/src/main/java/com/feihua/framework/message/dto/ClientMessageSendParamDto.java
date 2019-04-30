package com.feihua.framework.message.dto;

import com.feihua.framework.base.modules.loginclient.dto.BaseLoginClientDto;
import com.feihua.framework.base.modules.loginclient.po.BaseLoginClientChannelBindPo;
import com.feihua.framework.base.modules.user.po.BaseUserPo;
import com.feihua.framework.message.po.BaseMessagePo;
import com.feihua.framework.message.po.BaseMessageTemplatePo;
import feihua.jdbc.api.pojo.BaseConditionDto;

import java.util.List;

/**
 * Created by yangwei
 * Created at 2019/4/30 10:04
 */
public class ClientMessageSendParamDto extends BaseConditionDto {

    /**
     * 发送的消息实体
     */
    private BaseMessagePo baseMessagePo;
    /**
     * 原始发送参数对象
     */
    private BaseMessageSendParamsDto baseMessageSendParamsDto;
    /**
     * 目标用户
     */
    private List<BaseUserPo> userPos;
    /**
     * 发送的目标客户端
     */
    private BaseLoginClientDto clientDto;
    /**
     * 所支持的客户端三方渠道
     */
    private BaseLoginClientChannelBindPo clientChannelBindPo;
    /**
     * 消息模板定义
     */
    private BaseMessageTemplatePo templatePo;
    /**
     * 三方消息模板参数
     */
    private MsgTemplateThirdParamDto templateThirdParamDto;

    public BaseMessagePo getBaseMessagePo() {
        return baseMessagePo;
    }

    public void setBaseMessagePo(BaseMessagePo baseMessagePo) {
        this.baseMessagePo = baseMessagePo;
    }

    public BaseMessageSendParamsDto getBaseMessageSendParamsDto() {
        return baseMessageSendParamsDto;
    }

    public void setBaseMessageSendParamsDto(BaseMessageSendParamsDto baseMessageSendParamsDto) {
        this.baseMessageSendParamsDto = baseMessageSendParamsDto;
    }

    public List<BaseUserPo> getUserPos() {
        return userPos;
    }

    public void setUserPos(List<BaseUserPo> userPos) {
        this.userPos = userPos;
    }

    public BaseLoginClientDto getClientDto() {
        return clientDto;
    }

    public void setClientDto(BaseLoginClientDto clientDto) {
        this.clientDto = clientDto;
    }

    public BaseMessageTemplatePo getTemplatePo() {
        return templatePo;
    }

    public void setTemplatePo(BaseMessageTemplatePo templatePo) {
        this.templatePo = templatePo;
    }

    public MsgTemplateThirdParamDto getTemplateThirdParamDto() {
        return templateThirdParamDto;
    }

    public void setTemplateThirdParamDto(MsgTemplateThirdParamDto templateThirdParamDto) {
        this.templateThirdParamDto = templateThirdParamDto;
    }

    public BaseLoginClientChannelBindPo getClientChannelBindPo() {
        return clientChannelBindPo;
    }

    public void setClientChannelBindPo(BaseLoginClientChannelBindPo clientChannelBindPo) {
        this.clientChannelBindPo = clientChannelBindPo;
    }
}
