package com.feihua.framework.base.modules.oss.cloud.dto;

/**
 * 本地存储
 * Created by yangwei
 * Created at 2019/3/14 11:39
 */
public class LocalStorageConfig {
    // 文件的存放位置，绝对路径，D:/base-rest/images
    private String locationPath;
    private String localPrefix;

    public String getLocationPath() {
        return locationPath;
    }

    public void setLocationPath(String locationPath) {
        this.locationPath = locationPath;
    }

    public String getLocalPrefix() {
        return localPrefix;
    }

    public void setLocalPrefix(String localPrefix) {
        this.localPrefix = localPrefix;
    }
}
