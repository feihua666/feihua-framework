package com.feihua.framework.rest.modules.rel.mvc;


import com.feihua.exception.DataConflictException;
import com.feihua.framework.base.modules.rel.api.ApiBaseUserDataScopeRelPoService;
import com.feihua.framework.base.modules.rel.dto.BaseUserDataScopeRelDto;
import com.feihua.framework.base.modules.rel.dto.DataScopeBindUsersParamDto;
import com.feihua.framework.base.modules.rel.dto.UserBindDataScopesParamDto;
import com.feihua.framework.log.comm.annotation.OperationLog;
import com.feihua.framework.rest.ResponseJsonRender;
import com.feihua.framework.rest.interceptor.RepeatFormValidator;
import com.feihua.framework.rest.modules.common.mvc.BaseController;
import com.feihua.framework.rest.modules.rel.dto.DataScopeBindUsersFormDto;
import com.feihua.framework.rest.modules.rel.dto.UserBindDataScopesFormDto;
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
 * 用户与数据范围关系接口
 * Created by yangwei
 * Created at 2017/8/2 14:52
 */
@RestController
@RequestMapping("/base")
public class BaseUserDataScopeRelController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(BaseUserDataScopeRelController.class);

    @Autowired
    private ApiBaseUserDataScopeRelPoService apiBaseUserDataScopeRelPoService;

    /**
     * 用户绑定数据范围
     * @param userId 用户id
     * @return
     */
    @OperationLog(operation = "用户与数据范围关系接口",content = "用户绑定数据范围")
    @RequiresPermissions("base:user:dataScopes:rel:bind")
    @RequestMapping(value = "/user/{userId}/dataScopes/rel",method = RequestMethod.POST)
    public ResponseEntity userBindDataScope(@PathVariable String userId, UserBindDataScopesFormDto userBindDataScopesFormDto){
        logger.info("用户绑定数据范围开始");
        logger.info("当前登录用户id:{}",getLoginUser().getId());
        ResponseJsonRender resultData=new ResponseJsonRender();
        // 表单值设置
        UserBindDataScopesParamDto userBindDataScopesParamDto = new UserBindDataScopesParamDto();
        userBindDataScopesParamDto.setDataScopeIds(userBindDataScopesFormDto.getDataScopeIds());
        userBindDataScopesParamDto.setUserId(userId);
        userBindDataScopesParamDto.setCurrentUserId(getLoginUser().getId());

        try {
            int r = apiBaseUserDataScopeRelPoService.userBindDataScopes(userBindDataScopesParamDto);
        }catch (DataConflictException e){
            resultData.setCode(e.getCode());
            resultData.setMsg(e.getMessage());
            logger.info("绑定用户id:{}", userId);
            logger.info("用户绑定数据范围结束，失败");
            return new ResponseEntity(resultData, HttpStatus.CONFLICT);
        }

        logger.info("绑定用户id:{}", userId);
        logger.info("用户绑定数据范围结束，成功");
        return new ResponseEntity(resultData, HttpStatus.CREATED);
    }
    /**
     * 获取用户绑定的数据范围
     * @param userId 用户id
     * @return
     */
    @OperationLog(operation = "用户与数据范围关系接口",content = "获取用户绑定的数据范围")
    @RequiresPermissions("base:user:dataScopes:rel:get")
    @RequestMapping(value = "/user/{userId}/dataScopes/rel",method = RequestMethod.GET)
    public ResponseEntity userDataScopes(@PathVariable String userId){
        logger.info("获取用户绑定的数据范围开始");
        logger.info("当前登录用户id:{}",getLoginUser().getId());
        ResponseJsonRender resultData=new ResponseJsonRender();

        BaseUserDataScopeRelDto data = apiBaseUserDataScopeRelPoService.selectByUserId(userId);
        if(data == null){
            resultData.setCode(ResponseCode.E404_100001.getCode());
            resultData.setMsg(ResponseCode.E404_100001.getMsg());
            return new ResponseEntity(resultData, HttpStatus.NOT_FOUND);

        }
        resultData.setData(data);
        logger.info("绑定用户id:{}", userId);
        logger.info("获取用户绑定的数据范围结束，成功");
        return new ResponseEntity(resultData, HttpStatus.OK);
    }
    /**
     * 数据范围绑定用户
     * @param dataScopeId 用户id
     * @param dataScopeBindUsersFormDto 要绑定的用户id
     * @return
     */
    @OperationLog(operation = "用户与数据范围关系接口",content = "数据范围绑定用户")
    @RepeatFormValidator
    @RequiresPermissions("base:dataScope:users:rel:bind")
    @RequestMapping(value = "/dataScope/{dataScopeId}/users/rel",method = RequestMethod.POST)
    public ResponseEntity dataScopeBindUsers(@PathVariable String dataScopeId, DataScopeBindUsersFormDto dataScopeBindUsersFormDto){
        logger.info("数据范围绑定用户开始");
        logger.info("当前登录用户id:{}",getLoginUser().getId());
        ResponseJsonRender resultData=new ResponseJsonRender();
        // 表单值设置
        DataScopeBindUsersParamDto dataScopeBindUsersParamDto = new DataScopeBindUsersParamDto();
        dataScopeBindUsersParamDto.setUserIds(dataScopeBindUsersFormDto.getUserIds());
        dataScopeBindUsersParamDto.setDataScopeId(dataScopeId);
        dataScopeBindUsersParamDto.setCurrentUserId(getLoginUser().getId());

        int r = apiBaseUserDataScopeRelPoService.DataScopeBindUsers(dataScopeBindUsersParamDto);

        logger.info("绑定数据范围id:{}",dataScopeId);
        logger.info("数据范围绑定用户结束，成功");
        return new ResponseEntity(resultData, HttpStatus.CREATED);
    }
    /**
     * 获取数据范围绑定的用户
     * @param dataScopeId 数据范围id
     * @return
     */
    @OperationLog(operation = "用户与数据范围关系接口",content = "获取数据范围绑定的用户")
    @RequiresPermissions("base:dataScope:users:rel:get")
    @RequestMapping(value = "/dataScope/{dataScopeId}/users/rel",method = RequestMethod.GET)
    public ResponseEntity dataScopeUsers(@PathVariable String dataScopeId){
        logger.info("获取用户绑定用户开始");
        logger.info("当前登录用户id:{}",getLoginUser().getId());
        ResponseJsonRender resultData=new ResponseJsonRender();

        List<BaseUserDataScopeRelDto> data = apiBaseUserDataScopeRelPoService.selectByDataScopeId(dataScopeId);
        if(CollectionUtils.isEmpty(data)){
            resultData.setCode(ResponseCode.E404_100001.getCode());
            resultData.setMsg(ResponseCode.E404_100001.getMsg());
            return new ResponseEntity(resultData, HttpStatus.NOT_FOUND);

        }
        resultData.setData(data);
        logger.info("绑定用户id:{}",dataScopeId);
        logger.info("获取用户绑定用户结束，成功");
        return new ResponseEntity(resultData, HttpStatus.OK);
    }
}
