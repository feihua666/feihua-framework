package com.wwd.web.modules.wwd.mvc;

import com.feihua.framework.base.modules.dict.api.ApiBaseDictPoService;
import com.feihua.framework.base.modules.dict.po.BaseDictPo;
import com.feihua.framework.constants.DictEnum;
import com.feihua.framework.rest.ResponseJsonRender;
import com.feihua.framework.rest.interceptor.RepeatFormValidator;
import com.feihua.framework.rest.modules.common.mvc.BaseController;
import com.feihua.framework.utils.AliOssClientHelper;
import com.feihua.utils.calendar.CalendarUtils;
import com.feihua.utils.graphic.ImageUtils;
import com.feihua.utils.http.httpServletRequest.RequestUtils;
import com.feihua.utils.http.httpServletResponse.ResponseCode;
import com.feihua.utils.http.httpclient.HttpClientUtils;
import com.feihua.utils.io.StreamUtils;
import com.wwd.service.modules.wwd.api.ApiWwdUserPoService;
import com.wwd.service.modules.wwd.api.ApiWwdUserTagPoService;
import com.wwd.service.modules.wwd.dto.*;
import feihua.jdbc.api.pojo.BasePo;
import feihua.jdbc.api.pojo.PageAndOrderbyParamDto;
import feihua.jdbc.api.pojo.PageResultDto;
import feihua.jdbc.api.utils.OrderbyUtils;
import feihua.jdbc.api.utils.PageUtils;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
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
import com.wwd.service.modules.wwd.api.ApiWwdUserCardPoService;
import com.wwd.web.modules.wwd.dto.AddWwdUserCardFormDto;
import com.wwd.web.modules.wwd.dto.UpdateWwdUserCardFormDto;
import com.wwd.service.modules.wwd.po.WwdUserCardPo;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 汪汪队用户卡片管理
 * Created by yangwei
 */
@RestController
@RequestMapping("/wwd")
public class WwdUserCardController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(WwdUserCardController.class);

    @Autowired
    private ApiWwdUserCardPoService apiWwdUserCardPoService;
    @Autowired
    private ApiWwdUserPoService apiWwdUserPoService;

    /**
     * 单资源，添加
     * @param addFormDto
     * @return
     */
    @RepeatFormValidator
    @RequiresPermissions("wwd:user:card:add")
    @RequestMapping(value = "/user/card",method = RequestMethod.POST)
    public ResponseEntity add(AddWwdUserCardFormDto addFormDto){
        logger.info("添加汪汪队用户卡片开始");
        logger.info("当前登录用户id:{}",getLoginUser().getId());
        ResponseJsonRender resultData=new ResponseJsonRender();
        // 表单值设置
        WwdUserCardPo basePo = new WwdUserCardPo();
        basePo.setWwdUserId(addFormDto.getWwdUserId());
        basePo.setPicOriginUrl(addFormDto.getPicOriginUrl());
        basePo.setPicThumbUrl(addFormDto.getPicThumbUrl());
        basePo.setSequence(addFormDto.getSequence());
        basePo.setType(addFormDto.getType());
        basePo.setDescribtion(addFormDto.getDescribtion());
        //todo

        apiWwdUserCardPoService.preInsert(basePo,getLoginUser().getId());
        WwdUserCardDto r = apiWwdUserCardPoService.insert(basePo);
        if (r == null) {
            // 添加失败
            resultData.setCode(ResponseCode.E404_100001.getCode());
            resultData.setMsg(ResponseCode.E404_100001.getMsg());
            logger.info("code:{},msg:{}",resultData.getCode(),resultData.getMsg());
            logger.info("添加汪汪队用户卡片结束，失败");
            return new ResponseEntity(resultData,HttpStatus.NOT_FOUND);
        }else{
            // 添加成功，返回添加的数据
            resultData.setData(r);
            logger.info("添加汪汪队用户卡片id:{}",r.getId());
            logger.info("添加汪汪队用户卡片结束，成功");
            return new ResponseEntity(resultData, HttpStatus.CREATED);
        }
    }

    /**
     * 单资源，删除
     * @param id
     * @return
     */
    @RepeatFormValidator
    @RequiresPermissions("wwd:user:card:delete")
    @RequestMapping(value = "/user/card/{id}",method = RequestMethod.DELETE)
    public ResponseEntity delete(@PathVariable String id){
        logger.info("删除汪汪队用户卡片开始");
        logger.info("用户id:{}",getLoginUser().getId());
        logger.info("汪汪队用户卡片id:{}",id);
        ResponseJsonRender resultData=new ResponseJsonRender();

            int r = apiWwdUserCardPoService.deleteFlagByPrimaryKeyWithUpdateUser(id,getLoginUser().getId());
            if (r <= 0) {
                // 删除失败，可能没有找到资源
                resultData.setCode(ResponseCode.E404_100001.getCode());
                resultData.setMsg(ResponseCode.E404_100001.getMsg());
                logger.info("code:{},msg:{}",resultData.getCode(),resultData.getMsg());
                logger.info("删除汪汪队用户卡片结束，失败");
                return new ResponseEntity(resultData,HttpStatus.NOT_FOUND);
            }else{
                // 删除成功
                logger.info("删除的汪汪队用户卡片id:{}",id);
                logger.info("删除汪汪队用户卡片结束，成功");
                return new ResponseEntity(resultData,HttpStatus.NO_CONTENT);
            }
    }

    /**
     * 单资源，更新
     * @param id
     * @param updateFormDto
     * @return
     */
    @RepeatFormValidator
    @RequiresPermissions("wwd:user:card:update")
    @RequestMapping(value = "/user/card/{id}",method = RequestMethod.PUT)
    public ResponseEntity update(@PathVariable String id, UpdateWwdUserCardFormDto updateFormDto){
        logger.info("更新汪汪队用户卡片开始");
        logger.info("当前登录用户id:{}",getLoginUser().getId());
        logger.info("汪汪队用户卡片id:{}",id);
        ResponseJsonRender resultData=new ResponseJsonRender();
        // 表单值设置
        WwdUserCardPo basePo = new WwdUserCardPo();
        // id
        basePo.setId(id);
        basePo.setWwdUserId(updateFormDto.getWwdUserId());
        basePo.setPicOriginUrl(updateFormDto.getPicOriginUrl());
        basePo.setPicThumbUrl(updateFormDto.getPicThumbUrl());
        basePo.setSequence(updateFormDto.getSequence());
        basePo.setType(updateFormDto.getType());
        basePo.setDescribtion(updateFormDto.getDescribtion());

        // 用条件更新，乐观锁机制
        WwdUserCardPo basePoCondition = new WwdUserCardPo();
        basePoCondition.setId(id);
        basePoCondition.setDelFlag(BasePo.YesNo.N.name());
        basePoCondition.setUpdateAt(updateFormDto.getUpdateTime());
        apiWwdUserCardPoService.preUpdate(basePo,getLoginUser().getId());
        int r = apiWwdUserCardPoService.updateSelective(basePo,basePoCondition);
        if (r <= 0) {
            // 更新失败，资源不存在
            resultData.setCode(ResponseCode.E404_100001.getCode());
            resultData.setMsg(ResponseCode.E404_100001.getMsg());
            logger.info("code:{},msg:{}",resultData.getCode(),resultData.getMsg());
            logger.info("更新汪汪队用户卡片结束，失败");
            return new ResponseEntity(resultData,HttpStatus.NOT_FOUND);
        }else{
            // 更新成功，已被成功创建
            logger.info("更新的汪汪队用户卡片id:{}",id);
            logger.info("更新汪汪队用户卡片结束，成功");
            return new ResponseEntity(resultData, HttpStatus.CREATED);
        }
    }

    /**
     * 单资源，获取id汪汪队用户卡片
     * @param id
     * @return
     */
    @RepeatFormValidator
    @RequiresPermissions("wwd:user:card:getById")
    @RequestMapping(value = "/user/card/{id}",method = RequestMethod.GET)
    public ResponseEntity getById(@PathVariable String id){

        ResponseJsonRender resultData=new ResponseJsonRender();
        WwdUserCardDto baseDataScopeDto = apiWwdUserCardPoService.selectByPrimaryKey(id,false);
        if(baseDataScopeDto != null){
            resultData.setData(baseDataScopeDto);
            return new ResponseEntity(resultData, HttpStatus.OK);
        }else{
            resultData.setCode(ResponseCode.E404_100001.getCode());
            resultData.setMsg(ResponseCode.E404_100001.getMsg());
            return new ResponseEntity(resultData,HttpStatus.NOT_FOUND);
        }
    }

    /**
     * 复数资源，搜索汪汪队用户卡片
     * @param dto
     * @return
     */
    @RepeatFormValidator
    @RequiresPermissions("wwd:user:card:search")
    @RequestMapping(value = "/user/cards",method = RequestMethod.GET)
    public ResponseEntity search(SearchWwdUserCardsConditionDto dto){

        ResponseJsonRender resultData=new ResponseJsonRender();
        PageAndOrderbyParamDto pageAndOrderbyParamDto = new PageAndOrderbyParamDto(PageUtils.getPageFromThreadLocal(), OrderbyUtils.getOrderbyFromThreadLocal());
        // 设置当前登录用户id
        dto.setCurrentUserId(getLoginUser().getId());
        dto.setCurrentRoleId(getLoginUserRoleId());
        PageResultDto<WwdUserCardDto> list = apiWwdUserCardPoService.searchWwdUserCardsDsf(dto,pageAndOrderbyParamDto);

        if(CollectionUtils.isNotEmpty(list.getData())){
            resultData.setData(list.getData());
            resultData.setPage(list.getPage());
            return new ResponseEntity(resultData, HttpStatus.OK);
        }else{
            resultData.setCode(ResponseCode.E404_100001.getCode());
            resultData.setMsg(ResponseCode.E404_100001.getMsg());
            return new ResponseEntity(resultData,HttpStatus.NOT_FOUND);
        }
    }

    /**
     * 单资源，生成卡片
     * @return
     */
    @RepeatFormValidator
    //@RequiresPermissions("user")
    @RequestMapping(value = "/user/generatecard",method = RequestMethod.POST)
    public ResponseEntity generateCard(String userId){
        logger.info("生成卡片开始");
        logger.info("当前登录用户id:{}",getLoginUser().getId());
        ResponseJsonRender resultData=new ResponseJsonRender();

        if(StringUtils.isEmpty(userId)) {
            userId = getLoginUserId();
        }
        WwdUserCardPo cardPo = apiWwdUserCardPoService.generateCard(userId);

        if (cardPo == null) {
            // 资源不存在
            resultData.setCode(ResponseCode.E404_100001.getCode());
            resultData.setMsg(ResponseCode.E404_100001.getMsg());
            logger.info("code:{},msg:{}",resultData.getCode(),resultData.getMsg());
            logger.info("生成卡片结束，失败");
            return new ResponseEntity(resultData,HttpStatus.NOT_FOUND);

        }
            resultData.setData(cardPo);
            logger.info("生成卡片结束，成功");
            return new ResponseEntity(resultData, HttpStatus.CREATED);
    }


    /**
     * 单资源，获取当前登录用户汪汪队用户卡片

     * @return
     */
    @RepeatFormValidator
    @RequiresPermissions("user")
    @RequestMapping(value = "/user/current/card",method = RequestMethod.GET)
    public ResponseEntity getMyCard(String userId){

        ResponseJsonRender resultData=new ResponseJsonRender();

        if (StringUtils.isEmpty(userId)){
            userId = getLoginUserId();
        }
        WwdUserDto wwdUserDto = apiWwdUserPoService.selectByUserId(userId);

        if (wwdUserDto == null) {

            resultData.setCode(ResponseCode.E404_100001.getCode());
            resultData.setMsg(ResponseCode.E404_100001.getMsg());
            return new ResponseEntity(resultData,HttpStatus.NOT_FOUND);
        }

        WwdUserCardPo userCardPo = apiWwdUserCardPoService.selectByWwdUserId(wwdUserDto.getId());
        if(userCardPo != null){
            resultData.setData(userCardPo);
            return new ResponseEntity(resultData, HttpStatus.OK);
        }else{
            resultData.setCode(ResponseCode.E404_100001.getCode());
            resultData.setMsg(ResponseCode.E404_100001.getMsg());
            return new ResponseEntity(resultData,HttpStatus.NOT_FOUND);
        }
    }
}


