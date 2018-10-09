package com.feihua.framework.mybatis.orm.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Created by yangwei
 * Created at 2018/7/27 14:42
 */
public interface NativeSqlMapper {
    List<Map<String, Object>> selectByNativeSqlForList(@Param("nativeSql") String sql);
    Map<String, Object> selectByNativeSqlForOne(@Param("nativeSql") String sql);
    int insertByNativeSql(@Param("nativeSql") String sql);
    int updateByNativeSql(@Param("nativeSql") String sql);
    int updateByNativeSqlWithParam(@Param("nativeSql") String sql, @Param("p") Map<String, Object> map);
    int deleteByNativeSql(@Param("nativeSql") String sql);

    List<Map<String, Object>> selectByNativeSqlForList(@Param("nativeSql") String sql, @Param("p") Map<String, Object> map);
    Map<String, Object> selectByNativeSqlForOne(@Param("nativeSql") String sql, @Param("p") Map<String, Object> map);
}
