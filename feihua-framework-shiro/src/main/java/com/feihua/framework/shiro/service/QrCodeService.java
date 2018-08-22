package com.feihua.framework.shiro.service;

import java.awt.image.BufferedImage;

/**
 * 二维码相关
 * Created by yangwei
 * Created at 2018/8/3 18:53
 */
public interface QrCodeService {

    /**
     * 二维码阶段
     */
    public enum Phase {
        g,   // 已生成
        s,   // 已扫码
        a,   // 已授权
        l,    // 已登录
        le    // 已登录失败
    }

    /**
     * 生成二维码图片对象，以供登录扫码用，被扫码端调用
     * @return
     */
    BufferedImage generateQrCode();

    /**
     * 扫描二维码授权登录，一般是移动端扫码授权pc登录，扫码端调用
     * @param qrCodeContent
     * @return
     */
    void authorizeLogin(String qrCodeContent);

    /**
     * 设置二维码的阶段，两端都可调用
     * @param qrCodeContent
     * @param phase
     */
    void setPhase(String qrCodeContent,Phase phase);

    /**
     * 获取二维码登录阶段，两端都可调用
     * @param qrCodeContent
     * @return
     */
    Phase getPhase(String qrCodeContent);

    /**
     * 设置扫码端登录的用户id，扫码端调用
     * @param qrCodeContent
     * @param userId
     */
    void setScanClientUserId(String qrCodeContent,Object userId);

    /**
     * 获取扫码端登录用户的id，两端都可调用
     * @param qrCodeContent
     * @return
     */
    Object getScanClientUserId(String qrCodeContent);

    /**
     * 设置被扫码端二维码内容，被扫码端调用
     * @param qrCodeContent
     */
    void setScanedClientQrCodeContent(String qrCodeContent);

    /**
     * 获取被扫码端二维码内容，被扫码端调用
     */
    String getScanedClientQrCodeContent();
}
