package com.feihua.framework.rest;


import com.feihua.utils.http.httpServletResponse.ResponseCode;
import feihua.jdbc.api.pojo.Page;
import feihua.jdbc.api.utils.PageUtils;
import org.apache.commons.collections.map.HashedMap;

import java.util.Map;

public class ResponseJsonRender {

    public static String DATAKEY_PAGE="page";
    public static String DATAKEY_DATA="content";
    public static String DATAKEY_DICT="dict";

    /**
     * 消息
     */
    private String msg = null;
    private String code = ResponseCode.success.getCode();
    private Map<String,Object> data = new HashedMap();
    public ResponseJsonRender(){
        afterConstructed();
    }
    /**
     * 只添加成功数据
     * @param data
     */
    public ResponseJsonRender(Object data){

        this(null,data);
    }

    /**
     * 消息
     * @param msg
     */
    public ResponseJsonRender(String msg){

        this.msg = msg;
    }


    /**
     * 状态码、消息、数据
     * @param msg
     * @param data
     */
    public ResponseJsonRender( String msg, Object data){
        this(msg,data,null);
        afterConstructed();

    }
    /**
     * 状态码、消息、数据
     * @param msg
     * @param data
     */
    public ResponseJsonRender( String msg, Object data, Page page){

        this.msg = msg;
        this.setData(data);
        this.setPage(page);
    }
    private void afterConstructed(){
        Page page = PageUtils.getPageFromThreadLocal();
        if(page != null && page.isPageable())
        this.setPage(page);
    }
    public String getMsg() {
        if(msg == null){
            if(ResponseCode.success.getCode().equals(this.code)){
                msg = ResponseCode.success.getMsg();
            }else{
                msg = ResponseCode.valueOf(this.code).getMsg();
            }

        }
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }


    public Map<String,Object> getData() {
        return data;
    }

    public void setData(Object data) {
        this.data.put(DATAKEY_DATA,data);
    }
    public void addData(String name,Object data) {
        if(DATAKEY_DATA.equals(name)||DATAKEY_PAGE.equals(name)|| DATAKEY_DICT.equals(name)){
            throw new RuntimeException("the name param can not include the keywords like ["+DATAKEY_DATA+"]["+DATAKEY_PAGE+"]");
        }
        this.data.put(name,data);
    }
    public void setPage(Page page) {
        if(page == null){
            this.data.remove(DATAKEY_PAGE);
        }else {
            this.data.put(DATAKEY_PAGE,page);
        }

    }
    public void setDict(Object obj) {
        if(obj == null){
            this.data.remove(DATAKEY_DICT);
        }else {
            this.data.put(DATAKEY_DICT,obj);
        }

    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
