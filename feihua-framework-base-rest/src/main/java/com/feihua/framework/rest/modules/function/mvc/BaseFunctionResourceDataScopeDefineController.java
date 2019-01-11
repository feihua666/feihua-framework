package com.feihua.framework.rest.modules.function.mvc;

import com.feihua.framework.constants.DictEnum;
import com.feihua.framework.base.modules.function.api.ApiBaseFunctionResourceDataScopeDefinePoService;
import com.feihua.framework.base.modules.function.api.ApiBaseFunctionResourceDataScopeDefineSelfPoService;
import com.feihua.framework.base.modules.function.dto.BaseFunctionResourceDataScopeDefineDto;
import com.feihua.framework.base.modules.function.dto.FunctionResourceDataScopeParamDto;
import com.feihua.framework.base.modules.user.api.ApiBaseUserDataScopeDefinePoService;
import com.feihua.framework.base.modules.user.dto.BaseUserDataScopeDefineDto;
import com.feihua.framework.base.modules.user.dto.BaseUserDataScopeDefineParamDto;
import com.feihua.framework.base.modules.user.po.BaseUserDataScopeDefinePo;
import com.feihua.framework.log.comm.annotation.OperationLog;
import com.feihua.utils.http.httpServletResponse.ResponseCode;
import com.feihua.framework.rest.ResponseJsonRender;
import com.feihua.framework.rest.interceptor.RepeatFormValidator;
import com.feihua.framework.rest.modules.common.mvc.BaseController;
import com.feihua.framework.rest.modules.function.dto.FunctionResourceDataScopeDefineFormDto;
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

import java.util.HashMap;
import java.util.Map;

/**
 * 功能资源数据范围接口
 * Created by yangwei
 * Created at 2018/3/16 15:23
 */
@RestController
@RequestMapping("/base")
public class BaseFunctionResourceDataScopeDefineController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(BaseFunctionResourceDataScopeDefineController.class);

    @Autowired
    private ApiBaseFunctionResourceDataScopeDefinePoService apiBaseFunctionResourceDataScopeDefinePoService;

    @Autowired
    private ApiBaseFunctionResourceDataScopeDefineSelfPoService apiBaseFunctionResourceDataScopeDefineSelfPoService;

    /**
     * 功能资源数据范围定义
     * @param roleId  // 角色id
     * @param functionResourceDataScopeDefineFormDto
     * @return
     */
    @OperationLog(operation = "功能资源数据范围接口",content = "功能资源数据范围定义")
    @RepeatFormValidator
    @RequiresPermissions("base:functionResource:dataScope:define")
    @RequestMapping(value = "/role/{roleId}/functionResource/define",method = RequestMethod.POST)
    public ResponseEntity functionResourceDataScopedDefine(@PathVariable String roleId, FunctionResourceDataScopeDefineFormDto functionResourceDataScopeDefineFormDto){
        logger.info("设置功能资源数据范围定义开始");
        logger.info("当前登录用户id:{}",getLoginUser().getId());
        ResponseJsonRender resultData=new ResponseJsonRender();

        FunctionResourceDataScopeParamDto functionResourceDataScopeParamDto = new FunctionResourceDataScopeParamDto();
        functionResourceDataScopeParamDto.setCurrentUserId(getLoginUser().getId());
        functionResourceDataScopeParamDto.setRoleId(roleId);
        functionResourceDataScopeParamDto.setType(functionResourceDataScopeDefineFormDto.getType());
        functionResourceDataScopeParamDto.setFunctionResourceIds(functionResourceDataScopeDefineFormDto.getFunctionResourceIds());

        BaseFunctionResourceDataScopeDefineDto r = apiBaseFunctionResourceDataScopeDefinePoService.setFunctionResourceDataScope(functionResourceDataScopeParamDto);
        if (r == null) {
            // 添加失败
            resultData.setCode(ResponseCode.E404_100001.getCode());
            resultData.setMsg(ResponseCode.E404_100001.getMsg());
            logger.info("code:{},msg:{}",resultData.getCode(),resultData.getMsg());
            logger.info("设置功能资源数据范围定义结束，失败");
            return new ResponseEntity(resultData,HttpStatus.NOT_FOUND);
        }else{
            // 添加成功，返回添加的数据
            resultData.setData(r);
            logger.info("角色id:{}",roleId);
            logger.info("设置功能资源数据范围定义id:{}",r.getId());
            logger.info("设置功能资源数据范围定义结束，成功");
            return new ResponseEntity(resultData, HttpStatus.CREATED);
        }
    }
    /**
     * 功能资源数据范围定义
     * @param roleId  // 数据范围id
     * @return
     */
    @OperationLog(operation = "功能资源数据范围接口",content = "功能资源数据范围定义")
    @RepeatFormValidator
    @RequiresPermissions("base:functionResource:dataScope:define:get")
    @RequestMapping(value = "/role/{roleId}/functionResource/define",method = RequestMethod.GET)
    public ResponseEntity getFunctionResourceDataScopedDefine(@PathVariable String roleId){

        ResponseJsonRender resultData=new ResponseJsonRender();
        BaseFunctionResourceDataScopeDefineDto r = apiBaseFunctionResourceDataScopeDefinePoService.selectByRoleId(roleId);

        if(r != null){
            resultData.setData(r);
            // 如果是自定义，则查询自定义结果
            if(DictEnum.FunctionResourceDataScope.self.name().equals(r.getType())){
                resultData.addData("self",apiBaseFunctionResourceDataScopeDefineSelfPoService.selectByFunctionResourceDataScopeDefineId(r.getId()));
            }
            return new ResponseEntity(resultData, HttpStatus.OK);
        }else{
            resultData.setCode(ResponseCode.E404_100001.getCode());
            resultData.setMsg(ResponseCode.E404_100001.getMsg());
            return new ResponseEntity(resultData,HttpStatus.NOT_FOUND);
        }
    }
}
