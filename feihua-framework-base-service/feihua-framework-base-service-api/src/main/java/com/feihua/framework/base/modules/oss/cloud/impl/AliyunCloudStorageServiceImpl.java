package com.feihua.framework.base.modules.oss.cloud.impl;

import com.aliyun.oss.OSSClient;
import com.feihua.exception.BaseException;
import com.feihua.framework.base.modules.oss.cloud.dto.AliyunStorageConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * @Auther: wzn
 * @Date: 2019/2/20 15:25
 * @Description: 阿里云存储
 */
public class AliyunCloudStorageServiceImpl extends AbstractCloudStorageService {
    private static Logger logger = LoggerFactory.getLogger(AliyunCloudStorageServiceImpl.class);
    private OSSClient client;
    private AliyunStorageConfig config;
    public AliyunCloudStorageServiceImpl(AliyunStorageConfig config) {
        this.config = config;
        //初始化
        init();
    }

    private void init() {
        client = new OSSClient(config.getAliyunEndPoint(), config.getAliyunAccessKeyId(),
                config.getAliyunAccessKeySecret());
    }

    @Override
    public String upload(byte[] data, String filepath) {
        return upload(new ByteArrayInputStream(data), filepath);
    }

    @Override
    public String uploadSuffix(byte[] data,String prefixPath, String suffix) {
        return upload(data, getPath(config.getAliyunPrefix() + "/" + prefixPath, suffix));
    }

    @Override
    public String upload(InputStream inputStream, String filepath) {
        try {
            client.putObject(config.getAliyunBucketName(), filepath, inputStream);
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            throw new BaseException("阿里去上传文件失败，请检查配置信息");
        }

        return config.getAliyunDomain() + "/" + filepath;
    }

    @Override
    public String uploadSuffix(InputStream inputStream,String prefixPath, String suffix) {
        return upload(inputStream, getPath(config.getAliyunPrefix() + "/" + prefixPath, suffix));
    }

    @Override
    public byte[] download(String objectName) {

        return null;
    }
}
