package com.feihua.framework.rest.modules.group.mvc;

import com.feihua.framework.base.modules.area.dto.BaseAreaDto;
import com.feihua.framework.base.modules.office.api.ApiBaseOfficePoService;
import com.feihua.framework.base.modules.office.dto.BaseOfficeDto;
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
import com.feihua.framework.base.modules.group.dto.BaseUserGroupDto;
import com.feihua.framework.base.modules.group.dto.SearchBaseUserGroupsConditionDto;
import com.feihua.framework.base.modules.group.api.ApiBaseUserGroupPoService;
import com.feihua.framework.rest.modules.group.dto.AddBaseUserGroupFormDto;
import com.feihua.framework.rest.modules.group.dto.UpdateBaseUserGroupFormDto;
import com.feihua.framework.base.modules.group.po.BaseUserGroupPo;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户分组接口
 * Created by yangwei
 */
@RestController
@RequestMapping("/base")
public class BaseUserGroupController extends SuperController {

    private static Logger logger = LoggerFactory.getLogger(BaseUserGroupController.class);

    @Autowired
    private ApiBaseUserGroupPoService apiBaseUserGroupPoService;
    @Autowired
    private ApiBaseOfficePoService apiBaseOfficePoService;

    /**
     * 单资源，添加
     * @param dto
     * @return
     */
    @RepeatFormValidator
    @RequiresPermissions("base:group:add")
    @RequestMapping(value = "/group",method = RequestMethod.POST)
    public ResponseEntity add(AddBaseUserGroupFormDto dto){
        logger.info("添加用户分组开始");
        logger.info("当前登录用户id:{}",getLoginUser().getId());
        ResponseJsonRender resultData = new ResponseJsonRender();
        // 表单值设置
        BaseUserGroupPo basePo = new BaseUserGroupPo();
        basePo.setName(dto.getName());
        basePo.setCode(dto.getCode());
        basePo.setType(dto.getType());
        basePo.setDisabled(dto.getDisabled());
        basePo.setDataOfficeId(dto.getDataOfficeId());
        basePo.setIsPublic(dto.getIsPublic());

        basePo = apiBaseUserGroupPoService.preInsert(basePo,getLoginUser().getId());
        BaseUserGroupDto r = apiBaseUserGroupPoService.insert(basePo);
        if (r == null) {
            // 添加失败
            resultData.setCode(ResponseCode.E404_100001.getCode());
            resultData.setMsg(ResponseCode.E404_100001.getMsg());
            logger.info("code:{},msg:{}",resultData.getCode(),resultData.getMsg());
            logger.info("添加用户分组结束，失败");
            return new ResponseEntity(resultData,HttpStatus.NOT_FOUND);
        }else{
            // 添加成功，返回添加的数据
            resultData.setData(r);
            logger.info("添加用户分组id:{}",r.getId());
            logger.info("添加用户分组结束，成功");
            return new ResponseEntity(resultData, HttpStatus.CREATED);
        }
    }

    /**
     * 单资源，删除
     * @param id
     * @return
     */
    @RepeatFormValidator
    @RequiresPermissions("base:group:delete")
    @RequestMapping(value = "/group/{id}",method = RequestMethod.DELETE)
    public ResponseEntity delete(@PathVariable String id){
        logger.info("删除用户分组开始");
        logger.info("用户id:{}",getLoginUser().getId());
        logger.info("用户分组id:{}",id);
        ResponseJsonRender resultData = new ResponseJsonRender();

            int r = apiBaseUserGroupPoService.deleteFlagByPrimaryKeyWithUpdateUser(id,getLoginUser().getId());
            if (r <= 0) {
                // 删除失败，可能没有找到资源
                resultData.setCode(ResponseCode.E404_100001.getCode());
                resultData.setMsg(ResponseCode.E404_100001.getMsg());
                logger.info("code:{},msg:{}",resultData.getCode(),resultData.getMsg());
                logger.info("删除用户分组结束，失败");
                return new ResponseEntity(resultData,HttpStatus.NOT_FOUND);
            }else{
                // 删除成功
                logger.info("删除的用户分组id:{}",id);
                logger.info("删除用户分组结束，成功");
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
    @RequiresPermissions("base:group:update")
    @RequestMapping(value = "/group/{id}",method = RequestMethod.PUT)
    public ResponseEntity update(@PathVariable String id, UpdateBaseUserGroupFormDto dto){
        logger.info("更新用户分组开始");
        logger.info("当前登录用户id:{}",getLoginUser().getId());
        logger.info("用户分组id:{}",id);
        ResponseJsonRender resultData = new ResponseJsonRender();
        // 表单值设置
        BaseUserGroupPo basePo = new BaseUserGroupPo();
        // id
        basePo.setId(id);
        basePo.setName(dto.getName());
        basePo.setCode(dto.getCode());
        basePo.setType(dto.getType());
        basePo.setDisabled(dto.getDisabled());
        basePo.setDataOfficeId(dto.getDataOfficeId());
        basePo.setIsPublic(dto.getIsPublic());

        // 用条件更新，乐观锁机制
        BaseUserGroupPo basePoCondition = new BaseUserGroupPo();
        basePoCondition.setId(id);
        basePoCondition.setDelFlag(BasePo.YesNo.N.name());
        basePoCondition.setUpdateAt(dto.getUpdateTime());
        basePo = apiBaseUserGroupPoService.preUpdate(basePo,getLoginUser().getId());
        int r = apiBaseUserGroupPoService.updateSelective(basePo,basePoCondition);
        if (r <= 0) {
            // 更新失败，资源不存在
            resultData.setCode(ResponseCode.E404_100001.getCode());
            resultData.setMsg(ResponseCode.E404_100001.getMsg());
            logger.info("code:{},msg:{}",resultData.getCode(),resultData.getMsg());
            logger.info("更新用户分组结束，失败");
            return new ResponseEntity(resultData,HttpStatus.NOT_FOUND);
        }else{
            // 更新成功，已被成功创建
            logger.info("更新的用户分组id:{}",id);
            logger.info("更新用户分组结束，成功");
            return new ResponseEntity(resultData, HttpStatus.CREATED);
        }
    }

    /**
     * 单资源，获取id用户分组
     * @param id
     * @return
     */
    @RequiresPermissions("base:group:getById")
    @RequestMapping(value = "/group/{id}",method = RequestMethod.GET)
    public ResponseEntity getById(@PathVariable String id){

        ResponseJsonRender resultData = new ResponseJsonRender();
        BaseUserGroupDto baseDataScopeDto = apiBaseUserGroupPoService.selectByPrimaryKey(id,false);
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
     * 复数资源，搜索用户分组
     * @param dto
     * @return
     */
    @RequiresPermissions("base:group:search")
    @RequestMapping(value = "/groups",method = RequestMethod.GET)
    public ResponseEntity search(SearchBaseUserGroupsConditionDto dto, boolean includeOffice){

        ResponseJsonRender resultData = new ResponseJsonRender();
        PageAndOrderbyParamDto pageAndOrderbyParamDto = new PageAndOrderbyParamDto(PageUtils.getPageFromThreadLocal(), OrderbyUtils.getOrderbyFromThreadLocal());
        // 设置当前登录用户id
        dto.setCurrentUserId(getLoginUser().getId());
        dto.setCurrentRoleId(((BaseRoleDto) getLoginUser().getRole()).getId());
        PageResultDto<BaseUserGroupDto> list = apiBaseUserGroupPoService.searchBaseUserGroupsDsf(dto,pageAndOrderbyParamDto);
        if (list.getData() != null && !list.getData().isEmpty()) {
            //机构
            if (includeOffice) {

                //机构
                Map<String, BaseOfficeDto> officeDtoMap = new HashMap<>();
                BaseOfficeDto officeDto = null;
                for (BaseUserGroupDto userGroupDto : list.getData()) {

                    if(includeOffice && StringUtils.isNotEmpty(userGroupDto.getDataOfficeId())){
                        officeDto = apiBaseOfficePoService.selectByPrimaryKey(userGroupDto.getDataOfficeId());
                        if (officeDto != null) {
                            officeDtoMap.put(userGroupDto.getDataOfficeId(),officeDto);
                        }
                    }
                }
                if (!officeDtoMap.isEmpty()) {
                    resultData.addData("office",officeDtoMap);
                }

            }
        }

        resultData.setPage(list.getPage());
        return returnList(list.getData(),resultData);

    }
}
