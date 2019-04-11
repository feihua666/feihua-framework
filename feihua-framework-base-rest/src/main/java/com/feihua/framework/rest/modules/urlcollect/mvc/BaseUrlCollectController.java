package com.feihua.framework.rest.modules.urlcollect.mvc;

import com.feihua.framework.log.comm.annotation.OperationLog;
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
import com.feihua.framework.base.modules.urlcollect.dto.BaseUrlCollectDto;
import com.feihua.framework.base.modules.urlcollect.dto.SearchBaseUrlCollectsConditionDto;
import com.feihua.framework.base.modules.urlcollect.api.ApiBaseUrlCollectPoService;
import com.feihua.framework.rest.modules.urlcollect.dto.AddBaseUrlCollectFormDto;
import com.feihua.framework.rest.modules.urlcollect.dto.UpdateBaseUrlCollectFormDto;
import com.feihua.framework.base.modules.urlcollect.po.BaseUrlCollectPo;
/**
 * 网址收藏管理
 * Created by yangwei
 */
@RestController
@RequestMapping("/base")
public class BaseUrlCollectController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(BaseUrlCollectController.class);

    @Autowired
    private ApiBaseUrlCollectPoService apiBaseUrlCollectPoService;

    /**
     * 单资源，添加
     * @param addFormDto
     * @return
     */
    @OperationLog(operation = "网址收藏管理",content = "单资源，添加")
    @RepeatFormValidator
    @RequiresPermissions("user")
    @RequestMapping(value = "/urlcollect",method = RequestMethod.POST)
    public ResponseEntity add(AddBaseUrlCollectFormDto addFormDto){
        logger.info("添加网址收藏开始");
        logger.info("当前登录用户id:{}",getLoginUser().getId());
        ResponseJsonRender resultData=new ResponseJsonRender();
        // 表单值设置
        BaseUrlCollectPo basePo = new BaseUrlCollectPo();
        basePo.setUrl(addFormDto.getUrl());
        basePo.setUrlType(addFormDto.getUrlType());
        basePo.setName(addFormDto.getName());
        basePo.setRemark(addFormDto.getRemark());
        basePo.setIcon(addFormDto.getIcon());
        basePo.setIconType(addFormDto.getIconType());
        basePo.setDataUserId(getLoginUser().getId());


        basePo = apiBaseUrlCollectPoService.preInsert(basePo,getLoginUser().getId());
        BaseUrlCollectDto r = apiBaseUrlCollectPoService.insert(basePo);
        if (r == null) {
            // 添加失败
            resultData.setCode(ResponseCode.E404_100001.getCode());
            resultData.setMsg(ResponseCode.E404_100001.getMsg());
            logger.info("code:{},msg:{}",resultData.getCode(),resultData.getMsg());
            logger.info("添加网址收藏结束，失败");
            return new ResponseEntity(resultData,HttpStatus.NOT_FOUND);
        }else{
            // 添加成功，返回添加的数据
            resultData.setData(r);
            logger.info("添加网址收藏id:{}",r.getId());
            logger.info("添加网址收藏结束，成功");
            return new ResponseEntity(resultData, HttpStatus.CREATED);
        }
    }

    /**
     * 单资源，删除
     * @param id
     * @return
     */
    @OperationLog(operation = "网址收藏管理",content = "单资源，删除")
    @RepeatFormValidator
    @RequiresPermissions("user")
    @RequestMapping(value = "/urlcollect/{id}",method = RequestMethod.DELETE)
    public ResponseEntity delete(@PathVariable String id){
        logger.info("删除网址收藏开始");
        logger.info("用户id:{}",getLoginUser().getId());
        logger.info("网址收藏id:{}",id);
        ResponseJsonRender resultData=new ResponseJsonRender();

            int r = apiBaseUrlCollectPoService.deleteFlagByPrimaryKeyWithUpdateUser(id,getLoginUser().getId());
            if (r <= 0) {
                // 删除失败，可能没有找到资源
                resultData.setCode(ResponseCode.E404_100001.getCode());
                resultData.setMsg(ResponseCode.E404_100001.getMsg());
                logger.info("code:{},msg:{}",resultData.getCode(),resultData.getMsg());
                logger.info("删除网址收藏结束，失败");
                return new ResponseEntity(resultData,HttpStatus.NOT_FOUND);
            }else{
                // 删除成功
                logger.info("删除的网址收藏id:{}",id);
                logger.info("删除网址收藏结束，成功");
                return new ResponseEntity(resultData,HttpStatus.NO_CONTENT);
            }
    }

    /**
     * 单资源，更新
     * @param id
     * @param updateFormDto
     * @return
     */
    @OperationLog(operation = "网址收藏管理",content = "单资源，更新")
    @RepeatFormValidator
    @RequiresPermissions("user")
    @RequestMapping(value = "/urlcollect/{id}",method = RequestMethod.PUT)
    public ResponseEntity update(@PathVariable String id, UpdateBaseUrlCollectFormDto updateFormDto){
        logger.info("更新网址收藏开始");
        logger.info("当前登录用户id:{}",getLoginUser().getId());
        logger.info("网址收藏id:{}",id);
        ResponseJsonRender resultData=new ResponseJsonRender();
        // 表单值设置
        BaseUrlCollectPo basePo = new BaseUrlCollectPo();
        // id
        basePo.setId(id);
        basePo.setUrl(updateFormDto.getUrl());
        basePo.setUrlType(updateFormDto.getUrlType());
        basePo.setName(updateFormDto.getName());
        basePo.setRemark(updateFormDto.getRemark());
        basePo.setIcon(updateFormDto.getIcon());
        basePo.setIconType(updateFormDto.getIconType());
        basePo.setDataUserId(getLoginUser().getId());
        //todo

        // 用条件更新，乐观锁机制
        BaseUrlCollectPo basePoCondition = new BaseUrlCollectPo();
        basePoCondition.setId(id);
        basePoCondition.setDelFlag(BasePo.YesNo.N.name());
        basePoCondition.setUpdateAt(updateFormDto.getUpdateTime());
        basePo = apiBaseUrlCollectPoService.preUpdate(basePo,getLoginUser().getId());
        int r = apiBaseUrlCollectPoService.updateSelective(basePo,basePoCondition);
        if (r <= 0) {
            // 更新失败，资源不存在
            resultData.setCode(ResponseCode.E404_100001.getCode());
            resultData.setMsg(ResponseCode.E404_100001.getMsg());
            logger.info("code:{},msg:{}",resultData.getCode(),resultData.getMsg());
            logger.info("更新网址收藏结束，失败");
            return new ResponseEntity(resultData,HttpStatus.NOT_FOUND);
        }else{
            // 更新成功，已被成功创建
            logger.info("更新的网址收藏id:{}",id);
            logger.info("更新网址收藏结束，成功");
            return new ResponseEntity(resultData, HttpStatus.CREATED);
        }
    }

    /**
     * 单资源，获取id网址收藏
     * @param id
     * @return
     */
    @OperationLog(operation = "网址收藏管理",content = "单资源，获取id网址收藏")
    @RequiresPermissions("user")
    @RequestMapping(value = "/urlcollect/{id}",method = RequestMethod.GET)
    public ResponseEntity getById(@PathVariable String id){

        ResponseJsonRender resultData=new ResponseJsonRender();
        BaseUrlCollectDto baseDataScopeDto = apiBaseUrlCollectPoService.selectByPrimaryKey(id,false);
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
     * 单资源，获取url网址收藏
     * @param url
     * @param urlType
     * @return
     */
    @OperationLog(operation = "网址收藏管理",content = "单资源，获取url网址收藏")
    @RequiresPermissions("user")
    @RequestMapping(value = "/urlcollect/self",method = RequestMethod.GET)
    public ResponseEntity getUrl( String url,String urlType){

        ResponseJsonRender resultData=new ResponseJsonRender();
        BaseUrlCollectPo basePoCondition = new BaseUrlCollectPo();
        basePoCondition.setUrl(url);
        basePoCondition.setUrlType(urlType);
        basePoCondition.setDelFlag(BasePo.YesNo.N.name());
        basePoCondition.setDataUserId(getLoginUserId());

        BaseUrlCollectDto baseDataScopeDto = apiBaseUrlCollectPoService.selectOne(basePoCondition);
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
     * 复数资源，搜索网址收藏
     * @param dto
     * @return
     */
    @OperationLog(operation = "网址收藏管理",content = "复数资源，搜索网址收藏")
    @RequiresPermissions("urlcollect:search")
    @RequestMapping(value = "/urlcollects",method = RequestMethod.GET)
    public ResponseEntity search(SearchBaseUrlCollectsConditionDto dto){

        ResponseJsonRender resultData=new ResponseJsonRender();
        PageAndOrderbyParamDto pageAndOrderbyParamDto = new PageAndOrderbyParamDto(PageUtils.getPageFromThreadLocal(), OrderbyUtils.getOrderbyFromThreadLocal());
        // 设置当前登录用户id
        dto.setCurrentUserId(getLoginUser().getId());
        dto.setCurrentRoleId(getLoginUserRoleId());
        PageResultDto<BaseUrlCollectDto> list = apiBaseUrlCollectPoService.searchBaseUrlCollectsDsf(dto,pageAndOrderbyParamDto);

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
    /**
     * 复数资源，搜索网址收藏
     * @param dto
     * @return
     */
    @OperationLog(operation = "网址收藏管理",content = "复数资源，搜索网址收藏")
    @RequiresPermissions("user")
    @RequestMapping(value = "/urlcollects/self",method = RequestMethod.GET)
    public ResponseEntity searchSelf(SearchBaseUrlCollectsConditionDto dto){

        ResponseJsonRender resultData=new ResponseJsonRender();
        PageAndOrderbyParamDto pageAndOrderbyParamDto = new PageAndOrderbyParamDto(PageUtils.getPageFromThreadLocal(), OrderbyUtils.getOrderbyFromThreadLocal());
        // 设置当前登录用户id
        dto.setCurrentUserId(getLoginUser().getId());
        dto.setCurrentRoleId(getLoginUserRoleId());
        dto.setUserId(getLoginUser().getId());
        PageResultDto<BaseUrlCollectDto> list = apiBaseUrlCollectPoService.searchBaseUrlCollectsDsf(dto,pageAndOrderbyParamDto);

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
