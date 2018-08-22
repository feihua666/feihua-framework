package com.feihua.framework.shiro;

import com.feihua.framework.shiro.service.QrCodeService;
import com.feihua.utils.http.httpServletRequest.RequestUtils;
import com.feihua.utils.http.httpServletResponse.ResponseCode;
import com.feihua.utils.http.httpServletResponse.ResponseUtils;
import com.feihua.utils.string.StringUtils;
import org.apache.http.HttpStatus;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.web.filter.authc.AnonymousFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;

import javax.imageio.ImageIO;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 扫描二维码登录相关辅助功能，检查登录情况
 * Created by yangwei
 * Created at 2018/3/23 16:14
 */
public class ShiroQRcodeCheckFilter extends AnonymousFilter {

    Logger logger = LoggerFactory.getLogger(ShiroQRcodeCheckFilter.class);


    @Autowired
    private QrCodeService qrCodeService;

    @Override
    protected boolean onPreHandle(ServletRequest request, ServletResponse response, Object mappedValue) {
        // 大写请求方法
        HttpServletRequest httpServletRequest = ((HttpServletRequest)request);
        HttpServletResponse httpServletResponse = ((HttpServletResponse)response);
        if(HttpMethod.GET.matches(httpServletRequest.getMethod())){
            // 只支持ajax请求
            if (RequestUtils.isAjaxRequest(httpServletRequest)) {// 不是ajax请求
                QrCodeService.Phase phase = null;
                String qrCodeContent = httpServletRequest.getParameter("qrCodeContent");
                // 如果没有传二维码内容，且没有在登录状态,认为是被扫码端
                if(org.apache.commons.lang3.StringUtils.isEmpty(qrCodeContent) && !SecurityUtils.getSubject().isAuthenticated()){
                    qrCodeContent = qrCodeService.getScanedClientQrCodeContent();
                    phase = qrCodeService.getPhase(qrCodeContent);
                }else{
                    phase = qrCodeService.getPhase(qrCodeContent);
                }
                Map<String,Object> returntokenStr = new HashMap<>();
                returntokenStr.put("code", ResponseCode.success.getCode());
                returntokenStr.put("msg",ResponseCode.success.getMsg());
                if (phase != null) {
                    returntokenStr.put("data",phase.name());
                }
                httpServletResponse.setStatus(HttpStatus.SC_OK);
                try {
                    ResponseUtils.renderString(httpServletResponse,returntokenStr);
                } catch (Exception e) {
                    logger.error(e.getMessage(),e);
                }
            }

        }

        return false;
    }

}
