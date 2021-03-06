package com.feihua.framework.rest.modules.postjob.dto;

import com.feihua.framework.rest.dto.UpdateFormDto;
import java.util.Date;

/**
 * This class was generated by MyBatis Generator.
 * This class corresponds to the database table base_post_job
 *
 * @mbg.generated 2019-06-06 14:16:20
*/
public class UpdateBasePostJobFormDto extends UpdateFormDto {
    private String name;

    private String code;

    private String type;

    private String isPublic;

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

    public String getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(String isPublic) {
        this.isPublic = isPublic;
    }

    public String getDataOfficeId() {
        return dataOfficeId;
    }

    public void setDataOfficeId(String dataOfficeId) {
        this.dataOfficeId = dataOfficeId;
    }
}