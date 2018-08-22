package feihua.jdbc.api.dao;

import feihua.jdbc.api.pojo.BasePo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Created by feihua on 2015/6/29.
 * 查
 */
public interface SelectDao<PO extends BasePo,PK> extends BaseDao<PO,PK> {

    /**
     * 根据id查询（获取）
     * @param id 查询的id
     * @return
     */
    public PO selectByPrimaryKey(PK id);

    /**
     * 根据id查询,不包括已删除数据
     * @param id* @return
     */
    public PO selectByPrimaryKeyWithoutDeleted(PK id);

    /**
     * 条件查询（获取）,单条数据
     * @param entity 查询条件
     * @return
     */
    public PO selectOne(PO entity);

    /**
     * 条件查询（获取），可以分页，至于分页和orderby可以通过其它方式传递，如threadlocal
     * @param entity 查询条件
     * @return
     */
    public List<PO> selectList(PO entity);

    /**
     * 查询（获取），可以分页，至于分页和orderby可以通过其它方式传递，如threadlocal
     * 一般用于多表联查
     * @param map 查询条件
     * @return
     */
    public List<PO> select(Map<String, Object> map);

    /**
     * 查询（获取）所有数据
     * @param includeDeleted 是否包括已标记为删除的数据
     * @return
     */
    public List<PO> selectAll(boolean includeDeleted);

    /**
     * 根据主键查询
     * @param primaryKeys
     * @return
     */
    public List<PO> selectByPrimaryKeys(@Param("primaryKeys") List<PK> primaryKeys,@Param("includeDeleted") boolean includeDeleted);

}
