package com.feihua.framework.message.mvc;

import com.feihua.framework.base.modules.postjob.dto.BasePostDto;
import com.feihua.framework.base.modules.role.dto.BaseRoleDto;
import com.feihua.framework.rest.mvc.SuperController;
import com.feihua.framework.shiro.pojo.ShiroUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * controller基类，负责一些通用处理方法
 * Created by yw on 2015/9/23.
 */
public class BaseController extends SuperController{
    private static Logger logger = LoggerFactory.getLogger(BaseController.class);


    public BaseRoleDto getLoginUserRole(){
        ShiroUser su = super.getLoginUser();
        if(su != null){
            return (BaseRoleDto) su.getRole();
        }
        return null;
    }
    public BasePostDto getLoginUserPost(){
        ShiroUser su = super.getLoginUser();
        if(su != null){
            return (BasePostDto) su.getPost();
        }
        return null;
    }
    public String getLoginUserRoleId(){
        BaseRoleDto roleDto = getLoginUserRole();
        if(roleDto != null){
            return roleDto.getId();
        }
        return null;
    }
    public String getLoginUserPostId(){
        BasePostDto postDto = getLoginUserPost();
        if(postDto != null){
            return postDto.getId();
        }
        return null;
    }
}
