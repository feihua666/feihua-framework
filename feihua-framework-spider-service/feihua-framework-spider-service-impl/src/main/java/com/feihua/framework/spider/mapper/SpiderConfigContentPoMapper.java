package com.feihua.framework.spider.mapper;

import com.feihua.framework.spider.po.SpiderConfigContentPo;

/**
 * This class was generated by MyBatis Generator.
 * @author yangwei 2018-12-19 17:17:58
 */
public interface SpiderConfigContentPoMapper extends feihua.jdbc.api.dao.CrudDao<SpiderConfigContentPo, String> {
    java.util.List<SpiderConfigContentPo> searchSpiderConfigContents(com.feihua.framework.spider.dto.SearchSpiderConfigContentsConditionDto dto);
}