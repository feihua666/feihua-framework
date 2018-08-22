package feihua.jdbc.api.dao;

import feihua.jdbc.api.pojo.BasePo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Created by feihua on 2015/6/29.
 * 改
 */
public interface UpdateDao<PO extends BasePo,PK> extends BaseDao<PO,PK> {

    /**
     * 更新数据，全部更新
     * @param entity 更新的内容
     * @return 1：更新数据成功
     *         0：更新数据失败
     */
    public int updateByPrimaryKey(PO entity);

    /**
     * 更新数据,有值属性(不为null)更新
     * @param entity 更新的内容
     * @return 1：更新数据成功
     *         0：更新数据失败
     */
    public int updateByPrimaryKeySelective(PO entity);

    /**
     * 更新数据
     * @param entity 要更新的实体
     * @param condition 条件
     * @return
     */
    public int update(@Param("entity")PO entity,@Param("condition") PO condition);

    /**
     * 更新数据,有值属性(不为null)更新
     * @param entity
     * @param condition
     * @return
     */
    public int updateSelective(@Param("entity")PO entity,@Param("condition") PO condition);

    /**
     * 批量更新，根据各自id更新
     * @param entities
     * @return
     */
    public int updateBatchByPrimaryKey(@Param("entities") List<PO> entities);

    /**
     * 批量更新，将所有主键匹配更新为实体内容
     * @param primaryKeys
     * @param entity 更新的内容
     * @return
     */
    public int updateBatchByPrimaryKeys(@Param("primaryKeys") List<PK> primaryKeys,@Param("entity") PO entity);

    /**
     * 批量更新，将所有主键匹配更新为实体内容不为空的值
     * @param primaryKeys
     * @param entity
     * @return
     */
    public int updateBatchByPrimaryKeysSelective(@Param("primaryKeys") List<PK> primaryKeys,@Param("entity") PO entity);
}
