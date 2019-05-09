package com.feihua.framework.message.api;

import com.feihua.framework.message.dto.BaseMessageThirdDto;
import com.feihua.framework.message.po.BaseMessageThirdPo;

import java.util.List;

/**
 * This class was generated by MyBatis Generator.
 * @author yangwei 2019-05-07 11:35:31
 */
public interface ApiBaseMessageThirdPoService extends feihua.jdbc.api.service.ApiBaseService<BaseMessageThirdPo, BaseMessageThirdDto, String> {

    List<BaseMessageThirdPo> selectByMessageIdAndThirdType(String messageId,String thirdType);

    List<BaseMessageThirdPo> selectByMessageId(String messageId);

    void copyByMessageId(String oldMessageId,String newMessageId,String currentUserId);
}