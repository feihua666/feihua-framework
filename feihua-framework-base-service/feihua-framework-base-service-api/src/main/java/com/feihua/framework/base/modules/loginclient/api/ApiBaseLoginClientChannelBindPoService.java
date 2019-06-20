package com.feihua.framework.base.modules.loginclient.api;

import com.feihua.framework.base.modules.loginclient.dto.BaseLoginClientChannelBindDto;
import com.feihua.framework.base.modules.loginclient.po.BaseLoginClientChannelBindPo;

import java.util.List;

/**
 * This class was generated by MyBatis Generator.
 * @author yangwei 2019-04-04 17:36:52
 */
public interface ApiBaseLoginClientChannelBindPoService extends feihua.jdbc.api.service.ApiBaseService<BaseLoginClientChannelBindPo, BaseLoginClientChannelBindDto, String> {

    /**
     * 查询第三方渠道绑定
     * @param channelType 必填 渠道类型 目前可用的有com.feihua.framework.constants.DictEnum.WxAccountType
     * @param clientId 必填 客户端id
     * @return
     */
    BaseLoginClientChannelBindPo selectByChannelTypeAndClientId(String channelType,String clientId);

    /**
     * 根据客户端id查询
     * @param clientId
     * @return
     */
    List<BaseLoginClientChannelBindPo> selectByClientId(String clientId);
}