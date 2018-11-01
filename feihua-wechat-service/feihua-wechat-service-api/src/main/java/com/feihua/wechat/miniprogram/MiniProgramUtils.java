package com.feihua.wechat.miniprogram;

import com.feihua.framework.jedis.utils.JedisUtils;
import com.feihua.utils.http.httpclient.HttpClientUtils;
import com.feihua.utils.json.JSONUtils;
import com.feihua.utils.properties.PropertiesUtils;
import com.feihua.utils.spring.SpringContextHolder;
import com.feihua.wechat.CommonConstants;
import com.feihua.wechat.common.WxPublicConstants;
import com.feihua.wechat.common.api.ApiWeixinAccountPoService;
import com.feihua.wechat.common.dto.WeixinAccountDto;
import com.feihua.wechat.common.dto.WxTemplate;
import com.feihua.wechat.common.po.WeixinAccountPo;
import com.feihua.wechat.miniprogram.dto.MsgTemplateParamDto;
import com.feihua.wechat.miniprogram.dto.ResponseMessage;
import com.feihua.wechat.publicplatform.PublicUtils;
import com.feihua.wechat.publicplatform.dto.AccessToken;
import feihua.jdbc.api.pojo.BasePo;
import org.apache.commons.beanutils.BeanUtils;
import org.dom4j.DocumentException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yangwei
 * Created at 2018/8/16 13:48
 */
public class MiniProgramUtils {


    private static Logger logger = LoggerFactory.getLogger(MiniProgramUtils.class);

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

        return PublicUtils.validate(signature, echostr, timestamp, nonce, token);
    }

    /**
     * 获取微信调用凭据
     *
     * @return
     */
    public static AccessToken getAccessToken(String which) {
        String key = "miniprogramAccessToken_";
        AccessToken accessToken = (AccessToken) JedisUtils.getObject(key + which);

        if (accessToken != null && accessToken.isExpires() == false) return accessToken;

        String url = MiniConstants.GET_ACCESS_TOKEN_URL.replace(CommonConstants.PARAM_APPID, MiniProgramUtils.getAppid(which)).replace(CommonConstants.PARAM_APPSECRET, MiniProgramUtils.getAppsecret(which));
        try {
            JSONObject jsonObject = new JSONObject(HttpClientUtils.httpGet(url));
            if (jsonObject != null && jsonObject.has("access_token")) {
                accessToken = new AccessToken();
                accessToken.setToken(jsonObject.getString("access_token"));
                accessToken.setExpiresIn(jsonObject.getInt("expires_in"));
                accessToken.setCreateTime(System.currentTimeMillis() / 1000);
                accessToken.setWhich(which);
                JedisUtils.setObject(key + which, accessToken, 0);

            } else if (jsonObject != null && jsonObject.has("errcode")) {
                logger.error("Error occurs when get accessToken:{}" + jsonObject.toString());
            }
        } catch (IOException e) {
            logger.error("getAccessToken", e);
        }

        return accessToken;
    }

    /**
     * 获取配置的消息类型
     *
     * @param which
     *
     * @return
     */
    public static String getAppMsgType(String which) {
        List<WeixinAccountDto> weixinAccountDtos = getWeixinAccountDtos(which);
        if (weixinAccountDtos != null && weixinAccountDtos.size() > 0) {
            return weixinAccountDtos.get(0).getMsgType();
        } else {
            return PropertiesUtils.getProperty("miniprogram." + which + ".msgtype");
        }

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
            return PropertiesUtils.getProperty("miniprogram." + which + ".appID");
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
            return PropertiesUtils.getProperty("miniprogram." + which + ".appsecret");
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
            return PropertiesUtils.getProperty("miniprogram." + which + ".token");
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
        weixinPo.setType(WxPublicConstants.WxAccountType.WEIXIN_MINIPROGRAM.value());
        return apiWeixinAccountPoService.selectList(weixinPo);
    }

    /**
     * 发送模板消息
     *
     * @param msgTemplateParamDto
     * @param which
     */
    public static void sendMsgTemplate(MsgTemplateParamDto msgTemplateParamDto, String which) {
        AccessToken accessToken = getAccessToken(which);
        String url = MiniConstants.MSG_TEMPLATE_URL.replace(MiniConstants.PARAM_ACCESS_TOKEN, accessToken.getToken());

        try {
            JSONObject jsonObject = new JSONObject(HttpClientUtils.httpPostJson(url, JSONUtils.obj2json(msgTemplateParamDto)));
            if (jsonObject != null && jsonObject.has("errcode") && 0 == jsonObject.getInt("errcode")) {
                //正常发送
            } else if (jsonObject != null) {
                logger.error("sendMsgTemplate error with response {}", jsonObject.toString());
            } else {
                logger.error(jsonObject.toString());
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * 发送客服消息
     *
     * @param responseMessage
     * @param which
     */
    public static void sendMsgCustom(ResponseMessage responseMessage, String which) {
        AccessToken accessToken = getAccessToken(which);
        String url = MiniConstants.MSG_CUSTOM_URL.replace(MiniConstants.PARAM_ACCESS_TOKEN, accessToken.getToken());

        try {
            JSONObject jsonObject = new JSONObject(HttpClientUtils.httpPostJson(url, JSONUtils.obj2json(responseMessage)));
            if (jsonObject != null && jsonObject.has("errcode") && 0 == jsonObject.getInt("errcode")) {
                //正常发送
            } else if (jsonObject != null) {
                logger.error("sendMsgTemplate error with response {}", jsonObject.toString());
            } else {
                logger.error(jsonObject.toString());
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

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
        return PublicUtils.xmlToMessage(xmlRequestMessage, msg);
    }

    /**
     * 将json消息转为对象
     *
     * @param jsonRequestMessage
     * @param msg                应该为一个message的实现类对象
     *
     * @return
     */
    public static <T> T jsonToMessage(String jsonRequestMessage, T msg) {
        Map<String, Object> map = null;
        try {
            map = JSONUtils.json2map(jsonRequestMessage);

            if (map != null) {
                for (String key : map.keySet()) {
                    String fieldName = key.substring(0, 1).toLowerCase()
                            + key.substring(1);
                    BeanUtils.setProperty(msg, fieldName, map.get(key));
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return msg;
    }

    /**
     * 获取已添加至微信小程序帐号下模板列表
     * 返回参数：
     * <p>
     * {
     * "errcode": 0,
     * "errmsg": "ok",
     * "list": [
     * {
     * "template_id": "wDYzYZVxobJivW9oMpSCpuvACOfJXQIoKUm0PY397Tc",
     * "title": "购买成功通知",
     * "content": "购买地点{{keyword1.DATA}}\n购买时间{{keyword2.DATA}}\n物品名称{{keyword3.DATA}}\n",
     * "example": "购买地点：TIT造舰厂\n购买时间：2016年6月6日\n物品名称：咖啡\n"
     * }
     * ]
     * }
     * POST --->请求参数：
     * {
     * "offset": 0, 当前页
     * "count": 1   每页条数，最大20条
     * }
     *
     * @param which
     * @param type
     *
     * @return
     */
    public static List<WxTemplate> getWxAllPrivateTemplate(String which, String type) {
        String url = MiniConstants.GET_MINIPROGRAM_TEMPLATE_URL.replace(MiniConstants.PARAM_ACCESS_TOKEN, getAccessToken(which).getToken());

        JSONObject jsonObject = null;
        List<WxTemplate> wxTemplates = new ArrayList<>();
        try {
            Map param = new HashMap();
            param.put("offset", "0");
            param.put("count", "20");
            jsonObject = new JSONObject(HttpClientUtils.httpPost(url, param));
            logger.info("《=====获取已添加至微信小程序帐号下模板列表：{},{}", which, jsonObject.toString());
            if (jsonObject != null && jsonObject.has("errcode")) {
                Integer errCode = jsonObject.getInt("errcode");
                logger.error("Error occurs when getWxAllPrivateTemplate, requestUrl:{},error:{}", url, jsonObject);
                if (errCode != 0) {
                    throw new RuntimeException(jsonObject.toString());
                }
            }
            try {
                JSONArray jsonArray = (JSONArray) jsonObject.get("list");
                for (int i = 0; i < jsonArray.length(); i++) {
                    String template_id = jsonArray.getJSONObject(i).getString("template_id");
                    String title = jsonArray.getJSONObject(i).getString("title");
                    String content = jsonArray.getJSONObject(i).getString("content");
                    String example = jsonArray.getJSONObject(i).getString("example");
                    WxTemplate wxTemplate = new WxTemplate(template_id, title, null, null, content, example);
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
