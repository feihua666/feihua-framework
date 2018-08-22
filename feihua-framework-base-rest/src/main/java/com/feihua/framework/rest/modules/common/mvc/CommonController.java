package com.feihua.framework.rest.modules.common.mvc;

import com.feihua.framework.rest.ResponseJsonRender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 通用视图控制器
 * Created by yangwei
 * Created at 2017/8/2 14:52
 */
@RestController
public class CommonController extends BaseController{

    private static Logger logger = LoggerFactory.getLogger(CommonController.class);
    /**
     * 404 web.xml配置
     * @return
     */
    @RequestMapping(value = "/resources/404")
    public ResponseEntity pageNotFound(){

        ResponseJsonRender resultData=new ResponseJsonRender();
        resultData.setCode("E404");
        resultData.setMsg("resources not found");

        return new ResponseEntity(resultData, HttpStatus.NOT_FOUND);
    }
}
