package com.feihua.framework.rest.interceptor;


import com.feihua.framework.base.modules.user.api.ApiBaseUserAccessLasttimePoService;
import com.feihua.framework.base.modules.user.dto.BaseUserAccessLasttimeParamDto;
import com.feihua.framework.shiro.pojo.ShiroUser;
import com.feihua.framework.shiro.utils.ShiroUtils;
import feihua.jdbc.api.utils.OrderbyUtils;
import feihua.jdbc.api.utils.PageUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * 用户访问记录
 * Created by yw on 2016/1/25.
 */
public class UserAccessInterceptor extends HandlerInterceptorAdapter {




    @Autowired
    private ApiBaseUserAccessLasttimePoService apiBaseUserAccessLasttimePoService;
    /**
     * This implementation always returns {@code true}.
     *
     * @param request
     * @param response
     * @param handler
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Session session = SecurityUtils.getSubject().getSession();
        Long accessDataTime = (Long) session.getAttribute("accessDataTime");
        if (accessDataTime == null) {
            session.setAttribute("accessDataTime",System.currentTimeMillis());
        }
        accessDataTime = (Long) session.getAttribute("accessDataTime");
        // 每40秒记录一次
        if (System.currentTimeMillis() - accessDataTime > 400000) {
            ShiroUser su = ShiroUtils.getCurrentUser();
            if (su != null && StringUtils.isNotEmpty(su.getLoginClientId())) {

                BaseUserAccessLasttimeParamDto baseUserAccessLasttimeParamDto = new BaseUserAccessLasttimeParamDto();
                baseUserAccessLasttimeParamDto.setUserId(su.getId());
                baseUserAccessLasttimeParamDto.setUserNickname(su.getNickname());
                baseUserAccessLasttimeParamDto.setClientId(su.getLoginClientId());
                baseUserAccessLasttimeParamDto.setClientName(su.getLoginClientName());
                baseUserAccessLasttimeParamDto.setAccessIp(session.getHost());
                baseUserAccessLasttimeParamDto.setAccessLasttime(new Date());
                baseUserAccessLasttimeParamDto.setCurrentUserId(su.getId());
                // 防止请求太快有重复提交，数据已加了唯一索引，如果有重复数据会有异常，这里拦截一下
                // 不过上面已经加了40s才记录一次，不会有这种情况出现，以防万一
                try {
                    apiBaseUserAccessLasttimePoService.saveUserAccessLasttime(baseUserAccessLasttimeParamDto);
                    session.setAttribute("accessDataTime",System.currentTimeMillis());
                }catch (Exception e){
                }
            }
        }

        return super.preHandle(request, response, handler);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        super.afterCompletion(request, response, handler, ex);
    }
}
