package com.feihua.framework.base.modules.pay.api;

import feihua.jdbc.api.pojo.BaseConditionDto;

import java.util.Map;

/**
 * 支付结果异步通知
 * Created by yangwei
 * Created at 2019/3/21 10:11
 */
public interface ApiPayNotifyListenerService {

    /**
     *
     * @param param 通知的参数
     * @param payType 支付方式
     * @return
     */
    public void onNotify(Map<String,String> param,String payType);
}
