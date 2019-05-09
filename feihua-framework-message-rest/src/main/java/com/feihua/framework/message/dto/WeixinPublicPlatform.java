package com.feihua.framework.message.dto;

/**
 * Created by yangwei
 * Created at 2019/5/7 10:48
 */
public class WeixinPublicPlatform{
    /**
     * 绑定的微信公众帐号id
     */
    private String weixinPublicplatformAccoutId;
    private String weixinPublicplatformMsgTemplateId;
    private String weixinPublicplatformMsgTemplateContent;

    public String getWeixinPublicplatformAccoutId() {
        return weixinPublicplatformAccoutId;
    }

    public void setWeixinPublicplatformAccoutId(String weixinPublicplatformAccoutId) {
        this.weixinPublicplatformAccoutId = weixinPublicplatformAccoutId;
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
}
