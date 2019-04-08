package com.feihua.framework.shiro;


import com.feihua.exception.BaseException;
import com.feihua.framework.constants.DictEnum;
import com.feihua.framework.jedis.utils.JedisUtils;
import com.feihua.framework.shiro.pojo.ShiroUser;
import com.feihua.framework.shiro.pojo.token.*;
import com.feihua.framework.shiro.service.AccountService;

import com.feihua.framework.shiro.service.CaptchaService;
import com.feihua.framework.shiro.service.QrCodeService;
import com.feihua.framework.shiro.utils.ShiroUtils;
import com.feihua.utils.http.httpServletRequest.RequestUtils;
import com.feihua.utils.http.httpServletResponse.ResponseCode;
import com.feihua.utils.http.httpServletResponse.ResponseUtils;
import com.feihua.utils.properties.PropertiesUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;


import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yw on 2017/3/13.
 */
public class ShiroFormAuthenticationFilter extends FormAuthenticationFilter {

    private static Logger logger = LoggerFactory.getLogger(ShiroFormAuthenticationFilter.class);

    public static String param_loginType_key = "loginType";
    public static String param_principal_key = "principal";
    public static String param_password_key = "password";
    public static String param_loginClient_key = "loginClient";

    private static String failedMsgKey = "loginFailedKey";
    private static String failedStatusKey = "loginFailedStatusKey";
    private static String failedResponseCodeKey = "loginFailedResponseCodeKey";
    private static String tryLoginMaxNum_key = "tryLoginMaxNum_key";

    @Override
    public void afterCompletion(ServletRequest request, ServletResponse response, Exception exception) throws Exception {
        super.afterCompletion(request, response, exception);
    }

    private String loginUrlHtml;
    @Autowired
    private AccountService accountService;
    @Autowired
    private CaptchaService captchaService;
    @Autowired
    private QrCodeService qrCodeService;

    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {
        String loginClient = this.resolveLoginClient(request);
        // 如果尝试登录次数已经超越设置，开始验证验证码
        int configMaxNum = PropertiesUtils.getInteger("shiro.tryLoginMaxNum").intValue();
        if(accountService.validateCaptchaWhenLogin(request,response) && getTryLoginMaxNum() >= configMaxNum){
            String code = captchaService.resolveCaptcha((HttpServletRequest) request);
            //验证码
            if (!captchaService.validateCaptcha(code)) {
                request.setAttribute(failedStatusKey,HttpServletResponse.SC_BAD_REQUEST);
                request.setAttribute(failedMsgKey,ResponseCode.E400_100005.getMsg());
                request.setAttribute(failedResponseCodeKey,ResponseCode.E400_100005.getCode());
                return this.onLoginFailure(null,new AuthenticationException(),request,response);
            }
        }


        AuthenticationToken token = null;
        try {
            token = this.createToken(request, response);
        }catch (Exception e){
           return this.onLoginFailure(token,new AuthenticationException(e),request,response);
        }
        if(token == null) {
            String msg = "createToken method implementation returned null. A valid non-null AuthenticationToken must be created in order to execute a login attempt.";
            throw new IllegalStateException(msg);
        } else {
            try {
                Subject subject = this.getSubject(request, response);
                subject.login(token);
                return this.onLoginSuccess(token, subject, request, response);
            } catch (AuthenticationException ae) {
                return this.onLoginFailure(token, ae, request, response);
            }
        }
    }

    @Override
    protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) {
        String loginType_str = accountService.resolveLoginType(request);

        String principal_str = request.getParameter(param_principal_key);
        String password_str = request.getParameter(param_password_key);
        DictEnum.LoginType loginType = null;
        try {
            loginType = DictEnum.LoginType.valueOf(loginType_str);
        }catch (IllegalArgumentException e){
            logger.warn("不支持的登录类型{}，请自行处理。",loginType_str);
        }
        AuthenticationToken token = null;
        String host = getSubject(request,response).getSession().getHost();
        switch (loginType){
            case ACCOUNT:
                AccountPasswordToken accountToken = new AccountPasswordToken(principal_str,password_str,isRememberMe(request),host);
                token = accountToken;
                break;
            case MOBILE:
                MobilePasswordToken mobileToken = new MobilePasswordToken(principal_str,password_str,isRememberMe(request),host);
                token = mobileToken;
                break;
            case EMAIL:
                EmailPasswordToken emailToken = new EmailPasswordToken(principal_str,password_str,isRememberMe(request),host);
                token = emailToken;
                break;
            case QRCODE:
                QrcodeToken qr_accountToken = new QrcodeToken();
                qr_accountToken.setHost(host);
                Object userId = qrCodeService.getScanClientUserId(qrCodeService.getScanedClientQrCodeContent());
                qr_accountToken.setUserId((String) userId);
                token = qr_accountToken;
                break;
            // 第三方登录,其它登录托管
            default:
                token = accountService.createToken(request,loginType,accountService.resolveLoginType(request));
        }
        if (token == null) {
            request.setAttribute(failedStatusKey,HttpServletResponse.SC_BAD_REQUEST);
            request.setAttribute(failedMsgKey,ResponseCode.E400_100004.getMsg());
            request.setAttribute(failedResponseCodeKey,ResponseCode.E400_100004.getCode());
            throw new IllegalArgumentException(ResponseCode.E400_100004.getMsg());
        }

        return token;
    }


    @Override
    protected boolean onLoginFailure(AuthenticationToken token,
                                     AuthenticationException e, ServletRequest request, ServletResponse response) {

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        String loginType_str = accountService.resolveLoginType(request);
        // 如果是二维码登录，登录失败 后，设置失败 的阶段
        if(DictEnum.LoginType.QRCODE.name().equals(loginType_str)){
            qrCodeService.setPhase(qrCodeService.getScanedClientQrCodeContent(), QrCodeService.Phase.le);
        }

        if (DictEnum.LoginType.ACCOUNT.name().equals(loginType_str)
                ||DictEnum.LoginType.EMAIL.name().equals(loginType_str)
                ||DictEnum.LoginType.MOBILE.name().equals(loginType_str)){
            // 统计登录次数
            countTryLoginMaxNum();
        }


        //异常消息内容
        String msg = "";
        int httpcode = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
        String code = "0";
        if (e instanceof UnknownAccountException){
            msg = "unknownAccount";
            httpcode = HttpServletResponse.SC_UNAUTHORIZED;
            code = ResponseCode.E400_100001.getCode();
        }else if (e instanceof IncorrectCredentialsException){
            msg = "incorrectCredentials";
            httpcode = HttpServletResponse.SC_UNAUTHORIZED;
            code = ResponseCode.E400_100001.getCode();
        }else if (e instanceof LockedAccountException){
            msg = "lockedAccount";
            httpcode = HttpServletResponse.SC_UNAUTHORIZED;
            code = ResponseCode.E400_100002.getCode();
        }else if (e instanceof AuthenticationException){
            if (request.getAttribute(failedMsgKey) != null) {
                msg = request.getAttribute(failedMsgKey).toString();
                httpcode = (int)request.getAttribute(failedStatusKey);
                code = request.getAttribute(failedResponseCodeKey).toString();
            }else {
                msg = "authenticate faild";
                httpcode = HttpServletResponse.SC_UNAUTHORIZED;
                code = ResponseCode.E401_100001.getCode();
            }
        }else {
            msg = "authenticate faild.internal error";
            code = ResponseCode.E401_100001.getCode();
        }


        httpServletResponse.setStatus(httpcode);
        if (!RequestUtils.isAjaxRequest(httpServletRequest)) {// 不是ajax请求
            super.onLoginFailure(token,new AuthenticationException(e.getMessage()+"[msg:"+ msg +",code:"+ code +"]"),request,response);
        }else {
            try {
                Map<String,Object> msgMap = new HashMap<>();
                msgMap.put("msg",msg);
                msgMap.put("code",code);
                ResponseUtils.renderString(httpServletResponse,msgMap);
            } catch (Exception e1) {
            }
        }

        return false;

    }

    /**
     * 尝试登录次数key
     * @return
     */
    public static String getTryLoginMaxNumKey(){
        return tryLoginMaxNum_key + SecurityUtils.getSubject().getSession().getHost();
    }

    /**
     * 尝试登录次数
     * @return
     */
    public static int getTryLoginMaxNum(){
        String num = JedisUtils.get(getTryLoginMaxNumKey());
        if(num == null){
            return 0;
        }else {
            return Integer.parseInt(num);
        }
    }
    /**
     * 统计尝试登录次数
     */
    public static void countTryLoginMaxNum(){
        String num = JedisUtils.get(getTryLoginMaxNumKey());

        if (num == null) {
            num = 1 + "";
        }else{
            num = (Integer.parseInt(num) + 1) + "";
        }
        JedisUtils.set(getTryLoginMaxNumKey(),num,0);
        logger.info("尝试登录次数：{}={}",getTryLoginMaxNumKey(),num);
    }
    public static void removeTryLoginMaxNum(){
        JedisUtils.del(getTryLoginMaxNumKey());
    }

    /**
     * 获取loginclient
     * @param request
     * @return
     */
    public  String resolveLoginClient(ServletRequest request){
        // 取登录客户端
        String loginClient = accountService.resolveLoginClient(request);
        if(StringUtils.isEmpty(loginClient)){
            throw new BaseException("loginClient can not be null," + ShiroFormAuthenticationFilter.param_loginClient_key + " param must pass");
        }
        return loginClient;
    }
    @Override
    protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request, ServletResponse response) throws Exception {
        String loginType = accountService.resolveLoginType(request);
        String loginClient = this.resolveLoginClient(request);

        String principal = request.getParameter(param_principal_key);


        logger.info("用户登录成功[principal:{},token:{},loginType:{},header:{}]",principal, SecurityUtils.getSubject().getSession().getId(),loginType, RequestUtils.getHeaderText((HttpServletRequest)request));
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        // 取登录方式




        // 如果是二维码登录，登录成功后，设置失败的阶段
        if(DictEnum.LoginType.QRCODE.name().equals(resolveLoginClient(request))){
            qrCodeService.setPhase(qrCodeService.getScanedClientQrCodeContent(), QrCodeService.Phase.l);
        }
        // 注意调用先后顺序，initCurrentUserToSession中用到了该值，一定要放在前面
        SecurityUtils.getSubject().getSession().setAttribute(ShiroUtils.SHIRO_USER_LOGIN_TYPE_KEY,loginType);
        SecurityUtils.getSubject().getSession().setAttribute(ShiroUtils.SHIRO_USER_LOGIN_CLIENT_KEY,loginClient);
        // 把当前登录用户放session中
        ShiroUtils.initCurrentUserToSession();

        // 清除尝试的登录次数
        removeTryLoginMaxNum();

        // 踢出其它同一客户端
        ShiroUtils.kickoutOtherClient(subject.getSession().getId().toString(),ShiroUtils.getCurrentUser().getId(),loginClient);

        // 回调登录成功
        accountService.onLoginSuccess(token,subject,request,response);

        if (!RequestUtils.isAjaxRequest(httpServletRequest)) {// 不是ajax请求
            issueSuccessRedirect(request, response);
        } else {
            String tokenStr = SecurityUtils.getSubject().getSession().getId().toString();

            Map<String,Object> returntokenStr = new HashMap<>();
            returntokenStr.put("token",tokenStr);
            returntokenStr.put("code",ResponseCode.success.getCode());
            returntokenStr.put("msg",ResponseCode.success.getMsg());
            ResponseUtils.renderString(httpServletResponse,returntokenStr);
        }
        return false;
    }

    @Override
    public boolean onPreHandle(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {

        String loginType = accountService.resolveLoginType(request);

        String principal = request.getParameter(param_principal_key);

        Subject currentUser = SecurityUtils.getSubject();
        if (currentUser.isAuthenticated()) {
            //当已经存在登录时，如果用其他账户登录，则让当前账户登出
            currentUser.logout();
        }
        logger.info("用户尝试登录[principal:{},token:{},loginType:{},header:{}]",principal, SecurityUtils.getSubject().getSession().getId(),loginType, RequestUtils.getHeaderText((HttpServletRequest)request));

        return super.onPreHandle(request, response, mappedValue);
    }

    @Override
    protected void redirectToLogin(ServletRequest request, ServletResponse response) throws IOException {
        if(StringUtils.isEmpty(loginUrlHtml)){
            super.redirectToLogin(request,response);
        }else{
            WebUtils.issueRedirect(request, response, loginUrlHtml);
        }

    }

    public void setLoginUrlHtml(String loginUrlHtml) {
        this.loginUrlHtml = loginUrlHtml;
    }
}
