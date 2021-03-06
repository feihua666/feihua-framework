package com.feihua.framework.base.impl;

import com.feihua.exception.BaseException;
import com.feihua.framework.base.modules.datascope.api.ApiBaseDataScopeService;
import com.feihua.framework.base.modules.group.api.ApiBaseUserGroupDataScopeDefinePoService;
import com.feihua.framework.base.modules.group.api.ApiBaseUserGroupDataScopeDefineSelfPoService;
import com.feihua.framework.base.modules.group.dto.BaseUserGroupDataScopeDefineDto;
import com.feihua.framework.base.modules.group.dto.UserGroupDataScopeParamDto;
import com.feihua.framework.base.modules.group.po.BaseUserGroupDataScopeDefinePo;
import com.feihua.framework.base.modules.postjob.api.ApiBasePostPoService;
import com.feihua.framework.base.modules.postjob.po.BasePostPo;
import com.feihua.framework.base.modules.rel.api.ApiBasePostRoleRelPoService;
import com.feihua.framework.base.modules.rel.api.ApiBaseRoleDataScopeRelPoService;
import com.feihua.framework.base.modules.rel.api.ApiBaseUserDataScopeRelPoService;
import com.feihua.framework.base.modules.rel.dto.BasePostRoleRelDto;
import com.feihua.framework.base.modules.rel.dto.BaseRoleDataScopeRelDto;
import com.feihua.framework.base.modules.rel.dto.BaseUserDataScopeRelDto;
import com.feihua.framework.base.modules.role.api.ApiBaseRolePoService;
import com.feihua.framework.base.modules.role.po.BaseRolePo;
import com.feihua.framework.constants.DictEnum;
import feihua.jdbc.api.pojo.BasePo;
import feihua.jdbc.api.service.impl.ApiBaseServiceImpl;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * This class was generated by MyBatis Generator.
 * @author yangwei 2019-06-20 13:26:41
 */
@Service
public class ApiBaseUserGroupDataScopeDefinePoServiceImpl extends ApiBaseServiceImpl<BaseUserGroupDataScopeDefinePo, BaseUserGroupDataScopeDefineDto, String> implements ApiBaseUserGroupDataScopeDefinePoService, ApiBaseDataScopeService<BaseUserGroupDataScopeDefineDto> {
    @Autowired
    com.feihua.framework.base.mapper.BaseUserGroupDataScopeDefinePoMapper BaseUserGroupDataScopeDefinePoMapper;

    public ApiBaseUserGroupDataScopeDefinePoServiceImpl() {
        super(BaseUserGroupDataScopeDefineDto.class);
    }


    @Autowired
    private ApiBaseUserGroupDataScopeDefineSelfPoService apiBaseUserGroupDataScopeDefineSelfPoService;
    @Autowired
    private ApiBaseUserDataScopeRelPoService apiBaseUserDataScopeRelPoService;
    @Autowired
    private ApiBaseRoleDataScopeRelPoService apiBaseRoleDataScopeRelPoService;
    @Autowired
    private ApiBaseRolePoService apiBaseRolePoService;
    @Autowired
    private ApiBasePostPoService apiBasePostPoService;
    @Autowired
    private ApiBasePostRoleRelPoService apiBasePostRoleRelPoService;



    @Transactional( propagation = Propagation.SUPPORTS, readOnly = true )
    @Override
    public BaseUserGroupDataScopeDefineDto selectByDataScopeId(String dataScopeId) {
        if(StringUtils.isEmpty(dataScopeId)) return null;
        BaseUserGroupDataScopeDefinePo condition = new BaseUserGroupDataScopeDefinePo();
        condition.setDataScopeId(dataScopeId);
        condition.setDelFlag(BasePo.YesNo.N.name());
        return this.selectOne(condition);
    }
    @Transactional(rollbackFor = Exception.class)
    @Override
    public BaseUserGroupDataScopeDefineDto setUserGroupDataScope(UserGroupDataScopeParamDto userGroupDataScope) {
        // 先查询是否已经设置
        BaseUserGroupDataScopeDefinePo condition = new BaseUserGroupDataScopeDefinePo();
        condition.setDataScopeId(userGroupDataScope.getDataScopeId());
        condition.setDelFlag(BasePo.YesNo.N.name());
        BaseUserGroupDataScopeDefinePo dbUserGroupDataScopeDefinePo = this.selectOneSimple(condition);
        // 如果没有设置
        if(dbUserGroupDataScopeDefinePo == null){
            dbUserGroupDataScopeDefinePo = new BaseUserGroupDataScopeDefinePo();
            dbUserGroupDataScopeDefinePo.setDataScopeId(userGroupDataScope.getDataScopeId());
            dbUserGroupDataScopeDefinePo.setType(userGroupDataScope.getType());
            dbUserGroupDataScopeDefinePo = this.preInsert(dbUserGroupDataScopeDefinePo,userGroupDataScope.getCurrentUserId());
            dbUserGroupDataScopeDefinePo = this.insertSimple(dbUserGroupDataScopeDefinePo);
            if(dbUserGroupDataScopeDefinePo == null){
                throw new BaseException("add userGroupDataScopeDefine return null","E500");
            }
            // 如果是自定义且自定义勾选不为空，添加自定义数据
            if(DictEnum.UserGroupDataScope.self.name().equals(userGroupDataScope.getType())
                    && CollectionUtils.isNotEmpty(userGroupDataScope.getUserGroupIds())){
                int r = apiBaseUserGroupDataScopeDefineSelfPoService.insertUserGroupSelf(userGroupDataScope.getUserGroupIds(),dbUserGroupDataScopeDefinePo.getId(),userGroupDataScope.getCurrentUserId());
                // 如果失败
                if(r <= 0){
                    throw new BaseException("add userGroupDataScopeDefineSelf return " + r,"E500");
                }
            }
            return this.wrapDto(dbUserGroupDataScopeDefinePo);
        }
        // 如果已经设置
        else{
            dbUserGroupDataScopeDefinePo.setType(userGroupDataScope.getType());
            dbUserGroupDataScopeDefinePo = this.preUpdate(dbUserGroupDataScopeDefinePo,userGroupDataScope.getCurrentUserId());
            // 这里没有用乐观锁
            int updateR = this.updateByPrimaryKeySelective(dbUserGroupDataScopeDefinePo);
            // 更新失败
            if(updateR <= 0){
                throw new BaseException("update userGroupDataScopeDefine return " + updateR,"E500");
            }
            // 更新成功后
            else{
                // 先删除掉以前配置的自定义勾选
                int deleteR = apiBaseUserGroupDataScopeDefineSelfPoService.deleteUserGroupSelfByUserGroupDataScopeDefineId(dbUserGroupDataScopeDefinePo.getId(),userGroupDataScope.getCurrentUserId());

                if(DictEnum.UserGroupDataScope.self.name().equals(userGroupDataScope.getType())
                        && CollectionUtils.isNotEmpty(userGroupDataScope.getUserGroupIds())){
                    int insertR = apiBaseUserGroupDataScopeDefineSelfPoService.insertUserGroupSelf(userGroupDataScope.getUserGroupIds(),dbUserGroupDataScopeDefinePo.getId(),userGroupDataScope.getCurrentUserId());
                    // 如果失败
                    if(insertR <= 0){
                        throw new BaseException("add userGroupDataScopeDefineSelf return " + insertR,"E500");
                    }
                }
            }

            return this.wrapDto(dbUserGroupDataScopeDefinePo);
        }
    }

    @Override
    public BaseUserGroupDataScopeDefineDto selectDataScopeDefineByUserId(String userId, String roleId, String postId) {
        BaseUserDataScopeRelDto userDataScopeRelDto = apiBaseUserDataScopeRelPoService.selectByUserId(userId);
        // 用户设置了数据范围，以该数据范围优先
        if(userDataScopeRelDto != null){
            BaseUserGroupDataScopeDefineDto defineDto = this.selectByDataScopeId(userDataScopeRelDto.getDataScopeId());

            return defineDto;
        }
        // 取角色设置的数据范围
        BaseRolePo rolePo = apiBaseRolePoService.selectByPrimaryKeySimple(roleId,false);
        if (rolePo != null && !BasePo.YesNo.Y.name().equals(rolePo.getDisabled())) {
            BaseRoleDataScopeRelDto roleDataScopeRelDto = apiBaseRoleDataScopeRelPoService.selectByRoleId(roleId);
            if (roleDataScopeRelDto != null) {
                BaseUserGroupDataScopeDefineDto defineDto = this.selectByDataScopeId(roleDataScopeRelDto.getDataScopeId());
                return defineDto;
            }
        }
        // 取岗位绑定的角色设置的数据范围
        BasePostPo postPo = apiBasePostPoService.selectByPrimaryKeySimple(postId,false);
        if (postPo != null && !BasePo.YesNo.Y.name().equals(postPo.getDisabled())) {
            BasePostRoleRelDto postRoleRelDto = apiBasePostRoleRelPoService.selectByPostId(postPo.getId());
            if (postRoleRelDto != null) {
                BaseRoleDataScopeRelDto roleDataScopeRelDto = apiBaseRoleDataScopeRelPoService.selectByRoleId(roleId);
                if(roleDataScopeRelDto != null){
                    BaseUserGroupDataScopeDefineDto defineDto = this.selectByDataScopeId(roleDataScopeRelDto.getDataScopeId());
                    return defineDto;
                }
            }
        }

        return null;
    }

    @Override
    public boolean isAllData(BaseUserGroupDataScopeDefineDto dataScopeDefine) {
        if (dataScopeDefine != null && DictEnum.UserGroupDataScope.all.name().equals(dataScopeDefine.getType())) {
            return true;
        }
        return false;
    }

    @Override
    public BaseUserGroupDataScopeDefineDto wrapDto(BaseUserGroupDataScopeDefinePo po) {
        if (po == null) { return null; }
        BaseUserGroupDataScopeDefineDto dto = new BaseUserGroupDataScopeDefineDto();
        dto.setId(po.getId());
        dto.setDataScopeId(po.getDataScopeId());
        dto.setType(po.getType());
        dto.setDataUserId(po.getDataUserId());
        dto.setDataOfficeId(po.getDataOfficeId());
        dto.setDataType(po.getDataType());
        dto.setDataAreaId(po.getDataAreaId());
        dto.setUpdateAt(po.getUpdateAt());
        return dto;
    }
}