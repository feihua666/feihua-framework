package com.feihua.framework.cms.admin.rest.mvc;

import com.feihua.framework.base.modules.role.dto.BaseRoleDto;
import com.feihua.framework.cms.CmsConstants;
import com.feihua.framework.cms.dto.CmsSiteTemplateModelDto;
import com.feihua.framework.cms.dto.CmsTemplateModelContextDto;
import com.feihua.framework.rest.ResponseJsonRender;
import com.feihua.framework.rest.interceptor.RepeatFormValidator;
import com.feihua.framework.rest.mvc.SuperController;
import com.feihua.utils.http.httpServletRequest.RequestUtils;
import com.feihua.utils.http.httpServletResponse.ResponseCode;
import com.feihua.utils.io.FileUtils;
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
import com.feihua.framework.cms.dto.CmsSiteDto;
import com.feihua.framework.cms.dto.SearchCmsSitesConditionDto;
import com.feihua.framework.cms.api.ApiCmsSitePoService;
import com.feihua.framework.cms.admin.rest.dto.AddCmsSiteFormDto;
import com.feihua.framework.cms.admin.rest.dto.UpdateCmsSiteFormDto;
import com.feihua.framework.cms.po.CmsSitePo;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 站点管理
 * Created by yangwei
 */
@RestController
@RequestMapping("/cms")
public class CmsSiteController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(CmsSiteController.class);

    @Autowired
    private ApiCmsSitePoService apiCmsSitePoService;

    /**
     * 单资源，添加
     * @param dto
     * @return
     */
    @RepeatFormValidator
    @RequiresPermissions("site:add")
    @RequestMapping(value = "/site",method = RequestMethod.POST)
    public ResponseEntity add(AddCmsSiteFormDto dto){
        logger.info("添加站点开始");
        logger.info("当前登录用户id:{}",getLoginUser().getId());
        ResponseJsonRender resultData=new ResponseJsonRender();
        // 表单值设置
        CmsSitePo basePo = new CmsSitePo();
            basePo.setName(dto.getName());
            basePo.setDomain(dto.getDomain());
            basePo.setPath(dto.getPath());
            basePo.setTemplate(dto.getTemplate());
            basePo.setTemplatePath(dto.getTemplatePath());
            basePo.setStaticPath(dto.getStaticPath());
            basePo.setDeployPath(dto.getDeployPath());
            basePo.setIsMain(dto.getIsMain());

        basePo = apiCmsSitePoService.preInsert(basePo,getLoginUser().getId());
        CmsSiteDto r = apiCmsSitePoService.insert(basePo);
        if (r == null) {
            // 添加失败
            resultData.setCode(ResponseCode.E404_100001.getCode());
            resultData.setMsg(ResponseCode.E404_100001.getMsg());
            logger.info("code:{},msg:{}",resultData.getCode(),resultData.getMsg());
            logger.info("添加站点结束，失败");
            return new ResponseEntity(resultData,HttpStatus.NOT_FOUND);
        }else{
            // 添加成功，返回添加的数据
            resultData.setData(r);
            logger.info("添加站点id:{}",r.getId());
            logger.info("添加站点结束，成功");
            return new ResponseEntity(resultData, HttpStatus.CREATED);
        }
    }

    /**
     * 单资源，删除
     * @param id
     * @return
     */
    @RepeatFormValidator
    @RequiresPermissions("site:delete")
    @RequestMapping(value = "/site/{id}",method = RequestMethod.DELETE)
    public ResponseEntity delete(@PathVariable String id){
        logger.info("删除站点开始");
        logger.info("用户id:{}",getLoginUser().getId());
        logger.info("站点id:{}",id);
        ResponseJsonRender resultData=new ResponseJsonRender();

            int r = apiCmsSitePoService.deleteFlagByPrimaryKeyWithUpdateUser(id,getLoginUser().getId());
            if (r <= 0) {
                // 删除失败，可能没有找到资源
                resultData.setCode(ResponseCode.E404_100001.getCode());
                resultData.setMsg(ResponseCode.E404_100001.getMsg());
                logger.info("code:{},msg:{}",resultData.getCode(),resultData.getMsg());
                logger.info("删除站点结束，失败");
                return new ResponseEntity(resultData,HttpStatus.NOT_FOUND);
            }else{
                // 删除成功
                logger.info("删除的站点id:{}",id);
                logger.info("删除站点结束，成功");
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
    @RequiresPermissions("site:update")
    @RequestMapping(value = "/site/{id}",method = RequestMethod.PUT)
    public ResponseEntity update(@PathVariable String id, UpdateCmsSiteFormDto dto){
        logger.info("更新站点开始");
        logger.info("当前登录用户id:{}",getLoginUser().getId());
        logger.info("站点id:{}",id);
        ResponseJsonRender resultData=new ResponseJsonRender();
        // 表单值设置
        CmsSitePo basePo = new CmsSitePo();
        // id
            basePo.setId(id);
            basePo.setName(dto.getName());;
            basePo.setDomain(dto.getDomain());;
            basePo.setPath(dto.getPath());;
        basePo.setTemplate(dto.getTemplate());
        basePo.setTemplatePath(dto.getTemplatePath());
        basePo.setStaticPath(dto.getStaticPath());
        basePo.setDeployPath(dto.getDeployPath());
        basePo.setIsMain(dto.getIsMain());

        // 用条件更新，乐观锁机制
        CmsSitePo basePoCondition = new CmsSitePo();
        basePoCondition.setId(id);
        basePoCondition.setDelFlag(BasePo.YesNo.N.name());
        basePoCondition.setUpdateAt(dto.getUpdateTime());
        basePo = apiCmsSitePoService.preUpdate(basePo,getLoginUser().getId());
        int r = apiCmsSitePoService.updateSelective(basePo,basePoCondition);
        if (r <= 0) {
            // 更新失败，资源不存在
            resultData.setCode(ResponseCode.E404_100001.getCode());
            resultData.setMsg(ResponseCode.E404_100001.getMsg());
            logger.info("code:{},msg:{}",resultData.getCode(),resultData.getMsg());
            logger.info("更新站点结束，失败");
            return new ResponseEntity(resultData,HttpStatus.NOT_FOUND);
        }else{
            // 更新成功，已被成功创建
            logger.info("更新的站点id:{}",id);
            logger.info("更新站点结束，成功");
            return new ResponseEntity(resultData, HttpStatus.CREATED);
        }
    }

    /**
     * 单资源，获取id站点
     * @param id
     * @return
     */
    @RepeatFormValidator
    @RequiresPermissions("site:getById")
    @RequestMapping(value = "/site/{id}",method = RequestMethod.GET)
    public ResponseEntity getById(@PathVariable String id){

        ResponseJsonRender resultData=new ResponseJsonRender();
        CmsSiteDto cmsSiteDto = apiCmsSitePoService.selectByPrimaryKey(id,false);
        if(cmsSiteDto != null){
            resultData.setData(cmsSiteDto);
            return new ResponseEntity(resultData, HttpStatus.OK);
        }else{
            resultData.setCode(ResponseCode.E404_100001.getCode());
            resultData.setMsg(ResponseCode.E404_100001.getMsg());
            return new ResponseEntity(resultData,HttpStatus.NOT_FOUND);
        }
    }

    /**
     * 复数资源，搜索站点
     * @param dto
     * @return
     */
    @RepeatFormValidator
    @RequiresPermissions("site:search")
    @RequestMapping(value = "/sites",method = RequestMethod.GET)
    public ResponseEntity search(SearchCmsSitesConditionDto dto){

        ResponseJsonRender resultData=new ResponseJsonRender();
        PageAndOrderbyParamDto pageAndOrderbyParamDto = new PageAndOrderbyParamDto(PageUtils.getPageFromThreadLocal(), OrderbyUtils.getOrderbyFromThreadLocal());
        // 设置当前登录用户id
        dto.setCurrentUserId(getLoginUser().getId());
        dto.setCurrentRoleId(((BaseRoleDto) getLoginUser().getRole()).getId());
        PageResultDto<CmsSiteDto> list = apiCmsSitePoService.searchCmsSitesDsf(dto,pageAndOrderbyParamDto);

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



    /**
     * 站点首页地址
     * @param id
     * @return
     */
    @RepeatFormValidator
    @RequestMapping(value = "/site/{id}/address",method = RequestMethod.GET)
    public ResponseEntity indexAddress(@PathVariable String id){

        ResponseJsonRender resultData=new ResponseJsonRender();
        CmsSiteDto cmsSiteDto = apiCmsSitePoService.selectByPrimaryKey(id,false);
        if(cmsSiteDto != null){
            CmsTemplateModelContextDto cmsTemplateModelContextDto = new CmsTemplateModelContextDto(true);
            CmsSiteTemplateModelDto cmsSiteTemplateModelDto = new CmsSiteTemplateModelDto(cmsSiteDto,cmsTemplateModelContextDto);
            resultData.setData(cmsSiteTemplateModelDto);
            return new ResponseEntity(resultData, HttpStatus.OK);
        }else{
            resultData.setCode(ResponseCode.E404_100001.getCode());
            resultData.setMsg(ResponseCode.E404_100001.getMsg());
            return new ResponseEntity(resultData,HttpStatus.NOT_FOUND);
        }
    }

    /**
     * 单资源,站点模板路径
     * @return
     */
    @RepeatFormValidator
    @RequestMapping(value = "/site/templatepath",method = RequestMethod.GET)
    public ResponseEntity templatepath(){

        ResponseJsonRender resultData=new ResponseJsonRender();
        List<String> templatePathStr = super.getFileNames(null,true);
        if(templatePathStr != null && !templatePathStr.isEmpty()){
            resultData.setData(templatePathStr);
            return new ResponseEntity(resultData, HttpStatus.OK);
        }else{
            resultData.setCode(ResponseCode.E404_100001.getCode());
            resultData.setMsg(ResponseCode.E404_100001.getMsg());
            return new ResponseEntity(resultData,HttpStatus.NOT_FOUND);
        }
    }

    /**
     * 单资源,站点首页模板
     * @return
     */
    @RepeatFormValidator
    @RequestMapping(value = "/site/template",method = RequestMethod.GET)
    public ResponseEntity template(String templatePath){

        ResponseJsonRender resultData=new ResponseJsonRender();
        List<String> templatePathStr = super.getFileNames(templatePath,false);
        if(templatePathStr != null && !templatePathStr.isEmpty()){
            resultData.setData(templatePathStr);
            return new ResponseEntity(resultData, HttpStatus.OK);
        }else{
            resultData.setCode(ResponseCode.E404_100001.getCode());
            resultData.setMsg(ResponseCode.E404_100001.getMsg());
            return new ResponseEntity(resultData,HttpStatus.NOT_FOUND);
        }
    }
}
