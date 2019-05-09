package com.feihua.framework.message.mvc;

import com.feihua.framework.base.modules.role.dto.BaseRoleDto;
import com.feihua.framework.constants.DictEnum;
import com.feihua.framework.message.MsgTemplateUtils;
import com.feihua.framework.message.api.ApiBaseMessageTemplatePoService;
import com.feihua.framework.message.api.ApiBaseMessageTemplateThirdBindPoService;
import com.feihua.framework.message.dto.*;
import com.feihua.framework.message.po.BaseMessageTemplatePo;
import com.feihua.framework.message.po.BaseMessageTemplateThirdBindPo;
import com.feihua.framework.rest.ResponseJsonRender;
import com.feihua.framework.rest.interceptor.RepeatFormValidator;
import com.feihua.framework.rest.mvc.SuperController;
import com.feihua.utils.collection.CollectionUtils;
import com.feihua.utils.http.httpServletResponse.ResponseCode;
import com.feihua.utils.string.RegexUtils;
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

import java.util.ArrayList;
import java.util.List;

/**
 * 消息模板管理
 * Created by yangwei
 */
@RestController
@RequestMapping("/base")
public class BaseMessageTemplateController extends SuperController {

    private static Logger logger = LoggerFactory.getLogger(BaseMessageTemplateController.class);

    @Autowired
    private ApiBaseMessageTemplatePoService apiBaseMessageTemplatePoService;
    @Autowired
    private ApiBaseMessageTemplateThirdBindPoService apiBaseMessageTemplateThirdBindPoService;

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
        basePo.setTitle(addFormDto.getTitle());
        basePo.setProfile(addFormDto.getProfile());
        basePo.setMsgLevel(addFormDto.getMsgLevel());
        basePo.setMsgType(addFormDto.getMsgType());

        basePo = apiBaseMessageTemplatePoService.preInsert(basePo,getLoginUser().getId());
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
        basePo.setTitle(updateFormDto.getTitle());
        basePo.setProfile(updateFormDto.getProfile());
        basePo.setMsgLevel(updateFormDto.getMsgLevel());
        basePo.setMsgType(updateFormDto.getMsgType());

        // 用条件更新，乐观锁机制
        BaseMessageTemplatePo basePoCondition = new BaseMessageTemplatePo();
        basePoCondition.setId(id);
        basePoCondition.setDelFlag(BasePo.YesNo.N.name());
        basePoCondition.setUpdateAt(updateFormDto.getUpdateTime());
        basePo = apiBaseMessageTemplatePoService.preUpdate(basePo,getLoginUser().getId());
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
    @RequiresPermissions("messageTemplate:search")
    @RequestMapping(value = "/message/templates",method = RequestMethod.GET)
    public ResponseEntity search(SearchBaseMessageTemplatesConditionDto dto){

        ResponseJsonRender resultData=new ResponseJsonRender();
        PageAndOrderbyParamDto pageAndOrderbyParamDto = new PageAndOrderbyParamDto(PageUtils.getPageFromThreadLocal(), OrderbyUtils.getOrderbyFromThreadLocal());
        // 设置当前登录用户id
        dto.setCurrentUserId(getLoginUser().getId());
        dto.setCurrentRoleId(((BaseRoleDto) getLoginUser().getRole()).getId());
        PageResultDto<BaseMessageTemplateDto> list = apiBaseMessageTemplatePoService.searchBaseMessageTemplatesDsf(dto,pageAndOrderbyParamDto);

        if(!CollectionUtils.isNullOrEmpty(list.getData())){
            resultData.setData(list.getData());
            resultData.setPage(list.getPage());
            return new ResponseEntity(resultData, HttpStatus.OK);
        }else{
            resultData.setCode(ResponseCode.E404_100001.getCode());
            resultData.setMsg(ResponseCode.E404_100001.getMsg());
            return new ResponseEntity(resultData,HttpStatus.NOT_FOUND);
        }
    }

    // 以下是绑定三方模板相关
    @RequiresPermissions("messageTemplate:third:getById")
    @RequestMapping(value = "/message/template/{id}/third",method = RequestMethod.GET)
    public ResponseEntity thirdGetById(@PathVariable String id){

        ResponseJsonRender resultData=new ResponseJsonRender();
        BaseMessageTemplateThirdBindPo condition = new BaseMessageTemplateThirdBindPo();
        condition.setMessageTemplateId(id);
        condition.setDelFlag(BasePo.YesNo.N.name());
        List<BaseMessageTemplateThirdBindDto> list = apiBaseMessageTemplateThirdBindPoService.selectList(condition);
        return returnList(list,resultData);
    }

    /**
     * 绑定/删除三方模板
     * 如果不选择三方模板，视为删除先前绑定的三方模板，只填写模板内容无效，请谨慎操作
     * @param id
     * @param dto
     * @return
     */
    @RequiresPermissions("messageTemplate:third:bind")
    @RequestMapping(value = "/message/template/{id}/third",method = RequestMethod.POST)
    public ResponseEntity channelBind(@PathVariable String id, ThirdTemplateBindFormDto dto){

        ResponseJsonRender resultData=new ResponseJsonRender();
        // 先删除再添加

        BaseMessageTemplateThirdBindPo deleteConditon = new BaseMessageTemplateThirdBindPo();
        deleteConditon.setMessageTemplateId(id);
        apiBaseMessageTemplateThirdBindPoService.deleteSelective(deleteConditon);

        List<BaseMessageTemplateThirdBindPo> tobeInsert = new ArrayList<>();
        // 微信公众帐号
        List<WeixinPublicPlatform> weixinPublicPlatforms = dto.getWeixinPublicPlatforms();
        if (!CollectionUtils.isNullOrEmpty(weixinPublicPlatforms)) {
            for (WeixinPublicPlatform weixinPublicPlatform : weixinPublicPlatforms) {
                if(StringUtils.isNotEmpty(weixinPublicPlatform.getWeixinPublicplatformAccoutId())){
                    BaseMessageTemplateThirdBindPo weixinPublic = new BaseMessageTemplateThirdBindPo();
                    weixinPublic.setThirdType(DictEnum.WxAccountType.weixin_publicplatform.name());
                    weixinPublic.setMessageTemplateId(id);
                    weixinPublic.setThirdId(weixinPublicPlatform.getWeixinPublicplatformAccoutId());
                    weixinPublic.setThirdTemplateContent(weixinPublicPlatform.getWeixinPublicplatformMsgTemplateContent());
                    weixinPublic.setThirdTemplateId(weixinPublicPlatform.getWeixinPublicplatformMsgTemplateId());
                    weixinPublic = apiBaseMessageTemplateThirdBindPoService.preInsert(weixinPublic,getLoginUserId());
                    tobeInsert.add(weixinPublic);
                }
            }
        }

        // 微信小程序
        List<WeixinMiniProgram> weixinMiniPrograms = dto.getWeixinMiniPrograms();
        if (!CollectionUtils.isNullOrEmpty(weixinMiniPrograms)) {
            for (WeixinMiniProgram weixinMiniProgram : weixinMiniPrograms) {
                if(StringUtils.isNotEmpty(weixinMiniProgram.getWeixinMiniprogramAccoutId())){
                    BaseMessageTemplateThirdBindPo weixinMiniprogram = new BaseMessageTemplateThirdBindPo();
                    weixinMiniprogram.setThirdType(DictEnum.WxAccountType.weixin_miniprogram.name());
                    weixinMiniprogram.setMessageTemplateId(id);
                    weixinMiniprogram.setThirdId(weixinMiniProgram.getWeixinMiniprogramAccoutId());
                    weixinMiniprogram.setThirdTemplateContent(weixinMiniProgram.getWeixinMiniprogramMsgTemplateContent());
                    weixinMiniprogram.setThirdTemplateId(weixinMiniProgram.getWeixinMiniprogramMsgTemplateId());
                    weixinMiniprogram = apiBaseMessageTemplateThirdBindPoService.preInsert(weixinMiniprogram,getLoginUserId());
                    tobeInsert.add(weixinMiniprogram);

                }
            }
        }
        if (!CollectionUtils.isNullOrEmpty(tobeInsert)) {
            apiBaseMessageTemplateThirdBindPoService.insertBatch(tobeInsert);
        }

        return new ResponseEntity(resultData, HttpStatus.OK);
    }


    /**
     * 获取消息模板中已设置的参数
     * @param id
     * @return
     */
    @RequiresPermissions("messageTemplate:params")
    @RequestMapping(value = "/message/template/{id}/params",method = RequestMethod.GET)
    public ResponseEntity params(@PathVariable String id){

        ResponseJsonRender resultData=new ResponseJsonRender();

        // 获取模板
        BaseMessageTemplateDto messageTemplateDto = apiBaseMessageTemplatePoService.selectByPrimaryKey(id,false);
        if (messageTemplateDto == null) {
            return returnDto(null,resultData);
        }
        // 获取三方模板
        BaseMessageTemplateThirdBindPo condition = new BaseMessageTemplateThirdBindPo();
        condition.setMessageTemplateId(id);
        condition.setDelFlag(BasePo.YesNo.N.name());
        List<BaseMessageTemplateThirdBindDto> list = apiBaseMessageTemplateThirdBindPoService.selectList(condition);

        StringBuffer sb = new StringBuffer();
        com.feihua.utils.string.StringUtils.appendIfNotEmpty(sb,messageTemplateDto.getTitle());
        com.feihua.utils.string.StringUtils.appendIfNotEmpty(sb,messageTemplateDto.getProfile());
        com.feihua.utils.string.StringUtils.appendIfNotEmpty(sb,messageTemplateDto.getContent());
        if (list != null) {
            for (BaseMessageTemplateThirdBindDto baseMessageTemplateThirdBindDto : list) {
                com.feihua.utils.string.StringUtils.appendIfNotEmpty(sb,baseMessageTemplateThirdBindDto.getThirdTemplateContent());
            }
        }
        List<String> r = RegexUtils.getMatchedString(MsgTemplateUtils.label_pattern, sb.toString());
        return returnList(r,resultData);
    }
}
