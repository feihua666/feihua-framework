package com.feihua.wechat.publicplatform;

/**
 * 公众平台 常量
 * Created by yangwei
 * Created at 2018/7/20 12:14
 */
public class PublicConstants {

    public final static String PARAM_ACCESS_TOKEN = "ACCESS_TOKEN";
    public final static String PARAM_REDIRECT_URI = "REDIRECT_URI";
    public final static String PARAM_CODE = "CODE";
    public final static String PARAM_OPENID = "OPENID";
    public final static String PARAM_AUTHORIZE_SCOPE = "AUTHORIZE_SCOPE";
    public final static String PARAM_AUTHORIZE_REFRESH_TOKEN = "REFRESH_TOKEN";

    /**
     * 微信消息类型
     * 注意取name就可以，大小写不能变
     */
    public static enum MessageType{
        subscribe,   //关注
        unsubscribe, //取消关注
        SCAN,        //扫描带参数二维码
        LOCATION,    //上报地理位置
        CLICK,       //菜单点击事件
        text,        //文本消息
        news,        //图文消息
        image,       //图片消息
        transfer_customer_service,       //转客服消息

    }

    /**
     * 网页授权获取用户信息范围
     */
    public enum AuthorizeScope{
        snsapi_base,snsapi_userinfo
    }

    /**
     * 获取接口调用凭据url
     */
    public final static String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
    /**
     * 创建微信自定义菜单url
     */
    public final static String MENU_CREATE_URL = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";
    /**
     * 删除微信自定义菜单url
     */
    public final static String MENU_DELETE_URL = "https://api.weixin.qq.com/cgi-bin/menu/delete?access_token=ACCESS_TOKEN";
   /**
     * 网页授权access_token
     */
    public final static String AUTHORIZE_ACCESS_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=APPSECRET&code=CODE&grant_type=authorization_code";
    /**
     * 刷新网页授权access_token
     */
    public final static String AUTHORIZE_ACCESS_TOKEN_REFRESH_URL = "https://api.weixin.qq.com/sns/oauth2/refresh_token?appid=APPID&grant_type=refresh_token&refresh_token=REFRESH_TOKEN";

    /**
     * 用户信息url，该url一般是获取关注的用户信息
     */
    public final static String USER_INFO_URL = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";
    /**
     * JS接口的临时票据
     */
    public final static String JS_API_TICKET = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=ACCESS_TOKEN&type=jsapi";
    /**
     * 网页授权获取用户信息
     */
    public final static String AUTH_REDIRECT_URL = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=AUTHORIZE_SCOPE&state=STATE#wechat_redirect";
    /**
     * 网页授权后，获取用户信息
     */
    public final static String AUTH_USER_INFO_URL = "https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";

}
