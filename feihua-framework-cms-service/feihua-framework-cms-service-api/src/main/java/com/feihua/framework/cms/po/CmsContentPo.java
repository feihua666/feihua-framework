package com.feihua.framework.cms.po;

import feihua.jdbc.api.pojo.BasePo;
import java.util.Date;

/**
 * This class was generated by MyBatis Generator.
 * @author yangwei 2018-12-06 19:39:53
 * Database Table Remarks:
 *   cms内容表
 *
 * This class corresponds to the database table cms_content
 * @mbg.generated do_not_delete_during_merge 2018-12-06 19:39:53
*/
public class CmsContentPo extends feihua.jdbc.api.pojo.BasePo<String> {
    /**
     * Database Column Remarks:
     *   标题
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cms_content.TITLE
     *
     * @mbg.generated 2018-12-06 19:39:53
     */
    private String title;

    /**
     * Database Column Remarks:
     *   作者
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cms_content.AUTHOR
     *
     * @mbg.generated 2018-12-06 19:39:53
     */
    private String author;

    /**
     * Database Column Remarks:
     *   来源，原文，如果是原创，写原创即可
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cms_content.ORIGINAL
     *
     * @mbg.generated 2018-12-06 19:39:53
     */
    private String original;

    /**
     * Database Column Remarks:
     *   简介
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cms_content.PROFILE
     *
     * @mbg.generated 2018-12-06 19:39:53
     */
    private String profile;

    /**
     * Database Column Remarks:
     *   状态，draft=草稿，to_be_audit=待审核，audit=已通过/已发布
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cms_content.STATUS
     *
     * @mbg.generated 2018-12-06 19:39:53
     */
    private String status;

    /**
     * Database Column Remarks:
     *   发布时间
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cms_content.PUBLISH_AT
     *
     * @mbg.generated 2018-12-06 19:39:53
     */
    private Date publishAt;

    /**
     * Database Column Remarks:
     *   内容类型，article=文章，library=文库，gallery=图库
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cms_content.CONTENT_TYPE
     *
     * @mbg.generated 2018-12-06 19:39:53
     */
    private String contentType;

    /**
     * Database Column Remarks:
     *   站点id
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cms_content.SITE_ID
     *
     * @mbg.generated 2018-12-06 19:39:53
     */
    private String siteId;

    /**
     * Database Column Remarks:
     *   栏目id
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cms_content.CHANNEL_ID
     *
     * @mbg.generated 2018-12-06 19:39:53
     */
    private String channelId;

    /**
     * Database Column Remarks:
     *   模板默认index.html
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cms_content.TEMPLATE
     *
     * @mbg.generated 2018-12-06 19:39:53
     */
    private String template;

    /**
     * Database Column Remarks:
     *   图片url
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cms_content.IMAGE_URL
     *
     * @mbg.generated 2018-12-06 19:39:53
     */
    private String imageUrl;

    /**
     * Database Column Remarks:
     *   图片描述
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cms_content.IMAGE_DES
     *
     * @mbg.generated 2018-12-06 19:39:53
     */
    private String imageDes;

    /**
     * Database Column Remarks:
     *   页面访问量,页面点击次数
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cms_content.PV
     *
     * @mbg.generated 2018-12-06 19:39:53
     */
    private Integer pv;

    /**
     * Database Column Remarks:
     *   页面访问量，独立IP数，页面访问ip数
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cms_content.IV
     *
     * @mbg.generated 2018-12-06 19:39:53
     */
    private Integer iv;

    /**
     * Database Column Remarks:
     *   页面访问量，独立电脑客户端数
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cms_content.UV
     *
     * @mbg.generated 2018-12-06 19:39:53
     */
    private Integer uv;

    /**
     * Database Column Remarks:
     *   内容
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cms_content.CONTENT
     *
     * @mbg.generated 2018-12-06 19:39:53
     */
    private String content;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getOriginal() {
        return original;
    }

    public void setOriginal(String original) {
        this.original = original;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getPublishAt() {
        return publishAt;
    }

    public void setPublishAt(Date publishAt) {
        this.publishAt = publishAt;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageDes() {
        return imageDes;
    }

    public void setImageDes(String imageDes) {
        this.imageDes = imageDes;
    }

    public Integer getPv() {
        return pv;
    }

    public void setPv(Integer pv) {
        this.pv = pv;
    }

    public Integer getIv() {
        return iv;
    }

    public void setIv(Integer iv) {
        this.iv = iv;
    }

    public Integer getUv() {
        return uv;
    }

    public void setUv(Integer uv) {
        this.uv = uv;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public com.feihua.framework.cms.api.ApiCmsContentPoService service() {
        return com.feihua.utils.spring.SpringContextHolder.getBean(com.feihua.framework.cms.api.ApiCmsContentPoService.class);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", title=").append(title);
        sb.append(", author=").append(author);
        sb.append(", original=").append(original);
        sb.append(", profile=").append(profile);
        sb.append(", status=").append(status);
        sb.append(", publishAt=").append(publishAt);
        sb.append(", contentType=").append(contentType);
        sb.append(", siteId=").append(siteId);
        sb.append(", channelId=").append(channelId);
        sb.append(", template=").append(template);
        sb.append(", imageUrl=").append(imageUrl);
        sb.append(", imageDes=").append(imageDes);
        sb.append(", pv=").append(pv);
        sb.append(", iv=").append(iv);
        sb.append(", uv=").append(uv);
        sb.append(", content=").append(content);
        sb.append("]");
        return sb.toString();
    }
}