package com.feihua.framework.rest.modules.postjob.mvc;

import com.feihua.framework.base.modules.area.dto.BaseAreaDto;
import com.feihua.framework.base.modules.office.api.ApiBaseOfficePoService;
import com.feihua.framework.base.modules.office.dto.BaseOfficeDto;
import com.feihua.framework.base.modules.postjob.api.ApiBasePostJobPoService;
import com.feihua.framework.base.modules.postjob.dto.BasePostJobDto;
import com.feihua.framework.base.modules.role.dto.BaseRoleDto;
import com.feihua.framework.rest.ResponseJsonRender;
import com.feihua.framework.rest.interceptor.RepeatFormValidator;
import com.feihua.framework.rest.modules.common.mvc.BaseController;
import com.feihua.framework.rest.mvc.SuperController;
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
import com.feihua.framework.base.modules.postjob.dto.BasePostDto;
import com.feihua.framework.base.modules.postjob.dto.SearchBasePostsConditionDto;
import com.feihua.framework.base.modules.postjob.api.ApiBasePostPoService;
import com.feihua.framework.rest.modules.postjob.dto.AddBasePostFormDto;
import com.feihua.framework.rest.modules.postjob.dto.UpdateBasePostFormDto;
import com.feihua.framework.base.modules.postjob.po.BasePostPo;

import java.util.HashMap;
import java.util.Map;

/**
 * 岗位管理
 * Created by yangwei
 */
@RestController
@RequestMapping("/base/postjob")
public class BasePostController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(BasePostController.class);

    @Autowired
    private ApiBasePostPoService apiBasePostPoService;
    @Autowired
    private ApiBaseOfficePoService apiBaseOfficePoService;
    @Autowired
    private ApiBasePostJobPoService apiBasePostJobPoService;

    /**
     * 单资源，添加
     * @param dto
     * @return
     */
    @RepeatFormValidator
    @RequiresPermissions("base:postjob:post:add")
    @RequestMapping(value = "/post",method = RequestMethod.POST)
    public ResponseEntity add(AddBasePostFormDto dto){
        logger.info("添加岗位开始");
        logger.info("当前登录用户id:{}",getLoginUser().getId());
        ResponseJsonRender resultData = new ResponseJsonRender();
        // 表单值设置
        BasePostPo basePo = new BasePostPo();
        basePo.setName(dto.getName());
        basePo.setCode(dto.getCode());
        basePo.setType(dto.getType());
        basePo.setPostJobId(dto.getPostJobId());
        basePo.setDisabled(dto.getDisabled());
        basePo.setParentId(dto.getParentId());
        basePo.setDataOfficeId(dto.getDataOfficeId());
        basePo.setIsPublic(dto.getIsPublic());

        basePo = apiBasePostPoService.preInsert(basePo,getLoginUser().getId());
        BasePostDto r = apiBasePostPoService.insert(basePo);
        if (r == null) {
            // 添加失败
            resultData.setCode(ResponseCode.E404_100001.getCode());
            resultData.setMsg(ResponseCode.E404_100001.getMsg());
            logger.info("code:{},msg:{}",resultData.getCode(),resultData.getMsg());
            logger.info("添加岗位结束，失败");
            return new ResponseEntity(resultData,HttpStatus.NOT_FOUND);
        }else{
            // 添加成功，返回添加的数据
            resultData.setData(r);
            logger.info("添加岗位id:{}",r.getId());
            logger.info("添加岗位结束，成功");
            return new ResponseEntity(resultData, HttpStatus.CREATED);
        }
    }

    /**
     * 单资源，删除
     * @param id
     * @return
     */
    @RepeatFormValidator
    @RequiresPermissions("base:postjob:post:delete")
    @RequestMapping(value = "/post/{id}",method = RequestMethod.DELETE)
    public ResponseEntity delete(@PathVariable String id){
        logger.info("删除岗位开始");
        logger.info("用户id:{}",getLoginUser().getId());
        logger.info("岗位id:{}",id);
        ResponseJsonRender resultData = new ResponseJsonRender();

            int r = apiBasePostPoService.deleteFlagByPrimaryKeyWithUpdateUser(id,getLoginUser().getId());
            if (r <= 0) {
                // 删除失败，可能没有找到资源
                resultData.setCode(ResponseCode.E404_100001.getCode());
                resultData.setMsg(ResponseCode.E404_100001.getMsg());
                logger.info("code:{},msg:{}",resultData.getCode(),resultData.getMsg());
                logger.info("删除岗位结束，失败");
                return new ResponseEntity(resultData,HttpStatus.NOT_FOUND);
            }else{
                // 删除成功
                logger.info("删除的岗位id:{}",id);
                logger.info("删除岗位结束，成功");
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
    @RequiresPermissions("base:postjob:post:update")
    @RequestMapping(value = "/post/{id}",method = RequestMethod.PUT)
    public ResponseEntity update(@PathVariable String id, UpdateBasePostFormDto dto){
        logger.info("更新岗位开始");
        logger.info("当前登录用户id:{}",getLoginUser().getId());
        logger.info("岗位id:{}",id);
        ResponseJsonRender resultData = new ResponseJsonRender();
        // 表单值设置
        BasePostPo basePo = new BasePostPo();
        // id
        basePo.setId(id);
        basePo.setName(dto.getName());
        basePo.setCode(dto.getCode());
        basePo.setType(dto.getType());
        basePo.setPostJobId(dto.getPostJobId());
        basePo.setDisabled(dto.getDisabled());

        basePo.setParentId(dto.getParentId());
        basePo.setDataOfficeId(dto.getDataOfficeId());
        basePo.setIsPublic(dto.getIsPublic());

        // 用条件更新，乐观锁机制
        BasePostPo basePoCondition = new BasePostPo();
        basePoCondition.setId(id);
        basePoCondition.setDelFlag(BasePo.YesNo.N.name());
        basePoCondition.setUpdateAt(dto.getUpdateTime());
        basePo = apiBasePostPoService.preUpdate(basePo,getLoginUser().getId());
        int r = apiBasePostPoService.updateSelective(basePo,basePoCondition);
        if (r <= 0) {
            // 更新失败，资源不存在
            resultData.setCode(ResponseCode.E404_100001.getCode());
            resultData.setMsg(ResponseCode.E404_100001.getMsg());
            logger.info("code:{},msg:{}",resultData.getCode(),resultData.getMsg());
            logger.info("更新岗位结束，失败");
            return new ResponseEntity(resultData,HttpStatus.NOT_FOUND);
        }else{
            // 更新成功，已被成功创建
            logger.info("更新的岗位id:{}",id);
            logger.info("更新岗位结束，成功");
            return new ResponseEntity(resultData, HttpStatus.CREATED);
        }
    }

    /**
     * 单资源，获取id岗位
     * @param id
     * @return
     */
    @RequiresPermissions("base:postjob:post:getById")
    @RequestMapping(value = "/post/{id}",method = RequestMethod.GET)
    public ResponseEntity getById(@PathVariable String id){

        ResponseJsonRender resultData = new ResponseJsonRender();
        BasePostDto baseDataScopeDto = apiBasePostPoService.selectByPrimaryKey(id,false);
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
     * 复数资源，搜索岗位
     * @param dto
     * @return
     */
    @RequiresPermissions("base:postjob:post:search")
    @RequestMapping(value = "/posts",method = RequestMethod.GET)
    public ResponseEntity search(SearchBasePostsConditionDto dto, boolean includeParent, boolean includeOffice,boolean includePostJob){

        ResponseJsonRender resultData = new ResponseJsonRender();
        PageAndOrderbyParamDto pageAndOrderbyParamDto = new PageAndOrderbyParamDto(PageUtils.getPageFromThreadLocal(), OrderbyUtils.getOrderbyFromThreadLocal());
        // 设置当前登录用户id
        dto.setCurrentUserId(getLoginUser().getId());
        dto.setCurrentRoleId(getLoginUserRoleId());
        dto.setCurrentPostId(getLoginUserPostId());
        PageResultDto<BasePostDto> pageResultDto = apiBasePostPoService.searchBasePostsDsf(dto,pageAndOrderbyParamDto);
        //机构和职务、父级
        if (pageResultDto.getData() != null && (includeOffice || includePostJob || includeParent)) {
            //父级
            Map<String,BasePostDto> roleDtoMap = new HashMap<>();
            BasePostDto postDto = null;

            //职务
            Map<String, BasePostJobDto> postJobDtoMap = new HashMap<>();
            BasePostJobDto postJobDto = null;

            //机构
            Map<String, BaseOfficeDto> officeDtoMap = new HashMap<>();
            BaseOfficeDto officeDto = null;

            for (BasePostDto _postDto : pageResultDto.getData()) {
                if(includePostJob && StringUtils.isNotEmpty(_postDto.getPostJobId())){
                    postJobDto = apiBasePostJobPoService.selectByPrimaryKey(_postDto.getPostJobId());
                    if (postJobDto != null) {
                        postJobDtoMap.put(_postDto.getPostJobId(),postJobDto);
                    }
                }

                if(includeOffice && StringUtils.isNotEmpty(_postDto.getDataOfficeId())){
                    officeDto = apiBaseOfficePoService.selectByPrimaryKey(_postDto.getDataOfficeId());
                    if (officeDto != null) {
                        officeDtoMap.put(_postDto.getDataOfficeId(),officeDto);
                    }
                }

                if(includeParent && StringUtils.isNotEmpty(_postDto.getParentId())){
                    postDto = apiBasePostPoService.selectByPrimaryKey(_postDto.getParentId());
                    if (postDto != null) {
                        roleDtoMap.put(_postDto.getParentId(),postDto);
                    }
                }
            }
            if (!postJobDtoMap.isEmpty()) {
                resultData.addData("postJob",postJobDtoMap);
            }
            if (!officeDtoMap.isEmpty()) {
                resultData.addData("office",officeDtoMap);
            }
            if (!roleDtoMap.isEmpty()) {
                resultData.addData("parent",roleDtoMap);
            }
        }

        resultData.setPage(pageResultDto.getPage());
        return returnList(pageResultDto.getData(),resultData);

    }
}
