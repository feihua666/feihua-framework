package com.feihua.framework.base.modules.oss.cloud.dto;

import java.io.Serializable;

/**
 * @Auther: wzn
 * @Date: 2019/2/20 15:21
 * @Description:
 */
public class CloudStorageConfig implements Serializable {
    private static final long serialVersionUID = 3492572823723502561L;

    // 类型 QNY：七牛云  ALY：阿里云  TXY：腾讯云，LOCAL:本地存储，标识当前使用哪一个配置
    private String type;

    // 如果启用压缩 是否启用 Y/N
    // 启用图片压缩，图片上传后，先压缩再保存
    private String compress;
    // 压缩率，越小越清楚，文件越大
    private float compressQuality = 0.3f;

    private AliyunStorageConfig aliyun;
    private QcloudStorageConfig qcloud;
    private QiniuStorageConfig qiniu;
    private LocalStorageConfig local;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCompress() {
        return compress;
    }

    public void setCompress(String compress) {
        this.compress = compress;
    }

    public float getCompressQuality() {
        return compressQuality;
    }

    public void setCompressQuality(float compressQuality) {
        this.compressQuality = compressQuality;
    }

    public AliyunStorageConfig getAliyun() {
        return aliyun;
    }

    public void setAliyun(AliyunStorageConfig aliyun) {
        this.aliyun = aliyun;
    }

    public QcloudStorageConfig getQcloud() {
        return qcloud;
    }

    public void setQcloud(QcloudStorageConfig qcloud) {
        this.qcloud = qcloud;
    }

    public QiniuStorageConfig getQiniu() {
        return qiniu;
    }

    public void setQiniu(QiniuStorageConfig qiniu) {
        this.qiniu = qiniu;
    }

    public LocalStorageConfig getLocal() {
        return local;
    }

    public void setLocal(LocalStorageConfig local) {
        this.local = local;
    }
}
