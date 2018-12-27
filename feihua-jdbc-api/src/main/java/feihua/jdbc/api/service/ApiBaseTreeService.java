package feihua.jdbc.api.service;

import feihua.jdbc.api.pojo.*;

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
    List<PO> getChildren(PK primaryKey, Orderby orderby);
    PageResultDto<PO> getChildren(PK primaryKey, PageAndOrderbyParamDto pageAndOrderbyParamDto);

    /**
     * 获取所有子节点
     * @param primaryKey
     * @return
     */
    public List<PO> getChildrenAll(PK primaryKey);
    List<PO> getChildrenAll(PK primaryKey, Orderby orderby);
    PageResultDto<PO> getChildrenAll(PK primaryKey, PageAndOrderbyParamDto pageAndOrderbyParamDto);

    /**
     * 获取下一级的叶子节点
     * @param primaryKey
     * @return
     */
    public List<PO> getChildrenLeaf(PK primaryKey);
    List<PO> getChildrenLeaf(PK primaryKey, Orderby orderby);
    PageResultDto<PO> getChildrenLeaf(PK primaryKey, PageAndOrderbyParamDto pageAndOrderbyParamDto);
    /**
     * 获取所有父节点
     * @param primaryKey
     * @return
     */
    public List<PO> getParents(PK primaryKey);
    List<PO> getParents(PK primaryKey, Orderby orderby);
    PageResultDto<PO> getParents(PK primaryKey, PageAndOrderbyParamDto pageAndOrderbyParamDto);
}
