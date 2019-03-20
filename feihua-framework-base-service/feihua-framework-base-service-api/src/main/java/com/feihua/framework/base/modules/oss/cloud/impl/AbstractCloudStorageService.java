package com.feihua.framework.base.modules.oss.cloud.impl;

import com.feihua.framework.base.modules.oss.cloud.api.ApiCloudStorageService;
import com.feihua.framework.base.modules.oss.cloud.dto.CloudStorageConfig;
import com.feihua.utils.calendar.CalendarUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.UUID;

/**
 * Created by yangwei
 * Created at 2019/3/14 11:21
 */
public abstract class AbstractCloudStorageService implements ApiCloudStorageService {

    /**
     * 文件路径
     * @param prefix 前缀
     * @param suffix 后缀
     * @return 返回上传路径
     */
    public String getPath(String prefix, String suffix) {
        //生成uuid
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        //文件路径
        String path = CalendarUtils.format(new Date(),"yyyyMMdd") + "/" + uuid;

        if(StringUtils.isNotBlank(prefix)){
            path = prefix + "/" + path;
        }

        return path + suffix;
    }

    @Override
    public CloudStorageConfig getConfig() {
        return null;
    }

    @Override
    public int delete(String objectName) {
        return 0;
    }
}
