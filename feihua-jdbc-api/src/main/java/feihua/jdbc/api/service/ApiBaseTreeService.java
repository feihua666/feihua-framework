package feihua.jdbc.api.service;

import feihua.jdbc.api.pojo.BaseDto;
import feihua.jdbc.api.pojo.BasePo;

import java.util.List;

/**
 * tree service 接口类的基类，在写service接口时请先继承该类
 * Created by yangwei
 * Created at 2017/8/17 15:03
 */
public interface ApiBaseTreeService<PO extends BasePo,DTO extends BaseDto,PK> extends ApiBaseService<PO,DTO,PK>{

    /**
     * 获取下一级子节点
     * @param primaryKey
     * @return
     */
    public List<PO> getChildren(PK primaryKey);

    /**
     * 获取所有子节点
     * @param primaryKey
     * @return
     */
    public List<PO> getChildrenAll(PK primaryKey);

    /**
     * 获取下一级的叶子节点
     * @param primaryKey
     * @return
     */
    public List<PO> getChildrenLeaf(PK primaryKey);

    /**
     * 获取所有父节点
     * @param primaryKey
     * @return
     */
    public List<PO> getParents(PK primaryKey);
}
