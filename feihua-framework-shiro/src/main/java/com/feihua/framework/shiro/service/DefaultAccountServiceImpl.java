package com.feihua.framework.shiro.service;

import com.feihua.framework.shiro.ShiroFormAuthenticationFilter;
import com.feihua.framework.shiro.pojo.AuthenticationInfo;
import com.feihua.framework.shiro.pojo.PasswordAndSalt;
import com.feihua.framework.shiro.pojo.ShiroUser;
import com.feihua.framework.shiro.pojo.token.AccountPasswordToken;
import org.apache.shiro.authc.AuthenticationToken;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.util.HashSet;
import java.util.Set;

/**
 * 帐号报务默认实现
 * Created by yangwei
 * Created at 2017/7/26 16:58
 */
public class DefaultAccountServiceImpl extends AbstractAccountServiceImpl{

    private static String userId = "1";
    private static String userAccount="admin";

    @Override
    public AuthenticationInfo findUserByToken(AuthenticationToken authcToken) {

        AuthenticationInfo info = new AuthenticationInfo();
        info.setUserId(userId);
        info.setLocked(false);
        info.setPasswordAndSalt(PasswordAndSalt.entryptPassword("123456"));
        return info;
    }

    @Override
    public Object findPrincipalByToken(AuthenticationToken authcToken) {
        return userId;
    }

    @Override
    public String resolveLoginType(ServletRequest request) {
        return request.getParameter(ShiroFormAuthenticationFilter.param_loginType_key);
    }

    @Override
    public boolean validatePasswordWhenLogin(AuthenticationToken authcToken) {
        return true;
    }

    @Override
    public boolean validateCaptchaWhenLogin(ServletRequest request, ServletResponse response) {
        return true;
    }

    @Override
    public ShiroUser findUserInfo(String userId) {

        ShiroUser user = new ShiroUser();
        user.setAccount(userAccount);
        user.setId(userId);
        return user;
    }

    @Override
    public AuthenticationToken createToken(ServletRequest request, ShiroUser.LoginType loginType,String loginClient) {
        AccountPasswordToken token = new AccountPasswordToken(userAccount,"123456");
        return token;
    }

    @Override
    public Set<String> findStringPermissions(String userId) {
        Set<String> s = new HashSet<>();
        s.add("*");
        return s;
    }

    @Override
    public Set<String> findStringRoles(String userId) {
        return null;
    }
}
