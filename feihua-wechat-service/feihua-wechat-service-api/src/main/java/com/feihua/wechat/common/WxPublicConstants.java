package com.feihua.wechat.common;

/**
 * @Auther: wzn
 * @Date: 2018/10/31 13:51
 * @Description:
 */
public class WxPublicConstants {
    /**
     * 微信账号类型
     */
    public enum WxAccountType {
        /**
         * 小程序
         */
        WEIXIN_MINIPROGRAM("weixin_miniprogram"),

        /**
         * 公众号
         */
        WEIXIN_PUBLICPLATFORM("weixin_publicplatform");
        private String value;

        private WxAccountType(String value) {
            this.value = value;
        }

        public String value() {
            return this.value;
        }
    }

    /**
     * 微信账号类型
     */
    public enum WxMsgType {


        /**
         * 文本
         */
        TEXT("text"),
        /**
         * 图文
         */
        NEWS("news"),

        /**
         * 图片
         */
        IMAGE("image");
        private String value;

        private WxMsgType(String value) {
            this.value = value;
        }

        public String value() {
            return this.value;
        }
    }

}
