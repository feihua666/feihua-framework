package com.feihua.framework.base.impl;

import com.feihua.exception.BaseException;
import com.feihua.framework.base.modules.config.api.ApiBaseConfigService;
import com.feihua.framework.base.modules.config.dto.BaseConfigDto;
import com.feihua.framework.base.modules.config.po.BaseConfig;
import com.github.pagehelper.Page;
import com.google.gson.Gson;
import feihua.jdbc.api.pojo.BasePo;
import feihua.jdbc.api.pojo.PageResultDto;
import feihua.jdbc.api.service.impl.ApiBaseServiceImpl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * This class was generated by MyBatis Generator.
 *
 * @author revolver 2019-03-12 15:01:39
 */
@Service
public class ApiBaseConfigServiceImpl extends ApiBaseServiceImpl<BaseConfig, BaseConfigDto, String> implements ApiBaseConfigService {
    @Autowired
    com.feihua.framework.base.mapper.BaseConfigMapper BaseConfigMapper;

    public ApiBaseConfigServiceImpl() {
        super(BaseConfigDto.class);
    }

    @Override
    public <T> T getConfigObject(String key, Class<T> clazz) {
        String value = null;
        BaseConfig config = selectByConfigKey(key);
        if (config != null) {
            value = config.getConfigValue();
        }

        if (StringUtils.isNotBlank(value)) {
            return new Gson().fromJson(value, clazz);
        }

        try {
            return clazz.newInstance();
        } catch (Exception e) {
            throw new BaseException("获取参数失败");
        }
    }

    @Override
    public BaseConfig selectByConfigKey(String key) {
        if (org.apache.commons.lang3.StringUtils.isEmpty(key)) return null;
        BaseConfig query = new BaseConfig();
        query.setDelFlag(BasePo.YesNo.N.name());
        query.setConfigKey(key);
        return selectOneSimple(query);
    }

    @Override
    public PageResultDto<BaseConfigDto> searchBaseConfigsDsf(com.feihua.framework.base.modules.config.dto.SearchBaseConfigsConditionDto dto, feihua.jdbc.api.pojo.PageAndOrderbyParamDto pageAndOrderbyParamDto) {
        Page p = super.pageAndOrderbyStart(pageAndOrderbyParamDto);
        List<com.feihua.framework.base.modules.config.dto.BaseConfigDto> list = this.wrapDtos(BaseConfigMapper.searchBaseConfigs(dto));
        return new PageResultDto(list, this.wrapPage(p));
    }

    @Override
    public BaseConfigDto wrapDto(BaseConfig po) {
        if (po == null) {
            return null;
        }
        BaseConfigDto dto = new BaseConfigDto();
        dto.setId(po.getId());
        dto.setConfigKey(po.getConfigKey());
        dto.setConfigValue(po.getConfigValue());
        dto.setDescription(po.getDescription());
        dto.setStatus(po.getStatus());
        dto.setDataUserId(po.getDataUserId());
        dto.setDataOfficeId(po.getDataOfficeId());
        dto.setDataType(po.getDataType());
        dto.setDataAreaId(po.getDataAreaId());
        dto.setUpdateAt(po.getUpdateAt());
        return dto;
    }
}