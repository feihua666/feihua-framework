package com.feihua.framework.message.dto;

import com.feihua.framework.base.modules.loginclient.dto.BaseLoginClientDto;
import feihua.jdbc.api.pojo.BaseConditionDto;

import java.util.List;
import java.util.Map;

/**
 * Created by yangwei
 * Created at 2018/10/31 12:52
 */
public class BaseMessageSendParamsDto extends BaseConditionDto {

    /**
     * 如果要发送一条已经存在的消息只需要指定id
     */
    private String messageId;

    /**
     * 消息模板id
     */
    private String MsgTemplateCode;

    /**
     * 消息模板参数
     */
    private Map<String,String> templateParam;

    /**
     * 客户端用来推送三方消息，如果没有则不进行推送，一般pc端和h5等不需要
     */
    private List<BaseLoginClientDto> clients;

    /**
     * targetType 目标人类型，如office=机构下的人等，self=自定义人
     */
    private String targetType;

    /**
     * targetValues 自定义目标人的值，比如userId集合
     */
    private List<String> targetValues;

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public List<BaseLoginClientDto> getClients() {
        return clients;
    }

    public void setClients(List<BaseLoginClientDto> clients) {
        this.clients = clients;
    }

    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    public List<String> getTargetValues() {
        return targetValues;
    }

    public void setTargetValues(List<String> targetValues) {
        this.targetValues = targetValues;
    }

    public String getMsgTemplateCode() {
        return MsgTemplateCode;
    }

    public void setMsgTemplateCode(String msgTemplateCode) {
        MsgTemplateCode = msgTemplateCode;
    }

    public Map<String, String> getTemplateParam() {
        return templateParam;
    }

    public void setTemplateParam(Map<String, String> templateParam) {
        this.templateParam = templateParam;
    }

}
