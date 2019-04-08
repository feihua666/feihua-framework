package com.feihua.wechat.publicplatform;

import com.feihua.framework.constants.DictEnum;
import com.feihua.wechat.common.api.ApiWeixinUserPoService;
import com.feihua.wechat.common.po.WeixinUserPo;
import com.feihua.wechat.publicplatform.api.MsgTypeHandler;
import com.feihua.wechat.publicplatform.dto.RequestSubscribeMessage;
import feihua.jdbc.api.pojo.BasePo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by yangwei
 * Created at 2018/7/20 11:42
 */

/**
 * 取消关注事件处理
 */
@Service("default_wx_public_event_unsubscribe")
public class UnSubscribeMsgHandler implements MsgTypeHandler {

    @Autowired
    private ApiWeixinUserPoService apiWeixinUserPoService;
    /**
     * 取消关注，更新一下微信用户状态为未关注
     * @param postXmlData
     * @param which
     * @return
     */
    public String handleMsg( String postXmlData, String which) {
        RequestSubscribeMessage requestSubscribeMessage = PublicUtils.xmlToMessage(postXmlData,new RequestSubscribeMessage());

        WeixinUserPo weixinUserPoCondition = new WeixinUserPo();
        weixinUserPoCondition.setOpenid(requestSubscribeMessage.getFromUserName());
        weixinUserPoCondition.setType(DictEnum.WxAccountType.weixin_publicplatform.name());
        weixinUserPoCondition.setWhich(which);
        weixinUserPoCondition.setDelFlag(BasePo.YesNo.N.name());

        WeixinUserPo weixinUserPoDb = apiWeixinUserPoService.selectOneSimple(weixinUserPoCondition);
        if(weixinUserPoDb != null){

            WeixinUserPo weixinUserPoUpdateCondition = new WeixinUserPo();
            weixinUserPoUpdateCondition.setStatus(DictEnum.WeixinUserStatus.unsubscribe.name());
            weixinUserPoUpdateCondition.setId(weixinUserPoDb.getId());
            weixinUserPoCondition = apiWeixinUserPoService.preUpdate(weixinUserPoCondition, BasePo.DEFAULT_USER_ID);
            apiWeixinUserPoService.updateByPrimaryKeySelective(weixinUserPoUpdateCondition);
        }


        return "";
    }
}
