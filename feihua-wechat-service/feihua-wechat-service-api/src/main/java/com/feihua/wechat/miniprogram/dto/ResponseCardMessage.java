package com.feihua.wechat.miniprogram.dto;

import java.util.HashMap;
import java.util.Map;

/**
 * 小程序卡片消息
 * Created by yangwei
 * Created at 2018/8/16 15:49
 */
public class ResponseCardMessage extends ResponseMessage {

    private Map<String,String> miniprogrampage;
    public ResponseCardMessage(){
        super("miniprogrampage");
    }
    public ResponseCardMessage(String title, String pagepath, String thumb_media_id){
        this();
        this.miniprogrampage = new HashMap<>();
        miniprogrampage.put("title",title);
        miniprogrampage.put("pagepath",pagepath);
        miniprogrampage.put("thumb_media_id",thumb_media_id);
    }

    public Map<String, String> getMiniprogrampage() {
        return miniprogrampage;
    }

    public void setMiniprogrampage(Map<String, String> miniprogrampage) {
        this.miniprogrampage = miniprogrampage;
    }
}
