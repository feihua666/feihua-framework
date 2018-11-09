package com.feihua.wechat.common.dto;

/**
 * @Auther: wzn
 * @Date: 2018/11/2 15:18
 * @Description: 微信模板小程序数据，跳小程序所需数据，不需跳小程序可不用传该数据
 */
public class Miniprogram {
    /**
     * 所需跳转到的小程序appid（该小程序appid必须与发模板消息的公众号是绑定关联关系，暂不支持小游戏）
     */
   private String appid;
    /**
     * 所需跳转到小程序的具体页面路径，支持带参数,（示例index?foo=bar），暂不支持小游戏
     */
   private String pagepath;

    public Miniprogram() {
    }

    public Miniprogram(String appid, String pagepath) {
        this.appid = appid;
        this.pagepath = pagepath;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getPagepath() {
        return pagepath;
    }

    public void setPagepath(String pagepath) {
        this.pagepath = pagepath;
    }
}