package com.feihua.framework.shiro;

import com.feihua.framework.constants.DictEnum;
import com.feihua.framework.shiro.pojo.AuthenticationInfo;
import com.feihua.framework.shiro.pojo.PasswordAndSalt;
import com.feihua.framework.shiro.pojo.ShiroUser;
import com.feihua.framework.shiro.service.AbstractAccountServiceImpl;
import org.apache.shiro.authc.AuthenticationToken;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.util.Set;

/**
 * Created by yangwei
 * Created at 2017/7/26 16:58
 */
public class AccountServiceImpl extends AbstractAccountServiceImpl {
    @Override
    public AuthenticationInfo findUserByToken(AuthenticationToken authcToken) {

        AuthenticationInfo info = new AuthenticationInfo();
        info.setUserId("1");
        info.setLocked(false);
        info.setPasswordAndSalt(PasswordAndSalt.entryptPassword("123456"));
        return info;
    }

    @Override
    public Object findPrincipalByToken(AuthenticationToken authcToken) {
        return "1";
    }

    @Override
    public String resolveLoginType(ServletRequest request) {
        return request.getParameter(ShiroFormAuthenticationFilter.param_loginClient_code_key);
    }

    @Override
    public String getClientIdByClientCode(String clientCode) {
        return null;
    }

    @Override
    public String getClientNameByClientCode(String clientCode) {
        return null;
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
        user.setAccount("testaccount");
        user.setId(userId);
        return user;
    }

    @Override
    public AuthenticationToken createToken(ServletRequest request, DictEnum.LoginType loginType, String loginClient) {
        return null;
    }

    @Override
    public Set<String> findStringPermissions(String userId) {
        return null;
    }

    @Override
    public Set<String> findStringRoles(String userId) {
        return null;
    }
}
