package com.feihua.framework.rest.modules.dict.mvc;


import com.feihua.framework.base.modules.area.api.ApiBaseAreaPoService;
import com.feihua.framework.base.modules.area.dto.BaseAreaDto;
import com.feihua.framework.base.modules.dict.api.ApiBaseDictPoService;
import com.feihua.framework.base.modules.dict.dto.BaseDictDto;
import com.feihua.framework.base.modules.dict.dto.SearchDictsConditionDto;
import com.feihua.framework.base.modules.dict.dto.SelectDictsConditionDto;
import com.feihua.framework.base.modules.dict.po.BaseDictPo;
import com.feihua.framework.base.modules.office.api.ApiBaseOfficePoService;
import com.feihua.framework.base.modules.office.dto.BaseOfficeDto;
import com.feihua.framework.log.comm.annotation.OperationLog;
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
    @Autowired
    private ApiBaseAreaPoService apiBaseAreaPoService;
    @Autowired
    private ApiBaseOfficePoService apiBaseOfficePoService;
    /**
     * 单资源，添加字典
     * @param addDictFormDto
     * @return
     */
    @OperationLog(operation = "字典配置接口", content = "单资源，添加字典")
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

        baseDictPo = apiBaseDictPoService.preInsert(baseDictPo,getLoginUser().getId());
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
    @OperationLog(operation = "字典配置接口", content = "单资源，删除字典")
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
    @OperationLog(operation = "字典配置接口", content = "单资源，更新字典")
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
        baseDictPo = apiBaseDictPoService.preUpdate(baseDictPo,getLoginUser().getId());
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
    @OperationLog(operation = "字典",content = "根据ID获取字典",isInsert = false)
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
    @OperationLog(operation = "字典",content = "列表查询")
    @RequiresPermissions("base:dict:search")
    @RequestMapping(value = "/dicts",method = RequestMethod.GET)
    public ResponseEntity searchDicts(SearchDictsConditionDto dto, boolean includeParent,boolean includeOffice,boolean includeArea){

        ResponseJsonRender resultData=new ResponseJsonRender();
        PageAndOrderbyParamDto pageAndOrderbyParamDto = new PageAndOrderbyParamDto(PageUtils.getPageFromThreadLocal(), OrderbyUtils.getOrderbyFromThreadLocal());
        // 设置当前登录用户id
        dto.setCurrentUserId(getLoginUser().getId());
        dto.setCurrentRoleId(getLoginUserRoleId());
        PageResultDto<BaseDictDto> list = apiBaseDictPoService.searchDictsDsf(dto,pageAndOrderbyParamDto);

        if(list.getData() != null && !list.getData().isEmpty()){
            //机构和区域、父级
            if (includeOffice || includeArea || includeParent) {
                // 父级
                Map<String,BaseDictDto> parentDtoMap = new HashMap<>();
                BaseDictDto parentDto = null;

                //区域
                Map<String,BaseAreaDto> areaDtoMap = new HashMap<>();
                BaseAreaDto areaDto = null;

                //机构
                Map<String,BaseOfficeDto> officeDtoMap = new HashMap<>();
                BaseOfficeDto officeDto = null;
                for (BaseDictDto dictDto : list.getData()) {
                    if(includeArea && StringUtils.isNotEmpty(dictDto.getDataAreaId())){
                        areaDto = apiBaseAreaPoService.selectByPrimaryKey(dictDto.getDataAreaId());
                        if (areaDto != null) {
                            areaDtoMap.put(dictDto.getDataAreaId(),areaDto);
                        }
                    }

                    if(includeOffice && StringUtils.isNotEmpty(dictDto.getDataOfficeId())){
                        officeDto = apiBaseOfficePoService.selectByPrimaryKey(dictDto.getDataOfficeId());
                        if (officeDto != null) {
                            officeDtoMap.put(dictDto.getDataOfficeId(),officeDto);
                        }
                    }

                    if(includeParent && StringUtils.isNotEmpty(dictDto.getParentId())){
                        parentDto = apiBaseDictPoService.selectByPrimaryKey(dictDto.getParentId());
                        if (parentDto != null) {
                            parentDtoMap.put(dictDto.getParentId(),parentDto);
                        }
                    }
                }
                if (!areaDtoMap.isEmpty()) {
                    resultData.addData("area",areaDtoMap);
                }
                if (!officeDtoMap.isEmpty()) {
                    resultData.addData("office",officeDtoMap);
                }
                if (!parentDtoMap.isEmpty()) {
                    resultData.addData("parent",parentDtoMap);
                }
            }

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
