package com.feihua.framework.base.modules.postjob.dto;

import feihua.jdbc.api.pojo.BaseConditionDto;
import java.util.Date;

/**
 * This class was generated by MyBatis Generator.
 * This class corresponds to the database table base_post
 *
 * @mbg.generated 2019-06-06 11:05:12
*/
public class SearchBasePostsConditionDto extends BaseConditionDto {

    private String name;

    private String code;

    private String type;

    private String postJobId;

    private String disabled;

    private String isPublic;

    private String parentId;

    private String dataOfficeId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPostJobId() {
        return postJobId;
    }

    public void setPostJobId(String postJobId) {
        this.postJobId = postJobId;
    }

    public String getDisabled() {
        return disabled;
    }

    public void setDisabled(String disabled) {
        this.disabled = disabled;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getDataOfficeId() {
        return dataOfficeId;
    }

    public void setDataOfficeId(String dataOfficeId) {
        this.dataOfficeId = dataOfficeId;
    }

    public String getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(String isPublic) {
        this.isPublic = isPublic;
    }
}