package com.feihua.framework.cms.impl;

import com.feihua.framework.cms.api.ApiCmsContentPoService;
import com.feihua.framework.cms.dto.CmsContentDto;
import com.feihua.framework.cms.po.CmsContentPo;
import com.github.pagehelper.Page;
import feihua.jdbc.api.pojo.BasePo;
import feihua.jdbc.api.pojo.PageResultDto;
import feihua.jdbc.api.service.impl.ApiBaseServiceImpl;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * This class was generated by MyBatis Generator.
 * @author yangwei 2018-11-09 16:57:40
 */
@Service
public class ApiCmsContentPoServiceImpl extends ApiBaseServiceImpl<CmsContentPo, CmsContentDto, String> implements ApiCmsContentPoService {
    @Autowired
    com.feihua.framework.cms.mapper.CmsContentPoMapper CmsContentPoMapper;

    public ApiCmsContentPoServiceImpl() {
        super(CmsContentDto.class);
    }

    @Override
    public PageResultDto<CmsContentDto> searchCmsContentsDsf(com.feihua.framework.cms.dto.SearchCmsContentsConditionDto dto, feihua.jdbc.api.pojo.PageAndOrderbyParamDto pageAndOrderbyParamDto) {
        Page p = super.pageAndOrderbyStart(pageAndOrderbyParamDto);
        List<com.feihua.framework.cms.dto.CmsContentDto> list = this.wrapDtos(CmsContentPoMapper.searchCmsContents(dto));
        return new PageResultDto(list, this.wrapPage(p));
    }

    @Override
    public CmsContentPo selectByContentIdAndChannelIdAndSiteId(String contentId, String channelId, String siteId) {
        if (StringUtils.isAnyEmpty(contentId,channelId,siteId)){
            return null;
        }
        CmsContentPo cmsContentPo = new CmsContentPo();
        cmsContentPo.setId(contentId);
        cmsContentPo.setSiteId(siteId);
        cmsContentPo.setChannelId(channelId);
        cmsContentPo.setDelFlag(BasePo.YesNo.N.name());
        return this.selectOneSimple(cmsContentPo);
    }

    @Override
    public CmsContentDto wrapDto(CmsContentPo po) {
        if (po == null) { return null; }
        CmsContentDto dto = new CmsContentDto();
        dto.setId(po.getId());
        dto.setTitle(po.getTitle());
        dto.setAuthor(po.getAuthor());
        dto.setStatus(po.getStatus());
        dto.setSiteId(po.getSiteId());
        dto.setChannelId(po.getChannelId());
        dto.setPv(po.getPv());
        dto.setIv(po.getIv());
        dto.setUv(po.getUv());
        dto.setDataUserId(po.getDataUserId());
        dto.setDataOfficeId(po.getDataOfficeId());
        dto.setDataType(po.getDataType());
        dto.setDataAreaId(po.getDataAreaId());
        dto.setUpdateAt(po.getUpdateAt());
        dto.setContent(po.getContent());
        dto.setTemplate(po.getTemplate());
        return dto;
    }
}