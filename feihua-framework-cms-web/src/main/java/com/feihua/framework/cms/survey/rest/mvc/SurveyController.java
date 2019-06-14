package com.feihua.framework.cms.survey.rest.mvc;

import com.feihua.framework.base.modules.role.dto.BaseRoleDto;
import com.feihua.framework.cms.CmsConstants;
import com.feihua.framework.cms.admin.rest.mvc.BaseController;
import com.feihua.framework.cms.api.ApiCmsQuestionOptionsPoService;
import com.feihua.framework.cms.api.ApiCmsQuestionPoService;
import com.feihua.framework.cms.api.ApiCmsSurveyAnswerPoService;
import com.feihua.framework.cms.api.ApiCmsSurveyPoService;
import com.feihua.framework.cms.dto.*;
import com.feihua.framework.cms.po.CmsQuestionOptionsPo;
import com.feihua.framework.cms.po.CmsQuestionPo;
import com.feihua.framework.cms.po.CmsSurveyAnswerPo;
import com.feihua.framework.cms.po.CmsSurveyPo;
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
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.orderbyhelper.OrderByHelper;

import java.util.List;

/**
 * 调查管理 -前台
 * Created by yangwei
 */
@Controller
@RequestMapping("survey")
public class SurveyController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(SurveyController.class);

    @Autowired
    private ApiCmsSurveyPoService apiCmsSurveyPoService;

    @Autowired
    private ApiCmsQuestionPoService apiCmsQuestionPoService;

    @Autowired
    private ApiCmsQuestionOptionsPoService apiCmsQuestionOptionsPoService;

    @Autowired
    private ApiCmsSurveyAnswerPoService apiCmsSurveyAnswerPoService;

    /**
     * 单资源，获取id调查
     *
     * @param id
     *
     * @return
     */
    @ResponseBody
    @OperationLog(operation = "调查管理-前台", content = "单资源，获取id调查、问题、选项")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity getSurveyById(@PathVariable String id) {

        ResponseJsonRender resultData = new ResponseJsonRender();
        CmsSurveyDto baseDataScopeDto = getSurveyBy(id);
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
     * @param cmsSurveyAnswerDtos
     *
     * @return
     */
    @ResponseBody
    @OperationLog(operation = "调查管理-前台", content = "保存调查参与答案")
    @RequestMapping(value = "/answers/add/{surveyId}", method = RequestMethod.POST)
    public ResponseEntity saveAnswers(@PathVariable("surveyId") String surveyId, @RequestBody List<CmsSurveyAnswerDto> cmsSurveyAnswerDtos) {

        ResponseJsonRender resultData = new ResponseJsonRender();

        if (CollectionUtils.isNotEmpty(cmsSurveyAnswerDtos)) {
            for (CmsSurveyAnswerDto dto : cmsSurveyAnswerDtos) {
                // 表单值设置
                CmsSurveyAnswerPo basePo = new CmsSurveyAnswerPo();
                basePo.setId(dto.getId());
                basePo.setSurveyId(dto.getSurveyId());
                basePo.setQuestionId(dto.getQuestionId());
                basePo.setAnswers(dto.getAnswers());
                basePo.setDataUserId(dto.getDataUserId());
                basePo.setDataOfficeId(dto.getDataOfficeId());
                basePo.setDataType(dto.getDataType());
                basePo.setDataAreaId(dto.getDataAreaId());
                basePo.setDelFlag(dto.getDelFlag());
                basePo.setCreateAt(dto.getCreateAt());
                basePo.setCreateBy(dto.getCreateBy());
                basePo.setUpdateAt(dto.getUpdateAt());
                basePo.setUpdateBy(dto.getUpdateBy());
                basePo = apiCmsSurveyAnswerPoService.preInsert(basePo, getLoginUser() == null ? "" : getLoginUser().getId());
                apiCmsSurveyAnswerPoService.insert(basePo);
            }
        }
        //更新参与量
        CmsSurveyPo cmsSurveyPo = apiCmsSurveyPoService.selectByPrimaryKeySimple(surveyId);
        Integer count = cmsSurveyPo.getCount() == null ? 0 : cmsSurveyPo.getCount();
        cmsSurveyPo.setCount(count + 1);
        int i = apiCmsSurveyPoService.updateByPrimaryKey(cmsSurveyPo);
        if (i == 0) {
            // 添加失败
            resultData.setCode(ResponseCode.E404_100001.getCode());
            resultData.setMsg(ResponseCode.E404_100001.getMsg());
            return new ResponseEntity(resultData, HttpStatus.NOT_FOUND);
        } else {
            // 添加成功，返回添加的数据
            return new ResponseEntity(resultData, HttpStatus.CREATED);
        }
    }

    /**
     * 复数资源，搜索调查
     *
     * @param dto
     *
     * @return
     */
    @ResponseBody
    @OperationLog(operation = "调查管理-前台", content = "复数资源，搜索调查")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ResponseEntity search(SearchCmsSurveysConditionDto dto) {

        ResponseJsonRender resultData = new ResponseJsonRender();
        PageAndOrderbyParamDto pageAndOrderbyParamDto = new PageAndOrderbyParamDto(PageUtils.getPageFromThreadLocal(), OrderbyUtils.getOrderbyFromThreadLocal());
        // 设置当前登录用户id
        dto.setCurrentUserId(getLoginUser().getId());
        dto.setCurrentRoleId(getLoginUserRoleId());
        dto.setCurrentPostId(getLoginUserPostId());
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

    @OperationLog(operation = "调查管理-前台", content = "调查首页")
    @RequestMapping(value = {"/index.htm"}, method = RequestMethod.GET)
    public String index(@RequestParam(value = "id", required = false) String id, Model model) {
        CmsSurveyPo query = new CmsSurveyPo();
        query.setStatus("2");
        query.setDelFlag(BasePo.YesNo.N.name());
        OrderByHelper.orderBy("sequence desc,create_at");
        final List<CmsSurveyDto> cmsSurveyDtos = apiCmsSurveyPoService.selectList(query);
        CmsSurveyDto survey = new CmsSurveyDto();

        if (StringUtils.isNotBlank(id)) {
            survey = getSurveyBy(id);
        } else if (CollectionUtils.isNotEmpty(cmsSurveyDtos)) {
            survey = getSurveyBy(cmsSurveyDtos.get(0).getId());
        }
        model.addAttribute("survey", survey);
        model.addAttribute("surveys", cmsSurveyDtos);
        String templatePath = "/WEB-INF/template-cms/default/survey/" + RequestUtils.wrapStartSlash(CmsConstants.indexHtml);
        return templatePath;
    }

    private CmsSurveyDto getSurveyBy(String id) {
        CmsSurveyDto cmsSurveyDto = apiCmsSurveyPoService.selectByPrimaryKey(id, false);
        CmsQuestionPo question = new CmsQuestionPo();
        question.setSurveyId(id);
        question.setDelFlag(BasePo.YesNo.N.name());
        OrderByHelper.orderBy("sequence desc,create_at");
        List<CmsQuestionDto> cmsQuestionDtos = apiCmsQuestionPoService.selectList(question);
        if (CollectionUtils.isNotEmpty(cmsQuestionDtos)) {
            cmsSurveyDto.setQuestions(cmsQuestionDtos);
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
        return cmsSurveyDto;
    }

}
