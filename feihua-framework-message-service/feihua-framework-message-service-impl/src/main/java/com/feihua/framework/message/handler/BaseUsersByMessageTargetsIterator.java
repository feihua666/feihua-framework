package com.feihua.framework.message.handler;

import com.feihua.framework.base.modules.office.api.ApiBaseOfficePoService;
import com.feihua.framework.base.modules.office.po.BaseOfficePo;
import com.feihua.framework.base.modules.rel.api.ApiBaseUserRoleRelPoService;
import com.feihua.framework.base.modules.rel.dto.BaseUserRoleRelDto;
import com.feihua.framework.base.modules.role.api.ApiBaseRolePoService;
import com.feihua.framework.base.modules.role.po.BaseRolePo;
import com.feihua.framework.base.modules.user.api.ApiBaseUserPoService;
import com.feihua.framework.base.modules.user.dto.SearchUsersConditionDsfDto;
import com.feihua.framework.base.modules.user.po.BaseUserPo;
import com.feihua.framework.constants.DictEnum;
import com.feihua.utils.spring.SpringContextHolder;
import feihua.jdbc.api.pojo.BasePo;
import feihua.jdbc.api.pojo.PageAndOrderbyParamDto;
import feihua.jdbc.api.pojo.PageResultDto;
import feihua.jdbc.api.service.impl.ApiPageIteratorAbstractImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangwei
 * Created at 2018/11/1 20:49
 */
public class BaseUsersByMessageTargetsIterator extends ApiPageIteratorAbstractImpl<BaseUserPo> {

    private String targets;
    private List<String> targetsValue;
    
    private String officeSelfCondition;
    
    private List<String> roleBindUserIds;
    public BaseUsersByMessageTargetsIterator(int pageNo, int pageSize, String targets, List<String> targetsValue){
        super(pageNo,pageSize);
        this.targets = targets;
        this.targetsValue = targetsValue;
    }

    @Override
    public List<BaseUserPo> next() {
        ApiBaseUserPoService apiBaseUserPoService = SpringContextHolder.getBean(ApiBaseUserPoService.class);
        if (apiBaseUserPoService != null) {
            PageAndOrderbyParamDto pageAndOrderbyParamDto = new PageAndOrderbyParamDto();
            pageAndOrderbyParamDto.setPage(super.getPage());
            SearchUsersConditionDsfDto searchUsersConditionDsfDto = new SearchUsersConditionDsfDto();
            // 发送到所有人
            if (DictEnum.MessageTargets.all.name().equals(targets)){
                PageResultDto<BaseUserPo> userPoPageResultDto = apiBaseUserPoService.selectAllSimple(false,pageAndOrderbyParamDto);
                if (userPoPageResultDto != null) {
                    super.pageNoPlus(userPoPageResultDto.getPage());
                    return userPoPageResultDto.getData();
                }
            }
            // 多个人
            else if (DictEnum.MessageTargets.multi_people.name().equals(targets)){
                if (targetsValue != null && !targetsValue.isEmpty()){
                    PageResultDto<BaseUserPo> userPoPageResultDto = apiBaseUserPoService.selectByPrimaryKeysSimple(targetsValue,false,pageAndOrderbyParamDto);
                    if (userPoPageResultDto != null) {
                        super.pageNoPlus(userPoPageResultDto.getPage());
                        return userPoPageResultDto.getData();
                    }
                }
            }
            // 机构
            else if (DictEnum.MessageTargets.multi_office.name().equals(targets)){
                if (targetsValue != null && !targetsValue.isEmpty()){

                    if(officeSelfCondition == null){
                        StringBuffer sb = new StringBuffer("and (1!=1 ");

                        for (String officeId : targetsValue) {
                            sb.append(" or data_office_id = '").append(officeId).append("'");
                        }
                        officeSelfCondition = sb.toString();
                    }

                    if (officeSelfCondition != null){
                        searchUsersConditionDsfDto.setSelfCondition(officeSelfCondition);
                        PageResultDto<BaseUserPo> userPoPageResultDto = apiBaseUserPoService.searchBaseUsers(searchUsersConditionDsfDto,pageAndOrderbyParamDto);
                        if (userPoPageResultDto != null) {
                            super.pageNoPlus(userPoPageResultDto.getPage());
                            return userPoPageResultDto.getData();
                        }
                    }
                }
            }
            // 角色
            else if (DictEnum.MessageTargets.multi_role.name().equals(targets)){
                if (targetsValue != null && !targetsValue.isEmpty()){
                    // 根据角色查询用户id
                    if (roleBindUserIds == null){
                        roleBindUserIds = new ArrayList<>();

                        ApiBaseUserRoleRelPoService apiBaseUserRoleRelPoService = SpringContextHolder.getBean(ApiBaseUserRoleRelPoService.class);

                        for (String roleId : targetsValue) {
                            List<BaseUserRoleRelDto> userRoleRelDtos = apiBaseUserRoleRelPoService.selectByRoleId(roleId);
                            if (userRoleRelDtos != null) {
                                for (BaseUserRoleRelDto userRoleRelDto : userRoleRelDtos) {
                                    roleBindUserIds.add(userRoleRelDto.getUserId());
                                }
                            }
                        }

                    }
                    if (roleBindUserIds != null) {
                        PageResultDto<BaseUserPo> userPoPageResultDto = apiBaseUserPoService.selectByPrimaryKeysSimple(roleBindUserIds,false,pageAndOrderbyParamDto);
                        if (userPoPageResultDto != null) {
                            super.pageNoPlus(userPoPageResultDto.getPage());
                            return userPoPageResultDto.getData();
                        }
                    }

                }
            }
            // 区域
            else if (DictEnum.MessageTargets.multi_area.name().equals(targets)){
                if (targetsValue != null && !targetsValue.isEmpty()){

                    if(officeSelfCondition == null){
                        StringBuffer sb = new StringBuffer("and (1!=1 ");

                        for (String areaId : targetsValue) {
                            sb.append(" or data_area_id = '").append(areaId).append("'");
                        }
                        officeSelfCondition = sb.toString();
                    }

                    if (officeSelfCondition != null){
                        searchUsersConditionDsfDto.setSelfCondition(officeSelfCondition);
                        PageResultDto<BaseUserPo> userPoPageResultDto = apiBaseUserPoService.searchBaseUsers(searchUsersConditionDsfDto,pageAndOrderbyParamDto);
                        if (userPoPageResultDto != null) {
                            super.pageNoPlus(userPoPageResultDto.getPage());
                            return userPoPageResultDto.getData();
                        }
                    }
                }
            }
        }
        return null;
    }
}
