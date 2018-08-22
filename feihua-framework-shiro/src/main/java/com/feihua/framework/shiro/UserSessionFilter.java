package com.feihua.framework.shiro;

import com.feihua.framework.shiro.utils.ShiroUtils;
import com.feihua.utils.http.httpServletRequest.RequestUtils;
import com.feihua.utils.http.httpServletResponse.ResponseCode;
import com.feihua.utils.http.httpServletResponse.ResponseUtils;
import com.feihua.utils.properties.PropertiesUtils;
import org.apache.http.HttpStatus;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * 处理session相关事宜
 * Created by yw on 2017/3/13.
 */
public class UserSessionFilter extends AccessControlFilter {

    private static Logger logger = LoggerFactory.getLogger(UserSessionFilter.class);


    // 授权
    public static final String REFRESH_AUTHORIZATION_INFO_FLAG_KEY = "refresh_authorization_info_flag_key";
    // 用户信息userinfo
    public static final String REFRESH_SHIROUSER_INFO_FLAG_KEY = "refresh_shirouser_info_flag_key";
    // 踢出用户
    public static final String USER_KICKOUT_KEY = "user_kickout_key";

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        return false;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = ((HttpServletRequest)request);
        HttpServletResponse httpServletResponse = ((HttpServletResponse)response);
        Subject subject = getSubject(request,response);
        Session session = subject.getSession();
        // 如果已经设置了踢出，则直接踢出
        if("true".equals(session.getAttribute(USER_KICKOUT_KEY))){
            session.removeAttribute(USER_KICKOUT_KEY);
            subject.logout();
            if(RequestUtils.isAjaxRequest(httpServletRequest)){
                Map<String,Object> rMap = new HashMap<>();
                rMap.put("code", ResponseCode.E401_100003.getCode());
                rMap.put("msg",ResponseCode.E401_100003.getMsg());
                httpServletResponse.setStatus(HttpStatus.SC_UNAUTHORIZED);
                try {
                    ResponseUtils.renderString(httpServletResponse,rMap);
                } catch (Exception e) {
                    logger.error(e.getMessage(),e);
                }
            }else{
                WebUtils.getSavedRequest(request);
                //再重定向
                WebUtils.issueRedirect(request, response, PropertiesUtils.getProperty("shiro.url.kickoutUrl"));
            }


            return false;
        }

        if("true".equals(session.getAttribute(REFRESH_AUTHORIZATION_INFO_FLAG_KEY))){
            session.removeAttribute(REFRESH_AUTHORIZATION_INFO_FLAG_KEY);
            ShiroUtils.clearCachedAuthorizationInfo();
        }
        if("true".equals(session.getAttribute(REFRESH_SHIROUSER_INFO_FLAG_KEY))){
            session.removeAttribute(REFRESH_SHIROUSER_INFO_FLAG_KEY);
            ShiroUtils.initCurrentUserToSession();
        }
        // 这里添加刷新用户权限
        return true;
    }
}
