package com.feihua.framework.base.modules.office.api;

import com.feihua.framework.base.modules.office.dto.BaseOfficeDto;
import com.feihua.framework.base.modules.office.dto.SearchOfficesConditionDto;
import com.feihua.framework.base.modules.office.po.BaseOfficePo;
import feihua.jdbc.api.pojo.PageAndOrderbyParamDto;
import feihua.jdbc.api.pojo.PageResultDto;

import java.util.List;

/**
 * This class was generated by MyBatis Generator.
 * @author yangwei 2018-01-05 15:51:59
 */
public interface ApiBaseOfficePoService extends feihua.jdbc.api.service.ApiBaseTreeService<BaseOfficePo, BaseOfficeDto, String> {

    /**
     * 根据用户的id查询用户所在的机构
     * @param userId
     * @return
     */
    public BaseOfficeDto selectOfficeByUserId(String userId);
    /**
     * 根据角色的id查询角色所在的机构
     * @param roleId
     * @return
     */
    public BaseOfficeDto selectOfficeByRoleId(String roleId,boolean includeDisabledRole);
    /**
     * 根据岗位的id查询岗位所在的机构
     * @param postId
     * @param includeDisabledPost true=包含禁用的岗位，false=不包含禁用的岗位
     * @return
     */
    public BaseOfficeDto selectOfficeByPostId(String postId,boolean includeDisabledPost);
    /**
     * 根据用户分组的id查询用户分组所在的机构
     * @param userGroupId
     * @param includeDisabledUserGroup true=包含禁用的岗位，false=不包含禁用的岗位
     * @return
     */
    public BaseOfficeDto selectOfficeByUserGroupId(String userGroupId,boolean includeDisabledUserGroup);

    /**
     * 查找所在公司
     * @param officeId
     * @return 返回 officeId 最近的公司
     */
    public BaseOfficeDto selectParentCompany(String officeId,boolean includeSelf);
    /**
     * 模糊查询机构信息
     * @param dto
     * @param pageAndOrderbyParamDto
     * @return
     */
    public PageResultDto<BaseOfficeDto> searchOfficesDsf(SearchOfficesConditionDto dto, PageAndOrderbyParamDto pageAndOrderbyParamDto);
}