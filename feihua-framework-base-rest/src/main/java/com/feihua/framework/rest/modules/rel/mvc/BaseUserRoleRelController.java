package com.feihua.framework.rest.modules.rel.mvc;


import com.feihua.framework.base.modules.rel.api.ApiBaseUserRoleRelPoService;
import com.feihua.framework.base.modules.rel.dto.BaseUserRoleRelDto;
import com.feihua.framework.base.modules.rel.dto.RoleBindUsersParamDto;
import com.feihua.framework.base.modules.rel.dto.UserBindRolesParamDto;
import com.feihua.framework.log.comm.annotation.OperationLog;
import com.feihua.utils.http.httpServletResponse.ResponseCode;
import com.feihua.framework.rest.ResponseJsonRender;
import com.feihua.framework.rest.interceptor.RepeatFormValidator;
import com.feihua.framework.rest.modules.common.mvc.BaseController;
import com.feihua.framework.rest.modules.rel.dto.RoleBindUsersFormDto;
import com.feihua.framework.rest.modules.rel.dto.UserBindRolesFormDto;
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
 * 用户与角色关系接口
 * Created by yangwei
 * Created at 2017/8/2 14:52
 */
@RestController
@RequestMapping("/base")
public class BaseUserRoleRelController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(BaseUserRoleRelController.class);

    @Autowired
    private ApiBaseUserRoleRelPoService apiBaseUserRoleRelPoService;
    /**
     * 用户绑定角色
     * @param userId 用户id
     * @return
     */
    @OperationLog(operation = "用户与角色关系接口",content = "用户绑定角色")
    @RepeatFormValidator
    @RequiresPermissions("base:user:roles:rel:bind")
    @RequestMapping(value = "/user/{userId}/roles/rel",method = RequestMethod.POST)
    public ResponseEntity userBindRoles(@PathVariable String userId, UserBindRolesFormDto userBindRolesFormDto){
        logger.info("用户绑定角色开始");
        logger.info("当前登录用户id:{}",getLoginUser().getId());
        ResponseJsonRender resultData=new ResponseJsonRender();
        // 表单值设置
        UserBindRolesParamDto userBindRolesParamDto = new UserBindRolesParamDto();
        userBindRolesParamDto.setRoleIds(userBindRolesFormDto.getRoleIds());
        userBindRolesParamDto.setUserId(userId);
        userBindRolesParamDto.setCurrentUserId(getLoginUser().getId());

        int r = apiBaseUserRoleRelPoService.userBindRoles(userBindRolesParamDto);

        logger.info("绑定用户id:{}",userId);
        logger.info("用户绑定角色结束，成功");
        return new ResponseEntity(resultData, HttpStatus.CREATED);
    }
    /**
     * 获取用户角色
     * @param userId 用户id
     * @return
     */
    @OperationLog(operation = "用户与角色关系接口",content = "获取用户角色")
    @RepeatFormValidator
    @RequiresPermissions("base:user:roles:rel:get")
    @RequestMapping(value = "/user/{userId}/roles/rel",method = RequestMethod.GET)
    public ResponseEntity userRoles(@PathVariable String userId){
        logger.info("获取用户绑定角色开始");
        logger.info("当前登录用户id:{}",getLoginUser().getId());
        ResponseJsonRender resultData=new ResponseJsonRender();

        List<BaseUserRoleRelDto> data = apiBaseUserRoleRelPoService.selectByUserId(userId);
        if(CollectionUtils.isEmpty(data)){
            resultData.setCode(ResponseCode.E404_100001.getCode());
            resultData.setMsg(ResponseCode.E404_100001.getMsg());
            return new ResponseEntity(resultData, HttpStatus.NOT_FOUND);

        }
        resultData.setData(data);
        logger.info("绑定用户id:{}",userId);
        logger.info("获取用户绑定角色结束，成功");
        return new ResponseEntity(resultData, HttpStatus.OK);
    }
    /**
     * 角色绑定用户
     * @param roleId 角色id
     * @param roleBindUsersFormDto 要绑定的用户id
     * @return
     */
    @OperationLog(operation = "用户与角色关系接口",content = "角色绑定用户")
    @RepeatFormValidator
    @RequiresPermissions("base:role:users:rel:bind")
    @RequestMapping(value = "/role/{roleId}/users/rel",method = RequestMethod.POST)
    public ResponseEntity roleBindUsers(@PathVariable String roleId, RoleBindUsersFormDto roleBindUsersFormDto){
        logger.info("角色绑定用户开始");
        logger.info("当前登录用户id:{}",getLoginUser().getId());
        ResponseJsonRender resultData=new ResponseJsonRender();
        // 表单值设置
        RoleBindUsersParamDto roleBindUsersParamDto = new RoleBindUsersParamDto();
        roleBindUsersParamDto.setUserIds(roleBindUsersFormDto.getUserIds());
        roleBindUsersParamDto.setRoleId(roleId);
        roleBindUsersParamDto.setCurrentUserId(getLoginUser().getId());

        int r = apiBaseUserRoleRelPoService.roleBindUsers(roleBindUsersParamDto);

        logger.info("绑定角色id:{}",roleId);
        logger.info("角色绑定用户结束，成功");
        return new ResponseEntity(resultData, HttpStatus.CREATED);
    }
    /**
     * 获取角色绑定用户
     * @param roleId 角色id
     * @return
     */
    @OperationLog(operation = "用户与角色关系接口",content = "获取角色绑定用户")
    @RepeatFormValidator
    @RequiresPermissions("base:role:users:rel:get")
    @RequestMapping(value = "/role/{roleId}/users/rel",method = RequestMethod.GET)
    public ResponseEntity roleUsers(@PathVariable String roleId){
        logger.info("获取角色绑定用户开始");
        logger.info("当前登录用户id:{}",getLoginUser().getId());
        ResponseJsonRender resultData=new ResponseJsonRender();

        List<BaseUserRoleRelDto> data = apiBaseUserRoleRelPoService.selectByRoleId(roleId);
        if(CollectionUtils.isEmpty(data)){
            resultData.setCode(ResponseCode.E404_100001.getCode());
            resultData.setMsg(ResponseCode.E404_100001.getMsg());
            return new ResponseEntity(resultData, HttpStatus.NOT_FOUND);

        }
        resultData.setData(data);
        logger.info("绑定角色id:{}",roleId);
        logger.info("获取角色绑定用户结束，成功");
        return new ResponseEntity(resultData, HttpStatus.OK);
    }
}
