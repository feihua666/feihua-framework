package com.wwd.web.modules.wwd.mvc;

import com.wwd.service.modules.wwd.api.ApiWwdUserPicPoService;
import com.wwd.service.modules.wwd.api.ApiWwdUserPoService;
import com.wwd.service.modules.wwd.dto.WwdUserDto;
import com.wwd.service.modules.wwd.dto.WwdUserPicDto;
import com.wwd.service.modules.wwd.po.WwdUserPicPo;
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
public class WwdPicController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(WwdPicController.class);

    @Autowired
    private ApiWwdUserPoService apiWwdUserPoService;
    @Autowired
    private ApiWwdUserPicPoService apiWwdUserPicPoService;

    /**
     * 单资源，获取id汪汪队用户图片
     * @param id wwd_user_id
     * @return
     */
    @RepeatFormValidator
    @RequiresPermissions("wwd:user:pic:getById")
    @RequestMapping(value = "/user/{id}/pic",method = RequestMethod.GET)
    public ResponseEntity getPic(@PathVariable String id){

        ResponseJsonRender resultData=new ResponseJsonRender();
        List<WwdUserPicDto> list = apiWwdUserPicPoService.selectByWwdUserId(id);
        return super.returnList(list,resultData);
    }
    /**
     * 单资源，添加图片
     * @return
     */
    @RepeatFormValidator
    @RequiresPermissions("wwd:user:pic:current:add")
    @RequestMapping(value = "/user/current/pic",method = RequestMethod.POST)
    public ResponseEntity addCurrentPic(String url,String type,int sequence){

        ResponseJsonRender resultData=new ResponseJsonRender();
        String userId = getLoginUser().getId();
        WwdUserDto wwdUserDto = apiWwdUserPoService.selectByUserId(userId);

        // 添加一个图片，默认以头像做为主图
        WwdUserPicPo wwdUserPicPo = new WwdUserPicPo();
        wwdUserPicPo.setWwdUserId(wwdUserDto.getId());
        wwdUserPicPo.setSequence(sequence);
        wwdUserPicPo.setType(type);
        wwdUserPicPo.setPicOriginUrl(url);
        wwdUserPicPo.setPicThumbUrl(url);
        apiWwdUserPicPoService.preInsert(wwdUserPicPo,userId);
        WwdUserPicDto picDto = apiWwdUserPicPoService.insertSelective(wwdUserPicPo);
        return super.returnDto(picDto,resultData);
    }
    /**
     * 单资源，删除图片
     * @return
     */
    @RepeatFormValidator
    @RequiresPermissions("wwd:user:pic:current:delete")
    @RequestMapping(value = "/user/current/pic/{id}",method = RequestMethod.DELETE)
    public ResponseEntity addCurrentPic(@PathVariable String id){
        logger.info("汪汪队用户删除图片开始");
        logger.info("当前登录用户id:{}",getLoginUser().getId());
        ResponseJsonRender resultData=new ResponseJsonRender();

        int r = apiWwdUserPicPoService.deleteFlagByPrimaryKeyWithUpdateUser(id,getLoginUserId());
        if (r <= 0) {
            // 删除失败，可能没有找到资源
            resultData.setCode(ResponseCode.E404_100001.getCode());
            resultData.setMsg(ResponseCode.E404_100001.getMsg());
            logger.info("code:{},msg:{}",resultData.getCode(),resultData.getMsg());
            logger.info("汪汪队用户删除图片结束，失败");
            return new ResponseEntity(resultData,HttpStatus.NOT_FOUND);
        }else{
            // 删除成功
            logger.info("删除的图片id:{}",id);
            logger.info("汪汪队用户删除图片结束，成功");
            return new ResponseEntity(resultData,HttpStatus.NO_CONTENT);
        }
    }
    /**
     * 单资源，更新图片
     * @return
     */
    @RepeatFormValidator
    @RequiresPermissions("wwd:user:pic:current:update")
    @RequestMapping(value = "/user/current/pic/{id}",method = RequestMethod.PUT)
    public ResponseEntity updateCurrentPic(@PathVariable String id,String url,String type,int sequence){
        logger.info("汪汪队用户更新图片开始");
        logger.info("当前登录用户id:{}",getLoginUser().getId());
        ResponseJsonRender resultData=new ResponseJsonRender();
        String userId = getLoginUser().getId();

        // 添加一个图片，默认以头像做为主图
        WwdUserPicPo wwdUserPicPo = new WwdUserPicPo();
        wwdUserPicPo.setId(id);
        wwdUserPicPo.setSequence(sequence);
        wwdUserPicPo.setType(type);
        wwdUserPicPo.setPicOriginUrl(url);
        wwdUserPicPo.setPicThumbUrl(url);
        apiWwdUserPicPoService.preInsert(wwdUserPicPo,userId);
        int r = apiWwdUserPicPoService.updateByPrimaryKeySelective(wwdUserPicPo);
        if (r <= 0) {
            // 更新失败，资源不存在
            resultData.setCode(ResponseCode.E404_100001.getCode());
            resultData.setMsg(ResponseCode.E404_100001.getMsg());
            logger.info("code:{},msg:{}",resultData.getCode(),resultData.getMsg());
            logger.info("汪汪队用户更新图片结束，失败");
            return new ResponseEntity(resultData,HttpStatus.NOT_FOUND);
        }else{
            // 更新成功，已被成功创建
            logger.info("更新图片的id:{}",id);
            logger.info("汪汪队用户更新图片结束，成功");
            return new ResponseEntity(resultData, HttpStatus.CREATED);
        }
    }
    /**
     * 单资源，获取当前用户图片
     * @return
     */
    @RepeatFormValidator
    @RequiresPermissions("wwd:user:pic:current:getById")
    @RequestMapping(value = "/user/current/pic",method = RequestMethod.GET)
    public ResponseEntity getCurrentPic(){

        ResponseJsonRender resultData=new ResponseJsonRender();
        String userId = getLoginUser().getId();
        WwdUserDto userDto = apiWwdUserPoService.selectByUserId(userId);
        List<WwdUserPicDto> list = apiWwdUserPicPoService.selectByWwdUserId(userDto.getId());
        return super.returnList(list,resultData);
    }
}
