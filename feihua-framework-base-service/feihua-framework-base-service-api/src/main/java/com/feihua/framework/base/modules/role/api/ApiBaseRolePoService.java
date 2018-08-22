package com.feihua.framework.base.modules.role.api;

import com.feihua.framework.base.modules.role.dto.BaseRoleDto;
import com.feihua.framework.base.modules.role.dto.SearchRolesConditionDto;
import com.feihua.framework.base.modules.role.po.BaseRolePo;
import feihua.jdbc.api.pojo.PageAndOrderbyParamDto;
import feihua.jdbc.api.pojo.PageResultDto;

/**
 * This class was generated by MyBatis Generator.
 * @author yangwei 2017-11-18 19:01:48
 */
public interface ApiBaseRolePoService extends feihua.jdbc.api.service.ApiBaseTreeService<BaseRolePo, BaseRoleDto, String> {

    /**
     * 搜索角色
     * @param dto
     * @return
     */
    public PageResultDto<BaseRoleDto> searchRolesDsf(SearchRolesConditionDto dto, PageAndOrderbyParamDto pageAndOrderbyParamDto);

    /**
     * 根据用户获取用户的角色，目前一个用户只有一个角色
     * @param userId
     * @return
     */
    public BaseRoleDto selectByUserId(String userId);

    /**
     * 根据角色编码查询，编码唯一
     * @param code
     * @return
     */
    public BaseRoleDto selectByCode(String code);

}