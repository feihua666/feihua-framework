package com.feihua.framework.base.impl;

import com.feihua.framework.base.modules.datascope.api.ApiBaseDataScopeService;
import com.feihua.framework.base.modules.rel.api.ApiBaseUserDataScopeRelPoService;
import com.feihua.framework.base.modules.rel.dto.BaseUserDataScopeRelDto;
import com.feihua.framework.base.modules.rel.dto.DataScopeBindUsersParamDto;
import com.feihua.framework.base.modules.rel.dto.UserBindDataScopesParamDto;
import com.feihua.framework.base.modules.rel.po.BaseUserDataScopeRelPo;
import com.feihua.utils.spring.SpringContextHolder;
import feihua.jdbc.api.pojo.BasePo;
import feihua.jdbc.api.service.impl.ApiBaseServiceImpl;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * This class was generated by MyBatis Generator.
 * @author yangwei 2018-03-18 10:14:07
 */
@Service
public class ApiBaseUserDataScopeRelPoServiceImpl extends ApiBaseServiceImpl<BaseUserDataScopeRelPo, BaseUserDataScopeRelDto, String> implements ApiBaseUserDataScopeRelPoService {

    public ApiBaseUserDataScopeRelPoServiceImpl() {
        super(BaseUserDataScopeRelDto.class);
    }


    @Transactional( propagation = Propagation.SUPPORTS, readOnly = true )
    @Override
    public BaseUserDataScopeRelDto selectByUserId(String userId) {
        BaseUserDataScopeRelPo baseUserDataScopeRelPo = new BaseUserDataScopeRelPo();
        baseUserDataScopeRelPo.setUserId(userId);
        baseUserDataScopeRelPo.setDelFlag(BasePo.YesNo.N.name());
        return this.selectOne(baseUserDataScopeRelPo);    }

    @Transactional( propagation = Propagation.SUPPORTS, readOnly = true )
    @Override
    public List<BaseUserDataScopeRelDto> selectByDataScopeId(String dataScopeId) {
        BaseUserDataScopeRelPo baseUserDataScopeRelPo = new BaseUserDataScopeRelPo();
        baseUserDataScopeRelPo.setDataScopeId(dataScopeId);
        baseUserDataScopeRelPo.setDelFlag(BasePo.YesNo.N.name());
        return this.selectList(baseUserDataScopeRelPo);
    }
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int deleteFlagByUserId(String userId, String currentUserId) {
        BaseUserDataScopeRelPo baseUserDataScopeRelPo = new BaseUserDataScopeRelPo();
        baseUserDataScopeRelPo.setUserId(userId);
        baseUserDataScopeRelPo.setDelFlag(BasePo.YesNo.N.name());
        int r = this.deleteFlagSelectiveWithUpdateUser(baseUserDataScopeRelPo,currentUserId);
        return r;
    }
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int deleteFlagByDataScopeId(String dataScopeId, String currentUserId) {
        BaseUserDataScopeRelPo baseUserDataScopeRelPo = new BaseUserDataScopeRelPo();
        baseUserDataScopeRelPo.setDataScopeId(dataScopeId);
        baseUserDataScopeRelPo.setDelFlag(BasePo.YesNo.N.name());
        int r = this.deleteFlagSelectiveWithUpdateUser(baseUserDataScopeRelPo,currentUserId);
        return r;
    }
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int userBindDataScopes(UserBindDataScopesParamDto userBindDataScopesParamDto) {

        // 先根据用户id删除关系
        int deleteR = this.deleteFlagByUserId(userBindDataScopesParamDto.getUserId(),userBindDataScopesParamDto.getCurrentUserId());
        // 再插入新的关系
        List<String> dataScopeIds = userBindDataScopesParamDto.getDataScopeIds();
        if(CollectionUtils.isNotEmpty(dataScopeIds)){
            BaseUserDataScopeRelPo baseUserDataScopeRelPo = null;
            List<BaseUserDataScopeRelPo> insertedList = new ArrayList<>(dataScopeIds.size());
            for (String dataScopeId : dataScopeIds) {
                baseUserDataScopeRelPo = new BaseUserDataScopeRelPo();
                baseUserDataScopeRelPo.setUserId(userBindDataScopesParamDto.getUserId());
                baseUserDataScopeRelPo.setDataScopeId(dataScopeId);
                baseUserDataScopeRelPo.setDelFlag(BasePo.YesNo.N.name());
                baseUserDataScopeRelPo = this.preInsert(baseUserDataScopeRelPo,userBindDataScopesParamDto.getCurrentUserId());
                insertedList.add(baseUserDataScopeRelPo );
            }
            return this.insertBatch(insertedList);
        }
        return deleteR;
    }
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int DataScopeBindUsers(DataScopeBindUsersParamDto dataScopeBindUsersParamDto) {

        // 先根据数据范围id删除关系
        int deleteR = this.deleteFlagByDataScopeId(dataScopeBindUsersParamDto.getDataScopeId(),dataScopeBindUsersParamDto.getCurrentUserId());
        // 再插入新的关系
        List<String> roleIds = dataScopeBindUsersParamDto.getUserIds();
        if(CollectionUtils.isNotEmpty(roleIds)){
            BaseUserDataScopeRelPo baseUserDataScopeRelPo = null;
            List<BaseUserDataScopeRelPo> insertedList = new ArrayList<>(roleIds.size());
            for (String roleId : roleIds) {
                baseUserDataScopeRelPo = new BaseUserDataScopeRelPo();
                baseUserDataScopeRelPo.setDataScopeId(dataScopeBindUsersParamDto.getDataScopeId());
                baseUserDataScopeRelPo.setDataScopeId(roleId);
                baseUserDataScopeRelPo.setDelFlag(BasePo.YesNo.N.name());
                baseUserDataScopeRelPo = this.preInsert(baseUserDataScopeRelPo,dataScopeBindUsersParamDto.getCurrentUserId());
                insertedList.add(baseUserDataScopeRelPo );
            }
            return this.insertBatch(insertedList);
        }
        return deleteR;
    }

    @Override
    public BaseUserDataScopeRelDto wrapDto(BaseUserDataScopeRelPo po) {
        if (po == null) {
            return null;
        }
        BaseUserDataScopeRelDto baseUserDataScopeRelDto = new BaseUserDataScopeRelDto();
        baseUserDataScopeRelDto.setDataOfficeId(po.getDataOfficeId());
        baseUserDataScopeRelDto.setDataScopeId(po.getDataScopeId());
        baseUserDataScopeRelDto.setId(po.getId());
        baseUserDataScopeRelDto.setUserId(po.getUserId());
        baseUserDataScopeRelDto.setDataUserId(po.getDataUserId());
        baseUserDataScopeRelDto.setDataAreaId(po.getDataAreaId());
        baseUserDataScopeRelDto.setDataType(po.getDataType());
        baseUserDataScopeRelDto.setUpdateAt(po.getUpdateAt());
        return baseUserDataScopeRelDto;
    }
}