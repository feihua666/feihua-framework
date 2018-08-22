package com.feihua.framework.rest.modules.dict.dto;

import com.feihua.framework.rest.modules.common.dto.UpdateFormDto;

/**
 * 字典修改用表单dto
 * Created by yangwei
 * Created at 2017/12/21 17:39
 */
public class UpdateDictFormDto extends UpdateFormDto {
    private String value;
    private String name;
    private String type;
    private String discription;
    private Integer sequence;
    private String isPublic;
    private String isSystem;
    private String dataOfficeId;
    private String dataAreaId;
    private String parentId;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDiscription() {
        return discription;
    }

    public void setDiscription(String discription) {
        this.discription = discription;
    }

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    public String getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(String isPublic) {
        this.isPublic = isPublic;
    }

    public String getIsSystem() {
        return isSystem;
    }

    public void setIsSystem(String isSystem) {
        this.isSystem = isSystem;
    }

    public String getDataOfficeId() {
        return dataOfficeId;
    }

    public void setDataOfficeId(String dataOfficeId) {
        this.dataOfficeId = dataOfficeId;
    }

    public String getDataAreaId() {
        return dataAreaId;
    }

    public void setDataAreaId(String dataAreaId) {
        this.dataAreaId = dataAreaId;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }
}
