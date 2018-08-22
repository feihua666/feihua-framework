package feihua.jdbc.api.service;

import feihua.jdbc.api.pojo.BaseDto;
import feihua.jdbc.api.pojo.BasePo;

import java.util.List;

/**
 * insertservice 接口类的基类
 * Created by yangwei
 * Created at 2017/8/17 15:03
 */
public interface InsertBaseService<PO extends BasePo,DTO extends BaseDto> {

    PO insertSimple(PO record);
    DTO insert(PO record);
    PO insertSelectiveSimple(PO record);
    DTO insertSelective(PO record);
    PO insertWithPrimaryKeySimple(PO entity);
    DTO insertWithPrimaryKey(PO entity);
    PO insertSelectiveWithPrimaryKeySimple(PO entity);
    DTO insertSelectiveWithPrimaryKey(PO entity);
    int insertBatch(List<PO> entities);
    int insertBatchWithPrimaryKey(List<PO> entities);
}
