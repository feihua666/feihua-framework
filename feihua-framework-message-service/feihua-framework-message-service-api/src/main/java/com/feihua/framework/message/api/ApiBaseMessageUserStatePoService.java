package com.feihua.framework.message.api;

import com.feihua.framework.message.dto.BaseMessageUserStateDto;
import com.feihua.framework.message.po.BaseMessageUserStatePo;
import feihua.jdbc.api.pojo.PageAndOrderbyParamDto;
import feihua.jdbc.api.pojo.PageResultDto;

/**
 * This class was generated by MyBatis Generator.
 * @author yangwei 2018-10-29 20:28:33
 */
public interface ApiBaseMessageUserStatePoService extends feihua.jdbc.api.service.ApiBaseService<BaseMessageUserStatePo, BaseMessageUserStateDto, String> {


    /**
     * 查看消息已经情况
     * @param messageId
     * @param isRead
     * @param pageAndOrderbyParamDto
     * @return
     */
    PageResultDto<BaseMessageUserStateDto> viewReadPeople(String messageId, String isRead, PageAndOrderbyParamDto pageAndOrderbyParamDto);

    /**
     *
     * @param messageId
     * @param userId
     * @param isRead
     * @return
     */
    BaseMessageUserStatePo selectByMessageIdAndUserId(String messageId,String userId,String isRead);
}