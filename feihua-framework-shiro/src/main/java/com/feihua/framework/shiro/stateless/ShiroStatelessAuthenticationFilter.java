package com.feihua.framework.shiro.stateless;

import org.apache.shiro.web.filter.AccessControlFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * Created by yangwei
 * Created at 2017/7/27 13:09
 */
public class ShiroStatelessAuthenticationFilter extends AccessControlFilter {
    @Override
    protected boolean isAccessAllowed(ServletRequest servletRequest, ServletResponse servletResponse, Object o) throws Exception {
        return false;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        return false;
    }
}
