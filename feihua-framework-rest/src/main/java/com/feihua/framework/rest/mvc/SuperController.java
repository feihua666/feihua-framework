package com.feihua.framework.rest.mvc;

import com.feihua.framework.rest.ResponseJsonRender;
import com.feihua.framework.shiro.pojo.ShiroUser;
import com.feihua.framework.shiro.utils.ShiroUtils;
import com.feihua.utils.http.httpServletResponse.ResponseCode;
import feihua.jdbc.api.pojo.PageAndOrderbyParamDto;
import feihua.jdbc.api.pojo.PageResultDto;
import feihua.jdbc.api.utils.OrderbyUtils;
import feihua.jdbc.api.utils.PageUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * controller基类，负责一些通用处理方法
 * Created by yw on 2015/9/23.
 */
public class SuperController {
    private static Logger logger = LoggerFactory.getLogger(SuperController.class);


    public ShiroUser getLoginUser(){
        return ShiroUtils.getCurrentUser();
    }
    public String getLoginUserId(){
        ShiroUser su = getLoginUser();
        return su == null ? null: su.getId();
    }
    public PageAndOrderbyParamDto getPageAndOrderbyDto(){
        PageAndOrderbyParamDto pageAndOrderbyParamDto = new PageAndOrderbyParamDto(PageUtils.getPageFromThreadLocal(), OrderbyUtils.getOrderbyFromThreadLocal());

        return pageAndOrderbyParamDto;
    }

    /**
     * 封装pageResultDto
     * @param list
     * @param resultData
     * @return
     */
    public ResponseEntity returnPageResultDto(PageResultDto list,ResponseJsonRender resultData){
        if(list.getData() == null || list.getData().isEmpty()){
            resultData.setCode(ResponseCode.E404_100001.getCode());
            resultData.setMsg(ResponseCode.E404_100001.getMsg());
            return new ResponseEntity(resultData,HttpStatus.NOT_FOUND);
        }else{
            resultData.setData(list.getData());
            resultData.setPage(list.getPage());
            return new ResponseEntity(resultData, HttpStatus.OK);
        }
    }
    /**
     * 封装object
     * @param obj
     * @param resultData
     * @return
     */
    public ResponseEntity returnDto(Object obj,ResponseJsonRender resultData){
        if(obj == null){
            resultData.setCode(ResponseCode.E404_100001.getCode());
            resultData.setMsg(ResponseCode.E404_100001.getMsg());
            return new ResponseEntity(resultData,HttpStatus.NOT_FOUND);
        }else{
            resultData.setData(obj);
            return new ResponseEntity(resultData, HttpStatus.OK);
        }
    }
    /**
     * 封装object
     * @param obj
     * @param resultData
     * @return
     */
    public ResponseEntity returnList(List obj, ResponseJsonRender resultData){
        if(obj == null || obj.isEmpty()){
            resultData.setCode(ResponseCode.E404_100001.getCode());
            resultData.setMsg(ResponseCode.E404_100001.getMsg());
            return new ResponseEntity(resultData,HttpStatus.NOT_FOUND);
        }else{
            resultData.setData(obj);
            return new ResponseEntity(resultData, HttpStatus.OK);
        }
    }
}
