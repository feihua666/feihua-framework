package com.feihua.framework.base.mapper;

import com.feihua.framework.base.modules.loginclient.po.BaseLoginClientPo;
import feihua.jdbc.api.dao.CrudDao;

/**
 * This class was generated by MyBatis Generator.
 * @author yangwei 2019-04-04 11:42:32
 */
public interface BaseLoginClientPoMapper extends feihua.jdbc.api.dao.CrudDao<BaseLoginClientPo, String> {
    java.util.List<BaseLoginClientPo> searchBaseLoginClients(com.feihua.framework.base.modules.loginclient.dto.SearchBaseLoginClientsConditionDto dto);
}