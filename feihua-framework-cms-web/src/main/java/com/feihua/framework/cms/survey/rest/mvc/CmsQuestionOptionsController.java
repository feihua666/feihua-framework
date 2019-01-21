package com.feihua.framework.cms.survey.rest.mvc;

import com.feihua.framework.base.modules.role.dto.BaseRoleDto;
import com.feihua.framework.cms.admin.rest.mvc.BaseController;
import com.feihua.framework.log.comm.annotation.OperationLog;
import com.feihua.framework.rest.ResponseJsonRender;
import com.feihua.framework.rest.interceptor.RepeatFormValidator;
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
import com.feihua.framework.cms.dto.CmsQuestionOptionsDto;
import com.feihua.framework.cms.dto.SearchCmsQuestionOptionssConditionDto;
import com.feihua.framework.cms.api.ApiCmsQuestionOptionsPoService;
import com.feihua.framework.cms.survey.rest.dto.AddCmsQuestionOptionsFormDto;
import com.feihua.framework.cms.survey.rest.dto.UpdateCmsQuestionOptionsFormDto;
import com.feihua.framework.cms.po.CmsQuestionOptionsPo;

/**
 * 调查问题选项管理
 * Created by yangwei
 */
@RestController
@RequestMapping("/cms")
public class CmsQuestionOptionsController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(CmsQuestionOptionsController.class);

    @Autowired
    private ApiCmsQuestionOptionsPoService apiCmsQuestionOptionsPoService;

    /**
     * 单资源，添加
     *
     * @param dto
     *
     * @return
     */
    @OperationLog(operation = "调查问题选项管理", content = "单资源，添加")
    @RepeatFormValidator
    @RequiresPermissions("cms:question:options:add")
    @RequestMapping(value = "/question/options", method = RequestMethod.POST)
    public ResponseEntity add(AddCmsQuestionOptionsFormDto dto) {
        logger.info("添加问题选项开始");
        logger.info("当前登录用户id:{}", getLoginUser().getId());
        ResponseJsonRender resultData = new ResponseJsonRender();
        // 表单值设置
        CmsQuestionOptionsPo basePo = new CmsQuestionOptionsPo();
        basePo.setId(dto.getId());
        basePo.setQuestionId(dto.getQuestionId());
        basePo.setName(dto.getName());
        basePo.setDataUserId(dto.getDataUserId());
        basePo.setDataOfficeId(dto.getDataOfficeId());
        basePo.setDataType(dto.getDataType());
        basePo.setDataAreaId(dto.getDataAreaId());
        basePo.setDelFlag(dto.getDelFlag());
        basePo.setCreateAt(dto.getCreateAt());
        basePo.setCreateBy(dto.getCreateBy());
        basePo.setUpdateAt(dto.getUpdateAt());
        basePo.setUpdateBy(dto.getUpdateBy());

        basePo = apiCmsQuestionOptionsPoService.preInsert(basePo, getLoginUser().getId());
        CmsQuestionOptionsDto r = apiCmsQuestionOptionsPoService.insert(basePo);
        if (r == null) {
            // 添加失败
            resultData.setCode(ResponseCode.E404_100001.getCode());
            resultData.setMsg(ResponseCode.E404_100001.getMsg());
            logger.info("code:{},msg:{}", resultData.getCode(), resultData.getMsg());
            logger.info("添加问题选项结束，失败");
            return new ResponseEntity(resultData, HttpStatus.NOT_FOUND);
        } else {
            // 添加成功，返回添加的数据
            resultData.setData(r);
            logger.info("添加问题选项id:{}", r.getId());
            logger.info("添加问题选项结束，成功");
            return new ResponseEntity(resultData, HttpStatus.CREATED);
        }
    }

    /**
     * 单资源，删除
     *
     * @param id
     *
     * @return
     */
    @OperationLog(operation = "调查问题选项管理", content = "单资源，删除")
    @RepeatFormValidator
    @RequiresPermissions("cms:question:options:delete")
    @RequestMapping(value = "/question/options/{id}", method = RequestMethod.DELETE)
    public ResponseEntity delete(@PathVariable String id) {
        logger.info("删除问题选项开始");
        logger.info("用户id:{}", getLoginUser().getId());
        logger.info("问题选项id:{}", id);
        ResponseJsonRender resultData = new ResponseJsonRender();

        int r = apiCmsQuestionOptionsPoService.deleteFlagByPrimaryKeyWithUpdateUser(id, getLoginUser().getId());
        if (r <= 0) {
            // 删除失败，可能没有找到资源
            resultData.setCode(ResponseCode.E404_100001.getCode());
            resultData.setMsg(ResponseCode.E404_100001.getMsg());
            logger.info("code:{},msg:{}", resultData.getCode(), resultData.getMsg());
            logger.info("删除问题选项结束，失败");
            return new ResponseEntity(resultData, HttpStatus.NOT_FOUND);
        } else {
            // 删除成功
            logger.info("删除的问题选项id:{}", id);
            logger.info("删除问题选项结束，成功");
            return new ResponseEntity(resultData, HttpStatus.NO_CONTENT);
        }
    }

    /**
     * 单资源，更新
     *
     * @param id
     * @param dto
     *
     * @return
     */
    @OperationLog(operation = "调查问题选项管理", content = "单资源，更新")
    @RepeatFormValidator
    @RequiresPermissions("cms:question:options:update")
    @RequestMapping(value = "/question/options/{id}", method = RequestMethod.PUT)
    public ResponseEntity update(@PathVariable String id, UpdateCmsQuestionOptionsFormDto dto) {
        logger.info("更新问题选项开始");
        logger.info("当前登录用户id:{}", getLoginUser().getId());
        logger.info("问题选项id:{}", id);
        ResponseJsonRender resultData = new ResponseJsonRender();
        // 表单值设置
        CmsQuestionOptionsPo basePo = new CmsQuestionOptionsPo();
        // id
        basePo.setId(id);
        basePo.setId(dto.getId());
        basePo.setQuestionId(dto.getQuestionId());
        basePo.setName(dto.getName());
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
        CmsQuestionOptionsPo basePoCondition = new CmsQuestionOptionsPo();
        basePoCondition.setId(id);
        basePoCondition.setDelFlag(BasePo.YesNo.N.name());
        basePoCondition.setUpdateAt(dto.getUpdateTime());
        basePo = apiCmsQuestionOptionsPoService.preUpdate(basePo, getLoginUser().getId());
        int r = apiCmsQuestionOptionsPoService.updateSelective(basePo, basePoCondition);
        if (r <= 0) {
            // 更新失败，资源不存在
            resultData.setCode(ResponseCode.E404_100001.getCode());
            resultData.setMsg(ResponseCode.E404_100001.getMsg());
            logger.info("code:{},msg:{}", resultData.getCode(), resultData.getMsg());
            logger.info("更新问题选项结束，失败");
            return new ResponseEntity(resultData, HttpStatus.NOT_FOUND);
        } else {
            // 更新成功，已被成功创建
            logger.info("更新的问题选项id:{}", id);
            logger.info("更新问题选项结束，成功");
            return new ResponseEntity(resultData, HttpStatus.CREATED);
        }
    }

    /**
     * 单资源，获取id问题选项
     *
     * @param id
     *
     * @return
     */
    @OperationLog(operation = "调查问题选项管理", content = "单资源，获取id问题选项")
    @RepeatFormValidator
    @RequiresPermissions("cms:question:options:getById")
    @RequestMapping(value = "/question/options/{id}", method = RequestMethod.GET)
    public ResponseEntity getById(@PathVariable String id) {

        ResponseJsonRender resultData = new ResponseJsonRender();
        CmsQuestionOptionsDto baseDataScopeDto = apiCmsQuestionOptionsPoService.selectByPrimaryKey(id, false);
        if (baseDataScopeDto != null) {
            resultData.setData(baseDataScopeDto);
            return new ResponseEntity(resultData, HttpStatus.OK);
        } else {
            resultData.setCode(ResponseCode.E404_100001.getCode());
            resultData.setMsg(ResponseCode.E404_100001.getMsg());
            return new ResponseEntity(resultData, HttpStatus.NOT_FOUND);
        }
    }

    /**
     * 复数资源，搜索问题选项
     *
     * @param dto
     *
     * @return
     */
    @OperationLog(operation = "调查问题选项管理", content = "复数资源，搜索问题选项")
    @RepeatFormValidator
    @RequiresPermissions("cms:question:options:search")
    @RequestMapping(value = "/question/optionss", method = RequestMethod.GET)
    public ResponseEntity search(SearchCmsQuestionOptionssConditionDto dto) {

        ResponseJsonRender resultData = new ResponseJsonRender();
        PageAndOrderbyParamDto pageAndOrderbyParamDto = new PageAndOrderbyParamDto(PageUtils.getPageFromThreadLocal(), OrderbyUtils.getOrderbyFromThreadLocal());
        // 设置当前登录用户id
        dto.setCurrentUserId(getLoginUser().getId());
        dto.setCurrentRoleId(((BaseRoleDto) getLoginUser().getRole()).getId());
        PageResultDto<CmsQuestionOptionsDto> list = apiCmsQuestionOptionsPoService.searchCmsQuestionOptionssDsf(dto, pageAndOrderbyParamDto);

        if (CollectionUtils.isNotEmpty(list.getData())) {
            resultData.setData(list.getData());
            resultData.setPage(list.getPage());
            return new ResponseEntity(resultData, HttpStatus.OK);
        } else {
            resultData.setCode(ResponseCode.E404_100001.getCode());
            resultData.setMsg(ResponseCode.E404_100001.getMsg());
            return new ResponseEntity(resultData, HttpStatus.NOT_FOUND);
        }
    }
}
