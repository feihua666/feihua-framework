package com.feihua.framework.base.impl;

import com.feihua.framework.base.modules.pay.api.ApiPayService;
import com.feihua.framework.base.modules.pay.wxpay.*;
import com.feihua.framework.base.modules.pay.wxpay.sdk.MyWxPayConfig;
import com.feihua.framework.base.modules.pay.wxpay.sdk.WXPay;
import com.feihua.framework.base.modules.pay.wxpay.sdk.WXPayConstants;
import com.feihua.framework.base.modules.pay.wxpay.sdk.WXPayUtil;
import com.feihua.utils.json.JSONUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * 微信支付实现,微信支付统一下单入口
 */
@Service
public class ApiWxPayServiceImpl implements ApiPayService<WxUnifiedOrderParam> {
    private static Logger logger = LoggerFactory.getLogger(ApiWxPayServiceImpl.class);

    @Override
    public Map<String, String> unifiedOrder(WxUnifiedOrderParam payParam,String which) {

        return wxpayUnifiedOrder(payParam);
    }


    /**
     * 微信统一下单
     *
     * @param payParam
     * @return
     */
    private Map<String, String> wxpayUnifiedOrder(WxUnifiedOrderParam payParam) {
        Map<String, String> repData = null;
        try {
            MyWxPayConfig myWxPayConfig = new MyWxPayConfig();
            WXPay wxpay = new WXPay(new MyWxPayConfig());
            Map<String, String> data = new HashMap<String, String>();
            /**
             * 商品描述
             * 商品简单描述
             */
            data.put("body", StringUtils.trimToEmpty(payParam.getBody()));
            /**
             * 商品详情
             * 商品详细描述，对于使用单品优惠的商户，改字段必须按照规范上传
             */
            data.put("detail", StringUtils.trimToEmpty(payParam.getDetail()));
            /**
             * 附加数据
             * 附加数据，在查询API和支付通知中原样返回，可作为自定义参数使用。
             */
            data.put("attach", StringUtils.trimToEmpty(payParam.getAttach()));
            /**
             * 商户订单号
             * 商户系统内部订单号，要求32个字符内，只能是数字、大小写字母_-|* 且在同一个商户号下唯一。
             */
            data.put("out_trade_no", StringUtils.trimToEmpty(payParam.getOutTradeNo()));
            //设备号 否
            data.put("device_info", StringUtils.trimToEmpty(payParam.getDeviceInfo()));
            //标价币种 人民币 CNY(境内)
            data.put("fee_type", StringUtils.isEmpty(payParam.getFeeType()) ? "CNY" : payParam.getFeeType());
            /**
             * 用户标识
             * trade_type=JSAPI时（即JSAPI支付），此参数必传，此参数为微信用户在商户对应appid下的唯一标识。openid如何获取，可参考【获取openid】。企业号请使用【企业号OAuth2.0接口】获取企业号内成员userid，再调用【企业号userid转openid接口】进行转换
             */
            data.put("openid", StringUtils.trimToEmpty(payParam.getOpenid()));
            //标价金额
            BigDecimal totalPrice = new BigDecimal(payParam.getTotalFee());
            String total_fee = totalPrice.multiply(new BigDecimal(100)).toBigInteger().toString();
            data.put("total_fee", total_fee);
            //终端IP
            data.put("spbill_create_ip",StringUtils.trimToEmpty(payParam.getSpbillCreateIp()));
            /**
             * 通知地址
             * 异步接收微信支付结果通知的回调地址，通知url必须为外网可访问的url，不能携带参数。
             */
            data.put("notify_url", StringUtils.trimToEmpty(payParam.getNotifyUrl()));
            /**
             * 交易类型
             * JSAPI -JSAPI支付
             * NATIVE -Native支付
             * APP -APP支付
             */
            data.put("trade_type", StringUtils.trimToEmpty(payParam.getTradeType()));
            /**
             * 交易起始时间
             * 订单生成时间，格式为yyyyMMddHHmmss，如2009年12月25日9点10分10秒表示为20091225091010。
             */
            data.put("time_start", StringUtils.trimToEmpty(payParam.getTimeStart()));
            /**
             * 交易结束时间
             * 订单失效时间，格式为yyyyMMddHHmmss，如2009年12月27日9点10分10秒表示为20091227091010。订单失效时间是针对订单号而言的，由于在请求支付的时候有一个必传参数prepay_id只有两小时的有效期，所以在重入时间超过2小时的时候需要重新请求下单接口获取新的prepay_id。
             * 建议：最短失效时间间隔大于1分钟
             */
            data.put("time_expire", StringUtils.trimToEmpty(payParam.getTimeExpire()));
            /**
             * 订单优惠标记
             * 订单优惠标记，使用代金券或立减优惠功能时需要的参数
             */
            data.put("goods_tag", StringUtils.trimToEmpty(payParam.getGoodsTag()));
            /**
             * 商品ID
             * trade_type=NATIVE时，此参数必传。此参数为二维码中包含的商品ID，商户自行定义。
             */
            data.put("product_id", StringUtils.trimToEmpty(payParam.getProductId()));
            /**
             * 指定支付方式
             * 上传此参数no_credit--可限制用户不能使用信用卡支付
             */
            data.put("limit_pay", StringUtils.trimToEmpty(payParam.getLimitPay()));
            /**
             * 电子发票入口开放标识
             * Y，传入Y时，支付成功消息和支付详情页将出现开票入口。需要在微信支付商户平台或微信公众平台开通电子发票功能，传此字段才可生效
             */
            data.put("receipt", StringUtils.trimToEmpty(payParam.getReceipt()));
            /**
             * 场景信息
             */
            data.put("sceneInfo", StringUtils.trimToEmpty(payParam.getSceneInfo()));

            /** wxPay.unifiedOrder 这个方法中调用微信统一下单接口 */
            Map<String, String> respData = wxpay.unifiedOrder(data);
            if (respData.get("return_code").equals("SUCCESS")) {
                //返回给APP端的参数，APP端再调起支付接口

                repData = new HashMap<>();
                repData.put("appId", myWxPayConfig.getAppID());
                repData.put("timeStamp", System.currentTimeMillis() + "");
                repData.put("nonceStr", respData.get("nonce_str"));
                repData.put("package", "prepay_id="+ respData.get("prepay_id"));
                repData.put("signType", WXPayConstants.HMACSHA256);

                // 将以上设置的请求支付参数和商户秘钥共6个参数排序组合后生成密文，即sign签名
                String sign = WXPayUtil.generateSignature(repData, myWxPayConfig.getKey(), WXPayConstants.SignType.HMACSHA256); //签名
                repData.put("paySign", sign);
                logger.debug("预支付返回信息：{}", JSONUtils.obj2json(repData));
            }else{
                logger.debug("预支付返回信息失败：{}", JSONUtils.obj2json(repData));
            }
            return repData;
        } catch (Exception e) {
            try {
                logger.debug("预支付返回信息异常：{}", JSONUtils.obj2json(repData));
            }catch (Exception e1){}
            logger.error(e.getMessage(),e);
        }
        return repData;
    }
}
