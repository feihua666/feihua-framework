package com.feihua.framework.base.impl;

import com.feihua.exception.BaseException;
import com.feihua.exception.DataConflictException;
import com.feihua.framework.base.modules.datascope.api.ApiBaseDataScopeDataScopeDefinePoService;
import com.feihua.framework.base.modules.datascope.api.ApiBaseDataScopeDataScopeDefineSelfPoService;
import com.feihua.framework.base.modules.datascope.api.ApiBaseDataScopeService;
import com.feihua.framework.base.modules.datascope.dto.BaseDataScopeDataScopeDefineDto;
import com.feihua.framework.base.modules.datascope.dto.DataScopeDataScopeParamDto;
import com.feihua.framework.base.modules.datascope.po.BaseDataScopeDataScopeDefinePo;
import com.feihua.framework.base.modules.postjob.api.ApiBasePostPoService;
import com.feihua.framework.base.modules.postjob.po.BasePostPo;
import com.feihua.framework.base.modules.rel.api.ApiBasePostRoleRelPoService;
import com.feihua.framework.base.modules.rel.dto.BasePostRoleRelDto;
import com.feihua.framework.base.modules.role.api.ApiBaseRolePoService;
import com.feihua.framework.base.modules.role.po.BaseRolePo;
import com.feihua.framework.constants.DictEnum;
import com.feihua.framework.base.modules.rel.api.ApiBaseRoleDataScopeRelPoService;
import com.feihua.framework.base.modules.rel.api.ApiBaseUserDataScopeRelPoService;
import com.feihua.framework.base.modules.rel.dto.BaseRoleDataScopeRelDto;
import com.feihua.framework.base.modules.rel.dto.BaseUserDataScopeRelDto;
import feihua.jdbc.api.pojo.BasePo;
import feihua.jdbc.api.service.impl.ApiBaseServiceImpl;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * This class was generated by MyBatis Generator.
 * @author yangwei 2018-03-23 17:36:18
 */
@Service
public class ApiBaseDataScopeDataScopeDefinePoServiceImpl extends ApiBaseServiceImpl<BaseDataScopeDataScopeDefinePo, BaseDataScopeDataScopeDefineDto, String> implements ApiBaseDataScopeDataScopeDefinePoService,ApiBaseDataScopeService<BaseDataScopeDataScopeDefineDto> {

    public ApiBaseDataScopeDataScopeDefinePoServiceImpl() {
        super(BaseDataScopeDataScopeDefineDto.class);
    }
    @Autowired
    private ApiBaseUserDataScopeRelPoService apiBaseUserDataScopeRelPoService;
    @Autowired
    private ApiBaseRoleDataScopeRelPoService apiBaseRoleDataScopeRelPoService;
    @Autowired
    private ApiBaseDataScopeDataScopeDefineSelfPoService apiBaseDataScopeDataScopeDefineSelfPoService;
    @Autowired
    private ApiBaseRolePoService apiBaseRolePoService;
    @Autowired
    private ApiBasePostPoService apiBasePostPoService;
    @Autowired
    private ApiBasePostRoleRelPoService apiBasePostRoleRelPoService;

    @Override
    public BaseDataScopeDataScopeDefineDto selectByDataScopeId(String dataScopeId) {
        if(StringUtils.isEmpty(dataScopeId)) return null;
        BaseDataScopeDataScopeDefinePo condition = new BaseDataScopeDataScopeDefinePo();
        condition.setDataScopeId(dataScopeId);
        condition.setDelFlag(BasePo.YesNo.N.name());
        return this.selectOne(condition);
    }

    @Override
    public BaseDataScopeDataScopeDefineDto setDataScopeDataScope(DataScopeDataScopeParamDto dataScopeDataScope) {
        // 先查询是否已经设置
        BaseDataScopeDataScopeDefinePo condition = new BaseDataScopeDataScopeDefinePo();
        condition.setDataScopeId(dataScopeDataScope.getDataScopeId());
        condition.setDelFlag(BasePo.YesNo.N.name());
        BaseDataScopeDataScopeDefinePo dbDataScopeDataScopeDefinePo = this.selectOneSimple(condition);
        // 如果没有设置
        if(dbDataScopeDataScopeDefinePo == null){
            dbDataScopeDataScopeDefinePo = new BaseDataScopeDataScopeDefinePo();
            dbDataScopeDataScopeDefinePo.setDataScopeId(dataScopeDataScope.getDataScopeId());
            dbDataScopeDataScopeDefinePo.setType(dataScopeDataScope.getType());
            dbDataScopeDataScopeDefinePo = this.preInsert(dbDataScopeDataScopeDefinePo,dataScopeDataScope.getCurrentUserId());
            dbDataScopeDataScopeDefinePo = this.insertSimple(dbDataScopeDataScopeDefinePo);
            if(dbDataScopeDataScopeDefinePo == null){
                throw new BaseException("add dataScopeDataScopeDefine return null","E500");
            }
            // 如果是自定义且自定义勾选不为空，添加自定义数据
            if(DictEnum.DataScopeDataScope.self.name().equals(dataScopeDataScope.getType())
                    && CollectionUtils.isNotEmpty(dataScopeDataScope.getDataScopeIds())){
                int r = apiBaseDataScopeDataScopeDefineSelfPoService.insertDataScopeSelf(dataScopeDataScope.getDataScopeIds(),dbDataScopeDataScopeDefinePo.getId(),dataScopeDataScope.getCurrentUserId());
                // 如果失败
                if(r <= 0){
                    throw new BaseException("add dataScopeDataScopeDefineSelf return " + r,"E500");
                }
            }
            return this.wrapDto(dbDataScopeDataScopeDefinePo);
        }
        // 如果已经设置
        else{
            dbDataScopeDataScopeDefinePo.setType(dataScopeDataScope.getType());
            dbDataScopeDataScopeDefinePo = this.preUpdate(dbDataScopeDataScopeDefinePo,dataScopeDataScope.getCurrentUserId());
            // 这里没有用乐观锁
            int updateR = this.updateByPrimaryKeySelective(dbDataScopeDataScopeDefinePo);
            // 更新失败
            if(updateR <= 0){
                throw new BaseException("update dataScopeDataScopeDefine return " + updateR,"E500");
            }
            // 更新成功后
            else{
                // 先删除掉以前配置的自定义勾选
                int deleteR = apiBaseDataScopeDataScopeDefineSelfPoService.deleteDataScopeSelfByDataScopeDataScopeDefineId(dbDataScopeDataScopeDefinePo.getId(),dataScopeDataScope.getCurrentUserId());

                if(DictEnum.DataScopeDataScope.self.name().equals(dataScopeDataScope.getType())
                        && CollectionUtils.isNotEmpty(dataScopeDataScope.getDataScopeIds())){
                    int insertR = apiBaseDataScopeDataScopeDefineSelfPoService.insertDataScopeSelf(dataScopeDataScope.getDataScopeIds(),dbDataScopeDataScopeDefinePo.getId(),dataScopeDataScope.getCurrentUserId());
                    // 如果失败
                    if(insertR <= 0){
                        throw new BaseException("add dataScopeDataScopeDefineSelf return " + insertR,"E500");
                    }
                }
            }

            return this.wrapDto(dbDataScopeDataScopeDefinePo);
        }
    }

    @Override
    public BaseDataScopeDataScopeDefineDto selectDataScopeDefineByUserId(String userId, String roleId, String postId) {
        BaseUserDataScopeRelDto userDataScopeRelDto = apiBaseUserDataScopeRelPoService.selectByUserId(userId);
        // 用户设置了数据范围，以该数据范围优先
        if(userDataScopeRelDto != null){
            BaseDataScopeDataScopeDefineDto defineDto = this.selectByDataScopeId(userDataScopeRelDto.getDataScopeId());
            return defineDto;
        }
        // 取角色设置的数据范围
        BaseRolePo rolePo = apiBaseRolePoService.selectByPrimaryKeySimple(roleId,false);
        if (rolePo != null && !BasePo.YesNo.Y.name().equals(rolePo.getDisabled())) {
            BaseRoleDataScopeRelDto roleDataScopeRelDto = apiBaseRoleDataScopeRelPoService.selectByRoleId(roleId);
            if (roleDataScopeRelDto != null) {
                BaseDataScopeDataScopeDefineDto defineDto = this.selectByDataScopeId(roleDataScopeRelDto.getDataScopeId());
                return defineDto;

            }
        }
        // 取岗位绑定的角色设置的数据范围
        BasePostPo postPo = apiBasePostPoService.selectByPrimaryKeySimple(postId,false);
        if (postPo != null && !BasePo.YesNo.Y.name().equals(postPo.getDisabled())) {
            BasePostRoleRelDto postRoleRelDto = apiBasePostRoleRelPoService.selectByPostId(postId);
            if (postRoleRelDto != null) {
                BaseRoleDataScopeRelDto roleDataScopeRelDto = apiBaseRoleDataScopeRelPoService.selectByRoleId(postRoleRelDto.getRoleId());
                if(roleDataScopeRelDto != null){
                    BaseDataScopeDataScopeDefineDto defineDto = this.selectByDataScopeId(roleDataScopeRelDto.getDataScopeId());
                    return defineDto;
                }
            }
        }

        return null;
    }

    @Override
    public boolean isAllData(BaseDataScopeDataScopeDefineDto dataScopeDefine) {
        if (dataScopeDefine != null && DictEnum.DataScopeDataScope.all.name().equals(dataScopeDefine.getType())) {
            return true;
        }
        return false;
    }


    @Override
    public BaseDataScopeDataScopeDefineDto wrapDto(BaseDataScopeDataScopeDefinePo po) {
        if (po == null) {
            return null;
        }
        BaseDataScopeDataScopeDefineDto defineDto = new BaseDataScopeDataScopeDefineDto();
        defineDto.setDataScopeId(po.getDataScopeId());
        defineDto.setDataOfficeId(po.getDataOfficeId());
        defineDto.setType(po.getType());
        defineDto.setDataUserId(po.getDataUserId());
        defineDto.setId(po.getId());
        defineDto.setDataType(po.getDataType());
        defineDto.setDataAreaId(po.getDataAreaId());
        defineDto.setUpdateAt(po.getUpdateAt());
        return defineDto;
    }
}