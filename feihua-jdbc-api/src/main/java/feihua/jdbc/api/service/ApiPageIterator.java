package feihua.jdbc.api.service;

import feihua.jdbc.api.pojo.Page;
import feihua.jdbc.api.pojo.PageResultDto;

import java.util.List;

/**
 * Created by yangwei
 * Created at 2018/11/1 20:35
 */
public interface ApiPageIterator<T> {

    /**
     * 返回页数数据
     * @return
     */
    public List<T> next();
}
