package com.feihua.framework.statistic.mvc;

import com.feihua.framework.base.modules.role.dto.BaseRoleDto;
import com.feihua.framework.base.modules.user.api.ApiBaseUserPoService;
import com.feihua.framework.base.modules.user.dto.BaseUserDto;
import com.feihua.framework.rest.ResponseJsonRender;
import com.feihua.framework.rest.interceptor.RepeatFormValidator;
import com.feihua.framework.rest.mvc.SuperController;
import com.feihua.framework.statistic.api.ApiStatisticRecordPageViewPoService;
import com.feihua.framework.statistic.dto.AddStatisticRecordPageViewFormDto;
import com.feihua.framework.statistic.dto.SearchStatisticRecordPageViewsConditionDto;
import com.feihua.framework.statistic.dto.StatisticRecordPageViewDto;
import com.feihua.framework.statistic.po.StatisticRecordPageViewPo;
import com.feihua.utils.http.httpServletResponse.ResponseCode;
import feihua.jdbc.api.pojo.BasePo;
import feihua.jdbc.api.pojo.PageAndOrderbyParamDto;
import feihua.jdbc.api.pojo.PageResultDto;
import feihua.jdbc.api.utils.OrderbyUtils;
import feihua.jdbc.api.utils.PageUtils;
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
/**
 * 页面访问记录
 * Created by yangwei
 */
@RestController
@RequestMapping("/statistic/page")
public class StatisticRecordPageViewController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(StatisticRecordPageViewController.class);

    @Autowired
    private ApiStatisticRecordPageViewPoService apiStatisticRecordPageViewPoService;
    @Autowired
    private ApiBaseUserPoService apiBaseUserPoService;

    /**
     * 单资源，添加
     * @param dto
     * @return
     */
    @RepeatFormValidator
    @RequestMapping(value = "/view",method = RequestMethod.POST)
    public ResponseEntity add(AddStatisticRecordPageViewFormDto dto){
        logger.info("添加页面访问记录开始");
        logger.info("当前登录用户id:{}",getLoginUser().getId());
        ResponseJsonRender resultData = new ResponseJsonRender();
        // 表单值设置
        StatisticRecordPageViewPo basePo = new StatisticRecordPageViewPo();

        basePo.setUrl(dto.getUrl());
        basePo.setType(dto.getType());
        basePo.setContentId(dto.getContentId());
        basePo.setContentName(dto.getContentName());
        basePo.setWhereFrom(dto.getWhereFrom());
        basePo.setUserId(getLoginUserId());
        basePo.setUserNickname(getLoginUserNickname());
        basePo.setFromUserId(dto.getFromUserId());
        if (StringUtils.isNotEmpty(dto.getFromUserId())) {
            BaseUserDto baseUserDto = apiBaseUserPoService.selectByPrimaryKey(dto.getFromUserId());
            if (baseUserDto != null) {
                basePo.setFromUserNickname(baseUserDto.getNickname());
            }
        }
        String userId = getLoginUserId();
        if (StringUtils.isEmpty(userId)) {
            userId = BasePo.DEFAULT_USER_ID;
        }
        basePo = apiStatisticRecordPageViewPoService.preInsert(basePo,userId);
        StatisticRecordPageViewDto r = apiStatisticRecordPageViewPoService.insert(basePo);
        if (r == null) {
            // 添加失败
            resultData.setCode(ResponseCode.E404_100001.getCode());
            resultData.setMsg(ResponseCode.E404_100001.getMsg());
            logger.info("code:{},msg:{}",resultData.getCode(),resultData.getMsg());
            logger.info("添加页面访问记录结束，失败");
            return new ResponseEntity(resultData,HttpStatus.NOT_FOUND);
        }else{
            // 添加成功，返回添加的数据
            resultData.setData(r);
            logger.info("添加页面访问记录id:{}",r.getId());
            logger.info("添加页面访问记录结束，成功");
            return new ResponseEntity(resultData, HttpStatus.CREATED);
        }
    }

    /**
     * 单资源，删除
     * @param id
     * @return
     */
    @RepeatFormValidator
    @RequiresPermissions("statistic:page:viewdelete")
    @RequestMapping(value = "/view/{id}",method = RequestMethod.DELETE)
    public ResponseEntity delete(@PathVariable String id){
        logger.info("删除页面访问记录开始");
        logger.info("用户id:{}",getLoginUser().getId());
        logger.info("页面访问记录id:{}",id);
        ResponseJsonRender resultData = new ResponseJsonRender();

            int r = apiStatisticRecordPageViewPoService.deleteFlagByPrimaryKeyWithUpdateUser(id,getLoginUser().getId());
            if (r <= 0) {
                // 删除失败，可能没有找到资源
                resultData.setCode(ResponseCode.E404_100001.getCode());
                resultData.setMsg(ResponseCode.E404_100001.getMsg());
                logger.info("code:{},msg:{}",resultData.getCode(),resultData.getMsg());
                logger.info("删除页面访问记录结束，失败");
                return new ResponseEntity(resultData,HttpStatus.NOT_FOUND);
            }else{
                // 删除成功
                logger.info("删除的页面访问记录id:{}",id);
                logger.info("删除页面访问记录结束，成功");
                return new ResponseEntity(resultData,HttpStatus.NO_CONTENT);
            }
    }

    /**
     * 单资源，获取id页面访问记录
     * @param id
     * @return
     */
    @RequiresPermissions("statistic:page:viewgetById")
    @RequestMapping(value = "/view/{id}",method = RequestMethod.GET)
    public ResponseEntity getById(@PathVariable String id){

        ResponseJsonRender resultData = new ResponseJsonRender();
        StatisticRecordPageViewDto baseDataScopeDto = apiStatisticRecordPageViewPoService.selectByPrimaryKey(id,false);
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
     * 复数资源，搜索页面访问记录
     * @param dto
     * @return
     */
    @RepeatFormValidator
    @RequiresPermissions("statistic:page:viewsearch")
    @RequestMapping(value = "/views",method = RequestMethod.GET)
    public ResponseEntity search(SearchStatisticRecordPageViewsConditionDto dto){

        ResponseJsonRender resultData = new ResponseJsonRender();
        PageAndOrderbyParamDto pageAndOrderbyParamDto = new PageAndOrderbyParamDto(PageUtils.getPageFromThreadLocal(), OrderbyUtils.getOrderbyFromThreadLocal());
        // 设置当前登录用户id
        dto.setCurrentUserId(getLoginUser().getId());
        dto.setCurrentRoleId(getLoginUserRoleId());
        dto.setCurrentPostId(getLoginUserPostId());
        PageResultDto<StatisticRecordPageViewDto> list = apiStatisticRecordPageViewPoService.searchStatisticRecordPageViewsDsf(dto,pageAndOrderbyParamDto);

        resultData.setPage(list.getPage());
        return returnList(list.getData(),resultData);

    }
}
