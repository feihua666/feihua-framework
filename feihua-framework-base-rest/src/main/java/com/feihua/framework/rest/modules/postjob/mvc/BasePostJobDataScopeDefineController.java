package com.feihua.framework.rest.modules.postjob.mvc;

import com.feihua.exception.DataConflictException;
import com.feihua.framework.base.modules.postjob.api.ApiBasePostJobDataScopeDefinePoService;
import com.feihua.framework.base.modules.postjob.dto.BasePostJobDataScopeDefineDto;
import com.feihua.framework.base.modules.postjob.dto.BasePostJobDataScopeDefineParamDto;
import com.feihua.framework.base.modules.postjob.po.BasePostJobDataScopeDefinePo;
import com.feihua.framework.log.comm.annotation.OperationLog;
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
 * 职务数据范围接口
 * Created by yangwei
 * Created at 2018/3/16 15:23
 */
@RestController
@RequestMapping("/base")
public class BasePostJobDataScopeDefineController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(BasePostJobDataScopeDefineController.class);

    @Autowired
    private ApiBasePostJobDataScopeDefinePoService apiBasePostJobDataScopeDefinePoService;


    /**
     * 职务数据范围定义
     * @param dataScopeId  // 数据范围id
     * @param type         // 设置的数据范围类型
     * @return
     */
    @OperationLog(operation = "职务数据范围接口", content = "职务数据范围定义")
    @RepeatFormValidator
    @RequiresPermissions("base:postJob:dataScope:define")
    @RequestMapping(value = "/dataScope/{dataScopeId}/postJobDataScope/define",method = RequestMethod.POST)
    public ResponseEntity postJobDataScopedDefine(@PathVariable String dataScopeId,String type){
        logger.info("设置职务数据范围定义开始");
        logger.info("当前登录职务id:{}",getLoginUser().getId());
        ResponseJsonRender resultData=new ResponseJsonRender();

        BasePostJobDataScopeDefineParamDto basePostJobDataScopeDefineParamDto = new BasePostJobDataScopeDefineParamDto();
        basePostJobDataScopeDefineParamDto.setCurrentUserId(getLoginUser().getId());
        basePostJobDataScopeDefineParamDto.setDataScopeId(dataScopeId);
        basePostJobDataScopeDefineParamDto.setType(type);

        BasePostJobDataScopeDefineDto r = null;
        try {
            r = apiBasePostJobDataScopeDefinePoService.setPostJobDataScopeDefine(basePostJobDataScopeDefineParamDto);
        }catch (DataConflictException e){
            resultData.setCode(e.getCode());
            resultData.setMsg(e.getMessage());
            logger.info("code:{},msg:{}",resultData.getCode(),resultData.getMsg());
            logger.info("设置职务数据范围定义结束，失败");
            return new ResponseEntity(resultData, HttpStatus.CONFLICT);
        }
        if (r == null) {
            // 添加失败
            resultData.setCode(ResponseCode.E404_100001.getCode());
            resultData.setMsg(ResponseCode.E404_100001.getMsg());
            logger.info("code:{},msg:{}",resultData.getCode(),resultData.getMsg());
            logger.info("设置职务数据范围定义结束，失败");
            return new ResponseEntity(resultData,HttpStatus.NOT_FOUND);
        }else{
            // 添加成功，返回添加的数据
            resultData.setData(r);
            logger.info("职务数据范围id:{}",dataScopeId);
            logger.info("设置的职务数据范围定义id:{}",r.getId());
            logger.info("设置职务数据范围定义结束，成功");
            return new ResponseEntity(resultData, HttpStatus.CREATED);
        }
    }
    /**
     * 职务数据范围定义
     * @param dataScopeId  // 数据范围id
     * @return
     */
    @OperationLog(operation = "职务数据范围接口", content = "职务数据范围定义")
    @RequiresPermissions("base:postJob:dataScope:define:get")
    @RequestMapping(value = "/dataScope/{dataScopeId}/postJobDataScope/define",method = RequestMethod.GET)
    public ResponseEntity getPostJobDataScopedDefine(@PathVariable String dataScopeId){

        ResponseJsonRender resultData=new ResponseJsonRender();

        BasePostJobDataScopeDefinePo postJobDataScopeDefinePo = new BasePostJobDataScopeDefinePo();
        postJobDataScopeDefinePo.setDataScopeId(dataScopeId);
        postJobDataScopeDefinePo.setDelFlag(BasePo.YesNo.N.name());

        BasePostJobDataScopeDefineDto r = apiBasePostJobDataScopeDefinePoService.selectOne(postJobDataScopeDefinePo);

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
