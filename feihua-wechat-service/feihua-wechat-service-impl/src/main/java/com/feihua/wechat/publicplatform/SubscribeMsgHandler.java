package com.feihua.wechat.publicplatform;

import com.feihua.framework.constants.DictEnum;
import com.feihua.utils.xml.XmlUtils;
import com.feihua.wechat.common.api.ApiWeixinUserListener;
import com.feihua.wechat.common.api.ApiWeixinUserPoService;
import com.feihua.wechat.common.dto.WeixinUserDto;
import com.feihua.wechat.common.po.WeixinUserPo;
import com.feihua.wechat.publicplatform.api.MsgTypeHandler;
import com.feihua.wechat.publicplatform.dto.MsgType;
import com.feihua.wechat.publicplatform.dto.RequestSubscribeMessage;
import com.feihua.wechat.publicplatform.dto.RequestTextMessage;
import com.feihua.wechat.publicplatform.dto.ResponseTextMessage;
import feihua.jdbc.api.pojo.BasePo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by yangwei
 * Created at 2018/7/20 11:42
 */

/**
 * 关注事件处理
 */
@Service("event_subscribe")
public class SubscribeMsgHandler implements MsgTypeHandler {

    @Autowired
    private ScanQuSceneMsgHandler scanQuSceneMsgHandler;
    @Autowired
    private ApiWeixinUserPoService apiWeixinUserPoService;

    @Autowired
    ApiWeixinUserListener apiWeixinUserListener;

    public String handleMsg( String postXmlData, String which) {

        //关注事件，有两个情况，可能是直接关注，也有可能是扫描带参数二维码关注，这里处理一下带参数二维码情况
        String eventKey = XmlUtils.getElementText("EventKey",XmlUtils.stringToDocument(postXmlData));

        //处理关注事宜
        RequestSubscribeMessage requestSubscribeMessage = PublicUtils.xmlToMessage(postXmlData,new RequestSubscribeMessage());
        // 根据openid查询是否存在数据
        WeixinUserPo weixinUserPoCondition = new WeixinUserPo();
        weixinUserPoCondition.setOpenid(requestSubscribeMessage.getFromUserName());
        weixinUserPoCondition.setType(DictEnum.WeixinType.publicplatform.name());
        weixinUserPoCondition.setWhich(which);
        weixinUserPoCondition.setDelFlag(BasePo.YesNo.N.name());

        WeixinUserPo weixinUserPoDb = apiWeixinUserPoService.selectOneSimple(weixinUserPoCondition);
        // 如果库里没有，插入
        if (weixinUserPoDb == null) {
            WeixinUserPo weixinUserPo = PublicUtils.getWeixinUser(requestSubscribeMessage.getFromUserName(),which);
            weixinUserPo.setStatus(DictEnum.WeixinUserStatus.subscribe.name());
            apiWeixinUserPoService.preInsert(weixinUserPo,BasePo.DEFAULT_USER_ID);
            WeixinUserDto weixinUserDto = apiWeixinUserPoService.insert(weixinUserPo);
            //调用监听
            apiWeixinUserListener.onAddWexinUser(weixinUserDto);
        }else{
            WeixinUserPo weixinUserPo = new WeixinUserPo();
            weixinUserPo.setId(weixinUserPoDb.getId());
            weixinUserPo.setStatus(DictEnum.WeixinUserStatus.subscribe.name());
            apiWeixinUserPoService.preUpdate(weixinUserPo, BasePo.DEFAULT_USER_ID);
            apiWeixinUserPoService.updateByPrimaryKeySelective(weixinUserPo);
        }

        if(StringUtils.isNotEmpty(eventKey)){
            String scanR  =  scanQuSceneMsgHandler.handleMsg(postXmlData,which);
            if(StringUtils.isNotEmpty(scanR)){
                return scanR;
            }
        }
        String r = "欢迎关注！";
        ResponseTextMessage responseTextMessage = new ResponseTextMessage();
        responseTextMessage.setContent(r);
        PublicUtils.userChange(requestSubscribeMessage,responseTextMessage);
        r = PublicUtils.messageToXml(responseTextMessage,true);
        return r;
    }
}
