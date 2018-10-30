package com.feihua.framework.base.impl;

import com.feihua.framework.base.modules.message.api.ApiBaseMessageTemplatePoService;
import com.feihua.framework.base.modules.message.dto.BaseMessageTemplateDto;
import com.feihua.framework.base.modules.message.po.BaseMessageTemplatePo;
import com.github.pagehelper.Page;
import feihua.jdbc.api.pojo.PageResultDto;
import feihua.jdbc.api.service.impl.ApiBaseServiceImpl;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * This class was generated by MyBatis Generator.
 * @author yangwei 2018-10-29 20:18:23
 */
@Service
public class ApiBaseMessageTemplatePoServiceImpl extends ApiBaseServiceImpl<BaseMessageTemplatePo, BaseMessageTemplateDto, String> implements ApiBaseMessageTemplatePoService {
    @Autowired
    com.feihua.framework.base.mapper.BaseMessageTemplatePoMapper BaseMessageTemplatePoMapper;

    public ApiBaseMessageTemplatePoServiceImpl() {
        super(BaseMessageTemplateDto.class);
    }

    @Override
    public PageResultDto<BaseMessageTemplateDto> searchBaseMessageTemplatesDsf(com.feihua.framework.base.modules.message.dto.SearchBaseMessageTemplatesConditionDto dto, feihua.jdbc.api.pojo.PageAndOrderbyParamDto pageAndOrderbyParamDto) {
        Page p = super.pageAndOrderbyStart(pageAndOrderbyParamDto);
        List<com.feihua.framework.base.modules.message.dto.BaseMessageTemplateDto> list = this.wrapDtos(BaseMessageTemplatePoMapper.searchBaseMessageTemplates(dto));
        return new PageResultDto(list, this.wrapPage(p));
    }

    @Override
    public BaseMessageTemplateDto wrapDto(BaseMessageTemplatePo po) {
        if (po == null) {
            return null;
        }
        BaseMessageTemplateDto baseMessageTemplateDto = new BaseMessageTemplateDto();
        baseMessageTemplateDto.setName(po.getName());
        baseMessageTemplateDto.setCode(po.getCode());
        baseMessageTemplateDto.setDataOfficeId(po.getDataOfficeId());
        baseMessageTemplateDto.setDataUserId(po.getDataUserId());
        baseMessageTemplateDto.setDataAreaId(po.getDataAreaId());
        baseMessageTemplateDto.setId(po.getId());
        baseMessageTemplateDto.setUpdateAt(po.getUpdateAt());
        baseMessageTemplateDto.setContent(po.getContent());
        baseMessageTemplateDto.setDataType(po.getDataType());
        return baseMessageTemplateDto;
    }
}