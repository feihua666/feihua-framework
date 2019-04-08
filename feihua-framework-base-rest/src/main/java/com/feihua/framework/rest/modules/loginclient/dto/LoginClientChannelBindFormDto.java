package com.feihua.framework.rest.modules.loginclient.dto;

/**
 * Created by yangwei
 * Created at 2019/4/8 10:40
 */
public class LoginClientChannelBindFormDto {
    /**
     * 绑定的微信公众帐号id
     */
    private String weixinPublicplatformAccoutId;
    /**
     * 绑定的微信小程序帐号id
     */
    private String weixinMiniprogramAccoutId;

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
}
