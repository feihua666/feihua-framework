package com.feihua.framework.base.modules.pay.api;

import com.feihua.framework.base.modules.pay.dto.BaseUnifiedOrderParam;

import java.util.Map;

public interface ApiPayService<Param extends BaseUnifiedOrderParam> {
    /**
     * 统一下单支付
     * @param payParam 支付参数
     * @param which 哪一个公众号
     * @return 返回结果，暂时只涉及到微信支付直接返回到页面，以供页面支付接口使用
     */
    Map<String, String> unifiedOrder(Param payParam,String which);
}
