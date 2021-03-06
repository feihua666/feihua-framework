package com.feihua.framework.base.modules.datascope.api;

import com.feihua.framework.base.modules.datascope.dto.BaseDataScopeDto;
import com.feihua.framework.base.modules.datascope.dto.SearchDataScopesConditionDto;
import com.feihua.framework.base.modules.datascope.po.BaseDataScopePo;
import feihua.jdbc.api.pojo.PageAndOrderbyParamDto;
import feihua.jdbc.api.pojo.PageResultDto;

/**
 * This class was generated by MyBatis Generator.
 * @author yangwei 2018-01-09 13:35:54
 */
public interface ApiBaseDataScopePoService extends feihua.jdbc.api.service.ApiBaseService<BaseDataScopePo, BaseDataScopeDto, String> {
    /**
     * 模糊搜索数据范围信息
     * @param conditionDto
     * @param pageAndOrderbyParamDto
     * @return
     */
    PageResultDto<BaseDataScopeDto> searchDataScopesDsf(SearchDataScopesConditionDto conditionDto, PageAndOrderbyParamDto pageAndOrderbyParamDto);
}