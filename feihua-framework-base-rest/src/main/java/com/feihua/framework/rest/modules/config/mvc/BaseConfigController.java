package com.feihua.framework.rest.modules.config.mvc;

import com.feihua.framework.base.modules.config.api.ApiBaseConfigService;
import com.feihua.framework.base.modules.config.dto.BaseConfigDto;
import com.feihua.framework.base.modules.config.dto.SearchBaseConfigsConditionDto;
import com.feihua.framework.base.modules.config.po.BaseConfig;
import com.feihua.framework.base.modules.oss.cloud.dto.CloudStorageConfig;
import com.feihua.framework.base.modules.role.dto.BaseRoleDto;
import com.feihua.framework.rest.ResponseJsonRender;
import com.feihua.framework.rest.interceptor.RepeatFormValidator;
import com.feihua.framework.rest.modules.common.mvc.BaseController;
import com.feihua.framework.rest.modules.config.dto.AddBaseConfig;
import com.feihua.framework.rest.modules.config.dto.UpdateBaseConfig;
import com.feihua.utils.http.httpServletResponse.ResponseCode;
import com.google.gson.Gson;
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
import org.springframework.web.bind.annotation.*;

/**
 * 系统配置
 * Created by yangwei
 */
@RestController
@RequestMapping("/base")
public class BaseConfigController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(BaseConfigController.class);

    @Autowired
    private ApiBaseConfigService apiBaseConfigService;

    /**
     * 单资源，添加
     * @param dto
     * @return
     */
    @RepeatFormValidator
    @RequiresPermissions("base:config:add")
    @RequestMapping(value = "/config",method = RequestMethod.POST)
    public ResponseEntity add(@RequestBody AddBaseConfig dto){
        logger.info("添加系统配置开始");
        logger.info("当前登录用户id:{}",getLoginUser().getId());
        ResponseJsonRender resultData=new ResponseJsonRender();
        // 表单值设置
        BaseConfig basePo = new BaseConfig();
        basePo.setId(dto.getId());
        basePo.setConfigKey(dto.getConfigKey());
        basePo.setConfigValue(dto.getConfigValue());
        basePo.setDescription(dto.getDescription());
        basePo.setStatus(dto.getStatus());
        basePo.setDataUserId(dto.getDataUserId());
        basePo.setDataOfficeId(dto.getDataOfficeId());
        basePo.setDataType(dto.getDataType());
        basePo.setDataAreaId(dto.getDataAreaId());
        basePo.setDelFlag(dto.getDelFlag());
        basePo.setCreateAt(dto.getCreateAt());
        basePo.setCreateBy(dto.getCreateBy());
        basePo.setUpdateAt(dto.getUpdateAt());
        basePo.setUpdateBy(dto.getUpdateBy());

        basePo = apiBaseConfigService.preInsert(basePo,getLoginUser().getId());
        BaseConfigDto r = apiBaseConfigService.insert(basePo);
        if (r == null) {
            // 添加失败
            resultData.setCode(ResponseCode.E404_100001.getCode());
            resultData.setMsg(ResponseCode.E404_100001.getMsg());
            logger.info("code:{},msg:{}",resultData.getCode(),resultData.getMsg());
            logger.info("添加系统配置结束，失败");
            return new ResponseEntity(resultData,HttpStatus.NOT_FOUND);
        }else{
            // 添加成功，返回添加的数据
            resultData.setData(r);
            logger.info("添加系统配置id:{}",r.getId());
            logger.info("添加系统配置结束，成功");
            return new ResponseEntity(resultData, HttpStatus.CREATED);
        }
    }

    /**
     * 单资源，删除
     * @param id
     * @return
     */
    @RepeatFormValidator
    @RequiresPermissions("base:config:delete")
    @RequestMapping(value = "/config/{id}",method = RequestMethod.DELETE)
    public ResponseEntity delete(@PathVariable String id){
        logger.info("删除系统配置开始");
        logger.info("用户id:{}",getLoginUser().getId());
        logger.info("系统配置id:{}",id);
        ResponseJsonRender resultData=new ResponseJsonRender();

            int r = apiBaseConfigService.deleteFlagByPrimaryKeyWithUpdateUser(id,getLoginUser().getId());
            if (r <= 0) {
                // 删除失败，可能没有找到资源
                resultData.setCode(ResponseCode.E404_100001.getCode());
                resultData.setMsg(ResponseCode.E404_100001.getMsg());
                logger.info("code:{},msg:{}",resultData.getCode(),resultData.getMsg());
                logger.info("删除系统配置结束，失败");
                return new ResponseEntity(resultData,HttpStatus.NOT_FOUND);
            }else{
                // 删除成功
                logger.info("删除的系统配置id:{}",id);
                logger.info("删除系统配置结束，成功");
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
    @RequiresPermissions("base:config:update")
    @RequestMapping(value = "/config/{id}",method = RequestMethod.PUT)
    public ResponseEntity update(@PathVariable String id,@RequestBody UpdateBaseConfig dto){
        logger.info("更新系统配置开始");
        logger.info("当前登录用户id:{}",getLoginUser().getId());
        logger.info("系统配置id:{}",id);
        ResponseJsonRender resultData=new ResponseJsonRender();
        // 表单值设置
        BaseConfig basePo = new BaseConfig();
        // id
        basePo.setId(id);
        basePo.setId(dto.getId());
        basePo.setConfigKey(dto.getConfigKey());
        basePo.setConfigValue(dto.getConfigValue());
        basePo.setDescription(dto.getDescription());
        basePo.setStatus(dto.getStatus());
        basePo.setDataUserId(dto.getDataUserId());
        basePo.setDataOfficeId(dto.getDataOfficeId());
        basePo.setDataType(dto.getDataType());
        basePo.setDataAreaId(dto.getDataAreaId());
        basePo.setDelFlag(dto.getDelFlag());
        basePo.setCreateAt(dto.getCreateAt());
        basePo.setCreateBy(dto.getCreateBy());
        basePo.setUpdateAt(dto.getUpdateAt());
        basePo.setUpdateBy(dto.getUpdateBy());

        // 用条件更新，乐观锁机制
        BaseConfig basePoCondition = new BaseConfig();
        basePoCondition.setId(id);
        basePoCondition.setDelFlag(BasePo.YesNo.N.name());
        basePoCondition.setUpdateAt(dto.getUpdateTime());
        basePo = apiBaseConfigService.preUpdate(basePo,getLoginUser().getId());
        int r = apiBaseConfigService.updateSelective(basePo,basePoCondition);
        if (r <= 0) {
            // 更新失败，资源不存在
            resultData.setCode(ResponseCode.E404_100001.getCode());
            resultData.setMsg(ResponseCode.E404_100001.getMsg());
            logger.info("code:{},msg:{}",resultData.getCode(),resultData.getMsg());
            logger.info("更新系统配置结束，失败");
            return new ResponseEntity(resultData,HttpStatus.NOT_FOUND);
        }else{
            // 更新成功，已被成功创建
            logger.info("更新的系统配置id:{}",id);
            logger.info("更新系统配置结束，成功");
            return new ResponseEntity(resultData, HttpStatus.CREATED);
        }
    }

    /**
     * 单资源，获取id系统配置
     * @param id
     * @return
     */
    @RequiresPermissions("base:config:getById")
    @RequestMapping(value = "/config/{id}",method = RequestMethod.GET)
    public ResponseEntity getById(@PathVariable String id){

        ResponseJsonRender resultData=new ResponseJsonRender();
        BaseConfigDto baseDataScopeDto = apiBaseConfigService.selectByPrimaryKey(id,false);
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
     * 单资源，获取id系统配置
     * @param key
     * @return
     */
    @RequiresPermissions("base:config:getOssCofing")
    @RequestMapping(value = "/config/ossConfig/{key}",method = RequestMethod.GET)
    public ResponseEntity getOssCofingByKey(@PathVariable String key){

        ResponseJsonRender resultData=new ResponseJsonRender();
        BaseConfig query = new BaseConfig();
        query.setConfigKey(key);
        query.setDelFlag(BasePo.YesNo.N.name());
        CloudStorageConfig configObject = apiBaseConfigService.getConfigObject(key, CloudStorageConfig.class);
        if(configObject != null){
            resultData.setData(configObject);
            return new ResponseEntity(resultData, HttpStatus.OK);
        }else{
            resultData.setCode(ResponseCode.E404_100001.getCode());
            resultData.setMsg(ResponseCode.E404_100001.getMsg());
            return new ResponseEntity(resultData,HttpStatus.NOT_FOUND);
        }
    }


    /**
     * 单资源，获取id系统配置
     * @param id
     * @return
     */
    @RepeatFormValidator
    @RequiresPermissions("base:config:saveOssCofing")
    @RequestMapping(value = "/config/saveOssConfig/{id}",method = RequestMethod.PUT)
    public ResponseEntity saveOssConfig(@PathVariable String id,@RequestBody CloudStorageConfig config){

        ResponseJsonRender resultData=new ResponseJsonRender();
        BaseConfig baseConfig = new BaseConfig();
        baseConfig.setId(id);
        baseConfig.setConfigValue(new Gson().toJson(config));
        int r = apiBaseConfigService.updateByPrimaryKeySelective(baseConfig);
        if (r <= 0) {
            // 更新失败，资源不存在
            resultData.setCode(ResponseCode.E404_100001.getCode());
            resultData.setMsg(ResponseCode.E404_100001.getMsg());
            logger.info("code:{},msg:{}",resultData.getCode(),resultData.getMsg());
            logger.info("更新系统配置结束，失败");
            return new ResponseEntity(resultData,HttpStatus.NOT_FOUND);
        }else{
            // 更新成功，已被成功创建
            logger.info("更新的系统配置id:{}",id);
            logger.info("更新系统配置结束，成功");
            return new ResponseEntity(resultData, HttpStatus.CREATED);
        }
    }

    /**
     * 复数资源，搜索系统配置
     * @param dto
     * @return
     */
    @RequiresPermissions("base:config:search")
    @RequestMapping(value = "/configs",method = RequestMethod.GET)
    public ResponseEntity search(SearchBaseConfigsConditionDto dto){

        ResponseJsonRender resultData=new ResponseJsonRender();
        PageAndOrderbyParamDto pageAndOrderbyParamDto = new PageAndOrderbyParamDto(PageUtils.getPageFromThreadLocal(), OrderbyUtils.getOrderbyFromThreadLocal());
        // 设置当前登录用户id
        dto.setCurrentUserId(getLoginUser().getId());
        dto.setCurrentRoleId(getLoginUserRoleId());
        dto.setCurrentPostId(getLoginUserPostId());
        PageResultDto<BaseConfigDto> list = apiBaseConfigService.searchBaseConfigsDsf(dto,pageAndOrderbyParamDto);

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
