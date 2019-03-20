package com.feihua.framework.rest.modules.pay.mvc;

import com.feihua.framework.base.modules.pay.api.ApiPayService;
import com.feihua.framework.base.modules.pay.wxpay.WxUnifiedOrderParam;
import com.feihua.framework.rest.ResponseJsonRender;
import com.feihua.framework.rest.interceptor.RepeatFormValidator;
import com.feihua.framework.rest.mvc.SuperController;
import com.feihua.utils.http.httpServletRequest.RequestUtils;
import com.feihua.utils.http.httpServletResponse.ResponseCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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


    /**
     * 提供统一支付成功回调，暂未实现
     * @return
     */
    @RequestMapping(value = "/unifiedOrder/success")
    public ResponseEntity unifiedOrder(){
        ResponseJsonRender resultData = new ResponseJsonRender();
        System.out.println("************************unifiedOrder/success");
        return new ResponseEntity(resultData, HttpStatus.OK);
    }
}
