package com.feihua.framework.rest.modules.datascope.mvc;


import com.feihua.framework.base.modules.datascope.api.ApiBaseDataScopePoService;
import com.feihua.framework.base.modules.datascope.dto.BaseDataScopeDto;
import com.feihua.framework.base.modules.datascope.dto.SearchDataScopesConditionDto;
import com.feihua.framework.base.modules.datascope.po.BaseDataScopePo;
import com.feihua.framework.base.modules.office.api.ApiBaseOfficePoService;
import com.feihua.framework.base.modules.office.dto.BaseOfficeDto;
import com.feihua.framework.log.comm.annotation.OperationLog;
import com.feihua.utils.http.httpServletResponse.ResponseCode;
import com.feihua.framework.rest.ResponseJsonRender;
import com.feihua.framework.rest.interceptor.RepeatFormValidator;
import com.feihua.framework.rest.modules.common.mvc.BaseController;
import com.feihua.framework.rest.modules.datascope.dto.AddDataScopeFormDto;
import com.feihua.framework.rest.modules.datascope.dto.UpdateDataScopeFormDto;
import feihua.jdbc.api.pojo.BasePo;
import feihua.jdbc.api.pojo.PageAndOrderbyParamDto;
import feihua.jdbc.api.pojo.PageResultDto;
import feihua.jdbc.api.utils.OrderbyUtils;
import feihua.jdbc.api.utils.PageUtils;
import org.apache.commons.collections.CollectionUtils;
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
import java.util.Map;

/**
 * 数据范围配置接口
 * Created by yangwei
 * Created at 2017/12/21 13:13
 */
@RestController
@RequestMapping("/base")
public class BaseDataScopeController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(BaseDataScopeController.class);

    @Autowired
    private ApiBaseDataScopePoService apiBaseDataScopePoService;
    @Autowired
    private ApiBaseOfficePoService apiBaseOfficePoService;
    /**
     * 单资源，添加数据范围
     * @param addDataScopeFormDto
     * @return
     */
    @OperationLog(operation = "数据范围配置接口", content = "单资源，添加数据范围")
    @RepeatFormValidator
    @RequiresPermissions("base:dataScope:add")
    @RequestMapping(value = "/dataScope",method = RequestMethod.POST)
    public ResponseEntity addDataScope(AddDataScopeFormDto addDataScopeFormDto){
        logger.info("添加数据范围开始");
        logger.info("当前登录用户id:{}",getLoginUser().getId());
        ResponseJsonRender resultData=new ResponseJsonRender();
        // 表单值设置
        BaseDataScopePo baseDataScopePo = new BaseDataScopePo();
        baseDataScopePo.setName(addDataScopeFormDto.getName());
        baseDataScopePo.setType(addDataScopeFormDto.getType());
        baseDataScopePo.setDataOfficeId(addDataScopeFormDto.getDataOfficeId());

        baseDataScopePo = apiBaseDataScopePoService.preInsert(baseDataScopePo,getLoginUser().getId());
        BaseDataScopeDto r = apiBaseDataScopePoService.insert(baseDataScopePo);
        if (r == null) {
            // 添加失败
            resultData.setCode(ResponseCode.E404_100001.getCode());
            resultData.setMsg(ResponseCode.E404_100001.getMsg());
            logger.info("code:{},msg:{}",resultData.getCode(),resultData.getMsg());
            logger.info("添加数据范围结束，失败");
            return new ResponseEntity(resultData,HttpStatus.NOT_FOUND);
        }else{
            // 添加成功，返回添加的数据
            resultData.setData(r);
            logger.info("添加数据范围id:{}",r.getId());
            logger.info("添加数据范围结束，成功");
            return new ResponseEntity(resultData, HttpStatus.CREATED);
        }
    }

    /**
     * 单资源，删除数据范围
     * @param id
     * @return
     */
    @OperationLog(operation = "数据范围配置接口", content = "单资源，删除数据范围")
    @RepeatFormValidator
    @RequiresPermissions("base:dataScope:delete")
    @RequestMapping(value = "/dataScope/{id}",method = RequestMethod.DELETE)
    public ResponseEntity deleteDataScope(@PathVariable String id){
        logger.info("删除数据范围开始");
        logger.info("用户id:{}",getLoginUser().getId());
        logger.info("数据范围id:{}",id);
        ResponseJsonRender resultData=new ResponseJsonRender();

            int r = apiBaseDataScopePoService.deleteFlagByPrimaryKeyWithUpdateUser(id,getLoginUser().getId());
            if (r <= 0) {
                // 删除失败，可能没有找到资源
                resultData.setCode(ResponseCode.E404_100001.getCode());
                resultData.setMsg(ResponseCode.E404_100001.getMsg());
                logger.info("code:{},msg:{}",resultData.getCode(),resultData.getMsg());
                logger.info("删除数据范围结束，失败");
                return new ResponseEntity(resultData,HttpStatus.NOT_FOUND);
            }else{
                // 删除成功
                logger.info("删除的数据范围id:{}",id);
                logger.info("删除数据范围结束，成功");
                return new ResponseEntity(resultData,HttpStatus.NO_CONTENT);
            }
    }

    /**
     * 单资源，更新数据范围
     * @param id
     * @param updateDataScopeFormDto
     * @return
     */
    @OperationLog(operation = "数据范围配置接口", content = "单资源，更新数据范围")
    @RepeatFormValidator
    @RequiresPermissions("base:dataScope:update")
    @RequestMapping(value = "/dataScope/{id}",method = RequestMethod.PUT)
    public ResponseEntity updateDataScope(@PathVariable String id, UpdateDataScopeFormDto updateDataScopeFormDto){
        logger.info("更新数据范围开始");
        logger.info("当前登录用户id:{}",getLoginUser().getId());
        logger.info("数据范围id:{}",id);
        ResponseJsonRender resultData=new ResponseJsonRender();
        // 表单值设置
        BaseDataScopePo baseDataScopePo = new BaseDataScopePo();
        // id
        baseDataScopePo.setId(id);
        baseDataScopePo.setName(updateDataScopeFormDto.getName());
        baseDataScopePo.setType(updateDataScopeFormDto.getType());
        baseDataScopePo.setDataOfficeId(updateDataScopeFormDto.getDataOfficeId());

        // 用条件更新，乐观锁机制
        BaseDataScopePo baseDataScopePoCondition = new BaseDataScopePo();
        baseDataScopePoCondition.setId(id);
        baseDataScopePoCondition.setDelFlag(BasePo.YesNo.N.name());
        baseDataScopePoCondition.setUpdateAt(updateDataScopeFormDto.getUpdateTime());
        baseDataScopePo = apiBaseDataScopePoService.preUpdate(baseDataScopePo,getLoginUser().getId());
        int r = apiBaseDataScopePoService.updateSelective(baseDataScopePo,baseDataScopePoCondition);
        if (r <= 0) {
            // 更新失败，资源不存在
            resultData.setCode(ResponseCode.E404_100001.getCode());
            resultData.setMsg(ResponseCode.E404_100001.getMsg());
            logger.info("code:{},msg:{}",resultData.getCode(),resultData.getMsg());
            logger.info("更新数据范围结束，失败");
            return new ResponseEntity(resultData,HttpStatus.NOT_FOUND);
        }else{
            // 更新成功，已被成功创建
            logger.info("更新的数据范围id:{}",id);
            logger.info("更新数据范围结束，成功");
            return new ResponseEntity(resultData, HttpStatus.CREATED);
        }
    }

    /**
     * 单资源，获取id数据范围
     * @param id
     * @return
     */
    @OperationLog(operation = "数据范围配置接口", content = "单资源，获取id数据范围")
    @RepeatFormValidator
    @RequiresPermissions("base:dataScope:getById")
    @RequestMapping(value = "/dataScope/{id}",method = RequestMethod.GET)
    public ResponseEntity getById(@PathVariable String id){

        ResponseJsonRender resultData=new ResponseJsonRender();
        BaseDataScopeDto baseDataScopeDto = apiBaseDataScopePoService.selectByPrimaryKey(id,false);
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
     * 复数资源，搜索数据范围
     * @param dto
     * @return
     */
    @OperationLog(operation = "数据范围配置接口", content = "复数资源，搜索数据范围")
    @RepeatFormValidator
    @RequiresPermissions("base:dataScope:search")
    @RequestMapping(value = "/dataScopes",method = RequestMethod.GET)
    public ResponseEntity searchDataScopes(SearchDataScopesConditionDto dto,boolean includeOffice){

        ResponseJsonRender resultData=new ResponseJsonRender();
        PageAndOrderbyParamDto pageAndOrderbyParamDto = new PageAndOrderbyParamDto(PageUtils.getPageFromThreadLocal(), OrderbyUtils.getOrderbyFromThreadLocal());
        // 设置当前登录用户id
        dto.setCurrentUserId(getLoginUser().getId());
        dto.setCurrentRoleId(getLoginUserRoleId());
        PageResultDto<BaseDataScopeDto> list = apiBaseDataScopePoService.searchDataScopesDsf(dto,pageAndOrderbyParamDto);

        if(CollectionUtils.isNotEmpty(list.getData())){
            if(includeOffice){
                //机构
                Map<String,BaseOfficeDto> officeDtoMap = new HashMap<>();
                BaseOfficeDto officeDto = null;
                for (BaseDataScopeDto baseDataScopeDto : list.getData()) {
                    officeDto = apiBaseOfficePoService.selectByPrimaryKey(baseDataScopeDto.getDataOfficeId());
                    if (officeDto != null) {
                        officeDtoMap.put(baseDataScopeDto.getDataOfficeId(),officeDto);
                    }
                }
                if (!officeDtoMap.isEmpty()) {
                    resultData.addData("office",officeDtoMap);
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
