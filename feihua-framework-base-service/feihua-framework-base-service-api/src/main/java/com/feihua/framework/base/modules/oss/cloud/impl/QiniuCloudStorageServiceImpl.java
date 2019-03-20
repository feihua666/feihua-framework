package com.feihua.framework.base.modules.oss.cloud.impl;

import com.feihua.exception.BaseException;
import com.feihua.framework.base.modules.oss.cloud.dto.QiniuStorageConfig;
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
public class QiniuCloudStorageServiceImpl extends AbstractCloudStorageService {
    private static Logger logger = LoggerFactory.getLogger(QiniuCloudStorageServiceImpl.class);
    private QiniuStorageConfig config;
    private UploadManager uploadManager;
    private String token;

    public QiniuCloudStorageServiceImpl(QiniuStorageConfig config){
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
    public String upload(byte[] data, String filepath) {
        try {
            Response res = uploadManager.put(data, filepath, token);
            if (!res.isOK()) {
                logger.error("上传七牛出错：" + res.toString());
                throw new RuntimeException("上传七牛出错："+res.toString());
            }
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            throw new BaseException("上传文件失败，请核对七牛配置信息");
        }

        return config.getQiniuDomain() + "/" + filepath;
    }

    @Override
    public String uploadSuffix(byte[] data,String prefixPath, String suffix) {
        return upload(data, getPath(config.getQiniuPrefix() + "/" + prefixPath, suffix));
    }

    @Override
    public String upload(InputStream inputStream, String filepath) {
        try {
            byte[] data = IOUtils.toByteArray(inputStream);
            return this.upload(data, filepath);
        } catch (IOException e) {
            logger.error(e.getMessage(),e);
            throw new BaseException("七牛上传文件失败");
        }
    }

    @Override
    public String uploadSuffix(InputStream inputStream,String prefixPath, String suffix) {
        return upload(inputStream, getPath(config.getQiniuPrefix() + "/" + prefixPath, suffix));
    }

    @Override
    public byte[] download(String objectName) {

        return null;
    }
}
