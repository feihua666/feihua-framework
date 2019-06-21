package com.feihua.framework.base.impl;

import com.feihua.framework.base.modules.datascope.api.ApiBaseDataScopeService;
import com.feihua.framework.base.modules.postjob.api.ApiBasePostPoService;
import com.feihua.framework.base.modules.postjob.dto.BasePostDto;
import com.feihua.framework.base.modules.rel.api.ApiBasePostRoleRelPoService;
import com.feihua.framework.base.modules.rel.dto.BasePostRoleRelDto;
import com.feihua.framework.base.modules.rel.po.BasePostRoleRelPo;
import com.feihua.framework.constants.DictEnum;
import com.feihua.framework.base.modules.office.api.ApiBaseOfficePoService;
import com.feihua.framework.base.modules.office.dto.BaseOfficeDataScopeDefineDto;
import com.feihua.framework.base.modules.office.dto.BaseOfficeDto;
import com.feihua.framework.base.modules.office.dto.SearchOfficesConditionDto;
import com.feihua.framework.base.modules.office.po.BaseOfficePo;
import com.feihua.framework.base.modules.rel.api.ApiBaseRoleDataScopeRelPoService;
import com.feihua.framework.base.modules.rel.api.ApiBaseUserRoleRelPoService;
import com.feihua.framework.base.modules.rel.dto.BaseUserRoleRelDto;
import com.feihua.framework.base.modules.role.api.ApiBaseRoleDataScopeDefineSelfPoService;
import com.feihua.framework.base.modules.role.api.ApiBaseRolePoService;
import com.feihua.framework.base.modules.role.dto.*;
import com.feihua.framework.base.mapper.BaseRolePoMapper;
import com.feihua.framework.base.modules.role.po.BaseRolePo;
import com.feihua.utils.string.StringUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import feihua.jdbc.api.pojo.BasePo;
import feihua.jdbc.api.pojo.BaseTreePo;
import feihua.jdbc.api.pojo.PageAndOrderbyParamDto;
import feihua.jdbc.api.pojo.PageResultDto;
import feihua.jdbc.api.service.impl.ApiBaseTreeServiceImpl;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * This class was generated by MyBatis Generator.
 * @author yangwei 2017-11-18 17:51:13
 */
@Service
public class ApiBaseRolePoServiceImpl extends ApiBaseTreeServiceImpl<BaseRolePo, BaseRoleDto, String> implements ApiBaseRolePoService {

    public ApiBaseRolePoServiceImpl() {
        super(BaseRoleDto.class);
    }

    @Autowired
    private BaseRolePoMapper baseRolePoMapper;
    @Autowired
    private ApiBaseUserRoleRelPoService apiBaseUserRoleRelPoService;
    @Autowired
    private ApiBaseDataScopeService<BaseRoleDataScopeDefineDto> apiBaseRoleDataScopeService;
    @Autowired
    private ApiBaseDataScopeService<BaseOfficeDataScopeDefineDto> apiBaseOfficeDataScopeService;
    @Autowired
    private ApiBaseOfficePoService apiBaseOfficePoService;
    @Autowired
    private ApiBaseRoleDataScopeDefineSelfPoService apiBaseRoleDataScopeDefineSelfPoService;
    @Autowired
    private ApiBasePostRoleRelPoService apiBasePostRoleRelPoService;

    /**
     * 搜索角色
     * @param dto
     * @return
     */
    @Transactional( propagation = Propagation.SUPPORTS, readOnly = true )
    public PageResultDto<BaseRoleDto> searchRolesDsf(SearchRolesConditionDto dto, PageAndOrderbyParamDto pageAndOrderbyParamDto){
        BaseRoleDataScopeDefineDto roleDataScopeDefineDto = apiBaseRoleDataScopeService.selectDataScopeDefineByUserId(dto.getCurrentUserId(),dto.getCurrentRoleId(),dto.getCurrentPostId());
        // 如果没有设置数据范围定义或设置的为无权限
        if (roleDataScopeDefineDto == null || StringUtils.isEmpty(roleDataScopeDefineDto.getType()) || DictEnum.RoleDataScope.no.name().equals(roleDataScopeDefineDto.getType())) {
            return new PageResultDto();
        }

        SearchRolesConditionDsfDto searchRolesConditionDsfDto = new SearchRolesConditionDsfDto();
        searchRolesConditionDsfDto.setName(dto.getName());
        searchRolesConditionDsfDto.setCode(dto.getCode());
        searchRolesConditionDsfDto.setType(dto.getType());
        searchRolesConditionDsfDto.setDisabled(dto.getType());
        searchRolesConditionDsfDto.setParentId(dto.getParentId());
        searchRolesConditionDsfDto.setDataOfficeId(dto.getDataOfficeId());
        // 如果是所有角色
        if(DictEnum.RoleDataScope.all.name().equals(roleDataScopeDefineDto.getType())){

            super.pageAndOrderbyStart(pageAndOrderbyParamDto);
            Page p = PageHelper.getLocalPage();
            List<BaseRolePo> list = baseRolePoMapper.searchRolesDsf(searchRolesConditionDsfDto);
            return new PageResultDto(this.wrapDtos(list), this.wrapPage(p));
        }
        // 用户所在机构的角色
        if(DictEnum.RoleDataScope.useroffice.name().equals(roleDataScopeDefineDto.getType())){
            BaseOfficeDto officeDto = apiBaseOfficePoService.selectOfficeByUserId(dto.getCurrentUserId());
            if (officeDto == null) {
                return new PageResultDto();
            }
            super.pageAndOrderbyStart(pageAndOrderbyParamDto);
            Page p = PageHelper.getLocalPage();
            searchRolesConditionDsfDto.setDataOfficeId(officeDto.getId());
            List<BaseRolePo> list = baseRolePoMapper.searchRolesDsf(searchRolesConditionDsfDto);
            return new PageResultDto(this.wrapDtos(list), this.wrapPage(p));
        }
        // 用户所在机构及以下机构的角色
        if(DictEnum.RoleDataScope.userofficedown.name().equals(roleDataScopeDefineDto.getType())){
            BaseOfficeDto officeDto = apiBaseOfficePoService.selectOfficeByUserId(dto.getCurrentUserId());
            if (officeDto == null) {
                return new PageResultDto();
            }

            List<BaseOfficePo> officePoList = new ArrayList<>();
            officePoList.add(apiBaseOfficePoService.selectByPrimaryKeySimple(officeDto.getId(),false));
            // 查询子机构

            List<BaseOfficePo> officePos = apiBaseOfficePoService.getChildrenAll(officeDto.getId());
            if(CollectionUtils.isNotEmpty(officePos)){
                officePoList.addAll(officePos);
            }
            if(CollectionUtils.isEmpty(officePoList)){
                return new PageResultDto();
            }
            // 机构查询完，根据机构id查询角色

            StringBuffer sb = new StringBuffer("and (1!=1 ");
            for (BaseOfficePo officePo : officePoList) {
                sb.append(" or data_office_id = '").append(officePo.getId()).append("'");
            }
            sb.append(")");

            super.pageAndOrderbyStart(pageAndOrderbyParamDto);
            Page p = PageHelper.getLocalPage();
            searchRolesConditionDsfDto.setSelfCondition(sb.toString());
            List<BaseRolePo> list = baseRolePoMapper.searchRolesDsf(searchRolesConditionDsfDto);
            return new PageResultDto(this.wrapDtos(list), this.wrapPage(p));
        }
        // 岗位所在机构的角色
        if(DictEnum.RoleDataScope.postoffice.name().equals(roleDataScopeDefineDto.getType())){
            BaseOfficeDto officeDto = apiBaseOfficePoService.selectOfficeByPostId(dto.getCurrentPostId(),false);
            if (officeDto == null) {
                return new PageResultDto();
            }
            super.pageAndOrderbyStart(pageAndOrderbyParamDto);
            Page p = PageHelper.getLocalPage();
            searchRolesConditionDsfDto.setDataOfficeId(officeDto.getId());
            List<BaseRolePo> list = baseRolePoMapper.searchRolesDsf(searchRolesConditionDsfDto);
            return new PageResultDto(this.wrapDtos(list), this.wrapPage(p));
        }
        // 岗位所在机构及以下机构的角色
        if(DictEnum.RoleDataScope.postofficedown.name().equals(roleDataScopeDefineDto.getType())){
            BaseOfficeDto officeDto = apiBaseOfficePoService.selectOfficeByPostId(dto.getCurrentPostId(),false);
            if (officeDto == null) {
                return new PageResultDto();
            }

            List<BaseOfficePo> officePoList = new ArrayList<>();
            officePoList.add(apiBaseOfficePoService.selectByPrimaryKeySimple(officeDto.getId(),false));
            // 查询子机构

            List<BaseOfficePo> officePos = apiBaseOfficePoService.getChildrenAll(officeDto.getId());
            if(CollectionUtils.isNotEmpty(officePos)){
                officePoList.addAll(officePos);
            }
            if(CollectionUtils.isEmpty(officePoList)){
                return new PageResultDto();
            }
            // 机构查询完，根据机构id查询角色

            StringBuffer sb = new StringBuffer("and (1!=1 ");
            for (BaseOfficePo officePo : officePoList) {
                sb.append(" or data_office_id = '").append(officePo.getId()).append("'");
            }
            sb.append(")");

            super.pageAndOrderbyStart(pageAndOrderbyParamDto);
            Page p = PageHelper.getLocalPage();
            searchRolesConditionDsfDto.setSelfCondition(sb.toString());
            List<BaseRolePo> list = baseRolePoMapper.searchRolesDsf(searchRolesConditionDsfDto);
            return new PageResultDto(this.wrapDtos(list), this.wrapPage(p));
        }
        // 角色所在机构下的角色
        if(DictEnum.RoleDataScope.roleoffice.name().equals(roleDataScopeDefineDto.getType())){
            BaseOfficeDto officeDto = apiBaseOfficePoService.selectOfficeByRoleId(dto.getCurrentRoleId(),false);
            if (officeDto == null) {
                return new PageResultDto();
            }
            super.pageAndOrderbyStart(pageAndOrderbyParamDto);
            Page p = PageHelper.getLocalPage();
            searchRolesConditionDsfDto.setDataOfficeId(officeDto.getId());
            List<BaseRolePo> list = baseRolePoMapper.searchRolesDsf(searchRolesConditionDsfDto);
            return new PageResultDto(this.wrapDtos(list), this.wrapPage(p));
        }
        // 角色所在机构及以下机构角色
        if(DictEnum.RoleDataScope.roleofficedown.name().equals(roleDataScopeDefineDto.getType())){
            BaseOfficeDto officeDto = apiBaseOfficePoService.selectOfficeByRoleId(dto.getCurrentRoleId(),false);
            if (officeDto == null) {
                return new PageResultDto();
            }

            List<BaseOfficePo> officePoList = new ArrayList<>();
            officePoList.add(apiBaseOfficePoService.selectByPrimaryKeySimple(officeDto.getId(),false));
            // 查询子机构

            List<BaseOfficePo> officePos = apiBaseOfficePoService.getChildrenAll(officeDto.getId());
            if(CollectionUtils.isNotEmpty(officePos)){
                officePoList.addAll(officePos);
            }
            if(CollectionUtils.isEmpty(officePoList)){
                return new PageResultDto();
            }
            // 机构查询完，根据机构id查询角色

            StringBuffer sb = new StringBuffer("and (1!=1 ");
            for (BaseOfficePo officePo : officePoList) {
                sb.append(" or data_office_id = '").append(officePo.getId()).append("'");
            }
            sb.append(")");

            super.pageAndOrderbyStart(pageAndOrderbyParamDto);
            Page p = PageHelper.getLocalPage();
            searchRolesConditionDsfDto.setSelfCondition(sb.toString());
            List<BaseRolePo> list = baseRolePoMapper.searchRolesDsf(searchRolesConditionDsfDto);
            return new PageResultDto(this.wrapDtos(list), this.wrapPage(p));
        }
        // 机构数据范围下的角色
        if(DictEnum.RoleDataScope.office.name().equals(roleDataScopeDefineDto.getType())){
            // 判断机构是否设置的所有数据
            boolean isOfficeAllData = apiBaseOfficeDataScopeService.isAllData(apiBaseOfficeDataScopeService.selectDataScopeDefineByUserId(dto.getCurrentUserId(),dto.getCurrentRoleId(),dto.getCurrentPostId()));
            if (isOfficeAllData) {
                super.pageAndOrderbyStart(pageAndOrderbyParamDto);
                Page p = PageHelper.getLocalPage();
                List<BaseRolePo> list = baseRolePoMapper.searchRolesDsf(searchRolesConditionDsfDto);
                return new PageResultDto(this.wrapDtos(list), this.wrapPage(p));
            }

            PageResultDto<BaseOfficeDto> offices = apiBaseOfficePoService.searchOfficesDsf(new SearchOfficesConditionDto(),null);
            // 如果机构数据范围为空，返回空
            if (offices == null || CollectionUtils.isEmpty(offices.getData())) {
                return new PageResultDto();
            }


            StringBuffer sb = new StringBuffer("and (1!=1 ");
            for (BaseOfficeDto officeDto : offices.getData()) {
                sb.append(" or data_office_id = '").append(officeDto.getId()).append("'");
            }
            sb.append(")");

            super.pageAndOrderbyStart(pageAndOrderbyParamDto);
            Page p = PageHelper.getLocalPage();
            searchRolesConditionDsfDto.setSelfCondition(sb.toString());
            List<BaseRolePo> list = baseRolePoMapper.searchRolesDsf(searchRolesConditionDsfDto);
            return new PageResultDto(this.wrapDtos(list), this.wrapPage(p));
        }
        // 分配的角色
        if(DictEnum.RoleDataScope.assign.name().equals(roleDataScopeDefineDto.getType())){
            Set<String> roleIds = new HashSet<>();
            // 查询当前用户分配的角色
            List<BaseUserRoleRelDto> userRoleRelDtos = apiBaseUserRoleRelPoService.selectByUserId(dto.getCurrentUserId());
            if(CollectionUtils.isNotEmpty(userRoleRelDtos)){
                for (BaseUserRoleRelDto baseUserRoleRelDto : userRoleRelDtos) {
                    roleIds.add(baseUserRoleRelDto.getRoleId());
                }
            }

            if (roleIds.isEmpty()) {
                return new PageResultDto();
            }

            StringBuffer sb = new StringBuffer("and (1!=1 ");
            for (String roleId : roleIds) {
                sb.append(" or id = '").append(roleId).append("'");
            }
            sb.append(")");

            super.pageAndOrderbyStart(pageAndOrderbyParamDto);
            Page p = PageHelper.getLocalPage();
            searchRolesConditionDsfDto.setSelfCondition(sb.toString());
            List<BaseRolePo> list = baseRolePoMapper.searchRolesDsf(searchRolesConditionDsfDto);
            return new PageResultDto(this.wrapDtos(list), this.wrapPage(p));
        }
        // 分配的角色及以下角色
        if(DictEnum.RoleDataScope.assigndown.name().equals(roleDataScopeDefineDto.getType())){

            Set<String> roleIds = new HashSet<>();
            // 查询当前用户分配的角色
            List<BaseUserRoleRelDto> userRoleRelDtos = apiBaseUserRoleRelPoService.selectByUserId(dto.getCurrentUserId());
            if(CollectionUtils.isEmpty(userRoleRelDtos)){
            }else {
                for (BaseUserRoleRelDto baseUserRoleRelDto : userRoleRelDtos) {
                    roleIds.add(baseUserRoleRelDto.getRoleId());
                }
            }
            if (roleIds.isEmpty()) {
                return new PageResultDto();
            }
            // 子角色
            BaseRoleDto baseRoleDto = null;
            StringBuffer sb = new StringBuffer("and (1!=1 ");
            for (String roleId : roleIds) {
                baseRoleDto = this.selectByPrimaryKey(roleId,false);
                sb.append(" or id = '").append(roleId).append("'");
                sb.append(" or parent_id").append(baseRoleDto.getLevel()).append(" = '").append(roleId).append("'");
            }
            sb.append(")");

            super.pageAndOrderbyStart(pageAndOrderbyParamDto);
            Page p = PageHelper.getLocalPage();
            searchRolesConditionDsfDto.setSelfCondition(sb.toString());
            List<BaseRolePo> list = baseRolePoMapper.searchRolesDsf(searchRolesConditionDsfDto);
            return new PageResultDto(this.wrapDtos(list), this.wrapPage(p));

        }

        // 自定义角色
        if(DictEnum.RoleDataScope.self.name().equals(roleDataScopeDefineDto.getType())){
            List<BaseRoleDataScopeDefineSelfDto> selfDtos = apiBaseRoleDataScopeDefineSelfPoService.selectByRoleDataScopeDefineId(roleDataScopeDefineDto.getId());
            if(CollectionUtils.isEmpty(selfDtos)){
                return new PageResultDto();
            }
            StringBuffer sb = new StringBuffer("and (1!=1 ");
            for (BaseRoleDataScopeDefineSelfDto selfDto : selfDtos) {
                sb.append(" or id = '").append(selfDto.getRoleId()).append("'");
            }
            sb.append(")");

            super.pageAndOrderbyStart(pageAndOrderbyParamDto);
            Page p = PageHelper.getLocalPage();
            searchRolesConditionDsfDto.setSelfCondition(sb.toString());
            List<BaseRolePo> list = baseRolePoMapper.searchRolesDsf(searchRolesConditionDsfDto);
            return new PageResultDto(this.wrapDtos(list), this.wrapPage(p));
        }
        return new PageResultDto();
    }

    @Override
    public List<BaseRoleDto> selectByUserId(String userId,boolean includeDisabled) {
        // 角色信息
        List<BaseUserRoleRelDto> userRoleRelDtos =  apiBaseUserRoleRelPoService.selectByUserId(userId);
        if(CollectionUtils.isNotEmpty(userRoleRelDtos)) {
            List<String> roleIds = new ArrayList<>();
            for (BaseUserRoleRelDto userRoleRelDto : userRoleRelDtos) {
                roleIds.add(userRoleRelDto.getRoleId());
            }
            List<BaseRoleDto> roleDtos = this.selectByPrimaryKeys(roleIds, false);
            if (!includeDisabled) {
                Iterator<BaseRoleDto> iterator  = roleDtos.iterator();
                while (iterator.hasNext()){
                    BaseRoleDto roleDto = iterator.next();
                    if(BasePo.YesNo.Y.name().equals(roleDto.getDisabled())){
                        iterator.remove();
                    }
                }
            }
            return roleDtos;
        }
        return null;
    }

    @Override
    public BaseRoleDto selectByCode(String code) {
        BaseRolePo rolePo = new BaseRolePo();
        rolePo.setCode(code);
        rolePo.setDelFlag(BasePo.YesNo.N.name());
        return this.selectOne(rolePo);
    }

    @Override
    public BaseRoleDto selectByPostId(String postId) {
        BasePostRoleRelDto postRoleRelDto = apiBasePostRoleRelPoService.selectByPostId(postId);
        if (postRoleRelDto != null) {
            return this.selectByPrimaryKey(postRoleRelDto.getRoleId(),false);
        }
        return null;
    }

    @Override
    public BaseRoleDto wrapDto(BaseRolePo po) {
        if (po == null) {
            return null;
        }
        BaseRoleDto roleDto = new BaseRoleDto();
        roleDto.setName(po.getName());
        roleDto.setDataOfficeId(po.getDataOfficeId());
        roleDto.setLevel(po.getLevel());
        roleDto.setDataType(po.getDataType());
        roleDto.setUpdateAt(po.getUpdateAt());
        roleDto.setDataUserId(po.getDataUserId());
        roleDto.setDataAreaId(po.getDataAreaId());
        roleDto.setParentId(po.getParentId());
        roleDto.setCode(po.getCode());
        roleDto.setType(po.getType());
        roleDto.setDisabled(po.getDisabled());
        roleDto.setId(po.getId());
        return roleDto;
    }
}