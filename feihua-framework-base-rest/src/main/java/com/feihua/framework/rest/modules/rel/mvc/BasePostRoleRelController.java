package com.feihua.framework.rest.modules.rel.mvc;

import com.feihua.framework.base.modules.rel.api.ApiBasePostRoleRelPoService;
import com.feihua.framework.base.modules.rel.dto.BasePostRoleRelDto;
import com.feihua.framework.base.modules.rel.dto.PostBindRolesParamDto;
import com.feihua.framework.base.modules.rel.dto.RoleBindPostsParamDto;
import com.feihua.framework.log.comm.annotation.OperationLog;
import com.feihua.framework.rest.ResponseJsonRender;
import com.feihua.framework.rest.interceptor.RepeatFormValidator;
import com.feihua.framework.rest.modules.rel.dto.PostBindRolesFormDto;
import com.feihua.framework.rest.modules.rel.dto.RoleBindPostsFormDto;
import com.feihua.framework.rest.mvc.SuperController;
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

import java.util.List;

/**
 * 岗位与角色关系接口
 * Created by yangwei
 */
@RestController
@RequestMapping("/base")
public class BasePostRoleRelController extends SuperController {

    private static Logger logger = LoggerFactory.getLogger(BasePostRoleRelController.class);

    @Autowired
    private ApiBasePostRoleRelPoService apiBasePostRoleRelPoService;

    /**
     * 岗位绑定角色
     * @param dto
     * @return
     */
    @OperationLog(operation = "岗位与角色关系接口",content = "岗位绑定角色")
    @RepeatFormValidator
    @RequiresPermissions("base:post:roles:rel:bind")
    @RequestMapping(value = "/post/{postId}/roles/rel",method = RequestMethod.POST)
    public ResponseEntity add(@PathVariable String postId, PostBindRolesFormDto dto){
        logger.info("岗位绑定角色开始开始");
        logger.info("当前登录岗位id:{}",getLoginUserId());
        ResponseJsonRender resultData = new ResponseJsonRender();
        // 表单值设置
        PostBindRolesParamDto postBindRolesParamDto = new PostBindRolesParamDto();
        postBindRolesParamDto.setRoleIds(dto.getRoleIds());
        postBindRolesParamDto.setPostId(postId);
        postBindRolesParamDto.setCurrentUserId(getLoginUserId());

        int r = apiBasePostRoleRelPoService.postBindRoles(postBindRolesParamDto);

        logger.info("绑定岗位id:{}",postId);
        logger.info("岗位绑定角色结束，成功");
        return new ResponseEntity(resultData, HttpStatus.CREATED);
    }

    /**
     * 获取岗位角色
     * @param postId 岗位id
     * @return
     */
    @OperationLog(operation = "岗位与角色关系接口",content = "获取岗位角色")
    @RequiresPermissions("base:post:roles:rel:get")
    @RequestMapping(value = "/post/{postId}/roles/rel",method = RequestMethod.GET)
    public ResponseEntity postRoles(@PathVariable String postId){
        logger.info("获取岗位绑定角色开始");
        logger.info("当前登录岗位id:{}",getLoginUserId());
        ResponseJsonRender resultData=new ResponseJsonRender();

        BasePostRoleRelDto data = apiBasePostRoleRelPoService.selectByPostId(postId);
        if(data == null){
            resultData.setCode(ResponseCode.E404_100001.getCode());
            resultData.setMsg(ResponseCode.E404_100001.getMsg());
            return new ResponseEntity(resultData, HttpStatus.NOT_FOUND);

        }
        resultData.setData(data);
        logger.info("绑定岗位id:{}",postId);
        logger.info("获取岗位绑定角色结束，成功");
        return new ResponseEntity(resultData, HttpStatus.OK);
    }
    /**
     * 角色绑定岗位
     * @param roleId 角色id
     * @param roleBindPostsFormDto 要绑定的岗位id
     * @return
     */
    @OperationLog(operation = "岗位与角色关系接口",content = "角色绑定岗位")
    @RepeatFormValidator
    @RequiresPermissions("base:role:posts:rel:bind")
    @RequestMapping(value = "/role/{roleId}/posts/rel",method = RequestMethod.POST)
    public ResponseEntity roleBindPosts(@PathVariable String roleId, RoleBindPostsFormDto roleBindPostsFormDto){
        logger.info("角色绑定岗位开始");
        logger.info("当前登录岗位id:{}",getLoginUserId());
        ResponseJsonRender resultData=new ResponseJsonRender();
        // 表单值设置
        RoleBindPostsParamDto roleBindPostsParamDto = new RoleBindPostsParamDto();
        roleBindPostsParamDto.setPostIds(roleBindPostsFormDto.getPostIds());
        roleBindPostsParamDto.setRoleId(roleId);
        roleBindPostsParamDto.setCurrentUserId(getLoginUserId());

        int r = apiBasePostRoleRelPoService.roleBindPosts(roleBindPostsParamDto);

        logger.info("绑定角色id:{}",roleId);
        logger.info("角色绑定岗位结束，成功");
        return new ResponseEntity(resultData, HttpStatus.CREATED);
    }
    /**
     * 获取角色绑定岗位
     * @param roleId 角色id
     * @return
     */
    @OperationLog(operation = "岗位与角色关系接口",content = "获取角色绑定岗位")
    @RequiresPermissions("base:role:posts:rel:get")
    @RequestMapping(value = "/role/{roleId}/posts/rel",method = RequestMethod.GET)
    public ResponseEntity rolePosts(@PathVariable String roleId){
        logger.info("获取角色绑定岗位开始");
        logger.info("当前登录岗位id:{}",getLoginUserId());
        ResponseJsonRender resultData=new ResponseJsonRender();

        List<BasePostRoleRelDto> data = apiBasePostRoleRelPoService.selectByRoleId(roleId);
        if(data == null || data.isEmpty()){
            resultData.setCode(ResponseCode.E404_100001.getCode());
            resultData.setMsg(ResponseCode.E404_100001.getMsg());
            return new ResponseEntity(resultData, HttpStatus.NOT_FOUND);

        }
        resultData.setData(data);
        logger.info("绑定角色id:{}",roleId);
        logger.info("获取角色绑定岗位结束，成功");
        return new ResponseEntity(resultData, HttpStatus.OK);
    }
}
