package com.feihua.framework.base.modules.message.api;

import com.feihua.framework.base.modules.message.dto.BaseMessageDto;
import com.feihua.framework.base.modules.message.po.BaseMessagePo;
import feihua.jdbc.api.pojo.PageResultDto;

/**
 * This class was generated by MyBatis Generator.
 * @author yangwei 2018-10-29 20:14:49
 */
public interface ApiBaseMessagePoService extends feihua.jdbc.api.service.ApiBaseService<BaseMessagePo, BaseMessageDto, String> {
    PageResultDto<BaseMessageDto> searchBaseMessagesDsf(com.feihua.framework.base.modules.message.dto.SearchBaseMessagesConditionDto dto, feihua.jdbc.api.pojo.PageAndOrderbyParamDto pageAndOrderbyParamDto);
}