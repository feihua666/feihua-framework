package com.feihua.framework.base.impl;

import com.feihua.framework.base.modules.pay.api.ApiPayService;
import com.feihua.framework.base.modules.pay.wxpay.MyWxPayConfig;
import com.feihua.framework.base.modules.pay.wxpay.WXPay;
import com.feihua.framework.base.modules.pay.wxpay.WXPayUtil;
import com.feihua.utils.json.JSONUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
public class ApiPayServiceImpl implements ApiPayService {
    private static Logger logger = LoggerFactory.getLogger(ApiPayServiceImpl.class);

    @Override
    public Map<String, String> unifiedOrder(Map<String, String> payParam, String which) {
        Map<String, String> repData = null;
        //TODO 支付实现。。。。。。。。。。。。。。。。
        switch (which) {
            //微信
            case "wxpay":
                repData = wxpayUnifiedOrder(payParam);
                break;
        }
        return repData;
    }

    /**
     * 微信统一下单
     *
     * @param payParam
     * @return
     */
    private Map<String, String> wxpayUnifiedOrder(Map<String, String> payParam) {
        Map<String, String> repData = null;
        try {
            MyWxPayConfig myWxPayConfig = new MyWxPayConfig();
            WXPay wxpay = new WXPay(new MyWxPayConfig());

            Map<String, String> data = new HashMap<String, String>();
            //商品描述
            data.put("body", payParam.get("body"));
            //商户订单号 	是
            data.put("out_trade_no", WXPayUtil.generateNonceStr());
            //设备号 否
            data.put("device_info", "");
            //标价币种 人民币 CNY(境内)
            data.put("fee_type", "CNY");
            //标价金额
            String totalFee = payParam.get("totalFee");
            BigDecimal totalPrice = new BigDecimal(totalFee);
            String total_fee = totalPrice.multiply(new BigDecimal(100)).toBigInteger().toString();
            data.put("total_fee", total_fee);
            //终端IP
            data.put("spbill_create_ip", payParam.get("ip"));
            //// 订单结果通知, 微信主动回调此接口
            data.put("notify_url", payParam.get("notifyUrl"));
            //交易类型 SAPI -JSAPI支付 NATIVE -Native支付 APP -APP支付
            data.put("trade_type", "JSAPI");  // 固定填写 JSAPI

            System.out.println(data);
            /** wxPay.unifiedOrder 这个方法中调用微信统一下单接口 */
            Map<String, String> respData = wxpay.unifiedOrder(data);
            System.out.println(respData);
            if (respData.get("return_code").equals("SUCCESS")) {
                //返回给APP端的参数，APP端再调起支付接口
                repData = new HashMap<>();
                repData.put("appid", myWxPayConfig.getAppID());
                repData.put("mch_id", myWxPayConfig.getMchID());
                repData.put("prepayid", respData.get("prepay_id"));
                repData.put("package", "WXPay");
                repData.put("noncestr", respData.get("nonce_str"));
                repData.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
                repData.put("signType", "MD5");
                // 将以上设置的请求支付参数和商户秘钥共6个参数排序组合后生成密文，即sign签名
                String sign = WXPayUtil.generateSignature(repData, myWxPayConfig.getKey()); //签名
                repData.put("sign", sign);
                repData.put("timestamp", repData.get("timestamp"));
                logger.debug("预支付返回信息：{}", JSONUtils.obj2json(repData));
            }
            return repData;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return repData;
    }
}
