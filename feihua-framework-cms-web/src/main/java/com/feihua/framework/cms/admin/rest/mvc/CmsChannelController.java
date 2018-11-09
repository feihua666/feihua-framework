package com.feihua.framework.cms.admin.rest.mvc;

import com.feihua.framework.base.modules.role.dto.BaseRoleDto;
import com.feihua.framework.rest.ResponseJsonRender;
import com.feihua.framework.rest.interceptor.RepeatFormValidator;
import com.feihua.framework.rest.mvc.SuperController;
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
import com.feihua.framework.cms.dto.CmsChannelDto;
import com.feihua.framework.cms.dto.SearchCmsChannelsConditionDto;
import com.feihua.framework.cms.api.ApiCmsChannelPoService;
import com.feihua.framework.cms.admin.rest.dto.AddCmsChannelFormDto;
import com.feihua.framework.cms.admin.rest.dto.UpdateCmsChannelFormDto;
import com.feihua.framework.cms.po.CmsChannelPo;
/**
 * 栏目管理
 * Created by yangwei
 */
@RestController
@RequestMapping("/cms")
public class CmsChannelController extends SuperController {

    private static Logger logger = LoggerFactory.getLogger(CmsChannelController.class);

    @Autowired
    private ApiCmsChannelPoService apiCmsChannelPoService;

    /**
     * 单资源，添加
     * @param dto
     * @return
     */
    @RepeatFormValidator
    @RequiresPermissions("channel:add")
    @RequestMapping(value = "/channel",method = RequestMethod.POST)
    public ResponseEntity add(AddCmsChannelFormDto dto){
        logger.info("添加栏目开始");
        logger.info("当前登录用户id:{}",getLoginUser().getId());
        ResponseJsonRender resultData=new ResponseJsonRender();
        // 表单值设置
        CmsChannelPo basePo = new CmsChannelPo();

        basePo.setName(dto.getName());
        basePo.setPath(dto.getPath());
        basePo.setSequence(dto.getSequence());
        basePo.setSiteId(dto.getSiteId());


        apiCmsChannelPoService.preInsert(basePo,getLoginUser().getId());
        CmsChannelDto r = apiCmsChannelPoService.insert(basePo);
        if (r == null) {
            // 添加失败
            resultData.setCode(ResponseCode.E404_100001.getCode());
            resultData.setMsg(ResponseCode.E404_100001.getMsg());
            logger.info("code:{},msg:{}",resultData.getCode(),resultData.getMsg());
            logger.info("添加栏目结束，失败");
            return new ResponseEntity(resultData,HttpStatus.NOT_FOUND);
        }else{
            // 添加成功，返回添加的数据
            resultData.setData(r);
            logger.info("添加栏目id:{}",r.getId());
            logger.info("添加栏目结束，成功");
            return new ResponseEntity(resultData, HttpStatus.CREATED);
        }
    }

    /**
     * 单资源，删除
     * @param id
     * @return
     */
    @RepeatFormValidator
    @RequiresPermissions("channel:delete")
    @RequestMapping(value = "/channel/{id}",method = RequestMethod.DELETE)
    public ResponseEntity delete(@PathVariable String id){
        logger.info("删除栏目开始");
        logger.info("用户id:{}",getLoginUser().getId());
        logger.info("栏目id:{}",id);
        ResponseJsonRender resultData=new ResponseJsonRender();

            int r = apiCmsChannelPoService.deleteFlagByPrimaryKeyWithUpdateUser(id,getLoginUser().getId());
            if (r <= 0) {
                // 删除失败，可能没有找到资源
                resultData.setCode(ResponseCode.E404_100001.getCode());
                resultData.setMsg(ResponseCode.E404_100001.getMsg());
                logger.info("code:{},msg:{}",resultData.getCode(),resultData.getMsg());
                logger.info("删除栏目结束，失败");
                return new ResponseEntity(resultData,HttpStatus.NOT_FOUND);
            }else{
                // 删除成功
                logger.info("删除的栏目id:{}",id);
                logger.info("删除栏目结束，成功");
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
    @RequiresPermissions("channel:update")
    @RequestMapping(value = "/channel/{id}",method = RequestMethod.PUT)
    public ResponseEntity update(@PathVariable String id, UpdateCmsChannelFormDto dto){
        logger.info("更新栏目开始");
        logger.info("当前登录用户id:{}",getLoginUser().getId());
        logger.info("栏目id:{}",id);
        ResponseJsonRender resultData=new ResponseJsonRender();
        // 表单值设置
        CmsChannelPo basePo = new CmsChannelPo();
        // id
        basePo.setId(id);
        basePo.setName(dto.getName());
        basePo.setPath(dto.getPath());
        basePo.setSequence(dto.getSequence());
        basePo.setSiteId(dto.getSiteId());


        // 用条件更新，乐观锁机制
        CmsChannelPo basePoCondition = new CmsChannelPo();
        basePoCondition.setId(id);
        basePoCondition.setDelFlag(BasePo.YesNo.N.name());
        basePoCondition.setUpdateAt(dto.getUpdateTime());
        apiCmsChannelPoService.preUpdate(basePo,getLoginUser().getId());
        int r = apiCmsChannelPoService.updateSelective(basePo,basePoCondition);
        if (r <= 0) {
            // 更新失败，资源不存在
            resultData.setCode(ResponseCode.E404_100001.getCode());
            resultData.setMsg(ResponseCode.E404_100001.getMsg());
            logger.info("code:{},msg:{}",resultData.getCode(),resultData.getMsg());
            logger.info("更新栏目结束，失败");
            return new ResponseEntity(resultData,HttpStatus.NOT_FOUND);
        }else{
            // 更新成功，已被成功创建
            logger.info("更新的栏目id:{}",id);
            logger.info("更新栏目结束，成功");
            return new ResponseEntity(resultData, HttpStatus.CREATED);
        }
    }

    /**
     * 单资源，获取id栏目
     * @param id
     * @return
     */
    @RepeatFormValidator
    @RequiresPermissions("channel:getById")
    @RequestMapping(value = "/channel/{id}",method = RequestMethod.GET)
    public ResponseEntity getById(@PathVariable String id){

        ResponseJsonRender resultData=new ResponseJsonRender();
        CmsChannelDto baseDataScopeDto = apiCmsChannelPoService.selectByPrimaryKey(id,false);
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
     * 复数资源，搜索栏目
     * @param dto
     * @return
     */
    @RepeatFormValidator
    @RequiresPermissions("channel:search")
    @RequestMapping(value = "/channels",method = RequestMethod.GET)
    public ResponseEntity search(SearchCmsChannelsConditionDto dto){

        ResponseJsonRender resultData=new ResponseJsonRender();
        PageAndOrderbyParamDto pageAndOrderbyParamDto = new PageAndOrderbyParamDto(PageUtils.getPageFromThreadLocal(), OrderbyUtils.getOrderbyFromThreadLocal());
        // 设置当前登录用户id
        dto.setCurrentUserId(getLoginUser().getId());
        dto.setCurrentRoleId(((BaseRoleDto) getLoginUser().getRole()).getId());
        PageResultDto<CmsChannelDto> list = apiCmsChannelPoService.searchCmsChannelsDsf(dto,pageAndOrderbyParamDto);

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
