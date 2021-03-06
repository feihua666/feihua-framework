package com.feihua.framework.message.impl;

import com.feihua.framework.message.api.ApiBaseMessageSendPoService;
import com.feihua.framework.message.dto.BaseMessageSendDto;
import com.feihua.framework.message.po.BaseMessageSendPo;
import feihua.jdbc.api.service.impl.ApiBaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * This class was generated by MyBatis Generator.
 * @author yangwei 2019-05-07 14:28:52
 */
@Service
public class ApiBaseMessageSendPoServiceImpl extends ApiBaseServiceImpl<BaseMessageSendPo, BaseMessageSendDto, String> implements ApiBaseMessageSendPoService {
    @Autowired
    com.feihua.framework.message.mapper.BaseMessageSendPoMapper BaseMessageSendPoMapper;

    public ApiBaseMessageSendPoServiceImpl() {
        super(BaseMessageSendDto.class);
    }

    @Override
    public BaseMessageSendDto wrapDto(BaseMessageSendPo po) {
        if (po == null) { return null; }
        BaseMessageSendDto dto = new BaseMessageSendDto();
        dto.setId(po.getId());
        dto.setMessageId(po.getMessageId());
        dto.setSendUserId(po.getSendUserId());
        dto.setSendUserNickname(po.getSendUserNickname());
        dto.setDataUserId(po.getDataUserId());
        dto.setDataOfficeId(po.getDataOfficeId());
        dto.setDataType(po.getDataType());
        dto.setDataAreaId(po.getDataAreaId());
        dto.setUpdateAt(po.getUpdateAt());
        return dto;
    }
}