package com.feihua.wechat.publicplatform.dto;


import com.feihua.wechat.publicplatform.PublicConstants;

import java.util.HashMap;
import java.util.Map;


/**
 * @Auther: wzn
 * @Date: 2018/11/5 13:50
 * @Description: 微信发送图片消息
 */
public class ResponseImageMessage extends ResponseMessage {
    private Map<String, Object> Image;

    public ResponseImageMessage() {
        super();
        setMsgType(PublicConstants.MessageType.image.name());
    }

    /**
     * 格式化成符合微信API要求的实体
     *
     * @param ToUserName   普通用户openid
     * @param FromUserName 开发者微信号（公众平台原始ID）
     * @param MediaId      通过上传多媒体文件，得到的id。
     *
     * @return 符合API要求的实体
     *
     * @see
     */
    public ResponseImageMessage xmlFormatBean(String ToUserName, String FromUserName, String MediaId) {
        this.setToUserName(ToUserName);
        this.setFromUserName(FromUserName);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("MediaId", MediaId);
        this.setImage(map);
        return this;
    }

    public Map<String, Object> getImage() {
        return Image;
    }

    public void setImage(Map<String, Object> image) {
        Image = image;
    }
}
