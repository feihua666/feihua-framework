package com.feihua.framework.base.impl;

import com.feihua.framework.base.modules.config.api.ApiBaseConfigService;
import com.feihua.framework.base.modules.oss.cloud.api.ApiCloudStorageService;
import com.feihua.framework.base.modules.oss.cloud.dto.CloudStorageConfig;
import com.feihua.framework.base.modules.oss.cloud.impl.AliyunCloudStorageServiceImpl;
import com.feihua.framework.base.modules.oss.cloud.impl.LocalStorageServiceImpl;
import com.feihua.framework.base.modules.oss.cloud.impl.QcloudCloudStorageServiceImpl;
import com.feihua.framework.base.modules.oss.cloud.impl.QiniuCloudStorageServiceImpl;
import com.feihua.framework.constants.ConfigConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;

/**
 * @Auther: wzn
 * @Date: 2019/2/20 15:27
 * @Description: 云存储(支持七牛、阿里云、腾讯云、又拍云)
 */
@Service
public class ApiCloudStorageServiceImpl implements ApiCloudStorageService {

    @Autowired
    private ApiBaseConfigService apiBaseConfigService;

    /**
     * 构建云配置对象
     *
     * @return
     */
    private ApiCloudStorageService build() {
        CloudStorageConfig config = getConfig();
        //获取云存储配置信息
        if (ConfigConstant.OSSCloud.QNY.name().equals(config.getType())) {
            return new QiniuCloudStorageServiceImpl(config.getQiniu());
        } else if (ConfigConstant.OSSCloud.ALY.name().equals(config.getType())) {
            return new AliyunCloudStorageServiceImpl(config.getAliyun());
        } else if (ConfigConstant.OSSCloud.TXY.name().equals(config.getType())) {
            return new QcloudCloudStorageServiceImpl(config.getQcloud());
        }else if (ConfigConstant.OSSCloud.LOCAL.name().equals(config.getType())) {
            return new LocalStorageServiceImpl(config.getLocal());
        }
        return null;
    }

    @Override
    public CloudStorageConfig getConfig() {
        /** 云存储配置信息 */
        CloudStorageConfig config = apiBaseConfigService.getConfigObject(ConfigConstant.ConfigKey.OSS_CLOUD_STORAGE_CONFIG_KEY.name(), CloudStorageConfig.class);

        return config;
    }

    @Override
    public String upload(byte[] data, String filepath) {
        return this.build().upload(data, filepath);
    }

    @Override
    public String uploadSuffix(byte[] data,String prefixPath, String suffix) {
        return this.build().uploadSuffix(data,prefixPath,suffix);
    }

    @Override
    public String upload(InputStream inputStream, String filepath) {
        return this.build().upload(inputStream, filepath);
    }

    @Override
    public String uploadSuffix(InputStream inputStream,String prefixPath, String suffix) {
        return this.build().uploadSuffix(inputStream,prefixPath,suffix);
    }

    @Override
    public byte[] download(String objectName) {
        return this.build().download(objectName);
    }

    @Override
    public int delete(String objectName) {
        return this.build().delete(objectName);
    }
}
