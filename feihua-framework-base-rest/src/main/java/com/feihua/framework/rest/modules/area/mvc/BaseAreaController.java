package com.feihua.framework.rest.modules.area.mvc;

import com.feihua.framework.base.modules.area.api.ApiBaseAreaPoService;
import com.feihua.framework.base.modules.area.dto.BaseAreaDto;
import com.feihua.framework.base.modules.area.dto.SearchAreasConditionDto;
import com.feihua.framework.base.modules.area.po.BaseAreaPo;
import com.feihua.framework.base.modules.role.dto.BaseRoleDto;
import com.feihua.framework.log.comm.annotation.OperationLog;
import com.feihua.utils.http.httpServletResponse.ResponseCode;
import com.feihua.framework.rest.ResponseJsonRender;
import com.feihua.framework.rest.modules.area.dto.AddAreaFormDto;
import com.feihua.framework.rest.modules.area.dto.UpdateAreaFormDto;
import com.feihua.framework.rest.interceptor.RepeatFormValidator;
import com.feihua.framework.rest.modules.common.mvc.BaseController;
import feihua.jdbc.api.pojo.BasePo;
import feihua.jdbc.api.pojo.PageAndOrderbyParamDto;
import feihua.jdbc.api.pojo.PageResultDto;
import feihua.jdbc.api.utils.OrderbyUtils;
import feihua.jdbc.api.utils.PageUtils;
import org.apache.commons.collections.CollectionUtils;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 区域接口
 * Created by yangwei
 * Created at 2017/12/21 13:13
 */
@RestController
@RequestMapping("/base")
public class BaseAreaController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(BaseAreaController.class);

    @Autowired
    private ApiBaseAreaPoService apiBaseAreaPoService;

    /**
     * 单资源，添加区域
     * @param addAreaFormDto
     * @return
     */
    @OperationLog(operation = "区域接口", content = "单资源，添加区域")
    @RepeatFormValidator
    @RequiresPermissions("base:area:add")
    @RequestMapping(value = "/area",method = RequestMethod.POST)
    public ResponseEntity addArea(AddAreaFormDto addAreaFormDto){
        logger.info("添加区域开始");
        logger.info("当前登录用户id:{}",getLoginUser().getId());
        ResponseJsonRender resultData=new ResponseJsonRender();
        // 表单值设置
        BaseAreaPo baseAreaPo = new BaseAreaPo();
        baseAreaPo.setName(addAreaFormDto.getName());
        baseAreaPo.setType(addAreaFormDto.getType());
        baseAreaPo.setSequence(addAreaFormDto.getSequence());
        baseAreaPo.setParentId(addAreaFormDto.getParentId());

        baseAreaPo = apiBaseAreaPoService.preInsert(baseAreaPo,getLoginUser().getId());
        BaseAreaDto r = apiBaseAreaPoService.insert(baseAreaPo);
        if (r == null) {
            // 添加失败
            resultData.setCode(ResponseCode.E404_100001.getCode());
            resultData.setMsg(ResponseCode.E404_100001.getMsg());
            logger.info("code:{},msg:{}",resultData.getCode(),resultData.getMsg());
            logger.info("添加区域结束，失败");
            return new ResponseEntity(resultData,HttpStatus.NOT_FOUND);
        }else{
            // 添加成功，返回添加的数据
            resultData.setData(r);
            logger.info("添加区域id:{}",r.getId());
            logger.info("添加区域结束，成功");
            return new ResponseEntity(resultData, HttpStatus.CREATED);
        }
    }

    /**
     * 单资源，删除区域
     * @param id
     * @return
     */
    @OperationLog(operation = "区域接口", content = "单资源，删除区域")
    @RepeatFormValidator
    @RequiresPermissions("base:area:delete")
    @RequestMapping(value = "/area/{id}",method = RequestMethod.DELETE)
    public ResponseEntity deleteArea(@PathVariable String id){
        logger.info("删除区域开始");
        logger.info("用户id:{}",getLoginUser().getId());
        logger.info("区域id:{}",id);
        ResponseJsonRender resultData=new ResponseJsonRender();

        List<BaseAreaPo> children = apiBaseAreaPoService.getChildrenAll(id);
        // 如果存在子级，则不充许删除
        if(CollectionUtils.isNotEmpty(children)){
            resultData.setMsg(ResponseCode.E403_100003.getMsg() + ",children nodes exist");
            resultData.setCode(ResponseCode.E403_100003.getCode());
            logger.info("code:{},msg:{}",resultData.getCode(),resultData.getMsg());
            logger.info("删除区域结束，失败");
            return new ResponseEntity(resultData,HttpStatus.FORBIDDEN);
        }else {
            int r = apiBaseAreaPoService.deleteFlagByPrimaryKeyWithUpdateUser(id,getLoginUser().getId());
            if (r <= 0) {
                // 删除失败，可能没有找到资源
                resultData.setCode(ResponseCode.E404_100001.getCode());
                resultData.setMsg(ResponseCode.E404_100001.getMsg());
                logger.info("code:{},msg:{}",resultData.getCode(),resultData.getMsg());
                logger.info("删除区域结束，失败");
                return new ResponseEntity(resultData,HttpStatus.NOT_FOUND);
            }else{
                // 删除成功
                logger.info("删除的区域id:{}",id);
                logger.info("删除区域结束，成功");
                return new ResponseEntity(resultData,HttpStatus.NO_CONTENT);
            }
        }
    }

    /**
     * 单资源，更新区域
     * @param id
     * @param updateAreaFormDto
     * @return
     */
    @OperationLog(operation = "区域接口", content = "单资源，更新区域")
    @RepeatFormValidator
    @RequiresPermissions("base:area:update")
    @RequestMapping(value = "/area/{id}",method = RequestMethod.PUT)
    public ResponseEntity updateArea(@PathVariable String id, UpdateAreaFormDto updateAreaFormDto){
        logger.info("更新区域开始");
        logger.info("当前登录用户id:{}",getLoginUser().getId());
        logger.info("区域id:{}",id);
        ResponseJsonRender resultData=new ResponseJsonRender();
        // 表单值设置
        BaseAreaPo baseAreaPo = new BaseAreaPo();
        // id
        baseAreaPo.setId(id);
        baseAreaPo.setName(updateAreaFormDto.getName());
        baseAreaPo.setType(updateAreaFormDto.getType());
        baseAreaPo.setSequence(updateAreaFormDto.getSequence());
        baseAreaPo.setParentId(updateAreaFormDto.getParentId());

        // 用条件更新，乐观锁机制
        BaseAreaPo baseAreaPoCondition = new BaseAreaPo();
        baseAreaPoCondition.setId(id);
        baseAreaPoCondition.setDelFlag(BasePo.YesNo.N.name());
        baseAreaPoCondition.setUpdateAt(updateAreaFormDto.getUpdateTime());
        baseAreaPo = apiBaseAreaPoService.preUpdate(baseAreaPo,getLoginUser().getId());
        int r = apiBaseAreaPoService.updateSelective(baseAreaPo,baseAreaPoCondition);
        if (r <= 0) {
            // 更新失败，资源不存在
            resultData.setCode(ResponseCode.E404_100001.getCode());
            resultData.setMsg(ResponseCode.E404_100001.getMsg());
            logger.info("code:{},msg:{}",resultData.getCode(),resultData.getMsg());
            logger.info("更新区域结束，失败");
            return new ResponseEntity(resultData,HttpStatus.NOT_FOUND);
        }else{
            // 更新成功，已被成功创建
            logger.info("更新的区域id:{}",id);
            logger.info("更新区域结束，成功");
            return new ResponseEntity(resultData, HttpStatus.CREATED);
        }
    }

    /**
     * 单资源，获取id区域
     * @param id
     * @return
     */
    @OperationLog(operation = "区域接口", content = "单资源，获取id区域")
    @RequiresPermissions("base:area:getById")
    @RequestMapping(value = "/area/{id}",method = RequestMethod.GET)
    public ResponseEntity getById(@PathVariable String id){

        ResponseJsonRender resultData=new ResponseJsonRender();
        BaseAreaDto baseAreaDto = apiBaseAreaPoService.selectByPrimaryKey(id,false);
        if(baseAreaDto != null){
            resultData.setData(baseAreaDto);
            return new ResponseEntity(resultData, HttpStatus.OK);
        }else{
            resultData.setCode(ResponseCode.E404_100001.getCode());
            resultData.setMsg(ResponseCode.E404_100001.getMsg());
            return new ResponseEntity(resultData,HttpStatus.NOT_FOUND);
        }
    }

    /**
     * 复数资源，搜索区域
     * @param dto
     * @return
     */
    @OperationLog(operation = "区域接口", content = "复数资源，搜索区域")
    @RequiresPermissions("base:area:search")
    @RequestMapping(value = "/areas",method = RequestMethod.GET)
    public ResponseEntity searchAreas(SearchAreasConditionDto dto, boolean includeParent){

        ResponseJsonRender resultData=new ResponseJsonRender();
        PageAndOrderbyParamDto pageAndOrderbyParamDto = new PageAndOrderbyParamDto(PageUtils.getPageFromThreadLocal(), OrderbyUtils.getOrderbyFromThreadLocal());
        // 设置当前登录用户id
        dto.setCurrentUserId(getLoginUserId());
        dto.setCurrentRoleId(getLoginUserRoleId());
        PageResultDto<BaseAreaDto> list = apiBaseAreaPoService.searchAreasDsf(dto,pageAndOrderbyParamDto);

        if(CollectionUtils.isNotEmpty(list.getData())){
            //父级
            if (includeParent) {
                Map<String,BaseAreaDto> parentDtoMap = new HashMap<>();
                BaseAreaDto parentDto = null;
                for (BaseAreaDto baseAreaDto : list.getData()) {
                    if(StringUtils.isNotEmpty(baseAreaDto.getParentId())){
                        parentDto = apiBaseAreaPoService.selectByPrimaryKey(baseAreaDto.getParentId());
                        if (parentDto != null) {
                            parentDtoMap.put(baseAreaDto.getParentId(),parentDto);
                        }
                    }
                }

                if (!parentDtoMap.isEmpty()) {
                    resultData.addData("parent",parentDtoMap);
                }
            }
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
