package com.feihua.wechat.publicplatform;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.feihua.framework.constants.DictEnum;
import com.feihua.framework.jedis.utils.JedisUtils;
import com.feihua.utils.digest.DigestUtils;
import com.feihua.utils.http.httpclient.HttpClientUtils;
import com.feihua.utils.json.JSONUtils;
import com.feihua.utils.properties.PropertiesUtils;
import com.feihua.utils.spring.SpringContextHolder;
import com.feihua.wechat.CommonConstants;
import com.feihua.wechat.common.dto.WeixinAccountDto;
import com.feihua.wechat.common.po.WeixinUserPo;
import com.feihua.wechat.common.api.ApiWeixinAccountPoService;
import com.feihua.wechat.common.dto.WxTemplate;
import com.feihua.wechat.publicplatform.dto.*;
import com.feihua.wechat.common.po.WeixinAccountPo;
import feihua.jdbc.api.pojo.BasePo;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.MethodUtils;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * Created by yangwei
 * Created at 2018/7/20 13:11
 */
public class PublicUtils {

    private static Logger logger = LoggerFactory.getLogger(PublicUtils.class);

    /**
     * 将xml消息转为对象
     *
     * @param xmlRequestMessage
     * @param msg               应该为一个message的实现类对象
     *
     * @return
     *
     * @throws DocumentException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public static <T> T xmlToMessage(String xmlRequestMessage, T msg) {

        try {
            Document document = DocumentHelper.parseText(xmlRequestMessage);
            Element root = document.getRootElement();
            List<Element> elements = root.elements();
            for (Element e : elements) {
                String nodeName = e.getName();
                String fieldName = nodeName.substring(0, 1).toLowerCase()
                        + nodeName.substring(1);
                BeanUtils.setProperty(msg, fieldName, e.getText());
            }

        } catch (Exception e) {
            logger.error("xmlToMessage", e);
        }
        return msg;
    }

    /**
     * 将消息对象转为xml
     *
     * @param msg         应该是一个message的实现类对象
     * @param ignoreEmpty 是否忽略为空的属性 默认为true
     *
     * @return
     *
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     */
    public static String messageToXml(Message msg, boolean ignoreEmpty) {
        boolean _ignoreempty = ignoreEmpty;
        String result = "";
        try {
            result = "<xml>";
            PropertyDescriptor propertyDescriptor[] = org.springframework.beans.BeanUtils.getPropertyDescriptors(msg.getClass());
            for (PropertyDescriptor f : propertyDescriptor) {
                String fieldName = f.getName();
                String nodeName = fieldName.substring(0, 1).toUpperCase()
                        + fieldName.substring(1);
                //图文消息
                if ((f.getPropertyType().getName()).equals("java.util.List")) {
                    result += "<" + nodeName + ">";
                    List items = (List) MethodUtils.invokeMethod(msg, "get" + nodeName, null);
                    if (items != null) {
                        for (Object item : items) {
                            String itemNodename = item.getClass().getSimpleName().toLowerCase();
                            result += "<" + itemNodename + ">";
                            PropertyDescriptor itempropertyDescriptor[] = org.springframework.beans.BeanUtils.getPropertyDescriptors(item.getClass());
                            for (PropertyDescriptor itemf : itempropertyDescriptor) {
                                result += simpleFieldToXml(item, itemf, _ignoreempty);
                            }
                            result += "</" + itemNodename + ">";
                        }
                    }
                    result += "</" + nodeName + ">";
                }
                //其它简单字段
                else {
                    result += simpleFieldToXml(msg, f, _ignoreempty);
                }

            }
            result += "</xml>";
        } catch (Exception e) {
            logger.error("messageToXml", e);
        }

        return result;
    }

    /**
     * field转node
     *
     * @param obj
     * @param propertyDescriptor
     *
     * @return
     *
     * @throws IllegalAccessException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     */
    private static String simpleFieldToXml(Object obj, PropertyDescriptor propertyDescriptor, boolean _ignoreempty) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        String result = "";
        String fieldName = propertyDescriptor.getName();
        if ("class".equals(fieldName)) return "";
        String nodeName = fieldName.substring(0, 1).toUpperCase()
                + fieldName.substring(1);
        String value = BeanUtils.getSimpleProperty(obj, fieldName);
        if (StringUtils.isEmpty(value) && _ignoreempty) return "";
        result += "<" + nodeName + ">";
        //文本
        if ((propertyDescriptor.getPropertyType().getName()).equals("java.lang.String")) {
            result += wrapCDATA(value);
        } else {
            result += value;
        }
        result += "</" + nodeName + ">";
        return result;
    }

    /**
     * 获取微信用户信息
     *
     * @param openid
     * @param which
     *
     * @return {
     * "openid":" OPENID",
     * " nickname": NICKNAME,
     * "sex":"1",//0未知，1男，2女
     * "province":"PROVINCE"
     * "city":"CITY",
     * "country":"COUNTRY",
     * "headimgurl":    "http://wx.qlogo.cn/mmopen/g3MonUZtNHkdmzicIlibx6iaFqAc56vxLSUfpb6n5WKSYVY0ChQKkiaJSgQ1dZuTOgvLLrhJbERQQ4eMsv84eavHiaiceqxibJxCfHe/46",
     * "privilege":[
     * "PRIVILEGE1"
     * "PRIVILEGE2"
     * ],
     * "unionid": "o6_bmasdasdsad6_2sgVt7hMZOPfL"
     * }
     */
    public static WeixinUserPo getWeixinUser(String openid, String which) {
        String url = PublicConstants.USER_INFO_URL.replace(PublicConstants.PARAM_ACCESS_TOKEN, getAccessToken(which).getToken()).replace(PublicConstants.PARAM_OPENID, openid);

        JSONObject jsonObject = null;
        WeixinUserPo weiXinUserPo = null;
        try {

            jsonObject = new JSONObject(HttpClientUtils.httpGet(url));
            if (jsonObject.has("errcode")) {
                Integer errCode = jsonObject.getInt("errcode");
                logger.error("Error occurs when getWeixinUser, requestUrl:{},error:{}", url, jsonObject);
                if (errCode != 0) {
                    throw new RuntimeException(jsonObject.toString());
                }
            }
            weiXinUserPo = new WeixinUserPo();
            weiXinUserPo.setOpenid(openid);

            // 只有在用户将公众号绑定到微信开放平台帐号后，才会出现该字段。
            try {
                weiXinUserPo.setUnionid(jsonObject.getString("unionid"));
            } catch (JSONException e) {

            }
            weiXinUserPo.setNickname(jsonObject.getString("nickname"));
            weiXinUserPo.setGender(jsonObject.getInt("sex") + "");
            weiXinUserPo.setCity(jsonObject.getString("city"));
            weiXinUserPo.setProvince(jsonObject.getString("province"));
            weiXinUserPo.setCountry(jsonObject.getString("country"));
            weiXinUserPo.setLanguage(jsonObject.getString("language"));
            weiXinUserPo.setHeadImageUrl(jsonObject.getString("headimgurl"));
            String subscribe_scene = jsonObject.getString("subscribe_scene");
            if ("ADD_SCENEPROFILE LINK".equals(subscribe_scene)) {
                subscribe_scene = DictEnum.WeixinUserHowFrom.ADD_SCENEPROFILE_LINK.name();
            }
            weiXinUserPo.setHowFrom(subscribe_scene);
            weiXinUserPo.setType(DictEnum.WxAccountType.weixin_publicplatform.name());
            weiXinUserPo.setWhich(which);
        } catch (Exception e) {
            logger.error("Error occurs when getWeixinUser, requestUrl:" + url, e);
            throw new RuntimeException(e.getMessage());
        }
        return weiXinUserPo;
    }

    /**
     * @param text
     *
     * @return
     */
    public static String wrapCDATA(String text) {
        return "<![CDATA[" + text + "]]>";
    }

    /**
     * 获取微信调用凭据
     *
     * @return
     */
    public static AccessToken getAccessToken(String which) {
        AccessToken accessToken = (AccessToken) JedisUtils.getObject("weixinAccessToken_" + which);

        if (accessToken != null && accessToken.isExpires() == false) return accessToken;

        String url = PublicConstants.ACCESS_TOKEN_URL.replace(CommonConstants.PARAM_APPID, PublicUtils.getAppid(which)).replace(CommonConstants.PARAM_APPSECRET, PublicUtils.getAppsecret(which));
        try {
            JSONObject jsonObject = new JSONObject(HttpClientUtils.httpGet(url));
            if (jsonObject != null && jsonObject.has("access_token")) {
                accessToken = new AccessToken();
                accessToken.setToken(jsonObject.getString("access_token"));
                accessToken.setExpiresIn(jsonObject.getInt("expires_in"));
                accessToken.setCreateTime(System.currentTimeMillis() / 1000);
                accessToken.setWhich(which);
                JedisUtils.setObject("weixinAccessToken_" + which, accessToken, 0);

            } else if (jsonObject != null && jsonObject.has("errcode")) {
                logger.error("Error occurs when get accessToken:{}" + jsonObject.toString());
            }
        } catch (IOException e) {
            logger.error("getAccessToken", e);
        }

        return accessToken;
    }

    /**
     * 确认请求来至微信
     *
     * @param signature 微信加密签名
     * @param echostr   随机字符串
     * @param timestamp 时间戳
     * @param nonce     随机数
     *
     * @return
     */
    public static String validate(String signature, String echostr,
                                  String timestamp, String nonce, String token) {

        String[] str = {token, timestamp, nonce};
        Arrays.sort(str); // 字典序排序
        String bigStr = str[0] + str[1] + str[2];
        // SHA1加密
        String digest = DigestUtils.byteArrayToHexString(DigestUtils.sha1(bigStr.getBytes()))
                .toLowerCase();

        // 确认请求来至微信
        if (digest.equals(signature)) {
            return echostr;
        } else
            return null;
    }

    /**
     * 获取微信js调用凭据
     *
     * @return
     */
    public static JsapiTicket getJsapiTicket(String which) {
        JsapiTicket ticket = (JsapiTicket) JedisUtils.getObject("JsapiTicket_" + which);

        if (ticket != null && ticket.isExpires() == false) return ticket;
        AccessToken accessToken = getAccessToken(which);
        String url = PublicConstants.JS_API_TICKET.replace(PublicConstants.PARAM_ACCESS_TOKEN, accessToken.getToken());

        try {
            JSONObject jsonObject = new JSONObject(HttpClientUtils.httpGet(url));
            if (jsonObject != null && jsonObject.has("ticket")) {
                ticket = new JsapiTicket();
                ticket.setTicket(jsonObject.getString("ticket"));
                ticket.setExpiresIn(jsonObject.getInt("expires_in"));
                ticket.setCreateTime(System.currentTimeMillis() / 1000);
                JedisUtils.setObject("JsapiTicket_" + which, ticket, 0);
            } else if (jsonObject != null && jsonObject.has("errcode")) {
                logger.error("Error occurs when get getJsapiTicket:{}" + jsonObject.toString());
            }
        } catch (IOException e) {
            logger.error("getJsapiTicket", e);
        }
        return ticket;
    }

    /**
     * 获取微信js接口调用配置信息
     *
     * @return
     */
    public static JsInterfaceConfig getJsInterfaceConfig(JsapiTicket ticket, String realUrl, String which) {

        JsInterfaceConfig jsInterfaceConfig = new JsInterfaceConfig();
        jsInterfaceConfig.setAppId(getAppid(which));
        jsInterfaceConfig.setNonceStr(UUID.randomUUID().toString());
        jsInterfaceConfig.setTimestamp(Long.toString(System.currentTimeMillis() / 1000));
        //注意这里参数名必须全部小写，且必须有序
        String str = "jsapi_ticket=" + ticket.getTicket() +
                "&noncestr=" + jsInterfaceConfig.getNonceStr() +
                "&timestamp=" + jsInterfaceConfig.getTimestamp() +
                "&url=" + realUrl;
        try {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(str.getBytes("UTF-8"));
            Formatter formatter = new Formatter();
            for (byte b : crypt.digest()) {
                formatter.format("%02x", b);
            }
            String signature = formatter.toString();
            formatter.close();

            jsInterfaceConfig.setSignature(signature);
        } catch (NoSuchAlgorithmException e) {
            logger.error("getJsInterfaceConfig", e);
        } catch (UnsupportedEncodingException e) {
            logger.error("getJsInterfaceConfig", e);
        }
        return jsInterfaceConfig;
    }


    /**
     * 根据code获取accessToken
     *
     * @param code
     *
     * @return { "access_token":"ACCESS_TOKEN",
     * "expires_in":7200,
     * "refresh_token":"REFRESH_TOKEN",
     * "openid":"OPENID",
     * "scope":"SCOPE" }
     */
    public static AuthorizeAccessToken getAuthorizeAccessToken(String code, String which) {

        //如果没有code 返回空
        if (StringUtils.isEmpty(code)) {
            return null;
        }
        AuthorizeAccessToken authorizeAccessToken = (AuthorizeAccessToken) JedisUtils.getObject("authorizeAccessToken_" + which + code);
        JSONObject jsonObject = null;
        // 如果存在
        if (authorizeAccessToken != null) {
            // 如果没有过期
            if (authorizeAccessToken.isExpires() == false) {
                return authorizeAccessToken;
            }
            // 如果已过期，刷新token
            else {
                authorizeAccessToken = refreshAuthorizeAccessToken(authorizeAccessToken.getRefreshToken(), which);
                if (authorizeAccessToken != null) {
                    JedisUtils.setObject("authorizeAccessToken_" + which + code, authorizeAccessToken, 0);
                    return authorizeAccessToken;
                }
            }
        }


        String url = PublicConstants.AUTHORIZE_ACCESS_TOKEN_URL.replace(CommonConstants.PARAM_APPID, PublicUtils.getAppid(which)).replace(CommonConstants.PARAM_APPSECRET, PublicUtils.getAppsecret(which)).replace(PublicConstants.PARAM_CODE, code);

        try {
            jsonObject = new JSONObject(HttpClientUtils.httpGet(url));
            authorizeAccessToken = toAuthorizeAccessToken(jsonObject, which);
            JedisUtils.setObject("authorizeAccessToken_" + which, authorizeAccessToken, 0);
            return authorizeAccessToken;
        } catch (Exception e) {
            logger.error("Error occurs when getAuthorizeAccessToken, requestUrl:" + url, e);
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 刷新accessToken
     *
     * @param refreshToken
     *
     * @return
     */
    private static AuthorizeAccessToken refreshAuthorizeAccessToken(String refreshToken, String which) {
        String url = PublicConstants.AUTHORIZE_ACCESS_TOKEN_REFRESH_URL.replace(CommonConstants.PARAM_APPID, PublicUtils.getAppid(which))
                .replace(PublicConstants.PARAM_AUTHORIZE_REFRESH_TOKEN, refreshToken);
        try {
            JSONObject jsonObject = new JSONObject(HttpClientUtils.httpGet(url));
            return toAuthorizeAccessToken(jsonObject, which);
        } catch (Exception e) {
            logger.error("Error occurs when getAuthorizeAccessToken, requestUrl:" + url, e);
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 转AuthorizeAccessToken
     *
     * @param jsonObject
     *
     * @return
     */
    private static AuthorizeAccessToken toAuthorizeAccessToken(JSONObject jsonObject, String which) {
        AuthorizeAccessToken authorizeAccessToken = null;
        if (jsonObject != null && jsonObject.has("access_token")) {
            authorizeAccessToken = new AuthorizeAccessToken();
            authorizeAccessToken.setAccessToken(jsonObject.getString("access_token"));
            authorizeAccessToken.setExpiresIn(jsonObject.getInt("expires_in"));
            authorizeAccessToken.setRefreshToken(jsonObject.getString("refresh_token"));
            authorizeAccessToken.setOpenid(jsonObject.getString("openid"));
            authorizeAccessToken.setScope(jsonObject.getString("scope"));
            authorizeAccessToken.setCreateTime(System.currentTimeMillis() / 1000);
            authorizeAccessToken.setWhich(which);
        } else if (jsonObject != null && jsonObject.has("errcode")) {
            logger.error("Error occurs when get accessToken:{}" + jsonObject.toString());
        }
        return authorizeAccessToken;
    }


    /**
     * 网页授权，获取用户信息
     *
     * @param authorizeAccessToken
     *
     * @return {    "openid":" OPENID",
     * " nickname": NICKNAME,
     * "sex":"1",
     * "province":"PROVINCE"
     * "city":"CITY",
     * "country":"COUNTRY",
     * "headimgurl":    "http://thirdwx.qlogo.cn/mmopen/g3MonUZtNHkdmzicIlibx6iaFqAc56vxLSUfpb6n5WKSYVY0ChQKkiaJSgQ1dZuTOgvLLrhJbERQQ4eMsv84eavHiaiceqxibJxCfHe/46",
     * "privilege":[ "PRIVILEGE1" "PRIVILEGE2"     ],
     * "unionid": "o6_bmasdasdsad6_2sgVt7hMZOPfL"
     * }
     */
    public static WeixinUserPo getAuthorizeWeixinUser(AuthorizeAccessToken authorizeAccessToken) {
        String url = PublicConstants.AUTH_USER_INFO_URL.replace(PublicConstants.PARAM_ACCESS_TOKEN, authorizeAccessToken.getAccessToken())
                .replace(PublicConstants.PARAM_OPENID, authorizeAccessToken.getOpenid());

        JSONObject jsonObject = null;
        WeixinUserPo weiXinUserPo = null;
        try {

            jsonObject = new JSONObject(HttpClientUtils.httpGet(url));
            if (jsonObject.has("errcode")) {
                Integer errCode = jsonObject.getInt("errcode");
                logger.error("Error occurs when getWeixinUser, requestUrl:{},error:{}", url, jsonObject);
                if (errCode != 0) {
                    throw new RuntimeException(jsonObject.toString());
                }
            }
            weiXinUserPo = new WeixinUserPo();
            weiXinUserPo.setOpenid(jsonObject.getString("openid"));
            // 只有在用户将公众号绑定到微信开放平台帐号后，才会出现该字段。
            try {
                weiXinUserPo.setUnionid(jsonObject.getString("unionid"));
            } catch (JSONException e) {

            }
            weiXinUserPo.setNickname(jsonObject.getString("nickname"));
            weiXinUserPo.setGender(jsonObject.getInt("sex") + "");
            weiXinUserPo.setCity(jsonObject.getString("city"));
            weiXinUserPo.setProvince(jsonObject.getString("province"));
            weiXinUserPo.setCountry(jsonObject.getString("country"));
            weiXinUserPo.setHeadImageUrl(jsonObject.getString("headimgurl"));
            weiXinUserPo.setHowFrom(DictEnum.WeixinUserHowFrom.webAuthorize.name());
            weiXinUserPo.setLanguage("zh_CN");
            weiXinUserPo.setType(DictEnum.WxAccountType.weixin_publicplatform.name());
            weiXinUserPo.setWhich(authorizeAccessToken.getWhich());
        } catch (Exception e) {
            logger.error("Error occurs when getWeixinUser, requestUrl:" + url, e);
            throw new RuntimeException(e.getMessage());
        }
        return weiXinUserPo;
    }

    /**
     * 发送与接收用户调换
     *
     * @param from
     * @param to
     */
    public static void userChange(Message from, Message to) {
        to.setFromUserName(from.getToUserName());
        to.setToUserName(from.getFromUserName());
    }

    /**
     * @param which
     * @param menu
     *
     * @return true 成功，false 失败
     */
    public static boolean createMenu(String which, Menu menu) {
        AccessToken accessToken = getAccessToken(which);
        String url = PublicConstants.MENU_CREATE_URL.replace(PublicConstants.PARAM_ACCESS_TOKEN, accessToken.getToken());
        try {
            JSONObject jsonObject = new JSONObject(HttpClientUtils.httpPostJson(url, JSONUtils.obj2json(menu, JsonInclude.Include.NON_NULL)));
            if (jsonObject != null && jsonObject.getInt("errcode") == 0) {
                return true;
            } else {
                logger.error("Error occurs when get getJsapiTicket:{}" + jsonObject.toString());
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return false;
    }

    /**
     * 获取配置的appid
     *
     * @param which
     *
     * @return
     */
    public static String getAppid(String which) {
        List<WeixinAccountDto> weixinAccountDtos = getWeixinAccountDtos(which);
        if (weixinAccountDtos != null && weixinAccountDtos.size() > 0) {
            return weixinAccountDtos.get(0).getAppid();
        } else {
            return PropertiesUtils.getProperty("publicplatform." + which + ".appID");
        }
    }

    /**
     * 获取配置的appsecret
     *
     * @param which
     *
     * @return
     */
    public static String getAppsecret(String which) {
        List<WeixinAccountDto> weixinAccountDtos = getWeixinAccountDtos(which);
        if (weixinAccountDtos != null && weixinAccountDtos.size() > 0) {
            return weixinAccountDtos.get(0).getAppsecret();
        } else {
            return PropertiesUtils.getProperty("publicplatform." + which + ".appsecret");
        }
    }

    /**
     * 获取配置的token
     *
     * @param which
     *
     * @return
     */
    public static String getAppToken(String which) {
        List<WeixinAccountDto> weixinAccountDtos = getWeixinAccountDtos(which);
        if (weixinAccountDtos != null && weixinAccountDtos.size() > 0) {
            return weixinAccountDtos.get(0).getToken();
        } else {
            return PropertiesUtils.getProperty("publicplatform." + which + ".token");
        }
    }

    /**
     * 微信发送模板消息
     *
     * @param wxPublicTemplate 模板实体
     * @param which            公众号类型
     */
    public static void sendWxPublicTemplateMsg(WxPublicTemplateParam wxPublicTemplate, String which) {
        sedTemlateMsg(wxPublicTemplate, which, 0);
    }

    /**
     * 微信发送模板消息
     *
     * @param wxPublicTemplate 模板实体
     * @param which            公众号类型
     * @param maxRetries       最多重试三次
     */
    private static void sedTemlateMsg(WxPublicTemplateParam wxPublicTemplate, String which, int maxRetries) {
        try {
            //获取请求路径
            String url = PublicConstants.SEND_TEMPLATE_MSG.replace(PublicConstants.PARAM_ACCESS_TOKEN, getAccessToken(which).getToken());

            logger.info("微信:{},sendWxPublicTemplateMsg 发送消息模板：{},参数：{}", which, JSONUtils.obj2json(wxPublicTemplate));
            JSONObject jsonObject = new JSONObject(HttpClientUtils.httpPostJson(url, JSONUtils.obj2json(wxPublicTemplate)));

            logger.info("微信:{},sendWxPublicTemplateMsg 发送消息模板：{},返回：{}", which, jsonObject);
            //有可能会出现："errcode":40001/42001 access_token 失效情况，需要重新获取。最多重试3次
            if (jsonObject != null && jsonObject.getInt("errcode") != 0 && maxRetries < 3) {
                maxRetries++;
                sedTemlateMsg(wxPublicTemplate, which, maxRetries);
                logger.info("微信:{},sendWxPublicTemplateMsg 发送消息模板：{},maxRetries 重试：{} 次！", which, maxRetries);
            }
        } catch (Exception e) {
            logger.error("ERROR: sendWxPublicTemplateMsg fail ：", e);
        }
    }


    /**
     * 获取数据库公众号配置
     *
     * @param which
     *
     * @return
     */
    private static List<WeixinAccountDto> getWeixinAccountDtos(String which) {
        ApiWeixinAccountPoService apiWeixinAccountPoService = SpringContextHolder.getBean(ApiWeixinAccountPoService.class);
        WeixinAccountPo weixinPo = new WeixinAccountPo();
        weixinPo.setDelFlag(BasePo.YesNo.N.name());
        weixinPo.setStatus(BasePo.YesNo.Y.name()); //有效的
        weixinPo.setWhich(which);
        weixinPo.setType(DictEnum.WxAccountType.weixin_publicplatform.name());
        return apiWeixinAccountPoService.selectList(weixinPo);
    }

    /**
     * 获取微信关注欢迎语
     *
     * @param which
     *
     * @return
     */
    public static String getWxMessage(String which) {
        List<WeixinAccountDto> weixinAccountDtos = getWeixinAccountDtos(which);
        if (weixinAccountDtos != null && weixinAccountDtos.size() > 0) {
            final WeixinAccountDto weixinAccountDto = weixinAccountDtos.get(0);
            //TODO 微信关注欢迎语暂时只支持文本类型
            if (DictEnum.WxAccountType.weixin_publicplatform.name().equals(weixinAccountDto.getTemplateType()) && StringUtils.isNotBlank(weixinAccountDto.getTemplate())) {
                return weixinAccountDtos.get(0).getTemplate();
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * 获取已添加至帐号下所有模板列表，可在微信公众平台后台中查看模板列表信息
     *
     * @param which
     *
     * @return
     */
    public static List<WxTemplate> getWxAllPrivateTemplate(String which, String type) {
        String url = PublicConstants.GET_ALL_PRIVATE_TEMPLATE_URL.replace(PublicConstants.PARAM_ACCESS_TOKEN, PublicUtils.getAccessToken(which).getToken());

        JSONObject jsonObject = null;
        List<WxTemplate> wxTemplates = new ArrayList<>();
        try {

            jsonObject = new JSONObject(HttpClientUtils.httpGet(url));
            logger.info("《=====获取已添加至微信公众帐号下所有模板列表：{},{}", which, jsonObject.toString());
            if (jsonObject != null && jsonObject.has("errcode")) {
                Integer errCode = jsonObject.getInt("errcode");
                logger.error("Error occurs when getWxAllPrivateTemplate, requestUrl:{},error:{}", url, jsonObject);
                if (errCode != 0) {
                    throw new RuntimeException(jsonObject.toString());
                }
            }
            try {
                JSONArray jsonArray = (JSONArray) jsonObject.get("template_list");
                for (int i = 0; i < jsonArray.length(); i++) {
                    String template_id = jsonArray.getJSONObject(i).getString("template_id");
                    String title = jsonArray.getJSONObject(i).getString("title");
                    String primary_industry = jsonArray.getJSONObject(i).getString("primary_industry");
                    String deputy_industry = jsonArray.getJSONObject(i).getString("deputy_industry");
                    String content = jsonArray.getJSONObject(i).getString("content");
                    String example = jsonArray.getJSONObject(i).getString("example");
                    WxTemplate wxTemplate = new WxTemplate(template_id, title, primary_industry, deputy_industry, content, example);
                    wxTemplate.setType(type);
                    wxTemplates.add(wxTemplate);
                }
            } catch (JSONException e) {

            }
        } catch (Exception e) {
            logger.error("Error occurs when getWxAllPrivateTemplate, requestUrl:" + url, e);
            throw new RuntimeException(e.getMessage());
        }
        return wxTemplates;
    }
}
