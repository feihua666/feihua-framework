package com.feihua.framework.shiro.service;

import com.feihua.framework.shiro.ShiroFormAuthenticationFilter;
import com.feihua.framework.shiro.pojo.ShiroUser;
import com.feihua.utils.properties.PropertiesUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.util.Map;

/**
 * 帐号默认实现基类
 * Created by yangwei
 * Created at 2017/7/26 16:58
 */
public abstract class AbstractAccountServiceImpl implements AccountService {

    @Override
    public String resolveLoginType(ServletRequest request) {
        return request.getParameter(ShiroFormAuthenticationFilter.param_loginType_key);
    }
    @Override
    public String resolveLoginClient(ServletRequest request) {
        return ( request.getParameter(ShiroFormAuthenticationFilter.param_loginClient_code_key));
    }

    @Override
    public int resolveLoginClientMaxnum(String loginClient) {

        String maxnum = PropertiesUtils.getProperty("shiro.session." + loginClient + ".maxnum");
        if(StringUtils.isEmpty(maxnum)){
            return PropertiesUtils.getInteger("shiro.session.maxnum");
        }else{
            return Integer.parseInt(maxnum);
        }
    }

    @Override
    public boolean validatePasswordWhenLogin(AuthenticationToken authcToken) {
        return true;
    }
    @Override
    public boolean validateCaptchaWhenLogin(ServletRequest request, ServletResponse response) {
        return false;
    }

    @Override
    public void onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request, ServletResponse response) {

    }

    @Override
    public Map<String, Object> getAddtionalAttr(ShiroUser shiroUser) {
        return null;
    }
}
