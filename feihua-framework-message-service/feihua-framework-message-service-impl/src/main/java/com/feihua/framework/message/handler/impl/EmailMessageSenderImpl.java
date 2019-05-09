package com.feihua.framework.message.handler.impl;

import com.feihua.framework.base.modules.config.api.ApiBaseConfigService;
import com.feihua.framework.base.modules.config.po.BaseConfig;
import com.feihua.framework.base.modules.loginclient.dto.BaseLoginClientDto;
import com.feihua.framework.base.modules.user.api.ApiBaseUserAuthPoService;
import com.feihua.framework.base.modules.user.dto.BaseUserAuthDto;
import com.feihua.framework.base.modules.user.po.BaseUserPo;
import com.feihua.framework.constants.DictEnum;
import com.feihua.framework.message.api.ApiBaseMessageVUserPoService;
import com.feihua.framework.message.dto.BaseMessageClientDto;
import com.feihua.framework.message.dto.ClientMessageSendParamDto;
import com.feihua.framework.message.dto.VClientMessageSendParamDto;
import com.feihua.framework.message.handler.ApiClientMessageSender;
import com.feihua.framework.message.handler.ApiVClientMessageSender;
import com.feihua.framework.message.po.BaseMessagePo;
import com.feihua.utils.EmailUtils.EmailUtils;
import com.feihua.utils.EmailUtils.Mail;
import com.feihua.utils.collection.CollectionUtils;
import com.feihua.utils.json.JSONUtils;
import com.feihua.utils.spring.SpringContextHolder;
import org.apache.commons.lang3.BooleanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.Session;
import java.util.List;
import java.util.Map;

/**
 * 邮件消息发送
 * Created by yangwei
 * Created at 2019/5/8 13:30
 */
public class EmailMessageSenderImpl implements ApiVClientMessageSender, ApiClientMessageSender {
    private static final Logger logger = LoggerFactory.getLogger(EmailMessageSenderImpl.class);

    private Mysession mysession;
    // 用户比对配置是否发生变化
    private BaseConfig oldBaseConfig;

    ApiBaseUserAuthPoService apiBaseUserAuthPoService;
    ApiBaseMessageVUserPoService apiBaseMessageVUserPoService;


    @Override
    public void doMessageSend(ClientMessageSendParamDto dto) {
        send(dto.getBaseMessagePo(),dto.getClientDto(),null,dto.getUserPos(),dto.getCurrentUserId());

    }

    @Override
    public void doMessageSend(VClientMessageSendParamDto dto) {
        send(dto.getBaseMessagePo(),dto.getClientDto(),dto.getUserIdentifiers(),null,dto.getCurrentUserId());

    }

    @Override
    public boolean support(BaseLoginClientDto clientDto, List<BaseMessageClientDto> messageClientDtos) {
        if(AbstractClientMessageSenderImpl.isContents(DictEnum.MessageClientType.email.name(),messageClientDtos)){
            return true;
        }
        return false;
    }
    private void send(BaseMessagePo baseMessagePo, BaseLoginClientDto clientDto, List<String> userIdentifiers, List<BaseUserPo> userPos,String currentUserId){

        Mysession mysession = null;
        try {
            mysession = getSession();
        } catch (Exception e) {
            logger.error("getSession error",e);
        }
        if (mysession == null || mysession.getSession() == null) {
            return;
        }

        if (!CollectionUtils.isNullOrEmpty(userIdentifiers)) {
            for (String userIdentifier : userIdentifiers) {
                doSend(userIdentifier,mysession.getSession(),baseMessagePo,mysession.getSenderUsername(),clientDto,currentUserId);
            }
        }
        if (apiBaseUserAuthPoService == null) {
            apiBaseUserAuthPoService = SpringContextHolder.getBean(ApiBaseUserAuthPoService.class);
        }
        if (apiBaseMessageVUserPoService == null) {
            apiBaseMessageVUserPoService = SpringContextHolder.getBean(ApiBaseMessageVUserPoService.class);
        }
        if (!CollectionUtils.isNullOrEmpty(userPos)) {
            for (BaseUserPo userPo : userPos) {
                BaseUserAuthDto userAuthDto = apiBaseUserAuthPoService.selectByUserIdAndType(userPo.getId(),DictEnum.LoginType.EMAIL.name());
                if (userAuthDto != null) {
                    doSend(userAuthDto.getIdentifier(),mysession.getSession(),baseMessagePo,mysession.getSenderUsername(),clientDto,currentUserId);
                }
            }
        }
    }
    private void doSend(String address,Session session,BaseMessagePo messagePo,String from,BaseLoginClientDto clientDto,String currentUserId){
        Mail mail = new Mail();
        mail.addToAddress(address);
        mail.setSubject(messagePo.getTitle());
        mail.setFrom(from);
        mail.setContent(messagePo.getContent());
        try {
            EmailUtils.send(session,mail);
            // 更新发送状态
            apiBaseMessageVUserPoService.updateMessageStage(messagePo.getId(),address,clientDto.getId(),currentUserId,DictEnum.MessageState.sended);
        }catch (Exception e){
            logger.error(e.getMessage() + " EmailUtils.send",e);
        }
    }
    private Mysession getSession() throws Exception {
        ApiBaseConfigService apiBaseConfigService = SpringContextHolder.getBean(ApiBaseConfigService.class);
        BaseConfig config = apiBaseConfigService.selectByConfigKey("EMAIL_SEND_CONFIG_KEY");

        if (mysession != null) {
            if (config == null) {
                return mysession;
            }
            if(oldBaseConfig == null){
                return mysession;
            }
            // 配置没有变化
            else if (oldBaseConfig.getConfigValue().equals(config.getConfigValue())){
                return mysession;
            }
        }

        if (config != null) {
            Map<String,String> configparam = JSONUtils.json2map(config.getConfigValue(),String.class);
            if (configparam != null && !configparam.isEmpty()) {
                String smtpHost = configparam.get("smtpHost");
                String smtpPort = configparam.get("smtpPort");
                String senderUsername = configparam.get("senderUsername");
                String senderPassword = configparam.get("senderPassword");
                boolean smtpAuthValidateEnable = BooleanUtils.toBoolean(configparam.get("smtpAuthValidateEnable"));
                boolean sslEnable = BooleanUtils.toBoolean(configparam.get("sslEnable"));
                Session session = EmailUtils.createSession(smtpHost,senderUsername,senderPassword,smtpAuthValidateEnable,sslEnable,smtpPort);
                mysession = new Mysession(session,senderUsername);
            }
        }

        return mysession;
    }
    class Mysession {
        Session session;
        String senderUsername;

        public Mysession(Session session, String senderUsername) {
            this.session = session;
            this.senderUsername = senderUsername;
        }

        public Session getSession() {
            return session;
        }

        public void setSession(Session session) {
            this.session = session;
        }

        public String getSenderUsername() {
            return senderUsername;
        }

        public void setSenderUsername(String senderUsername) {
            this.senderUsername = senderUsername;
        }
    }
}
