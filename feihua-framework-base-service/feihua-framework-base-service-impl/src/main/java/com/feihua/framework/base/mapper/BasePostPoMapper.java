package com.feihua.framework.base.mapper;

import com.feihua.framework.base.modules.postjob.dto.SearchPostsConditionDsfDto;
import com.feihua.framework.base.modules.postjob.po.BasePostPo;
import feihua.jdbc.api.dao.CrudDao;

/**
 * This class was generated by MyBatis Generator.
 * @author yangwei 2019-06-06 11:05:12
 */
public interface BasePostPoMapper extends feihua.jdbc.api.dao.CrudDao<BasePostPo, String> {

    java.util.List<BasePostPo> searchBasePosts(SearchPostsConditionDsfDto dto);
}