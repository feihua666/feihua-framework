package com.feihua.framework.cms.admin.rest.dto;

import java.util.List;

/**
 * This class corresponds to the database table cms_content_library
 * 2018-12-07 09:19:31
*/
public class AddCmsContentLibraryFormDto {

    private String filename;

    private String url;

    private String ext;

    private String type;

    private String size;

    private String description;

    private String imageUrl;

    private String imageDes;

    private Integer sequence;
    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }
}