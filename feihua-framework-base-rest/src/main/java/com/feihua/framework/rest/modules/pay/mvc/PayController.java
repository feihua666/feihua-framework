package com.feihua.framework.rest.modules.pay.mvc;

import com.feihua.framework.base.modules.pay.api.ApiPayService;
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
    private ApiPayService apiPayService;

    /**
     * 统一下单接口
     * @param which    来源 wxpay/alpay ...
     * @param payParam
     * @return
     */
    @RepeatFormValidator
//    @RequiresPermissions("wxpay:unifiedOrder")
    @RequestMapping(value = "/{which}/unifiedOrder", method = RequestMethod.POST)
    public ResponseEntity unifiedOrder(@PathVariable("which") String which, @RequestBody Map<String, String> payParam) throws Exception {
        logger.info("添加微信账号开始");
        logger.info("当前登录用户id:{}", getLoginUser().getId());
        ResponseJsonRender resultData = new ResponseJsonRender();
        payParam.put("ip", RequestUtils.getRemoteAddr(RequestUtils.getRequest()));
        Map<String, String> repData = apiPayService.unifiedOrder(payParam, which);

        if (repData != null) {
            resultData.setData(repData);
            return new ResponseEntity(resultData, HttpStatus.OK);
        } else {
            resultData.setCode(ResponseCode.E404_100001.getCode());
            return new ResponseEntity(resultData, HttpStatus.NOT_FOUND);
        }
    }
}
