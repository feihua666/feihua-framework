package com.feihua.framework.rest.interceptor;

import com.feihua.exception.BaseException;
import com.feihua.utils.digest.DigestUtils;
import com.feihua.utils.http.httpServletResponse.ResponseCode;
import com.feihua.utils.json.JSONUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

/**
 * 表单重复提交拦截
 * Created by yw on 2016/1/25.
 */
public class RepeatFormInterceptor extends HandlerInterceptorAdapter {

    Logger logger = LoggerFactory.getLogger(RepeatFormInterceptor.class);
    /**
     * This implementation always returns {@code true}.
     *
     * @param request
     * @param response
     * @param handler
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            HttpMethod httpMethod = HttpMethod.resolve(request.getMethod());
            RepeatFormValidator annotation = method.getAnnotation(RepeatFormValidator.class);
            if (annotation != null && RepeatFormValidatorLogic.VALIDATE == annotation.value() && repeatDataValidator(request)) {
                throw new BaseException("repeated request in a short time", ResponseCode.E403_100002.getCode(),HttpServletResponse.SC_FORBIDDEN);
            }else if (annotation == null && ( httpMethod == HttpMethod.POST  || httpMethod == HttpMethod.PUT || httpMethod == HttpMethod.DELETE ) && repeatDataValidator(request) ) {
                throw new BaseException("repeated request in a short time",ResponseCode.E403_100002.getCode(),HttpServletResponse.SC_FORBIDDEN);
            }
        }
        return super.preHandle(request, response, handler);
    }
    /**
     * 验证同一个url数据是否相同提交  ,相同返回true
     * @param httpServletRequest
     * @return
     */
    public boolean repeatDataValidator(HttpServletRequest httpServletRequest) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        Session session = SecurityUtils.getSubject().getSession();
        String params = null;
        try {
            params = JSONUtils.obj2json(httpServletRequest.getParameterMap());
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
        }
        String url = httpServletRequest.getRequestURI();
        Map<String,String> map = new HashMap<String,String>();
        map.put(url, params);
        String nowUrlParams = map.toString();
        nowUrlParams = DigestUtils.md5(nowUrlParams);
        Object preUrlParams = session.getAttribute("repeatData");
        if(preUrlParams==null)//如果上一个数据为null,表示还没有访问页面
        {
            session.setAttribute("repeatData", nowUrlParams);
            session.setAttribute("repeatDataTime",System.currentTimeMillis());
            return false;
        }
        else//否则，已经访问过页面
        {
            if(preUrlParams.toString().equals(nowUrlParams))//如果上次url+数据和本次url+数据相同，则表示城府添加数据
            {
                long repeatDataTime = (Long) session.getAttribute("repeatDataTime");
                // 800毫秒以内的同一个请求，不让请求
                if(System.currentTimeMillis() - repeatDataTime > 800){
                    session.setAttribute("repeatDataTime",System.currentTimeMillis());
                    return false;
                }else{
                    return true;
                }

            }
            else//如果上次 url+数据 和本次url加数据不同，则不是重复提交
            {
                session.setAttribute("repeatData", nowUrlParams);
                session.setAttribute("repeatDataTime",System.currentTimeMillis());
                return false;
            }

        }
    }
}
