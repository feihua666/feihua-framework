package com.feihua.framework.base.modules.role.api;

import com.feihua.framework.base.modules.role.dto.BaseRoleDto;
import com.feihua.framework.base.modules.role.dto.SearchRolesConditionDto;
import com.feihua.framework.base.modules.role.po.BaseRolePo;
import feihua.jdbc.api.pojo.PageAndOrderbyParamDto;
import feihua.jdbc.api.pojo.PageResultDto;

import java.util.List;

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
     * 根据用户获取用户的角色
     * @param userId
     * @return
     */
    public List<BaseRoleDto> selectByUserId(String userId,boolean includeDisabled);

    /**
     * 根据角色编码查询，编码唯一
     * @param code
     * @return
     */
    public BaseRoleDto selectByCode(String code);
    /**
     * 根据岗位获取用户的角色
     * @param postId
     * @return
     */
    public BaseRoleDto selectByPostId(String postId);
}