package com.feihua.framework.message.mvc;

import com.feihua.framework.base.modules.loginclient.api.ApiBaseLoginClientPoService;
import com.feihua.framework.base.modules.loginclient.dto.BaseLoginClientDto;
import com.feihua.framework.base.modules.role.dto.BaseRoleDto;
import com.feihua.framework.base.modules.user.api.ApiBaseUserPoService;
import com.feihua.framework.base.modules.user.dto.BaseUserDto;
import com.feihua.framework.constants.DictEnum;
import com.feihua.framework.message.api.*;
import com.feihua.framework.message.dto.*;
import com.feihua.framework.message.po.BaseMessagePo;
import com.feihua.framework.message.po.BaseMessageThirdPo;
import com.feihua.framework.message.po.BaseMessageUserPo;
import com.feihua.framework.rest.ResponseJsonRender;
import com.feihua.framework.rest.interceptor.RepeatFormValidator;
import com.feihua.framework.rest.mvc.SuperController;
import com.feihua.utils.collection.CollectionUtils;
import com.feihua.utils.http.httpServletResponse.ResponseCode;
import com.feihua.utils.json.JSONUtils;
import feihua.jdbc.api.pojo.BasePo;
import feihua.jdbc.api.pojo.PageAndOrderbyParamDto;
import feihua.jdbc.api.pojo.PageResultDto;
import feihua.jdbc.api.utils.OrderbyUtils;
import feihua.jdbc.api.utils.PageUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.Logical;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 消息管理
 * Created by yangwei
 */
@RestController
@RequestMapping("/base")
public class BaseMessageController extends SuperController {

    private static Logger logger = LoggerFactory.getLogger(BaseMessageController.class);

    @Autowired
    private ApiBaseMessagePoService apiBaseMessagePoService;

    @Autowired
    private ApiBaseUserPoService apiBaseUserPoService;
    @Autowired
    private ApiMessageService apiMessageService;
    @Autowired
    private ApiBaseMessageUserPoService  apiBaseMessageUserPoService;
    @Autowired
    private ApiBaseLoginClientPoService apiBaseLoginClientPoService;
    @Autowired
    private ApiBaseMessageTemplatePoService apiBaseMessageTemplatePoService;
    @Autowired
    private ApiBaseMessageThirdPoService apiBaseMessageThirdPoService;

    /**
     * 单资源，添加
     * @param addFormDto
     * @return
     */
    @RepeatFormValidator
    @RequiresPermissions("message:add")
    @RequestMapping(value = "/message",method = RequestMethod.POST)
    public ResponseEntity add(AddBaseMessageFormDto addFormDto){
        logger.info("添加消息开始");
        logger.info("当前登录用户id:{}",getLoginUser().getId());
        String currentRoleId = ((BaseRoleDto)getLoginUser().getRole()).getId();
        ResponseJsonRender resultData=new ResponseJsonRender();
        // 表单值设置
        CreateMessageParamsDto createMessageParamsDto = new CreateMessageParamsDto();

        createMessageParamsDto.setTitle(addFormDto.getTitle());
        createMessageParamsDto.setProfile(addFormDto.getProfile());
        createMessageParamsDto.setContent(addFormDto.getContent());
        createMessageParamsDto.setMsgType(addFormDto.getMsgType());
        createMessageParamsDto.setMsgLevel(addFormDto.getMsgLevel());
        createMessageParamsDto.setMsgState(DictEnum.MessageState.to_be_sended.name());
        createMessageParamsDto.setMessageTemplateId(addFormDto.getMessageTemplateId());
        try {
            createMessageParamsDto.setTemplateParams(getTemplateParams(addFormDto.getTemplateParams()));
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return returnBadRequest("templateParam","templateParam is invalid",resultData);
        }
        createMessageParamsDto.setCurrentUserId(getLoginUserId());
        createMessageParamsDto.setCurrentRoleId(currentRoleId);
        BaseMessagePo r = apiBaseMessagePoService.addMessage(createMessageParamsDto);
        if (r == null) {
            // 添加失败
            resultData.setCode(ResponseCode.E404_100001.getCode());
            resultData.setMsg(ResponseCode.E404_100001.getMsg());
            logger.info("code:{},msg:{}",resultData.getCode(),resultData.getMsg());
            logger.info("添加消息结束，失败");
            return new ResponseEntity(resultData,HttpStatus.NOT_FOUND);
        }else{
            // 添加成功，返回添加的数据
            resultData.setData(r);
            logger.info("添加消息id:{}",r.getId());
            logger.info("添加消息结束，成功");
            return new ResponseEntity(resultData, HttpStatus.CREATED);
        }
    }

    /**
     * 单资源，删除
     * @param id
     * @return
     */
    @RepeatFormValidator
    @RequiresPermissions("message:delete")
    @RequestMapping(value = "/message/{id}",method = RequestMethod.DELETE)
    public ResponseEntity delete(@PathVariable String id){
        logger.info("删除消息开始");
        logger.info("用户id:{}",getLoginUser().getId());
        logger.info("消息id:{}",id);
        ResponseJsonRender resultData=new ResponseJsonRender();

            int r = apiBaseMessagePoService.deleteFlagByPrimaryKeyWithUpdateUser(id,getLoginUser().getId());
            if (r <= 0) {
                // 删除失败，可能没有找到资源
                resultData.setCode(ResponseCode.E404_100001.getCode());
                resultData.setMsg(ResponseCode.E404_100001.getMsg());
                logger.info("code:{},msg:{}",resultData.getCode(),resultData.getMsg());
                logger.info("删除消息结束，失败");
                return new ResponseEntity(resultData,HttpStatus.NOT_FOUND);
            }else{
                // 删除成功
                logger.info("删除的消息id:{}",id);
                logger.info("删除消息结束，成功");
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
    @RequiresPermissions("message:update")
    @RequestMapping(value = "/message/{id}",method = RequestMethod.PUT)
    public ResponseEntity update(@PathVariable String id, UpdateBaseMessageFormDto updateFormDto){
        logger.info("更新消息开始");
        logger.info("当前登录用户id:{}",getLoginUser().getId());
        logger.info("消息id:{}",id);
        ResponseJsonRender resultData=new ResponseJsonRender();

        //检查
        BaseMessagePo basePoCheckCondition = new BaseMessagePo();
        basePoCheckCondition.setId(id);
        basePoCheckCondition.setDelFlag(BasePo.YesNo.N.name());
        BaseMessageDto baseMessageDto = apiBaseMessagePoService.selectOne(basePoCheckCondition);
        // 如果已发送或发送中则不允许修改
        if (baseMessageDto != null && !DictEnum.MessageState.to_be_sended.name().equals(baseMessageDto.getMsgState())) {
            // 更新失败，资源不允许修改
            resultData.setCode(ResponseCode.E403_100004.getCode());
            resultData.setMsg(ResponseCode.E403_100004.getMsg());
            logger.info("code:{},msg:{}",resultData.getCode(),resultData.getMsg());
            logger.info("更新消息结束，失败");
            return new ResponseEntity(resultData,HttpStatus.FORBIDDEN);
        }
        CreateMessageParamsDto createMessageParamsDto = new CreateMessageParamsDto();
        createMessageParamsDto.setMessageId(id);
        createMessageParamsDto.setTitle(updateFormDto.getTitle());
        createMessageParamsDto.setProfile(updateFormDto.getProfile());
        createMessageParamsDto.setContent(updateFormDto.getContent());
        createMessageParamsDto.setMsgType(updateFormDto.getMsgType());
        createMessageParamsDto.setMsgLevel(updateFormDto.getMsgLevel());
        createMessageParamsDto.setMsgState(updateFormDto.getMsgState());
        createMessageParamsDto.setMessageTemplateId(updateFormDto.getMessageTemplateId());
        createMessageParamsDto.setCurrentUserId(getLoginUserId());
        createMessageParamsDto.setCurrentRoleId(((BaseRoleDto)getLoginUser().getRole()).getId());
        try {
            createMessageParamsDto.setTemplateParams(getTemplateParams(updateFormDto.getTemplateParams()));
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return returnBadRequest("templateParam","templateParam is invalid",resultData);
        }
        // 表单值设置
        BaseMessagePo r = apiBaseMessagePoService.updateMessage(createMessageParamsDto);
        if (r == null) {
            // 更新失败，资源不存在
            resultData.setCode(ResponseCode.E404_100001.getCode());
            resultData.setMsg(ResponseCode.E404_100001.getMsg());
            logger.info("code:{},msg:{}",resultData.getCode(),resultData.getMsg());
            logger.info("更新消息结束，失败");
            return new ResponseEntity(resultData,HttpStatus.NOT_FOUND);
        }else{
            // 更新成功，已被成功创建
            logger.info("更新的消息id:{}",id);
            logger.info("更新消息结束，成功");
            return new ResponseEntity(resultData, HttpStatus.CREATED);
        }
    }

    /**
     * 单资源，获取id消息管理
     * @param id
     * @return
     */
    @RequiresPermissions(value = {"message:getById","user"},logical = Logical.OR)
    @RequestMapping(value = "/message/{id}",method = RequestMethod.GET)
    public ResponseEntity getById(@PathVariable String id){

        ResponseJsonRender resultData=new ResponseJsonRender();
        BaseMessageDto baseDataScopeDto = apiBaseMessagePoService.selectByPrimaryKey(id,false);
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
     * 复数资源，搜索消息管理
     * @param dto
     * @return
     */
    @RequiresPermissions("message:search")
    @RequestMapping(value = "/messages",method = RequestMethod.GET)
    public ResponseEntity search(SearchBaseMessagesConditionDto dto){

        ResponseJsonRender resultData=new ResponseJsonRender();
        PageAndOrderbyParamDto pageAndOrderbyParamDto = new PageAndOrderbyParamDto(PageUtils.getPageFromThreadLocal(), OrderbyUtils.getOrderbyFromThreadLocal());
        // 设置当前登录用户id
        dto.setCurrentUserId(getLoginUser().getId());
        dto.setCurrentRoleId(((BaseRoleDto)getLoginUser().getRole()).getId());
        PageResultDto<BaseMessageDto> list = apiBaseMessagePoService.searchBaseMessagesDsf(dto,pageAndOrderbyParamDto);

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

    /**
     * 复数资源，查看已读人员
     * @param id 消息id
     * @param isRead Y/N 已读未读标识
     * @return
     */
    @RequiresPermissions("message:viewReadPeople")
    @RequestMapping(value = "/message/{id}/viewReadPeople",method = RequestMethod.GET)
    public ResponseEntity viewReadPeople(@PathVariable String id,String isRead){

        ResponseJsonRender resultData = new ResponseJsonRender();
        PageAndOrderbyParamDto pageAndOrderbyParamDto = new PageAndOrderbyParamDto(PageUtils.getPageFromThreadLocal(), OrderbyUtils.getOrderbyFromThreadLocal());
        BaseMessageUserPo condition = new BaseMessageUserPo();
        condition.setDelFlag(BasePo.YesNo.N.name());
        condition.setIsRead(isRead);
        condition.setMessageId(id);
        PageResultDto<BaseMessageUserDto> list = apiBaseMessageUserPoService.selectList(condition,pageAndOrderbyParamDto);
        if(list.getData() != null && !list.getData().isEmpty()){
            resultData.setData(list.getData());
            resultData.setPage(list.getPage());
            // 添加用户
            List<String> userIds = new ArrayList<>(list.getData().size());
            List<String> cientIds = new ArrayList<>();
            for (BaseMessageUserDto messageUserDto : list.getData()) {
                userIds.add(messageUserDto.getUserId());
                if(StringUtils.isNotEmpty(messageUserDto.getReadClientId())){
                    cientIds.add(messageUserDto.getReadClientId());
                }
            }
            if (userIds != null && !userIds.isEmpty()){
                List<BaseUserDto> userDtos = apiBaseUserPoService.selectByPrimaryKeys(userIds,false);
                if (userDtos != null && !userDtos.isEmpty()){
                    resultData.addData("users",userDtos);
                }
            }
            if (cientIds != null && !cientIds.isEmpty()){

                List<BaseLoginClientDto> loginClientDtos = apiBaseLoginClientPoService.selectByPrimaryKeys(cientIds,false);
                if (loginClientDtos != null && !loginClientDtos.isEmpty()){
                    resultData.addData("clients",loginClientDtos);
                }
            }

            return new ResponseEntity(resultData, HttpStatus.OK);
        }else{
            resultData.setCode(ResponseCode.E404_100001.getCode());
            resultData.setMsg(ResponseCode.E404_100001.getMsg());
            return new ResponseEntity(resultData,HttpStatus.NOT_FOUND);
        }
    }

    /**
     * 单资源，复制一条消息
     * @param id
     * @return
     */
    @RepeatFormValidator
    @RequiresPermissions("message:copy")
    @RequestMapping(value = "/message/{id}/copy",method = RequestMethod.POST)
    public ResponseEntity copy(@PathVariable String id){
        logger.info("复制消息开始");
        logger.info("当前登录用户id:{}",getLoginUser().getId());
        ResponseJsonRender resultData=new ResponseJsonRender();

        BaseMessageDto baseMessageDtoDb = apiBaseMessagePoService.selectByPrimaryKey(id,false);
        if (baseMessageDtoDb == null) {
            // 复制失败
            resultData.setCode(ResponseCode.E404_100001.getCode());
            resultData.setMsg(ResponseCode.E404_100001.getMsg());
            logger.info("code:{},msg:{}",resultData.getCode(),resultData.getMsg());
            logger.info("复制消息结束，失败");
            return new ResponseEntity(resultData,HttpStatus.NOT_FOUND);
        }
        BaseMessagePo basePo = new BaseMessagePo();
        basePo.setTitle(baseMessageDtoDb.getTitle() + "[copy]");
        basePo.setProfile(baseMessageDtoDb.getProfile());
        basePo.setContent(baseMessageDtoDb.getContent());
        basePo.setMsgType(baseMessageDtoDb.getMsgType());
        basePo.setMsgLevel(baseMessageDtoDb.getMsgLevel());
        basePo.setMsgState(DictEnum.MessageState.to_be_sended.name());
        basePo.setMessageTemplateId(baseMessageDtoDb.getMessageTemplateId());

        basePo = apiBaseMessagePoService.preInsert(basePo,getLoginUser().getId());
        BaseMessageDto r = apiBaseMessagePoService.insert(basePo);
        // 三方消息复制

        if (r == null) {
            // 复制失败
            resultData.setCode(ResponseCode.E404_100001.getCode());
            resultData.setMsg(ResponseCode.E404_100001.getMsg());
            logger.info("code:{},msg:{}",resultData.getCode(),resultData.getMsg());
            logger.info("复制消息结束，失败");
            return new ResponseEntity(resultData,HttpStatus.NOT_FOUND);
        }else{
            apiBaseMessageThirdPoService.copyByMessageId(id,r.getId(),getLoginUserId());
            // 复制成功，返回添加的数据
            resultData.setData(r);
            logger.info("复制消息id:{}",r.getId());
            logger.info("复制消息结束，成功");
            return new ResponseEntity(resultData, HttpStatus.CREATED);
        }
    }

    /**
     * 单资源，发送消息
     * @param id
     * @return
     */
    @RepeatFormValidator
    @RequiresPermissions("message:send")
    @RequestMapping(value = "/message/{id}/send",method = RequestMethod.POST)
    public ResponseEntity send(@PathVariable String id,MessageSendFormDto formDto){
        logger.info("发送消息开始");
        logger.info("当前登录用户id:{}",getLoginUser().getId());
        String currentUserId = getLoginUser().getId();
        String currentRoleId = ((BaseRoleDto)getLoginUser().getRole()).getId();
        ResponseJsonRender resultData=new ResponseJsonRender();

        //检查
        BaseMessagePo basePoCheckCondition = new BaseMessagePo();
        basePoCheckCondition.setId(id);
        basePoCheckCondition.setDelFlag(BasePo.YesNo.N.name());
        BaseMessageDto baseMessageDto = apiBaseMessagePoService.selectOne(basePoCheckCondition);
        // 如果已发送或发送中则不允许修改
        if (baseMessageDto != null && !DictEnum.MessageState.to_be_sended.name().equals(baseMessageDto.getMsgState())) {
            // 发送失败，资源不是待发送状态
            resultData.setCode(ResponseCode.E403_100005.getCode());
            resultData.setMsg(ResponseCode.E403_100005.getMsg());
            logger.info("code:{},msg:{}",resultData.getCode(),resultData.getMsg());
            logger.info("发送消息结束，失败");
            return new ResponseEntity(resultData,HttpStatus.FORBIDDEN);
        }

        return sendMessage(resultData,id,formDto,currentUserId,currentRoleId);

    }

    /**
     * 单资源，创建并发送消息
     * @param
     * @return
     */
    @RepeatFormValidator
    @RequiresPermissions("message:newsend")
    @RequestMapping(value = "/message/newsend/{messageTemplateId}",method = RequestMethod.POST)
    public ResponseEntity newsend(@PathVariable String messageTemplateId,MessageSendFormDto formDto){
        logger.info("发送消息开始");
        logger.info("当前登录用户id:{}",getLoginUser().getId());
        String currentUserId = getLoginUser().getId();
        String currentRoleId = ((BaseRoleDto)getLoginUser().getRole()).getId();
        ResponseJsonRender resultData = new ResponseJsonRender();


        BaseMessageTemplateDto messageTemplateDto = apiBaseMessageTemplatePoService.selectByPrimaryKey(messageTemplateId);

        CreateMessageParamsDto createMessageParamsDto = new CreateMessageParamsDto();
        createMessageParamsDto.setTitle(messageTemplateDto.getTitle());
        createMessageParamsDto.setProfile(messageTemplateDto.getProfile());
        createMessageParamsDto.setContent(messageTemplateDto.getContent());
        createMessageParamsDto.setMsgType(messageTemplateDto.getMsgType());
        createMessageParamsDto.setMsgLevel(messageTemplateDto.getMsgLevel());
        createMessageParamsDto.setMsgState(DictEnum.MessageState.to_be_sended.name());
        createMessageParamsDto.setMessageTemplateId(messageTemplateId);
        try {
            createMessageParamsDto.setTemplateParams(getTemplateParams(formDto.getTemplateParams()));
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return returnBadRequest("templateParam","templateParam is invalid",resultData);
        }
        createMessageParamsDto.setCurrentUserId(currentUserId);
        createMessageParamsDto.setCurrentRoleId(currentRoleId);
        BaseMessagePo r = apiBaseMessagePoService.addMessage(createMessageParamsDto);


        return sendMessage(resultData,r.getId(),formDto,currentUserId,currentRoleId);

    }
    private ResponseEntity sendMessage(ResponseJsonRender resultData,String messageId,MessageSendFormDto formDto,String currentUserId,String currentRoleId){
        // 发送参数
        BaseMessageSendParamsDto baseMessageSendParamsDto = new BaseMessageSendParamsDto();
        baseMessageSendParamsDto.setMessageId(messageId);
        // 客户端发送参数
        BaseMessageSendClientParamDto clientParamDto = new BaseMessageSendClientParamDto();
        clientParamDto.setTargetType(formDto.getTargetType());
        clientParamDto.setTargetValues(formDto.getTargetValues());
        List<String> clientIds = formDto.getClientIds();
        if (clientIds != null && !clientIds.isEmpty()) {
            clientParamDto.setClients(apiBaseLoginClientPoService.selectByPrimaryKeys(clientIds,false));
        }
        baseMessageSendParamsDto.setClientParamDto(clientParamDto);
        // 虚拟客户端发送参数
        List<MessageVSendFormDto> vSendFormDtos = formDto.getvSendFormDtos();
        if (!CollectionUtils.isNullOrEmpty(vSendFormDtos)) {
            // 虚拟客户端容器
            List<BaseMessageSendVClientParamDto> vClientParamDtos = new ArrayList<>(vSendFormDtos.size());
            BaseMessageSendVClientParamDto vClientParamDto = null;
            for (MessageVSendFormDto vSendFormDto : vSendFormDtos) {
                vClientParamDto = new BaseMessageSendVClientParamDto();
                vClientParamDto.setVclient(apiBaseLoginClientPoService.selectByPrimaryKey(vSendFormDto.getClientId(),false));
                vClientParamDto.setVtargetType(vSendFormDto.getvTargetType());
                vClientParamDto.setVtargetValues(CollectionUtils.StringToList(vSendFormDto.getvTargetValues(),","));
                vClientParamDtos.add(vClientParamDto);
            }
            baseMessageSendParamsDto.setvClientParamDtos(vClientParamDtos);
        }
        baseMessageSendParamsDto.setCurrentUserId(currentUserId);
        baseMessageSendParamsDto.setCurrentRoleId(currentRoleId);

        try {
            apiMessageService.messageSend(baseMessageSendParamsDto,true);
        }catch (Exception e){
            resultData.setCode(ResponseCode.E404_100001.getCode());
            resultData.setMsg(ResponseCode.E404_100001.getMsg());
            logger.info("code:{},msg:{}",resultData.getCode(),resultData.getMsg());
            logger.info("发送消息结束，失败");
            return new ResponseEntity(resultData,HttpStatus.NOT_FOUND);
        }
        logger.info("发送消息id:{}",messageId);
        logger.info("发送消息结束，成功");
        return new ResponseEntity(resultData, HttpStatus.CREATED);
    }
    /**
     * 复数资源，查询当前用户的消息
     * @param dto
     * @return
     */
    @RequiresPermissions("user")
    @RequestMapping(value = "/message/currentuser/messages",method = RequestMethod.GET)
    public ResponseEntity searchCurrentUserMessages(SearchUserMessageConditionDto dto){

        ResponseJsonRender resultData=new ResponseJsonRender();
        PageAndOrderbyParamDto pageAndOrderbyParamDto = new PageAndOrderbyParamDto(PageUtils.getPageFromThreadLocal(), OrderbyUtils.getOrderbyFromThreadLocal());
        // 设置当前登录用户id
        dto.setCurrentUserId(getLoginUser().getId());
        dto.setCurrentRoleId(((BaseRoleDto)getLoginUser().getRole()).getId());
        dto.setClientId(getLoginUser().getLoginClientId());

        dto.setUserId(getLoginUserId());
        PageResultDto<UserMessageDto> list = apiBaseMessagePoService.searchUserMessage(dto,pageAndOrderbyParamDto);

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


    /**
     * 单资源，读取消息，并标记为已读
     * @param id
     * @param markOnly 如果为true 响应中不携带数据，只标记为已读
     * @return
     */
    @RepeatFormValidator
    @RequiresPermissions("user")
    @RequestMapping(value = "/message/{id}/currentuser",method = RequestMethod.POST)
    public ResponseEntity readCurrentUserMessage(@PathVariable String id ,boolean markOnly){
        logger.info("读取消息并标记为已读开始");
        logger.info("当前登录用户id:{}",getLoginUser().getId());
        ResponseJsonRender resultData=new ResponseJsonRender();

        String clientId = getLoginUser().getLoginClientId();

        apiMessageService.readMessage(id,getLoginUserId(),clientId);
        // 读取成功，返回添加的数据
        if (!markOnly) {
            resultData.setData(apiBaseMessagePoService.selectByPrimaryKey(id));
        }
        logger.info("读取消息id:{}",id);
        logger.info("读取消息并标记为已读结束，成功");
        return new ResponseEntity(resultData, HttpStatus.OK);
    }

    private Map<String,String> getTemplateParams(String templateParam) throws Exception {
        if (StringUtils.isNotEmpty(templateParam)) {

            Map<String,Object> params = JSONUtils.json2map(templateParam);
            if(params != null && !params.isEmpty()){
                Map<String,String> _params = new HashMap<>();
                for (String key : params.keySet()) {
                    _params.put(key, (String) params.get(key));
                }
                return (_params);
            }

        }
        return null;
    }
}
