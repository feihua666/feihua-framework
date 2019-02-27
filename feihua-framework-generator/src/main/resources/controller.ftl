package com.feihua.framework.rest.modules.${moduleName}.mvc;

import com.feihua.framework.base.modules.role.dto.BaseRoleDto;
import com.feihua.framework.rest.ResponseJsonRender;
import com.feihua.framework.rest.interceptor.RepeatFormValidator;
import com.feihua.framework.rest.modules.common.mvc.BaseController;
import com.feihua.utils.http.httpServletResponse.ResponseCode;
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
<#list importList as item>
import ${item};
</#list>
/**
 * ${controllerComment}
 * Created by yangwei
 */
@RestController
@RequestMapping("${classMappingPath}")
public class ${controllerName} extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(${controllerName}.class);

    @Autowired
    private ${serviceApiName} ${serviceApiVarName};

    /**
     * 单资源，添加
     * @param dto
     * @return
     */
    @RepeatFormValidator
    @RequiresPermissions("${methodRequiresPermissionsPre}add")
    @RequestMapping(value = "${methodMappingPath}",method = RequestMethod.POST)
    public ResponseEntity add(${addFormDto} dto){
        logger.info("添加${moduleComment}开始");
        logger.info("当前登录用户id:{}",getLoginUser().getId());
        ResponseJsonRender resultData=new ResponseJsonRender();
        // 表单值设置
        ${modelName} basePo = new ${modelName}();
        <#list formAttrSet as item>
        ${item}
        </#list>

        basePo = ${serviceApiVarName}.preInsert(basePo,getLoginUser().getId());
        ${dtoName} r = ${serviceApiVarName}.insert(basePo);
        if (r == null) {
            // 添加失败
            resultData.setCode(ResponseCode.E404_100001.getCode());
            resultData.setMsg(ResponseCode.E404_100001.getMsg());
            logger.info("code:{},msg:{}",resultData.getCode(),resultData.getMsg());
            logger.info("添加${moduleComment}结束，失败");
            return new ResponseEntity(resultData,HttpStatus.NOT_FOUND);
        }else{
            // 添加成功，返回添加的数据
            resultData.setData(r);
            logger.info("添加${moduleComment}id:{}",r.getId());
            logger.info("添加${moduleComment}结束，成功");
            return new ResponseEntity(resultData, HttpStatus.CREATED);
        }
    }

    /**
     * 单资源，删除
     * @param id
     * @return
     */
    @RepeatFormValidator
    @RequiresPermissions("${methodRequiresPermissionsPre}delete")
    @RequestMapping(value = "${methodMappingPath}/{id}",method = RequestMethod.DELETE)
    public ResponseEntity delete(@PathVariable String id){
        logger.info("删除${moduleComment}开始");
        logger.info("用户id:{}",getLoginUser().getId());
        logger.info("${moduleComment}id:{}",id);
        ResponseJsonRender resultData=new ResponseJsonRender();

            int r = ${serviceApiVarName}.deleteFlagByPrimaryKeyWithUpdateUser(id,getLoginUser().getId());
            if (r <= 0) {
                // 删除失败，可能没有找到资源
                resultData.setCode(ResponseCode.E404_100001.getCode());
                resultData.setMsg(ResponseCode.E404_100001.getMsg());
                logger.info("code:{},msg:{}",resultData.getCode(),resultData.getMsg());
                logger.info("删除${moduleComment}结束，失败");
                return new ResponseEntity(resultData,HttpStatus.NOT_FOUND);
            }else{
                // 删除成功
                logger.info("删除的${moduleComment}id:{}",id);
                logger.info("删除${moduleComment}结束，成功");
                return new ResponseEntity(resultData,HttpStatus.NO_CONTENT);
            }
    }

    /**
     * 单资源，更新
     * @param id
     * @param dto
     * @return
     */
    @RepeatFormValidator
    @RequiresPermissions("${methodRequiresPermissionsPre}update")
    @RequestMapping(value = "${methodMappingPath}/{id}",method = RequestMethod.PUT)
    public ResponseEntity update(@PathVariable String id, ${updateFormDto} dto){
        logger.info("更新${moduleComment}开始");
        logger.info("当前登录用户id:{}",getLoginUser().getId());
        logger.info("${moduleComment}id:{}",id);
        ResponseJsonRender resultData=new ResponseJsonRender();
        // 表单值设置
        ${modelName} basePo = new ${modelName}();
        // id
        basePo.setId(id);
        <#list formAttrSet as item>
        ${item}
        </#list>

        // 用条件更新，乐观锁机制
        ${modelName} basePoCondition = new ${modelName}();
        basePoCondition.setId(id);
        basePoCondition.setDelFlag(BasePo.YesNo.N.name());
        basePoCondition.setUpdateAt(dto.getUpdateTime());
        basePo = ${serviceApiVarName}.preUpdate(basePo,getLoginUser().getId());
        int r = ${serviceApiVarName}.updateSelective(basePo,basePoCondition);
        if (r <= 0) {
            // 更新失败，资源不存在
            resultData.setCode(ResponseCode.E404_100001.getCode());
            resultData.setMsg(ResponseCode.E404_100001.getMsg());
            logger.info("code:{},msg:{}",resultData.getCode(),resultData.getMsg());
            logger.info("更新${moduleComment}结束，失败");
            return new ResponseEntity(resultData,HttpStatus.NOT_FOUND);
        }else{
            // 更新成功，已被成功创建
            logger.info("更新的${moduleComment}id:{}",id);
            logger.info("更新${moduleComment}结束，成功");
            return new ResponseEntity(resultData, HttpStatus.CREATED);
        }
    }

    /**
     * 单资源，获取id${moduleComment}
     * @param id
     * @return
     */
    @RepeatFormValidator
    @RequiresPermissions("${methodRequiresPermissionsPre}getById")
    @RequestMapping(value = "${methodMappingPath}/{id}",method = RequestMethod.GET)
    public ResponseEntity getById(@PathVariable String id){

        ResponseJsonRender resultData=new ResponseJsonRender();
        ${dtoName} baseDataScopeDto = ${serviceApiVarName}.selectByPrimaryKey(id,false);
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
     * 复数资源，搜索${moduleComment}
     * @param dto
     * @return
     */
    @RepeatFormValidator
    @RequiresPermissions("${methodRequiresPermissionsPre}search")
    @RequestMapping(value = "${methodMappingPath}s",method = RequestMethod.GET)
    public ResponseEntity search(${searchConditionDtoName} dto){

        ResponseJsonRender resultData=new ResponseJsonRender();
        PageAndOrderbyParamDto pageAndOrderbyParamDto = new PageAndOrderbyParamDto(PageUtils.getPageFromThreadLocal(), OrderbyUtils.getOrderbyFromThreadLocal());
        // 设置当前登录用户id
        dto.setCurrentUserId(getLoginUser().getId());
        dto.setCurrentRoleId(((BaseRoleDto) getLoginUser().getRole()).getId());
        PageResultDto<${dtoName}> list = ${serviceApiVarName}.${searchDsfMethodName}(dto,pageAndOrderbyParamDto);

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
