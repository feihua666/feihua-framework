package com.feihua.framework.rest.modules.rel.mvc;


import com.feihua.exception.DataConflictException;
import com.feihua.framework.base.modules.rel.api.ApiBaseRoleDataScopeRelPoService;
import com.feihua.framework.base.modules.rel.dto.BaseRoleDataScopeRelDto;
import com.feihua.framework.base.modules.rel.dto.DataScopeBindRolesParamDto;
import com.feihua.framework.base.modules.rel.dto.RoleBindDataScopesParamDto;
import com.feihua.framework.log.comm.annotation.OperationLog;
import com.feihua.framework.rest.ResponseJsonRender;
import com.feihua.framework.rest.interceptor.RepeatFormValidator;
import com.feihua.framework.rest.modules.common.mvc.BaseController;
import com.feihua.framework.rest.modules.rel.dto.DataScopeBindRolesFormDto;
import com.feihua.framework.rest.modules.rel.dto.RoleBindDataScopesFormDto;
import com.feihua.utils.http.httpServletResponse.ResponseCode;
import org.apache.commons.collections.CollectionUtils;
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

import java.util.List;

/**
 * 角色与数据范围关系接口
 * Created by yangwei
 * Created at 2017/8/2 14:52
 */
@RestController
@RequestMapping("/base")
public class BaseRoleDataScopeRelController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(BaseRoleDataScopeRelController.class);

    @Autowired
    private ApiBaseRoleDataScopeRelPoService apiBaseRoleDataScopeRelPoService;
    /**
     * 角色绑定数据范围
     * @param roleId 角色id
     * @return
     */
    @OperationLog(operation = "角色与数据范围关系接口",content = "角色绑定数据范围")
    @RepeatFormValidator
    @RequiresPermissions("base:role:dataScopes:rel:bind")
    @RequestMapping(value = "/role/{roleId}/dataScopes/rel",method = RequestMethod.POST)
    public ResponseEntity roleBindDataScope(@PathVariable String roleId, RoleBindDataScopesFormDto roleBindDataScopesFormDto){
        logger.info("角色绑定数据范围开始");
        logger.info("当前登录用户id:{}",getLoginUser().getId());
        ResponseJsonRender resultData=new ResponseJsonRender();
        // 表单值设置
        RoleBindDataScopesParamDto roleBindDataScopesParamDto = new RoleBindDataScopesParamDto();
        roleBindDataScopesParamDto.setDataScopeIds(roleBindDataScopesFormDto.getDataScopeIds());
        roleBindDataScopesParamDto.setRoleId(roleId);
        roleBindDataScopesParamDto.setCurrentUserId(getLoginUser().getId());

        try {
            int r = apiBaseRoleDataScopeRelPoService.roleBindDataScopes(roleBindDataScopesParamDto);
        }catch (DataConflictException e){
            resultData.setCode(e.getCode());
            resultData.setMsg(e.getMessage());
            logger.info("绑定角色id:{}",roleId);
            logger.info("角色绑定数据范围结束，失败");
            return new ResponseEntity(resultData, HttpStatus.CONFLICT);
        }
        logger.info("绑定角色id:{}",roleId);
        logger.info("角色绑定数据范围结束，成功");
        return new ResponseEntity(resultData, HttpStatus.CREATED);
    }
    /**
     * 获取角色绑定的数据范围
     * @param roleId 角色id
     * @return
     */
    @OperationLog(operation = "角色与数据范围关系接口",content = "获取角色绑定的数据范围")
    @RequiresPermissions("base:role:dataScopes:rel:get")
    @RequestMapping(value = "/role/{roleId}/dataScopes/rel",method = RequestMethod.GET)
    public ResponseEntity roleDataScopes(@PathVariable String roleId){
        logger.info("获取角色绑定的数据范围开始");
        logger.info("当前登录用户id:{}",getLoginUser().getId());
        ResponseJsonRender resultData=new ResponseJsonRender();

        List<BaseRoleDataScopeRelDto> data = apiBaseRoleDataScopeRelPoService.selectByRoleId(roleId);
        if(CollectionUtils.isEmpty(data)){
            resultData.setCode(ResponseCode.E404_100001.getCode());
            resultData.setMsg(ResponseCode.E404_100001.getMsg());
            return new ResponseEntity(resultData, HttpStatus.NOT_FOUND);

        }
        resultData.setData(data);
        logger.info("绑定角色id:{}",roleId);
        logger.info("获取角色绑定的数据范围结束，成功");
        return new ResponseEntity(resultData, HttpStatus.OK);
    }
    /**
     * 数据范围绑定角色
     * @param dataScopeId 角色id
     * @param dataScopeBindRolesFormDto 要绑定的角色id
     * @return
     */
    @OperationLog(operation = "角色与数据范围关系接口",content = "数据范围绑定角色")
    @RepeatFormValidator
    @RequiresPermissions("base:dataScope:roles:rel:bind")
    @RequestMapping(value = "/dataScope/{dataScopeId}/roles/rel",method = RequestMethod.POST)
    public ResponseEntity dataScopeBindRoles(@PathVariable String dataScopeId, DataScopeBindRolesFormDto dataScopeBindRolesFormDto){
        logger.info("数据范围绑定角色开始");
        logger.info("当前登录用户id:{}",getLoginUser().getId());
        ResponseJsonRender resultData=new ResponseJsonRender();
        // 表单值设置
        DataScopeBindRolesParamDto dataScopeBindRolesParamDto = new DataScopeBindRolesParamDto();
        dataScopeBindRolesParamDto.setRoleIds(dataScopeBindRolesFormDto.getRoleIds());
        dataScopeBindRolesParamDto.setDataScopeId(dataScopeId);
        dataScopeBindRolesParamDto.setCurrentUserId(getLoginUser().getId());

        int r = apiBaseRoleDataScopeRelPoService.DataScopeBindRoles(dataScopeBindRolesParamDto);

        logger.info("绑定数据范围id:{}",dataScopeId);
        logger.info("数据范围绑定角色结束，成功");
        return new ResponseEntity(resultData, HttpStatus.CREATED);
    }
    /**
     * 获取数据范围绑定的角色
     * @param dataScopeId 数据范围id
     * @return
     */
    @OperationLog(operation = "角色与数据范围关系接口",content = "获取数据范围绑定的角色")
    @RequiresPermissions("base:dataScope:roles:rel:get")
    @RequestMapping(value = "/dataScope/{dataScopeId}/roles/rel",method = RequestMethod.GET)
    public ResponseEntity dataScopeRoles(@PathVariable String dataScopeId){
        logger.info("获取角色绑定用户开始");
        logger.info("当前登录用户id:{}",getLoginUser().getId());
        ResponseJsonRender resultData=new ResponseJsonRender();

        List<BaseRoleDataScopeRelDto> data = apiBaseRoleDataScopeRelPoService.selectByDataScopeId(dataScopeId);
        if(CollectionUtils.isEmpty(data)){
            resultData.setCode(ResponseCode.E404_100001.getCode());
            resultData.setMsg(ResponseCode.E404_100001.getMsg());
            return new ResponseEntity(resultData, HttpStatus.NOT_FOUND);

        }
        resultData.setData(data);
        logger.info("绑定角色id:{}",dataScopeId);
        logger.info("获取角色绑定用户结束，成功");
        return new ResponseEntity(resultData, HttpStatus.OK);
    }
}
