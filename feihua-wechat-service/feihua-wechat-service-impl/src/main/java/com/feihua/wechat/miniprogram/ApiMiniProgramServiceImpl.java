package com.feihua.wechat.miniprogram;

import com.feihua.utils.http.httpclient.HttpClientUtils;
import com.feihua.utils.json.JSONUtils;
import com.feihua.utils.xml.XmlUtils;
import com.feihua.wechat.CommonConstants;
import com.feihua.wechat.ParamsDto;
import com.feihua.wechat.miniprogram.api.ApiMiniProgramService;
import com.feihua.wechat.miniprogram.api.MsgTypeHandler;
import com.feihua.wechat.miniprogram.dto.LoginCredentialsDto;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

/**
 * Created by yangwei
 * Created at 2018/4/27 18:29
 */
@Service
public class ApiMiniProgramServiceImpl implements ApiMiniProgramService {

    private  static Logger logger = LoggerFactory.getLogger(ApiMiniProgramServiceImpl.class);

    private static String defaultResponse = "success";

    @Autowired(required = false)
    private Map<String,MsgTypeHandler> msgTypeHandlerMap;

    @Override
    public String processXmlMsg(String postXmlData, String which) {
        Document document = XmlUtils.stringToDocument(postXmlData);
        String messageType = XmlUtils.getElementText("MsgType",document);
        String event = XmlUtils.getElementText("Event",document);
        MsgTypeHandler msgTypeHandler = null;
        if(msgTypeHandlerMap !=null ){
            String stuffix = "";
            if(StringUtils.isNotEmpty(event)){
                stuffix = "_"+ event;
            }
            msgTypeHandler = msgTypeHandlerMap.get(messageType + stuffix);
        }
        return msgTypeHandler == null? defaultResponse : msgTypeHandler.handleXmlMsg(postXmlData,which);
    }

    @Override
    public String processjsonMsg(String postJsonData, String which) {
        Map<String,Object> data = null;
        try {
            data = JSONUtils.json2map(postJsonData);

        } catch (Exception e) {
            logger.error(e.getMessage(),e);
        }

        String messageType = data.get("MsgType").toString();
        MsgTypeHandler msgTypeHandler = null;
        if(msgTypeHandlerMap !=null ){
            msgTypeHandler = msgTypeHandlerMap.get(messageType);
        }

        return msgTypeHandler == null? defaultResponse : msgTypeHandler.handleJsonMsg(postJsonData,which);
    }

    public LoginCredentialsDto fetchLoginCredentials(String code, ParamsDto paramsDto) {
        String r = null;
        // 请求的url和参数
        String url = MiniConstants.ACCESS_TOKEN_URL.replace(MiniConstants.PARAM_JSCODE,code)
                .replace(CommonConstants.PARAM_APPID,paramsDto.getAppId())
                .replace(CommonConstants.PARAM_APPSECRET,paramsDto.getSecret());
        // 请求
        try {
            r = HttpClientUtils.httpGet(url);
        } catch (IOException e) {
            logger.error(e.getMessage(),e);
        }
        // 返回结果封装
        LoginCredentialsDto loginCredentialsDto = null;
        if (r != null) {
            Map<String,Object> map = null;
            try {
                map = JSONUtils.json2map(r);
            } catch (Exception e) {
                logger.error(e.getMessage(),e);
            }
            if (map != null) {
                loginCredentialsDto = new LoginCredentialsDto();
                loginCredentialsDto.setOpenid(map.get("openid").toString());
                loginCredentialsDto.setSessionKey(map.get("session_key").toString());
                Object unionid = map.get("unionid");
                if (unionid != null) {
                    loginCredentialsDto.setOpenid(unionid.toString());
                }
            }
        }
        return  loginCredentialsDto;
    }
}
