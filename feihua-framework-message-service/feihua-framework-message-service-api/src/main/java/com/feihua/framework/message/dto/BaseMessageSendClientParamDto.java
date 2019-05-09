package com.feihua.framework.message.dto;

import com.feihua.framework.base.modules.loginclient.dto.BaseLoginClientDto;
import feihua.jdbc.api.pojo.BaseDto;

import java.util.List;

/**
 * 消息发送客户端参数，只支持非虚拟客户端类型
 * 该参数所有目标客户端共享用户数据
 * Created by yangwei
 * Created at 2019/5/7 14:09
 */
public class BaseMessageSendClientParamDto extends BaseDto {
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
}
