package feihua.jdbc.api.service;

import feihua.jdbc.api.dao.DeleteDao;
import feihua.jdbc.api.pojo.BaseDto;
import feihua.jdbc.api.pojo.BasePo;

import java.util.List;

/**
 * deleteservice
 * Created by yangwei
 * Created at 2017/8/17 15:03
 */
public interface DeleteBaseService<PO extends BasePo,PK> {

    /**
     * @see DeleteDao#deleteByPrimaryKey(java.lang.Object)
     * @param id
     * @return
     */
    int deleteByPrimaryKey(PK id);

    int deleteByPrimaryKeys(List<PK> ids);

    /**
     * @see feihua.jdbc.api.service.DeleteBaseService#deleteFlagByPrimaryKey(java.lang.Object) 同时将记录更新人和更新时间
     * @param id
     * @param userId
     * @return
     */
    int deleteFlagByPrimaryKeyWithUpdateUser(PK id, PK userId);

    /**
     * @see DeleteDao#deleteFlagByPrimaryKey(java.lang.Object)
     * @param id
     * @return
     */
    int deleteFlagByPrimaryKey(PK id);

    int deleteFlagByPrimaryKeys(List<PK> ids);

    int deleteFlagByPrimaryKeysWithUpdateUser(List<PK> ids, PK userId);

    int deleteFlagBatchByPrimaryKeysWithUpdateUser(List<PO> pos, PK userId);
    /**
     * @see DeleteDao#deleteSelective(feihua.jdbc.api.pojo.BasePo)
     * @param entity
     * @return
     */
    int deleteSelective(PO entity);

    /**
     * @see DeleteDao#deleteFlagSelective(feihua.jdbc.api.pojo.BasePo)
     * @param entity
     * @return
     */
    int deleteFlagSelective(PO entity);

    /**
     *
     * @param entity
     * @param userId
     * @return
     */
    int deleteFlagSelectiveWithUpdateUser(PO entity, PK userId);
}
