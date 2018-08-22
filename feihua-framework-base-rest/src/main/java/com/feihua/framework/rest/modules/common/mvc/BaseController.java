package com.feihua.framework.rest.modules.common.mvc;

import com.feihua.framework.base.modules.role.dto.BaseRoleDto;
import com.feihua.framework.rest.ResponseJsonRender;
import com.feihua.framework.rest.mvc.SuperController;
import com.feihua.framework.rest.service.AccountServiceImpl;
import com.feihua.framework.shiro.pojo.ShiroUser;
import com.feihua.framework.shiro.utils.ShiroUtils;
import com.feihua.utils.http.httpServletResponse.ResponseCode;
import feihua.jdbc.api.pojo.PageAndOrderbyParamDto;
import feihua.jdbc.api.pojo.PageResultDto;
import feihua.jdbc.api.utils.OrderbyUtils;
import feihua.jdbc.api.utils.PageUtils;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

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
    public String getLoginUserRoleId(){
        BaseRoleDto roleDto = getLoginUserRole();
        if(roleDto != null){
            return roleDto.getId();
        }
        return null;
    }
}
