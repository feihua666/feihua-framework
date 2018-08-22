package com.feihua.framework.rest.modules.dict.mvc;


import com.feihua.framework.base.modules.dict.api.ApiBaseDictPoService;
import com.feihua.framework.base.modules.dict.dto.BaseDictDto;
import com.feihua.framework.base.modules.dict.dto.SearchDictsConditionDto;
import com.feihua.framework.base.modules.dict.dto.SelectDictsConditionDto;
import com.feihua.framework.base.modules.dict.po.BaseDictPo;
import com.feihua.utils.http.httpServletResponse.ResponseCode;
import com.feihua.framework.rest.ResponseJsonRender;
import com.feihua.framework.rest.interceptor.RepeatFormValidator;
import com.feihua.framework.rest.modules.common.mvc.BaseController;
import com.feihua.framework.rest.modules.dict.dto.AddDictFormDto;
import com.feihua.framework.rest.modules.dict.dto.UpdateDictFormDto;
import feihua.jdbc.api.pojo.BasePo;
import feihua.jdbc.api.pojo.PageAndOrderbyParamDto;
import feihua.jdbc.api.pojo.PageResultDto;
import feihua.jdbc.api.utils.OrderbyUtils;
import feihua.jdbc.api.utils.PageUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 字典配置接口
 * Created by yangwei
 * Created at 2017/12/21 13:13
 */
@RestController
@RequestMapping("/base")
public class BaseDictController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(BaseDictController.class);

    @Autowired
    private ApiBaseDictPoService apiBaseDictPoService;

    /**
     * 单资源，添加字典
     * @param addDictFormDto
     * @return
     */
    @RepeatFormValidator
    @RequiresPermissions("base:dict:add")
    @RequestMapping(value = "/dict",method = RequestMethod.POST)
    public ResponseEntity addDict(AddDictFormDto addDictFormDto){
        logger.info("添加字典开始");
        logger.info("当前登录用户id:{}",getLoginUser().getId());
        ResponseJsonRender resultData=new ResponseJsonRender();
        // 表单值设置
        BaseDictPo baseDictPo = new BaseDictPo();
        baseDictPo.setValue(addDictFormDto.getValue());
        baseDictPo.setName(addDictFormDto.getName());
        baseDictPo.setDiscription(addDictFormDto.getDiscription());
        baseDictPo.setType(addDictFormDto.getType());
        baseDictPo.setSequence(addDictFormDto.getSequence());
        baseDictPo.setIsSystem(addDictFormDto.getIsSystem());
        baseDictPo.setIsPublic(addDictFormDto.getIsPublic());
        baseDictPo.setParentId(addDictFormDto.getParentId());
        baseDictPo.setDataAreaId(addDictFormDto.getDataAreaId());
        baseDictPo.setDataOfficeId(addDictFormDto.getDataOfficeId());

        apiBaseDictPoService.preInsert(baseDictPo,getLoginUser().getId());
        BaseDictDto r = apiBaseDictPoService.insert(baseDictPo);
        if (r == null) {
            // 添加失败
            resultData.setCode(ResponseCode.E404_100001.getCode());
            resultData.setMsg(ResponseCode.E404_100001.getMsg());
            logger.info("code:{},msg:{}",resultData.getCode(),resultData.getMsg());
            logger.info("添加字典结束，失败");
            return new ResponseEntity(resultData,HttpStatus.NOT_FOUND);
        }else{
            // 添加成功，返回添加的数据
            resultData.setData(r);
            logger.info("添加字典id:{}",r.getId());
            logger.info("添加字典结束，成功");
            return new ResponseEntity(resultData, HttpStatus.CREATED);
        }
    }

    /**
     * 单资源，删除字典
     * @param id
     * @return
     */
    @RepeatFormValidator
    @RequiresPermissions("base:dict:delete")
    @RequestMapping(value = "/dict/{id}",method = RequestMethod.DELETE)
    public ResponseEntity deleteDict(@PathVariable String id){
        logger.info("删除字典开始");
        logger.info("用户id:{}",getLoginUser().getId());
        logger.info("字典id:{}",id);
        ResponseJsonRender resultData=new ResponseJsonRender();

        List<BaseDictPo> children = apiBaseDictPoService.getChildrenAll(id);
        // 如果存在子级，则不充许删除
        if(CollectionUtils.isNotEmpty(children)){
            resultData.setMsg(ResponseCode.E403_100003.getMsg() + ",children nodes exist");
            resultData.setCode(ResponseCode.E403_100003.getCode());
            logger.info("code:{},msg:{}",resultData.getCode(),resultData.getMsg());
            logger.info("删除字典结束，失败");
            return new ResponseEntity(resultData,HttpStatus.FORBIDDEN);
        }else {
            int r = apiBaseDictPoService.deleteFlagByPrimaryKeyWithUpdateUser(id,getLoginUser().getId());
            if (r <= 0) {
                // 删除失败，可能没有找到资源
                resultData.setCode(ResponseCode.E404_100001.getCode());
                resultData.setMsg(ResponseCode.E404_100001.getMsg());
                logger.info("code:{},msg:{}",resultData.getCode(),resultData.getMsg());
                logger.info("删除字典结束，失败");
                return new ResponseEntity(resultData,HttpStatus.NOT_FOUND);
            }else{
                // 删除成功
                logger.info("删除的字典id:{}",id);
                logger.info("删除字典结束，成功");
                return new ResponseEntity(resultData,HttpStatus.NO_CONTENT);
            }
        }
    }

    /**
     * 单资源，更新字典
     * @param id
     * @param updateDictFormDto
     * @return
     */
    @RepeatFormValidator
    @RequiresPermissions("base:dict:update")
    @RequestMapping(value = "/dict/{id}",method = RequestMethod.PUT)
    public ResponseEntity updateDict(@PathVariable String id, UpdateDictFormDto updateDictFormDto){
        logger.info("更新字典开始");
        logger.info("当前登录用户id:{}",getLoginUser().getId());
        logger.info("字典id:{}",id);
        ResponseJsonRender resultData=new ResponseJsonRender();
        // 表单值设置
        BaseDictPo baseDictPo = new BaseDictPo();
        // id
        baseDictPo.setId(id);
        baseDictPo.setValue(updateDictFormDto.getValue());
        baseDictPo.setName(updateDictFormDto.getName());
        baseDictPo.setDiscription(updateDictFormDto.getDiscription());
        baseDictPo.setType(updateDictFormDto.getType());
        baseDictPo.setSequence(updateDictFormDto.getSequence());
        baseDictPo.setIsSystem(updateDictFormDto.getIsSystem());
        baseDictPo.setIsPublic(updateDictFormDto.getIsPublic());
        baseDictPo.setDataAreaId(updateDictFormDto.getDataAreaId());
        baseDictPo.setDataOfficeId(updateDictFormDto.getDataOfficeId());
        baseDictPo.setParentId(updateDictFormDto.getParentId());

        // 用条件更新，乐观锁机制
        BaseDictPo baseDictPoCondition = new BaseDictPo();
        baseDictPoCondition.setId(id);
        baseDictPoCondition.setDelFlag(BasePo.YesNo.N.name());
        baseDictPoCondition.setUpdateAt(updateDictFormDto.getUpdateTime());
        apiBaseDictPoService.preUpdate(baseDictPo,getLoginUser().getId());
        int r = apiBaseDictPoService.updateSelective(baseDictPo,baseDictPoCondition);
        if (r <= 0) {
            // 更新失败，资源不存在
            resultData.setCode(ResponseCode.E404_100001.getCode());
            resultData.setMsg(ResponseCode.E404_100001.getMsg());
            logger.info("code:{},msg:{}",resultData.getCode(),resultData.getMsg());
            logger.info("更新字典结束，失败");
            return new ResponseEntity(resultData,HttpStatus.NOT_FOUND);
        }else{
            // 更新成功，已被成功创建
            logger.info("更新的字典id:{}",id);
            logger.info("更新字典结束，成功");
            return new ResponseEntity(resultData, HttpStatus.CREATED);
        }
    }

    /**
     * 单资源，获取id字典
     * @param id
     * @return
     */
    @RepeatFormValidator
    @RequiresPermissions("base:dict:getById")
    @RequestMapping(value = "/dict/{id}",method = RequestMethod.GET)
    public ResponseEntity getById(@PathVariable String id){

        ResponseJsonRender resultData=new ResponseJsonRender();
        BaseDictDto baseDictDto = apiBaseDictPoService.selectByPrimaryKey(id,false);
        if(baseDictDto != null){
            resultData.setData(baseDictDto);
            return new ResponseEntity(resultData, HttpStatus.OK);
        }else{
            resultData.setCode(ResponseCode.E404_100001.getCode());
            resultData.setMsg(ResponseCode.E404_100001.getMsg());
            return new ResponseEntity(resultData,HttpStatus.NOT_FOUND);
        }
    }
    /**
     * 复数资源，搜索字典，支持多个以逗号分隔
     * @param type
     * @return
     */
    @RepeatFormValidator
    // @RequiresPermissions("base:dict:search")
    // 好多地方都用到，不设置权限
    @RequestMapping(value = "/dicts/{type}",method = RequestMethod.GET)
    public ResponseEntity getDictsByParentType(@PathVariable String type){

        ResponseJsonRender resultData=new ResponseJsonRender();
        String types[] = type.split(",");
        if(types.length == 1){

            SelectDictsConditionDto conditionDto = new SelectDictsConditionDto();
            conditionDto.setType(type);
            conditionDto.setCurrentUserId(getLoginUserId());
            conditionDto.setCurrentRoleId(getLoginUserRoleId());
            List<BaseDictDto> parentList = apiBaseDictPoService.selectByTypeDsf(conditionDto, OrderbyUtils.getOrderbyFromThreadLocal());
            if(parentList != null && !parentList.isEmpty()){
                resultData.setData(parentList);
                return new ResponseEntity(resultData, HttpStatus.OK);
            }
            resultData.setCode(ResponseCode.E404_100001.getCode());
            resultData.setMsg(ResponseCode.E404_100001.getMsg());
            return new ResponseEntity(resultData,HttpStatus.NOT_FOUND);
        }else{
            Map<String,Object> result = new HashMap<>();
            for (String t : types) {
                SelectDictsConditionDto conditionDto = new SelectDictsConditionDto();
                conditionDto.setType(t);
                conditionDto.setCurrentUserId(getLoginUserId());
                conditionDto.setCurrentRoleId(getLoginUserRoleId());
                List<BaseDictDto> parentList = apiBaseDictPoService.selectByTypeDsf(conditionDto, OrderbyUtils.getOrderbyFromThreadLocal());
                if(CollectionUtils.isNotEmpty(parentList)){
                    result.put(t,parentList);
                }
            }
            if(result != null && !result.isEmpty()){
                resultData.setData(result);
                return new ResponseEntity(resultData, HttpStatus.OK);
            }
            resultData.setCode(ResponseCode.E404_100001.getCode());
            resultData.setMsg(ResponseCode.E404_100001.getMsg());
            return new ResponseEntity(resultData,HttpStatus.NOT_FOUND);
        }

    }
    /**
     * 复数资源，搜索字典
     * @param dto
     * @return
     */
    @RepeatFormValidator
    @RequiresPermissions("base:dict:search")
    @RequestMapping(value = "/dicts",method = RequestMethod.GET)
    public ResponseEntity searchDicts(SearchDictsConditionDto dto){

        ResponseJsonRender resultData=new ResponseJsonRender();
        PageAndOrderbyParamDto pageAndOrderbyParamDto = new PageAndOrderbyParamDto(PageUtils.getPageFromThreadLocal(), OrderbyUtils.getOrderbyFromThreadLocal());
        // 设置当前登录用户id
        dto.setCurrentUserId(getLoginUser().getId());
        dto.setCurrentRoleId(getLoginUserRoleId());
        PageResultDto<BaseDictDto> list = apiBaseDictPoService.searchDictsDsf(dto,pageAndOrderbyParamDto);

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
