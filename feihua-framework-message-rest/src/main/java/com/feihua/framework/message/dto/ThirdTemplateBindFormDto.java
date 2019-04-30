package com.feihua.framework.message.dto;

/**
 * Created by yangwei
 * Created at 2019/4/8 10:40
 */
public class ThirdTemplateBindFormDto {
    /**
     * 绑定的微信公众帐号id
     */
    private String weixinPublicplatformAccoutId;
    private String weixinPublicplatformMsgTemplateId;
    private String weixinPublicplatformMsgTemplateContent;
    /**
     * 绑定的微信小程序帐号id
     */
    private String weixinMiniprogramAccoutId;
    private String weixinMiniprogramMsgTemplateId;
    private String weixinMiniprogramMsgTemplateContent;

    public String getWeixinPublicplatformAccoutId() {
        return weixinPublicplatformAccoutId;
    }

    public void setWeixinPublicplatformAccoutId(String weixinPublicplatformAccoutId) {
        this.weixinPublicplatformAccoutId = weixinPublicplatformAccoutId;
    }

    public String getWeixinMiniprogramAccoutId() {
        return weixinMiniprogramAccoutId;
    }

    public void setWeixinMiniprogramAccoutId(String weixinMiniprogramAccoutId) {
        this.weixinMiniprogramAccoutId = weixinMiniprogramAccoutId;
    }

    public String getWeixinPublicplatformMsgTemplateId() {
        return weixinPublicplatformMsgTemplateId;
    }

    public void setWeixinPublicplatformMsgTemplateId(String weixinPublicplatformMsgTemplateId) {
        this.weixinPublicplatformMsgTemplateId = weixinPublicplatformMsgTemplateId;
    }

    public String getWeixinPublicplatformMsgTemplateContent() {
        return weixinPublicplatformMsgTemplateContent;
    }

    public void setWeixinPublicplatformMsgTemplateContent(String weixinPublicplatformMsgTemplateContent) {
        this.weixinPublicplatformMsgTemplateContent = weixinPublicplatformMsgTemplateContent;
    }

    public String getWeixinMiniprogramMsgTemplateId() {
        return weixinMiniprogramMsgTemplateId;
    }

    public void setWeixinMiniprogramMsgTemplateId(String weixinMiniprogramMsgTemplateId) {
        this.weixinMiniprogramMsgTemplateId = weixinMiniprogramMsgTemplateId;
    }

    public String getWeixinMiniprogramMsgTemplateContent() {
        return weixinMiniprogramMsgTemplateContent;
    }

    public void setWeixinMiniprogramMsgTemplateContent(String weixinMiniprogramMsgTemplateContent) {
        this.weixinMiniprogramMsgTemplateContent = weixinMiniprogramMsgTemplateContent;
    }
}
