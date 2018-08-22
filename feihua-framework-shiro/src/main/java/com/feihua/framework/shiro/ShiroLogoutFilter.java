package com.feihua.framework.shiro;

import com.feihua.utils.http.httpServletRequest.RequestUtils;
import com.feihua.utils.http.httpServletResponse.ResponseCode;
import com.feihua.utils.http.httpServletResponse.ResponseUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yangwei
 * Created at 2018/3/23 16:14
 */
public class ShiroLogoutFilter extends org.apache.shiro.web.filter.authc.LogoutFilter {
    @Override
    protected void issueRedirect(ServletRequest request, ServletResponse response, String redirectUrl) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        if (RequestUtils.isAjaxRequest(httpServletRequest)){
            try {
                Map<String,Object> msgMap = new HashMap<>();
                msgMap.put("msg", ResponseCode.success.getMsg());
                msgMap.put("code",ResponseCode.success.getCode());
                ResponseUtils.renderString(httpServletResponse,msgMap);
            } catch (Exception e1) {
            }
        }else{
            super.issueRedirect(request, response, redirectUrl);
        }
    }
}
