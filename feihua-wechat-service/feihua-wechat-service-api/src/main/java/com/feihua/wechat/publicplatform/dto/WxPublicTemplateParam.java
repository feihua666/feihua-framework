package com.feihua.wechat.publicplatform.dto;

import com.feihua.wechat.common.dto.Miniprogram;
import com.feihua.wechat.common.dto.WxPublicTemplateData;

import java.util.Map;

/**
 * @Auther: wzn
 * @Date: 2018/11/2 16:03
 * @Description: 微信消息模板
 */
public class WxPublicTemplateParam {

    /**
     * 模板ID
     */
    private String template_id;

    /**
     * 接收者openid
     */
    private String touser;

    /**
     * 模板跳转链接（海外帐号没有跳转能力）
     */
    private String url;

    /**
     * 跳小程序所需数据，不需跳小程序可不用传该数据
     */
    private Miniprogram miniprogram;

    /**
     * 模板数据
     */
    private Map<String, WxPublicTemplateData> data;

    public WxPublicTemplateParam() {
    }

    public WxPublicTemplateParam(String template_id, String touser, String url, Miniprogram miniprogram, Map<String, WxPublicTemplateData> data) {
        this.template_id = template_id;
        this.touser = touser;
        this.url = url;
        this.miniprogram = miniprogram;
        this.data = data;
    }

    public WxPublicTemplateParam(String template_id, String touser, String url, Map<String, WxPublicTemplateData> data) {
        this.template_id = template_id;
        this.touser = touser;
        this.url = url;
        this.data = data;
    }

    public String getTemplate_id() {

        return template_id;

    }

    public void setTemplate_id(String template_id) {

        this.template_id = template_id;

    }

    public String getTouser() {

        return touser;

    }

    public void setTouser(String touser) {

        this.touser = touser;

    }

    public String getUrl() {

        return url;

    }

    public void setUrl(String url) {

        this.url = url;

    }

    public Map<String, WxPublicTemplateData> getData() {

        return data;

    }

    public void setData(Map<String, WxPublicTemplateData> data) {

        this.data = data;

    }

    public Miniprogram getMiniprogram() {
        return miniprogram;
    }

    public void setMiniprogram(Miniprogram miniprogram) {
        this.miniprogram = miniprogram;
    }
}
