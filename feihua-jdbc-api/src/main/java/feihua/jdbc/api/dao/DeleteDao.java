package feihua.jdbc.api.dao;

import feihua.jdbc.api.pojo.BasePo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by feihua on 2015/6/29.
 * 删
 */
public interface DeleteDao<PO extends BasePo,PK> extends BaseDao<PO,PK> {

    /**
     * 删除数据，数据物理删除
     * @param id 删除的数据id
     * @return 1：删除数据成功
     *         0：删除数据失败
     */
    public int deleteByPrimaryKey(PK id);
    /**
     * 删除数据，数据物理删除
     * @param primaryKeys 删除的数据id
     * @return >=1：删除数据成功
     *         0：删除数据失败
     */
    int deleteByPrimaryKeys(List<PK> primaryKeys);

    /**
     * 删除数据，一般为数据逻辑删除,将删除标记置为已删除
     * @param id 删除的数据id
     * @return 1：删除数据成功
     *         0：删除数据失败
     */
    public int deleteFlagByPrimaryKey(PK id);
    /**
     * 删除数据，一般为数据逻辑删除,将删除标记置为已删除
     * @param primaryKeys 删除的数据id
     * @return >=1：删除数据成功
     *         0：删除数据失败
     */
    public int deleteFlagByPrimaryKeys(List<PK> primaryKeys);

    /**
     * 逻辑删除数据，实际是更新删除标记，并更新更新人字段
     * @param primaryKeys
     * @param userId 更新人id
     * @return
     */

    public int deleteFlagByPrimaryKeysWithUpdateUser(@Param("primaryKeys") List<PK> primaryKeys, @Param("userId") PK userId);
    /**
     * 删除数据，数据物理删除
     * @param entity 删除筛选条件
     * @return
     */
    public int deleteSelective(PO entity);

    /**
     * 删除数据，一般为数据逻辑删除，将删除标记置为已删除
     * @param entity 删除筛选条件
     * @return
     */
    public int deleteFlagSelective(PO entity);
    /**
     * 删除数据，一般为数据逻辑删除，将删除标记置为已删除,并设置更新人字段
     * @param entity 删除筛选条件
     * @return
     */
    public int deleteFlagSelectiveWithUpdateUser(@Param("entity") PO entity,@Param("userId")PK userId);

}
