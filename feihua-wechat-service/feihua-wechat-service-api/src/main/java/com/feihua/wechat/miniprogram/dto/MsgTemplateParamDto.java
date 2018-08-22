package com.feihua.wechat.miniprogram.dto;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 下发模板消息参数
 * Created by yangwei
 * Created at 2018/8/16 13:50
 */
public class MsgTemplateParamDto implements Serializable{

    private String touser;
    private String template_id;
    private String page;
    private String form_id;
    private Map<String,Object> data;
    private String emphasis_keyword;

    public void addData(String key,Object value){
        if(data == null){
            data = new HashMap<>();
        }
        Map<String,Object> valueMap = new HashMap<>();
        valueMap.put("value",value);
        data.put(key,valueMap);
    }

    public String getTouser() {
        return touser;
    }

    public void setTouser(String touser) {
        this.touser = touser;
    }

    public String getTemplate_id() {
        return template_id;
    }

    public void setTemplate_id(String template_id) {
        this.template_id = template_id;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getForm_id() {
        return form_id;
    }

    public void setForm_id(String form_id) {
        this.form_id = form_id;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    public String getEmphasis_keyword() {
        return emphasis_keyword;
    }

    public void setEmphasis_keyword(String emphasis_keyword) {
        this.emphasis_keyword = emphasis_keyword;
    }
}
