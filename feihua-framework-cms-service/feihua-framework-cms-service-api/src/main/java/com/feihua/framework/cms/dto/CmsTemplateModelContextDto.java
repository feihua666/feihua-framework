package com.feihua.framework.cms.dto;

/**
 * Created by yangwei
 * Created at 2018/11/21 18:32
 */
public class CmsTemplateModelContextDto {

    public CmsTemplateModelContextDto(boolean dynamic) {
        this.dynamic = dynamic;
    }

    /**
     * 是否动态页
     */
    private boolean dynamic = true;

    public boolean isDynamic() {
        return dynamic;
    }

    public void setDynamic(boolean dynamic) {
        this.dynamic = dynamic;
    }
}
