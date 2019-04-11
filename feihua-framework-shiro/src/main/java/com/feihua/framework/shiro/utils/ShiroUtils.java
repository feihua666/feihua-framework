package com.feihua.framework.shiro.utils;

import com.feihua.framework.shiro.pojo.PasswordAndSalt;
import com.feihua.framework.shiro.pojo.ShiroUser;
import com.feihua.framework.shiro.realms.Realm;
import com.feihua.framework.shiro.service.AccountService;
import com.feihua.framework.shiro.session.ShiroJedisSessionDAO;
import com.feihua.utils.encode.EncodeUtils;
import com.feihua.utils.spring.SpringContextHolder;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.DefaultSessionManager;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static com.feihua.framework.shiro.UserSessionFilter.*;
import static com.feihua.framework.shiro.pojo.PasswordAndSalt.getCredentialsMatcher;

/**
 * Created by yangwei
 * Created at 2017/7/24 16:36
 */
public class ShiroUtils {

    private static Logger logger = LoggerFactory.getLogger(ShiroUtils.class);


    private static String SHIRO_USER_SESSION_KEY = "shiro_user_session_key";
    public static String SHIRO_USER_LOGIN_TYPE_KEY = "shiro_user_login_type_key";
    public static String SHIRO_USER_LOGIN_CLIENT_KEY = "SHIRO_USER_LOGIN_CLIENT_KEY";


    private static AccountService accountService;
    private static Realm  realm;

    /**
     * 获取当前用户
     * @return
     */
    public static ShiroUser getCurrentUser(){

        ShiroUser su = (ShiroUser) SecurityUtils.getSubject().getSession().getAttribute(SHIRO_USER_SESSION_KEY);

        if(su == null){
            initCurrentUserToSession();
            su = (ShiroUser) SecurityUtils.getSubject().getSession().getAttribute(SHIRO_USER_SESSION_KEY);
        }
        return su;

    }

    /**
     * 获取session中的用户信息
     * @param session
     * @return
     */
    public static ShiroUser getShiroUser(Session session){
        return (ShiroUser) session.getAttribute(SHIRO_USER_SESSION_KEY);
    }
    public static String getLoginType(Session session){
        return (String) session.getAttribute(SHIRO_USER_LOGIN_TYPE_KEY);
    }
    public static String getLoginClient(Session session){
        return (String) session.getAttribute(SHIRO_USER_LOGIN_CLIENT_KEY);
    }
    public static Date getLoginTime(Session session){
        return session.getStartTimestamp();
    }
    public static Date getLastAccessTime(Session session){
        return session.getLastAccessTime();
    }
    public static String getHost(Session session){
        return session.getHost();
    }
    public static String getKickout(Session session){
        return (String) session.getAttribute(USER_KICKOUT_KEY);
    }
    public static List<Session> getSessionsByUserId(final String userId){
        final SessionDAO sessionDAO = SpringContextHolder.getBean(DefaultSessionManager.class).getSessionDAO();
        List<Session> sessions  = ((ShiroJedisSessionDAO)sessionDAO).getSessionsByUserId(userId);
        return sessions;
    }
    /**
     * 把当前登录用户信息放到session
     */
    public static void initCurrentUserToSession(){
        Object principal = SecurityUtils.getSubject().getPrincipal();
        if(principal != null){
            ShiroUser su = getAccountService().findUserInfo(principal.toString());
            if(su != null){

                //登录成功之后设置的
                ShiroUser sessionSu = (ShiroUser) SecurityUtils.getSubject().getSession().getAttribute(SHIRO_USER_SESSION_KEY);

                if (sessionSu != null){
                    su.setLoginType(sessionSu.getLoginType());
                    su.setHost(sessionSu.getHost());
                    su.setLoginClient(sessionSu.getLoginClient());
                }else {
                    su.setLoginType((String) SecurityUtils.getSubject().getSession().getAttribute(SHIRO_USER_LOGIN_TYPE_KEY));
                    su.setHost(SecurityUtils.getSubject().getSession().getHost());
                    su.setLoginClient((String) SecurityUtils.getSubject().getSession().getAttribute(SHIRO_USER_LOGIN_CLIENT_KEY));
                }
                SecurityUtils.getSubject().getSession().setAttribute(SHIRO_USER_SESSION_KEY,su);
                SecurityUtils.getSubject().getSession().touch();
            }
        }
    }

    private static AccountService getAccountService(){

        if(ShiroUtils.accountService == null){
            throw new RuntimeException("accountService has not bean init. use setAccountService method to init");
        }

        return ShiroUtils.accountService;
    }

    public static void setAccountService(AccountService accountService) {
        if(ShiroUtils.accountService == null){
            ShiroUtils.accountService = accountService;
        }else {
            //throw new RuntimeException("accountService in ShiroUtils has not bean set");
        }
    }

    public static void setRealm(Realm realm) {
        if (ShiroUtils.realm == null) {
            ShiroUtils.realm = realm;
        }
    }

    public static boolean validatePassword(String tokenPassword, PasswordAndSalt ps) {
        UsernamePasswordToken token = new UsernamePasswordToken();
        token.setPassword(tokenPassword.toCharArray());

        byte[] _salt = EncodeUtils.decodeHex(ps.getSalt());
        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(ps,
                ps.getPassword(), ByteSource.Util.bytes(_salt), "");
        return getCredentialsMatcher().doCredentialsMatch(token, info);
    }

    /**
     * 清除当前登录用户的授权信息
     */
    public static void clearCachedAuthorizationInfo(){
        ShiroUtils.realm.clearCachedAuthorizationInfo(SecurityUtils.getSubject().getPrincipal());
    }


    /**
     * 获取设置的同一客户端可登录的session数量
     * @param loginClient
     * @return
     */
    public static int getClientMaxnum(String loginClient){

        return accountService.resolveLoginClientMaxnum(loginClient);
    }

    /**
     * 踢出用户，使其不在登录状态，一般用于使用用户下线，
     * @param userId
     * @param loginClient  可以为null，表示所有客户端
     */
    public static void kickoutClient(final String userId,final String loginClient){
        final SessionDAO sessionDAO = SpringContextHolder.getBean(DefaultSessionManager.class).getSessionDAO();
        List<Session> sessions  = ((ShiroJedisSessionDAO)sessionDAO).getSessionsByUserId(userId);
        if (sessions != null) {
            for (Session session : sessions) {
                if(loginClient == null){
                    session.setAttribute(USER_KICKOUT_KEY,USER_KICKOUT_VALUE_KICKOUT);
                    sessionDAO.update(session);
                }else{
                    ShiroUser su = getShiroUser(session);
                    if (su != null && su.getLoginClient().equals(loginClient)) {
                        session.setAttribute(USER_KICKOUT_KEY,USER_KICKOUT_VALUE_KICKOUT);
                        sessionDAO.update(session);
                    }
                }
            }
        }
    }
    /**
     * 踢用户，使其不在登录状态，一般用于一个用户登录其它同一设备，另一个同一设备自动下线
     * 该方法不会将用户所有设备都下线，每个设备保留的登录数以设置为准
     * @param excludeSessionId 不包括的sessionId,
     * @param userId 踢出的用户id
     * @param loginClient 客户端类型，不可以为null
     */
    public static void kickoutOtherClient(final String excludeSessionId,final String userId,final String loginClient){
        final int  maxnum = getClientMaxnum(loginClient);
        //如果不限制，不踢除用户
        if(-1 == maxnum){
            return;
        }
        if(userId == null) return;
        final SessionDAO sessionDAO = SpringContextHolder.getBean(DefaultSessionManager.class).getSessionDAO();
        try {
            //除了当前登录的session,存放其它需要踢除的session
            List<Session> currentUsers = new ArrayList<>();
            List<Session> sessions  = ((ShiroJedisSessionDAO)sessionDAO).getSessionsByUserId(userId);
            for (Session session : sessions) {
                // 不能踢出自己
                if(session.getId().equals(excludeSessionId)){
                    continue;
                }
                ShiroUser su = getShiroUser(session);
                // 默认没有clienttype 则为pc
                if (su != null && su.getLoginClient().equals(loginClient)) {
                    currentUsers.add(session);
                }
            }

            //如果已在的用户数不够保留的用户数，返回
            if (currentUsers.size() <= maxnum-1) return;
            //时间从大到小，踢除最不活跃的session
            Collections.sort(currentUsers, new Comparator<Session>() {
                @Override
                public int compare(Session o1, Session o2) {
                    if (o1.getLastAccessTime().getTime() > o2.getLastAccessTime().getTime()) {
                        return 1;
                    } else if (o1.getLastAccessTime().getTime() < o2.getLastAccessTime().getTime()) {
                        return -1;
                    } else {
                        return 0;
                    }
                }
            });

            int currentSessionSize = currentUsers.size();
            int toBedeletedNum = currentSessionSize - (maxnum-1);
            int deletedNum = 0;
            for (int i = 0; i < currentUsers.size(); i++) {

                if (deletedNum >= toBedeletedNum) {
                    break;
                } else {
                    Session session = currentUsers.get(i);
                    logger.debug("mark session for logoutOtherClient id: {} ", session.getId());
                    session.setAttribute(USER_KICKOUT_KEY,USER_KICKOUT_VALUE_ANOTHER);
                    sessionDAO.update(session);
                    deletedNum++;
                }
            }


        } catch (Exception e) {
            logger.error("踢除其它同一个客户端的同一用户异常", e);
        }
    }

    /**
     * 刷新用户权限数据
     * @param userId
     */
    public static void refreshAuthorizationInfoByUserId(String userId){
        final SessionDAO sessionDAO = SpringContextHolder.getBean(DefaultSessionManager.class).getSessionDAO();
        List<Session> sessions  = ((ShiroJedisSessionDAO)sessionDAO).getSessionsByUserId(userId);
        if (sessions != null) {
            for (Session session : sessions) {
                session.setAttribute(REFRESH_AUTHORIZATION_INFO_FLAG_KEY,"true");
                sessionDAO.update(session);
            }
        }
    }
    /**
     * 刷新登录用户信息
     * @param userId
     */
    public static void refreshShiroUserInfoByUserId(String userId){
        final SessionDAO sessionDAO = SpringContextHolder.getBean(DefaultSessionManager.class).getSessionDAO();
        List<Session> sessions  = ((ShiroJedisSessionDAO)sessionDAO).getSessionsByUserId(userId);
        if (sessions != null) {
            for (Session session : sessions) {
                session.setAttribute(REFRESH_SHIROUSER_INFO_FLAG_KEY,"true");
                sessionDAO.update(session);
            }
        }
    }
    /**
     * 刷新当前登录用户信息
     * 将在下一次请求中生效
     */
    public static void refreshShiroUserInfo(){
        Session session = SecurityUtils.getSubject().getSession();
        session.setAttribute(REFRESH_SHIROUSER_INFO_FLAG_KEY,"true");
        session.touch();
    }

    /**
     * 立即刷新当前登录用户信息
     */
    public static void refreshShiroUserInfoImidiately(){
        initCurrentUserToSession();
    }
}
