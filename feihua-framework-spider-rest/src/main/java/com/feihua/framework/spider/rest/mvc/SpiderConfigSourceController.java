package com.feihua.framework.spider.rest.mvc;

import com.feihua.framework.base.modules.role.dto.BaseRoleDto;
import com.feihua.framework.rest.ResponseJsonRender;
import com.feihua.framework.rest.interceptor.RepeatFormValidator;
import com.feihua.framework.rest.mvc.SuperController;
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
import com.feihua.framework.spider.dto.SpiderConfigSourceDto;
import com.feihua.framework.spider.dto.SearchSpiderConfigSourcesConditionDto;
import com.feihua.framework.spider.api.ApiSpiderConfigSourcePoService;
import com.feihua.framework.spider.rest.dto.AddSpiderConfigSourceFormDto;
import com.feihua.framework.spider.rest.dto.UpdateSpiderConfigSourceFormDto;
import com.feihua.framework.spider.po.SpiderConfigSourcePo;
/**
 * 内容评论管理
 * Created by yangwei
 */
@RestController
@RequestMapping("/spider")
public class SpiderConfigSourceController extends SuperController {

    private static Logger logger = LoggerFactory.getLogger(SpiderConfigSourceController.class);

    @Autowired
    private ApiSpiderConfigSourcePoService apiSpiderConfigSourceService;

    /**
     * 单资源，添加
     * @param dto
     * @return
     */
    @RepeatFormValidator
    @RequiresPermissions("configSource:add")
    @RequestMapping(value = "/configSource",method = RequestMethod.POST)
    public ResponseEntity add(AddSpiderConfigSourceFormDto dto){
        logger.info("添加内容评论开始");
        logger.info("当前登录用户id:{}",getLoginUser().getId());
        ResponseJsonRender resultData=new ResponseJsonRender();
        // 表单值设置
        SpiderConfigSourcePo basePo = new SpiderConfigSourcePo();
        basePo.setName(dto.getName());
        basePo.setDomain(dto.getDomain());
        basePo.setIsDynamic(dto.getIsDynamic());
        basePo.setDynamicWait(dto.getDynamicWait());


        basePo = apiSpiderConfigSourceService.preInsert(basePo,getLoginUser().getId());
        SpiderConfigSourceDto r = apiSpiderConfigSourceService.insert(basePo);
        if (r == null) {
            // 添加失败
            resultData.setCode(ResponseCode.E404_100001.getCode());
            resultData.setMsg(ResponseCode.E404_100001.getMsg());
            logger.info("code:{},msg:{}",resultData.getCode(),resultData.getMsg());
            logger.info("添加内容评论结束，失败");
            return new ResponseEntity(resultData,HttpStatus.NOT_FOUND);
        }else{
            // 添加成功，返回添加的数据
            resultData.setData(r);
            logger.info("添加内容评论id:{}",r.getId());
            logger.info("添加内容评论结束，成功");
            return new ResponseEntity(resultData, HttpStatus.CREATED);
        }
    }

    /**
     * 单资源，删除
     * @param id
     * @return
     */
    @RepeatFormValidator
    @RequiresPermissions("configSource:delete")
    @RequestMapping(value = "/configSource/{id}",method = RequestMethod.DELETE)
    public ResponseEntity delete(@PathVariable String id){
        logger.info("删除内容评论开始");
        logger.info("用户id:{}",getLoginUser().getId());
        logger.info("内容评论id:{}",id);
        ResponseJsonRender resultData=new ResponseJsonRender();

            int r = apiSpiderConfigSourceService.deleteFlagByPrimaryKeyWithUpdateUser(id,getLoginUser().getId());
            if (r <= 0) {
                // 删除失败，可能没有找到资源
                resultData.setCode(ResponseCode.E404_100001.getCode());
                resultData.setMsg(ResponseCode.E404_100001.getMsg());
                logger.info("code:{},msg:{}",resultData.getCode(),resultData.getMsg());
                logger.info("删除内容评论结束，失败");
                return new ResponseEntity(resultData,HttpStatus.NOT_FOUND);
            }else{
                // 删除成功
                logger.info("删除的内容评论id:{}",id);
                logger.info("删除内容评论结束，成功");
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
    @RequiresPermissions("configSource:update")
    @RequestMapping(value = "/configSource/{id}",method = RequestMethod.PUT)
    public ResponseEntity update(@PathVariable String id, UpdateSpiderConfigSourceFormDto dto){
        logger.info("更新内容评论开始");
        logger.info("当前登录用户id:{}",getLoginUser().getId());
        logger.info("内容评论id:{}",id);
        ResponseJsonRender resultData=new ResponseJsonRender();
        // 表单值设置
        SpiderConfigSourcePo basePo = new SpiderConfigSourcePo();
        // id
        basePo.setId(id);
        basePo.setName(dto.getName());
        basePo.setDomain(dto.getDomain());
        basePo.setIsDynamic(dto.getIsDynamic());
        basePo.setDynamicWait(dto.getDynamicWait());


        // 用条件更新，乐观锁机制
        SpiderConfigSourcePo basePoCondition = new SpiderConfigSourcePo();
        basePoCondition.setId(id);
        basePoCondition.setDelFlag(BasePo.YesNo.N.name());
        basePoCondition.setUpdateAt(dto.getUpdateTime());
        basePo = apiSpiderConfigSourceService.preUpdate(basePo,getLoginUser().getId());
        int r = apiSpiderConfigSourceService.updateSelective(basePo,basePoCondition);
        if (r <= 0) {
            // 更新失败，资源不存在
            resultData.setCode(ResponseCode.E404_100001.getCode());
            resultData.setMsg(ResponseCode.E404_100001.getMsg());
            logger.info("code:{},msg:{}",resultData.getCode(),resultData.getMsg());
            logger.info("更新内容评论结束，失败");
            return new ResponseEntity(resultData,HttpStatus.NOT_FOUND);
        }else{
            // 更新成功，已被成功创建
            logger.info("更新的内容评论id:{}",id);
            logger.info("更新内容评论结束，成功");
            return new ResponseEntity(resultData, HttpStatus.CREATED);
        }
    }

    /**
     * 单资源，获取id内容评论
     * @param id
     * @return
     */
    @RequiresPermissions("configSource:getById")
    @RequestMapping(value = "/configSource/{id}",method = RequestMethod.GET)
    public ResponseEntity getById(@PathVariable String id){

        ResponseJsonRender resultData=new ResponseJsonRender();
        SpiderConfigSourceDto baseDataScopeDto = apiSpiderConfigSourceService.selectByPrimaryKey(id,false);
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
     * 复数资源，搜索内容评论
     * @param dto
     * @return
     */
    @RequiresPermissions("configSource:search")
    @RequestMapping(value = "/configSources",method = RequestMethod.GET)
    public ResponseEntity search(SearchSpiderConfigSourcesConditionDto dto){

        ResponseJsonRender resultData=new ResponseJsonRender();
        PageAndOrderbyParamDto pageAndOrderbyParamDto = new PageAndOrderbyParamDto(PageUtils.getPageFromThreadLocal(), OrderbyUtils.getOrderbyFromThreadLocal());
        // 设置当前登录用户id
        dto.setCurrentUserId(getLoginUser().getId());
        dto.setCurrentRoleId(((BaseRoleDto) getLoginUser().getRole()).getId());
        PageResultDto<SpiderConfigSourceDto> list = apiSpiderConfigSourceService.searchSpiderConfigSourcesDsf(dto,pageAndOrderbyParamDto);

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
