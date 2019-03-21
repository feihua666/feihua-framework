package com.feihua.framework.rest.modules.pay.mvc;

import com.feihua.framework.base.modules.pay.api.ApiPayNotifyListenerService;
import com.feihua.framework.base.modules.pay.api.ApiPayService;
import com.feihua.framework.base.modules.pay.wxpay.WxUnifiedOrderParam;
import com.feihua.framework.base.modules.pay.wxpay.sdk.MyWxPayConfig;
import com.feihua.framework.base.modules.pay.wxpay.sdk.WXPayConstants;
import com.feihua.framework.base.modules.pay.wxpay.sdk.WXPayUtil;
import com.feihua.framework.constants.ConfigConstant;
import com.feihua.framework.rest.ResponseJsonRender;
import com.feihua.framework.rest.interceptor.RepeatFormValidator;
import com.feihua.framework.rest.mvc.SuperController;
import com.feihua.utils.http.httpServletRequest.RequestUtils;
import com.feihua.utils.http.httpServletResponse.ResponseCode;
import feihua.jdbc.api.pojo.BasePo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 支付管理
 * Created by revolver
 */
@RestController
@RequestMapping("/pay")
public class PayController extends SuperController {

    private static Logger logger = LoggerFactory.getLogger(PayController.class);

    @Autowired
    private ApiPayService<WxUnifiedOrderParam> apiPayService;

    @Autowired(required = false)
    private Map<String, ApiPayNotifyListenerService> apiPayNotifyListenerServiceMap;

    /**
     * 提供统一支付成功回调，暂未实现
     * @return
     */
    @RequestMapping(value = "/wx/unifiedOrder/success", produces = {"application/xml;charset=UTF-8"})
    public ResponseEntity unifiedOrder(@RequestBody String xml,@PathVariable String payType){
        Map<String,String> requestData = null;
        try {
            requestData =  WXPayUtil.xmlToMap(xml);
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
        }
        if (apiPayNotifyListenerServiceMap != null && requestData != null) {

            // 订单成功修改订单状态
            if("SUCCESS".equals(requestData.get("result_code"))){

                // 验证签名
                String sign = requestData.get("sign");
                MyWxPayConfig myWxPayConfig = null;
                try {
                    myWxPayConfig = new MyWxPayConfig();
                } catch (Exception e) {
                    logger.error(e.getMessage(),e);
                }
                String newsign = null; //签名
                try {
                    newsign = WXPayUtil.generateSignature(requestData, myWxPayConfig.getKey(), WXPayConstants.SignType.HMACSHA256);
                } catch (Exception e) {
                    logger.error(e.getMessage(),e);
                }
                if(newsign != null && newsign.equals(sign)){

                    for (String key : apiPayNotifyListenerServiceMap.keySet()) {
                        apiPayNotifyListenerServiceMap.get(key).onNotify(requestData, ConfigConstant.PayType.WXPAY.name());
                    }
                }else {
                    logger.error("sign error sign={},newsign={}",sign,newsign);
                }

            }

        }
        Map<String,String> resultMap = new HashMap<>();
        resultMap.put("return_code","SUCCESS");
        resultMap.put("return_msg","OK");
        String returnXml = null;
        try {
            returnXml = WXPayUtil.mapToXml(resultMap);
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
        }
        return new ResponseEntity(returnXml, HttpStatus.OK);
    }
}
