package com.feihua.framework.base.modules.file.po;

import feihua.jdbc.api.pojo.BasePo;

/**
 * This class was generated by MyBatis Generator.
 * @author yangwei 2018-04-16 17:39:51
 *
 * This class corresponds to the database table base_file
 * @mbg.generated do_not_delete_during_merge 2018-04-16 17:39:51
*/
public class BaseFilePo extends feihua.jdbc.api.pojo.BasePo<String> {
    /**
     * Database Column Remarks:
     *   名称
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column base_file.NAME
     *
     * @mbg.generated 2018-04-16 17:39:51
     */
    private String name;

    /**
     * Database Column Remarks:
     *   文件名
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column base_file.FILENAME
     *
     * @mbg.generated 2018-04-16 17:39:51
     */
    private String filename;

    /**
     * Database Column Remarks:
     *   文件相对路径
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column base_file.FILE_PATH
     *
     * @mbg.generated 2018-04-16 17:39:51
     */
    private String filePath;

    /**
     * Database Column Remarks:
     *   生成文件耗时长，单位秒
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column base_file.DURATION
     *
     * @mbg.generated 2018-04-16 17:39:51
     */
    private String duration;

    /**
     * Database Column Remarks:
     *   下载次数
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column base_file.DOWNLOAD_NUM
     *
     * @mbg.generated 2018-04-16 17:39:51
     */
    private Integer downloadNum;

    /**
     * Database Column Remarks:
     *   分类，图片，文件等
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column base_file.TYPE
     *
     * @mbg.generated 2018-04-16 17:39:51
     */
    private String type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public Integer getDownloadNum() {
        return downloadNum;
    }

    public void setDownloadNum(Integer downloadNum) {
        this.downloadNum = downloadNum;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public com.feihua.framework.base.modules.file.api.ApiBaseFilePoService service() {
        return com.feihua.utils.spring.SpringContextHolder.getBean(com.feihua.framework.base.modules.file.api.ApiBaseFilePoService.class);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", name=").append(name);
        sb.append(", filename=").append(filename);
        sb.append(", filePath=").append(filePath);
        sb.append(", duration=").append(duration);
        sb.append(", downloadNum=").append(downloadNum);
        sb.append(", type=").append(type);
        sb.append("]");
        return sb.toString();
    }
}