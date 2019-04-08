package com.feihua.wechat.miniprogram;

/**
 * 小程序 常量
 * Created by yangwei
 * Created at 2018/4/27 18:14
 */
public class MiniConstants {

    public final static String PARAM_JSCODE = "JSCODE";
    public final static String PARAM_ACCESS_TOKEN = "ACCESS_TOKEN";

    /**
     * 获取 session_key 和 openid 等
     * 登录凭证校验。通过 wx.login() 接口获得临时登录凭证 code 后传到开发者服务器调用此接口完成登录流程。更多使用方法详见 小程序登录。
     */
    public final static String JSCODE2_SESSION_URL = "https://api.weixin.qq.com/sns/jscode2session?appid=APPID&secret=APPSECRET&js_code=JSCODE&grant_type=authorization_code";
    /**
     * 发送模板消息
     */
    public final static String MSG_TEMPLATE_URL = "https://api.weixin.qq.com/cgi-bin/message/wxopen/template/send?access_token=ACCESS_TOKEN";
    /**
     * 发送客服消息
     */
    public final static String MSG_CUSTOM_URL = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=ACCESS_TOKEN";

    /**
     * 获取已添加至微信小程序帐号下模板列表
     */
    public final static String GET_MINIPROGRAM_TEMPLATE_URL = "https://api.weixin.qq.com/cgi-bin/wxopen/template/list?access_token=ACCESS_TOKEN";


    /**
     * 获取 session_key
     */
    public final static String GET_ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";


}
