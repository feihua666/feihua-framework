package com.feihua.framework.base.mapper;

import com.feihua.framework.base.modules.group.dto.SearchUserGroupsConditionDsfDto;
import com.feihua.framework.base.modules.group.po.BaseUserGroupPo;
import feihua.jdbc.api.dao.CrudDao;

/**
 * This class was generated by MyBatis Generator.
 * @author yangwei 2019-06-20 13:18:42
 */
public interface BaseUserGroupPoMapper extends feihua.jdbc.api.dao.CrudDao<BaseUserGroupPo, String> {
    java.util.List<BaseUserGroupPo> searchBaseUserGroups(SearchUserGroupsConditionDsfDto dto);
}