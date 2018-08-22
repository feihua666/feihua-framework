package com.feihua.framework.base.impl;

import com.feihua.framework.base.modules.datascope.api.ApiBaseDataScopeService;
import com.feihua.framework.base.modules.datascope.dto.BaseDataScopeDataScopeDefineDto;
import com.feihua.framework.base.modules.office.dto.BaseOfficeDataScopeDefineDto;
import com.feihua.framework.base.modules.rel.api.ApiBaseRoleDataScopeRelPoService;
import com.feihua.framework.base.modules.rel.api.ApiBaseUserDataScopeRelPoService;
import com.feihua.framework.base.modules.rel.dto.BaseRoleDataScopeRelDto;
import com.feihua.framework.base.modules.rel.dto.BaseUserDataScopeRelDto;
import com.feihua.framework.base.modules.role.dto.BaseRoleDataScopeDefineDto;
import com.feihua.framework.base.modules.user.dto.BaseUserDataScopeDefineDto;
import com.feihua.framework.base.modules.user.po.BaseUserDataScopeDefinePo;
import com.feihua.utils.spring.SpringContextHolder;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 数据范围冲突检测服务
 * Created by yangwei
 * Created at 2018/3/25 14:33
 */
@Service
public class DataScopeConflictService {


    @Autowired
    private ApiBaseUserDataScopeRelPoService apiBaseUserDataScopeRelPoService;
    @Autowired
    private ApiBaseRoleDataScopeRelPoService apiBaseRoleDataScopeRelPoService;
    /**
     * 检测所有数据范围冲突
     * @param dataScopeIds
     */
    public void checkConflict(List<String> dataScopeIds){
        Map<String,ApiBaseDataScopeService> dataScopeServiceMap = SpringContextHolder.getApplicationContext().getBeansOfType(ApiBaseDataScopeService.class);
        Set<String> strings = dataScopeServiceMap.keySet();
        for (String string : strings) {
            dataScopeServiceMap.get(string).checkConflict(dataScopeIds);
        }
    }

    /**
     * 检测一个数据资源范围，是否跟设置的冲突
     * @param dataScopeId
     * @param apiBaseDataScopeService
     */
    public void checkConflict(String dataScopeId,ApiBaseDataScopeService apiBaseDataScopeService){
        if(dataScopeId == null) return;
        // 检查数据资源冲突
        // 用户是否有冲突
        List<BaseUserDataScopeRelDto> userDataScopeRelDtos = apiBaseUserDataScopeRelPoService.selectByDataScopeId(dataScopeId);
        if(CollectionUtils.isNotEmpty(userDataScopeRelDtos)){
            for (BaseUserDataScopeRelDto userDataScopeRelDto : userDataScopeRelDtos) {
                List<BaseUserDataScopeRelDto>  userDataScopeRelDtos1 = apiBaseUserDataScopeRelPoService.selectByUserId(userDataScopeRelDto.getUserId());
                if(CollectionUtils.isNotEmpty(userDataScopeRelDtos1)){
                    List<String> dataScopeIds = new ArrayList<>();
                    for (BaseUserDataScopeRelDto baseUserDataScopeRelDto : userDataScopeRelDtos1) {
                        dataScopeIds.add(baseUserDataScopeRelDto.getDataScopeId());
                    }
                    apiBaseDataScopeService.checkConflict(dataScopeIds);
                }
            }

        }
        // 角色是否有冲突
        List<BaseRoleDataScopeRelDto> roleDataScopeRelDtos = apiBaseRoleDataScopeRelPoService.selectByDataScopeId(dataScopeId);
        if(CollectionUtils.isNotEmpty(roleDataScopeRelDtos)){
            for (BaseRoleDataScopeRelDto roleDataScopeRelDto : roleDataScopeRelDtos) {
                List<BaseRoleDataScopeRelDto>  roleDataScopeRelDtos1 = apiBaseRoleDataScopeRelPoService.selectByRoleId(roleDataScopeRelDto.getRoleId());
                if(CollectionUtils.isNotEmpty(roleDataScopeRelDtos1)){
                    List<String> dataScopeIds = new ArrayList<>();
                    for (BaseRoleDataScopeRelDto baseRoleDataScopeRelDto : roleDataScopeRelDtos1) {
                        dataScopeIds.add(baseRoleDataScopeRelDto.getDataScopeId());
                    }
                    apiBaseDataScopeService.checkConflict(dataScopeIds);
                }
            }
        }
    }
}
