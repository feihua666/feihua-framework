package feihua.jdbc.api.dao;

import feihua.jdbc.api.pojo.BasePo;

/**
 * Created by feihua on 2015/6/29.
 * 增、删、改、查
 */
public interface CrudDao<PO extends BasePo,PK> extends InsertDao<PO,PK>,DeleteDao<PO,PK>,UpdateDao<PO,PK>,SelectDao<PO,PK> ,CountDao<PO,PK>{

}
