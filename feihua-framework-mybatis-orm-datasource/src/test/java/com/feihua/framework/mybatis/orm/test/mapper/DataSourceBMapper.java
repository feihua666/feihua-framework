package com.feihua.framework.mybatis.orm.test.mapper;

import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * Created by yangwei
 * Created at 2018/6/29 11:45
 */
public interface DataSourceBMapper {

    @Select("select * from NBDIR_BUILDING_DIRECTORY")
    List<Map<String,Object>> getAll();
}
