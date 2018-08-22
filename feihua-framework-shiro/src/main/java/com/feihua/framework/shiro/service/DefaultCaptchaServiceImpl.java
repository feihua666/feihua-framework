package com.feihua.framework.shiro.service;

import com.feihua.framework.shiro.ShiroCaptchaFilter;
import com.feihua.utils.graphic.ImageUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;

import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * 验证码相关默认实现类
 * Created by yangwei
 * Created at 2018/8/3 18:59
 */
public class DefaultCaptchaServiceImpl implements CaptchaService {

    private final String captcha = "captcha";

    @Override
    public BufferedImage generateCaptcha() {
        int a = RandomUtils.nextInt(1,50);
        int b = RandomUtils.nextInt(1,50);
        String text[]  = new  String[]{a + "","+",b + "","=","?"};
        BufferedImage image = ImageUtils.createSimpleCaptchaImage(100,30, Color.white,20,text,10);
        // 将结果存放在session中
        SecurityUtils.getSubject().getSession().setAttribute(ShiroCaptchaFilter.validateCodeKey,(a + b) + "");
        return image;
    }

    @Override
    public boolean validateCaptcha(String captcha) {
        String r = (String) SecurityUtils.getSubject().getSession().getAttribute(ShiroCaptchaFilter.validateCodeKey);
        if (r != null) {
            return r.equals(captcha);
        }
        return false;
    }

    @Override
    public String resolveCaptcha(HttpServletRequest request) {
        return request.getParameter(captcha);
    }
}
