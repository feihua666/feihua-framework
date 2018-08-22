package feihua.jdbc.api.service;

import feihua.jdbc.api.dao.DeleteDao;
import feihua.jdbc.api.pojo.BaseDto;
import feihua.jdbc.api.pojo.BasePo;

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
