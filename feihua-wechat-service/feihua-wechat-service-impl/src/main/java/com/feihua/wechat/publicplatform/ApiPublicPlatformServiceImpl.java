package com.feihua.wechat.publicplatform;

import com.feihua.utils.xml.XmlUtils;
import com.feihua.wechat.publicplatform.api.ApiPublicPlatformService;
import com.feihua.wechat.publicplatform.api.MsgTypeHandler;
import com.feihua.wechat.publicplatform.dto.MsgEvent;
import com.feihua.wechat.publicplatform.dto.MsgType;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by yangwei
 * Created at 2018/7/20 9:55
 */
@Service
public class ApiPublicPlatformServiceImpl implements ApiPublicPlatformService {


    @Autowired(required = false)
    private Map<String,MsgTypeHandler> msgHandlers;

    public String processMsg(String postXmlData, String which) {
        Document document = XmlUtils.stringToDocument(postXmlData);
        MsgType msgType = new MsgType();
        msgType.setName(XmlUtils.getElementText("MsgType",document));
        MsgEvent msgEvent = new MsgEvent();
        msgEvent.setName(XmlUtils.getElementText("Event",document));
        msgType.setMsgEvent(msgEvent);
        MsgTypeHandler msgTypeHandler = null;
        if(msgHandlers != null){
            String stuffix = "";
            if(StringUtils.isNotEmpty(msgEvent.getName())){
                stuffix = "_"+ msgEvent.getName();
            }
            msgTypeHandler = msgHandlers.get(msgType.getName() + stuffix);
        }
        return msgTypeHandler == null?"":msgTypeHandler.handleMsg(postXmlData,which);
    }

}
