package com.feihua.framework.base.impl;

import com.feihua.exception.BaseException;
import com.feihua.framework.base.modules.datascope.api.ApiBaseDataScopeService;
import com.feihua.framework.constants.DictEnum;
import com.feihua.framework.base.modules.function.api.ApiBaseFunctionResourceDataScopeDefinePoService;
import com.feihua.framework.base.modules.function.api.ApiBaseFunctionResourceDataScopeDefineSelfPoService;
import com.feihua.framework.base.modules.function.dto.BaseFunctionResourceDataScopeDefineDto;
import com.feihua.framework.base.modules.function.dto.FunctionResourceDataScopeParamDto;
import com.feihua.framework.base.modules.function.po.BaseFunctionResourceDataScopeDefinePo;
import com.feihua.framework.base.modules.rel.dto.BaseRoleDataScopeRelDto;
import com.feihua.framework.base.modules.rel.dto.BaseUserDataScopeRelDto;
import feihua.jdbc.api.pojo.BasePo;
import feihua.jdbc.api.service.impl.ApiBaseServiceImpl;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * This class was generated by MyBatis Generator.
 * @author yangwei 2018-03-19 14:24:58
 */
@Service
public class ApiBaseFunctionResourceDataScopeDefinePoServiceImpl extends ApiBaseServiceImpl<BaseFunctionResourceDataScopeDefinePo, BaseFunctionResourceDataScopeDefineDto, String> implements ApiBaseFunctionResourceDataScopeDefinePoService,ApiBaseDataScopeService<BaseFunctionResourceDataScopeDefineDto> {

    public ApiBaseFunctionResourceDataScopeDefinePoServiceImpl() {
        super(BaseFunctionResourceDataScopeDefineDto.class);
    }


    @Autowired
    private ApiBaseFunctionResourceDataScopeDefineSelfPoService apiBaseFunctionResourceDataScopeDefineSelfPoService;

    @Transactional( propagation = Propagation.SUPPORTS, readOnly = true )
    @Override
    public BaseFunctionResourceDataScopeDefineDto selectByRoleId(String roleId) {
        if (roleId == null) {
            return null;
        }
        BaseFunctionResourceDataScopeDefinePo condition = new BaseFunctionResourceDataScopeDefinePo();
        condition.setRoleId(roleId);
        condition.setDelFlag(BasePo.YesNo.N.name());
        return this.selectOne(condition);
    }
    @Transactional(rollbackFor = Exception.class)
    @Override
    public BaseFunctionResourceDataScopeDefineDto setFunctionResourceDataScope(FunctionResourceDataScopeParamDto functionResourceDataScope) {
        // 先查询是否已经设置
        BaseFunctionResourceDataScopeDefinePo condition = new BaseFunctionResourceDataScopeDefinePo();
        condition.setRoleId(functionResourceDataScope.getRoleId());
        condition.setDelFlag(BasePo.YesNo.N.name());
        BaseFunctionResourceDataScopeDefinePo dbFunctionResourceDataScopeDefinePo = this.selectOneSimple(condition);
        // 如果没有设置
        if(dbFunctionResourceDataScopeDefinePo == null){
            dbFunctionResourceDataScopeDefinePo = new BaseFunctionResourceDataScopeDefinePo();
            dbFunctionResourceDataScopeDefinePo.setRoleId(functionResourceDataScope.getRoleId());
            dbFunctionResourceDataScopeDefinePo.setType(functionResourceDataScope.getType());
            this.preInsert(dbFunctionResourceDataScopeDefinePo,functionResourceDataScope.getCurrentUserId());
            dbFunctionResourceDataScopeDefinePo = this.insertSimple(dbFunctionResourceDataScopeDefinePo);
            if(dbFunctionResourceDataScopeDefinePo == null){
                throw new BaseException("add functionResourceDataScopeDefine return null","E500");
            }
            // 如果是自定义且自定义勾选不为空，添加自定义数据
            if(DictEnum.FunctionResourceDataScope.self.name().equals(functionResourceDataScope.getType())
                    && CollectionUtils.isNotEmpty(functionResourceDataScope.getFunctionResourceIds())){
                int r = apiBaseFunctionResourceDataScopeDefineSelfPoService.insertFunctionResourceSelf(functionResourceDataScope.getFunctionResourceIds(),dbFunctionResourceDataScopeDefinePo.getId(),functionResourceDataScope.getCurrentUserId());
                // 如果失败
                if(r <= 0){
                    throw new BaseException("add functionResourceDataScopeDefineSelf return " + r,"E500");
                }
            }
            return this.wrapDto(dbFunctionResourceDataScopeDefinePo);
        }
        // 如果已经设置
        else{
            dbFunctionResourceDataScopeDefinePo.setType(functionResourceDataScope.getType());
            this.preUpdate(dbFunctionResourceDataScopeDefinePo,functionResourceDataScope.getCurrentUserId());
            // 这里没有用乐观锁
            int updateR = this.updateByPrimaryKeySelective(dbFunctionResourceDataScopeDefinePo);
            // 更新失败
            if(updateR <= 0){
                throw new BaseException("update functionResourceDataScopeDefine return " + updateR,"E500");
            }
            // 更新成功后
            else{
                // 先删除掉以前配置的自定义勾选
                int deleteR = apiBaseFunctionResourceDataScopeDefineSelfPoService.deleteFunctionResourceSelfByFunctionResourceDataScopeDefineId(dbFunctionResourceDataScopeDefinePo.getId(),functionResourceDataScope.getCurrentUserId());

                if(DictEnum.FunctionResourceDataScope.self.name().equals(functionResourceDataScope.getType())
                        && CollectionUtils.isNotEmpty(functionResourceDataScope.getFunctionResourceIds())){
                    int insertR = apiBaseFunctionResourceDataScopeDefineSelfPoService.insertFunctionResourceSelf(functionResourceDataScope.getFunctionResourceIds(),dbFunctionResourceDataScopeDefinePo.getId(),functionResourceDataScope.getCurrentUserId());
                    // 如果失败
                    if(insertR <= 0){
                        throw new BaseException("add functionResourceDataScopeDefineSelf return " + insertR,"E500");
                    }
                }
            }

            return this.wrapDto(dbFunctionResourceDataScopeDefinePo);
        }
    }
    @Transactional( propagation = Propagation.SUPPORTS, readOnly = true )
    @Override
    public BaseFunctionResourceDataScopeDefineDto selectDataScopeDefineByUserId(String userId, String roleId) {

        return selectByRoleId(roleId);
    }
    @Transactional( propagation = Propagation.SUPPORTS, readOnly = true )
    @Override
    public boolean isAllData(BaseFunctionResourceDataScopeDefineDto dataScopeDefine) {
        if (dataScopeDefine != null && DictEnum.RoleDataScope.all.name().equals(dataScopeDefine.getType())) {
            return true;
        }
        return false;
    }

    @Override
    public void checkConflict(List<String> dataScopeIds) throws BaseException {
        // 没有冲突处理
    }

    @Override
    public BaseFunctionResourceDataScopeDefineDto wrapDto(BaseFunctionResourceDataScopeDefinePo po) {
        if (po == null) {
            return null;
        }
        BaseFunctionResourceDataScopeDefineDto functionResourceDataScopeDefineDto = new BaseFunctionResourceDataScopeDefineDto();
        functionResourceDataScopeDefineDto.setDataOfficeId(po.getDataOfficeId());
        functionResourceDataScopeDefineDto.setRoleId(po.getRoleId());
        functionResourceDataScopeDefineDto.setType(po.getType());
        functionResourceDataScopeDefineDto.setDataUserId(po.getDataUserId());
        functionResourceDataScopeDefineDto.setId(po.getId());
        functionResourceDataScopeDefineDto.setDataType(po.getDataType());
        functionResourceDataScopeDefineDto.setDataAreaId(po.getDataAreaId());
        functionResourceDataScopeDefineDto.setUpdateAt(po.getUpdateAt());
        return functionResourceDataScopeDefineDto;
    }
}