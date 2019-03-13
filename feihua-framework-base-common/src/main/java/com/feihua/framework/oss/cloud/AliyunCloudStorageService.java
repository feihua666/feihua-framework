package com.feihua.framework.oss.cloud;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.GetObjectRequest;
import com.aliyun.oss.model.ObjectMetadata;
import com.feihua.exception.BaseException;
import com.feihua.framework.utils.AliOssClientHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.UUID;

/**
 * @Auther: wzn
 * @Date: 2019/2/20 15:25
 * @Description: 阿里云存储
 */
public class AliyunCloudStorageService extends CloudStorageService {
    private static Logger logger = LoggerFactory.getLogger(AliyunCloudStorageService.class);
    private OSSClient client;

    public AliyunCloudStorageService(CloudStorageConfig config) {
        this.config = config;
        //初始化
        init();
    }

    private void init() {
        client = new OSSClient(config.getAliyunEndPoint(), config.getAliyunAccessKeyId(),
                config.getAliyunAccessKeySecret());
    }

    @Override
    public String upload(byte[] data, String path) {
        return upload(new ByteArrayInputStream(data), path);
    }

    @Override
    public String uploadSuffix(byte[] data, String suffix) {
        return upload(data, getPath(config.getAliyunPrefix(), suffix));
    }

    @Override
    public String upload(InputStream inputStream, String path) {
        try {
            client.putObject(config.getAliyunBucketName(), path, inputStream);
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            throw new BaseException("阿里去上传文件失败，请检查配置信息");
        }

        return config.getAliyunDomain() + "/" + path;
    }

    @Override
    public String uploadSuffix(InputStream inputStream, String suffix) {
        return upload(inputStream, getPath(config.getAliyunPrefix(), suffix));
    }

    @Override
    public void download(String objectName) {
        try {
            ObjectMetadata object = client.getObject(new GetObjectRequest(config.getAliyunBucketName(), objectName), new File(UUID.randomUUID().toString()));
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            client.shutdown();
        }
    }
}
