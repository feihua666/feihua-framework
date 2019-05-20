package com.feihua.framework.shiro.realms;


import com.feihua.framework.shiro.service.AccountService;
import com.feihua.framework.shiro.pojo.PasswordAndSalt;
import com.feihua.framework.shiro.pojo.AuthenticationInfo;
import com.feihua.framework.shiro.utils.ShiroUtils;
import com.feihua.utils.encode.EncodeUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.List;
import java.util.Set;


public class Realm extends AuthorizingRealm {

    private static Logger logger = LoggerFactory.getLogger(Realm.class);

    @Autowired
    protected AccountService accountService;

    /**
     * 认证回调函数,登录时调用.
     */
    @Override
    protected org.apache.shiro.authc.AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {

        AuthenticationInfo SAuthenticationInfo = accountService.findUserByToken(authcToken);
        if(SAuthenticationInfo == null){
            return null;
        }
        if(SAuthenticationInfo.isLocked()){
            throw new LockedAccountException();
        }


        byte[] salt = null;
        String password = null;

        if (SAuthenticationInfo.getPasswordAndSalt() != null) {
            salt = EncodeUtils.decodeHex(SAuthenticationInfo.getPasswordAndSalt().getSalt());
            password = SAuthenticationInfo.getPasswordAndSalt().getPassword();
        }
        return new SimpleAuthenticationInfo(SAuthenticationInfo.getUserId(),
                password, new ShiroSimpleByteSource(salt), getName());
    }

    /**
     * 授权查询回调函数, 进行鉴权但缓存中无用户的授权信息时调用.
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        //principal is a userId
        Object principal = SecurityUtils.getSubject().getPrincipal();
        if(principal != null){
            SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
            Set<String> stringPermissions = accountService.findStringPermissions(principal.toString());
            if(!org.springframework.util.CollectionUtils.isEmpty(stringPermissions)){
                info.setStringPermissions(stringPermissions);
            }
            Set<String> stringRoles = accountService.findStringRoles(principal.toString());
            if(!org.springframework.util.CollectionUtils.isEmpty(stringRoles)){
                info.addRoles(stringRoles);
            }

            return info;
        }else {
            return null;
        }

    }

    @Override
    public void clearCachedAuthorizationInfo(PrincipalCollection principals) {
        super.clearCachedAuthorizationInfo(principals);
    }

    /**
     * 更新用户授权信息缓存.
     */
    public void clearCachedAuthorizationInfo(Object principal) {
        SimplePrincipalCollection principals = new SimplePrincipalCollection(principal, getName());
        clearCachedAuthorizationInfo(principals);
    }

    @Override
    protected Object getAuthenticationCacheKey(AuthenticationToken token) {
        return accountService.findPrincipalByToken(token);
    }

    @Override
    protected Object getAuthorizationCacheKey(PrincipalCollection principals) {
        return principals.toString();
    }

    @Override
    public boolean supports(AuthenticationToken token) {
        //return super.supports(token);
        return true;
    }

    /**
     * 清除所有用户授权信息缓存.
     */
    public void clearAllCachedAuthorizationInfo() {
        Cache<Object, AuthorizationInfo> cache = getAuthorizationCache();
        if (cache != null) {
            for (Object key : cache.keys()) {
                cache.remove(key);
            }
        }
    }

    /**
     * 设定Password校验的Hash算法与迭代次数.
     */
    @PostConstruct
    public void initCredentialsMatcher() {
        super.setCredentialsMatcher(PasswordAndSalt.getCredentialsMatcher());
    }

    @Override
    protected void assertCredentialsMatch(AuthenticationToken token, org.apache.shiro.authc.AuthenticationInfo info) throws AuthenticationException {
        if(accountService.validatePasswordWhenLogin(token)){
            super.assertCredentialsMatch(token, info);
        }

    }

    @Override
    protected void checkPermission(Permission permission, AuthorizationInfo info) {
        authorizationValidate(permission);
        super.checkPermission(permission, info);
    }

    @Override
    protected boolean[] isPermitted(List<Permission> permissions, AuthorizationInfo info) {
        if (permissions != null && !permissions.isEmpty()) {
            for (Permission permission : permissions) {
                authorizationValidate(permission);
            }
        }
        return super.isPermitted(permissions, info);
    }

    @Override
    public boolean isPermitted(PrincipalCollection principals, Permission permission) {
        if(authorizationValidate(permission)){ return true;}
        return super.isPermitted(principals, permission);
    }

    @Override
    protected boolean isPermittedAll(Collection<Permission> permissions, AuthorizationInfo info) {
        if (permissions != null && !permissions.isEmpty()) {
            for (Permission permission : permissions) {
                if(authorizationValidate(permission)){ return true;}
            }
        }
        return super.isPermittedAll(permissions, info);
    }

    /**
     * 授权验证方法
     * @param permission
     */
    private boolean authorizationValidate(Permission permission){
        // 模块授权预留接口
        return ShiroUtils.getCurrentUser().isSuperAdmin();

    }

    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }

}
