package com.feihua.framework.message.handler.impl;

import com.feihua.exception.ParamInvalidException;
import com.feihua.framework.base.modules.loginclient.api.ApiBaseLoginClientChannelBindPoService;
import com.feihua.framework.base.modules.loginclient.dto.BaseLoginClientDto;
import com.feihua.framework.base.modules.loginclient.po.BaseLoginClientChannelBindPo;
import com.feihua.framework.base.modules.user.api.ApiBaseUserAuthPoService;
import com.feihua.framework.base.modules.user.dto.BaseUserAuthDto;
import com.feihua.framework.base.modules.user.po.BaseUserPo;
import com.feihua.framework.constants.DictEnum;
import com.feihua.framework.message.dto.ClientMessageSendParamDto;
import com.feihua.framework.message.dto.MsgTemplateThirdParamDto;
import com.feihua.utils.json.JSONUtils;
import com.feihua.utils.spring.SpringContextHolder;
import com.feihua.wechat.common.api.ApiWeixinAccountPoService;
import com.feihua.wechat.common.dto.Miniprogram;
import com.feihua.wechat.common.dto.WxPublicTemplateData;
import com.feihua.wechat.common.po.WeixinAccountPo;
import com.feihua.wechat.publicplatform.api.ApiPublicTemplateMessageService;
import com.feihua.wechat.publicplatform.dto.WxPublicTemplateParam;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * 微信公众平台模板消息发送
 * Created by yangwei
 * Created at 2018/11/1 18:24
 */
public class H5WeixinPublicPlatformTemplateMessageSenderImpl extends AbstractClientMessageSenderImpl {

    private static final Logger logger = LoggerFactory.getLogger(H5WeixinPublicPlatformTemplateMessageSenderImpl.class);

    @Override
    public void doMessageSend(ClientMessageSendParamDto dto) {
        ApiPublicTemplateMessageService apiPublicTemplateMessageService = SpringContextHolder.getBean(ApiPublicTemplateMessageService.class);
        ApiWeixinAccountPoService apiWeixinAccountPoService = SpringContextHolder.getBean(ApiWeixinAccountPoService.class);
        ApiBaseUserAuthPoService apiBaseUserAuthPoService = SpringContextHolder.getBean(ApiBaseUserAuthPoService.class);

        // 查询客户端绑定渠道
        BaseLoginClientChannelBindPo clientChannelBindPo = dto.getClientChannelBindPo();
        if (clientChannelBindPo == null) {
            throw new ParamInvalidException("clientChannelBindPo is null,check client channel config please");
        }
        // 查询渠道实体
        /*WeixinAccountPo weixinAccountPo = apiWeixinAccountPoService.selectByPrimaryKeySimple(clientChannelBindPo.getChannelId(),false);
        // 判断是否为空，为空未设置渠道，不发送
        if (weixinAccountPo == null) {
            logger.debug("H5WeixinPublicPlatformTemplateMessageSender can not find weixinAccount by clientId={},do you have bind?",dto.getClientDto().getId());
            return;
        }*/
        MsgTemplateThirdParamDto msgTemplateThirdParamDto = dto.getTemplateThirdParamDto();
        if (msgTemplateThirdParamDto == null) {
            throw new ParamInvalidException("msgTemplateThirdParamDto is null,check msg template config please");

        }
        String param = msgTemplateThirdParamDto.getThirdTemplateContent();
        Map<String,Object> jsonMap = null;
        try {
            jsonMap = JSONUtils.json2map(param);
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            throw new ParamInvalidException("third template json2map exception with templateId={}",msgTemplateThirdParamDto.getTemplateThirdBindPo().getId());
        }

        WxPublicTemplateParam wxPublicTemplateParam = new WxPublicTemplateParam();

        String templateId = msgTemplateThirdParamDto.getTemplateThirdBindPo().getThirdTemplateId();
        if(StringUtils.isEmpty(templateId)){
            templateId = (String) jsonMap.get("template_id");
        }
        wxPublicTemplateParam.setTemplate_id(templateId);
        wxPublicTemplateParam.setUrl((String) jsonMap.get("url"));
        Map<String,Object> miniprogramMap = (Map<String, Object>) jsonMap.get("miniprogram");
        if (miniprogramMap != null){
            Miniprogram miniprogram = new Miniprogram((String)miniprogramMap.get("appid"),(String)miniprogramMap.get("pagepath"));
            wxPublicTemplateParam.setMiniprogram(miniprogram);
        }

        Map<String, WxPublicTemplateData> data = new HashMap<>();
        Map<String,Object> dataMap = (Map<String, Object>) jsonMap.get("data");
        for (String key : dataMap.keySet()) {
            Map<String,Object> valueMap = (Map<String, Object>) dataMap.get(key);
            data.put(key,new WxPublicTemplateData(((String) valueMap.get("value")), ((String) valueMap.get("color"))));
        }
        wxPublicTemplateParam.setData(data);
        for (BaseUserPo userPo : dto.getUserPos()) {
            BaseUserAuthDto userAuthDto = apiBaseUserAuthPoService.selectByUserIdAndType(userPo.getId(),DictEnum.LoginType.WX_PLATFORM.name());
            wxPublicTemplateParam.setTouser(userAuthDto.getIdentifier());
            apiPublicTemplateMessageService.send(wxPublicTemplateParam,clientChannelBindPo.getChannelId());
        }
    }

    @Override
    public boolean support(BaseLoginClientDto clientDto) {
        return DictEnum.LoginClientType.wx_publicplatform.name().equals(clientDto.getClientType());
    }
}
