package com.feihua.framework.rest.modules.office.mvc;


import com.feihua.framework.base.modules.area.api.ApiBaseAreaPoService;
import com.feihua.framework.base.modules.area.dto.BaseAreaDto;
import com.feihua.framework.base.modules.office.api.ApiBaseOfficePoService;
import com.feihua.framework.base.modules.office.dto.BaseOfficeDto;
import com.feihua.framework.base.modules.office.dto.SearchOfficesConditionDto;
import com.feihua.framework.base.modules.office.po.BaseOfficePo;
import com.feihua.framework.base.modules.role.dto.BaseRoleDto;
import com.feihua.framework.log.comm.annotation.OperationLog;
import com.feihua.utils.http.httpServletResponse.ResponseCode;
import com.feihua.framework.rest.ResponseJsonRender;
import com.feihua.framework.rest.modules.office.dto.AddOfficeFormDto;
import com.feihua.framework.rest.modules.office.dto.UpdateOfficeFormDto;
import com.feihua.framework.rest.interceptor.RepeatFormValidator;
import com.feihua.framework.rest.modules.common.mvc.BaseController;
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
 * 机构树接口
 * Created by yangwei
 * Created at 2017/12/21 13:13
 */
@RestController
@RequestMapping("/base")
public class BaseOfficeController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(BaseOfficeController.class);

    @Autowired
    private ApiBaseOfficePoService apiBaseOfficePoService;
    @Autowired
    private ApiBaseAreaPoService apiBaseAreaPoService;

    /**
     * 单资源，添加机构
     * @param addOfficeFormDto
     * @return
     */
    @OperationLog(operation = "机构树接口",content = "单资源，添加机构")
    @RepeatFormValidator
    @RequiresPermissions("base:office:add")
    @RequestMapping(value = "/office",method = RequestMethod.POST)
    public ResponseEntity addOffice(AddOfficeFormDto addOfficeFormDto){
        logger.info("添加机构开始");
        logger.info("当前登录用户id:{}",getLoginUser().getId());
        ResponseJsonRender resultData=new ResponseJsonRender();
        // 表单值设置
        BaseOfficePo baseOfficePo = new BaseOfficePo();
        baseOfficePo.setName(addOfficeFormDto.getName());
        baseOfficePo.setAreaId(addOfficeFormDto.getAreaId());
        baseOfficePo.setType(addOfficeFormDto.getType());
        baseOfficePo.setAddress(addOfficeFormDto.getAddress());
        baseOfficePo.setZipCode(addOfficeFormDto.getZipCode());
        baseOfficePo.setMaster(addOfficeFormDto.getMaster());
        baseOfficePo.setPhone(addOfficeFormDto.getPhone());
        baseOfficePo.setFax(addOfficeFormDto.getFax());
        baseOfficePo.setEmail(addOfficeFormDto.getEmail());
        baseOfficePo.setPrimaryUserId(addOfficeFormDto.getPrimaryUserId());
        baseOfficePo.setDeputyUserId(addOfficeFormDto.getDeputyUserId());
        baseOfficePo.setParentId(addOfficeFormDto.getParentId());

        baseOfficePo = apiBaseOfficePoService.preInsert(baseOfficePo,getLoginUser().getId());
        BaseOfficeDto r = apiBaseOfficePoService.insert(baseOfficePo);
        if (r == null) {
            // 添加失败
            resultData.setCode(ResponseCode.E404_100001.getCode());
            resultData.setMsg(ResponseCode.E404_100001.getMsg());
            logger.info("code:{},msg:{}",resultData.getCode(),resultData.getMsg());
            logger.info("添加机构结束，失败");
            return new ResponseEntity(resultData,HttpStatus.NOT_FOUND);
        }else{
            // 添加成功，返回添加的数据
            resultData.setData(r);
            logger.info("添加机构id:{}",r.getId());
            logger.info("添加机构结束，成功");
            return new ResponseEntity(resultData, HttpStatus.CREATED);
        }
    }

    /**
     * 单资源，删除机构
     * @param id
     * @return
     */
    @OperationLog(operation = "机构树接口",content = "单资源，删除机构")
    @RepeatFormValidator
    @RequiresPermissions("base:office:delete")
    @RequestMapping(value = "/office/{id}",method = RequestMethod.DELETE)
    public ResponseEntity deleteOffice(@PathVariable String id){
        logger.info("删除机构开始");
        logger.info("用户id:{}",getLoginUser().getId());
        logger.info("机构id:{}",id);
        ResponseJsonRender resultData=new ResponseJsonRender();

        List<BaseOfficePo> children = apiBaseOfficePoService.getChildrenAll(id);
        // 如果存在子级，则不充许删除
        if(CollectionUtils.isNotEmpty(children)){
            resultData.setMsg(ResponseCode.E403_100003.getMsg() + ",children nodes exist");
            resultData.setCode(ResponseCode.E403_100003.getCode());
            logger.info("code:{},msg:{}",resultData.getCode(),resultData.getMsg());
            logger.info("删除机构结束，失败");
            return new ResponseEntity(resultData,HttpStatus.FORBIDDEN);
        }else {
            int r = apiBaseOfficePoService.deleteFlagByPrimaryKeyWithUpdateUser(id,getLoginUser().getId());
            if (r <= 0) {
                // 删除失败，可能没有找到资源
                resultData.setCode(ResponseCode.E404_100001.getCode());
                resultData.setMsg(ResponseCode.E404_100001.getMsg());
                logger.info("code:{},msg:{}",resultData.getCode(),resultData.getMsg());
                logger.info("删除机构结束，失败");
                return new ResponseEntity(resultData,HttpStatus.NOT_FOUND);
            }else{
                // 删除成功
                logger.info("删除的机构id:{}",id);
                logger.info("删除机构结束，成功");
                return new ResponseEntity(resultData,HttpStatus.NO_CONTENT);
            }
        }
    }

    /**
     * 单资源，更新机构
     * @param id
     * @param updateOfficeFormDto
     * @return
     */
    @OperationLog(operation = "机构树接口",content = "单资源，更新机构")
    @RepeatFormValidator
    @RequiresPermissions("base:office:update")
    @RequestMapping(value = "/office/{id}",method = RequestMethod.PUT)
    public ResponseEntity updateOffice(@PathVariable String id, UpdateOfficeFormDto updateOfficeFormDto){
        logger.info("更新机构开始");
        logger.info("当前登录用户id:{}",getLoginUser().getId());
        logger.info("机构id:{}",id);
        ResponseJsonRender resultData=new ResponseJsonRender();
        // 表单值设置
        BaseOfficePo baseOfficePo = new BaseOfficePo();
        // id
        baseOfficePo.setId(id);
        baseOfficePo.setName(updateOfficeFormDto.getName());
        baseOfficePo.setAreaId(updateOfficeFormDto.getAreaId());
        baseOfficePo.setType(updateOfficeFormDto.getType());
        baseOfficePo.setAddress(updateOfficeFormDto.getAddress());
        baseOfficePo.setZipCode(updateOfficeFormDto.getZipCode());
        baseOfficePo.setMaster(updateOfficeFormDto.getMaster());
        baseOfficePo.setPhone(updateOfficeFormDto.getPhone());
        baseOfficePo.setFax(updateOfficeFormDto.getFax());
        baseOfficePo.setEmail(updateOfficeFormDto.getEmail());
        baseOfficePo.setPrimaryUserId(updateOfficeFormDto.getPrimaryUserId());
        baseOfficePo.setDeputyUserId(updateOfficeFormDto.getDeputyUserId());
        baseOfficePo.setParentId(updateOfficeFormDto.getParentId());

        // 用条件更新，乐观锁机制
        BaseOfficePo baseOfficePoCondition = new BaseOfficePo();
        baseOfficePoCondition.setId(id);
        baseOfficePoCondition.setDelFlag(BasePo.YesNo.N.name());
        baseOfficePoCondition.setUpdateAt(updateOfficeFormDto.getUpdateTime());
        baseOfficePo = apiBaseOfficePoService.preUpdate(baseOfficePo,getLoginUser().getId());
        int r = apiBaseOfficePoService.updateSelective(baseOfficePo,baseOfficePoCondition);
        if (r <= 0) {
            // 更新失败，资源不存在
            resultData.setCode(ResponseCode.E404_100001.getCode());
            resultData.setMsg(ResponseCode.E404_100001.getMsg());
            logger.info("code:{},msg:{}",resultData.getCode(),resultData.getMsg());
            logger.info("更新机构结束，失败");
            return new ResponseEntity(resultData,HttpStatus.NOT_FOUND);
        }else{
            // 更新成功，已被成功创建
            logger.info("更新的机构id:{}",id);
            logger.info("更新机构结束，成功");
            return new ResponseEntity(resultData, HttpStatus.CREATED);
        }
    }

    /**
     * 单资源，获取id机构
     * @param id
     * @return
     */
    @OperationLog(operation = "机构树接口",content = "单资源，获取id机构")
    @RequiresPermissions("base:office:getById")
    @RequestMapping(value = "/office/{id}",method = RequestMethod.GET)
    public ResponseEntity getById(@PathVariable String id){

        ResponseJsonRender resultData=new ResponseJsonRender();
        BaseOfficeDto baseOfficeDto = apiBaseOfficePoService.selectByPrimaryKey(id,false);
        if(baseOfficeDto != null){
            resultData.setData(baseOfficeDto);
            return new ResponseEntity(resultData, HttpStatus.OK);
        }else{
            resultData.setCode(ResponseCode.E404_100001.getCode());
            resultData.setMsg(ResponseCode.E404_100001.getMsg());
            return new ResponseEntity(resultData,HttpStatus.NOT_FOUND);
        }
    }

    /**
     * 复数资源，搜索机构
     * @param dto
     * @return
     */
    @OperationLog(operation = "机构树接口",content = "复数资源，搜索机构")
    @RequiresPermissions("base:office:search")
    @RequestMapping(value = "/offices",method = RequestMethod.GET)
    public ResponseEntity searchOffices(SearchOfficesConditionDto dto, boolean includeParent, boolean includeArea){

        ResponseJsonRender resultData=new ResponseJsonRender();
        PageAndOrderbyParamDto pageAndOrderbyParamDto = new PageAndOrderbyParamDto(PageUtils.getPageFromThreadLocal(), OrderbyUtils.getOrderbyFromThreadLocal());
        // 设置当前登录用户id
        dto.setCurrentUserId(getLoginUser().getId());
        dto.setCurrentRoleId(getLoginUserRoleId());
        PageResultDto<BaseOfficeDto> list = apiBaseOfficePoService.searchOfficesDsf(dto,pageAndOrderbyParamDto);

        if(list.getData() != null && !list.getData().isEmpty()){

            //区域和父级
            if(includeArea || includeParent){
                Map<String,BaseOfficeDto> officeDtoMap = new HashMap<>();
                BaseOfficeDto officeDto = null;

                //区域
                Map<String,BaseAreaDto> areaDtoMap = new HashMap<>();
                BaseAreaDto areaDto = null;
                for (BaseOfficeDto _officeDto : list.getData()) {
                    if(includeArea && StringUtils.isNotEmpty(_officeDto.getAreaId())){
                        areaDto = apiBaseAreaPoService.selectByPrimaryKey(_officeDto.getAreaId());
                        if (areaDto != null) {
                            areaDtoMap.put(_officeDto.getAreaId(),areaDto);
                        }
                    }

                    if(includeParent && StringUtils.isNotEmpty(_officeDto.getParentId())){
                        officeDto = apiBaseOfficePoService.selectByPrimaryKey(_officeDto.getParentId());
                        if (officeDto != null) {
                            officeDtoMap.put(_officeDto.getParentId(),officeDto);
                        }
                    }
                }
                if (!areaDtoMap.isEmpty()) {
                    resultData.addData("area",areaDtoMap);
                }
                if (!officeDtoMap.isEmpty()) {
                    resultData.addData("parent",officeDtoMap);
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
