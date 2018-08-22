package com.feihua.wechat.miniprogram;

/**
 * 小程序 常量
 * Created by yangwei
 * Created at 2018/4/27 18:14
 */
public class MiniConstants {

    public final static String PARAM_JSCODE = "JSCODE";
    public final static String PARAM_ACCESS_TOKEN = "ACCESS_TOKEN";
    public final static String MESSAGE_XML_TYPE = "xml";
    public final static String MESSAGE_JSON_TYPE = "json";
    /**
     * 获取 session_key 和 openid 等
     */
    public final static String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/sns/jscode2session?appid=APPID&secret=APPSECRET&js_code=JSCODE&grant_type=authorization_code";
    /**
     * 发送模板消息
     */
    public final static String MSG_TEMPLATE_URL = "https://api.weixin.qq.com/cgi-bin/message/wxopen/template/send?access_token=ACCESS_TOKEN";
    /**
     * 发送客服消息
     */
    public final static String MSG_custom_URL = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=ACCESS_TOKEN";

}
