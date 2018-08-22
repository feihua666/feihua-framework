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
    public void preInsert(PO po, PK userId);

    /**
     * 更新前请调用此方法以设置人员信息和更新时间
     * @param po
     * @param userId
     */
    public void preUpdate(PO po, PK userId);
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

}
