package com.feihua.framework.message.mvc;

import com.feihua.framework.base.modules.loginclient.api.ApiBaseLoginClientPoService;
import com.feihua.framework.base.modules.role.dto.BaseRoleDto;
import com.feihua.framework.rest.ResponseJsonRender;
import com.feihua.framework.rest.interceptor.RepeatFormValidator;
import com.feihua.framework.rest.mvc.SuperController;
import com.feihua.utils.collection.CollectionUtils;
import com.feihua.utils.http.httpServletResponse.ResponseCode;
import feihua.jdbc.api.pojo.BasePo;
import feihua.jdbc.api.pojo.PageAndOrderbyParamDto;
import feihua.jdbc.api.pojo.PageResultDto;
import feihua.jdbc.api.utils.OrderbyUtils;
import feihua.jdbc.api.utils.PageUtils;
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
import com.feihua.framework.message.dto.BaseMessageClientDto;
import com.feihua.framework.message.dto.SearchBaseMessageClientsConditionDto;
import com.feihua.framework.message.api.ApiBaseMessageClientPoService;
import com.feihua.framework.message.dto.AddBaseMessageClientFormDto;
import com.feihua.framework.message.dto.UpdateBaseMessageClientFormDto;
import com.feihua.framework.message.po.BaseMessageClientPo;

import java.util.ArrayList;
import java.util.List;

/**
 * 消息客户端配置
 * Created by yangwei
 */
@RestController
@RequestMapping("/base")
public class BaseMessageClientController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(BaseMessageClientController.class);

    @Autowired
    private ApiBaseMessageClientPoService apiBaseMessageClientPoService;
    @Autowired
    private ApiBaseLoginClientPoService apiBaseLoginClientPoService;

    /**
     * 单资源，添加
     * @param dto
     * @return
     */
    @RepeatFormValidator
    @RequiresPermissions("message:client:add")
    @RequestMapping(value = "/message/client",method = RequestMethod.POST)
    public ResponseEntity add(AddBaseMessageClientFormDto dto){
        logger.info("添加消息客户端开始");
        logger.info("当前登录用户id:{}",getLoginUser().getId());
        ResponseJsonRender resultData=new ResponseJsonRender();
        // 表单值设置
        BaseMessageClientPo basePo = new BaseMessageClientPo();

        basePo.setClientId(dto.getClientId());
        basePo.setMessageType(dto.getMessageType());
        basePo.setIsEnable(dto.getIsEnable());


        basePo = apiBaseMessageClientPoService.preInsert(basePo,getLoginUser().getId());
        BaseMessageClientDto r = apiBaseMessageClientPoService.insert(basePo);
        if (r == null) {
            // 添加失败
            resultData.setCode(ResponseCode.E404_100001.getCode());
            resultData.setMsg(ResponseCode.E404_100001.getMsg());
            logger.info("code:{},msg:{}",resultData.getCode(),resultData.getMsg());
            logger.info("添加消息客户端结束，失败");
            return new ResponseEntity(resultData,HttpStatus.NOT_FOUND);
        }else{
            // 添加成功，返回添加的数据
            resultData.setData(r);
            logger.info("添加消息客户端id:{}",r.getId());
            logger.info("添加消息客户端结束，成功");
            return new ResponseEntity(resultData, HttpStatus.CREATED);
        }
    }

    /**
     * 单资源，删除
     * @param id
     * @return
     */
    @RepeatFormValidator
    @RequiresPermissions("message:client:delete")
    @RequestMapping(value = "/message/client/{id}",method = RequestMethod.DELETE)
    public ResponseEntity delete(@PathVariable String id){
        logger.info("删除消息客户端开始");
        logger.info("用户id:{}",getLoginUser().getId());
        logger.info("消息客户端id:{}",id);
        ResponseJsonRender resultData=new ResponseJsonRender();

            int r = apiBaseMessageClientPoService.deleteFlagByPrimaryKeyWithUpdateUser(id,getLoginUser().getId());
            if (r <= 0) {
                // 删除失败，可能没有找到资源
                resultData.setCode(ResponseCode.E404_100001.getCode());
                resultData.setMsg(ResponseCode.E404_100001.getMsg());
                logger.info("code:{},msg:{}",resultData.getCode(),resultData.getMsg());
                logger.info("删除消息客户端结束，失败");
                return new ResponseEntity(resultData,HttpStatus.NOT_FOUND);
            }else{
                // 删除成功
                logger.info("删除的消息客户端id:{}",id);
                logger.info("删除消息客户端结束，成功");
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
    @RequiresPermissions("message:client:update")
    @RequestMapping(value = "/message/client/{id}",method = RequestMethod.PUT)
    public ResponseEntity update(@PathVariable String id, UpdateBaseMessageClientFormDto dto){
        logger.info("更新消息客户端开始");
        logger.info("当前登录用户id:{}",getLoginUser().getId());
        logger.info("消息客户端id:{}",id);
        ResponseJsonRender resultData=new ResponseJsonRender();
        // 表单值设置
        BaseMessageClientPo basePo = new BaseMessageClientPo();
        // id
        basePo.setId(id);
        basePo.setClientId(dto.getClientId());
        basePo.setMessageType(dto.getMessageType());
        basePo.setIsEnable(dto.getIsEnable());

        // 用条件更新，乐观锁机制
        BaseMessageClientPo basePoCondition = new BaseMessageClientPo();
        basePoCondition.setId(id);
        basePoCondition.setDelFlag(BasePo.YesNo.N.name());
        basePoCondition.setUpdateAt(dto.getUpdateTime());
        basePo = apiBaseMessageClientPoService.preUpdate(basePo,getLoginUser().getId());
        int r = apiBaseMessageClientPoService.updateSelective(basePo,basePoCondition);
        if (r <= 0) {
            // 更新失败，资源不存在
            resultData.setCode(ResponseCode.E404_100001.getCode());
            resultData.setMsg(ResponseCode.E404_100001.getMsg());
            logger.info("code:{},msg:{}",resultData.getCode(),resultData.getMsg());
            logger.info("更新消息客户端结束，失败");
            return new ResponseEntity(resultData,HttpStatus.NOT_FOUND);
        }else{
            // 更新成功，已被成功创建
            logger.info("更新的消息客户端id:{}",id);
            logger.info("更新消息客户端结束，成功");
            return new ResponseEntity(resultData, HttpStatus.CREATED);
        }
    }

    /**
     * 单资源，获取id消息客户端
     * @param id
     * @return
     */
    @RequiresPermissions("message:client:getById")
    @RequestMapping(value = "/message/client/{id}",method = RequestMethod.GET)
    public ResponseEntity getById(@PathVariable String id){

        ResponseJsonRender resultData=new ResponseJsonRender();
        BaseMessageClientDto baseDataScopeDto = apiBaseMessageClientPoService.selectByPrimaryKey(id,false);
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
     * 复数资源，搜索消息客户端
     * @param dto
     * @return
     */
    @RequiresPermissions("message:client:search")
    @RequestMapping(value = "/message/clients",method = RequestMethod.GET)
    public ResponseEntity search(SearchBaseMessageClientsConditionDto dto){

        ResponseJsonRender resultData = new ResponseJsonRender();
        PageAndOrderbyParamDto pageAndOrderbyParamDto = new PageAndOrderbyParamDto(PageUtils.getPageFromThreadLocal(), OrderbyUtils.getOrderbyFromThreadLocal());
        // 设置当前登录用户id
        dto.setCurrentUserId(getLoginUser().getId());
        dto.setCurrentRoleId(getLoginUserRoleId());
        dto.setCurrentPostId(getLoginUserPostId());
        PageResultDto<BaseMessageClientDto> list = apiBaseMessageClientPoService.searchBaseMessageClientsDsf(dto,pageAndOrderbyParamDto);

        if (!CollectionUtils.isNullOrEmpty(list.getData())) {
            List<String> clientIds = new ArrayList<>(list.getData().size());
            for (BaseMessageClientDto datum : list.getData()) {
                clientIds.add(datum.getClientId());
            }
            if (!CollectionUtils.isNullOrEmpty(clientIds)) {
                resultData.addData("clients", apiBaseLoginClientPoService.selectByPrimaryKeys(clientIds,false));
            }
        }

        return returnList(list.getData(),resultData);
    }
}
