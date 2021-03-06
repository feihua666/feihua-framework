package com.feihua.framework.base.modules.postjob.api;

import com.feihua.framework.base.modules.postjob.dto.BasePostJobDto;
import com.feihua.framework.base.modules.postjob.po.BasePostJobPo;
import feihua.jdbc.api.pojo.PageResultDto;

/**
 * This class was generated by MyBatis Generator.
 * @author yangwei 2019-06-06 14:16:20
 */
public interface ApiBasePostJobPoService extends feihua.jdbc.api.service.ApiBaseService<BasePostJobPo, BasePostJobDto, String> {
    PageResultDto<BasePostJobDto> searchBasePostJobsDsf(com.feihua.framework.base.modules.postjob.dto.SearchBasePostJobsConditionDto dto, feihua.jdbc.api.pojo.PageAndOrderbyParamDto pageAndOrderbyParamDto);
}