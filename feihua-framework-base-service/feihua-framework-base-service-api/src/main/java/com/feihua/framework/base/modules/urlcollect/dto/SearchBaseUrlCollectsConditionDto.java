package com.feihua.framework.base.modules.urlcollect.dto;

import feihua.jdbc.api.pojo.BaseConditionDto;
import java.util.Date;

/**
 * This class was generated by MyBatis Generator.
 * This class corresponds to the database table base_url_collect
 *
 * @mbg.generated 2018-10-26 19:12:51
*/
public class SearchBaseUrlCollectsConditionDto extends BaseConditionDto {

    private String userId;

    private String url;

    private String urlType;

    private String name;

    private String remark;


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrlType() {
        return urlType;
    }

    public void setUrlType(String urlType) {
        this.urlType = urlType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}