package com.feihua.framework.rest.interceptor;


import feihua.jdbc.api.utils.OrderbyUtils;
import feihua.jdbc.api.utils.PageUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * page分页
 * Created by yw on 2016/1/25.
 */
public class PageHandlerInterceptor extends HandlerInterceptorAdapter {
    /**
     * This implementation always returns {@code true}.
     *
     * @param request
     * @param response
     * @param handler
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //分页
        PageUtils.putPageToThreadLocal(PageUtils.getPageFromRequest(request));

        //排序
        OrderbyUtils.putOrderbyToThreadLocal(OrderbyUtils.getOrderbyFromRequest(request));

        return super.preHandle(request, response, handler);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        super.afterCompletion(request, response, handler, ex);
    }
}
