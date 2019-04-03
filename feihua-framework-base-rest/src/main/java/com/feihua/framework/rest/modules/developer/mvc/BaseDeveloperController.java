package com.feihua.framework.rest.modules.developer.mvc;


import com.feihua.framework.base.modules.dict.api.ApiBaseDictPoService;
import com.feihua.framework.base.modules.dict.dto.BaseDictDto;
import com.feihua.framework.base.modules.dict.dto.SearchDictsConditionDto;
import com.feihua.framework.base.modules.dict.po.BaseDictPo;
import com.feihua.framework.log.comm.annotation.OperationLog;
import com.feihua.utils.http.httpServletResponse.ResponseCode;
import com.feihua.framework.rest.ResponseJsonRender;
import com.feihua.framework.rest.interceptor.RepeatFormValidator;
import com.feihua.framework.rest.modules.common.mvc.BaseController;
import com.feihua.framework.rest.modules.developer.MyHttpServletRequest;
import com.feihua.framework.rest.modules.dict.dto.AddDictFormDto;
import com.feihua.framework.rest.modules.dict.dto.UpdateDictFormDto;
import com.feihua.utils.string.RegularExpression;
import feihua.jdbc.api.pojo.BasePo;
import feihua.jdbc.api.pojo.PageAndOrderbyParamDto;
import feihua.jdbc.api.pojo.PageResultDto;
import feihua.jdbc.api.utils.OrderbyUtils;
import feihua.jdbc.api.utils.PageUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 开发者工具接口
 * Created by yangwei
 * Created at 2017/12/21 13:13
 */
@RestController
@RequestMapping("/base/developer")
public class BaseDeveloperController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(BaseDeveloperController.class);
    @Autowired
    private RequestMappingHandlerMapping handlerMapping;
    @Autowired
    private HttpServletRequest httpServletRequest;
    /**
     * 搜索url对应的controller方法及注解
     * @param url
     * @return
     */
    @OperationLog(operation = "开发者工具接口", content = "搜索url对应的controller方法及注解")
    @RequiresPermissions("base:developer:urlmapping")
    @RequestMapping(value = "/urlmapping",method = RequestMethod.GET)
    public ResponseEntity urlmapping(String url,String method){

        ResponseJsonRender resultData=new ResponseJsonRender();
        List<String> list = new ArrayList<>();

        MyHttpServletRequest myHttpServletRequest = new MyHttpServletRequest(httpServletRequest);
        myHttpServletRequest.setMyMethod(method);
        myHttpServletRequest.setMyRequestURL(url);
        myHttpServletRequest.setMyRequestURI(url);
        HandlerMethod handlerMethod = null;
        try {
            Object obj = handlerMapping.getHandler(myHttpServletRequest).getHandler();
            if(obj instanceof HandlerMethod){
                handlerMethod = (HandlerMethod) obj;
            }

        }catch (Exception e) {
            logger.error(e.getMessage(),e);
        }
        if(handlerMethod != null){
            list.add(handlerMethod.getMethod().toString());
            RequiresPermissions requiresPermissions = handlerMethod.getMethodAnnotation(RequiresPermissions.class);
            if(requiresPermissions != null){
                list.add("@requiresPermissions" + requiresPermissions.toString());
            }
            RequestMapping requestMapping = handlerMethod.getMethodAnnotation(RequestMapping.class);
            if (requestMapping != null) {
                list.add("@requestMapping" + requestMapping.toString());
            }

        }
        if(CollectionUtils.isNotEmpty(list)){
            resultData.setData(list);
            return new ResponseEntity(resultData, HttpStatus.OK);
        }else{
            resultData.setCode(ResponseCode.E404_100001.getCode());
            resultData.setMsg(ResponseCode.E404_100001.getMsg());
            return new ResponseEntity(resultData,HttpStatus.NOT_FOUND);
        }
    }

    /**
     * 正则表达式列表
     * @return
     */
    @RequestMapping(value = "/regexps",method = RequestMethod.GET)
    public ResponseEntity regularExps(){

        ResponseJsonRender resultData=new ResponseJsonRender();
        Map<String,Object> result = new HashMap<>();
        for (RegularExpression regularExpression : RegularExpression.values()) {
            result.put(regularExpression.name(),regularExpression.getReg());
        }
        resultData.setData(result);

        return new ResponseEntity(resultData, HttpStatus.OK);
    }
}
