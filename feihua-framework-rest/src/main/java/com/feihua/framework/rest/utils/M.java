package com.feihua.framework.rest.utils;

import com.feihua.utils.http.httpServletRequest.RequestUtils;
import org.springframework.web.servlet.support.RequestContext;

/**
 * 国际化消息工具类
 * Created by yangwei
 * Created at 2017/9/27 15:34
 */
public class M {

    public static RequestContext getRequestContext(){
        RequestContext requestContext = new RequestContext(RequestUtils.getRequest());
        return requestContext;
    }

    public static String getMessage(String code){
        return getRequestContext().getMessage(code);
    }
    public static String getMessage(String code,Object... args){
        return getRequestContext().getMessage(code,args);
    }
}
