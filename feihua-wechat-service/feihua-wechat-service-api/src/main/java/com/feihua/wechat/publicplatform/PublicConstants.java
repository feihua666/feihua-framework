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
    public final static String PARAM_AUTHORIZE_STATE = "STATE";
    public final static String PARAM_AUTHORIZE_REFRESH_TOKEN = "REFRESH_TOKEN";
    public final static String PARAM_TICKET = "TICKET";


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

    /**
     * 获取已添加至帐号下所有模板列表，可在微信公众平台后台中查看模板列表信息
     */
    public final static String GET_ALL_PRIVATE_TEMPLATE_URL = "https://api.weixin.qq.com/cgi-bin/template/get_all_private_template?access_token=ACCESS_TOKEN";

    /**
     * 微信发送模板消息
     */
    public final static String SEND_TEMPLATE_MSG = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=ACCESS_TOKEN";

    /**
     * 新增其他类型永久素材
     * request:
     * access_token	是	调用接口凭证
     * type	是	媒体文件类型，分别有图片（image）、语音（voice）、视频（video）和缩略图（thumb）
     * media	是	form-data中媒体文件标识，有filename、filelength、content-type等信息
     * return:
     * {
     * "media_id":MEDIA_ID,
     * "url":URL
     * }
     */
    public final static String ADD_MATERIAL = "https://api.weixin.qq.com/cgi-bin/material/add_material?access_token=ACCESS_TOKEN&type=TYPE";
    /**
     * 生成带参数二维码
     */
    public final static String CREATE_QRCODE_PARAM = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=ACCESS_TOKEN";
    /**
     * 下载带参数二维码
     */
    public final static String SHOW_QRCODE_PARAM = "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=TICKET";

}
