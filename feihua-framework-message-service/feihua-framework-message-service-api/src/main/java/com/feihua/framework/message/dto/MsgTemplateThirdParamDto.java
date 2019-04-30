package com.feihua.framework.message.dto;

import com.feihua.framework.message.po.BaseMessageTemplateThirdBindPo;
import feihua.jdbc.api.pojo.BaseDto;

/**
 * 三方消息模板实现内容参数
 * Created by yangwei
 * Created at 2019/4/29 11:49
 */
public class MsgTemplateThirdParamDto extends BaseDto {
    private BaseMessageTemplateThirdBindPo templateThirdBindPo;
    private String thirdTemplateContent;

    public BaseMessageTemplateThirdBindPo getTemplateThirdBindPo() {
        return templateThirdBindPo;
    }

    public void setTemplateThirdBindPo(BaseMessageTemplateThirdBindPo templateThirdBindPo) {
        this.templateThirdBindPo = templateThirdBindPo;
    }

    public String getThirdTemplateContent() {
        return thirdTemplateContent;
    }

    public void setThirdTemplateContent(String thirdTemplateContent) {
        this.thirdTemplateContent = thirdTemplateContent;
    }
}
