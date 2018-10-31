package com.feihua.wechat.rest.publicplatform.mvc;

import com.feihua.framework.base.modules.role.dto.BaseRoleDto;
import com.feihua.framework.rest.ResponseJsonRender;
import com.feihua.framework.rest.interceptor.RepeatFormValidator;
import com.feihua.framework.rest.mvc.SuperController;
import com.feihua.utils.http.httpServletResponse.ResponseCode;
import com.feihua.wechat.publicplatform.PublicUtils;
import com.feihua.wechat.publicplatform.api.ApiWeixinAccountPoService;
import com.feihua.wechat.publicplatform.dto.SearchWeixinAccountsConditionDto;
import com.feihua.wechat.publicplatform.dto.WeixinAccountDto;
import com.feihua.wechat.publicplatform.dto.WxTemplate;
import com.feihua.wechat.publicplatform.po.WeixinAccountPo;
import feihua.jdbc.api.pojo.PageAndOrderbyParamDto;
import feihua.jdbc.api.pojo.PageResultDto;
import feihua.jdbc.api.utils.OrderbyUtils;
import feihua.jdbc.api.utils.PageUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 微信账号管理
 * Created by revolver
 */
@RestController
@RequestMapping("/weixinaccount")
public class WeixinAccountController extends SuperController {

    private static Logger logger = LoggerFactory.getLogger(WeixinAccountController.class);

    @Autowired
    private ApiWeixinAccountPoService apiWeixinAccountPoService;

    /**
     * 单资源，添加
     *
     * @param dto
     *
     * @return
     */
    @RepeatFormValidator
    @RequiresPermissions("weixinaccount:add")
    @RequestMapping(value = "/account", method = RequestMethod.POST)
    public ResponseEntity add(@RequestBody WeixinAccountDto dto) {
        logger.info("添加微信账号开始");
        logger.info("当前登录用户id:{}", getLoginUser().getId());
        ResponseJsonRender resultData = new ResponseJsonRender();
        // 表单值设置
        WeixinAccountPo basePo = new WeixinAccountPo();
        //todo
        BeanUtils.copyProperties(dto, basePo);

        apiWeixinAccountPoService.preInsert(basePo, getLoginUser().getId());
        WeixinAccountDto r = apiWeixinAccountPoService.insert(basePo);
        if (r == null) {
            // 添加失败
            resultData.setCode(ResponseCode.E404_100001.getCode());
            resultData.setMsg(ResponseCode.E404_100001.getMsg());
            logger.info("code:{},msg:{}", resultData.getCode(), resultData.getMsg());
            logger.info("添加微信账号结束，失败");
            return new ResponseEntity(resultData, HttpStatus.NOT_FOUND);
        } else {
            // 添加成功，返回添加的数据
            resultData.setData(r);
            logger.info("添加微信账号id:{}", r.getId());
            logger.info("添加微信账号结束，成功");
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
    @RepeatFormValidator
    @RequiresPermissions("weixinaccount:delete")
    @RequestMapping(value = "/account/{id}", method = RequestMethod.DELETE)
    public ResponseEntity delete(@PathVariable String id) {
        logger.info("删除微信账号开始");
        logger.info("用户id:{}", getLoginUser().getId());
        logger.info("微信账号id:{}", id);
        ResponseJsonRender resultData = new ResponseJsonRender();

        int r = apiWeixinAccountPoService.deleteFlagByPrimaryKeyWithUpdateUser(id, getLoginUser().getId());
        if (r <= 0) {
            // 删除失败，可能没有找到资源
            resultData.setCode(ResponseCode.E404_100001.getCode());
            resultData.setMsg(ResponseCode.E404_100001.getMsg());
            logger.info("code:{},msg:{}", resultData.getCode(), resultData.getMsg());
            logger.info("删除微信账号结束，失败");
            return new ResponseEntity(resultData, HttpStatus.NOT_FOUND);
        } else {
            // 删除成功
            logger.info("删除的微信账号id:{}", id);
            logger.info("删除微信账号结束，成功");
            return new ResponseEntity(resultData, HttpStatus.NO_CONTENT);
        }
    }

    /**
     * 单资源，更新
     *
     * @param dto
     *
     * @return
     */
    @RepeatFormValidator
    @RequiresPermissions("weixinaccount:update")
    @RequestMapping(value = "/account", method = RequestMethod.PUT)
    public ResponseEntity update(@RequestBody WeixinAccountDto dto) {
        logger.info("更新微信账号开始");
        logger.info("当前登录用户id:{}", getLoginUser().getId());
        ResponseJsonRender resultData = new ResponseJsonRender();
        // 表单值设置
        WeixinAccountPo basePo = new WeixinAccountPo();
        //todo
        BeanUtils.copyProperties(dto, basePo);
        apiWeixinAccountPoService.preUpdate(basePo, getLoginUser().getId());
        int r = apiWeixinAccountPoService.updateByPrimaryKeySelective(basePo);
        if (r <= 0) {
            // 更新失败，资源不存在
            resultData.setCode(ResponseCode.E404_100001.getCode());
            resultData.setMsg(ResponseCode.E404_100001.getMsg());
            logger.info("code:{},msg:{}", resultData.getCode(), resultData.getMsg());
            logger.info("更新微信账号结束，失败");
            return new ResponseEntity(resultData, HttpStatus.NOT_FOUND);
        } else {
            // 更新成功，已被成功创建
            logger.info("更新微信账号结束，成功");
            return new ResponseEntity(resultData, HttpStatus.CREATED);
        }
    }

    /**
     * 单资源，获取id微信账号
     *
     * @param id
     *
     * @return
     */
    @RepeatFormValidator
    @RequiresPermissions("weixinaccount:getById")
    @RequestMapping(value = "/account/{id}", method = RequestMethod.GET)
    public ResponseEntity getById(@PathVariable String id) {

        ResponseJsonRender resultData = new ResponseJsonRender();
        WeixinAccountDto baseDataScopeDto = apiWeixinAccountPoService.selectByPrimaryKey(id, false);
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
     * 复数资源，搜索微信账号
     *
     * @param dto
     *
     * @return
     */
    @RepeatFormValidator
    @RequiresPermissions("weixinaccount:search")
    @RequestMapping(value = "/accounts", method = RequestMethod.GET)
    public ResponseEntity search(SearchWeixinAccountsConditionDto dto) {

        ResponseJsonRender resultData = new ResponseJsonRender();
        PageAndOrderbyParamDto pageAndOrderbyParamDto = new PageAndOrderbyParamDto(PageUtils.getPageFromThreadLocal(), OrderbyUtils.getOrderbyFromThreadLocal());
        // 设置当前登录用户id
        dto.setCurrentUserId(getLoginUser().getId());
        BaseRoleDto roleDto = (BaseRoleDto) getLoginUser().getRole();
        dto.setCurrentRoleId(roleDto.getId());
        PageResultDto<WeixinAccountDto> list = apiWeixinAccountPoService.searchWeixinAccountsDsf(dto, pageAndOrderbyParamDto);

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

    /**
     * 获取已添加至帐号下所有模板列表，可在微信公众平台后台中查看模板列表信息
     *
     * @param which
     *
     * @return
     */
    @RepeatFormValidator
    @RequiresPermissions("weixinaccount:wxalltemplates")
    @RequestMapping(value = "/wxalltemplates/{which}", method = RequestMethod.GET)
    public ResponseEntity getWxAllPrivateTemplates(@PathVariable String which) {
        if (StringUtils.isEmpty(which)) return null;
        ResponseJsonRender resultData = new ResponseJsonRender();
        List<WxTemplate> wxAllPrivateTemplate = null;
        try {
            wxAllPrivateTemplate = PublicUtils.getWxAllPrivateTemplate(which);
        } catch (Exception e) {
           logger.error(e.getMessage(),e);
        }
        if (wxAllPrivateTemplate != null) {
            resultData.setData(wxAllPrivateTemplate);
            return new ResponseEntity(resultData, HttpStatus.OK);
        } else {
            resultData.setCode(ResponseCode.E404_100001.getCode());
            resultData.setMsg(ResponseCode.E404_100001.getMsg());
            return new ResponseEntity(resultData, HttpStatus.NOT_FOUND);
        }
    }
}
