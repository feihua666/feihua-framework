package com.feihua.framework.shiro.service;

import com.feihua.framework.shiro.ShiroFormAuthenticationFilter;
import com.feihua.framework.shiro.pojo.AuthenticationInfo;
import com.feihua.framework.shiro.pojo.PasswordAndSalt;
import com.feihua.framework.shiro.pojo.ShiroUser;
import com.feihua.framework.shiro.pojo.token.AccountPasswordToken;
import com.feihua.framework.shiro.pojo.token.EmailPasswordToken;
import com.feihua.framework.shiro.pojo.token.MobilePasswordToken;
import org.apache.shiro.authc.AuthenticationToken;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.util.HashSet;
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
    public String resolveLoginClient(ServletRequest request) {
        return request.getParameter(ShiroFormAuthenticationFilter.param_loginClient_key);
    }
    @Override
    public boolean validatePasswordWhenLogin(AuthenticationToken authcToken) {
        return true;
    }
    @Override
    public boolean validateCaptchaWhenLogin(ServletRequest request, ServletResponse response) {
        return false;
    }
}
