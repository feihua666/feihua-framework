package feihua.jdbc.api.service;

import feihua.jdbc.api.dao.UpdateDao;
import feihua.jdbc.api.pojo.BaseDto;
import feihua.jdbc.api.pojo.BasePo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * updateservice 接口类的基类
 * Created by yangwei
 * Created at 2017/8/17 15:03
 */
public interface UpdateBaseService<PO extends BasePo,PK> {
    /**
     * @see UpdateDao#updateByPrimaryKeySelective(feihua.jdbc.api.pojo.BasePo)
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(PO record);

    /**
     * @see UpdateDao#updateByPrimaryKey(feihua.jdbc.api.pojo.BasePo)
     * @param record
     * @return
     */
    int updateByPrimaryKey(PO record);

    /**
     * @see UpdateDao#update(feihua.jdbc.api.pojo.BasePo, feihua.jdbc.api.pojo.BasePo)
     * @param entity
     * @param condition
     * @return
     */
    public int update(PO entity,PO condition);

    /**
     * @see UpdateDao#updateSelective(feihua.jdbc.api.pojo.BasePo, feihua.jdbc.api.pojo.BasePo)
     * @param entity
     * @param condition
     * @return
     */
    public int updateSelective(PO entity,PO condition);

    /**
     * @see UpdateDao#updateBatchByPrimaryKey(java.util.List)
     * @param entities
     * @return
     */
    public int updateBatchByPrimaryKey(List<PO> entities);

    /**
     * @see feihua.jdbc.api.dao.UpdateDao#updateBatchByPrimaryKeySelective(java.util.List)
     * @param entities
     * @return
     */
    public int updateBatchByPrimaryKeySelective(List<PO> entities);

    /**
     * @see UpdateDao#updateBatchByPrimaryKeys(java.util.List, feihua.jdbc.api.pojo.BasePo)
     * @param primaryKeys
     * @param entity
     * @return
     */
    public int updateBatchByPrimaryKeys(List<PK> primaryKeys,PO entity);

    /**
     * @see UpdateDao#updateBatchByPrimaryKeysSelective(java.util.List, feihua.jdbc.api.pojo.BasePo)
     * @param primaryKeys
     * @param entity
     * @return
     */
    public int updateBatchByPrimaryKeysSelective( List<PK> primaryKeys, PO entity);

}
