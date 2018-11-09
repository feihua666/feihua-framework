package com.feihua.wechat.common.dto;

/**
 * @Auther: wzn
 * @Date: 2018/11/2 15:18
 * @Description: 微信模板数据
 */
public class WxPublicTemplateData {

    /**
     * 内容
     */
    private String value;

    /**
     * 模板内容字体颜色，不填默认为黑色
     */
    private String color;

    public WxPublicTemplateData() {
    }

    public WxPublicTemplateData(String value, String color) {
        this.value = value;
        this.color = color;
    }

    public String getValue() {

        return value;

    }

    public void setValue(String value) {

        this.value = value;

    }

    public String getColor() {

        return color;

    }

    public void setColor(String color) {

        this.color = color;

    }

}