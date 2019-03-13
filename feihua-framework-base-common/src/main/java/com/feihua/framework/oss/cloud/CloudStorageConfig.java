package com.feihua.framework.oss.cloud;

import java.io.Serializable;

/**
 * @Auther: wzn
 * @Date: 2019/2/20 15:21
 * @Description:
 */
public class CloudStorageConfig implements Serializable {
    private static final long serialVersionUID = 3492572823723502561L;

    //类型 QNY：七牛云  ALY：阿里云  TXY：腾讯云
    private String type;

    //状态 是否启用 Y/N
    private String open;

    //如果启用压缩 是否启用 Y/N
    private String compress;

    //七牛绑定的域名
    private String qiniuDomain;
    //七牛路径前缀
    private String qiniuPrefix;
    //七牛ACCESS_KEY

    private String qiniuAccessKey;
    //七牛SECRET_KEY

    private String qiniuSecretKey;
    //七牛存储空间名

    private String qiniuBucketName;

    //阿里云绑定的域名

    private String aliyunDomain;
    //阿里云路径前缀
    private String aliyunPrefix;
    //阿里云EndPoint

    private String aliyunEndPoint;
    //阿里云AccessKeyId

    private String aliyunAccessKeyId;
    //阿里云AccessKeySecret

    private String aliyunAccessKeySecret;
    //阿里云BucketName

    private String aliyunBucketName;

    //腾讯云绑定的域名

    private String qcloudDomain;
    //腾讯云路径前缀
    private String qcloudPrefix;
    //腾讯云AppId

    private Integer qcloudAppId;
    //腾讯云SecretId

    private String qcloudSecretId;
    //腾讯云SecretKey

    private String qcloudSecretKey;
    //腾讯云BucketName

    private String qcloudBucketName;
    //腾讯云COS所属地区

    private String qcloudRegion;

    public String getCompress() {
        return compress;
    }

    public void setCompress(String compress) {
        this.compress = compress;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getQiniuDomain() {
        return qiniuDomain;
    }

    public void setQiniuDomain(String qiniuDomain) {
        this.qiniuDomain = qiniuDomain;
    }

    public String getQiniuAccessKey() {
        return qiniuAccessKey;
    }

    public void setQiniuAccessKey(String qiniuAccessKey) {
        this.qiniuAccessKey = qiniuAccessKey;
    }

    public String getQiniuSecretKey() {
        return qiniuSecretKey;
    }

    public void setQiniuSecretKey(String qiniuSecretKey) {
        this.qiniuSecretKey = qiniuSecretKey;
    }

    public String getQiniuBucketName() {
        return qiniuBucketName;
    }

    public void setQiniuBucketName(String qiniuBucketName) {
        this.qiniuBucketName = qiniuBucketName;
    }

    public String getQiniuPrefix() {
        return qiniuPrefix;
    }

    public void setQiniuPrefix(String qiniuPrefix) {
        this.qiniuPrefix = qiniuPrefix;
    }

    public String getAliyunDomain() {
        return aliyunDomain;
    }

    public void setAliyunDomain(String aliyunDomain) {
        this.aliyunDomain = aliyunDomain;
    }

    public String getAliyunPrefix() {
        return aliyunPrefix;
    }

    public void setAliyunPrefix(String aliyunPrefix) {
        this.aliyunPrefix = aliyunPrefix;
    }

    public String getAliyunEndPoint() {
        return aliyunEndPoint;
    }

    public void setAliyunEndPoint(String aliyunEndPoint) {
        this.aliyunEndPoint = aliyunEndPoint;
    }

    public String getAliyunAccessKeyId() {
        return aliyunAccessKeyId;
    }

    public void setAliyunAccessKeyId(String aliyunAccessKeyId) {
        this.aliyunAccessKeyId = aliyunAccessKeyId;
    }

    public String getAliyunAccessKeySecret() {
        return aliyunAccessKeySecret;
    }

    public void setAliyunAccessKeySecret(String aliyunAccessKeySecret) {
        this.aliyunAccessKeySecret = aliyunAccessKeySecret;
    }

    public String getAliyunBucketName() {
        return aliyunBucketName;
    }

    public void setAliyunBucketName(String aliyunBucketName) {
        this.aliyunBucketName = aliyunBucketName;
    }

    public String getQcloudDomain() {
        return qcloudDomain;
    }

    public void setQcloudDomain(String qcloudDomain) {
        this.qcloudDomain = qcloudDomain;
    }

    public String getQcloudPrefix() {
        return qcloudPrefix;
    }

    public void setQcloudPrefix(String qcloudPrefix) {
        this.qcloudPrefix = qcloudPrefix;
    }

    public Integer getQcloudAppId() {
        return qcloudAppId;
    }

    public void setQcloudAppId(Integer qcloudAppId) {
        this.qcloudAppId = qcloudAppId;
    }

    public String getQcloudSecretId() {
        return qcloudSecretId;
    }

    public void setQcloudSecretId(String qcloudSecretId) {
        this.qcloudSecretId = qcloudSecretId;
    }

    public String getQcloudSecretKey() {
        return qcloudSecretKey;
    }

    public void setQcloudSecretKey(String qcloudSecretKey) {
        this.qcloudSecretKey = qcloudSecretKey;
    }

    public String getQcloudBucketName() {
        return qcloudBucketName;
    }

    public void setQcloudBucketName(String qcloudBucketName) {
        this.qcloudBucketName = qcloudBucketName;
    }

    public String getQcloudRegion() {
        return qcloudRegion;
    }

    public void setQcloudRegion(String qcloudRegion) {
        this.qcloudRegion = qcloudRegion;
    }

    public String getOpen() {
        return open;
    }

    public void setOpen(String open) {
        this.open = open;
    }
}