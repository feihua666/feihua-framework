package com.feihua.framework.message.dto;

/**
 * Created by yangwei
 * Created at 2019/5/7 10:47
 */
public class WeixinMiniProgram{

    /**
     * 绑定的微信小程序帐号id
     */
    private String weixinMiniprogramAccoutId;
    private String weixinMiniprogramMsgTemplateId;
    private String weixinMiniprogramMsgTemplateContent;

    public String getWeixinMiniprogramAccoutId() {
        return weixinMiniprogramAccoutId;
    }

    public void setWeixinMiniprogramAccoutId(String weixinMiniprogramAccoutId) {
        this.weixinMiniprogramAccoutId = weixinMiniprogramAccoutId;
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
