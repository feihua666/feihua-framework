package com.feihua.framework.mybatis.orm;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import java.util.Map;

/**
 * Created by yangwei
 * Created at 2018/6/29 11:16
 */
public class MultipleDataSource extends AbstractRoutingDataSource {



    private static final ThreadLocal<String> dataSourceKey = new InheritableThreadLocal<String>();

    public static void setDataSourceKey(String dataSource) {
        dataSourceKey.set(dataSource);
    }
    @Override
    protected Object determineCurrentLookupKey() {
        return dataSourceKey.get();
    }

    public enum DataSourceKey{
        dataSourceDefault
    }
}
