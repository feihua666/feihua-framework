package com.feihua.framework.base.modules.oss.cloud.api;

import com.feihua.framework.base.modules.oss.cloud.dto.CloudStorageConfig;

import java.io.InputStream;

/**
 * Created by yangwei
 * Created at 2019/3/14 10:47
 */
public interface ApiCloudStorageService {

    public CloudStorageConfig getConfig();

    /**
     * 文件上传
     * @param data    文件字节数组
     * @param filepath    文件路径，包含文件名 /head/yw/xxxx.png
     * @return        返回http地址
     */
    public String upload(byte[] data, String filepath);

    /**
     * 文件上传
     * @param data     文件字节数组
     * @param suffix   后缀 .png
     * @param prefixPath   前缀路径 photo,test/photo
     * @return         返回http地址
     */
    public String uploadSuffix(byte[] data,String prefixPath, String suffix);

    /**
     * 文件上传
     * @param inputStream   字节流
     * @param filepath          文件路径，包含文件名，包含文件名 /head/yw/xxxx.png
     * @return              返回http地址
     */
    public String upload(InputStream inputStream, String filepath);

    /**
     * 文件上传
     * @param inputStream  字节流
     * @param suffix       后缀
     * @param prefixPath   前缀路径 photo,test/photo
     * @return             返回http地址
     */
    public String uploadSuffix(InputStream inputStream,String prefixPath, String suffix);

    /**
     * 文件下载
     * @param objectName  文件路径
     */
    public byte[] download(String objectName );

    public int delete(String objectName);

}
