package com.feihua.framework.base.modules.oss.cloud;

import com.feihua.framework.base.modules.config.api.ApiBaseConfigService;
import com.feihua.framework.constants.ConfigConstant;
import com.feihua.utils.spring.SpringContextHolder;

/**
 * @Auther: wzn
 * @Date: 2019/2/20 15:43
 * @Description: 系统云配置工厂
 */
public final class OSSFactory {
    private static CloudStorageConfig config;

    static {
        ApiBaseConfigService apiBaseConfigService = SpringContextHolder.getBean(ApiBaseConfigService.class);
        OSSFactory.config = apiBaseConfigService.getConfigObject(ConfigConstant.OSS_CLOUD_STORAGE_CONFIG_KEY, CloudStorageConfig.class);
    }

    /**
     * 构建云配置对象
     *
     * @return
     */
    public static CloudStorageService build() {
        //获取云存储配置信息
        if (config.getType().equals(ConfigConstant.OSSCloud.QINIUCLOUD.getValue())) {
            return new QiniuCloudStorageService(config);
        } else if (config.getType().equals(ConfigConstant.OSSCloud.ALIYUN.getValue())) {
            return new AliyunCloudStorageService(config);
        } else if (config.getType().equals(ConfigConstant.OSSCloud.QCLOUD.getValue())) {
            return new QcloudCloudStorageService(config);
        }
        return null;
    }

    /**
     * 获取云配置名称
     *
     * @return
     */
    public static String getType() {
        return ConfigConstant.OSSCloud.getEnum(config.getType()).getName();
    }

    /**
     * 是否开启云配置
     *
     * @return
     */
    public static boolean open() {
        if (config != null) {
            return config.getOpen().equalsIgnoreCase("y") ? true : false;
        }
        return false;
    }

    /**
     * 是否开启压缩
     *
     * @return
     */
    public static boolean compress() {
        if (config != null) {
            return config.getCompress().equalsIgnoreCase("y") ? true : false;
        }
        return false;
    }

}
