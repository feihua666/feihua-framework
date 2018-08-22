package feihua.jdbc.api.service;

import feihua.jdbc.api.dao.SelectDao;
import feihua.jdbc.api.pojo.BaseDto;
import feihua.jdbc.api.pojo.BasePo;

import java.util.List;
import java.util.Map;

/**
 * 计数
 * service 接口类的基类，在写service接口时请先继承该类
 * Created by yangwei
 * Created at 2017/8/17 15:03
 */
public interface CountBaseService<PO extends BasePo> {
    /**
     * 计数
     **/
    public int count(PO entity);

    /**
     * 计数
     * @param map
     * @return
     */
    public int counts(Map<String,Object> map);

}
