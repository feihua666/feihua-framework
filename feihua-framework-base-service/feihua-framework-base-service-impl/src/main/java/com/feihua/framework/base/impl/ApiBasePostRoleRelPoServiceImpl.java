package com.feihua.framework.base.impl;

import com.feihua.framework.base.modules.rel.api.ApiBasePostRoleRelPoService;
import com.feihua.framework.base.modules.rel.dto.BasePostRoleRelDto;
import com.feihua.framework.base.modules.rel.dto.PostBindRolesParamDto;
import com.feihua.framework.base.modules.rel.dto.RoleBindPostsParamDto;
import com.feihua.framework.base.modules.rel.po.BasePostRoleRelPo;
import feihua.jdbc.api.pojo.BasePo;
import feihua.jdbc.api.service.impl.ApiBaseServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * This class was generated by MyBatis Generator.
 * @author yangwei 2019-06-12 14:06:12
 */
@Service
public class ApiBasePostRoleRelPoServiceImpl extends ApiBaseServiceImpl<BasePostRoleRelPo, BasePostRoleRelDto, String> implements ApiBasePostRoleRelPoService {
    @Autowired
    com.feihua.framework.base.mapper.BasePostRoleRelPoMapper BasePostRoleRelPoMapper;

    public ApiBasePostRoleRelPoServiceImpl() {
        super(BasePostRoleRelDto.class);
    }

    @Override
    public BasePostRoleRelDto selectByPostId(String postId) {
        if(StringUtils.isEmpty(postId)) return null;
        BasePostRoleRelPo condition = new BasePostRoleRelPo();
        condition.setPostId(postId);
        condition.setDelFlag(BasePo.YesNo.N.name());
        return this.selectOne(condition);
    }

    @Override
    public List<BasePostRoleRelDto> selectByRoleId(String roleId) {
        BasePostRoleRelPo condition = new BasePostRoleRelPo();
        condition.setRoleId(roleId);
        condition.setDelFlag(BasePo.YesNo.N.name());
        return this.selectList(condition);
    }

    @Override
    public BasePostRoleRelDto selectByPostIdAndRoleId(String postId, String roleId) {
        BasePostRoleRelPo baseUserRoleRelPoCondition = new BasePostRoleRelPo();
        baseUserRoleRelPoCondition.setRoleId(roleId);
        baseUserRoleRelPoCondition.setPostId(postId);
        baseUserRoleRelPoCondition.setDelFlag(BasePo.YesNo.N.name());
        return this.selectOne(baseUserRoleRelPoCondition);
    }

    @Override
    public int deleteFlagByPostId(String postId, String currentUserId) {
        BasePostRoleRelPo baseUserRoleRelPoCondition = new BasePostRoleRelPo();
        baseUserRoleRelPoCondition.setPostId(postId);
        baseUserRoleRelPoCondition.setDelFlag(BasePo.YesNo.N.name());
        return this.deleteFlagSelectiveWithUpdateUser(baseUserRoleRelPoCondition,currentUserId);
    }

    @Override
    public int deleteFlagByRoleId(String roleId, String currentUserId) {
        BasePostRoleRelPo baseUserRoleRelPoCondition = new BasePostRoleRelPo();
        baseUserRoleRelPoCondition.setRoleId(roleId);
        baseUserRoleRelPoCondition.setDelFlag(BasePo.YesNo.N.name());
        return this.deleteFlagSelectiveWithUpdateUser(baseUserRoleRelPoCondition,currentUserId);
    }

    @Override
    public int postBindRoles(PostBindRolesParamDto postBindRolesParamDto) {
        // 先删除以前的
        int deleteR = this.deleteFlagByPostId(postBindRolesParamDto.getPostId(),postBindRolesParamDto.getCurrentUserId());
        List<String> roleIds = postBindRolesParamDto.getRoleIds();
        // 批量插入新关系
        if(roleIds != null && !roleIds.isEmpty()){
            BasePostRoleRelPo userRoleRelPo = null;
            List<BasePostRoleRelPo> addList = new ArrayList<>(roleIds.size());
            for (String roleId : roleIds) {
                userRoleRelPo = new BasePostRoleRelPo();
                userRoleRelPo.setRoleId(roleId);
                userRoleRelPo.setPostId(postBindRolesParamDto.getPostId());
                userRoleRelPo.setDelFlag(BasePo.YesNo.N.name());
                userRoleRelPo = this.preInsert(userRoleRelPo,postBindRolesParamDto.getCurrentUserId());
                addList.add(userRoleRelPo);
            }
            return this.insertBatch(addList);
        }

        return deleteR;
    }

    @Override
    public int roleBindPosts(RoleBindPostsParamDto roleBindPostsParamDto) {
        // 先删除以前的
        int deleteR = this.deleteFlagByRoleId(roleBindPostsParamDto.getRoleId(),roleBindPostsParamDto.getCurrentUserId());
        List<String> postIds = roleBindPostsParamDto.getPostIds();
        // 批量插入新关系
        if(postIds != null && !postIds.isEmpty()){
            BasePostRoleRelPo userRoleRelPo = null;
            List<BasePostRoleRelPo> addList = new ArrayList<>(postIds.size());
            for (String userId : postIds) {
                userRoleRelPo = new BasePostRoleRelPo();
                userRoleRelPo.setPostId(userId);
                userRoleRelPo.setRoleId(roleBindPostsParamDto.getRoleId());
                userRoleRelPo.setDelFlag(BasePo.YesNo.N.name());
                userRoleRelPo = this.preInsert(userRoleRelPo,roleBindPostsParamDto.getCurrentUserId());
                addList.add(userRoleRelPo);
            }
            return this.insertBatch(addList);
        }

        return deleteR;
    }

    @Override
    public BasePostRoleRelDto wrapDto(BasePostRoleRelPo po) {
        if (po == null) { return null; }
        BasePostRoleRelDto dto = new BasePostRoleRelDto();
        dto.setId(po.getId());
        dto.setPostId(po.getPostId());
        dto.setRoleId(po.getRoleId());
        dto.setDataUserId(po.getDataUserId());
        dto.setDataOfficeId(po.getDataOfficeId());
        dto.setDataType(po.getDataType());
        dto.setDataAreaId(po.getDataAreaId());
        dto.setUpdateAt(po.getUpdateAt());
        return dto;
    }
}