package com.feihua.framework.message.dto;

import java.util.List;

/**
 * 绑定第三方模板表单
 * Created by yangwei
 * Created at 2019/4/8 10:40
 */
public class ThirdTemplateBindFormDto {

    /**
     * 微信公众号模板消息
     */
    List<WeixinPublicPlatform> weixinPublicPlatforms;
    /**
     * 微信小程序模板消息
     */
    List<WeixinMiniProgram> weixinMiniPrograms;

    public List<WeixinPublicPlatform> getWeixinPublicPlatforms() {
        return weixinPublicPlatforms;
    }

    public void setWeixinPublicPlatforms(List<WeixinPublicPlatform> weixinPublicPlatforms) {
        this.weixinPublicPlatforms = weixinPublicPlatforms;
    }

    public List<WeixinMiniProgram> getWeixinMiniPrograms() {
        return weixinMiniPrograms;
    }

    public void setWeixinMiniPrograms(List<WeixinMiniProgram> weixinMiniPrograms) {
        this.weixinMiniPrograms = weixinMiniPrograms;
    }


}
