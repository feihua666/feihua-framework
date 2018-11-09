package com.feihua.framework.cms.admin.rest.mvc;

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
import com.feihua.framework.cms.dto.CmsContentDto;
import com.feihua.framework.cms.dto.SearchCmsContentsConditionDto;
import com.feihua.framework.cms.api.ApiCmsContentPoService;
import com.feihua.framework.cms.admin.rest.dto.AddCmsContentFormDto;
import com.feihua.framework.cms.admin.rest.dto.UpdateCmsContentFormDto;
import com.feihua.framework.cms.po.CmsContentPo;
/**
 * 内容管理
 * Created by yangwei
 */
@RestController
@RequestMapping("/cms")
public class CmsContentController extends SuperController {

    private static Logger logger = LoggerFactory.getLogger(CmsContentController.class);

    @Autowired
    private ApiCmsContentPoService apiCmsContentPoService;

    /**
     * 单资源，添加
     * @param dto
     * @return
     */
    @RepeatFormValidator
    @RequiresPermissions("content:add")
    @RequestMapping(value = "/content",method = RequestMethod.POST)
    public ResponseEntity add(AddCmsContentFormDto dto){
        logger.info("添加内容开始");
        logger.info("当前登录用户id:{}",getLoginUser().getId());
        ResponseJsonRender resultData=new ResponseJsonRender();
        // 表单值设置
        CmsContentPo basePo = new CmsContentPo();
        basePo.setTitle(dto.getTitle());
        basePo.setAuthor(dto.getAuthor());
        basePo.setStatus(dto.getStatus());
        basePo.setSiteId(dto.getSiteId());
        basePo.setChannelId(dto.getChannelId());

        basePo.setContent(dto.getContent());

        apiCmsContentPoService.preInsert(basePo,getLoginUser().getId());
        CmsContentDto r = apiCmsContentPoService.insert(basePo);
        if (r == null) {
            // 添加失败
            resultData.setCode(ResponseCode.E404_100001.getCode());
            resultData.setMsg(ResponseCode.E404_100001.getMsg());
            logger.info("code:{},msg:{}",resultData.getCode(),resultData.getMsg());
            logger.info("添加内容结束，失败");
            return new ResponseEntity(resultData,HttpStatus.NOT_FOUND);
        }else{
            // 添加成功，返回添加的数据
            resultData.setData(r);
            logger.info("添加内容id:{}",r.getId());
            logger.info("添加内容结束，成功");
            return new ResponseEntity(resultData, HttpStatus.CREATED);
        }
    }

    /**
     * 单资源，删除
     * @param id
     * @return
     */
    @RepeatFormValidator
    @RequiresPermissions("content:delete")
    @RequestMapping(value = "/content/{id}",method = RequestMethod.DELETE)
    public ResponseEntity delete(@PathVariable String id){
        logger.info("删除内容开始");
        logger.info("用户id:{}",getLoginUser().getId());
        logger.info("内容id:{}",id);
        ResponseJsonRender resultData=new ResponseJsonRender();

            int r = apiCmsContentPoService.deleteFlagByPrimaryKeyWithUpdateUser(id,getLoginUser().getId());
            if (r <= 0) {
                // 删除失败，可能没有找到资源
                resultData.setCode(ResponseCode.E404_100001.getCode());
                resultData.setMsg(ResponseCode.E404_100001.getMsg());
                logger.info("code:{},msg:{}",resultData.getCode(),resultData.getMsg());
                logger.info("删除内容结束，失败");
                return new ResponseEntity(resultData,HttpStatus.NOT_FOUND);
            }else{
                // 删除成功
                logger.info("删除的内容id:{}",id);
                logger.info("删除内容结束，成功");
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
    @RequiresPermissions("content:update")
    @RequestMapping(value = "/content/{id}",method = RequestMethod.PUT)
    public ResponseEntity update(@PathVariable String id, UpdateCmsContentFormDto dto){
        logger.info("更新内容开始");
        logger.info("当前登录用户id:{}",getLoginUser().getId());
        logger.info("内容id:{}",id);
        ResponseJsonRender resultData=new ResponseJsonRender();
        // 表单值设置
        CmsContentPo basePo = new CmsContentPo();
        // id
        basePo.setId(id);
        basePo.setTitle(dto.getTitle());
        basePo.setAuthor(dto.getAuthor());
        basePo.setStatus(dto.getStatus());
        basePo.setSiteId(dto.getSiteId());
        basePo.setChannelId(dto.getChannelId());

        basePo.setContent(dto.getContent());

        // 用条件更新，乐观锁机制
        CmsContentPo basePoCondition = new CmsContentPo();
        basePoCondition.setId(id);
        basePoCondition.setDelFlag(BasePo.YesNo.N.name());
        basePoCondition.setUpdateAt(dto.getUpdateTime());
        apiCmsContentPoService.preUpdate(basePo,getLoginUser().getId());
        int r = apiCmsContentPoService.updateSelective(basePo,basePoCondition);
        if (r <= 0) {
            // 更新失败，资源不存在
            resultData.setCode(ResponseCode.E404_100001.getCode());
            resultData.setMsg(ResponseCode.E404_100001.getMsg());
            logger.info("code:{},msg:{}",resultData.getCode(),resultData.getMsg());
            logger.info("更新内容结束，失败");
            return new ResponseEntity(resultData,HttpStatus.NOT_FOUND);
        }else{
            // 更新成功，已被成功创建
            logger.info("更新的内容id:{}",id);
            logger.info("更新内容结束，成功");
            return new ResponseEntity(resultData, HttpStatus.CREATED);
        }
    }

    /**
     * 单资源，获取id内容
     * @param id
     * @return
     */
    @RepeatFormValidator
    @RequiresPermissions("content:getById")
    @RequestMapping(value = "/content/{id}",method = RequestMethod.GET)
    public ResponseEntity getById(@PathVariable String id){

        ResponseJsonRender resultData=new ResponseJsonRender();
        CmsContentDto baseDataScopeDto = apiCmsContentPoService.selectByPrimaryKey(id,false);
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
     * 复数资源，搜索内容
     * @param dto
     * @return
     */
    @RepeatFormValidator
    @RequiresPermissions("content:search")
    @RequestMapping(value = "/contents",method = RequestMethod.GET)
    public ResponseEntity search(SearchCmsContentsConditionDto dto){

        ResponseJsonRender resultData=new ResponseJsonRender();
        PageAndOrderbyParamDto pageAndOrderbyParamDto = new PageAndOrderbyParamDto(PageUtils.getPageFromThreadLocal(), OrderbyUtils.getOrderbyFromThreadLocal());
        // 设置当前登录用户id
        dto.setCurrentUserId(getLoginUser().getId());
        dto.setCurrentRoleId(((BaseRoleDto) getLoginUser().getRole()).getId());
        PageResultDto<CmsContentDto> list = apiCmsContentPoService.searchCmsContentsDsf(dto,pageAndOrderbyParamDto);

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
