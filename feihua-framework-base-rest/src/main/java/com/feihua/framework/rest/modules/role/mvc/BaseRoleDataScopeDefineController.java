package com.feihua.framework.rest.modules.role.mvc;

import com.feihua.exception.DataConflictException;
import com.feihua.framework.constants.DictEnum;
import com.feihua.framework.base.modules.role.api.ApiBaseRoleDataScopeDefinePoService;
import com.feihua.framework.base.modules.role.api.ApiBaseRoleDataScopeDefineSelfPoService;
import com.feihua.framework.base.modules.role.dto.BaseRoleDataScopeDefineDto;
import com.feihua.framework.base.modules.role.dto.RoleDataScopeParamDto;
import com.feihua.framework.log.comm.annotation.OperationLog;
import com.feihua.framework.rest.ResponseJsonRender;
import com.feihua.framework.rest.interceptor.RepeatFormValidator;
import com.feihua.framework.rest.modules.common.mvc.BaseController;
import com.feihua.framework.rest.modules.role.dto.RoleDataScopeDefineFormDto;
import com.feihua.utils.http.httpServletResponse.ResponseCode;
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
 * 角色数据范围定义接口
 * Created by yangwei
 * Created at 2018/3/16 15:23
 */
@RestController
@RequestMapping("/base")
public class BaseRoleDataScopeDefineController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(BaseRoleDataScopeDefineController.class);

    @Autowired
    private ApiBaseRoleDataScopeDefinePoService apiBaseRoleDataScopeDefinePoService;

    @Autowired
    private ApiBaseRoleDataScopeDefineSelfPoService apiBaseRoleDataScopeDefineSelfPoService;

    /**
     * 角色数据范围定义
     * @param dataScopeId  // 数据范围id
     * @param roleDataScopeDefineFormDto
     * @return
     */
    @OperationLog(operation = "角色数据范围定义接口",content = "角色数据范围定义")
    @RepeatFormValidator
    @RequiresPermissions("base:role:dataScope:define")
    @RequestMapping(value = "/dataScope/{dataScopeId}/role/define",method = RequestMethod.POST)
    public ResponseEntity roleDataScopedDefine(@PathVariable String dataScopeId, RoleDataScopeDefineFormDto roleDataScopeDefineFormDto){
        logger.info("设置角色数据范围定义开始");
        logger.info("当前登录用户id:{}",getLoginUser().getId());
        ResponseJsonRender resultData=new ResponseJsonRender();

        RoleDataScopeParamDto roleDataScopeParamDto = new RoleDataScopeParamDto();
        roleDataScopeParamDto.setCurrentUserId(getLoginUser().getId());
        roleDataScopeParamDto.setDataScopeId(dataScopeId);
        roleDataScopeParamDto.setType(roleDataScopeDefineFormDto.getType());
        roleDataScopeParamDto.setRoleIds(roleDataScopeDefineFormDto.getRoleIds());

        BaseRoleDataScopeDefineDto r = null;
        try {
            r = apiBaseRoleDataScopeDefinePoService.setRoleDataScope(roleDataScopeParamDto);
        }catch (DataConflictException e){
            resultData.setCode(e.getCode());
            resultData.setMsg(e.getMessage());
            logger.info("code:{},msg:{}",resultData.getCode(),resultData.getMsg());
            logger.info("设置角色数据范围定义结束，失败");
            return new ResponseEntity(resultData, HttpStatus.CONFLICT);
        }
        if (r == null) {
            // 添加失败
            resultData.setCode(ResponseCode.E404_100001.getCode());
            resultData.setMsg(ResponseCode.E404_100001.getMsg());
            logger.info("code:{},msg:{}",resultData.getCode(),resultData.getMsg());
            logger.info("设置角色数据范围定义结束，失败");
            return new ResponseEntity(resultData,HttpStatus.NOT_FOUND);
        }else{
            // 添加成功，返回添加的数据
            resultData.setData(r);
            logger.info("数据范围id:{}",dataScopeId);
            logger.info("设置角色数据范围定义id:{}",r.getId());
            logger.info("设置角色数据范围定义结束，成功");
            return new ResponseEntity(resultData, HttpStatus.CREATED);
        }
    }
    /**
     * 角色数据范围定义
     * @param dataScopeId  // 数据范围id
     * @return
     */
    @OperationLog(operation = "角色数据范围定义接口",content = "获取角色数据范围定义")
    @RequiresPermissions("base:role:dataScope:define:get")
    @RequestMapping(value = "/dataScope/{dataScopeId}/role/define",method = RequestMethod.GET)
    public ResponseEntity getRoleDataScopedDefine(@PathVariable String dataScopeId){

        ResponseJsonRender resultData=new ResponseJsonRender();
        BaseRoleDataScopeDefineDto r = apiBaseRoleDataScopeDefinePoService.selectByDataScopeId(dataScopeId);

        if(r != null){
            resultData.setData(r);
            // 如果是自定义，则查询自定义结果
            if(DictEnum.RoleDataScope.self.name().equals(r.getType())){
                resultData.addData("self",apiBaseRoleDataScopeDefineSelfPoService.selectByRoleDataScopeDefineId(r.getId()));
            }
            return new ResponseEntity(resultData, HttpStatus.OK);
        }else{
            resultData.setCode(ResponseCode.E404_100001.getCode());
            resultData.setMsg(ResponseCode.E404_100001.getMsg());
            return new ResponseEntity(resultData,HttpStatus.NOT_FOUND);
        }
    }
}
