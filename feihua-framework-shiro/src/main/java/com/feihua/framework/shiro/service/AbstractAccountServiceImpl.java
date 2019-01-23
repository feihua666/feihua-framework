package com.feihua.framework.shiro.service;

import com.feihua.framework.shiro.LoginClient;
import com.feihua.framework.shiro.ShiroFormAuthenticationFilter;
import com.feihua.framework.shiro.pojo.AuthenticationInfo;
import com.feihua.framework.shiro.pojo.PasswordAndSalt;
import com.feihua.framework.shiro.pojo.ShiroUser;
import com.feihua.framework.shiro.pojo.token.AccountPasswordToken;
import com.feihua.framework.shiro.pojo.token.EmailPasswordToken;
import com.feihua.framework.shiro.pojo.token.MobilePasswordToken;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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
    public LoginClient resolveLoginClient(ServletRequest request) {
        LoginClient loginClient = new LoginClient();
        loginClient.setClientType( request.getParameter(ShiroFormAuthenticationFilter.param_loginClient_key));
        loginClient.setSubClientType( request.getParameter(ShiroFormAuthenticationFilter.param_subloginClient_key));
        return loginClient;
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
