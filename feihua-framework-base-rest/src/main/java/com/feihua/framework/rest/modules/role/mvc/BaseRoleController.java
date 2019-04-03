package com.feihua.framework.rest.modules.role.mvc;


import com.feihua.framework.base.modules.area.api.ApiBaseAreaPoService;
import com.feihua.framework.base.modules.area.dto.BaseAreaDto;
import com.feihua.framework.base.modules.office.api.ApiBaseOfficePoService;
import com.feihua.framework.base.modules.office.dto.BaseOfficeDto;
import com.feihua.framework.base.modules.role.api.ApiBaseRolePoService;
import com.feihua.framework.base.modules.role.dto.BaseRoleDto;
import com.feihua.framework.base.modules.role.dto.SearchRolesConditionDto;
import com.feihua.framework.base.modules.role.po.BaseRolePo;
import com.feihua.framework.log.comm.annotation.OperationLog;
import com.feihua.utils.http.httpServletResponse.ResponseCode;
import com.feihua.framework.rest.ResponseJsonRender;
import com.feihua.framework.rest.modules.role.dto.AddRoleFormDto;
import com.feihua.framework.rest.modules.role.dto.UpdateRoleFormDto;
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
 * 角色接口
 * Created by yangwei
 * Created at 2017/8/2 14:52
 */
@RestController
@RequestMapping("/base")
public class BaseRoleController extends BaseController {
    private static Logger logger = LoggerFactory.getLogger(BaseRoleController.class);


    @Autowired
    private ApiBaseRolePoService apiBaseRolePoService;
    @Autowired
    private ApiBaseOfficePoService apiBaseOfficePoService;
    @Autowired
    private ApiBaseAreaPoService apiBaseAreaPoService;

    /**
     * 单资源，添加角色
     * @param addRoleFormDto
     * @return
     */
    @OperationLog(operation = "角色接口",content = "单资源，添加角色")
    @RepeatFormValidator
    @RequiresPermissions("base:role:add")
    @RequestMapping(value = "/role",method = RequestMethod.POST)
    public ResponseEntity addRole(AddRoleFormDto addRoleFormDto){
        logger.info("添加角色开始");
        logger.info("角色id:{}",getLoginUser().getId());
        ResponseJsonRender resultData = new ResponseJsonRender();
        // 查检
        if(StringUtils.isNotEmpty(addRoleFormDto.getCode())){
            BaseRolePo baseRolePoCheckConditon = new BaseRolePo();
            baseRolePoCheckConditon.setCode(addRoleFormDto.getCode());
            baseRolePoCheckConditon.setDelFlag(BasePo.YesNo.N.name());

            List dblist = apiBaseRolePoService.selectListSimple(baseRolePoCheckConditon);
            if (dblist != null && !dblist.isEmpty()) {
                // 添加失败
                resultData.setCode(ResponseCode.E409_100001.getCode());
                resultData.setMsg(ResponseCode.E409_100001.getMsg());
                logger.info("code:{},msg:{}",resultData.getCode(),resultData.getMsg());
                logger.info("添加角色结束，失败");
                return new ResponseEntity(resultData,HttpStatus.CONFLICT);
            }
        }

        

        BaseRolePo baseRolePo = new BaseRolePo();
        baseRolePo.setName(addRoleFormDto.getName());
        baseRolePo.setCode(addRoleFormDto.getCode());
        baseRolePo.setDisabled(addRoleFormDto.getDisabled());
        baseRolePo.setType(addRoleFormDto.getType());
        baseRolePo.setParentId(addRoleFormDto.getParentId());
        baseRolePo.setDataOfficeId(addRoleFormDto.getDataOfficeId());

        baseRolePo = apiBaseRolePoService.preInsert(baseRolePo,getLoginUser().getId());
        BaseRoleDto roleDto = apiBaseRolePoService.insert(baseRolePo);
        if(roleDto == null){
            logger.info("code:{},msg:{}",resultData.getCode(),resultData.getMsg());
            logger.info("添加角色结束，失败");
            return new ResponseEntity(resultData,HttpStatus.NOT_FOUND);
        }else {
            resultData.setData(roleDto);
            logger.info("code:{},msg:{}",resultData.getCode(),resultData.getMsg());
            logger.info("添加角色结束，成功");
            return new ResponseEntity(resultData, HttpStatus.CREATED);
        }

    }

    /**
     * 单资源，删除角色
     * @param id
     * @return
     */
    @OperationLog(operation = "角色接口",content = "单资源，删除角色")
    @RequiresPermissions("base:role:delete")
    @RequestMapping(value = "/role/{id}",method = RequestMethod.DELETE)
    public ResponseEntity roleDeleteById(@PathVariable("id") String id){
        logger.info("删除角色开始");
        logger.info("当前登录角色id:{}",getLoginUser().getId());
        logger.info("角色id:{}",id);
        ResponseJsonRender resultData = new ResponseJsonRender();

        List<BaseRolePo> childrenAll = apiBaseRolePoService.getChildrenAll(id);

        if(CollectionUtils.isNotEmpty(childrenAll)){
            resultData.setMsg(ResponseCode.E403_100003.getMsg() + ",children nodes exist");
            resultData.setCode(ResponseCode.E403_100003.getCode());
            logger.info("code:{},msg:{}",resultData.getCode(),resultData.getMsg());
            logger.info("删除角色结束，失败");
            return new ResponseEntity(resultData,HttpStatus.FORBIDDEN);
        }else{
            int r = apiBaseRolePoService.deleteFlagByPrimaryKeyWithUpdateUser(id,getLoginUser().getId());
            if(r == 0){
                resultData.setCode(ResponseCode.E404_100001.getCode());
                resultData.setMsg(ResponseCode.E404_100001.getMsg());
                logger.info("code:{},msg:{}",resultData.getCode(),resultData.getMsg());
                logger.info("删除角色结束，失败");
                return new ResponseEntity(resultData, HttpStatus.NOT_FOUND);
            }else{
                // 删除成功
                logger.info("删除的角色id:{}",id);
                logger.info("删除角色结束，成功");
                return new ResponseEntity(resultData,HttpStatus.NO_CONTENT);
            }
        }
    }

    /**
     * 单资源，更新角色
     * @param id
     * @param updateRoleFormDto
     * @return
     */
    @OperationLog(operation = "角色接口",content = "单资源，更新角色")
    @RequiresPermissions("base:role:update")
    @RequestMapping(value = "/role/{id}",method = RequestMethod.PUT)
    public ResponseEntity roleUpdateById(@PathVariable String id,UpdateRoleFormDto updateRoleFormDto){
        logger.info("更新角色开始");
        logger.info("当前登录角色id:{}",getLoginUser().getId());
        logger.info("角色id:{}",id);
        ResponseJsonRender resultData=new ResponseJsonRender();
        // 查检
        if (StringUtils.isNotEmpty(updateRoleFormDto.getCode())) {
            BaseRolePo baseRolePoCheckConditon = new BaseRolePo();
            baseRolePoCheckConditon.setCode(updateRoleFormDto.getCode());
            baseRolePoCheckConditon.setDelFlag(BasePo.YesNo.N.name());

            List<BaseRolePo> dblist = apiBaseRolePoService.selectListSimple(baseRolePoCheckConditon);
            if (dblist != null && !dblist.isEmpty() && !dblist.get(0).getId().equals(id)) {
                // 添加失败
                resultData.setCode(ResponseCode.E409_100001.getCode());
                resultData.setMsg(ResponseCode.E409_100001.getMsg());
                logger.info("code:{},msg:{}",resultData.getCode(),resultData.getMsg());
                logger.info("更新角色结束，失败");
                return new ResponseEntity(resultData,HttpStatus.CONFLICT);
            }
        }


        // 更新数据
        BaseRolePo baseRolePo = new BaseRolePo();
        baseRolePo.setId(id);
        baseRolePo.setName(updateRoleFormDto.getName());
        baseRolePo.setCode(updateRoleFormDto.getCode());
        baseRolePo.setDisabled(updateRoleFormDto.getDisabled());
        baseRolePo.setType(updateRoleFormDto.getType());
        baseRolePo.setParentId(updateRoleFormDto.getParentId());
        baseRolePo.setDataOfficeId(updateRoleFormDto.getDataOfficeId());

        // 更新条件
        BaseRolePo updateConditionBaseRolePo = new BaseRolePo();
        updateConditionBaseRolePo.setId(id);
        updateConditionBaseRolePo.setUpdateAt(updateRoleFormDto.getUpdateTime());

        baseRolePo = apiBaseRolePoService.preUpdate(baseRolePo,getLoginUser().getId());
        int r = apiBaseRolePoService.updateSelective(baseRolePo,updateConditionBaseRolePo);
        if(r == 0){
            // 更新失败，资源不存在
            resultData.setCode(ResponseCode.E404_100001.getCode());
            resultData.setMsg(ResponseCode.E404_100001.getMsg());
            logger.info("code:{},msg:{}",resultData.getCode(),resultData.getMsg());
            logger.info("更新角色结束，失败");
            return new ResponseEntity(resultData, HttpStatus.NOT_FOUND);
        }else{
            // 更新成功，已被成功创建
            logger.info("更新的角色id:{}",id);
            logger.info("更新角色结束，成功");
            return new ResponseEntity(resultData, HttpStatus.CREATED);
        }
    }

    /**
     * 单资源，获取id角色
     * @param id
     * @return
     */
    @OperationLog(operation = "角色接口",content = "单资源，获取id角色")
    @RequiresPermissions("base:role:id:get")
    @RequestMapping(value = "/role/{id}",method = RequestMethod.GET)
    public ResponseEntity roleById(@PathVariable("id") String id){
        ResponseJsonRender resultData = new ResponseJsonRender();
        BaseRoleDto roleDto = apiBaseRolePoService.selectByPrimaryKey(id,false);
        if(roleDto == null){
            resultData.setCode(ResponseCode.E404_100001.getCode());
            resultData.setMsg(ResponseCode.E404_100001.getMsg());
            return new ResponseEntity(resultData, HttpStatus.NOT_FOUND);
        }
        resultData.setData(roleDto);
        return new ResponseEntity(resultData, HttpStatus.OK);
    }

    /**
     * 多资源，搜索角色
     * @param dto
     * @return
     */
    @OperationLog(operation = "角色接口",content = "多资源，搜索角色")
    @RequiresPermissions("base:role:search")
    @RequestMapping(value = "/roles",method = RequestMethod.GET)
    public ResponseEntity searchUsers(SearchRolesConditionDto dto, boolean includeParent, boolean includeOffice,boolean includeArea){
        ResponseJsonRender resultData = new ResponseJsonRender();
        PageResultDto<BaseRoleDto> pageResultDto = apiBaseRolePoService.searchRolesDsf(dto,new PageAndOrderbyParamDto(PageUtils.getPageFromThreadLocal(), OrderbyUtils.getOrderbyFromThreadLocal()));

        if(CollectionUtils.isEmpty(pageResultDto.getData())){
            resultData.setCode(ResponseCode.E404_100001.getCode());
            resultData.setMsg(ResponseCode.E404_100001.getMsg());
            return new ResponseEntity(resultData, HttpStatus.NOT_FOUND);
        }

        //机构和区域、父级
        if (includeOffice || includeArea || includeParent) {
            //父级
            Map<String,BaseRoleDto> roleDtoMap = new HashMap<>();
            BaseRoleDto roleDto = null;

            //区域
            Map<String,BaseAreaDto> areaDtoMap = new HashMap<>();
            BaseAreaDto areaDto = null;

            //机构
            Map<String,BaseOfficeDto> officeDtoMap = new HashMap<>();
            BaseOfficeDto officeDto = null;
            for (BaseRoleDto _roleDto : pageResultDto.getData()) {
                if(includeArea && StringUtils.isNotEmpty(_roleDto.getDataAreaId())){
                    areaDto = apiBaseAreaPoService.selectByPrimaryKey(_roleDto.getDataAreaId());
                    if (areaDto != null) {
                        areaDtoMap.put(_roleDto.getDataAreaId(),areaDto);
                    }
                }

                if(includeOffice && StringUtils.isNotEmpty(_roleDto.getDataOfficeId())){
                    officeDto = apiBaseOfficePoService.selectByPrimaryKey(_roleDto.getDataOfficeId());
                    if (officeDto != null) {
                        officeDtoMap.put(_roleDto.getDataOfficeId(),officeDto);
                    }
                }

                if(includeParent && StringUtils.isNotEmpty(_roleDto.getParentId())){
                    roleDto = apiBaseRolePoService.selectByPrimaryKey(_roleDto.getParentId());
                    if (roleDto != null) {
                        roleDtoMap.put(_roleDto.getParentId(),roleDto);
                    }
                }
            }
            if (!areaDtoMap.isEmpty()) {
                resultData.addData("area",areaDtoMap);
            }
            if (!officeDtoMap.isEmpty()) {
                resultData.addData("office",officeDtoMap);
            }
            if (!roleDtoMap.isEmpty()) {
                resultData.addData("parent",roleDtoMap);
            }
        }

        resultData.setData(pageResultDto.getData());
        resultData.setPage(pageResultDto.getPage());
        return new ResponseEntity(resultData, HttpStatus.OK);
    }


}
