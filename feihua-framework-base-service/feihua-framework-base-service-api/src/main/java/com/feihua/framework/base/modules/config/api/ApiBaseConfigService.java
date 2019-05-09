package com.feihua.framework.base.modules.config.api;

import com.feihua.framework.base.modules.config.dto.BaseConfigDto;
import com.feihua.framework.base.modules.config.po.BaseConfig;
import feihua.jdbc.api.pojo.PageResultDto;

/**
 * This class was generated by MyBatis Generator.
 * @author revolver 2019-03-12 15:01:39
 */
public interface ApiBaseConfigService extends feihua.jdbc.api.service.ApiBaseService<BaseConfig, BaseConfigDto, String> {
    PageResultDto<BaseConfigDto> searchBaseConfigsDsf(com.feihua.framework.base.modules.config.dto.SearchBaseConfigsConditionDto dto, feihua.jdbc.api.pojo.PageAndOrderbyParamDto pageAndOrderbyParamDto);
    /**
     * 根据key，获取value的Object对象
     * @param key    key
     * @param clazz  Object对象
     */
    public <T> T getConfigObject(String key, Class<T> clazz);

    BaseConfig selectByConfigKey(String key);
}