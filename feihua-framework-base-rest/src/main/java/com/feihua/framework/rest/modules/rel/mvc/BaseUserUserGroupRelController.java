package com.feihua.framework.rest.modules.rel.mvc;


import com.feihua.framework.base.modules.rel.api.ApiBaseUserUserGroupRelPoService;
import com.feihua.framework.base.modules.rel.dto.BaseUserUserGroupRelDto;
import com.feihua.framework.base.modules.rel.dto.UserGroupBindUsersParamDto;
import com.feihua.framework.base.modules.rel.dto.UserBindUserGroupsParamDto;
import com.feihua.framework.log.comm.annotation.OperationLog;
import com.feihua.framework.rest.ResponseJsonRender;
import com.feihua.framework.rest.interceptor.RepeatFormValidator;
import com.feihua.framework.rest.modules.common.mvc.BaseController;
import com.feihua.framework.rest.modules.rel.dto.UserGroupBindUsersFormDto;
import com.feihua.framework.rest.modules.rel.dto.UserBindUserGroupsFormDto;
import com.feihua.framework.shiro.utils.ShiroUtils;
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
 * 用户与用户组关系接口
 * Created by yangwei
 * Created at 2017/8/2 14:52
 */
@RestController
@RequestMapping("/base")
public class BaseUserUserGroupRelController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(BaseUserUserGroupRelController.class);

    @Autowired
    private ApiBaseUserUserGroupRelPoService apiBaseUserUserGroupRelPoService;
    /**
     * 用户绑定用户分组
     * @param userId 用户id
     * @return
     */
    @OperationLog(operation = "用户与用户分组关系接口",content = "用户绑定用户分组")
    @RepeatFormValidator
    @RequiresPermissions("base:user:userGroups:rel:bind")
    @RequestMapping(value = "/user/{userId}/userGroups/rel",method = RequestMethod.POST)
    public ResponseEntity userBindUserGroups(@PathVariable String userId, UserBindUserGroupsFormDto userBindUserGroupsFormDto){
        logger.info("用户绑定用户分组开始");
        logger.info("当前登录用户id:{}",getLoginUser().getId());
        ResponseJsonRender resultData=new ResponseJsonRender();
        // 表单值设置
        UserBindUserGroupsParamDto userBindUserGroupsParamDto = new UserBindUserGroupsParamDto();
        userBindUserGroupsParamDto.setUserGroupIds(userBindUserGroupsFormDto.getUserGroupIds());
        userBindUserGroupsParamDto.setUserId(userId);
        userBindUserGroupsParamDto.setCurrentUserId(getLoginUser().getId());

        int r = apiBaseUserUserGroupRelPoService.userBindUserGroups(userBindUserGroupsParamDto);
        //用户分组设置完成刷新用户信息和权限信息
        ShiroUtils.refreshShiroUserInfoByUserId(userId);
        ShiroUtils.refreshAuthorizationInfoByUserId(userId);

        logger.info("绑定用户id:{}",userId);
        logger.info("用户绑定用户分组结束，成功");
        return new ResponseEntity(resultData, HttpStatus.CREATED);
    }
    /**
     * 获取用户用户分组
     * @param userId 用户id
     * @return
     */
    @OperationLog(operation = "用户与用户分组关系接口",content = "获取用户用户分组")
    @RequiresPermissions("base:user:userGroups:rel:get")
    @RequestMapping(value = "/user/{userId}/userGroups/rel",method = RequestMethod.GET)
    public ResponseEntity userUserGroups(@PathVariable String userId){
        logger.info("获取用户绑定用户分组开始");
        logger.info("当前登录用户id:{}",getLoginUser().getId());
        ResponseJsonRender resultData=new ResponseJsonRender();

        List<BaseUserUserGroupRelDto> data = apiBaseUserUserGroupRelPoService.selectByUserId(userId);
        if(CollectionUtils.isEmpty(data)){
            resultData.setCode(ResponseCode.E404_100001.getCode());
            resultData.setMsg(ResponseCode.E404_100001.getMsg());
            return new ResponseEntity(resultData, HttpStatus.NOT_FOUND);

        }
        resultData.setData(data);
        logger.info("绑定用户id:{}",userId);
        logger.info("获取用户绑定用户分组结束，成功");
        return new ResponseEntity(resultData, HttpStatus.OK);
    }
    /**
     * 用户分组绑定用户
     * @param userGroupId 用户分组id
     * @param userGroupBindUsersFormDto 要绑定的用户id
     * @return
     */
    @OperationLog(operation = "用户与用户分组关系接口",content = "用户分组绑定用户")
    @RepeatFormValidator
    @RequiresPermissions("base:userGroup:users:rel:bind")
    @RequestMapping(value = "/userGroup/{userGroupId}/users/rel",method = RequestMethod.POST)
    public ResponseEntity userGroupBindUsers(@PathVariable String userGroupId, UserGroupBindUsersFormDto userGroupBindUsersFormDto){
        logger.info("用户分组绑定用户开始");
        logger.info("当前登录用户id:{}",getLoginUser().getId());
        ResponseJsonRender resultData=new ResponseJsonRender();
        // 表单值设置
        UserGroupBindUsersParamDto userGroupBindUsersParamDto = new UserGroupBindUsersParamDto();
        userGroupBindUsersParamDto.setUserIds(userGroupBindUsersFormDto.getUserIds());
        userGroupBindUsersParamDto.setUserGroupId(userGroupId);
        userGroupBindUsersParamDto.setCurrentUserId(getLoginUser().getId());

        int r = apiBaseUserUserGroupRelPoService.userGroupBindUsers(userGroupBindUsersParamDto);

        logger.info("绑定用户分组id:{}",userGroupId);
        logger.info("用户分组绑定用户结束，成功");
        return new ResponseEntity(resultData, HttpStatus.CREATED);
    }
    /**
     * 获取用户分组绑定用户
     * @param userGroupId 用户分组id
     * @return
     */
    @OperationLog(operation = "用户与用户分组关系接口",content = "获取用户分组绑定用户")
    @RequiresPermissions("base:userGroup:users:rel:get")
    @RequestMapping(value = "/userGroup/{userGroupId}/users/rel",method = RequestMethod.GET)
    public ResponseEntity userGroupUsers(@PathVariable String userGroupId){
        logger.info("获取用户分组绑定用户开始");
        logger.info("当前登录用户id:{}",getLoginUser().getId());
        ResponseJsonRender resultData=new ResponseJsonRender();

        List<BaseUserUserGroupRelDto> data = apiBaseUserUserGroupRelPoService.selectByUserGroupId(userGroupId);
        if(CollectionUtils.isEmpty(data)){
            resultData.setCode(ResponseCode.E404_100001.getCode());
            resultData.setMsg(ResponseCode.E404_100001.getMsg());
            return new ResponseEntity(resultData, HttpStatus.NOT_FOUND);

        }
        resultData.setData(data);
        logger.info("绑定用户分组id:{}",userGroupId);
        logger.info("获取用户分组绑定用户结束，成功");
        return new ResponseEntity(resultData, HttpStatus.OK);
    }
}
