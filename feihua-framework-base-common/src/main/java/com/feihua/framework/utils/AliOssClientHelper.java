package com.feihua.framework.utils;

import com.aliyun.oss.ClientConfiguration;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.common.utils.IOUtils;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.ObjectMetadata;
import com.feihua.utils.calendar.CalendarUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * 阿里去oss客户端工具类
 * Created by yangwei
 * Created at 2018/9/14 16:14
 */
public class AliOssClientHelper {
    private static Logger logger = LoggerFactory.getLogger(AliOssClientHelper.class);

    private String accessKeyId;
    private String accessKeySecret;
    private String endpoint;
    // 内网
    private String endpointInternal;
    private int timeout;
    private String bucketName;
    private OSSClient ossClient;

    /**
     * OSS初始化
     */
    public void init() {
        ClientConfiguration conf = new ClientConfiguration();
        conf.setConnectionTimeout(timeout);
        conf.setMaxErrorRetry(10);
        ossClient = new OSSClient("http://" + getBetterEndpoint(), accessKeyId, accessKeySecret, conf);
        logger.debug("ossClient init completed");
    }

    /**
     * 如果配置内网，优先使用内网
     * @return
     */
    private String getBetterEndpoint(){
        return StringUtils.isEmpty(endpointInternal)? endpoint : endpointInternal;
    }

    /**
     * 销毁
     */
    public void destroy() {
        ossClient.shutdown();
        logger.debug("ossClient shutdown");
    }

    /**
     * 指定的key是否存在
     */
    public boolean isExist(String key) {
        return ossClient.doesObjectExist(bucketName, formatKey(key));
    }

    /**
     * 从OSS中获取文件输入流
     */
    public InputStream getObjeInputStream(String key) {
        OSSObject obj = ossClient.getObject(bucketName, formatKey(key));
        return obj.getObjectContent();
    }

    /**
     * 将输入流下载存到指定的File文件中
     */
    public void saveIsToFile(InputStream is, File file) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            byte[] buffer = new byte[1024 * 10];
            int len;
            while ((len = is.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            IOUtils.safeClose(fos);
        }
    }


    /**
     * 文件下载,以流的形式
     */
    public void downLoadToFile(String key, File file) {
        InputStream is = getObjeInputStream(formatKey(key));
        saveIsToFile(is, file);
    }

    /**
     * 简单上传OSS文件
     *
     * @param fileName    文件名
     * @param file        文件
     * @param is          输入流
     * @param path    分类路径比如 头像:headpic,资讯:news,课程:course,任意发挥
     * @param contentType 手动设置文件类型：image/png
     * @return OSS文件Key的路径
     */
    private String uploadFile(String path, String fileName, File file, InputStream is, String contentType) {
        String key = path + "/" + CalendarUtils.dateToString(CalendarUtils.getCurrentDate(),"yyyyMMdd") + "/" + fileName;
        //
        key = formatKey(key);
        ObjectMetadata meta = null;
        if (contentType != null) {
            meta = new ObjectMetadata();
            meta.setContentType(contentType);
        }
        if (file != null) {
            ossClient.putObject(bucketName, key, file, meta);
        } else if (is != null) {
            ossClient.putObject(bucketName, key, is, meta);
        } else {
            throw new RuntimeException("file and inputStream are both null");
        }
        return key;
    }

    /**
     * 根据key得到绝对路径
     * @param key
     * @return
     */
    public String getAbsolutePath(String key){
        return "http://" + bucketName + "." + endpoint + "/" + key;
    }
    /**
     * 根据key得到绝对路径，内网
     * @param key
     * @return
     */
    public String getAbsolutePathInternal(String key){
        return "http://" + bucketName + "." + endpointInternal + "/" + key;
    }
    /**
     * 简单上传OSS文件
     *
     * @param name        文件名
     * @param file        文件
     * @param path    分类路径比如 头像:headpic,资讯:news,课程:course,任意发挥
     * @param contentType 手动设置文件类型：image/png
     * @return OSS文件Key的路径
     */
    public String uploadFile(String path, String name, File file, String contentType) {
        return uploadFile(path, name, file, null, contentType);
    }

    /**
     * 简单上传OSS文件
     *
     * @param name        文件名
     * @param is          输入流
     * @param path    分类路径比如 头像:headpic,资讯:news,课程:course,任意发挥
     * @param contentType 手动设置文件类型：image/png
     * @return OSS文件Key的路径
     */
    public String uploadFile(String path, String name, InputStream is, String contentType) {
        return uploadFile(path, name, null, is, contentType);
    }

    /**
     * 删除指定key
     */
    public void delFile(String key) {
        ossClient.deleteObject(bucketName, key);
    }

    /**
     * 处理key开头是/,返回开头没有/的key
     */
    private String formatKey(String pathKey) {
        if (pathKey != null) {
            pathKey = pathKey.replaceAll("\\\\", "/");
        }
        while (pathKey != null && pathKey.startsWith("/")) {
            pathKey = pathKey.substring(1, pathKey.length());
        }
        return pathKey;
    }

    public String getAccessKeyId() {
        return accessKeyId;
    }

    public void setAccessKeyId(String accessKeyId) {
        this.accessKeyId = accessKeyId;
    }

    public String getAccessKeySecret() {
        return accessKeySecret;
    }

    public void setAccessKeySecret(String accessKeySecret) {
        this.accessKeySecret = accessKeySecret;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getEndpointInternal() {
        return endpointInternal;
    }

    public void setEndpointInternal(String endpointInternal) {
        this.endpointInternal = endpointInternal;
    }
}
