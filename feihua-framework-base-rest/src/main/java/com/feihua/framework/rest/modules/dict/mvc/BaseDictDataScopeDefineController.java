package com.feihua.framework.rest.modules.dict.mvc;

import com.feihua.exception.DataConflictException;
import com.feihua.framework.base.modules.dict.api.ApiBaseDictDataScopeDefinePoService;
import com.feihua.framework.base.modules.dict.dto.BaseDictDataScopeDefineDto;
import com.feihua.framework.base.modules.dict.dto.BaseDictDataScopeDefineParamDto;
import com.feihua.framework.base.modules.dict.po.BaseDictDataScopeDefinePo;
import com.feihua.framework.rest.ResponseJsonRender;
import com.feihua.framework.rest.interceptor.RepeatFormValidator;
import com.feihua.framework.rest.modules.common.mvc.BaseController;
import com.feihua.utils.http.httpServletResponse.ResponseCode;
import feihua.jdbc.api.pojo.BasePo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 字典数据范围接口
 * Created by yangwei
 * Created at 2018/3/16 15:23
 */
@RestController
@RequestMapping("/base")
public class BaseDictDataScopeDefineController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(BaseDictDataScopeDefineController.class);

    @Autowired
    private ApiBaseDictDataScopeDefinePoService apiBaseDictDataScopeDefinePoService;


    /**
     * 字典数据范围定义
     * @param dataScopeId  // 数据范围id
     * @param type         // 设置的数据范围类型
     * @return
     */
    @RepeatFormValidator
    @RequiresPermissions("base:dict:dataScope:define")
    @RequestMapping(value = "/dataScope/{dataScopeId}/dictDataScope/define",method = RequestMethod.POST)
    public ResponseEntity dictDataScopedDefine(@PathVariable String dataScopeId,String type){
        logger.info("设置字典数据范围定义开始");
        logger.info("当前登录字典id:{}",getLoginUser().getId());
        ResponseJsonRender resultData=new ResponseJsonRender();

        BaseDictDataScopeDefineParamDto baseDictDataScopeDefineParamDto = new BaseDictDataScopeDefineParamDto();
        baseDictDataScopeDefineParamDto.setCurrentUserId(getLoginUser().getId());
        baseDictDataScopeDefineParamDto.setDataScopeId(dataScopeId);
        baseDictDataScopeDefineParamDto.setType(type);

        BaseDictDataScopeDefineDto r = null;
        try {
            r = apiBaseDictDataScopeDefinePoService.setDictDataScopeDefine(baseDictDataScopeDefineParamDto);
        }catch (DataConflictException e){
            resultData.setCode(e.getCode());
            resultData.setMsg(e.getMessage());
            logger.info("code:{},msg:{}",resultData.getCode(),resultData.getMsg());
            logger.info("设置字典数据范围定义结束，失败");
            return new ResponseEntity(resultData, HttpStatus.CONFLICT);
        }
        if (r == null) {
            // 添加失败
            resultData.setCode(ResponseCode.E404_100001.getCode());
            resultData.setMsg(ResponseCode.E404_100001.getMsg());
            logger.info("code:{},msg:{}",resultData.getCode(),resultData.getMsg());
            logger.info("设置字典数据范围定义结束，失败");
            return new ResponseEntity(resultData,HttpStatus.NOT_FOUND);
        }else{
            // 添加成功，返回添加的数据
            resultData.setData(r);
            logger.info("字典数据范围id:{}",dataScopeId);
            logger.info("设置的字典数据范围定义id:{}",r.getId());
            logger.info("设置字典数据范围定义结束，成功");
            return new ResponseEntity(resultData, HttpStatus.CREATED);
        }
    }
    /**
     * 字典数据范围定义
     * @param dataScopeId  // 数据范围id
     * @return
     */
    @RepeatFormValidator
    @RequiresPermissions("base:dict:dataScope:define:get")
    @RequestMapping(value = "/dataScope/{dataScopeId}/dictDataScope/define",method = RequestMethod.GET)
    public ResponseEntity getDictDataScopedDefine(@PathVariable String dataScopeId){

        ResponseJsonRender resultData=new ResponseJsonRender();

        BaseDictDataScopeDefinePo dictDataScopeDefinePo = new BaseDictDataScopeDefinePo();
        dictDataScopeDefinePo.setDataScopeId(dataScopeId);
        dictDataScopeDefinePo.setDelFlag(BasePo.YesNo.N.name());

        BaseDictDataScopeDefineDto r = apiBaseDictDataScopeDefinePoService.selectOne(dictDataScopeDefinePo);

        if(r != null){
            resultData.setData(r);
            return new ResponseEntity(resultData, HttpStatus.OK);
        }else{
            resultData.setCode(ResponseCode.E404_100001.getCode());
            resultData.setMsg(ResponseCode.E404_100001.getMsg());
            return new ResponseEntity(resultData,HttpStatus.NOT_FOUND);
        }
    }
}
