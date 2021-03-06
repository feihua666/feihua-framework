package com.feihua.framework.base.impl;

import com.feihua.framework.base.modules.rel.api.ApiBaseUserGroupDataScopeRelPoService;
import com.feihua.framework.base.modules.rel.dto.BaseUserGroupDataScopeRelDto;
import com.feihua.framework.base.modules.rel.po.BaseUserGroupDataScopeRelPo;
import feihua.jdbc.api.service.impl.ApiBaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * This class was generated by MyBatis Generator.
 * @author yangwei 2019-06-20 13:28:16
 */
@Service
public class ApiBaseUserGroupDataScopeRelPoServiceImpl extends ApiBaseServiceImpl<BaseUserGroupDataScopeRelPo, BaseUserGroupDataScopeRelDto, String> implements ApiBaseUserGroupDataScopeRelPoService {
    @Autowired
    com.feihua.framework.base.mapper.BaseUserGroupDataScopeRelPoMapper BaseUserGroupDataScopeRelPoMapper;

    public ApiBaseUserGroupDataScopeRelPoServiceImpl() {
        super(BaseUserGroupDataScopeRelDto.class);
    }

    @Override
    public BaseUserGroupDataScopeRelDto wrapDto(BaseUserGroupDataScopeRelPo po) {
        if (po == null) { return null; }
        BaseUserGroupDataScopeRelDto dto = new BaseUserGroupDataScopeRelDto();
        dto.setId(po.getId());
        dto.setUserGroupId(po.getUserGroupId());
        dto.setDataScopeId(po.getDataScopeId());
        dto.setDataUserId(po.getDataUserId());
        dto.setDataOfficeId(po.getDataOfficeId());
        dto.setDataType(po.getDataType());
        dto.setDataAreaId(po.getDataAreaId());
        dto.setUpdateAt(po.getUpdateAt());
        return dto;
    }
}