package com.feihua.wechat.publicplatform;

import com.feihua.framework.constants.DictEnum;
import com.feihua.wechat.common.api.ApiWeixinUserPoService;
import com.feihua.wechat.common.po.WeixinUserPo;
import com.feihua.wechat.publicplatform.api.MsgTypeHandler;
import com.feihua.wechat.publicplatform.dto.RequestLocationMessage;
import com.feihua.wechat.publicplatform.dto.RequestSubscribeMessage;
import feihua.jdbc.api.pojo.BasePo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * 上报地理位置处理
 * Created by yangwei
 * Created at 2018/7/20 11:42
 */
@Service
public class LocationMsgHandler implements MsgTypeHandler {
    @Autowired
    private ApiWeixinUserPoService apiWeixinUserPoService;

    public String handleMsg( String postXmlData, String which) {

        RequestLocationMessage requestLocationMessage = PublicUtils.xmlToMessage(postXmlData,new RequestLocationMessage());

        WeixinUserPo weixinUserPoCondition = new WeixinUserPo();
        weixinUserPoCondition.setOpenid(requestLocationMessage.getFromUserName());
        weixinUserPoCondition.setType(DictEnum.WeixinType.publicplatform.name());
        weixinUserPoCondition.setWhich(which);
        weixinUserPoCondition.setDelFlag(BasePo.YesNo.N.name());

        WeixinUserPo weixinUserPoDb = apiWeixinUserPoService.selectOneSimple(weixinUserPoCondition);

        WeixinUserPo weixinUserPoUpdateCondition = new WeixinUserPo();
        weixinUserPoUpdateCondition.setStatus(DictEnum.WeixinUserStatus.unsubscribe.name());
        weixinUserPoUpdateCondition.setId(weixinUserPoDb.getId());

        weixinUserPoUpdateCondition.setLatitude(requestLocationMessage.getLatitude());
        weixinUserPoUpdateCondition.setLongitude(requestLocationMessage.getLongitude());
        weixinUserPoUpdateCondition.setPrecisions(requestLocationMessage.getPrecision());
        weixinUserPoCondition = apiWeixinUserPoService.preUpdate(weixinUserPoCondition, BasePo.DEFAULT_USER_ID);
        apiWeixinUserPoService.updateByPrimaryKeySelective(weixinUserPoUpdateCondition);

        return "";
    }
}
