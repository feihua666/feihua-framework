package com.feihua.framework.cms.dto;

import com.feihua.framework.cms.CmsConstants;
import com.feihua.utils.io.FileUtils;
import feihua.jdbc.api.pojo.BaseDto;

import java.util.Date;

import static com.feihua.framework.cms.CmsConstants.requestChannelPathPrefix;
import static com.feihua.framework.cms.CmsConstants.suffixHtm;
import static com.feihua.framework.cms.CmsConstants.suffixHtml;

/**
 * This class was generated by MyBatis Generator.
 * This class corresponds to the database table cms_channel
 *
 * @mbg.generated 2018-11-20 14:44:49
*/
public class CmsChannelTemplateModelDto extends BaseDto {

    public CmsChannelTemplateModelDto(CmsChannelDto channelDto,CmsTemplateModelContextDto contextDto) {
        this.id = channelDto.getId();
        this.name = channelDto.getName();
        this.path = channelDto.getPath();
        this.sequence = channelDto.getSequence();
        this.siteId = channelDto.getSiteId();
        this.channelType = channelDto.getChannelType();
        this.template = channelDto.getTemplate();
        this.pv = channelDto.getPv();
        this.iv = channelDto.getIv();
        this.uv = channelDto.getUv();
        this.level = channelDto.getLevel();
        this.parentId = channelDto.getParentId();
        this.dataUserId = channelDto.getDataUserId();
        this.dataOfficeId = channelDto.getDataOfficeId();
        this.dataType = channelDto.getDataType();
        this.dataAreaId = channelDto.getDataAreaId();
        this.delFlag = channelDto.getDelFlag();
        this.createAt = channelDto.getCreateAt();
        this.createBy = channelDto.getCreateBy();
        this.updateAt = channelDto.getUpdateAt();
        this.updateBy = channelDto.getUpdateBy();
        this.parentId1 = channelDto.getParentId1();
        this.parentId2 = channelDto.getParentId2();
        this.parentId3 = channelDto.getParentId3();
        this.parentId4 = channelDto.getParentId4();
        this.parentId5 = channelDto.getParentId5();
        this.parentId6 = channelDto.getParentId6();
        this.parentId7 = channelDto.getParentId7();
        this.parentId8 = channelDto.getParentId8();
        this.parentId9 = channelDto.getParentId9();
        this.parentId10 = channelDto.getParentId10();

        this.contextDto = contextDto;
    }

    private CmsTemplateModelContextDto contextDto;


    private String id;

    private String name;

    private String path;

    private Integer sequence;

    private String siteId;

    private String channelType;

    private String template;

    private Integer pv;

    private Integer iv;

    private Integer uv;

    private Integer level;

    private String parentId;

    private String parentId1;

    private String parentId2;

    private String parentId3;

    private String parentId4;

    private String parentId5;

    private String parentId6;

    private String parentId7;

    private String parentId8;

    private String parentId9;

    private String parentId10;

    private String dataUserId;

    private String dataOfficeId;

    private String dataType;

    private String dataAreaId;

    private String delFlag;

    private Date createAt;

    private String createBy;

    private Date updateAt;

    private String updateBy;


    // additional
    public String getChannelPath() {
        return requestChannelPathPrefix + FileUtils.slash +  this.id;
    }
    public String getSuffix() {
        return contextDto.isDynamic() ? suffixHtm : suffixHtml;
    }
    public String getChannelUrl(){
        return getChannelPath() + getSuffix();
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getChannelType() {
        return channelType;
    }

    public void setChannelType(String channelType) {
        this.channelType = channelType;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
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

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getParentId1() {
        return parentId1;
    }

    public void setParentId1(String parentId1) {
        this.parentId1 = parentId1;
    }

    public String getParentId2() {
        return parentId2;
    }

    public void setParentId2(String parentId2) {
        this.parentId2 = parentId2;
    }

    public String getParentId3() {
        return parentId3;
    }

    public void setParentId3(String parentId3) {
        this.parentId3 = parentId3;
    }

    public String getParentId4() {
        return parentId4;
    }

    public void setParentId4(String parentId4) {
        this.parentId4 = parentId4;
    }

    public String getParentId5() {
        return parentId5;
    }

    public void setParentId5(String parentId5) {
        this.parentId5 = parentId5;
    }

    public String getParentId6() {
        return parentId6;
    }

    public void setParentId6(String parentId6) {
        this.parentId6 = parentId6;
    }

    public String getParentId7() {
        return parentId7;
    }

    public void setParentId7(String parentId7) {
        this.parentId7 = parentId7;
    }

    public String getParentId8() {
        return parentId8;
    }

    public void setParentId8(String parentId8) {
        this.parentId8 = parentId8;
    }

    public String getParentId9() {
        return parentId9;
    }

    public void setParentId9(String parentId9) {
        this.parentId9 = parentId9;
    }

    public String getParentId10() {
        return parentId10;
    }

    public void setParentId10(String parentId10) {
        this.parentId10 = parentId10;
    }

    public String getDataUserId() {
        return dataUserId;
    }

    public void setDataUserId(String dataUserId) {
        this.dataUserId = dataUserId;
    }

    public String getDataOfficeId() {
        return dataOfficeId;
    }

    public void setDataOfficeId(String dataOfficeId) {
        this.dataOfficeId = dataOfficeId;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getDataAreaId() {
        return dataAreaId;
    }

    public void setDataAreaId(String dataAreaId) {
        this.dataAreaId = dataAreaId;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(Date updateAt) {
        this.updateAt = updateAt;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public CmsTemplateModelContextDto getContextDto() {
        return contextDto;
    }

    public void setContextDto(CmsTemplateModelContextDto contextDto) {
        this.contextDto = contextDto;
    }
}