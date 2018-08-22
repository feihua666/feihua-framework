package feihua.jdbc.api.service.impl;


import com.github.pagehelper.PageHelper;
import feihua.jdbc.api.dao.CrudDao;
import feihua.jdbc.api.pojo.*;
import feihua.jdbc.api.service.ApiBaseService;
import feihua.jdbc.api.utils.OrderbyUtils;
import feihua.jdbc.api.utils.PageUtils;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.DateConverter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.orderbyhelper.OrderByHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * service实现类基类
 * Created by yangwei
 * Created at 2017/8/17 14:49
 */
public abstract class ApiBaseServiceImpl<PO extends BasePo,DTO extends BaseDto,PK>implements ApiBaseService<PO,DTO,PK> {

    public final static Logger logger = LoggerFactory.getLogger(ApiBaseServiceImpl.class);


    private Class aClass;
    public ApiBaseServiceImpl(){};
    public ApiBaseServiceImpl(Class<? extends BaseDto> aClass){
        this.aClass = aClass;
    }

    public CrudDao<PO, PK> getCrudDao() {
        return crudDao;
    }

    @Autowired
    private CrudDao<PO,PK> crudDao;
    @Transactional(rollbackFor = Exception.class)
    @Override
    public PO insertSimple(PO record) {
        int r = crudDao.insert(record);
        if(r == 1){
            logger.info("insertSimple data:{}", ToStringBuilder.reflectionToString(record, ToStringStyle.SHORT_PREFIX_STYLE));
            return record;
        }
        return null;
    }
    @Transactional(rollbackFor = Exception.class)
    @Override
    public DTO insert(PO record) {
        int r = crudDao.insert(record);
        if(r == 1) {
            logger.info("insert data:{}", ToStringBuilder.reflectionToString(record, ToStringStyle.SHORT_PREFIX_STYLE));
            return wrapDto(record);
        }

        return null;
    }
    @Transactional(rollbackFor = Exception.class)
    @Override
    public PO insertSelectiveSimple(PO record) {
        int r = crudDao.insertSelective(record);
        if(r == 1) {
            logger.info("insertSelectiveSimple data:{}", ToStringBuilder.reflectionToString(record, ToStringStyle.SHORT_PREFIX_STYLE));
            return record;
        }
        return null;
    }
    @Transactional(rollbackFor = Exception.class)
    @Override
    public DTO insertSelective(PO record) {
        int r = crudDao.insertSelective(record);
        if(r == 1) {
            logger.info("insertSelective data:{}", ToStringBuilder.reflectionToString(record, ToStringStyle.SHORT_PREFIX_STYLE));
            return wrapDto(record);
        }
        return null;
    }
    @Transactional(rollbackFor = Exception.class)
    @Override
    public PO insertWithPrimaryKeySimple(PO entity) {
        int r = crudDao.insertWithPrimaryKey(entity);
        if(r == 1) {
            logger.info("insertWithPrimaryKeySimple data:{}", ToStringBuilder.reflectionToString(entity, ToStringStyle.SHORT_PREFIX_STYLE));
            return entity;
        }
        return null;
    }
    @Transactional(rollbackFor = Exception.class)
    @Override
    public DTO insertWithPrimaryKey(PO entity) {
        int r = crudDao.insertWithPrimaryKey(entity);
        if(r == 1) {
            logger.info("insertWithPrimaryKey data:{}", ToStringBuilder.reflectionToString(entity, ToStringStyle.SHORT_PREFIX_STYLE));
            return wrapDto(entity);
        }
        return null;
    }
    @Transactional(rollbackFor = Exception.class)
    @Override
    public PO insertSelectiveWithPrimaryKeySimple(PO entity) {
        int r = crudDao.insertSelectiveWithPrimaryKey(entity);
        if(r == 1) {
            logger.info("insertSelectiveWithPrimaryKeySimple data:{}", ToStringBuilder.reflectionToString(entity, ToStringStyle.SHORT_PREFIX_STYLE));
            return entity;
        }
        return null;
    }
    @Transactional(rollbackFor = Exception.class)
    @Override
    public DTO insertSelectiveWithPrimaryKey(PO entity) {
        int r = crudDao.insertSelectiveWithPrimaryKey(entity);
        if(r == 1) {
            logger.info("insertSelectiveWithPrimaryKeySimple data:{}", ToStringBuilder.reflectionToString(entity, ToStringStyle.SHORT_PREFIX_STYLE));
            return wrapDto(entity);
        }
        return null;
    }
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int insertBatch(List<PO> entities) {
        int r = crudDao.insertBatch(entities);
        if(r > 0) {
            logger.info("insertBatch data:{}", ToStringBuilder.reflectionToString(entities, ToStringStyle.SHORT_PREFIX_STYLE));
        }
        return r;
    }
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int insertBatchWithPrimaryKey(List<PO> entities) {
        int r = crudDao.insertBatchWithPrimaryKey(entities);
        if(r > 0) {
            logger.info("insertBatchWithPrimaryKey data:{}", ToStringBuilder.reflectionToString(entities, ToStringStyle.SHORT_PREFIX_STYLE));
        }
        return r;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int deleteByPrimaryKey(PK id) {
        int r = crudDao.deleteByPrimaryKey(id);
        if(r == 1){
            logger.info("deleteByPrimaryKey data:{}", id);
        }

        return r;
    }
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int deleteFlagByPrimaryKeyWithUpdateUser(PK id, PK userId) {
        PO po = crudDao.selectByPrimaryKey(id);
        int r = 0;

        if(po != null){
            if(BasePo.YesNo.Y.name().equals(po.getDelFlag())){
                return r;
            }
            preUpdate(po,userId);
            po.setDelFlag(BasePo.YesNo.Y.name());
            r = crudDao.updateByPrimaryKey(po);
            if(r == 1){
                logger.info("deleteFlagByPrimaryKeyWithUpdateUser data:{}", id);
            }
        }

        return r;
    }
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int deleteFlagByPrimaryKey(PK id) {
        PO po = crudDao.selectByPrimaryKey(id);
        int r = 0;
        if(po != null){
            po.setDelFlag(BasePo.YesNo.Y.name());
            r = crudDao.updateByPrimaryKey(po);
            if(r == 1){
                logger.info("deleteFlagByPrimaryKey data:{}", id);
            }
        }

        return r;
    }
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int deleteSelective(PO record) {
        int r = crudDao.deleteSelective(record);
        if(r > 0){
            logger.info("deleteSelective data:{}", ToStringBuilder.reflectionToString(record, ToStringStyle.SHORT_PREFIX_STYLE));
        }
        return r;
    }
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int deleteFlagSelective(PO record) {
        int r = crudDao.deleteFlagSelective(record);
        if(r > 0){
            logger.info("deleteFlagSelective data:{}", ToStringBuilder.reflectionToString(record, ToStringStyle.SHORT_PREFIX_STYLE));
        }
        return r;
    }
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int deleteFlagSelectiveWithUpdateUser(PO record, PK userId) {
        int r = 0;
        List<PO> dbList = selectListSimple(record);
        if(dbList != null && !dbList.isEmpty()){
            for (PO po : dbList) {
                preUpdate(po,userId);
                po.setDelFlag(BasePo.YesNo.Y.name());
            }
            r = updateBatchByPrimaryKey(dbList);
            if(r > 0){
                logger.info("deleteFlagSelective data:{}", ToStringBuilder.reflectionToString(dbList, ToStringStyle.SHORT_PREFIX_STYLE));
            }
        }
        return r;
    }
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int updateByPrimaryKeySelective(PO record) {
        int r = crudDao.updateByPrimaryKeySelective(record);
        if(r == 1){
            logger.info("updateByPrimaryKeySelective data:{}", ToStringBuilder.reflectionToString(record, ToStringStyle.SHORT_PREFIX_STYLE));
        }
        return r;
    }
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int updateByPrimaryKey(PO record) {
        int r = crudDao.updateByPrimaryKey(record);
        if(r == 1){
            logger.info("updateByPrimaryKey data:{}", ToStringBuilder.reflectionToString(record, ToStringStyle.SHORT_PREFIX_STYLE));
        }
        return r;
    }
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int update(PO entity, PO condition) {
        int r = crudDao.update(entity,condition);
        if(r == 1){
            logger.info("update data:{},condition:{}", ToStringBuilder.reflectionToString(entity, ToStringStyle.SHORT_PREFIX_STYLE)
            ,ToStringBuilder.reflectionToString(condition, ToStringStyle.SHORT_PREFIX_STYLE));
        }
        return r;
    }
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int updateSelective(PO entity, PO condition) {
        int r = crudDao.updateSelective(entity,condition);
        if(r == 1){
            logger.info("updateSelective data:{},condition:{}", ToStringBuilder.reflectionToString(entity, ToStringStyle.SHORT_PREFIX_STYLE)
                    ,ToStringBuilder.reflectionToString(condition, ToStringStyle.SHORT_PREFIX_STYLE));
        }
        return r;
    }
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int updateBatchByPrimaryKey(List<PO> entities) {
        int r = crudDao.updateBatchByPrimaryKey(entities);
        if(r == 1){
            logger.info("updateBatchByPrimaryKey data:{}", ToStringBuilder.reflectionToString(entities, ToStringStyle.SHORT_PREFIX_STYLE));
        }
        return r;
    }
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int updateBatchByPrimaryKeys(List<PK> primaryKeys, PO entity) {
        int r = crudDao.updateBatchByPrimaryKeys(primaryKeys,entity);
        if(r == 1){
            logger.info("updateBatchByPrimaryKeys data:{},condition:{}", ToStringBuilder.reflectionToString(entity, ToStringStyle.SHORT_PREFIX_STYLE)
                    ,ToStringBuilder.reflectionToString(primaryKeys, ToStringStyle.SHORT_PREFIX_STYLE));
        }
        return r;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int updateBatchByPrimaryKeysSelective(List<PK> primaryKeys, PO entity) {
        int r = crudDao.updateBatchByPrimaryKeysSelective(primaryKeys,entity);
        if(r == 1){
            logger.info("updateBatchByPrimaryKeysSelective data:{},condition:{}", ToStringBuilder.reflectionToString(entity, ToStringStyle.SHORT_PREFIX_STYLE)
                    ,ToStringBuilder.reflectionToString(primaryKeys, ToStringStyle.SHORT_PREFIX_STYLE));
        }
        return r;
    }

    @Transactional(propagation= Propagation.SUPPORTS,readOnly=true)
    @Override
    public PO selectByPrimaryKeySimple(PK id) {
        return crudDao.selectByPrimaryKey(id);
    }
    @Transactional(propagation= Propagation.SUPPORTS,readOnly=true)
    @Override
    public PO selectByPrimaryKeySimple(PK id, boolean includeDeleted) {
        return includeDeleted ? crudDao.selectByPrimaryKey(id) : crudDao.selectByPrimaryKeyWithoutDeleted(id);
    }

    @Transactional(propagation= Propagation.SUPPORTS,readOnly=true)
    @Override
    public DTO selectByPrimaryKey(PK id) {
        return wrapDto(crudDao.selectByPrimaryKey(id));
    }

    @Override
    public DTO selectByPrimaryKey(PK id, boolean includeDeleted) {
        return includeDeleted ? wrapDto(crudDao.selectByPrimaryKey(id)) : wrapDto(crudDao.selectByPrimaryKeyWithoutDeleted(id));
    }

    @Transactional(propagation= Propagation.SUPPORTS,readOnly=true)
    @Override
    public PO selectOneSimple(PO record) {

        return crudDao.selectOne(record);
    }
    @Transactional(propagation= Propagation.SUPPORTS,readOnly=true)
    @Override
    public DTO selectOne(PO record) {
        return wrapDto(crudDao.selectOne(record));
    }
    @Transactional(propagation= Propagation.SUPPORTS,readOnly=true)
    @Override
    public List<PO> selectListSimple(PO record) {
        return crudDao.selectList(record);
    }

    @Override
    public List<PO> selectListSimple(PO entity, Orderby orderby) {
        orderbyStart(orderby);
        return crudDao.selectList(entity);
    }

    @Override
    public PageResultDto<PO> selectListSimple(PO entity, PageAndOrderbyParamDto pageAndOrderbyParamDto) {
        com.github.pagehelper.Page p = pageAndOrderbyStart(pageAndOrderbyParamDto);
        List<PO> list = crudDao.selectList(entity);
        return new PageResultDto<PO>(list,wrapPage(p));
    }

    @Transactional(propagation= Propagation.SUPPORTS,readOnly=true)
    @Override
    public List<DTO> selectList(PO record) {
        return wrapDtos(crudDao.selectList(record));
    }

    @Override
    public List<DTO> selectList(PO entity, Orderby orderby) {
        orderbyStart(orderby);
        return wrapDtos(crudDao.selectList(entity));
    }

    @Override
    public PageResultDto<DTO> selectList(PO entity, PageAndOrderbyParamDto pageAndOrderbyParamDto) {
        com.github.pagehelper.Page p = pageAndOrderbyStart(pageAndOrderbyParamDto);
        List<DTO> list = wrapDtos(crudDao.selectList(entity));
        return new PageResultDto<DTO>(list,wrapPage(p));
    }

    @Transactional(propagation= Propagation.SUPPORTS,readOnly=true)
    @Override
    public List<PO> selectSimple(Map<String, Object> map) {
        return crudDao.select(map);
    }

    @Override
    public List<PO> selectSimple(Map<String, Object> map, Orderby orderby) {
        orderbyStart(orderby);
        return crudDao.select(map);
    }

    @Override
    public PageResultDto<PO> selectSimple(Map<String, Object> map, PageAndOrderbyParamDto pageAndOrderbyParamDto) {
        com.github.pagehelper.Page p = pageAndOrderbyStart(pageAndOrderbyParamDto);
        List<PO> list = crudDao.select(map);
        return new PageResultDto<PO>(list,wrapPage(p));
    }

    @Transactional(propagation= Propagation.SUPPORTS,readOnly=true)
    @Override
    public List<DTO> select(Map<String, Object> map) {
        return wrapDtos(crudDao.select(map));
    }

    @Override
    public List<DTO> select(Map<String, Object> map, Orderby orderby) {
        orderbyStart(orderby);
        return wrapDtos(crudDao.select(map));
    }

    @Override
    public PageResultDto<DTO> select(Map<String, Object> map, PageAndOrderbyParamDto pageAndOrderbyParamDto) {
        com.github.pagehelper.Page p = pageAndOrderbyStart(pageAndOrderbyParamDto);
        List<DTO> list = wrapDtos(crudDao.select(map));
        return new PageResultDto<DTO>(list,wrapPage(p));
    }

    @Transactional(propagation= Propagation.SUPPORTS,readOnly=true)
    @Override
    public List<PO> selectAllSimple(boolean includeDeleted) {
        return crudDao.selectAll(includeDeleted);
    }

    @Override
    public List<PO> selectAllSimple(boolean includeDeleted, Orderby orderby) {
        orderbyStart(orderby);
        return crudDao.selectAll(includeDeleted);
    }

    @Override
    public PageResultDto<PO> selectAllSimple(boolean includeDeleted, PageAndOrderbyParamDto pageAndOrderbyParamDto) {
        com.github.pagehelper.Page p = pageAndOrderbyStart(pageAndOrderbyParamDto);
        List<PO> list = crudDao.selectAll(includeDeleted);
        return new PageResultDto<PO>(list,wrapPage(p));
    }

    @Transactional(propagation= Propagation.SUPPORTS,readOnly=true)
    @Override
    public List<DTO> selectAll(boolean includeDeleted) {
        return wrapDtos(crudDao.selectAll(includeDeleted));
    }

    @Override
    public List<DTO> selectAll(boolean includeDeleted, Orderby orderby) {
        orderbyStart(orderby);
        return wrapDtos(crudDao.selectAll(includeDeleted));
    }

    @Override
    public PageResultDto<DTO> selectAll(boolean includeDeleted, PageAndOrderbyParamDto pageAndOrderbyParamDto) {
        com.github.pagehelper.Page p = pageAndOrderbyStart(pageAndOrderbyParamDto);
        p = PageHelper.getLocalPage();
        List<DTO> list = wrapDtos(crudDao.selectAll(includeDeleted));
        return new PageResultDto<DTO>(list,wrapPage(p));
    }

    @Override
    public List<PO> selectByPrimaryKeysSimple(List<PK> primaryKeys, boolean includeDeleted) {
        return crudDao.selectByPrimaryKeys(primaryKeys,includeDeleted);
    }

    @Override
    public List<PO> selectByPrimaryKeysSimple(List<PK> primaryKeys, boolean includeDeleted, Orderby orderby) {
        orderbyStart(orderby);
        return crudDao.selectByPrimaryKeys(primaryKeys,includeDeleted);
    }

    @Override
    public PageResultDto<PO> selectByPrimaryKeysSimple(List<PK> primaryKeys, boolean includeDeleted, PageAndOrderbyParamDto pageAndOrderbyParamDto) {
        com.github.pagehelper.Page p = pageAndOrderbyStart(pageAndOrderbyParamDto);
        p = PageHelper.getLocalPage();
        List<PO> list = crudDao.selectByPrimaryKeys(primaryKeys,includeDeleted);
        return new PageResultDto<PO>(list,wrapPage(p));
    }

    @Override
    public List<DTO> selectByPrimaryKeys(List<PK> primaryKeys, boolean includeDeleted) {
        return wrapDtos(crudDao.selectByPrimaryKeys(primaryKeys,includeDeleted));
    }

    @Override
    public List<DTO> selectByPrimaryKeys(List<PK> primaryKeys, boolean includeDeleted, Orderby orderby) {
        orderbyStart(orderby);
        return wrapDtos(crudDao.selectByPrimaryKeys(primaryKeys,includeDeleted));
    }

    @Override
    public PageResultDto<DTO> selectByPrimaryKeys(List<PK> primaryKeys, boolean includeDeleted, PageAndOrderbyParamDto pageAndOrderbyParamDto) {
        com.github.pagehelper.Page p = pageAndOrderbyStart(pageAndOrderbyParamDto);
        p = PageHelper.getLocalPage();
        List<DTO> list = wrapDtos(crudDao.selectByPrimaryKeys(primaryKeys,includeDeleted));
        return new PageResultDto<DTO>(list,wrapPage(p));
    }


    @Override
    public void preInsert(PO po, PK userId) {
        po.setCreateBy(userId);
        po.setCreateAt(new Date());
        po.setUpdateBy(userId);
        po.setUpdateAt(new Date());
        po.setDelFlag(BasePo.YesNo.N.name());
    }

    @Override
    public void preUpdate(PO po, PK userId) {
        po.setUpdateBy(userId);
        po.setUpdateAt(new Date());
    }

    public DTO wrapDto(PO po) {
        if(aClass == null){
            throw new IllegalArgumentException("aClass can not be null");
        }
        if(po == null) return null;
        DTO dto = (DTO) BeanUtils.instantiateClass(aClass);
        //spring的判断了参数类型，id是动态的类型不匹配
        //BeanUtils.copyProperties(po, dto);
        try {
            ConvertUtils.register(new DateConverter(null), java.util.Date.class);
            BeanUtilsBean.getInstance().copyProperties(dto,po);
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
        }
        return dto;
    }

    public  List<DTO> wrapDtos(List<PO> pos) {
        DTO dto = null;
        if (!CollectionUtils.isEmpty(pos)) {
            List<DTO> dtos = new ArrayList<>();
            for (PO po : pos) {
                dtos.add(wrapDto(po));
            }
            return dtos;
        }
        return null;
    }
    @Transactional(propagation= Propagation.SUPPORTS,readOnly=true)
    @Override
    public int count(PO entity) {
        return crudDao.count(entity);
    }
    @Transactional(propagation= Propagation.SUPPORTS,readOnly=true)
    @Override
    public int counts(Map<String, Object> map) {
        return crudDao.counts(map);
    }

    public void orderbyStart(Orderby orderby){
        if (orderby != null) {
            OrderbyUtils.putOrderbyToThreadLocal(orderby);
            OrderbyUtils.orderbyStart();
            if(orderby.isOrderable()){
                OrderByHelper.orderBy(orderby.toString());
            }
        }

    }

    public com.github.pagehelper.Page pageAndOrderbyStart(PageAndOrderbyParamDto pageAndOrderbyParamDto){
        if (pageAndOrderbyParamDto != null) {
            orderbyStart(pageAndOrderbyParamDto.getOrderby());
            if (pageAndOrderbyParamDto.getPage() != null) {
                PageUtils.putPageToThreadLocal(pageAndOrderbyParamDto.getPage());
                PageUtils.pageStart();
                if(pageAndOrderbyParamDto.getPage().isPageable()){
                    com.github.pagehelper.Page p = PageHelper.startPage(pageAndOrderbyParamDto.getPage().getPageNo(), pageAndOrderbyParamDto.getPage().getPageSize());
                    return p;
                }
            }
        }
        return null;
    }

    public Page wrapPage(com.github.pagehelper.Page p) {
        if (p != null) {
            Page mypage = new Page();
            mypage.setPageNo(p.getPageNum());
            mypage.setPageSize(p.getPageSize());
            mypage.setDataNum((int) p.getTotal());
            //这里改为true，肯定是分页了
            mypage.setPageable(true);
            return mypage;
        }

        return null;
    }
}
