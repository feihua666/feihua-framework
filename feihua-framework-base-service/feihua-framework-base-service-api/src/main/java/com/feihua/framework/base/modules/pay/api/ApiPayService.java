package com.feihua.framework.base.modules.pay.api;

import java.util.Map;

public interface ApiPayService {
    /**
     * 统一下单支付
     *
     * @param payParam 支付参数
     * @param which    来源 wxpay/alpay ...
     * @return
     */
    Map<String, String> unifiedOrder(Map<String, String> payParam, String which);
}
