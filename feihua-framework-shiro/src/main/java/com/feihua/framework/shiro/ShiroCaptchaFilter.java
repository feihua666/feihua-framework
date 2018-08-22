package com.feihua.framework.shiro;

import com.feihua.framework.shiro.service.CaptchaService;
import com.feihua.utils.http.httpServletRequest.RequestUtils;
import com.feihua.utils.http.httpServletResponse.ResponseCode;
import com.feihua.utils.http.httpServletResponse.ResponseUtils;
import org.apache.http.HttpStatus;
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
 * Created by yangwei
 * Created at 2018/3/23 16:14
 */
public class ShiroCaptchaFilter extends AnonymousFilter {

    Logger logger = LoggerFactory.getLogger(ShiroCaptchaFilter.class);

    public final static String validateCodeKey = "validateCodeKey";

    @Autowired
    private CaptchaService captchaService;


    @Override
    protected boolean onPreHandle(ServletRequest request, ServletResponse response, Object mappedValue) {
        // 大写请求方法
        HttpServletRequest httpServletRequest = ((HttpServletRequest)request);
        HttpServletResponse httpServletResponse = ((HttpServletResponse)response);
        // 获取验证码
        if (HttpMethod.GET.matches(httpServletRequest.getMethod())) {
            BufferedImage image = captchaService.generateCaptcha();
            httpServletResponse.setContentType("image/jpeg");
            httpServletResponse.setDateHeader("expries", -1);
            httpServletResponse.setHeader("Cache-Control", "no-cache");
            httpServletResponse.setHeader("Pragma", "no-cache");
            try {
                ImageIO.write(image, "jpg", response.getOutputStream());
            } catch (IOException e) {
                logger.error(e.getMessage(),e);
            }
        }
        // 判断验证码是否正确
        else if(HttpMethod.POST.matches(httpServletRequest.getMethod())){
            // 只支持ajax请求
            if (RequestUtils.isAjaxRequest(httpServletRequest)) {// 不是ajax请求
                String captcha = httpServletRequest.getParameter("captcha");
                // 如果验证码正常
                if(captchaService.validateCaptcha(captcha)){
                    Map<String,Object> returntokenStr = new HashMap<>();
                    returntokenStr.put("code", ResponseCode.success.getCode());
                    returntokenStr.put("msg",ResponseCode.success.getMsg());
                    httpServletResponse.setStatus(HttpStatus.SC_OK);
                    try {
                        ResponseUtils.renderString(httpServletResponse,returntokenStr);
                    } catch (Exception e) {
                        logger.error(e.getMessage(),e);
                    }
                }
                // 如果验证码不正确
                else{
                    Map<String,Object> returntokenStr = new HashMap<>();
                    returntokenStr.put("code", ResponseCode.E400_100005.getCode());
                    returntokenStr.put("msg",ResponseCode.E400_100005.getMsg());
                    httpServletResponse.setStatus(HttpStatus.SC_BAD_REQUEST);
                    try {
                        ResponseUtils.renderString(httpServletResponse,returntokenStr);
                    } catch (Exception e) {
                        logger.error(e.getMessage(),e);
                    }
                }
            }

        }

        return false;
    }

}
