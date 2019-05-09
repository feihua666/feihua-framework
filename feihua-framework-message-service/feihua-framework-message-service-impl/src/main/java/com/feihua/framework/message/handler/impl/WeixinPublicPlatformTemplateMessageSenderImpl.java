package com.feihua.framework.message.handler.impl;

import com.feihua.exception.ParamInvalidException;
import com.feihua.framework.base.modules.loginclient.api.ApiBaseLoginClientChannelBindPoService;
import com.feihua.framework.base.modules.loginclient.dto.BaseLoginClientDto;
import com.feihua.framework.base.modules.loginclient.po.BaseLoginClientChannelBindPo;
import com.feihua.framework.base.modules.user.api.ApiBaseUserAuthPoService;
import com.feihua.framework.base.modules.user.dto.BaseUserAuthDto;
import com.feihua.framework.base.modules.user.po.BaseUserPo;
import com.feihua.framework.constants.DictEnum;
import com.feihua.framework.message.api.ApiBaseMessageThirdPoService;
import com.feihua.framework.message.api.ApiBaseMessageVUserPoService;
import com.feihua.framework.message.dto.BaseMessageClientDto;
import com.feihua.framework.message.dto.ClientMessageSendParamDto;
import com.feihua.framework.message.dto.VClientMessageSendParamDto;
import com.feihua.framework.message.handler.ApiClientMessageSender;
import com.feihua.framework.message.handler.ApiVClientMessageSender;
import com.feihua.framework.message.po.BaseMessagePo;
import com.feihua.framework.message.po.BaseMessageThirdPo;
import com.feihua.utils.collection.CollectionUtils;
import com.feihua.utils.json.JSONUtils;
import com.feihua.utils.spring.SpringContextHolder;
import com.feihua.wechat.common.api.ApiWeixinAccountPoService;
import com.feihua.wechat.common.dto.Miniprogram;
import com.feihua.wechat.common.dto.WxPublicTemplateData;
import com.feihua.wechat.publicplatform.api.ApiPublicTemplateMessageService;
import com.feihua.wechat.publicplatform.dto.WxPublicTemplateParam;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 微信公众平台模板消息发送,只支持非虚拟客户端
 * Created by yangwei
 * Created at 2018/11/1 18:24
 */
public class WeixinPublicPlatformTemplateMessageSenderImpl implements ApiVClientMessageSender, ApiClientMessageSender {

    private static final Logger logger = LoggerFactory.getLogger(WeixinPublicPlatformTemplateMessageSenderImpl.class);

    ApiPublicTemplateMessageService apiPublicTemplateMessageService;
    ApiBaseLoginClientChannelBindPoService apiBaseLoginClientChannelBindPoService;
    ApiBaseMessageThirdPoService apiBaseMessageThirdPoService;
    ApiBaseUserAuthPoService apiBaseUserAuthPoService;
    ApiBaseMessageVUserPoService apiBaseMessageVUserPoService;
    @Override
    public void doMessageSend(VClientMessageSendParamDto dto) {
        send(dto.getBaseMessagePo(),dto.getClientDto(),dto.getUserIdentifiers(),null,dto.getCurrentUserId());
    }

    @Override
    public void doMessageSend(ClientMessageSendParamDto dto) {
        send(dto.getBaseMessagePo(),dto.getClientDto(),null,dto.getUserPos(),dto.getCurrentUserId());

    }
    @Override
    public boolean support(BaseLoginClientDto clientDto, List<BaseMessageClientDto> messageClientDtos) {
        if(AbstractClientMessageSenderImpl.isContents(DictEnum.MessageClientType.wx_publicplatform_template.name(),messageClientDtos)){
            return true;
        }
        return false;
    }

    private void send(BaseMessagePo baseMessagePo,BaseLoginClientDto clientDto,List<String> userIdentifiers,List<BaseUserPo> userPos,String currentUserId){

        if (apiPublicTemplateMessageService == null) {
            apiPublicTemplateMessageService = SpringContextHolder.getBean(ApiPublicTemplateMessageService.class);
        }
        if (apiBaseLoginClientChannelBindPoService == null) {
            apiBaseLoginClientChannelBindPoService = SpringContextHolder.getBean(ApiBaseLoginClientChannelBindPoService.class);
        }
        if (apiBaseMessageThirdPoService == null) {
            apiBaseMessageThirdPoService = SpringContextHolder.getBean(ApiBaseMessageThirdPoService.class);
        }
        if (apiBaseUserAuthPoService == null) {
            apiBaseUserAuthPoService = SpringContextHolder.getBean(ApiBaseUserAuthPoService.class);
        }
        if (apiBaseMessageVUserPoService == null) {
            apiBaseMessageVUserPoService = SpringContextHolder.getBean(ApiBaseMessageVUserPoService.class);
        }

        // 查询客户端绑定渠道
        BaseLoginClientChannelBindPo clientChannelBindPo = apiBaseLoginClientChannelBindPoService.selectByChannelTypeAndClientId(DictEnum.WxAccountType.weixin_publicplatform.name(),clientDto.getId());
        if (clientChannelBindPo == null) {
            throw new ParamInvalidException("clientChannelBindPo is null,check client channel config please");
        }
        List<BaseMessageThirdPo> messageThirdPos = apiBaseMessageThirdPoService.selectByMessageIdAndThirdType(baseMessagePo.getId(),DictEnum.WxAccountType.weixin_publicplatform.name());
        if (messageThirdPos == null) {
            throw new ParamInvalidException("messageThirdPos is null,WeixinPublicPlatformTemplateMessageSender need third template");

        }
        for (BaseMessageThirdPo messageThirdPo : messageThirdPos) {
            String param = messageThirdPo.getThirdTemplateContent();
            Map<String,Object> jsonMap = null;
            try {
                jsonMap = JSONUtils.json2map(param);
            } catch (Exception e) {
                logger.error(e.getMessage(),e);
                throw new ParamInvalidException("message third template json2map exception with messageId={}",baseMessagePo.getId());
            }

            WxPublicTemplateParam wxPublicTemplateParam = new WxPublicTemplateParam();

            String templateId = messageThirdPo.getThirdTemplateId();
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
            if (!CollectionUtils.isNullOrEmpty(userIdentifiers)) {
                for (String userIdentifier : userIdentifiers) {
                    wxPublicTemplateParam.setTouser(userIdentifier);
                    try {
                        apiPublicTemplateMessageService.send(wxPublicTemplateParam,clientChannelBindPo.getChannelId());
                        // 更新发送状态
                        apiBaseMessageVUserPoService.updateMessageStage(baseMessagePo.getId(),userIdentifier,clientDto.getId(),currentUserId,DictEnum.MessageState.sended);
                    } catch (Exception e) {
                        logger.error(e.getMessage(),e);
                    }

                }
            }
            if (!CollectionUtils.isNullOrEmpty(userPos)) {
                for (BaseUserPo userPo : userPos) {
                    BaseUserAuthDto userAuthDto = apiBaseUserAuthPoService.selectByUserIdAndType(userPo.getId(),DictEnum.LoginType.WX_PLATFORM.name());
                    if (userAuthDto != null) {
                        wxPublicTemplateParam.setTouser(userAuthDto.getIdentifier());
                        try {
                            apiPublicTemplateMessageService.send(wxPublicTemplateParam,clientChannelBindPo.getChannelId());
                        } catch (Exception e) {
                            logger.error(e.getMessage(),e);
                        }
                    }

                }
            }

        }
    }

}
