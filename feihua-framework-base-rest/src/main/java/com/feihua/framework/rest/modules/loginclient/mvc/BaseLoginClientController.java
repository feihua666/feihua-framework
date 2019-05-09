package com.feihua.framework.rest.modules.loginclient.mvc;

import com.feihua.framework.base.modules.loginclient.api.ApiBaseLoginClientChannelBindPoService;
import com.feihua.framework.base.modules.loginclient.dto.BaseLoginClientChannelBindDto;
import com.feihua.framework.base.modules.loginclient.po.BaseLoginClientChannelBindPo;
import com.feihua.framework.base.modules.role.dto.BaseRoleDto;
import com.feihua.framework.constants.DictEnum;
import com.feihua.framework.rest.ResponseJsonRender;
import com.feihua.framework.rest.interceptor.RepeatFormValidator;
import com.feihua.framework.rest.modules.common.mvc.BaseController;
import com.feihua.framework.rest.modules.loginclient.dto.LoginClientChannelBindFormDto;
import com.feihua.utils.http.httpServletResponse.ResponseCode;
import feihua.jdbc.api.pojo.BasePo;
import feihua.jdbc.api.pojo.PageAndOrderbyParamDto;
import feihua.jdbc.api.pojo.PageResultDto;
import feihua.jdbc.api.utils.OrderbyUtils;
import feihua.jdbc.api.utils.PageUtils;
import org.apache.commons.collections.CollectionUtils;
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
import com.feihua.framework.base.modules.loginclient.dto.BaseLoginClientDto;
import com.feihua.framework.base.modules.loginclient.dto.SearchBaseLoginClientsConditionDto;
import com.feihua.framework.base.modules.loginclient.api.ApiBaseLoginClientPoService;
import com.feihua.framework.rest.modules.loginclient.dto.AddBaseLoginClientFormDto;
import com.feihua.framework.rest.modules.loginclient.dto.UpdateBaseLoginClientFormDto;
import com.feihua.framework.base.modules.loginclient.po.BaseLoginClientPo;

import java.util.List;

/**
 * 客户端管理
 * Created by yangwei
 */
@RestController
@RequestMapping("/base/client")
public class BaseLoginClientController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(BaseLoginClientController.class);

    @Autowired
    private ApiBaseLoginClientPoService apiBaseLoginClientPoService;
    @Autowired
    private ApiBaseLoginClientChannelBindPoService apiBaseLoginClientChannelBindPoService;

    /**
     * 单资源，添加
     * @param dto
     * @return
     */
    @RepeatFormValidator
    @RequiresPermissions("base:loginclient:add")
    @RequestMapping(value = "/loginclient",method = RequestMethod.POST)
    public ResponseEntity add(AddBaseLoginClientFormDto dto){
        logger.info("添加客户端开始");
        logger.info("当前登录用户id:{}",getLoginUser().getId());
        ResponseJsonRender resultData=new ResponseJsonRender();
        // 表单值设置
        BaseLoginClientPo basePo = new BaseLoginClientPo();
        basePo.setCode(dto.getCode());
        basePo.setName(dto.getName());
        basePo.setType(dto.getType());
        basePo.setDescription(dto.getDescription());
        basePo.setLoginMaxnum(dto.getMaxnum());
        basePo.setIsVirtual(dto.getIsVirtual());

        basePo = apiBaseLoginClientPoService.preInsert(basePo,getLoginUser().getId());
        BaseLoginClientDto r = apiBaseLoginClientPoService.insert(basePo);
        if (r == null) {
            // 添加失败
            resultData.setCode(ResponseCode.E404_100001.getCode());
            resultData.setMsg(ResponseCode.E404_100001.getMsg());
            logger.info("code:{},msg:{}",resultData.getCode(),resultData.getMsg());
            logger.info("添加客户端结束，失败");
            return new ResponseEntity(resultData,HttpStatus.NOT_FOUND);
        }else{
            // 添加成功，返回添加的数据
            resultData.setData(r);
            logger.info("添加客户端id:{}",r.getId());
            logger.info("添加客户端结束，成功");
            return new ResponseEntity(resultData, HttpStatus.CREATED);
        }
    }

    /**
     * 单资源，删除
     * @param id
     * @return
     */
    @RepeatFormValidator
    @RequiresPermissions("base:loginclient:delete")
    @RequestMapping(value = "/loginclient/{id}",method = RequestMethod.DELETE)
    public ResponseEntity delete(@PathVariable String id){
        logger.info("删除客户端开始");
        logger.info("用户id:{}",getLoginUser().getId());
        logger.info("客户端id:{}",id);
        ResponseJsonRender resultData=new ResponseJsonRender();

            int r = apiBaseLoginClientPoService.deleteFlagByPrimaryKeyWithUpdateUser(id,getLoginUser().getId());
            if (r <= 0) {
                // 删除失败，可能没有找到资源
                resultData.setCode(ResponseCode.E404_100001.getCode());
                resultData.setMsg(ResponseCode.E404_100001.getMsg());
                logger.info("code:{},msg:{}",resultData.getCode(),resultData.getMsg());
                logger.info("删除客户端结束，失败");
                return new ResponseEntity(resultData,HttpStatus.NOT_FOUND);
            }else{
                // 删除成功
                logger.info("删除的客户端id:{}",id);
                logger.info("删除客户端结束，成功");
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
    @RequiresPermissions("base:loginclient:update")
    @RequestMapping(value = "/loginclient/{id}",method = RequestMethod.PUT)
    public ResponseEntity update(@PathVariable String id, UpdateBaseLoginClientFormDto dto){
        logger.info("更新客户端开始");
        logger.info("当前登录用户id:{}",getLoginUser().getId());
        logger.info("客户端id:{}",id);
        ResponseJsonRender resultData=new ResponseJsonRender();
        // 表单值设置
        BaseLoginClientPo basePo = new BaseLoginClientPo();
        // id
        basePo.setId(id);
        basePo.setCode(dto.getCode());
        basePo.setName(dto.getName());
        basePo.setType(dto.getType());
        basePo.setDescription(dto.getDescription());
        basePo.setLoginMaxnum(dto.getMaxnum());
        basePo.setIsVirtual(dto.getIsVirtual());
        // 用条件更新，乐观锁机制
        BaseLoginClientPo basePoCondition = new BaseLoginClientPo();
        basePoCondition.setId(id);
        basePoCondition.setDelFlag(BasePo.YesNo.N.name());
        basePoCondition.setUpdateAt(dto.getUpdateTime());
        basePo = apiBaseLoginClientPoService.preUpdate(basePo,getLoginUser().getId());
        int r = apiBaseLoginClientPoService.updateSelective(basePo,basePoCondition);
        if (r <= 0) {
            // 更新失败，资源不存在
            resultData.setCode(ResponseCode.E404_100001.getCode());
            resultData.setMsg(ResponseCode.E404_100001.getMsg());
            logger.info("code:{},msg:{}",resultData.getCode(),resultData.getMsg());
            logger.info("更新客户端结束，失败");
            return new ResponseEntity(resultData,HttpStatus.NOT_FOUND);
        }else{
            // 更新成功，已被成功创建
            logger.info("更新的客户端id:{}",id);
            logger.info("更新客户端结束，成功");
            return new ResponseEntity(resultData, HttpStatus.CREATED);
        }
    }

    /**
     * 单资源，获取id客户端
     * @param id
     * @return
     */
    @RequiresPermissions("base:loginclient:getById")
    @RequestMapping(value = "/loginclient/{id}",method = RequestMethod.GET)
    public ResponseEntity getById(@PathVariable String id){

        ResponseJsonRender resultData=new ResponseJsonRender();
        BaseLoginClientDto baseDataScopeDto = apiBaseLoginClientPoService.selectByPrimaryKey(id,false);
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
     * 复数资源，搜索客户端
     * @param dto
     * @return
     */
    @RequiresPermissions(value = {"base:loginclient:search","user"},logical = Logical.OR)
    @RequestMapping(value = "/loginclients",method = RequestMethod.GET)
    public ResponseEntity search(SearchBaseLoginClientsConditionDto dto){

        ResponseJsonRender resultData=new ResponseJsonRender();
        PageAndOrderbyParamDto pageAndOrderbyParamDto = new PageAndOrderbyParamDto(PageUtils.getPageFromThreadLocal(), OrderbyUtils.getOrderbyFromThreadLocal());
        // 设置当前登录用户id
        dto.setCurrentUserId(getLoginUser().getId());
        dto.setCurrentRoleId(((BaseRoleDto) getLoginUser().getRole()).getId());
        PageResultDto<BaseLoginClientDto> list = apiBaseLoginClientPoService.searchBaseLoginClientsDsf(dto,pageAndOrderbyParamDto);

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


    // 以下是绑定渠道相关

    /**
     * 复数资源获取绑定的渠道
     * @param id
     * @return
     */
    @RequiresPermissions("base:loginclient:channel:getById")
    @RequestMapping(value = "/loginclient/{id}/channel",method = RequestMethod.GET)
    public ResponseEntity channelGetById(@PathVariable String id){

        ResponseJsonRender resultData=new ResponseJsonRender();
        BaseLoginClientChannelBindPo condition = new BaseLoginClientChannelBindPo();
        condition.setClientId(id);
        condition.setDelFlag(BasePo.YesNo.N.name());
        List<BaseLoginClientChannelBindDto> list = apiBaseLoginClientChannelBindPoService.selectList(condition);
        return returnList(list,resultData);
    }
    /**
     * 绑定渠道
     * @param id
     * @return
     */
    @RequiresPermissions("base:loginclient:channel:bind")
    @RequestMapping(value = "/loginclient/{id}/channel",method = RequestMethod.POST)
    public ResponseEntity channelBind(@PathVariable String id, LoginClientChannelBindFormDto dto){

        ResponseJsonRender resultData=new ResponseJsonRender();
        // 先删除再添加

        BaseLoginClientChannelBindPo deleteConditon = new BaseLoginClientChannelBindPo();
        deleteConditon.setClientId(id);
        apiBaseLoginClientChannelBindPoService.deleteSelective(deleteConditon);
        // 微信公众帐号
        if(StringUtils.isNotEmpty(dto.getWeixinPublicplatformAccoutId())){
            BaseLoginClientChannelBindPo weixinPublic = new BaseLoginClientChannelBindPo();
            weixinPublic.setChannelType(DictEnum.WxAccountType.weixin_publicplatform.name());
            weixinPublic.setClientId(id);
            weixinPublic.setChannelId(dto.getWeixinPublicplatformAccoutId());
            weixinPublic = apiBaseLoginClientChannelBindPoService.preInsert(weixinPublic,getLoginUserId());
            apiBaseLoginClientChannelBindPoService.insert(weixinPublic);
        }

        // 微信小程序
        if(StringUtils.isNotEmpty(dto.getWeixinMiniprogramAccoutId())){
            BaseLoginClientChannelBindPo weixinMiniprogram = new BaseLoginClientChannelBindPo();
            weixinMiniprogram.setChannelType(DictEnum.WxAccountType.weixin_miniprogram.name());
            weixinMiniprogram.setClientId(id);
            weixinMiniprogram.setChannelId(dto.getWeixinMiniprogramAccoutId());
            weixinMiniprogram = apiBaseLoginClientChannelBindPoService.preInsert(weixinMiniprogram,getLoginUserId());
            apiBaseLoginClientChannelBindPoService.insert(weixinMiniprogram);

        }

        return new ResponseEntity(resultData, HttpStatus.OK);
    }
}
