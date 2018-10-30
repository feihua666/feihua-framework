package com.feihua.framework.rest.modules.message.mvc;

import com.feihua.framework.rest.ResponseJsonRender;
import com.feihua.framework.rest.interceptor.RepeatFormValidator;
import com.feihua.framework.rest.modules.common.mvc.BaseController;
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
import com.feihua.framework.base.modules.message.dto.BaseMessageTemplateDto;
import com.feihua.framework.base.modules.message.dto.SearchBaseMessageTemplatesConditionDto;
import com.feihua.framework.base.modules.message.api.ApiBaseMessageTemplatePoService;
import com.feihua.framework.rest.modules.message.dto.AddBaseMessageTemplateFormDto;
import com.feihua.framework.rest.modules.message.dto.UpdateBaseMessageTemplateFormDto;
import com.feihua.framework.base.modules.message.po.BaseMessageTemplatePo;

import java.util.List;

/**
 * 消息模板管理
 * Created by yangwei
 */
@RestController
@RequestMapping("/base")
public class BaseMessageTemplateController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(BaseMessageTemplateController.class);

    @Autowired
    private ApiBaseMessageTemplatePoService apiBaseMessageTemplatePoService;

    /**
     * 单资源，添加
     * @param addFormDto
     * @return
     */
    @RepeatFormValidator
    @RequiresPermissions("messageTemplate:add")
    @RequestMapping(value = "/message/template",method = RequestMethod.POST)
    public ResponseEntity add(AddBaseMessageTemplateFormDto addFormDto){
        logger.info("添加消息模板开始");
        logger.info("当前登录用户id:{}",getLoginUser().getId());
        ResponseJsonRender resultData=new ResponseJsonRender();
        // 检查code 是否存在
        BaseMessageTemplatePo basePoCheckCondition = new BaseMessageTemplatePo();
        basePoCheckCondition.setCode(addFormDto.getCode());
        basePoCheckCondition.setDelFlag(BasePo.YesNo.N.name());

        List dblist = apiBaseMessageTemplatePoService.selectListSimple(basePoCheckCondition);
        if (dblist != null && !dblist.isEmpty()) {
            // 添加失败
            resultData.setCode(ResponseCode.E409_100001.getCode());
            resultData.setMsg(ResponseCode.E409_100001.getMsg());
            logger.info("code:{},msg:{}",resultData.getCode(),resultData.getMsg());
            logger.info("添加消息模板结束，失败");
            return new ResponseEntity(resultData,HttpStatus.CONFLICT);
        }

        // 表单值设置
        BaseMessageTemplatePo basePo = new BaseMessageTemplatePo();
        basePo.setName(addFormDto.getName());
        basePo.setCode(addFormDto.getCode());
        basePo.setContent(addFormDto.getContent());

        apiBaseMessageTemplatePoService.preInsert(basePo,getLoginUser().getId());
        BaseMessageTemplateDto r = apiBaseMessageTemplatePoService.insert(basePo);
        if (r == null) {
            // 添加失败
            resultData.setCode(ResponseCode.E404_100001.getCode());
            resultData.setMsg(ResponseCode.E404_100001.getMsg());
            logger.info("code:{},msg:{}",resultData.getCode(),resultData.getMsg());
            logger.info("添加消息模板结束，失败");
            return new ResponseEntity(resultData,HttpStatus.NOT_FOUND);
        }else{
            // 添加成功，返回添加的数据
            resultData.setData(r);
            logger.info("添加消息模板id:{}",r.getId());
            logger.info("添加消息模板结束，成功");
            return new ResponseEntity(resultData, HttpStatus.CREATED);
        }
    }

    /**
     * 单资源，删除
     * @param id
     * @return
     */
    @RepeatFormValidator
    @RequiresPermissions("messageTemplate:delete")
    @RequestMapping(value = "/message/template/{id}",method = RequestMethod.DELETE)
    public ResponseEntity delete(@PathVariable String id){
        logger.info("删除消息模板开始");
        logger.info("用户id:{}",getLoginUser().getId());
        logger.info("消息模板id:{}",id);
        ResponseJsonRender resultData=new ResponseJsonRender();

            int r = apiBaseMessageTemplatePoService.deleteFlagByPrimaryKeyWithUpdateUser(id,getLoginUser().getId());
            if (r <= 0) {
                // 删除失败，可能没有找到资源
                resultData.setCode(ResponseCode.E404_100001.getCode());
                resultData.setMsg(ResponseCode.E404_100001.getMsg());
                logger.info("code:{},msg:{}",resultData.getCode(),resultData.getMsg());
                logger.info("删除消息模板结束，失败");
                return new ResponseEntity(resultData,HttpStatus.NOT_FOUND);
            }else{
                // 删除成功
                logger.info("删除的消息模板id:{}",id);
                logger.info("删除消息模板结束，成功");
                return new ResponseEntity(resultData,HttpStatus.NO_CONTENT);
            }
    }

    /**
     * 单资源，更新
     * @param id
     * @param updateFormDto
     * @return
     */
    @RepeatFormValidator
    @RequiresPermissions("messageTemplate:update")
    @RequestMapping(value = "/message/template/{id}",method = RequestMethod.PUT)
    public ResponseEntity update(@PathVariable String id, UpdateBaseMessageTemplateFormDto updateFormDto){
        logger.info("更新消息模板开始");
        logger.info("当前登录用户id:{}",getLoginUser().getId());
        logger.info("消息模板id:{}",id);
        ResponseJsonRender resultData=new ResponseJsonRender();

        // 检查code 是否存在
        BaseMessageTemplatePo basePoCheckCondition = new BaseMessageTemplatePo();
        basePoCheckCondition.setCode(updateFormDto.getCode());
        basePoCheckCondition.setDelFlag(BasePo.YesNo.N.name());

        List<BaseMessageTemplatePo> dblist = apiBaseMessageTemplatePoService.selectListSimple(basePoCheckCondition);
        if (dblist != null && !dblist.isEmpty() && !dblist.get(0).getId().equals(id)) {
            // 添加失败
            resultData.setCode(ResponseCode.E409_100001.getCode());
            resultData.setMsg(ResponseCode.E409_100001.getMsg());
            logger.info("code:{},msg:{}",resultData.getCode(),resultData.getMsg());
            logger.info("添加消息模板结束，失败");
            return new ResponseEntity(resultData,HttpStatus.CONFLICT);
        }

        // 表单值设置
        BaseMessageTemplatePo basePo = new BaseMessageTemplatePo();
        // id
        basePo.setId(id);
        basePo.setName(updateFormDto.getName());
        basePo.setCode(updateFormDto.getCode());
        basePo.setContent(updateFormDto.getContent());

        // 用条件更新，乐观锁机制
        BaseMessageTemplatePo basePoCondition = new BaseMessageTemplatePo();
        basePoCondition.setId(id);
        basePoCondition.setDelFlag(BasePo.YesNo.N.name());
        basePoCondition.setUpdateAt(updateFormDto.getUpdateTime());
        apiBaseMessageTemplatePoService.preUpdate(basePo,getLoginUser().getId());
        int r = apiBaseMessageTemplatePoService.updateSelective(basePo,basePoCondition);
        if (r <= 0) {
            // 更新失败，资源不存在
            resultData.setCode(ResponseCode.E404_100001.getCode());
            resultData.setMsg(ResponseCode.E404_100001.getMsg());
            logger.info("code:{},msg:{}",resultData.getCode(),resultData.getMsg());
            logger.info("更新消息模板结束，失败");
            return new ResponseEntity(resultData,HttpStatus.NOT_FOUND);
        }else{
            // 更新成功，已被成功创建
            logger.info("更新的消息模板id:{}",id);
            logger.info("更新消息模板结束，成功");
            return new ResponseEntity(resultData, HttpStatus.CREATED);
        }
    }

    /**
     * 单资源，获取id消息模板
     * @param id
     * @return
     */
    @RepeatFormValidator
    @RequiresPermissions("messageTemplate:getById")
    @RequestMapping(value = "/message/template/{id}",method = RequestMethod.GET)
    public ResponseEntity getById(@PathVariable String id){

        ResponseJsonRender resultData=new ResponseJsonRender();
        BaseMessageTemplateDto baseDataScopeDto = apiBaseMessageTemplatePoService.selectByPrimaryKey(id,false);
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
     * 复数资源，搜索消息模板
     * @param dto
     * @return
     */
    @RepeatFormValidator
    @RequiresPermissions("messageTemplate:search")
    @RequestMapping(value = "/message/templates",method = RequestMethod.GET)
    public ResponseEntity search(SearchBaseMessageTemplatesConditionDto dto){

        ResponseJsonRender resultData=new ResponseJsonRender();
        PageAndOrderbyParamDto pageAndOrderbyParamDto = new PageAndOrderbyParamDto(PageUtils.getPageFromThreadLocal(), OrderbyUtils.getOrderbyFromThreadLocal());
        // 设置当前登录用户id
        dto.setCurrentUserId(getLoginUser().getId());
        dto.setCurrentRoleId(getLoginUserRoleId());
        PageResultDto<BaseMessageTemplateDto> list = apiBaseMessageTemplatePoService.searchBaseMessageTemplatesDsf(dto,pageAndOrderbyParamDto);

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
