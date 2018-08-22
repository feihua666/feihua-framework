package com.feihua.wechat.miniprogram.dto;

import java.util.HashMap;
import java.util.Map;

/**
 * 图文消息
 * Created by yangwei
 * Created at 2018/8/16 15:49
 */
public class ResponseLinkMessage extends ResponseMessage {

    private Map<String,String> link;
    public ResponseLinkMessage(){
        super("link");
    }
    public ResponseLinkMessage(String title,String description,String url,String thumb_url){
        this();
        this.link = new HashMap<>();
        link.put("title",title);
        link.put("description",description);
        link.put("url",url);
        link.put("thumb_url",thumb_url);
    }

    public Map<String, String> getLink() {
        return link;
    }

    public void setLink(Map<String, String> link) {
        this.link = link;
    }
}
