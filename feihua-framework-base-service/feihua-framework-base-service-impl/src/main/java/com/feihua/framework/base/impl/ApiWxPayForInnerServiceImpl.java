package com.feihua.framework.base.impl;

import com.feihua.framework.base.modules.pay.api.ApiPayService;
import com.feihua.framework.base.modules.pay.wxpay.WxUnifiedOrderForInnerParam;
import com.feihua.framework.base.modules.pay.wxpay.WxUnifiedOrderParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 微信支付实现，微信公众号内支付
 */
@Service
public class ApiWxPayForInnerServiceImpl implements ApiPayService<WxUnifiedOrderForInnerParam> {
    private static Logger logger = LoggerFactory.getLogger(ApiWxPayForInnerServiceImpl.class);

    @Autowired
    private ApiPayService<WxUnifiedOrderParam> apiWxPayService;
    @Override
    public Map<String, String> unifiedOrder(WxUnifiedOrderForInnerParam payParam, String which) {

        WxUnifiedOrderParam wxUnifiedOrderParam = new WxUnifiedOrderParam();
        wxUnifiedOrderParam.setDeviceInfo(payParam.getDeviceInfo());
        wxUnifiedOrderParam.setBody(payParam.getBody());
        wxUnifiedOrderParam.setDetail(payParam.getDetail());
        wxUnifiedOrderParam.setAttach(payParam.getAttach());
        wxUnifiedOrderParam.setOutTradeNo(payParam.getOutTradeNo());
        wxUnifiedOrderParam.setFeeType(payParam.getFeeType());
        wxUnifiedOrderParam.setTotalFee(payParam.getTotalFee());
        wxUnifiedOrderParam.setSpbillCreateIp(payParam.getSpbillCreateIp());
        wxUnifiedOrderParam.setTimeStart(payParam.getTimeStart());
        wxUnifiedOrderParam.setTimeExpire(payParam.getTimeExpire());
        wxUnifiedOrderParam.setGoodsTag(payParam.getGoodsTag());
        wxUnifiedOrderParam.setNotifyUrl(payParam.getNotifyUrl());
        wxUnifiedOrderParam.setTradeType("JSAPI");
        wxUnifiedOrderParam.setProductId(payParam.getProductId());
        wxUnifiedOrderParam.setOpenid(payParam.getOpenid());
        wxUnifiedOrderParam.setSceneInfo(payParam.getSceneInfo());

        return apiWxPayService.unifiedOrder(wxUnifiedOrderParam,which);
    }

}
