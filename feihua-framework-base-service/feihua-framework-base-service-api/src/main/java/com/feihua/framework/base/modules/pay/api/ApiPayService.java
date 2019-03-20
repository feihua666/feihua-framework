package com.feihua.framework.base.modules.pay.api;

import com.feihua.framework.base.modules.pay.dto.BaseUnifiedOrderParam;

import java.util.Map;

public interface ApiPayService<Param extends BaseUnifiedOrderParam> {
    /**
     * 统一下单支付
     *
     * @param payParam 支付参数
     * @return
     */
    Map<String, String> unifiedOrder(Param payParam,String which);
}
