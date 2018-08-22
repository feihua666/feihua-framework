package com.feihua.framework.activity.dto;

import feihua.jdbc.api.pojo.BaseConditionDto;

import java.util.Map;

/**
 * Created by yangwei
 * Created at 2018/4/12 9:29
 */
public class ApplyParamDto extends BaseConditionDto {
    /**
     * 业务id
     */
    private String businessId;
    /**
     * 扩展参数
     */
    private Map<String,Object> addtional;

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public Map<String, Object> getAddtional() {
        return addtional;
    }

    public void setAddtional(Map<String, Object> addtional) {
        this.addtional = addtional;
    }
}
