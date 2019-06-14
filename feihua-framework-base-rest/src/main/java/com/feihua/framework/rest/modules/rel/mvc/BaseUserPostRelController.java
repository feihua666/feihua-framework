package com.feihua.framework.rest.modules.rel.mvc;

import com.feihua.framework.base.modules.rel.api.ApiBaseUserPostRelPoService;
import com.feihua.framework.base.modules.rel.dto.BaseUserPostRelDto;
import com.feihua.framework.base.modules.rel.dto.PostBindUsersParamDto;
import com.feihua.framework.base.modules.rel.dto.UserBindPostsParamDto;
import com.feihua.framework.log.comm.annotation.OperationLog;
import com.feihua.framework.rest.ResponseJsonRender;
import com.feihua.framework.rest.interceptor.RepeatFormValidator;
import com.feihua.framework.rest.modules.rel.dto.PostBindUsersFormDto;
import com.feihua.framework.rest.modules.rel.dto.UserBindPostsFormDto;
import com.feihua.framework.rest.mvc.SuperController;
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
 * 用户与岗位关系接口
 * Created by yangwei
 */
@RestController
@RequestMapping("/base")
public class BaseUserPostRelController extends SuperController {

    private static Logger logger = LoggerFactory.getLogger(BaseUserPostRelController.class);

    @Autowired
    private ApiBaseUserPostRelPoService apiBaseUserPostRelPoService;
    /**
     * 用户绑定岗位
     * @param userId 用户id
     * @return
     */
    @OperationLog(operation = "用户与岗位关系接口",content = "用户绑定岗位")
    @RepeatFormValidator
    @RequiresPermissions("base:user:posts:rel:bind")
    @RequestMapping(value = "/user/{userId}/posts/rel",method = RequestMethod.POST)
    public ResponseEntity userBindPosts(@PathVariable String userId, UserBindPostsFormDto userBindPostsFormDto){
        logger.info("用户绑定岗位开始");
        logger.info("当前登录用户id:{}",getLoginUser().getId());
        ResponseJsonRender resultData=new ResponseJsonRender();
        // 表单值设置
        UserBindPostsParamDto userBindPostsParamDto = new UserBindPostsParamDto();
        userBindPostsParamDto.setPostIds(userBindPostsFormDto.getPostIds());
        userBindPostsParamDto.setUserId(userId);
        userBindPostsParamDto.setCurrentUserId(getLoginUser().getId());

        int r = apiBaseUserPostRelPoService.userBindPosts(userBindPostsParamDto);

        logger.info("绑定用户id:{}",userId);
        logger.info("用户绑定岗位结束，成功");
        return new ResponseEntity(resultData, HttpStatus.CREATED);
    }
    /**
     * 获取用户岗位
     * @param userId 用户id
     * @return
     */
    @OperationLog(operation = "用户与岗位关系接口",content = "获取用户岗位")
    @RequiresPermissions("base:user:posts:rel:get")
    @RequestMapping(value = "/user/{userId}/posts/rel",method = RequestMethod.GET)
    public ResponseEntity userPosts(@PathVariable String userId){
        logger.info("获取用户绑定岗位开始");
        logger.info("当前登录用户id:{}",getLoginUser().getId());
        ResponseJsonRender resultData=new ResponseJsonRender();

        List<BaseUserPostRelDto> data = apiBaseUserPostRelPoService.selectByUserId(userId);
        if(CollectionUtils.isEmpty(data)){
            resultData.setCode(ResponseCode.E404_100001.getCode());
            resultData.setMsg(ResponseCode.E404_100001.getMsg());
            return new ResponseEntity(resultData, HttpStatus.NOT_FOUND);

        }
        resultData.setData(data);
        logger.info("绑定用户id:{}",userId);
        logger.info("获取用户绑定岗位结束，成功");
        return new ResponseEntity(resultData, HttpStatus.OK);
    }
    /**
     * 岗位绑定用户
     * @param postId 岗位id
     * @param postBindUsersFormDto 要绑定的用户id
     * @return
     */
    @OperationLog(operation = "用户与岗位关系接口",content = "岗位绑定用户")
    @RepeatFormValidator
    @RequiresPermissions("base:post:users:rel:bind")
    @RequestMapping(value = "/post/{postId}/users/rel",method = RequestMethod.POST)
    public ResponseEntity postBindUsers(@PathVariable String postId, PostBindUsersFormDto postBindUsersFormDto){
        logger.info("岗位绑定用户开始");
        logger.info("当前登录用户id:{}",getLoginUser().getId());
        ResponseJsonRender resultData=new ResponseJsonRender();
        // 表单值设置
        PostBindUsersParamDto postBindUsersParamDto = new PostBindUsersParamDto();
        postBindUsersParamDto.setUserIds(postBindUsersFormDto.getUserIds());
        postBindUsersParamDto.setPostId(postId);
        postBindUsersParamDto.setCurrentUserId(getLoginUser().getId());

        int r = apiBaseUserPostRelPoService.postBindUsers(postBindUsersParamDto);

        logger.info("绑定岗位id:{}",postId);
        logger.info("岗位绑定用户结束，成功");
        return new ResponseEntity(resultData, HttpStatus.CREATED);
    }
    /**
     * 获取岗位绑定用户
     * @param postId 岗位id
     * @return
     */
    @OperationLog(operation = "用户与岗位关系接口",content = "获取岗位绑定用户")
    @RequiresPermissions("base:post:users:rel:get")
    @RequestMapping(value = "/post/{postId}/users/rel",method = RequestMethod.GET)
    public ResponseEntity postUsers(@PathVariable String postId){
        logger.info("获取岗位绑定用户开始");
        logger.info("当前登录用户id:{}",getLoginUser().getId());
        ResponseJsonRender resultData=new ResponseJsonRender();

        List<BaseUserPostRelDto> data = apiBaseUserPostRelPoService.selectByPostId(postId);
        if(CollectionUtils.isEmpty(data)){
            resultData.setCode(ResponseCode.E404_100001.getCode());
            resultData.setMsg(ResponseCode.E404_100001.getMsg());
            return new ResponseEntity(resultData, HttpStatus.NOT_FOUND);

        }
        resultData.setData(data);
        logger.info("绑定岗位id:{}",postId);
        logger.info("获取岗位绑定用户结束，成功");
        return new ResponseEntity(resultData, HttpStatus.OK);
    }
}
