package com.feihua.framework.shiro.service;

import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;

/**
 * 验证码相关
 * Created by yangwei
 * Created at 2018/8/3 18:53
 */
public interface CaptchaService {
    /**
     * 生成验证码图片对象
     * @return
     */
    BufferedImage generateCaptcha();

    /**
     * 验证是否正确
     * @param code
     * @return
     */
    boolean validateCaptcha(String code);

    /**
     * 获取验证码
     * @param request
     * @return
     */
    String resolveCaptcha(HttpServletRequest request);
}
