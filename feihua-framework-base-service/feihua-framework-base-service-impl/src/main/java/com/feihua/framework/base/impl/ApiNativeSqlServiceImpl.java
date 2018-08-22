package com.feihua.framework.base.impl;


import com.feihua.framework.base.modules.ApiNativeSqlService;
import com.feihua.framework.mybatis.orm.mapper.NativeSqlMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by yangwei
 * Created at 2018/7/24 14:33
 */
@Service
public class ApiNativeSqlServiceImpl implements ApiNativeSqlService {

    @Autowired
    private NativeSqlMapper nativeSqlMapper;
    @Override
    public List<Map<String, Object>> selectByNativeSqlForList(String sql) {
        return nativeSqlMapper.selectByNativeSqlForList(sql);
    }

    @Override
    public Map<String, Object> selectByNativeSqlForOne(String sql) {
        return nativeSqlMapper.selectByNativeSqlForOne(sql);
    }

    @Override
    public int insertByNativeSql(String sql) {
        return nativeSqlMapper.insertByNativeSql(sql);
    }

    @Override
    public int updateByNativeSql(String sql) {
        return nativeSqlMapper.updateByNativeSql(sql);
    }

    @Override
    public int deleteByNativeSql(String sql) {
        return nativeSqlMapper.deleteByNativeSql(sql);
    }

    @Override
    public List<Map<String, Object>> selectByNativeSqlForList(String sql, Map<String, Object> map) {
        return nativeSqlMapper.selectByNativeSqlForList(sql, map);
    }

    @Override
    public Map<String, Object> selectByNativeSqlForOne(String sql, Map<String, Object> map) {
        return nativeSqlMapper.selectByNativeSqlForOne(sql,map);
    }
}
