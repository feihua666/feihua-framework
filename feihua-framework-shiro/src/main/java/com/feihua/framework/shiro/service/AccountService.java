package com.feihua.framework.shiro.service;


import com.feihua.framework.shiro.pojo.AuthenticationInfo;
import com.feihua.framework.shiro.pojo.ShiroUser;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.util.Set;

/**
 * Created by yangwei on 2015/8/19.
 */
public interface AccountService {

    /**
     * 根据 authcToken 查找认证信息
     * @param authcToken
     * @return
     */
    public AuthenticationInfo findUserByToken(AuthenticationToken authcToken);

    /**
     * 根据token获取用户标识，一般为用户id或帐号
     * @param authcToken
     * @return
     */
    public Object findPrincipalByToken(AuthenticationToken authcToken);

    /**
     * 提取用户登录类型
     * @param request
     * @return
     */
    public String resolveLoginType(ServletRequest request);

    /**
     * 提取用户登录的客户端
     * @param request
     * @return
     */
    public String resolveLoginClient(ServletRequest request);

    /**
     * 登录时，是否验证密码
     * @param authcToken
     * @return
     */
    public boolean validatePasswordWhenLogin(AuthenticationToken authcToken);

    /**
     * 是否在登录时，开启验证验证码，
     * 如果用户尝试登录次超限，且该开关开启，则会验证验证码
     * @param request
     * @param response
     * @return
     */
    public boolean validateCaptchaWhenLogin(ServletRequest request, ServletResponse response);

    /**
     * 根据用户id详细信息
     * @param userId
     * @return
     */
    public ShiroUser findUserInfo(String userId);

    /**
     * 创建第三方登录的token
     * @param request
     * @param loginClient
     * @return
     */
    public AuthenticationToken createToken(ServletRequest request,ShiroUser.LoginType loginType,String loginClient);
    /**
     * 根据用户id查询shiro psermissions
     * @param userId
     * @return
     */
    public Set<String> findStringPermissions(String userId);

    /**
     * 根据用户id查询 roles
     * @param userId
     * @return
     */
    public Set<String> findStringRoles(String userId);

    /**
     * 登录成功后调用
     * @param token
     * @param subject
     * @param request
     * @param response
     */
    public void onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request, ServletResponse response);
}
