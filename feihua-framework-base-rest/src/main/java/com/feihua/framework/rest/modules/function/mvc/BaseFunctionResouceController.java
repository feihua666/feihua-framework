package com.feihua.framework.rest.modules.function.mvc;

import com.feihua.framework.base.modules.function.api.ApiBaseFunctionResourcePoService;
import com.feihua.framework.base.modules.function.dto.BaseFunctionResourceDto;
import com.feihua.framework.base.modules.function.dto.SearchFunctionResourcesConditionDto;
import com.feihua.framework.base.modules.function.po.BaseFunctionResourcePo;
import com.feihua.utils.http.httpServletResponse.ResponseCode;
import com.feihua.framework.rest.ResponseJsonRender;
import com.feihua.framework.rest.modules.function.dto.AddFunctionResourceFormDto;
import com.feihua.framework.rest.modules.function.dto.UpdateFunctionResourceFormDto;
import com.feihua.framework.rest.interceptor.RepeatFormValidator;
import com.feihua.framework.rest.modules.common.mvc.BaseController;
import feihua.jdbc.api.pojo.BasePo;
import feihua.jdbc.api.pojo.PageAndOrderbyParamDto;
import feihua.jdbc.api.pojo.PageResultDto;
import feihua.jdbc.api.utils.OrderbyUtils;
import feihua.jdbc.api.utils.PageUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.authz.annotation.Logical;
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
 * 功能资源接口
 * Created by yangwei
 * Created at 2017/12/21 13:13
 */
@RestController
@RequestMapping("/base")
public class BaseFunctionResouceController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(BaseFunctionResouceController.class);

    @Autowired
    private ApiBaseFunctionResourcePoService apiBaseFunctionResourcePoService;

    /**
     * 单资源，添加功能资源
     * @param addFunctionResourceFormDto
     * @return
     */
    @RepeatFormValidator
    @RequiresPermissions("base:functionResource:add")
    @RequestMapping(value = "/functionResource",method = RequestMethod.POST)
    public ResponseEntity addFunctionResource(AddFunctionResourceFormDto addFunctionResourceFormDto){
        logger.info("添加功能资源开始");
        logger.info("当前登录用户id:{}",getLoginUser().getId());
        ResponseJsonRender resultData=new ResponseJsonRender();
        // 表单值设置
        BaseFunctionResourcePo baseFunctionResourcePo = new BaseFunctionResourcePo();
        baseFunctionResourcePo.setName(addFunctionResourceFormDto.getName());
        baseFunctionResourcePo.setIcon(addFunctionResourceFormDto.getIcon());
        baseFunctionResourcePo.setIsShow(addFunctionResourceFormDto.getIsShow());
        baseFunctionResourcePo.setUrl(addFunctionResourceFormDto.getUrl());
        baseFunctionResourcePo.setPermissions(addFunctionResourceFormDto.getPermissions());
        baseFunctionResourcePo.setType(addFunctionResourceFormDto.getType());
        baseFunctionResourcePo.setSequence(addFunctionResourceFormDto.getSequence());
        baseFunctionResourcePo.setParentId(addFunctionResourceFormDto.getParentId());

        apiBaseFunctionResourcePoService.preInsert(baseFunctionResourcePo,getLoginUser().getId());
        BaseFunctionResourceDto r = apiBaseFunctionResourcePoService.insert(baseFunctionResourcePo);
        if (r == null) {
            // 添加失败
            resultData.setCode(ResponseCode.E404_100001.getCode());
            resultData.setMsg(ResponseCode.E404_100001.getMsg());
            logger.info("code:{},msg:{}",resultData.getCode(),resultData.getMsg());
            logger.info("添加功能资源结束，失败");
            return new ResponseEntity(resultData,HttpStatus.NOT_FOUND);
        }else{
            // 添加成功，返回添加的数据
            resultData.setData(r);
            logger.info("添加功能资源id:{}",r.getId());
            logger.info("添加功能资源结束，成功");
            return new ResponseEntity(resultData, HttpStatus.CREATED);
        }
    }

    /**
     * 单资源，删除功能资源
     * @param id
     * @return
     */
    @RepeatFormValidator
    @RequiresPermissions("base:functionResource:delete")
    @RequestMapping(value = "/functionResource/{id}",method = RequestMethod.DELETE)
    public ResponseEntity deleteFunctionResource(@PathVariable String id){
        logger.info("删除功能资源开始");
        logger.info("用户id:{}",getLoginUser().getId());
        logger.info("功能资源id:{}",id);
        ResponseJsonRender resultData=new ResponseJsonRender();

        List<BaseFunctionResourcePo> children = apiBaseFunctionResourcePoService.getChildrenAll(id);
        // 如果存在子级，则不充许删除
        if(CollectionUtils.isNotEmpty(children)){
            resultData.setMsg(ResponseCode.E403_100003.getMsg() + ",children nodes exist");
            resultData.setCode(ResponseCode.E403_100003.getCode());
            logger.info("code:{},msg:{}",resultData.getCode(),resultData.getMsg());
            logger.info("删除功能资源结束，失败");
            return new ResponseEntity(resultData,HttpStatus.FORBIDDEN);
        }else {
            int r = apiBaseFunctionResourcePoService.deleteFlagByPrimaryKeyWithUpdateUser(id,getLoginUser().getId());
            if (r <= 0) {
                // 删除失败，可能没有找到资源
                resultData.setCode(ResponseCode.E404_100001.getCode());
                resultData.setMsg(ResponseCode.E404_100001.getMsg());
                logger.info("code:{},msg:{}",resultData.getCode(),resultData.getMsg());
                logger.info("删除功能资源结束，失败");
                return new ResponseEntity(resultData,HttpStatus.NOT_FOUND);
            }else{
                // 删除成功
                logger.info("删除的功能资源id:{}",id);
                logger.info("删除功能资源结束，成功");
                return new ResponseEntity(resultData,HttpStatus.NO_CONTENT);
            }
        }
    }

    /**
     * 单资源，更新功能资源
     * @param id
     * @param updateFunctionResourceFormDto
     * @return
     */
    @RepeatFormValidator
    @RequiresPermissions("base:functionResource:update")
    @RequestMapping(value = "/functionResource/{id}",method = RequestMethod.PUT)
    public ResponseEntity updateFunctionResource(@PathVariable String id, UpdateFunctionResourceFormDto updateFunctionResourceFormDto){
        logger.info("更新功能资源开始");
        logger.info("当前登录用户id:{}",getLoginUser().getId());
        logger.info("功能资源id:{}",id);
        ResponseJsonRender resultData=new ResponseJsonRender();
        // 表单值设置
        BaseFunctionResourcePo baseFunctionResourcePo = new BaseFunctionResourcePo();
        // id
        baseFunctionResourcePo.setId(id);
        baseFunctionResourcePo.setName(updateFunctionResourceFormDto.getName());
        baseFunctionResourcePo.setIcon(updateFunctionResourceFormDto.getIcon());
        baseFunctionResourcePo.setIsShow(updateFunctionResourceFormDto.getIsShow());
        baseFunctionResourcePo.setUrl(updateFunctionResourceFormDto.getUrl());
        baseFunctionResourcePo.setPermissions(updateFunctionResourceFormDto.getPermissions());
        baseFunctionResourcePo.setType(updateFunctionResourceFormDto.getType());
        baseFunctionResourcePo.setSequence(updateFunctionResourceFormDto.getSequence());
        baseFunctionResourcePo.setParentId(updateFunctionResourceFormDto.getParentId());

        // 用条件更新，乐观锁机制
        BaseFunctionResourcePo baseFunctionResourcePoCondition = new BaseFunctionResourcePo();
        baseFunctionResourcePoCondition.setId(id);
        baseFunctionResourcePoCondition.setDelFlag(BasePo.YesNo.N.name());
        baseFunctionResourcePoCondition.setUpdateAt(updateFunctionResourceFormDto.getUpdateTime());
        apiBaseFunctionResourcePoService.preUpdate(baseFunctionResourcePo,getLoginUser().getId());
        int r = apiBaseFunctionResourcePoService.updateSelective(baseFunctionResourcePo,baseFunctionResourcePoCondition);
        if (r <= 0) {
            // 更新失败，资源不存在
            resultData.setCode(ResponseCode.E404_100001.getCode());
            resultData.setMsg(ResponseCode.E404_100001.getMsg());
            logger.info("code:{},msg:{}",resultData.getCode(),resultData.getMsg());
            logger.info("更新功能资源结束，失败");
            return new ResponseEntity(resultData,HttpStatus.NOT_FOUND);
        }else{
            // 更新成功，已被成功创建
            logger.info("更新的功能资源id:{}",id);
            logger.info("更新功能资源结束，成功");
            return new ResponseEntity(resultData, HttpStatus.CREATED);
        }
    }

    /**
     * 单资源，获取id功能资源
     * @param id
     * @return
     */
    @RepeatFormValidator
    @RequiresPermissions("base:functionResource:getById")
    @RequestMapping(value = "/functionResource/{id}",method = RequestMethod.GET)
    public ResponseEntity getById(@PathVariable String id){

        ResponseJsonRender resultData=new ResponseJsonRender();
        BaseFunctionResourceDto baseFunctionResourceDto = apiBaseFunctionResourcePoService.selectByPrimaryKey(id,false);
        if(baseFunctionResourceDto != null){
            resultData.setData(baseFunctionResourceDto);
            return new ResponseEntity(resultData, HttpStatus.OK);
        }else{
            resultData.setCode(ResponseCode.E404_100001.getCode());
            resultData.setMsg(ResponseCode.E404_100001.getMsg());
            return new ResponseEntity(resultData,HttpStatus.NOT_FOUND);
        }
    }

    /**
     * 复数资源，搜索功能资源
     * @param dto
     * @return
     */
    @RepeatFormValidator
    @RequiresPermissions(value = {"base:functionResource:search","user"},logical = Logical.OR)
    @RequestMapping(value = "/functionResources",method = RequestMethod.GET)
    public ResponseEntity searchFunctionResources(SearchFunctionResourcesConditionDto dto){

        ResponseJsonRender resultData=new ResponseJsonRender();
        PageAndOrderbyParamDto pageAndOrderbyParamDto = new PageAndOrderbyParamDto(PageUtils.getPageFromThreadLocal(), OrderbyUtils.getOrderbyFromThreadLocal());
        // 设置当前登录用户id
        dto.setCurrentUserId(getLoginUser().getId());
        dto.setCurrentRoleId(getLoginUserRoleId());
        PageResultDto<BaseFunctionResourceDto> list = apiBaseFunctionResourcePoService.searchFunctionResourcesDsf(dto,pageAndOrderbyParamDto);

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

}
