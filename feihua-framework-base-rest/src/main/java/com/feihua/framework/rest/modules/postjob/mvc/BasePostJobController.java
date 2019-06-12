package com.feihua.framework.rest.modules.postjob.mvc;

import com.feihua.framework.base.modules.office.api.ApiBaseOfficePoService;
import com.feihua.framework.base.modules.office.dto.BaseOfficeDto;
import com.feihua.framework.base.modules.postjob.dto.BasePostDto;
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
import com.feihua.framework.base.modules.postjob.dto.BasePostJobDto;
import com.feihua.framework.base.modules.postjob.dto.SearchBasePostJobsConditionDto;
import com.feihua.framework.base.modules.postjob.api.ApiBasePostJobPoService;
import com.feihua.framework.rest.modules.postjob.dto.AddBasePostJobFormDto;
import com.feihua.framework.rest.modules.postjob.dto.UpdateBasePostJobFormDto;
import com.feihua.framework.base.modules.postjob.po.BasePostJobPo;

import java.util.HashMap;
import java.util.Map;

/**
 * 岗位职务管理
 * Created by yangwei
 */
@RestController
@RequestMapping("/base/postjob")
public class BasePostJobController extends SuperController {

    private static Logger logger = LoggerFactory.getLogger(BasePostJobController.class);

    @Autowired
    private ApiBasePostJobPoService apiBasePostJobPoService;
    @Autowired
    private ApiBaseOfficePoService apiBaseOfficePoService;

    /**
     * 单资源，添加
     * @param dto
     * @return
     */
    @RepeatFormValidator
    @RequiresPermissions("base:postjob:postjob:add")
    @RequestMapping(value = "/postjob",method = RequestMethod.POST)
    public ResponseEntity add(AddBasePostJobFormDto dto){
        logger.info("添加岗位职务开始");
        logger.info("当前登录用户id:{}",getLoginUser().getId());
        ResponseJsonRender resultData = new ResponseJsonRender();
        // 表单值设置
        BasePostJobPo basePo = new BasePostJobPo();
        basePo.setName(dto.getName());
        basePo.setCode(dto.getCode());
        basePo.setType(dto.getType());
        basePo.setIsPublic(dto.getIsPublic());
        basePo.setDataOfficeId(dto.getDataOfficeId());

        basePo = apiBasePostJobPoService.preInsert(basePo,getLoginUser().getId());
        BasePostJobDto r = apiBasePostJobPoService.insert(basePo);
        if (r == null) {
            // 添加失败
            resultData.setCode(ResponseCode.E404_100001.getCode());
            resultData.setMsg(ResponseCode.E404_100001.getMsg());
            logger.info("code:{},msg:{}",resultData.getCode(),resultData.getMsg());
            logger.info("添加岗位职务结束，失败");
            return new ResponseEntity(resultData,HttpStatus.NOT_FOUND);
        }else{
            // 添加成功，返回添加的数据
            resultData.setData(r);
            logger.info("添加岗位职务id:{}",r.getId());
            logger.info("添加岗位职务结束，成功");
            return new ResponseEntity(resultData, HttpStatus.CREATED);
        }
    }

    /**
     * 单资源，删除
     * @param id
     * @return
     */
    @RepeatFormValidator
    @RequiresPermissions("base:postjob:postjob:delete")
    @RequestMapping(value = "/postjob/{id}",method = RequestMethod.DELETE)
    public ResponseEntity delete(@PathVariable String id){
        logger.info("删除岗位职务开始");
        logger.info("用户id:{}",getLoginUser().getId());
        logger.info("岗位职务id:{}",id);
        ResponseJsonRender resultData = new ResponseJsonRender();

            int r = apiBasePostJobPoService.deleteFlagByPrimaryKeyWithUpdateUser(id,getLoginUser().getId());
            if (r <= 0) {
                // 删除失败，可能没有找到资源
                resultData.setCode(ResponseCode.E404_100001.getCode());
                resultData.setMsg(ResponseCode.E404_100001.getMsg());
                logger.info("code:{},msg:{}",resultData.getCode(),resultData.getMsg());
                logger.info("删除岗位职务结束，失败");
                return new ResponseEntity(resultData,HttpStatus.NOT_FOUND);
            }else{
                // 删除成功
                logger.info("删除的岗位职务id:{}",id);
                logger.info("删除岗位职务结束，成功");
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
    @RequiresPermissions("base:postjob:postjob:update")
    @RequestMapping(value = "/postjob/{id}",method = RequestMethod.PUT)
    public ResponseEntity update(@PathVariable String id, UpdateBasePostJobFormDto dto){
        logger.info("更新岗位职务开始");
        logger.info("当前登录用户id:{}",getLoginUser().getId());
        logger.info("岗位职务id:{}",id);
        ResponseJsonRender resultData = new ResponseJsonRender();
        // 表单值设置
        BasePostJobPo basePo = new BasePostJobPo();
        // id
        basePo.setId(id);
        basePo.setName(dto.getName());
        basePo.setCode(dto.getCode());
        basePo.setType(dto.getType());
        basePo.setIsPublic(dto.getIsPublic());
        basePo.setDataOfficeId(dto.getDataOfficeId());

        // 用条件更新，乐观锁机制
        BasePostJobPo basePoCondition = new BasePostJobPo();
        basePoCondition.setId(id);
        basePoCondition.setDelFlag(BasePo.YesNo.N.name());
        basePoCondition.setUpdateAt(dto.getUpdateTime());
        basePo = apiBasePostJobPoService.preUpdate(basePo,getLoginUser().getId());
        int r = apiBasePostJobPoService.updateSelective(basePo,basePoCondition);
        if (r <= 0) {
            // 更新失败，资源不存在
            resultData.setCode(ResponseCode.E404_100001.getCode());
            resultData.setMsg(ResponseCode.E404_100001.getMsg());
            logger.info("code:{},msg:{}",resultData.getCode(),resultData.getMsg());
            logger.info("更新岗位职务结束，失败");
            return new ResponseEntity(resultData,HttpStatus.NOT_FOUND);
        }else{
            // 更新成功，已被成功创建
            logger.info("更新的岗位职务id:{}",id);
            logger.info("更新岗位职务结束，成功");
            return new ResponseEntity(resultData, HttpStatus.CREATED);
        }
    }

    /**
     * 单资源，获取id岗位职务
     * @param id
     * @return
     */
    @RepeatFormValidator
    @RequiresPermissions("base:postjob:postjob:getById")
    @RequestMapping(value = "/postjob/{id}",method = RequestMethod.GET)
    public ResponseEntity getById(@PathVariable String id){

        ResponseJsonRender resultData = new ResponseJsonRender();
        BasePostJobDto baseDataScopeDto = apiBasePostJobPoService.selectByPrimaryKey(id,false);
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
     * 复数资源，搜索岗位职务
     * @param dto
     * @return
     */
    @RequiresPermissions("base:postjob:postjob:search")
    @RequestMapping(value = "/postjobs",method = RequestMethod.GET)
    public ResponseEntity search(SearchBasePostJobsConditionDto dto, boolean includeOffice){

        ResponseJsonRender resultData = new ResponseJsonRender();
        PageAndOrderbyParamDto pageAndOrderbyParamDto = new PageAndOrderbyParamDto(PageUtils.getPageFromThreadLocal(), OrderbyUtils.getOrderbyFromThreadLocal());
        // 设置当前登录用户id
        dto.setCurrentUserId(getLoginUser().getId());
        dto.setCurrentRoleId(((BaseRoleDto) getLoginUser().getRole()).getId());
        PageResultDto<BasePostJobDto> pageResultDto = apiBasePostJobPoService.searchBasePostJobsDsf(dto,pageAndOrderbyParamDto);
        //机构
        if (pageResultDto.getData() != null && includeOffice) {

            //机构
            Map<String, BaseOfficeDto> officeDtoMap = new HashMap<>();
            BaseOfficeDto officeDto = null;

            for (BasePostJobDto _postJobDto : pageResultDto.getData()) {

                if(includeOffice && StringUtils.isNotEmpty(_postJobDto.getDataOfficeId())){
                    officeDto = apiBaseOfficePoService.selectByPrimaryKey(_postJobDto.getDataOfficeId());
                    if (officeDto != null) {
                        officeDtoMap.put(_postJobDto.getDataOfficeId(),officeDto);
                    }
                }

            }
            if (!officeDtoMap.isEmpty()) {
                resultData.addData("office",officeDtoMap);
            }
        }

        resultData.setPage(pageResultDto.getPage());
        return returnList(pageResultDto.getData(),resultData);

    }
}
