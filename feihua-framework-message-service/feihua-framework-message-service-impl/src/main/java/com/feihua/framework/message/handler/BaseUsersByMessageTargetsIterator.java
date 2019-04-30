package com.feihua.framework.message.handler;

import com.feihua.framework.base.modules.rel.api.ApiBaseUserRoleRelPoService;
import com.feihua.framework.base.modules.rel.dto.BaseUserRoleRelDto;
import com.feihua.framework.base.modules.user.api.ApiBaseUserPoService;
import com.feihua.framework.base.modules.user.dto.SearchUsersConditionDsfDto;
import com.feihua.framework.base.modules.user.po.BaseUserPo;
import com.feihua.framework.constants.DictEnum;
import com.feihua.utils.spring.SpringContextHolder;
import feihua.jdbc.api.pojo.PageAndOrderbyParamDto;
import feihua.jdbc.api.pojo.PageResultDto;
import feihua.jdbc.api.service.impl.AbstractPageIteratorImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangwei
 * Created at 2018/11/1 20:49
 */
public class BaseUsersByMessageTargetsIterator extends AbstractPageIteratorImpl<BaseUserPo> {

    private String targetType;
    private List<String> targetValues;
    
    private String officeSelfCondition;
    
    private List<String> roleBindUserIds;
    public BaseUsersByMessageTargetsIterator(int pageNo, int pageSize, String targetType, List<String> targetValues){
        super(pageNo,pageSize);
        this.targetType = targetType;
        this.targetValues = targetValues;
    }

    @Override
    public List<BaseUserPo> next() {
        ApiBaseUserPoService apiBaseUserPoService = SpringContextHolder.getBean(ApiBaseUserPoService.class);
        if (apiBaseUserPoService != null) {
            PageAndOrderbyParamDto pageAndOrderbyParamDto = new PageAndOrderbyParamDto();
            pageAndOrderbyParamDto.setPage(super.getPage());
            SearchUsersConditionDsfDto searchUsersConditionDsfDto = new SearchUsersConditionDsfDto();
            // 发送到所有人
            if (DictEnum.MessageTargetType.all.name().equals(targetType)){
                PageResultDto<BaseUserPo> userPoPageResultDto = apiBaseUserPoService.selectAllSimple(false,pageAndOrderbyParamDto);
                if (userPoPageResultDto != null) {
                    super.pageNoPlus(userPoPageResultDto.getPage());
                    return userPoPageResultDto.getData();
                }
            }
            // 多个人
            else if (DictEnum.MessageTargetType.multi_people.name().equals(targetType)){
                if (targetValues != null && !targetValues.isEmpty()){
                    PageResultDto<BaseUserPo> userPoPageResultDto = apiBaseUserPoService.selectByPrimaryKeysSimple(targetValues,false,pageAndOrderbyParamDto);
                    if (userPoPageResultDto != null) {
                        super.pageNoPlus(userPoPageResultDto.getPage());
                        return userPoPageResultDto.getData();
                    }
                }
            }
            // 机构
            else if (DictEnum.MessageTargetType.multi_office.name().equals(targetType)){
                if (targetValues != null && !targetValues.isEmpty()){

                    if(officeSelfCondition == null){
                        StringBuffer sb = new StringBuffer("and (1!=1 ");

                        for (String officeId : targetValues) {
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
            else if (DictEnum.MessageTargetType.multi_role.name().equals(targetType)){
                if (targetValues != null && !targetValues.isEmpty()){
                    // 根据角色查询用户id
                    if (roleBindUserIds == null){
                        roleBindUserIds = new ArrayList<>();

                        ApiBaseUserRoleRelPoService apiBaseUserRoleRelPoService = SpringContextHolder.getBean(ApiBaseUserRoleRelPoService.class);

                        for (String roleId : targetValues) {
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
            else if (DictEnum.MessageTargetType.multi_area.name().equals(targetType)){
                if (targetValues != null && !targetValues.isEmpty()){

                    if(officeSelfCondition == null){
                        StringBuffer sb = new StringBuffer("and (1!=1 ");

                        for (String areaId : targetValues) {
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
