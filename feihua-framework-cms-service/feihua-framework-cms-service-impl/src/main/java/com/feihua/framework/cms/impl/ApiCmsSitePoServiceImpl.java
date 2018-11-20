package com.feihua.framework.cms.impl;

import com.feihua.framework.cms.api.ApiCmsSitePoService;
import com.feihua.framework.cms.dto.CmsSiteDto;
import com.feihua.framework.cms.po.CmsSitePo;
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
 * @author yangwei 2018-11-09 16:15:06
 */
@Service
public class ApiCmsSitePoServiceImpl extends ApiBaseServiceImpl<CmsSitePo, CmsSiteDto, String> implements ApiCmsSitePoService {
    @Autowired
    com.feihua.framework.cms.mapper.CmsSitePoMapper CmsSitePoMapper;

    public ApiCmsSitePoServiceImpl() {
        super(CmsSiteDto.class);
    }

    @Override
    public PageResultDto<CmsSiteDto> searchCmsSitesDsf(com.feihua.framework.cms.dto.SearchCmsSitesConditionDto dto, feihua.jdbc.api.pojo.PageAndOrderbyParamDto pageAndOrderbyParamDto) {
        Page p = super.pageAndOrderbyStart(pageAndOrderbyParamDto);
        List<com.feihua.framework.cms.dto.CmsSiteDto> list = this.wrapDtos(CmsSitePoMapper.searchCmsSites(dto));
        return new PageResultDto(list, this.wrapPage(p));
    }

    @Override
    public CmsSitePo selecltMainSiteByDomain(String domain) {

        CmsSitePo cmsSitePo = new CmsSitePo();
        cmsSitePo.setDelFlag(BasePo.YesNo.N.name());
        if(StringUtils.isEmpty(domain)) return null;
        cmsSitePo.setDomain(domain);
        cmsSitePo.setIsMain(BasePo.YesNo.Y.name());
        return this.selectOneSimple(cmsSitePo);
    }

    @Override
    public List<CmsSitePo> selecltByDomain(String domain) {
        CmsSitePo cmsSitePo = new CmsSitePo();
        cmsSitePo.setDelFlag(BasePo.YesNo.N.name());
        if(StringUtils.isEmpty(domain)) return null;
        cmsSitePo.setDomain(domain);
        return this.selectListSimple(cmsSitePo);
    }

    @Override
    public CmsSitePo selecltByPath(String contextPath) {
        CmsSitePo cmsSitePo = new CmsSitePo();
        cmsSitePo.setDelFlag(BasePo.YesNo.N.name());
        if(StringUtils.isEmpty(contextPath)) return null;
        cmsSitePo.setPath(contextPath);
        return this.selectOneSimple(cmsSitePo);
    }

    @Override
    public CmsSiteDto wrapDto(CmsSitePo po) {
        if (po == null) { return null; }
        CmsSiteDto dto = new CmsSiteDto();
        dto.setId(po.getId());
        dto.setName(po.getName());
        dto.setDomain(po.getDomain());
        dto.setPath(po.getPath());
        dto.setDataUserId(po.getDataUserId());
        dto.setDataOfficeId(po.getDataOfficeId());
        dto.setDataType(po.getDataType());
        dto.setDataAreaId(po.getDataAreaId());
        dto.setUpdateAt(po.getUpdateAt());
        dto.setTemplate(po.getTemplate());
        dto.setTemplatePath(po.getTemplatePath());
        dto.setStaticPath(po.getStaticPath());
        return dto;
    }
}