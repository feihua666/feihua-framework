package feihua.jdbc.api.service;

import feihua.jdbc.api.pojo.*;

import java.io.Serializable;
import java.util.List;

/**
 * service 接口类的基类，在写service接口时请先继承该类
 * Created by yangwei
 * Created at 2017/8/17 15:03
 */
public interface ApiBaseService<PO extends BasePo,DTO extends BaseDto,PK> extends InsertBaseService<PO,DTO>,DeleteBaseService<PO,PK>,UpdateBaseService<PO,PK>,SelectBaseService<PO,DTO,PK>,CountBaseService<PO> {


    /**
     * 插入前请调用此方法以设置人员信息和插入时间
     * @param po
     * @param userId
     */
    public PO preInsert(PO po, PK userId);

    /**
     * 更新前请调用此方法以设置人员信息和更新时间
     * @param po
     * @param userId
     */
    public PO preUpdate(PO po, PK userId);

    /**
     * 根据id智能判断是preInsert还是preUpdate
     * @param po
     * @param userId
     * @return
     */
    public PO preSave(PO po, PK userId);

    /**
     * 将 po 转为 dto
     *
     * @param po
     * @return
     */
    public DTO wrapDto(PO po);
    /**
     * 将 pos 转为 dtos
     *
     * @param pos
     * @return
     */
    public  List<DTO> wrapDtos(List<PO> pos);

    public List<PK> toPrimaryKeysSimple(List<PO> pos);

    public List<PK> toPrimaryKeys(List<DTO> pos);

    /**
     * 批量保存，po的id存在是更新的，不存在是添加的，id存在，但没有在db中的是要删除的
     * @param savePos 要保存的
     * @param dbPos 数据库中已经存在的
     * @return 成功的数据条数,返回0不代表没有成功，删除的数据条数没有计算在内
     */
    public int batchSave(List<PO> savePos,List<PO> dbPos,PK currentUserId);

    /**
     * 批量保存，批量保存，po的id存在是更新的，不存在是添加的，id存在，但没有在db中的是要删除的
     * @param savePos 要保存的
     * @param selectCondition 查询数据库是已存在的查询条件
     * @return 成功的数据条数,返回0不代表没有成功，删除的数据条数没有计算在内
     */
    public int batchSave(List<PO> savePos,PO selectCondition,PK currentUserId);
}
