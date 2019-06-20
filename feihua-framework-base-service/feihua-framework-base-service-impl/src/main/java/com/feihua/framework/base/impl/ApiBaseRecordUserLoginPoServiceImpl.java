package com.feihua.framework.base.impl;

import com.feihua.framework.base.modules.user.api.ApiBaseRecordUserLoginPoService;
import com.feihua.framework.base.modules.user.dto.BaseRecordUserLoginDto;
import com.feihua.framework.base.modules.user.po.BaseRecordUserLoginPo;
import feihua.jdbc.api.service.impl.ApiBaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * This class was generated by MyBatis Generator.
 * @author yangwei 2019-04-15 15:39:05
 */
@Service
public class ApiBaseRecordUserLoginPoServiceImpl extends ApiBaseServiceImpl<BaseRecordUserLoginPo, BaseRecordUserLoginDto, String> implements ApiBaseRecordUserLoginPoService {
    @Autowired
    com.feihua.framework.base.mapper.BaseRecordUserLoginPoMapper BaseRecordUserLoginPoMapper;

    public ApiBaseRecordUserLoginPoServiceImpl() {
        super(BaseRecordUserLoginDto.class);
    }

    @Override
    public BaseRecordUserLoginDto wrapDto(BaseRecordUserLoginPo po) {
        if (po == null) { return null; }
        BaseRecordUserLoginDto dto = new BaseRecordUserLoginDto();
        dto.setId(po.getId());
        dto.setUserId(po.getUserId());
        dto.setUserNickname(po.getUserNickname());
        dto.setClientCode(po.getClientCode());
        dto.setLoginTime(po.getLoginTime());
        dto.setLoginIp(po.getLoginIp());
        dto.setDeviceId(po.getDeviceId());
        dto.setDevicetype(po.getDevicetype());
        dto.setOsversion(po.getOsversion());
        dto.setUserAgent(po.getUserAgent());
        dto.setAppversion(po.getAppversion());
        dto.setScreenscale(po.getScreenscale());
        dto.setDataUserId(po.getDataUserId());
        dto.setDataOfficeId(po.getDataOfficeId());
        dto.setDataType(po.getDataType());
        dto.setDataAreaId(po.getDataAreaId());
        dto.setUpdateAt(po.getUpdateAt());
        dto.setScreenwidth(po.getScreenwidth());
        dto.setScreenheight(po.getScreenheight());
        return dto;
    }
}