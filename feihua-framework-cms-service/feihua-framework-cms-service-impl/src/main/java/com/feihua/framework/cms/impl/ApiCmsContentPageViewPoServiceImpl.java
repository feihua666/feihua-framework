package com.feihua.framework.cms.impl;

import com.feihua.framework.cms.api.ApiCmsContentPageViewPoService;
import com.feihua.framework.cms.dto.CmsContentPageViewDto;
import com.feihua.framework.cms.po.CmsContentPageViewPo;
import feihua.jdbc.api.service.impl.ApiBaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * This class was generated by MyBatis Generator.
 * @author yangwei 2018-11-09 17:01:20
 */
@Service
public class ApiCmsContentPageViewPoServiceImpl extends ApiBaseServiceImpl<CmsContentPageViewPo, CmsContentPageViewDto, String> implements ApiCmsContentPageViewPoService {
    @Autowired
    com.feihua.framework.cms.mapper.CmsContentPageViewPoMapper CmsContentPageViewPoMapper;

    public ApiCmsContentPageViewPoServiceImpl() {
        super(CmsContentPageViewDto.class);
    }

    @Override
    public CmsContentPageViewDto wrapDto(CmsContentPageViewPo po) {
        if (po == null) { return null; }
        CmsContentPageViewDto dto = new CmsContentPageViewDto();
        dto.setId(po.getId());
        dto.setContentId(po.getContentId());
        dto.setSiteId(po.getSiteId());
        dto.setCookie(po.getCookie());
        dto.setHost(po.getHost());
        dto.setDataUserId(po.getDataUserId());
        dto.setDataOfficeId(po.getDataOfficeId());
        dto.setDataType(po.getDataType());
        dto.setDataAreaId(po.getDataAreaId());
        dto.setUpdateAt(po.getUpdateAt());
        return dto;
    }
}