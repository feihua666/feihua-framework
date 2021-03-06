package com.feihua.framework.base.modules.postjob.api;

import com.feihua.framework.base.modules.postjob.dto.BasePostDataScopeDefineDto;
import com.feihua.framework.base.modules.postjob.dto.BasePostDataScopeDefineParamDto;
import com.feihua.framework.base.modules.postjob.dto.BasePostDataScopeDefineDto;
import com.feihua.framework.base.modules.postjob.po.BasePostDataScopeDefinePo;

/**
 * This class was generated by MyBatis Generator.
 * @author yangwei 2019-06-14 11:32:30
 */
public interface ApiBasePostDataScopeDefinePoService extends feihua.jdbc.api.service.ApiBaseService<BasePostDataScopeDefinePo, BasePostDataScopeDefineDto, String> {
    /**
     * 根据数据范围id查询，只有一条数据
     * @param dataScopeId
     * @return
     */
    public BasePostDataScopeDefineDto selectByDataScopeId(String dataScopeId);

    /**
     * 设置数据范围的字典范围
     * @param postDataScopeParamDto
     * @return
     */
    public BasePostDataScopeDefineDto setPostDataScopeDefine(BasePostDataScopeDefineParamDto postDataScopeParamDto);

}