package com.feihua.framework.base.mapper;

import com.feihua.framework.base.modules.dict.dto.SearchDictsConditionDto;
import com.feihua.framework.base.modules.dict.po.BaseDictPo;

import java.util.List;

/**
 * This class was generated by MyBatis Generator.
 * @author yangwei 2018-03-26 12:04:43
 */
public interface BaseDictPoMapper extends feihua.jdbc.api.dao.CrudDao<BaseDictPo, String> {

    public List<BaseDictPo> searchDicts(SearchDictsConditionDto conditionDto);
}