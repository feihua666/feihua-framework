package com.feihua.framework.image.migration;

import java.util.Map;

/**
 * Created by yangwei
 * Created at 2018/10/9 13:38
 */
public class Context {

    private Config config;
    private String sourceTableName;
    private String sourceSql;
    private int count;

    public Config getConfig() {
        return config;
    }

    public void setConfig(Config config) {
        this.config = config;
    }

    public String getSourceTableName() {
        return sourceTableName;
    }

    public void setSourceTableName(String sourceTableName) {
        this.sourceTableName = sourceTableName;
    }

    public String getSourceSql() {
        return sourceSql;
    }

    public void setSourceSql(String sourceSql) {
        this.sourceSql = sourceSql;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
