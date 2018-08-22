package com.feihua.framework.base.modules;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 直接运行sql
 * Created by yangwei
 * Created at 2018/7/24 14:32
 */
public interface ApiNativeSqlService {

    List<Map<String, Object>> selectByNativeSqlForList(String sql);
    Map<String, Object> selectByNativeSqlForOne(String sql);
    int insertByNativeSql(String sql);
    int updateByNativeSql(String sql);
    int deleteByNativeSql(String sql);

    List<Map<String, Object>> selectByNativeSqlForList( String sql, Map<String, Object> map);
    Map<String, Object> selectByNativeSqlForOne(String sql,Map<String, Object> map);
}
