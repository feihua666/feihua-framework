package com.feihua.framework.cms.survey.rest.mvc;

import com.feihua.exception.DataNotFoundException;
import com.feihua.framework.base.modules.role.dto.BaseRoleDto;
import com.feihua.framework.cms.admin.rest.mvc.BaseController;
import com.feihua.framework.cms.api.ApiCmsQuestionOptionsPoService;
import com.feihua.framework.cms.api.ApiCmsQuestionPoService;
import com.feihua.framework.cms.api.ApiCmsSurveyPoService;
import com.feihua.framework.cms.dto.CmsQuestionDto;
import com.feihua.framework.cms.dto.CmsQuestionOptionsDto;
import com.feihua.framework.cms.dto.CmsSurveyDto;
import com.feihua.framework.cms.dto.SearchCmsSurveysConditionDto;
import com.feihua.framework.cms.po.CmsQuestionOptionsPo;
import com.feihua.framework.cms.po.CmsQuestionPo;
import com.feihua.framework.cms.po.CmsSurveyPo;
import com.feihua.framework.cms.survey.rest.dto.AddCmsSurveyFormDto;
import com.feihua.framework.cms.survey.rest.dto.UpdateCmsSurveyFormDto;
import com.feihua.framework.log.comm.annotation.OperationLog;
import com.feihua.framework.rest.ResponseJsonRender;
import com.feihua.framework.rest.interceptor.RepeatFormValidator;
import com.feihua.utils.http.httpServletRequest.RequestUtils;
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
import org.springframework.web.bind.annotation.*;
import tk.mybatis.orderbyhelper.OrderByHelper;

import java.util.Date;
import java.util.List;

/**
 * 调查管理
 * Created by yangwei
 */
@RestController
@RequestMapping("/cms")
public class CmsSurveyController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(CmsSurveyController.class);

    @Autowired
    private ApiCmsSurveyPoService apiCmsSurveyPoService;

    @Autowired
    private ApiCmsQuestionPoService apiCmsQuestionPoService;

    @Autowired
    private ApiCmsQuestionOptionsPoService apiCmsQuestionOptionsPoService;

    /**
     * 单资源，添加
     *
     * @param dto
     *
     * @return
     */
    @OperationLog(operation = "调查管理", content = "单资源，添加")
    @RepeatFormValidator
    @RequiresPermissions("cms:survey:add")
    @RequestMapping(value = "/survey", method = RequestMethod.POST)
    public ResponseEntity add(@RequestBody AddCmsSurveyFormDto dto) {
        logger.info("添加调查开始");
        logger.info("当前登录用户id:{}", getLoginUser().getId());
        ResponseJsonRender resultData = new ResponseJsonRender();
        // 表单值设置
        CmsSurveyPo basePo = new CmsSurveyPo();
        basePo.setId(dto.getId());
        basePo.setTitle(dto.getTitle());
        basePo.setDescription(dto.getDescription());
        basePo.setType(dto.getType());
        basePo.setStartTime(dto.getStartTime());
        basePo.setEndTime(dto.getEndTime());
        basePo.setStatus(dto.getStatus());
        basePo.setSequence(dto.getSequence());
        basePo.setRegister(dto.getRegister());
        basePo.setRepeatLimit(dto.getRepeatLimit());
        basePo.setUrl(dto.getUrl());
        basePo.setCount(dto.getCount());
        basePo.setDataUserId(dto.getDataUserId());
        basePo.setDataOfficeId(dto.getDataOfficeId());
        basePo.setDataType(dto.getDataType());
        basePo.setDataAreaId(dto.getDataAreaId());
        basePo.setDelFlag(dto.getDelFlag());
        basePo.setCreateAt(dto.getCreateAt());
        basePo.setCreateBy(dto.getCreateBy());
        basePo.setUpdateAt(dto.getUpdateAt());
        basePo.setUpdateBy(dto.getUpdateBy());

        basePo = apiCmsSurveyPoService.preInsert(basePo, getLoginUser().getId());
        CmsSurveyDto r = apiCmsSurveyPoService.insert(basePo);
        if (r == null) {
            // 添加失败
            resultData.setCode(ResponseCode.E404_100001.getCode());
            resultData.setMsg(ResponseCode.E404_100001.getMsg());
            logger.info("code:{},msg:{}", resultData.getCode(), resultData.getMsg());
            logger.info("添加调查结束，失败");
            return new ResponseEntity(resultData, HttpStatus.NOT_FOUND);
        } else {
            // 添加成功，返回添加的数据
            resultData.setData(r);
            logger.info("添加调查id:{}", r.getId());
            logger.info("添加调查结束，成功");
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
    @OperationLog(operation = "调查管理", content = "单资源，删除")
    @RepeatFormValidator
    @RequiresPermissions("cms:survey:delete")
    @RequestMapping(value = "/survey/{id}", method = RequestMethod.DELETE)
    public ResponseEntity delete(@PathVariable String id) {
        logger.info("删除调查开始");
        logger.info("用户id:{}", getLoginUser().getId());
        logger.info("调查id:{}", id);
        ResponseJsonRender resultData = new ResponseJsonRender();

        int r = apiCmsSurveyPoService.deleteFlagByPrimaryKeyWithUpdateUser(id, getLoginUser().getId());
        if (r <= 0) {
            // 删除失败，可能没有找到资源
            resultData.setCode(ResponseCode.E404_100001.getCode());
            resultData.setMsg(ResponseCode.E404_100001.getMsg());
            logger.info("code:{},msg:{}", resultData.getCode(), resultData.getMsg());
            logger.info("删除调查结束，失败");
            return new ResponseEntity(resultData, HttpStatus.NOT_FOUND);
        } else {
            // 删除成功
            logger.info("删除的调查id:{}", id);
            logger.info("删除调查结束，成功");
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
    @OperationLog(operation = "调查管理", content = "单资源，更新")
    @RepeatFormValidator
    @RequiresPermissions("cms:survey:update")
    @RequestMapping(value = "/survey/{id}", method = RequestMethod.PUT)
    public ResponseEntity update(@PathVariable String id, @RequestBody UpdateCmsSurveyFormDto dto) {
        logger.info("更新调查开始");
        logger.info("当前登录用户id:{}", getLoginUser().getId());
        logger.info("调查id:{}", id);
        ResponseJsonRender resultData = new ResponseJsonRender();
        // 表单值设置
        CmsSurveyPo basePo = new CmsSurveyPo();
        // id
        basePo.setId(id);
        basePo.setId(dto.getId());
        basePo.setTitle(dto.getTitle());
        basePo.setDescription(dto.getDescription());
        basePo.setType(dto.getType());
        basePo.setStartTime(dto.getStartTime());
        basePo.setEndTime(dto.getEndTime());
        basePo.setStatus(dto.getStatus());
        basePo.setSequence(dto.getSequence());
        basePo.setRegister(dto.getRegister());
        basePo.setRepeatLimit(dto.getRepeatLimit());
        basePo.setUrl(dto.getUrl());
        basePo.setCount(dto.getCount());
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
        CmsSurveyPo basePoCondition = new CmsSurveyPo();
        basePoCondition.setId(id);
        basePoCondition.setDelFlag(BasePo.YesNo.N.name());
        basePoCondition.setUpdateAt(dto.getUpdateTime());
        basePo = apiCmsSurveyPoService.preUpdate(basePo, getLoginUser().getId());
        int r = apiCmsSurveyPoService.updateSelective(basePo, basePoCondition);
        if (r <= 0) {
            // 更新失败，资源不存在
            resultData.setCode(ResponseCode.E404_100001.getCode());
            resultData.setMsg(ResponseCode.E404_100001.getMsg());
            logger.info("code:{},msg:{}", resultData.getCode(), resultData.getMsg());
            logger.info("更新调查结束，失败");
            return new ResponseEntity(resultData, HttpStatus.NOT_FOUND);
        } else {
            // 更新成功，已被成功创建
            logger.info("更新的调查id:{}", id);
            logger.info("更新调查结束，成功");
            return new ResponseEntity(resultData, HttpStatus.CREATED);
        }
    }


    /**
     * 单资源，更新，发布调查
     *
     * @param id
     * @param status
     * @param updateTime
     *
     * @return
     */
    @OperationLog(operation = "调查管理", content = "单资源，更新，发布调查")
    @RepeatFormValidator
    @RequiresPermissions("cms:survey:publish")
    @RequestMapping(value = "/survey/publish", method = RequestMethod.PUT)
    public ResponseEntity publish(String id, String status, Date updateTime) {
        ResponseJsonRender resultData = new ResponseJsonRender();
        // 表单值设置
        CmsSurveyPo basePo = new CmsSurveyPo();
        //TODO 调查发布还没想好逻辑
        CmsSurveyDto cmsSurveyDto = apiCmsSurveyPoService.selectByPrimaryKey(id);
        // id
        String url = null;
        basePo.setId(id);
        basePo.setStatus(status);
        //如果状态为进行中，则验证问题，和问题选项完整性
        if ("2".equals(status)) {
            url = RequestUtils.getWebUrl();
            CmsQuestionPo question = new CmsQuestionPo();
            question.setSurveyId(id);
            question.setDelFlag(BasePo.YesNo.N.name());
            final List<CmsQuestionDto> cmsQuestionDtos = apiCmsQuestionPoService.selectList(question);
            if (cmsQuestionDtos == null || cmsQuestionDtos.size() == 0) {
                throw new DataNotFoundException("当前调查没有添加问题，暂时不能发布，请添加问题！");
            }
            for (CmsQuestionDto cmsQuestionDto : cmsQuestionDtos) {
                if (!"text".equalsIgnoreCase(cmsQuestionDto.getType())) {
                    CmsQuestionOptionsPo options = new CmsQuestionOptionsPo();
                    options.setQuestionId(cmsQuestionDto.getId());
                    final List<CmsQuestionOptionsDto> cmsQuestionOptionsDtos = apiCmsQuestionOptionsPoService.selectList(options);
                    if (cmsQuestionOptionsDtos == null || cmsQuestionOptionsDtos.size() == 0) {
                        throw new DataNotFoundException("当前有调查问题没有添加问题选项，暂时不能发布，请添加问题选项！");
                    }
                }
            }
        }
        // 用条件更新，乐观锁机制
        CmsSurveyPo basePoCondition = new CmsSurveyPo();
        basePoCondition.setId(id);
        basePoCondition.setDelFlag(BasePo.YesNo.N.name());
        basePoCondition.setUpdateAt(updateTime);

        basePo.setUrl(url);
        basePo = apiCmsSurveyPoService.preUpdate(basePo, getLoginUser().getId());
        int r = apiCmsSurveyPoService.updateSelective(basePo, basePoCondition);
        if (r <= 0) {
            // 更新失败，资源不存在
            resultData.setCode(ResponseCode.E404_100001.getCode());
            resultData.setMsg(ResponseCode.E404_100001.getMsg());
            logger.info("code:{},msg:{}", resultData.getCode(), resultData.getMsg());
            logger.info("更新调查结束，失败");
            return new ResponseEntity(resultData, HttpStatus.NOT_FOUND);
        } else {
            // 更新成功，已被成功创建
            logger.info("更新的调查id:{}", id);
            logger.info("更新调查结束，成功");
            return new ResponseEntity(resultData, HttpStatus.CREATED);
        }
    }

    /**
     * 单资源，获取id调查
     *
     * @param id
     *
     * @return
     */
    @OperationLog(operation = "调查管理", content = "单资源，获取id调查")
    @RequiresPermissions("cms:survey:getById")
    @RequestMapping(value = "/survey/{id}", method = RequestMethod.GET)
    public ResponseEntity getById(@PathVariable String id) {
        ResponseJsonRender resultData = new ResponseJsonRender();
        CmsSurveyDto baseDataScopeDto = apiCmsSurveyPoService.selectByPrimaryKey(id, false);
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
     * 单资源，获取id调查
     *
     * @param id
     *
     * @return
     */
    @OperationLog(operation = "调查管理", content = "单资源，获取id调查、问题、选项")
    @RequiresPermissions("cms:survey:getById")
    @RequestMapping(value = "/survey/{id}/questions", method = RequestMethod.GET)
    public ResponseEntity getSurveyById(@PathVariable String id) {
        ResponseJsonRender resultData = new ResponseJsonRender();
        CmsSurveyDto baseDataScopeDto = apiCmsSurveyPoService.selectByPrimaryKey(id, false);
        CmsQuestionPo question = new CmsQuestionPo();
        question.setSurveyId(id);
        question.setDelFlag(BasePo.YesNo.N.name());
        OrderByHelper.orderBy("sequence desc,create_at");
        List<CmsQuestionDto> cmsQuestionDtos = apiCmsQuestionPoService.selectList(question);
        if (CollectionUtils.isNotEmpty(cmsQuestionDtos)) {
            baseDataScopeDto.setQuestions(cmsQuestionDtos);
            for (CmsQuestionDto cmsQuestionDto : cmsQuestionDtos) {
                if (!"text".equalsIgnoreCase(cmsQuestionDto.getType())) {
                    CmsQuestionOptionsPo options = new CmsQuestionOptionsPo();
                    options.setQuestionId(cmsQuestionDto.getId());
                    options.setDelFlag(BasePo.YesNo.N.name());
                    OrderByHelper.orderBy("create_at");
                    List<CmsQuestionOptionsDto> cmsQuestionOptionsDtos = apiCmsQuestionOptionsPoService.selectList(options);
                    if (CollectionUtils.isNotEmpty(cmsQuestionOptionsDtos)) {
                        cmsQuestionDto.setOptions(cmsQuestionOptionsDtos);
                    }
                }
            }
        }
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
     * 复数资源，搜索调查
     *
     * @param dto
     *
     * @return
     */
    @OperationLog(operation = "调查管理", content = "复数资源，搜索调查")
    @RequiresPermissions("cms:survey:search")
    @RequestMapping(value = "/surveys", method = RequestMethod.GET)
    public ResponseEntity search(SearchCmsSurveysConditionDto dto) {

        ResponseJsonRender resultData = new ResponseJsonRender();
        PageAndOrderbyParamDto pageAndOrderbyParamDto = new PageAndOrderbyParamDto(PageUtils.getPageFromThreadLocal(), OrderbyUtils.getOrderbyFromThreadLocal());
        // 设置当前登录用户id
        dto.setCurrentUserId(getLoginUser().getId());
        dto.setCurrentRoleId(((BaseRoleDto) getLoginUser().getRole()).getId());
        PageResultDto<CmsSurveyDto> list = apiCmsSurveyPoService.searchCmsSurveysDsf(dto, pageAndOrderbyParamDto);

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
