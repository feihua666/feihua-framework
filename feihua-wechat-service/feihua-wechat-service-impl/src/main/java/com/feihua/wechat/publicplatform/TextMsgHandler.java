package com.feihua.wechat.publicplatform;

import com.feihua.wechat.publicplatform.api.MsgTypeHandler;
import com.feihua.wechat.publicplatform.dto.RequestTextMessage;
import org.springframework.stereotype.Service;

/**
 * 文本消息处理
 * Created by yangwei
 * Created at 2018/7/20 11:42
 */
@Service
public class TextMsgHandler implements MsgTypeHandler {

    public String handleMsg( String postXmlData, String which) {
        RequestTextMessage requestTextMessage = (RequestTextMessage) PublicUtils.xmlToMessage(postXmlData,new RequestTextMessage());


        return "";
    }
}
