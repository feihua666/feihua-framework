package feihua.jdbc.api.dao;

import feihua.jdbc.api.pojo.BasePo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by feihua on 2015/6/29.
 * 增
 */
public interface InsertDao<PO extends BasePo,PK> extends BaseDao<PO,PK> {

    /**
     * 创建（增加）数据,自动生成id
     * @param entity 创建的数据内容
     * @return 1：增加数据成功
     *         0：增加数据失败
     */
    public int insert(PO entity);

    /**
     * 创建（增加）数据，请指定id
     * @param entity 创建的数据内容
     * @return 1：增加数据成功
     *         0：增加数据失败
     */
    public int insertWithPrimaryKey(PO entity);
    /**
     * 创建（增加）数据,自动生成id
     * @param entity 创建的数据内容
     * @return 1：增加数据成功
     *         0：增加数据失败
     */
    public int insertSelective(PO entity);

    /**
     * 创建（增加）数据,请指定id
     * @param entity 创建的数据内容
     * @return 1：增加数据成功
     *         0：增加数据失败
     */
    public int insertSelectiveWithPrimaryKey(PO entity);

    /**
     * 批量插入，自动生成id
     * @param entities
     * @return
     */
    public int insertBatch(@Param("entities") List<PO> entities);

    /**
     * 批量插入，请指定id
     * @param entities
     * @return
     */
    public int insertBatchWithPrimaryKey(@Param("entities") List<PO> entities);

}
