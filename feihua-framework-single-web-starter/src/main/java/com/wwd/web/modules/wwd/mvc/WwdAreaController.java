package com.wwd.web.modules.wwd.mvc;

import com.wwd.service.modules.wwd.api.ApiWwdUserAreaPoService;
import com.wwd.service.modules.wwd.api.ApiWwdUserPoService;
import com.wwd.service.modules.wwd.dto.WwdUserAreaDto;
import com.wwd.service.modules.wwd.dto.WwdUserDto;
import com.feihua.framework.rest.ResponseJsonRender;
import com.feihua.framework.rest.interceptor.RepeatFormValidator;
import com.feihua.framework.rest.modules.common.mvc.BaseController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 汪汪队用户管理
 * Created by yangwei
 */
@RestController
@RequestMapping("/wwd")
public class WwdAreaController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(WwdAreaController.class);

    @Autowired
    private ApiWwdUserPoService apiWwdUserPoService;
    @Autowired
    private ApiWwdUserAreaPoService apiWwdUserAreaPoService;
    /**
     * 单资源，获取id汪汪队用户区域
     * @param id wwd_user_id
     * @return
     */
    @RepeatFormValidator
    @RequiresPermissions("wwd:user:area:getById")
    @RequestMapping(value = "/user/{id}/area",method = RequestMethod.GET)
    public ResponseEntity getArea(@PathVariable String id){

        ResponseJsonRender resultData=new ResponseJsonRender();
        WwdUserAreaDto userAreaDto = apiWwdUserAreaPoService.selectByWwdUserId(id);
        return super.returnDto(userAreaDto,resultData);
    }
    /**
     * 单资源，获取当前用户区域
     * @return
     */
    @RepeatFormValidator
    @RequiresPermissions("wwd:user:area:current:getById")
    @RequestMapping(value = "/user/current/area",method = RequestMethod.GET)
    public ResponseEntity getCurrentUserArea(){

        ResponseJsonRender resultData=new ResponseJsonRender();
        String userId = getLoginUser().getId();
        WwdUserDto userDto = apiWwdUserPoService.selectByUserId(userId);
        WwdUserAreaDto userAreaDto = apiWwdUserAreaPoService.selectByWwdUserId(userDto.getId());
        return super.returnDto(userAreaDto,resultData);
    }
}
