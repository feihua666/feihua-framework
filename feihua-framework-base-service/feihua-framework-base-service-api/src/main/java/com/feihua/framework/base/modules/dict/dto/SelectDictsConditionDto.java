package com.feihua.framework.base.modules.dict.dto;

import feihua.jdbc.api.pojo.BaseConditionDto;

/**
 * Created by yangwei
 * Created at 2017/12/21 13:23
 */
public class SelectDictsConditionDto extends BaseConditionDto {


    /**
     * 类型
     */
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
