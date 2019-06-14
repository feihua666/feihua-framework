package com.feihua.framework.cms.admin.rest.mvc;

import com.feihua.framework.base.modules.role.dto.BaseRoleDto;
import com.feihua.framework.cms.admin.rest.dto.AddCmsContentCategoryFormDto;
import com.feihua.framework.cms.admin.rest.dto.UpdateCmsContentCategoryFormDto;
import com.feihua.framework.cms.api.ApiCmsChannelPoService;
import com.feihua.framework.cms.api.ApiCmsContentCategoryPoService;
import com.feihua.framework.cms.api.ApiCmsSitePoService;
import com.feihua.framework.cms.dto.CmsChannelDto;
import com.feihua.framework.cms.dto.CmsContentCategoryDto;
import com.feihua.framework.cms.dto.CmsSiteDto;
import com.feihua.framework.cms.dto.SearchCmsContentCategorysConditionDto;
import com.feihua.framework.cms.po.CmsContentCategoryPo;
import com.feihua.framework.rest.ResponseJsonRender;
import com.feihua.framework.rest.interceptor.RepeatFormValidator;
import com.feihua.utils.http.httpServletResponse.ResponseCode;
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
import java.util.Map;

/**
 * 内容分类管理
 * Created by yangwei
 */
@RestController
@RequestMapping("/cms")
public class CmsContentCategoryController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(CmsContentCategoryController.class);

    @Autowired
    private ApiCmsContentCategoryPoService apiCmsContentCategoryPoService;
    @Autowired
    private ApiCmsSitePoService apiCmsSitePoService;
    @Autowired
    private ApiCmsChannelPoService apiCmsChannelPoService;

    /**
     * 单资源，添加
     * @param dto
     * @return
     */
    @RepeatFormValidator
    @RequiresPermissions("contentCategory:add")
    @RequestMapping(value = "/content/category",method = RequestMethod.POST)
    public ResponseEntity add(AddCmsContentCategoryFormDto dto){
        logger.info("添加内容分类开始");
        logger.info("当前登录用户id:{}",getLoginUser().getId());
        ResponseJsonRender resultData=new ResponseJsonRender();
        // 表单值设置
        CmsContentCategoryPo basePo = new CmsContentCategoryPo();
        
        basePo.setName(dto.getName());
        basePo.setSiteId(dto.getSiteId());
        basePo.setDescription(dto.getDescription());
        basePo.setImageUrl(dto.getImageUrl());
        basePo.setImageDes(dto.getImageDes());
        basePo.setSequence(dto.getSequence());
        basePo.setParentId(dto.getParentId());
        basePo.setChannelId(dto.getChannelId());


        basePo = apiCmsContentCategoryPoService.preInsert(basePo,getLoginUser().getId());
        CmsContentCategoryDto r = apiCmsContentCategoryPoService.insert(basePo);
        if (r == null) {
            // 添加失败
            resultData.setCode(ResponseCode.E404_100001.getCode());
            resultData.setMsg(ResponseCode.E404_100001.getMsg());
            logger.info("code:{},msg:{}",resultData.getCode(),resultData.getMsg());
            logger.info("添加内容分类结束，失败");
            return new ResponseEntity(resultData,HttpStatus.NOT_FOUND);
        }else{
            // 添加成功，返回添加的数据
            resultData.setData(r);
            logger.info("添加内容分类id:{}",r.getId());
            logger.info("添加内容分类结束，成功");
            return new ResponseEntity(resultData, HttpStatus.CREATED);
        }
    }

    /**
     * 单资源，删除
     * @param id
     * @return
     */
    @RepeatFormValidator
    @RequiresPermissions("contentCategory:delete")
    @RequestMapping(value = "/content/category/{id}",method = RequestMethod.DELETE)
    public ResponseEntity delete(@PathVariable String id){
        logger.info("删除内容分类开始");
        logger.info("用户id:{}",getLoginUser().getId());
        logger.info("内容分类id:{}",id);
        ResponseJsonRender resultData=new ResponseJsonRender();

            int r = apiCmsContentCategoryPoService.deleteFlagByPrimaryKeyWithUpdateUser(id,getLoginUser().getId());
            if (r <= 0) {
                // 删除失败，可能没有找到资源
                resultData.setCode(ResponseCode.E404_100001.getCode());
                resultData.setMsg(ResponseCode.E404_100001.getMsg());
                logger.info("code:{},msg:{}",resultData.getCode(),resultData.getMsg());
                logger.info("删除内容分类结束，失败");
                return new ResponseEntity(resultData,HttpStatus.NOT_FOUND);
            }else{
                // 删除成功
                logger.info("删除的内容分类id:{}",id);
                logger.info("删除内容分类结束，成功");
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
    @RequiresPermissions("contentCategory:update")
    @RequestMapping(value = "/content/category/{id}",method = RequestMethod.PUT)
    public ResponseEntity update(@PathVariable String id, UpdateCmsContentCategoryFormDto dto){
        logger.info("更新内容分类开始");
        logger.info("当前登录用户id:{}",getLoginUser().getId());
        logger.info("内容分类id:{}",id);
        ResponseJsonRender resultData=new ResponseJsonRender();
        // 表单值设置
        CmsContentCategoryPo basePo = new CmsContentCategoryPo();
        // id
        basePo.setId(id);
        basePo.setName(dto.getName());
        basePo.setSiteId(dto.getSiteId());
        basePo.setDescription(dto.getDescription());
        basePo.setImageUrl(dto.getImageUrl());
        basePo.setImageDes(dto.getImageDes());
        basePo.setSequence(dto.getSequence());
        basePo.setParentId(dto.getParentId());
        basePo.setChannelId(dto.getChannelId());

        // 用条件更新，乐观锁机制
        CmsContentCategoryPo basePoCondition = new CmsContentCategoryPo();
        basePoCondition.setId(id);
        basePoCondition.setDelFlag(BasePo.YesNo.N.name());
        basePoCondition.setUpdateAt(dto.getUpdateTime());
        basePo = apiCmsContentCategoryPoService.preUpdate(basePo,getLoginUser().getId());
        int r = apiCmsContentCategoryPoService.updateSelective(basePo,basePoCondition);
        if (r <= 0) {
            // 更新失败，资源不存在
            resultData.setCode(ResponseCode.E404_100001.getCode());
            resultData.setMsg(ResponseCode.E404_100001.getMsg());
            logger.info("code:{},msg:{}",resultData.getCode(),resultData.getMsg());
            logger.info("更新内容分类结束，失败");
            return new ResponseEntity(resultData,HttpStatus.NOT_FOUND);
        }else{
            // 更新成功，已被成功创建
            logger.info("更新的内容分类id:{}",id);
            logger.info("更新内容分类结束，成功");
            return new ResponseEntity(resultData, HttpStatus.CREATED);
        }
    }

    /**
     * 单资源，获取id内容分类
     * @param id
     * @return
     */
    @RequiresPermissions("contentCategory:getById")
    @RequestMapping(value = "/content/category/{id}",method = RequestMethod.GET)
    public ResponseEntity getById(@PathVariable String id){

        ResponseJsonRender resultData=new ResponseJsonRender();
        CmsContentCategoryDto baseDataScopeDto = apiCmsContentCategoryPoService.selectByPrimaryKey(id,false);
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
     * 复数资源，搜索内容分类
     * @param dto
     * @return
     */
    @RequiresPermissions("contentCategory:search")
    @RequestMapping(value = "/content/categorys",method = RequestMethod.GET)
    public ResponseEntity search(SearchCmsContentCategorysConditionDto dto,boolean includeSite,boolean includeChannel,boolean includeParent){

        ResponseJsonRender resultData=new ResponseJsonRender();
        PageAndOrderbyParamDto pageAndOrderbyParamDto = new PageAndOrderbyParamDto(PageUtils.getPageFromThreadLocal(), OrderbyUtils.getOrderbyFromThreadLocal());
        // 设置当前登录用户id
        dto.setCurrentUserId(getLoginUser().getId());
        dto.setCurrentRoleId(getLoginUserRoleId());
        dto.setCurrentPostId(getLoginUserPostId());
        PageResultDto<CmsContentCategoryDto> list = apiCmsContentCategoryPoService.searchCmsContentCategorysDsf(dto,pageAndOrderbyParamDto);

        if(list.getData() != null && !list.getData().isEmpty()){

            if ( includeSite || includeChannel || includeParent) {
                Map<String, CmsSiteDto> siteDtoMap = new HashMap<>();
                Map<String, CmsChannelDto> channelDtoMap = new HashMap<>();
                Map<String, CmsContentCategoryDto> categoryParentDtoMap = new HashMap<>();
                CmsSiteDto siteDto = null;
                CmsChannelDto channelDto = null;
                CmsContentCategoryDto categoryParentDto = null;
                for (CmsContentCategoryDto cmsContentCategoryDto : list.getData()) {
                    if(includeSite && StringUtils.isNotEmpty(cmsContentCategoryDto.getSiteId())){
                        siteDto = apiCmsSitePoService.selectByPrimaryKey(cmsContentCategoryDto.getSiteId());
                        if (siteDto != null) {
                            siteDtoMap.put(cmsContentCategoryDto.getSiteId(),siteDto);
                        }
                    }
                    if(includeChannel && StringUtils.isNotEmpty(cmsContentCategoryDto.getChannelId())){
                        channelDto = apiCmsChannelPoService.selectByPrimaryKey(cmsContentCategoryDto.getChannelId());
                        if (channelDto != null) {
                            channelDtoMap.put(cmsContentCategoryDto.getChannelId(),channelDto);
                        }
                    }
                    if(includeParent && StringUtils.isNotEmpty(cmsContentCategoryDto.getParentId())){
                        categoryParentDto = apiCmsContentCategoryPoService.selectByPrimaryKey(cmsContentCategoryDto.getParentId());
                        if (categoryParentDto != null) {
                            categoryParentDtoMap.put(cmsContentCategoryDto.getParentId(),categoryParentDto);
                        }
                    }
                }

                if (!siteDtoMap.isEmpty()) {
                    resultData.addData("site",siteDtoMap);
                }
                if (!channelDtoMap.isEmpty()) {
                    resultData.addData("channel",channelDtoMap);
                }
                if (!categoryParentDtoMap.isEmpty()) {
                    resultData.addData("parent",categoryParentDtoMap);
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
