package com.feihua.framework.constants;

/**
 * @Auther: wzn
 * @Date: 2019/2/20 15:54
 * @Description:
 */
public class ConfigConstant {

    /**
     * 配置key
     */
    public enum ConfigKey{
        /**
         * OSS 系统配置
         */
        OSS_CLOUD_STORAGE_CONFIG_KEY,
        /**
         * 微信支付系统配置
         */
        WXPAY_STORAGE_CONFIG_KEY
    }

    /**
     * 支付类型
     */
    public enum PayType{
        /**
         * 微信支付
         */
        WXPAY,
        /**
         * 阿里支付宝支付
         */
        ALIPAY

    }
    /**
     * 云服务商
     */
    public enum OSSCloud {

        /**
         * 阿里云
         */
        ALY,
        /**
         * 七牛云
         */
        QNY,
        /**
         * 腾讯云
         */
        TXY,
        /**
         * 本地存储
         */
        LOCAL

    }
}
