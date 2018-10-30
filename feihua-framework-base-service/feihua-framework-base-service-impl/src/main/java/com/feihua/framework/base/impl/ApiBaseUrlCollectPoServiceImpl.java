package com.feihua.framework.base.impl;

import com.feihua.framework.base.modules.urlcollect.api.ApiBaseUrlCollectPoService;
import com.feihua.framework.base.modules.urlcollect.dto.BaseUrlCollectDto;
import com.feihua.framework.base.modules.urlcollect.po.BaseUrlCollectPo;
import com.github.pagehelper.Page;
import feihua.jdbc.api.pojo.PageResultDto;
import feihua.jdbc.api.service.impl.ApiBaseServiceImpl;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * This class was generated by MyBatis Generator.
 * @author yangwei 2018-10-26 19:12:51
 */
@Service
public class ApiBaseUrlCollectPoServiceImpl extends ApiBaseServiceImpl<BaseUrlCollectPo, BaseUrlCollectDto, String> implements ApiBaseUrlCollectPoService {
    @Autowired
    com.feihua.framework.base.mapper.BaseUrlCollectPoMapper BaseUrlCollectPoMapper;

    public ApiBaseUrlCollectPoServiceImpl() {
        super(BaseUrlCollectDto.class);
    }

    @Override
    public PageResultDto<BaseUrlCollectDto> searchBaseUrlCollectsDsf(com.feihua.framework.base.modules.urlcollect.dto.SearchBaseUrlCollectsConditionDto dto, feihua.jdbc.api.pojo.PageAndOrderbyParamDto pageAndOrderbyParamDto) {
        Page p = super.pageAndOrderbyStart(pageAndOrderbyParamDto);
        List<com.feihua.framework.base.modules.urlcollect.dto.BaseUrlCollectDto> list = this.wrapDtos(BaseUrlCollectPoMapper.searchBaseUrlCollects(dto));
        return new PageResultDto(list, this.wrapPage(p));
    }

    @Override
    public BaseUrlCollectDto wrapDto(BaseUrlCollectPo po) {
        if (po == null) {
            return null;
        }
        BaseUrlCollectDto baseUrlCollectDto = new BaseUrlCollectDto();
        baseUrlCollectDto.setName(po.getName());
        baseUrlCollectDto.setUrl(po.getUrl());
        baseUrlCollectDto.setIcon(po.getIcon());
        baseUrlCollectDto.setDataOfficeId(po.getDataOfficeId());
        baseUrlCollectDto.setDataType(po.getDataType());
        baseUrlCollectDto.setDataUserId(po.getDataUserId());
        baseUrlCollectDto.setDataAreaId(po.getDataAreaId());
        baseUrlCollectDto.setIconType(po.getIconType());
        baseUrlCollectDto.setId(po.getId());
        baseUrlCollectDto.setRemark(po.getRemark());
        baseUrlCollectDto.setUrlType(po.getUrlType());
        baseUrlCollectDto.setUpdateAt(po.getUpdateAt());
        return baseUrlCollectDto;
    }
}