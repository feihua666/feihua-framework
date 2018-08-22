package com.feihua.wechat.miniprogram;

import com.feihua.framework.jedis.utils.JedisUtils;
import com.feihua.utils.http.httpclient.HttpClientUtils;
import com.feihua.utils.json.JSONUtils;
import com.feihua.utils.properties.PropertiesUtils;
import com.feihua.wechat.CommonConstants;
import com.feihua.wechat.miniprogram.dto.MsgTemplateParamDto;
import com.feihua.wechat.miniprogram.dto.RequestMessage;
import com.feihua.wechat.miniprogram.dto.ResponseMessage;
import com.feihua.wechat.publicplatform.PublicUtils;
import com.feihua.wechat.publicplatform.dto.AccessToken;
import org.apache.commons.beanutils.BeanUtils;
import org.dom4j.DocumentException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
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
     * @return
     */
    public static String validate(String signature, String echostr,
                                  String timestamp, String nonce, String token) {

        return PublicUtils.validate(signature,echostr,timestamp,nonce,token);
    }
    /**
     * 获取微信调用凭据
     * @return
     */
    public static AccessToken getAccessToken(String which){
        String key = "miniprogramAccessToken_";
        AccessToken accessToken = (AccessToken) JedisUtils.getObject(key + which);

        if(accessToken != null && accessToken.isExpires() == false) return accessToken;

        String url = MiniConstants.ACCESS_TOKEN_URL.replace(CommonConstants.PARAM_APPID, MiniProgramUtils.getAppid(which)).replace(CommonConstants.PARAM_APPSECRET,MiniProgramUtils.getAppsecret(which));
        try {
            JSONObject jsonObject = new JSONObject(HttpClientUtils.httpGet(url));
            if (jsonObject != null && jsonObject.has("access_token")) {
                accessToken = new AccessToken();
                accessToken.setToken(jsonObject.getString("access_token"));
                accessToken.setExpiresIn(jsonObject.getInt("expires_in"));
                accessToken.setCreateTime(System.currentTimeMillis() / 1000);
                accessToken.setWhich(which);
                JedisUtils.setObject(key + which,accessToken,0);

            } else if (jsonObject != null && jsonObject.has("errcode")) {
                logger.error("Error occurs when get accessToken:{}"+jsonObject.toString());
            }
        } catch (IOException e) {
            logger.error("getAccessToken",e);
        }

        return accessToken;
    }
    /**
     * 获取配置的appid
     * @param which
     * @return
     */
    public static String getAppid(String which){
        return PropertiesUtils.getProperty("miniprogram."+ which +".appID");
    }

    /**
     * 获取配置的appsecret
     * @param which
     * @return
     */
    public static String getAppsecret(String which){
        return PropertiesUtils.getProperty("miniprogram."+ which +".appsecret");
    }

    /**
     * 获取配置的token
     * @param which
     * @return
     */
    public static String getAppToken(String which){
        return PropertiesUtils.getProperty("miniprogram."+ which +".token");
    }

    /**
     * 获取配置的消息类型
     * @param which
     * @return
     */
    public static String getAppMsgType(String which){
        return PropertiesUtils.getProperty("miniprogram."+ which +".msgtype");
    }
    /**
     * 发送模板消息
     * @param msgTemplateParamDto
     * @param which
     */
    public static void sendMsgTemplate(MsgTemplateParamDto msgTemplateParamDto,String which){
        AccessToken accessToken = getAccessToken(which);
        String url = MiniConstants.MSG_TEMPLATE_URL.replace(MiniConstants.PARAM_ACCESS_TOKEN,accessToken.getToken());

        try {
            JSONObject jsonObject = new JSONObject(HttpClientUtils.httpPostJson(url, JSONUtils.obj2json(msgTemplateParamDto)));
            if (jsonObject != null && jsonObject.has("errcode") && 0 == jsonObject.getInt("errcode")) {
                //正常发送
            }else if(jsonObject != null){
                logger.error("sendMsgTemplate error with response {}",jsonObject.toString());
            }else {
                logger.error(jsonObject.toString());
            }
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
        }
    }
    /**
     * 发送客服消息
     * @param responseMessage
     * @param which
     */
    public static void sendMsgCustom(ResponseMessage responseMessage, String which){
        AccessToken accessToken = getAccessToken(which);
        String url = MiniConstants.MSG_custom_URL.replace(MiniConstants.PARAM_ACCESS_TOKEN,accessToken.getToken());

        try {
            JSONObject jsonObject = new JSONObject(HttpClientUtils.httpPostJson(url, JSONUtils.obj2json(responseMessage)));
            if (jsonObject != null && jsonObject.has("errcode") && 0 == jsonObject.getInt("errcode")) {
                //正常发送
            }else if(jsonObject != null){
                logger.error("sendMsgTemplate error with response {}",jsonObject.toString());
            }else {
                logger.error(jsonObject.toString());
            }
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
        }
    }
    /**
     * 将xml消息转为对象
     * @param xmlRequestMessage
     * @param msg 应该为一个message的实现类对象
     * @return
     * @throws DocumentException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public static <T> T  xmlToMessage(String xmlRequestMessage,T msg){
        return PublicUtils.xmlToMessage(xmlRequestMessage,msg);
    }

    /**
     * 将json消息转为对象
     * @param jsonRequestMessage
     * @param msg 应该为一个message的实现类对象
     * @return
     */
    public static <T> T  jsonToMessage(String jsonRequestMessage,T msg){
        Map<String,Object> map = null;
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
            logger.error(e.getMessage(),e);
        }

        return msg;
    }
}
