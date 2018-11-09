package com.feihua.wechat.publicplatform;

import com.feihua.wechat.common.dto.Miniprogram;
import com.feihua.wechat.common.dto.WxPublicTemplateData;

import java.util.Map;

/**
 * @Auther: wzn
 * @Date: 2018/11/2 16:03
 * @Description: 微信消息模板
 */
public class WxPublicTemplate {

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
     * 模板标题字体颜色，不填默认为黑色
     */
    private String topcolor;

    /**
     * 跳小程序所需数据，不需跳小程序可不用传该数据
     */
    private Miniprogram miniprogram;

    /**
     * 模板数据
     */
    private Map<String, WxPublicTemplateData> data;

    public WxPublicTemplate() {
    }

    public WxPublicTemplate(String template_id, String touser, String url, String topcolor, Miniprogram miniprogram, Map<String, WxPublicTemplateData> data) {
        this.template_id = template_id;
        this.touser = touser;
        this.url = url;
        this.topcolor = topcolor;
        this.miniprogram = miniprogram;
        this.data = data;
    }

    public WxPublicTemplate(String template_id, String touser, String url, String topcolor, Map<String, WxPublicTemplateData> data) {
        this.template_id = template_id;
        this.touser = touser;
        this.url = url;
        this.topcolor = topcolor;
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

    public String getTopcolor() {

        return topcolor;

    }

    public void setTopcolor(String topcolor) {

        this.topcolor = topcolor;

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
