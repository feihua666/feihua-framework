package com.feihua.framework.statistic.po;

import feihua.jdbc.api.pojo.BasePo;

/**
 * This class was generated by MyBatis Generator.
 * @author yangwei 2019-05-28 14:00:30
 * Database Table Remarks:
 *   页面分享记录表
 *
 * This class corresponds to the database table statistic_record_page_share
 * @mbg.generated do_not_delete_during_merge 2019-05-28 14:00:30
*/
public class StatisticRecordPageSharePo extends feihua.jdbc.api.pojo.BasePo<String> {
    /**
     * Database Column Remarks:
     *   页面地址
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column statistic_record_page_share.URL
     *
     * @mbg.generated 2019-05-28 14:00:30
     */
    private String url;

    /**
     * Database Column Remarks:
     *   用户id，标识是哪一个用户分享的
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column statistic_record_page_share.USER_ID
     *
     * @mbg.generated 2019-05-28 14:00:30
     */
    private String userId;

    /**
     * Database Column Remarks:
     *   用户昵称，冗余字段方便展示
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column statistic_record_page_share.USER_NICKNAME
     *
     * @mbg.generated 2019-05-28 14:00:30
     */
    private String userNickname;

    /**
     * Database Column Remarks:
     *   页面类型，标识是哪一类页面，如用户详情，活动详情等等
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column statistic_record_page_share.TYPE
     *
     * @mbg.generated 2019-05-28 14:00:30
     */
    private String type;

    /**
     * Database Column Remarks:
     *   内容的id，如用户详情就是用户的id
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column statistic_record_page_share.CONTENT_ID
     *
     * @mbg.generated 2019-05-28 14:00:30
     */
    private String contentId;

    /**
     * Database Column Remarks:
     *   内容的名称，如用户详情就是用户的昵称
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column statistic_record_page_share.CONTENT_NAME
     *
     * @mbg.generated 2019-05-28 14:00:30
     */
    private String contentName;

    /**
     * Database Column Remarks:
     *   分享到哪里，字典
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column statistic_record_page_share.SHARE_TO
     *
     * @mbg.generated 2019-05-28 14:00:30
     */
    private String shareTo;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserNickname() {
        return userNickname;
    }

    public void setUserNickname(String userNickname) {
        this.userNickname = userNickname;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public String getContentName() {
        return contentName;
    }

    public void setContentName(String contentName) {
        this.contentName = contentName;
    }

    public String getShareTo() {
        return shareTo;
    }

    public void setShareTo(String shareTo) {
        this.shareTo = shareTo;
    }

    public com.feihua.framework.statistic.api.ApiStatisticRecordPageSharePoService service() {
        return com.feihua.utils.spring.SpringContextHolder.getBean(com.feihua.framework.statistic.api.ApiStatisticRecordPageSharePoService.class);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", url=").append(url);
        sb.append(", userId=").append(userId);
        sb.append(", userNickname=").append(userNickname);
        sb.append(", type=").append(type);
        sb.append(", contentId=").append(contentId);
        sb.append(", contentName=").append(contentName);
        sb.append(", shareTo=").append(shareTo);
        sb.append("]");
        return sb.toString();
    }
}