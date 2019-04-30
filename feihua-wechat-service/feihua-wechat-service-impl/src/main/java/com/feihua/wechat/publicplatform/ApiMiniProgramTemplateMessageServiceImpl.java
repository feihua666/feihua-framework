package com.feihua.wechat.publicplatform;

import com.feihua.wechat.miniprogram.MiniProgramUtils;
import com.feihua.wechat.miniprogram.api.ApiMiniProgramTemplateMessageService;
import com.feihua.wechat.miniprogram.dto.MsgTemplateParamDto;

/**
 * Created by yangwei
 * Created at 2019/4/28 18:06
 */
public class ApiMiniProgramTemplateMessageServiceImpl implements ApiMiniProgramTemplateMessageService {
    @Override
    public void send(MsgTemplateParamDto dto, String which) {
        MiniProgramUtils.sendMsgTemplate(dto,which);
    }
}
