package com.feihua.framework.cms.po;

import feihua.jdbc.api.pojo.BasePo;

/**
 * This class was generated by MyBatis Generator.
 * @author yangwei 2018-12-06 16:08:26
 * Database Table Remarks:
 *   内容评论表分类
 *
 * This class corresponds to the database table cms_content_comment
 * @mbg.generated do_not_delete_during_merge 2018-12-06 16:08:26
*/
public class CmsContentCommentPo extends feihua.jdbc.api.pojo.BasePo<String> {
    /**
     * Database Column Remarks:
     *   评论标题
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cms_content_comment.TITLE
     *
     * @mbg.generated 2018-12-06 16:08:26
     */
    private String title;

    /**
     * Database Column Remarks:
     *   评论内容
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cms_content_comment.CONTENT
     *
     * @mbg.generated 2018-12-06 16:08:26
     */
    private String content;

    /**
     * Database Column Remarks:
     *   允许添加一张图片
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cms_content_comment.IMAGE_URL
     *
     * @mbg.generated 2018-12-06 16:08:26
     */
    private String imageUrl;

    /**
     * Database Column Remarks:
     *   站点id
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cms_content_comment.SITE_ID
     *
     * @mbg.generated 2018-12-06 16:08:26
     */
    private String siteId;

    /**
     * Database Column Remarks:
     *   内容id
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cms_content_comment.CONTENT_ID
     *
     * @mbg.generated 2018-12-06 16:08:26
     */
    private String contentId;

    /**
     * Database Column Remarks:
     *   评论人用户id
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cms_content_comment.COMMONT_USER_ID
     *
     * @mbg.generated 2018-12-06 16:08:26
     */
    private String commontUserId;

    /**
     * Database Column Remarks:
     *   状态，draft=草稿，audit=已通过/已发布
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cms_content_comment.STATUS
     *
     * @mbg.generated 2018-12-06 16:08:26
     */
    private String status;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public String getCommontUserId() {
        return commontUserId;
    }

    public void setCommontUserId(String commontUserId) {
        this.commontUserId = commontUserId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public com.feihua.framework.cms.api.ApiCmsContentCommentPoService service() {
        return com.feihua.utils.spring.SpringContextHolder.getBean(com.feihua.framework.cms.api.ApiCmsContentCommentPoService.class);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", title=").append(title);
        sb.append(", content=").append(content);
        sb.append(", imageUrl=").append(imageUrl);
        sb.append(", siteId=").append(siteId);
        sb.append(", contentId=").append(contentId);
        sb.append(", commontUserId=").append(commontUserId);
        sb.append(", status=").append(status);
        sb.append("]");
        return sb.toString();
    }
}