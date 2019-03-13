package com.feihua.framework.oss.cloud;

import com.feihua.exception.BaseException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

/**
 * @Auther: wzn
 * @Date: 2019/2/20 15:41
 * @Description: 七牛云存储
 */
public class QiniuCloudStorageService extends CloudStorageService {
    private static Logger logger = LoggerFactory.getLogger(QiniuCloudStorageService.class);

    private UploadManager uploadManager;
    private String token;

    public QiniuCloudStorageService(CloudStorageConfig config){
        this.config = config;
        //初始化
        init();
    }

    private void init(){
        uploadManager = new UploadManager(new Configuration(Zone.autoZone()));
        token = Auth.create(config.getQiniuAccessKey(), config.getQiniuSecretKey()).
                uploadToken(config.getQiniuBucketName());
    }

    @Override
    public String upload(byte[] data, String path) {
        try {
            Response res = uploadManager.put(data, path, token);
            if (!res.isOK()) {
                logger.error("上传七牛出错：" + res.toString());
                throw new RuntimeException("上传七牛出错："+res.toString());
            }
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            throw new BaseException("上传文件失败，请核对七牛配置信息");
        }

        return config.getQiniuDomain() + "/" + path;
    }

    @Override
    public String uploadSuffix(byte[] data, String suffix) {
        return upload(data, getPath(config.getQiniuPrefix(), suffix));
    }

    @Override
    public String upload(InputStream inputStream, String path) {
        try {
            byte[] data = IOUtils.toByteArray(inputStream);
            return this.upload(data, path);
        } catch (IOException e) {
            logger.error(e.getMessage(),e);
            throw new BaseException("七牛上传文件失败");
        }
    }

    @Override
    public String uploadSuffix(InputStream inputStream, String suffix) {
        return upload(inputStream, getPath(config.getQiniuPrefix(), suffix));
    }

    @Override
    public void download(String objectName) {

    }
}
