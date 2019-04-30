package com.feihua.wechat.publicplatform;

import com.feihua.wechat.publicplatform.api.ApiPublicTemplateMessageService;
import com.feihua.wechat.publicplatform.dto.WxPublicTemplateParam;
import org.springframework.stereotype.Service;

/**
 * Created by yangwei
 * Created at 2019/4/28 17:18
 */
@Service
public class ApiPublicTemplateMessageImpl implements ApiPublicTemplateMessageService {
    @Override
    public void send(WxPublicTemplateParam wxPublicTemplate, String which) {
        PublicUtils.sendWxPublicTemplateMsg(wxPublicTemplate,which);
    }
}
