package com.feihua.framework.shiro;

import com.feihua.framework.shiro.service.CaptchaService;
import com.feihua.framework.shiro.service.QrCodeService;
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
 * 扫描二维码登录相关辅助功能,生成和授权登录
 * Created by yangwei
 * Created at 2018/3/23 16:14
 */
public class ShiroQRcodeFilter extends AnonymousFilter {

    Logger logger = LoggerFactory.getLogger(ShiroQRcodeFilter.class);


    @Autowired
    private QrCodeService qrCodeService;

    @Override
    protected boolean onPreHandle(ServletRequest request, ServletResponse response, Object mappedValue) {
        // 大写请求方法
        HttpServletRequest httpServletRequest = ((HttpServletRequest)request);
        HttpServletResponse httpServletResponse = ((HttpServletResponse)response);
        // 生成登录二维码
        if (HttpMethod.GET.matches(httpServletRequest.getMethod())) {
            BufferedImage image = qrCodeService.generateQrCode();
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
        // 扫码后登录提交授权
        else if(HttpMethod.POST.matches(httpServletRequest.getMethod())){
            // 只支持ajax请求
            if (RequestUtils.isAjaxRequest(httpServletRequest)) {// 不是ajax请求
                Map<String,Object> returntokenStr = new HashMap<>();
                returntokenStr.put("code", ResponseCode.success.getCode());
                returntokenStr.put("msg",ResponseCode.success.getMsg());
                String qrCodeContent = httpServletRequest.getParameter("qrCodeContent");
                // 可选操作
                String opt = httpServletRequest.getParameter("opt");
                if("phase".equals(opt)){
                    String phase = httpServletRequest.getParameter("phase");
                    qrCodeService.setPhase(qrCodeContent, QrCodeService.Phase.valueOf(phase));
                }else if("authorize".equals(opt)){
                    qrCodeService.authorizeLogin(qrCodeContent);
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
