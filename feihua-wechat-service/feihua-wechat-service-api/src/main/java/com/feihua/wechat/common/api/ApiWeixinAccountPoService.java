package com.feihua.wechat.common.api;

import com.feihua.wechat.common.dto.SearchWeixinAccountsConditionDto;
import com.feihua.wechat.common.dto.WeixinAccountDto;
import com.feihua.wechat.common.po.WeixinAccountPo;
import feihua.jdbc.api.pojo.PageResultDto;

/**
 * This class was generated by MyBatis Generator.
 * @author revolver 2018-10-19 16:31:29
 */
public interface ApiWeixinAccountPoService extends feihua.jdbc.api.service.ApiBaseService<WeixinAccountPo, WeixinAccountDto, String> {
    PageResultDto<WeixinAccountDto> searchWeixinAccountsDsf(SearchWeixinAccountsConditionDto dto, feihua.jdbc.api.pojo.PageAndOrderbyParamDto pageAndOrderbyParamDto);
}