package com.feihua.framework.rest.modules.area.dto;

import com.feihua.framework.rest.modules.common.dto.UpdateFormDto;

/**
 * Created by yangwei
 * Created at 2018/1/9 16:47
 */
public class UpdateAreaFormDto extends UpdateFormDto {
    private String name;

    private String type;

    private Integer sequence;
    private String parentId;

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

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }
}
