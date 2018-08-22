package feihua.jdbc.api.service.impl;


import feihua.jdbc.api.pojo.*;
import feihua.jdbc.api.service.ApiBaseTreeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * tree service实现类基类
 * Created by yangwei
 * Created at 2017/8/17 14:49
 */
public abstract   class ApiBaseTreeServiceImpl<PO extends BaseTreePo,DTO extends BaseDto,PK> extends ApiBaseServiceImpl<PO ,DTO,PK> implements ApiBaseTreeService<PO ,DTO,PK> {

    public final static Logger logger = LoggerFactory.getLogger(ApiBaseTreeServiceImpl.class);

    public ApiBaseTreeServiceImpl(){}
    public ApiBaseTreeServiceImpl(Class<? extends BaseDto> aClass){
        super(aClass);
    }

    /**
     * 插入前请调用此方法以设置人员信息、level、parent_id[x]和插入时间
     * @param po
     * @param userId
     */
    @Override
    public void preInsert(PO po, PK userId) {
        super.preInsert(po,userId);
        if(po.getParentId() == null || "".equals(po.getParentId())){
            po.setParentId(BaseTreePo.defaultRootParentId);
        }
        //关于tree
        BaseTreePo parentEntity = null;
        //如果添加的是根节点
        if((BaseTreePo.defaultRootParentId).equals(po.getParentId())){
            BaseTreePo<PK> _root  = new BaseTreePo<PK>();
            _root.setId((PK) BaseTreePo.defaultRootParentId);
            _root.setLevel(BaseTreePo.defaultRootLevel-1);
            setParentIdsWithDefault(_root);
            parentEntity = _root;
        }else {
            //如果添加的不是根节点
            parentEntity = super.selectByPrimaryKeySimple((PK)po.getParentId(),false);
        }

        if(parentEntity == null){
            throw new IllegalArgumentException(po.getParentId()+" not exist");
        }
            //变更当前节点
            moveNode(po,parentEntity);
    }

    /**
     * 更新前请调用此方法以设置人员信息、level、parent_id[x]和插入时间
     * @param po
     * @param userId
     */
    @Override
    public void preUpdate(PO po, PK userId) {
        super.preUpdate(po,userId);
        if(po.getParentId() == null || "".equals(po.getParentId())){
            po.setParentId(BaseTreePo.defaultRootParentId);
        }
        BaseTreePo dbEntity = super.selectByPrimaryKeySimple((PK) po.getId(),false);
        //如果父节点有变动，则子节点也要变动，原来的子节点上移
        if(!dbEntity.getParentId().equals(po.getParentId())){
            BaseTreePo sourceParent = null;
            BaseTreePo targetParent = null;
            BaseTreePo _root  = new BaseTreePo();
            _root.setId(BaseTreePo.defaultRootParentId);
            _root.setLevel(BaseTreePo.defaultRootLevel-1);
            setParentIdsWithDefault(_root);
                //如果是根节点
                if((BaseTreePo.defaultRootParentId).equals(dbEntity.getParentId())){
                    sourceParent = _root;
                }else {
                    sourceParent = super.selectByPrimaryKeySimple((PK) dbEntity.getParentId(),false);
                }
                //原子节点上移
                moveChildrenNodes((PK) dbEntity.getId(),sourceParent);
                //如果是根节点
                if((BaseTreePo.defaultRootParentId).equals(po.getParentId())){
                    targetParent = _root;
                }else {
                    targetParent = super.selectByPrimaryKeySimple((PK) po.getParentId(),false);
                }
                //变更当前节点
                moveNode(po,targetParent);

        }
    }
    /**
     * 下移下级节点
     * @param sourceParentId 该节点下的子一级节点们
     * @param targetParent 移动到该节点下
     */
    public void moveChildrenNodes(PK sourceParentId,BaseTreePo targetParent) {
        for(PO child:getChildren(sourceParentId) ){
            BaseTreePo _child = child;
            moveNode(_child,targetParent);
            super.updateByPrimaryKey((PO) _child);
            moveChildrenNodes((PK) _child.getId(),_child);
        }
    }
    /**
     * 获取下一级子节点
     * @param primaryKey
     * @return
     */
    @Override
    public   List<PO> getChildren(PK primaryKey){
        Map<String,Object> condition = new HashMap<>();
        condition.put(BasePo.PROPERTY_DEL_FLAG,BasePo.YesNo.N.name());
        condition.put(BaseTreePo.PROPERTY_PARENT_ID,primaryKey);
        return super.selectSimple(condition);

    }

    @Override
    public List<PO> getChildrenAll(PK primaryKey) {
        BaseTreePo currentPo = super.selectByPrimaryKeySimple(primaryKey,false);
        if(currentPo != null){
            Map<String,Object> condition = new HashMap<>();
            condition.put(BasePo.PROPERTY_DEL_FLAG,BasePo.YesNo.N.name());
            condition.put(BaseTreePo.PROPERTY_PARENT_ID + currentPo.getLevel(),primaryKey);
            return super.selectSimple(condition);
        }
        return null;
    }

    @Override
    public List<PO> getChildrenLeaf(PK primaryKey) {
        BaseTreePo currentPo = super.selectByPrimaryKeySimple(primaryKey,false);
        if(currentPo != null){
            Map<String,Object> condition = new HashMap<>();
            condition.put(BasePo.PROPERTY_DEL_FLAG,BasePo.YesNo.N.name());
            condition.put(BaseTreePo.PROPERTY_PARENT_ID + currentPo.getLevel(),primaryKey);
            if(currentPo.getLevel() < BaseTreePo.maxLevel){
                condition.put(BaseTreePo.PROPERTY_PARENT_ID + currentPo.getLevel()+1,BaseTreePo.defaultParentIdX);
            }
            return super.selectSimple(condition);
        }
        return null;
    }

    @Override
    public List<PO> getParents(PK primaryKey) {
        BaseTreePo currentPo = super.selectByPrimaryKeySimple(primaryKey,false);
        if(currentPo != null){
            List<PK> primaryKeys = new ArrayList<>();
            primaryKeys.add((PK) currentPo.getParentId1());
            primaryKeys.add((PK) currentPo.getParentId2());
            primaryKeys.add((PK) currentPo.getParentId3());
            primaryKeys.add((PK) currentPo.getParentId4());
            primaryKeys.add((PK) currentPo.getParentId5());
            primaryKeys.add((PK) currentPo.getParentId6());
            primaryKeys.add((PK) currentPo.getParentId7());
            primaryKeys.add((PK) currentPo.getParentId8());
            primaryKeys.add((PK) currentPo.getParentId9());
            primaryKeys.add((PK) currentPo.getParentId10());

            return super.selectByPrimaryKeysSimple(primaryKeys,false);
        }
        return null;
    }

    /**
     * 移动一个节点作为目标节点下级
     * @param entity 要移动的节点
     * @param parent parent 前提是正确的，目标节点
     */
    public void moveNode(BaseTreePo entity,BaseTreePo parent) {
        if(parent.getLevel().intValue() == BaseTreePo.maxLevel){
            throw new RuntimeException("maxLevel is " + BaseTreePo.maxLevel);
        }
        if(entity.getId() != null && entity.getId().equals(parent.getId())){
            throw new RuntimeException("node id=" + parent.getId() + " can not  be moved as itself child");
        }
        setParentIds(entity,parent);

        if(parent.getLevel().intValue() == 1){
            entity.setParentId1(parent.getId());
        }else if(parent.getLevel().intValue() == 2){
            entity.setParentId2(parent.getId());
        }else if(parent.getLevel().intValue() == 3){
            entity.setParentId3(parent.getId());
        }else if(parent.getLevel().intValue() == 4){
            entity.setParentId4(parent.getId());
        }else if(parent.getLevel().intValue() == 5){
            entity.setParentId5(parent.getId());
        }else if(parent.getLevel().intValue() == 6){
            entity.setParentId6(parent.getId());
        }else if(parent.getLevel().intValue() == 7){
            entity.setParentId7(parent.getId());
        }else if(parent.getLevel().intValue() == 8){
            entity.setParentId8(parent.getId());
        }else if(parent.getLevel().intValue() == 9){
            entity.setParentId9(parent.getId());
        }else if(parent.getLevel().intValue() == 10){
            entity.setParentId10(parent.getId());
        }

        entity.setParentId(parent.getId());
        entity.setLevel(parent.getLevel() + 1);
    }
    private void setParentIds(BaseTreePo target,BaseTreePo source){
        target.setParentId1(source.getParentId1());
        target.setParentId2(source.getParentId2());
        target.setParentId3(source.getParentId3());
        target.setParentId4(source.getParentId4());
        target.setParentId5(source.getParentId5());
        target.setParentId6(source.getParentId6());
        target.setParentId7(source.getParentId7());
        target.setParentId8(source.getParentId8());
        target.setParentId9(source.getParentId9());
        target.setParentId10(source.getParentId10());

    }
    private void setParentIdsWithDefault(BaseTreePo target){
        target.setParentId1((PK) BaseTreePo.defaultParentIdX);
        target.setParentId2((PK) BaseTreePo.defaultParentIdX);
        target.setParentId3((PK) BaseTreePo.defaultParentIdX);
        target.setParentId4((PK) BaseTreePo.defaultParentIdX);
        target.setParentId5((PK) BaseTreePo.defaultParentIdX);
        target.setParentId6((PK) BaseTreePo.defaultParentIdX);
        target.setParentId7((PK) BaseTreePo.defaultParentIdX);
        target.setParentId8((PK) BaseTreePo.defaultParentIdX);
        target.setParentId9((PK) BaseTreePo.defaultParentIdX);
        target.setParentId10((PK) BaseTreePo.defaultParentIdX);
    }
}
