package feihua.jdbc.api.dao;

import feihua.jdbc.api.pojo.BasePo;

import java.util.Map;

/**
 * Created by feihua on 2015/6/29.
 * 查
 */
public interface CountDao<PO extends BasePo,PK> extends BaseDao<PO,PK> {
    /**
     * 计数
     **/
    public int count(PO entity);

    /**
     * 计数
     * @param map
     * @return
     */
    public int counts(Map<String,Object> map);
}
