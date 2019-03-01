package com.wwd.web.modules.wwd.mvc;

import com.wwd.service.modules.wwd.api.ApiWwdUserEnjoyPoService;
import com.wwd.service.modules.wwd.api.ApiWwdUserPicPoService;
import com.wwd.service.modules.wwd.api.ApiWwdUserPoService;
import com.wwd.service.modules.wwd.dto.WwdUserDto;
import com.wwd.service.modules.wwd.dto.WwdUserEnjoyDto;
import com.wwd.service.modules.wwd.dto.WwdUserPicDto;
import com.wwd.service.modules.wwd.po.WwdUserEnjoyPo;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 汪汪队用户管理
 * Created by yangwei
 */
@RestController
@RequestMapping("/wwd")
public class WwdEnjoyController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(WwdEnjoyController.class);

    @Autowired
    private ApiWwdUserPoService apiWwdUserPoService;
    @Autowired
    private ApiWwdUserPicPoService apiWwdUserPicPoService;
    @Autowired
    private ApiWwdUserEnjoyPoService apiWwdUserEnjoyPoService;

    /**
     * 单资源，我是否对他有意思
     * @return
     */
    @RepeatFormValidator
    @RequiresPermissions("wwd:user:current:enjoy:getById")
    @RequestMapping(value = "/user/current/enjoy/{enjoyedWwdUserId}",method = RequestMethod.GET)
    public ResponseEntity getEnjoy(@PathVariable String enjoyedWwdUserId){

        ResponseJsonRender resultData=new ResponseJsonRender();

        String userId = getLoginUser().getId();
        WwdUserDto userDto = apiWwdUserPoService.selectByUserId(userId);
        WwdUserEnjoyDto wwdUserEnjoyDto = apiWwdUserEnjoyPoService.selectEnjoyedFromTo(userDto.getId(),enjoyedWwdUserId);
        return super.returnDto(wwdUserEnjoyDto,resultData);
    }
    /**
     * 单资源，我对他/她有意思
     * @return
     */
    @RepeatFormValidator
    @RequiresPermissions("wwd:user:current:enjoy:add")
    @RequestMapping(value = "/user/current/enjoy/{enjoyedWwdUserId}",method = RequestMethod.POST)
    public ResponseEntity addEnjoy(@PathVariable String enjoyedWwdUserId){
        logger.info("汪汪队添加有意思开始");
        logger.info("当前登录用户id:{}",getLoginUser().getId());
        ResponseJsonRender resultData=new ResponseJsonRender();

        String userId = getLoginUser().getId();
        WwdUserDto userDto = apiWwdUserPoService.selectByUserId(userId);

        WwdUserEnjoyPo wwdUserEnjoyPo = new WwdUserEnjoyPo();
        wwdUserEnjoyPo.setWwdUserId(userDto.getId());
        wwdUserEnjoyPo.setEnjoyedWwdUserId(enjoyedWwdUserId);
        apiWwdUserEnjoyPoService.preInsert(wwdUserEnjoyPo,getLoginUserId());
        WwdUserEnjoyDto r = apiWwdUserEnjoyPoService.insertSelective(wwdUserEnjoyPo);

        if (r == null) {
            // 添加失败
            resultData.setCode(ResponseCode.E404_100001.getCode());
            resultData.setMsg(ResponseCode.E404_100001.getMsg());
            logger.info("code:{},msg:{}",resultData.getCode(),resultData.getMsg());
            logger.info("汪汪队添加有意思汪汪队添加有意思结束，失败");
            return new ResponseEntity(resultData,HttpStatus.NOT_FOUND);
        }else{
            // 添加成功，已被成功创建
            logger.info("添加的汪汪队用户id:{}",enjoyedWwdUserId);
            logger.info("汪汪队添加有意思结束，成功");
            resultData.setData(r);
            return new ResponseEntity(resultData, HttpStatus.CREATED);
        }
    }

    /**
     * 单资源，我是否对他有意思
     * @return
     */
    @RepeatFormValidator
    @RequiresPermissions("wwd:user:current:enjoys:get")
    @RequestMapping(value = "/user/current/enjoys/{status}",method = RequestMethod.GET)
    public ResponseEntity selectEnjoys(@PathVariable String status){

        ResponseJsonRender resultData=new ResponseJsonRender();

        String userId = getLoginUser().getId();
        WwdUserDto userDto = apiWwdUserPoService.selectByUserId(userId);
        List<WwdUserEnjoyDto> wwdUserEnjoyDtos = null;
        List<String> wwdUserIds = new ArrayList<>();
        if("1".equals(status)){
            wwdUserEnjoyDtos = apiWwdUserEnjoyPoService.selectByWwdUserId(userDto.getId());
            if(wwdUserEnjoyDtos!=null && wwdUserEnjoyDtos.size()>0){
                for (WwdUserEnjoyDto wwdUserEnjoyDto : wwdUserEnjoyDtos) {
                    wwdUserIds.add(wwdUserEnjoyDto.getEnjoyedWwdUserId());
                }
            }
        }else if("2".equals(status)){
            wwdUserEnjoyDtos =  apiWwdUserEnjoyPoService.selectByEnjoyedWwdUserId(userDto.getId());
            if(wwdUserEnjoyDtos!=null && wwdUserEnjoyDtos.size()>0){
                for (WwdUserEnjoyDto wwdUserEnjoyDto : wwdUserEnjoyDtos) {
                    wwdUserIds.add(wwdUserEnjoyDto.getWwdUserId());
                }
            }
        }else {
            wwdUserEnjoyDtos =  apiWwdUserEnjoyPoService.selectEnjoyedByWwdUserId(userDto.getId());
            if(wwdUserEnjoyDtos!=null && wwdUserEnjoyDtos.size()>0){
                for (WwdUserEnjoyDto wwdUserEnjoyDto : wwdUserEnjoyDtos) {
                    wwdUserIds.add(wwdUserEnjoyDto.getEnjoyedWwdUserId());
                }
            }
        }
        List<WwdUserDto> wwdUserDtos = null;
        if (wwdUserIds != null && !wwdUserIds.isEmpty()) {
            wwdUserDtos = apiWwdUserPoService.selectByPrimaryKeys(wwdUserIds, false);
            if (wwdUserDtos!=null && wwdUserDtos.size()>0) {
                Map<String, String> picMap = null;
                List<Map<String, String>> picList = new ArrayList<>();
                for (WwdUserDto dto : wwdUserDtos) {
                    if("1".equals(status) || "1".equals(status)){
                        dto.setWechatNumber(null);
                    }
                    List<WwdUserPicDto> picDtos = apiWwdUserPicPoService.selectByWwdUserId(dto.getId());
                    if (picDtos != null && !picDtos.isEmpty()) {
                        for (WwdUserPicDto picDto : picDtos) {
                            if ("main".equals(picDto.getType())) {
                                picMap = new HashMap<>();
                                picMap.put("picUrl", picDto.getPicOriginUrl());
                                picMap.put("wwdUserId", dto.getId());
                                picList.add(picMap);
                                break;
                            }
                        }
                    }
                }
                resultData.addData("pic",picList);
            }
        }

        return super.returnList(wwdUserDtos,resultData);
    }

}
