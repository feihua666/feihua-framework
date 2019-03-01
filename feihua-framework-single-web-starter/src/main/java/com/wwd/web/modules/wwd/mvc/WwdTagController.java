package com.wwd.web.modules.wwd.mvc;

import com.wwd.service.modules.wwd.api.ApiWwdUserPoService;
import com.wwd.service.modules.wwd.api.ApiWwdUserTagPoService;
import com.wwd.service.modules.wwd.dto.WwdUserDto;
import com.wwd.service.modules.wwd.dto.WwdUserTagDto;
import com.wwd.service.modules.wwd.po.WwdUserTagPo;
import com.feihua.framework.rest.ResponseJsonRender;
import com.feihua.framework.rest.interceptor.RepeatFormValidator;
import com.feihua.framework.rest.modules.common.mvc.BaseController;
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
 * 汪汪队用户管理
 * Created by yangwei
 */
@RestController
@RequestMapping("/wwd")
public class WwdTagController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(WwdTagController.class);

    @Autowired
    private ApiWwdUserPoService apiWwdUserPoService;
    @Autowired
    private ApiWwdUserTagPoService apiWwdUserTagPoService;

    /**
     * 单资源，添加用户标签
     * @return
     */
    @RepeatFormValidator
    @RequiresPermissions("wwd:user:tag:current:add")
    @RequestMapping(value = "/user/current/tag/{type}",method = RequestMethod.POST)
    public ResponseEntity addCurrentUserTag(@PathVariable String type,String content,String selfContent){
        logger.info("汪汪队当前用户添加标签开始");
        logger.info("当前登录用户id:{}",getLoginUser().getId());
        ResponseJsonRender resultData=new ResponseJsonRender();
        String userId = getLoginUser().getId();
        WwdUserDto userDto = apiWwdUserPoService.selectByUserId(userId);
        // 查询标签信息
        WwdUserTagPo wwdUserTagPo = new WwdUserTagPo();
        wwdUserTagPo.setWwdUserId(userDto.getId());
        wwdUserTagPo.setType(type);
        wwdUserTagPo.setContent(content);
        wwdUserTagPo.setSelfContent(selfContent);
        apiWwdUserTagPoService.preInsert(wwdUserTagPo,getLoginUserId());
        WwdUserTagDto r = apiWwdUserTagPoService.insertSelective(wwdUserTagPo);
        if (r == null) {
            // 添加失败
            resultData.setCode(ResponseCode.E404_100001.getCode());
            resultData.setMsg(ResponseCode.E404_100001.getMsg());
            logger.info("code:{},msg:{}",resultData.getCode(),resultData.getMsg());
            logger.info("汪汪队当前用户添加标签结束，失败");
            return new ResponseEntity(resultData,HttpStatus.NOT_FOUND);
        }else{
            // 添加成功，已被成功创建
            logger.info("添加的标签类型:{}",type);
            logger.info("汪汪队当前用户添加标签结束，成功");
            resultData.setData(r);
            return new ResponseEntity(resultData, HttpStatus.CREATED);
        }
    }

    /**
     * 单资源，更新用户标签
     * @return
     */
    @RepeatFormValidator
    @RequiresPermissions("wwd:user:tag:current:update")
    @RequestMapping(value = "/user/current/tag/{type}",method = RequestMethod.PUT)
    public ResponseEntity updateCurrentUserTag(@PathVariable String type,String content,String selfContent){
        logger.info("汪汪队当前用户更新标签开始");
        logger.info("当前登录用户id:{}",getLoginUser().getId());
        ResponseJsonRender resultData=new ResponseJsonRender();
        String userId = getLoginUser().getId();
        WwdUserDto userDto = apiWwdUserPoService.selectByUserId(userId);
        WwdUserTagDto wwdUserTagDtodb = apiWwdUserTagPoService.selectByWwdUserIdAndType(userDto.getId(),type);
        if(wwdUserTagDtodb == null){
            resultData.setCode(ResponseCode.E404_100001.getCode());
            resultData.setMsg(ResponseCode.E404_100001.getMsg());
            logger.info("code:{},msg:{}",resultData.getCode(),resultData.getMsg());
            logger.info("汪汪队当前用户更新标签结束，失败");
            return new ResponseEntity(resultData,HttpStatus.NOT_FOUND);
        }else{
            WwdUserTagPo wwdUserTagPo = new WwdUserTagPo();
            wwdUserTagPo.setId(wwdUserTagDtodb.getId());
            wwdUserTagPo.setContent(content);
            wwdUserTagPo.setSelfContent(selfContent);
            apiWwdUserTagPoService.preUpdate(wwdUserTagPo,userId);
            int r = apiWwdUserTagPoService.updateByPrimaryKeySelective(wwdUserTagPo);
            if (r <= 0) {
                // 更新失败
                resultData.setCode(ResponseCode.E404_100001.getCode());
                resultData.setMsg(ResponseCode.E404_100001.getMsg());
                logger.info("code:{},msg:{}",resultData.getCode(),resultData.getMsg());
                logger.info("汪汪队当前用户更新标签结束，失败");
                return new ResponseEntity(resultData,HttpStatus.NOT_FOUND);
            }else{
                // 更新成功，已被成功创建
                logger.info("添加的标签类型:{}",type);
                logger.info("汪汪队当前用户更新标签结束，成功");
                return new ResponseEntity(resultData, HttpStatus.CREATED);
            }
        }

    }
    /**
     * 多资源，获取id汪汪队用户标签
     * @param id wwd_user_id
     * @return
     */
    @RepeatFormValidator
    @RequiresPermissions("wwd:user:tags:getById")
    @RequestMapping(value = "/user/{id}/tags",method = RequestMethod.GET)
    public ResponseEntity getTag(@PathVariable String id){

        ResponseJsonRender resultData=new ResponseJsonRender();
        List<WwdUserTagDto> list = apiWwdUserTagPoService.selectByWwdUserId(id);
        return super.returnList(list,resultData);
    }

    /**
     * 多资源，获取当前用户标签
     * @return
     */
    @RepeatFormValidator
    @RequiresPermissions("wwd:user:tags:current:get")
    @RequestMapping(value = "/user/current/tags",method = RequestMethod.GET)
    public ResponseEntity getCurrentUserTags(){

        ResponseJsonRender resultData=new ResponseJsonRender();
        String userId = getLoginUser().getId();
        WwdUserDto userDto = apiWwdUserPoService.selectByUserId(userId);
        List<WwdUserTagDto> list = apiWwdUserTagPoService.selectByWwdUserId(userDto.getId());
        return super.returnList(list,resultData);
    }
    /**
     * 单资源，获取当前用户标签
     * @return
     */
    @RepeatFormValidator
    @RequiresPermissions("wwd:user:tag:current:get")
    @RequestMapping(value = "/user/current/tag/{type}",method = RequestMethod.GET)
    public ResponseEntity getCurrentUserTag(@PathVariable String type){

        ResponseJsonRender resultData=new ResponseJsonRender();
        String userId = getLoginUser().getId();
        WwdUserDto userDto = apiWwdUserPoService.selectByUserId(userId);
        WwdUserTagDto list = apiWwdUserTagPoService.selectByWwdUserIdAndType(userDto.getId(),type);
        return super.returnDto(list,resultData);
    }
}
