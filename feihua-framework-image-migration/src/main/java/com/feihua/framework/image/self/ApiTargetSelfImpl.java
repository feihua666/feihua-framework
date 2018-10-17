package com.feihua.framework.image.self;

import com.feihua.framework.image.migration.target.ApiTargetImpl;
import com.feihua.framework.mybatis.orm.MultipleDataSource;

import java.util.Map;

/**
 * Created by yangwei
 * Created at 2018/10/9 13:15
 */
public class ApiTargetSelfImpl extends ApiTargetImpl{
    @Override
    public Object saveToTargetDb(Map<String, Object> map) {
        MultipleDataSource.setDataSourceKey("dataSourceB");
        return null;
    }

    @Override
    public String getPrefix() {
        return "http://staticimg-target.test.com";
    }
}
