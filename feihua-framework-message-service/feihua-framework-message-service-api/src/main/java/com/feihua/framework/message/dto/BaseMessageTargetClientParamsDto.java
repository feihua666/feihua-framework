package com.feihua.framework.message.dto;

import feihua.jdbc.api.pojo.BaseConditionDto;

/**
 * Created by yangwei
 * Created at 2018/11/2 13:30
 */
public class BaseMessageTargetClientParamsDto extends BaseConditionDto {

    private String targetClient;

    public String getTargetClient() {
        return targetClient;
    }

    public void setTargetClient(String targetClient) {
        this.targetClient = targetClient;
    }

}
