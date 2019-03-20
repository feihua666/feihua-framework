package com.feihua.framework.base.modules.oss.cloud.impl;

import com.feihua.exception.BaseException;
import com.feihua.framework.base.modules.oss.cloud.dto.QcloudStorageConfig;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.request.UploadFileRequest;
import com.qcloud.cos.sign.Credentials;
import net.sf.json.JSONObject;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

/**
 * @Auther: wzn
 * @Date: 2019/2/20 15:39
 * @Description: 腾讯云存储
 */
public class QcloudCloudStorageServiceImpl extends AbstractCloudStorageService {
    private static Logger logger = LoggerFactory.getLogger(QcloudCloudStorageServiceImpl.class);

    private QcloudStorageConfig config;
    private COSClient client;

    public QcloudCloudStorageServiceImpl(QcloudStorageConfig config){
        this.config = config;
        //初始化
        init();
    }

    private void init(){
        Credentials credentials = new Credentials(config.getQcloudAppId(), config.getQcloudSecretId(),
                config.getQcloudSecretKey());

        //初始化客户端配置
        ClientConfig clientConfig = new ClientConfig();
        //设置bucket所在的区域，华南：gz 华北：tj 华东：sh
        clientConfig.setRegion(config.getQcloudRegion());

        client = new COSClient(clientConfig, credentials);
    }

    @Override
    public String upload(byte[] data, String filepath) {
        //腾讯云必需要以"/"开头
        if(!filepath.startsWith("/")) {
            filepath = "/" + filepath;
        }

        //上传到腾讯云
        UploadFileRequest request = new UploadFileRequest(config.getQcloudBucketName(), filepath, data);
        String response = client.uploadFile(request);

        JSONObject jsonObject = JSONObject.fromObject(response);
        if(jsonObject.getInt("code") != 0) {
            logger.error("文件上传失败，" + jsonObject.getString("message"));
            throw new BaseException("腾讯云文件上传失败");
        }

        return config.getQcloudDomain() + filepath;
    }

    @Override
    public String uploadSuffix(byte[] data,String prefixPath, String suffix) {
        return upload(data, getPath(config.getQcloudPrefix() + "/" + prefixPath, suffix));
    }

    @Override
    public String upload(InputStream inputStream, String filepath) {
        try {
            byte[] data = IOUtils.toByteArray(inputStream);
            return this.upload(data, filepath);
        } catch (IOException e) {
            logger.error(e.getMessage(),e);
            throw new BaseException("腾讯云文件上传失败");
        }
    }

    @Override
    public String uploadSuffix(InputStream inputStream,String prefixPath, String suffix) {
        return upload(inputStream, getPath(config.getQcloudPrefix() + "/" + prefixPath, suffix));
    }

    @Override
    public byte[] download(String objectName) {

        return null;
    }
}
