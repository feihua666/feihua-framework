package feihua.jdbc.api.service;

import feihua.jdbc.api.dao.SelectDao;
import feihua.jdbc.api.pojo.*;

import java.util.List;
import java.util.Map;

/**
 * service 接口类的基类，在写service接口时请先继承该类
 * Created by yangwei
 * Created at 2017/8/17 15:03
 */
public interface SelectBaseService<PO extends BasePo,DTO extends BaseDto,PK> {

    /**
     * feihua.jdbc.api.dao.SelectDao#selectByPrimaryKey(java.lang.Object)
     * @param id
     * @return
     */
    PO selectByPrimaryKeySimple(PK id);

    /**
     *
     * @param id
     * @param includeDeleted if true @see feihua.jdbc.api.service.SelectBaseService#selectByPrimaryKeySimple(java.lang.Object)
     * @return
     */
    PO selectByPrimaryKeySimple(PK id,boolean includeDeleted);
    /**
     * 封装dto
     * @param id
     * @return
     */
    DTO selectByPrimaryKey(PK id);

    /**
     * 封装dto
     * @param id
     * @param includeDeleted
     * @return
     */
    DTO selectByPrimaryKey(PK id,boolean includeDeleted);
    /**
     * @see feihua.jdbc.api.dao.SelectDao#selectOne(feihua.jdbc.api.pojo.BasePo)
     * @param entity
     * @return
     */
    PO selectOneSimple(PO entity);

    /**
     * 封装dto
     * @param entity
     * @return
     */
    DTO selectOne(PO entity);
    /**
     * @see SelectDao#selectList(feihua.jdbc.api.pojo.BasePo)
     * @param entity
     * @return
     */
    List<PO> selectListSimple(PO entity);
    List<PO> selectListSimple(PO entity, Orderby orderby);
    PageResultDto<PO> selectListSimple(PO entity, PageAndOrderbyParamDto pageAndOrderbyParamDto);



    /**
     * 封装dto
     * @param entity
     * @return
     */
    List<DTO> selectList(PO entity);
    List<DTO> selectList(PO entity, Orderby orderby);
    PageResultDto<DTO> selectList(PO entity, PageAndOrderbyParamDto pageAndOrderbyParamDto);

    /**
     * @see SelectDao#select(java.util.Map)
     * @param map
     * @return
     */
    List<PO> selectSimple(Map<String, Object> map);
    List<PO> selectSimple(Map<String, Object> map, Orderby orderby);
    PageResultDto<PO> selectSimple(Map<String, Object> map, PageAndOrderbyParamDto pageAndOrderbyParamDto);

    /**
     * 封装dto
     * @param map
     * @return
     */
    List<DTO> select(Map<String, Object> map);
    List<DTO> select(Map<String, Object> map, Orderby orderby);
    PageResultDto<DTO> select(Map<String, Object> map, PageAndOrderbyParamDto pageAndOrderbyParamDto);

    /**
     * @see SelectDao#selectAll(boolean)
     * @param includeDeleted
     * @return
     */
    List<PO> selectAllSimple(boolean includeDeleted);
    List<PO> selectAllSimple(boolean includeDeleted, Orderby orderby);
    PageResultDto<PO> selectAllSimple(boolean includeDeleted, PageAndOrderbyParamDto pageAndOrderbyParamDto);

    /**
     * 封装dto
     * @param includeDeleted
     * @return
     */
    List<DTO> selectAll(boolean includeDeleted);
    List<DTO> selectAll(boolean includeDeleted, Orderby orderby);
    PageResultDto<DTO> selectAll(boolean includeDeleted, PageAndOrderbyParamDto pageAndOrderbyParamDto);

    List<PO> selectByPrimaryKeysSimple(List<PK> primaryKeys,boolean includeDeleted);
    List<PO> selectByPrimaryKeysSimple(List<PK> primaryKeys,boolean includeDeleted, Orderby orderby);
    PageResultDto<PO> selectByPrimaryKeysSimple(List<PK> primaryKeys,boolean includeDeleted, PageAndOrderbyParamDto pageAndOrderbyParamDto);

    List<DTO> selectByPrimaryKeys(List<PK> primaryKeys,boolean includeDeleted);
    List<DTO> selectByPrimaryKeys(List<PK> primaryKeys,boolean includeDeleted, Orderby orderby);
    PageResultDto<DTO> selectByPrimaryKeys(List<PK> primaryKeys,boolean includeDeleted, PageAndOrderbyParamDto pageAndOrderbyParamDto);
}
