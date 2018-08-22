package com.feihua.framework.shiro.service;

import com.feihua.exception.DataNotFoundException;
import com.feihua.framework.jedis.utils.JedisUtils;
import com.feihua.framework.shiro.utils.ShiroUtils;
import com.feihua.utils.graphic.ImageUtils;
import com.feihua.utils.string.StringUtils;
import com.google.zxing.WriterException;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.UUID;

/**
 * 二维码登录相关
 * Created by yangwei
 * Created at 2018/8/7 14:10
 */
public class DefaultQrCodeServiceImpl implements QrCodeService {

    private static final Logger logger = LoggerFactory.getLogger(DefaultQrCodeServiceImpl.class);

    private String qrCodeLoginPrefix = "qrcode_login_";
    private String qrCodeLoginUserInfoPrefix = "qrcode_login_userInfo_";



    @Override
    public BufferedImage generateQrCode() {

        String uuid = UUID.randomUUID().toString().replace("-","");
        String content = "login-" + uuid;
        try {
            BufferedImage image = ImageUtils.createQrCode(165, content, "utf-8", 0, Color.white,  Color.black);
            setScanedClientQrCodeContent(uuid);
            // 设置阶段为已生成
            setPhase(uuid,Phase.g);
            return image;
        } catch (WriterException e) {
            logger.error(e.getMessage(),e);
        }

        return null;
    }

    @Override
    public void authorizeLogin(String qrCodeContent) {
        String phaseStr = JedisUtils.get(qrCodeLoginPrefix + qrCodeContent);
        // 如果不存在，说明已经失效
        if(StringUtils.isEmpty(phaseStr)){
            throw new DataNotFoundException("qrCodeContent not found");
        }
        // 如果存在
        else {

            setScanClientUserId(qrCodeContent,ShiroUtils.getCurrentUser().getId());
            // 修改一下状态,改为已授权
            setPhase(qrCodeContent,Phase.a);

        }
    }

    @Override
    public void setPhase(String qrCodeContent,Phase phase) {
        JedisUtils.set(qrCodeLoginPrefix + qrCodeContent, phase.name(),300);
    }

    @Override
    public Phase getPhase(String qrCodeContent) {
        String phaseStr = JedisUtils.get(qrCodeLoginPrefix + qrCodeContent);
        // 如果不存在，说明已经失效
        if(StringUtils.isEmpty(phaseStr)){
            return null;
        }else {
            return Phase.valueOf(phaseStr);
        }
    }

    @Override
    public void setScanClientUserId(String qrCodeContent, Object userId) {
        // 设置当前登录用户信息（扫码端已经登录的用户）,这里只记录了用户id
        JedisUtils.set(qrCodeLoginUserInfoPrefix + qrCodeContent, (String) userId,300);
    }

    @Override
    public Object getScanClientUserId(String qrCodeContent) {
        return JedisUtils.get(qrCodeLoginUserInfoPrefix + qrCodeContent);
    }

    @Override
    public void setScanedClientQrCodeContent(String qrCodeContent) {
        SecurityUtils.getSubject().getSession().setAttribute("login_qrCodeContent",qrCodeContent);
    }

    @Override
    public String getScanedClientQrCodeContent() {
        return (String) SecurityUtils.getSubject().getSession().getAttribute("login_qrCodeContent");
    }

}
