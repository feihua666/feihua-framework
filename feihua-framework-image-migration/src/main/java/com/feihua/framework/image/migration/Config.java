package com.feihua.framework.image.migration;

import java.util.List;
import java.util.Map;

/**
 * Created by yangwei
 * Created at 2018/10/9 13:34
 */
public class Config {

    public static final String sourceTableName = "sourceTableName";
    public static final String sourceSql = "sourceSql";

    /**
     * map格式为
     * sourceTableName:,
     * sourceSql:,
     *
     */
    List<Map<String,String>> sourceTables;

    public List<Map<String, String>> getSourceTables() {
        return sourceTables;
    }

    public void setSourceTables(List<Map<String, String>> sourceTables) {
        this.sourceTables = sourceTables;
    }
}
