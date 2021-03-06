package com.feihua.framework.base.impl;

import com.feihua.framework.base.modules.datascope.api.ApiBaseDataScopeService;
import com.feihua.framework.base.modules.office.api.ApiBaseOfficePoService;
import com.feihua.framework.base.modules.office.dto.BaseOfficeDto;
import com.feihua.framework.base.modules.office.po.BaseOfficePo;
import com.feihua.framework.base.modules.postjob.api.ApiBasePostPoService;
import com.feihua.framework.base.modules.postjob.dto.BasePostDataScopeDefineDto;
import com.feihua.framework.base.modules.postjob.dto.BasePostDto;
import com.feihua.framework.base.modules.postjob.dto.SearchBasePostsConditionDto;
import com.feihua.framework.base.modules.postjob.dto.SearchPostsConditionDsfDto;
import com.feihua.framework.base.modules.postjob.po.BasePostPo;
import com.feihua.framework.base.modules.rel.api.ApiBasePostRoleRelPoService;
import com.feihua.framework.base.modules.rel.api.ApiBaseUserPostRelPoService;
import com.feihua.framework.base.modules.rel.dto.BaseUserPostRelDto;
import com.feihua.framework.constants.DictEnum;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import feihua.jdbc.api.pojo.BasePo;
import feihua.jdbc.api.pojo.PageAndOrderbyParamDto;
import feihua.jdbc.api.pojo.PageResultDto;
import feihua.jdbc.api.service.impl.ApiBaseServiceImpl;

import java.util.*;

import feihua.jdbc.api.service.impl.ApiBaseTreeServiceImpl;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * This class was generated by MyBatis Generator.
 * @author yangwei 2019-06-06 11:05:12
 */
@Service
public class ApiBasePostPoServiceImpl extends ApiBaseTreeServiceImpl<BasePostPo, BasePostDto, String> implements ApiBasePostPoService {
    @Autowired
    com.feihua.framework.base.mapper.BasePostPoMapper basePostPoMapper;

    @Autowired
    private ApiBaseDataScopeService<BasePostDataScopeDefineDto> apiBasePostDataScopeService;
    @Autowired
    private ApiBaseUserPostRelPoService apiBaseUserPostRelPoService;
    @Autowired
    private ApiBaseOfficePoService apiBaseOfficePoService;


    public ApiBasePostPoServiceImpl() {
        super(BasePostDto.class);
    }

    @Override
    public PageResultDto<BasePostDto> searchBasePostsDsf(SearchBasePostsConditionDto conditionDto, PageAndOrderbyParamDto pageAndOrderbyParamDto) {

        BasePostDataScopeDefineDto defineDto = apiBasePostDataScopeService.selectDataScopeDefineByUserId(conditionDto.getCurrentUserId(),conditionDto.getCurrentRoleId(),conditionDto.getCurrentPostId());
        // 如果未设置数据范围定义，没有数据
        if (defineDto == null || StringUtils.isEmpty(defineDto.getType()) || DictEnum.PostDataScope.no.name().equals(defineDto.getType())) {
            return new PageResultDto(null, null);
        }
        SearchPostsConditionDsfDto searchPostsConditionDsfDto = new SearchPostsConditionDsfDto();
        searchPostsConditionDsfDto.setCode(conditionDto.getCode());
        searchPostsConditionDsfDto.setName(conditionDto.getName());
        searchPostsConditionDsfDto.setType(conditionDto.getType());
        searchPostsConditionDsfDto.setPostJobId(conditionDto.getPostJobId());
        searchPostsConditionDsfDto.setParentId(conditionDto.getParentId());
        searchPostsConditionDsfDto.setIsPublic(conditionDto.getIsPublic());
        searchPostsConditionDsfDto.setDisabled(conditionDto.getDisabled());
        searchPostsConditionDsfDto.setDataOfficeId(conditionDto.getDataOfficeId());


        // 所有数据
        if(DictEnum.PostDataScope.all.name().equals(defineDto.getType())){
            Page p = super.pageAndOrderbyStart(pageAndOrderbyParamDto);
            List<BasePostDto> list = this.wrapDtos(basePostPoMapper.searchBasePosts(searchPostsConditionDsfDto));
            return new PageResultDto(list, this.wrapPage(p));
        }
        // 公共数据
        if(DictEnum.PostDataScope.publics.name().equals(defineDto.getType())){

            String selfCondition = com.feihua.utils.string.StringUtils.messageFormat(" and is_public = ''{0}'' ",BasePo.YesNo.Y.name());

            Page p = super.pageAndOrderbyStart(pageAndOrderbyParamDto);
            searchPostsConditionDsfDto.setSelfCondition(selfCondition);
            List<BasePostDto> list = this.wrapDtos(basePostPoMapper.searchBasePosts(searchPostsConditionDsfDto));
            return new PageResultDto(list, this.wrapPage(p));
        }
        // 公共 + 用户所在机构
        if(DictEnum.PostDataScope.publics_useroffice.name().equals(defineDto.getType())){
            Page p = super.pageAndOrderbyStart(pageAndOrderbyParamDto);

            BaseOfficeDto officeDto = apiBaseOfficePoService.selectOfficeByUserId(conditionDto.getCurrentUserId());

            String selfCondition = null;
            if (officeDto == null) {
                selfCondition = com.feihua.utils.string.StringUtils.messageFormat(" and is_public = ''{0}'' ",BasePo.YesNo.Y.name());
            }else {
                selfCondition = com.feihua.utils.string.StringUtils.messageFormat(" and (is_public = ''{0}'' or data_office_id = ''{1}'') ",BasePo.YesNo.Y.name(),officeDto.getId());
            }
            searchPostsConditionDsfDto.setSelfCondition(selfCondition);
            List<BasePostDto> list = this.wrapDtos(basePostPoMapper.searchBasePosts(searchPostsConditionDsfDto));
            return new PageResultDto(list, this.wrapPage(p));
        }
        // 公共 + 用户所在机构及以下机构
        if(DictEnum.PostDataScope.publics_userofficedown.name().equals(defineDto.getType())){
            Page p = super.pageAndOrderbyStart(pageAndOrderbyParamDto);

            BaseOfficeDto officeDto = apiBaseOfficePoService.selectOfficeByUserId(conditionDto.getCurrentUserId());

            String selfCondition = null;
            if (officeDto == null) {
                selfCondition = com.feihua.utils.string.StringUtils.messageFormat(" and is_public = ''{0}'' ",BasePo.YesNo.Y.name());
            }else {

                List<BaseOfficePo> officePoList = new ArrayList<>();
                // 查询子机构

                List<BaseOfficePo> officePos = apiBaseOfficePoService.getChildrenAll(officeDto.getId());
                if(CollectionUtils.isNotEmpty(officePos)){
                    officePoList.addAll(officePos);
                }
                if(CollectionUtils.isEmpty(officePoList)){
                    return new PageResultDto();
                }
                // 机构查询完，根据机构id查询

                StringBuffer sb = new StringBuffer("and (is_public = '"+ BasePo.YesNo.Y.name() +"' or data_office_id ='"+ officeDto.getId() +"' ");
                for (BaseOfficePo officePo : officePoList) {
                    sb.append(" or data_office_id = '").append(officePo.getId()).append("'");
                }
                sb.append(")");

                selfCondition = sb.toString();
            }
            searchPostsConditionDsfDto.setSelfCondition(selfCondition);
            List<BasePostDto> list = this.wrapDtos(basePostPoMapper.searchBasePosts(searchPostsConditionDsfDto));
            return new PageResultDto(list, this.wrapPage(p));
        }
        // 用户所在机构
        if(DictEnum.PostDataScope.useroffice.name().equals(defineDto.getType())){
            Page p = super.pageAndOrderbyStart(pageAndOrderbyParamDto);

            BaseOfficeDto officeDto = apiBaseOfficePoService.selectOfficeByUserId(conditionDto.getCurrentUserId());
            // 如果机构不存在，直接返回空
            if(officeDto == null){
                return new PageResultDto(null, null);
            }
            String selfCondition = com.feihua.utils.string.StringUtils.messageFormat("and data_office_id = ''{0}'' ",officeDto.getId());

            searchPostsConditionDsfDto.setSelfCondition(selfCondition);
            List<BasePostDto> list = this.wrapDtos(basePostPoMapper.searchBasePosts(searchPostsConditionDsfDto));
            return new PageResultDto(list, this.wrapPage(p));
        }
        // 用户所在机构及以下机构
        if(DictEnum.PostDataScope.userofficedown.name().equals(defineDto.getType())){
            Page p = super.pageAndOrderbyStart(pageAndOrderbyParamDto);

            BaseOfficeDto officeDto = apiBaseOfficePoService.selectOfficeByUserId(conditionDto.getCurrentUserId());
            // 如果机构不存在，直接返回空
            if(officeDto == null){
                return new PageResultDto(null, null);
            }

            List<BaseOfficePo> officePoList = new ArrayList<>();
            // 查询子机构

            List<BaseOfficePo> officePos = apiBaseOfficePoService.getChildrenAll(officeDto.getId());
            if(CollectionUtils.isNotEmpty(officePos)){
                officePoList.addAll(officePos);
            }
            if(CollectionUtils.isEmpty(officePoList)){
                return new PageResultDto();
            }
            // 机构查询完，根据机构id查询

            StringBuffer sb = new StringBuffer("and (data_office_id ='"+ officeDto.getId() +"' ");
            for (BaseOfficePo officePo : officePoList) {
                sb.append(" or data_office_id = '").append(officePo.getId()).append("'");
            }
            sb.append(")");
            String selfCondition = sb.toString();

            searchPostsConditionDsfDto.setSelfCondition(selfCondition);
            List<BasePostDto> list = this.wrapDtos(basePostPoMapper.searchBasePosts(searchPostsConditionDsfDto));
            return new PageResultDto(list, this.wrapPage(p));
        }
        // 公共 + 用户角色所在机构
        if(DictEnum.PostDataScope.publics_userroleoffice.name().equals(defineDto.getType())){
            Page p = super.pageAndOrderbyStart(pageAndOrderbyParamDto);

            BaseOfficeDto officeDto = apiBaseOfficePoService.selectOfficeByRoleId(conditionDto.getCurrentRoleId(),false);
            String selfCondition = null;
            if (officeDto == null) {
                selfCondition = com.feihua.utils.string.StringUtils.messageFormat(" and is_public = ''{0}'' ",BasePo.YesNo.Y.name());
            }else {
                selfCondition = com.feihua.utils.string.StringUtils.messageFormat(" and (is_public = ''{0}'' or data_office_id = ''{1}'') ",BasePo.YesNo.Y.name(),officeDto.getId());
            }
            searchPostsConditionDsfDto.setSelfCondition(selfCondition);
            List<BasePostDto> list = this.wrapDtos(basePostPoMapper.searchBasePosts(searchPostsConditionDsfDto));
            return new PageResultDto(list, this.wrapPage(p));
        }
        // 公共 + 用户角色所在机构及以下机构
        if(DictEnum.PostDataScope.publics_userroleofficedown.name().equals(defineDto.getType())){
            Page p = super.pageAndOrderbyStart(pageAndOrderbyParamDto);

            BaseOfficeDto officeDto = apiBaseOfficePoService.selectOfficeByRoleId(conditionDto.getCurrentRoleId(),false);
            String selfCondition = null;
            if (officeDto == null) {
                selfCondition = com.feihua.utils.string.StringUtils.messageFormat(" and is_public = ''{0}'' ",BasePo.YesNo.Y.name());
            }else {

                List<BaseOfficePo> officePoList = new ArrayList<>();
                // 查询子机构

                List<BaseOfficePo> officePos = apiBaseOfficePoService.getChildrenAll(officeDto.getId());
                if(CollectionUtils.isNotEmpty(officePos)){
                    officePoList.addAll(officePos);
                }
                if(CollectionUtils.isEmpty(officePoList)){
                    return new PageResultDto();
                }
                // 机构查询完，根据机构id查询

                StringBuffer sb = new StringBuffer("and (is_public = '"+ BasePo.YesNo.Y.name() +"' or data_office_id ='"+ officeDto.getId() +"' ");
                for (BaseOfficePo officePo : officePoList) {
                    sb.append(" or data_office_id = '").append(officePo.getId()).append("'");
                }
                sb.append(")");

                selfCondition = sb.toString();
            }
            searchPostsConditionDsfDto.setSelfCondition(selfCondition);
            List<BasePostDto> list = this.wrapDtos(basePostPoMapper.searchBasePosts(searchPostsConditionDsfDto));
            return new PageResultDto(list, this.wrapPage(p));
        }
        // 用户角色所在机构
        if(DictEnum.PostDataScope.userroleoffice.name().equals(defineDto.getType())){
            Page p = super.pageAndOrderbyStart(pageAndOrderbyParamDto);

            BaseOfficeDto officeDto = apiBaseOfficePoService.selectOfficeByRoleId(conditionDto.getCurrentRoleId(),false);
            // 如果机构不存在，直接返回空
            if(officeDto == null){
                return new PageResultDto(null, null);
            }

            String selfCondition = com.feihua.utils.string.StringUtils.messageFormat("and data_office_id = ''{0}'' ",officeDto.getId());

            searchPostsConditionDsfDto.setSelfCondition(selfCondition);
            List<BasePostDto> list = this.wrapDtos(basePostPoMapper.searchBasePosts(searchPostsConditionDsfDto));
            return new PageResultDto(list, this.wrapPage(p));
        }
        // 用户角色所在机构及以下机构
        if(DictEnum.PostDataScope.userroleofficedown.name().equals(defineDto.getType())){
            Page p = super.pageAndOrderbyStart(pageAndOrderbyParamDto);

            BaseOfficeDto officeDto = apiBaseOfficePoService.selectOfficeByRoleId(conditionDto.getCurrentRoleId(),false);
            // 如果机构不存在，直接返回空
            if(officeDto == null){
                return new PageResultDto(null, null);
            }


            List<BaseOfficePo> officePoList = new ArrayList<>();
            // 查询子机构

            List<BaseOfficePo> officePos = apiBaseOfficePoService.getChildrenAll(officeDto.getId());
            if(CollectionUtils.isNotEmpty(officePos)){
                officePoList.addAll(officePos);
            }
            if(CollectionUtils.isEmpty(officePoList)){
                return new PageResultDto();
            }
            // 机构查询完，根据机构id查询

            StringBuffer sb = new StringBuffer("and (data_office_id ='"+ officeDto.getId() +"' ");
            for (BaseOfficePo officePo : officePoList) {
                sb.append(" or data_office_id = '").append(officePo.getId()).append("'");
            }
            sb.append(")");
            String selfCondition = sb.toString();

            searchPostsConditionDsfDto.setSelfCondition(selfCondition);
            List<BasePostDto> list = this.wrapDtos(basePostPoMapper.searchBasePosts(searchPostsConditionDsfDto));
            return new PageResultDto(list, this.wrapPage(p));
        }
        // 公共 + 用户岗位所在机构
        if(DictEnum.PostDataScope.publics_userroleoffice.name().equals(defineDto.getType())){
            Page p = super.pageAndOrderbyStart(pageAndOrderbyParamDto);

            BaseOfficeDto officeDto = apiBaseOfficePoService.selectOfficeByPostId(conditionDto.getCurrentPostId(),false);
            String selfCondition = null;
            if (officeDto == null) {
                selfCondition = com.feihua.utils.string.StringUtils.messageFormat(" and is_public = ''{0}'' ",BasePo.YesNo.Y.name());
            }else {
                selfCondition = com.feihua.utils.string.StringUtils.messageFormat(" and (is_public = ''{0}'' or data_office_id = ''{1}'') ",BasePo.YesNo.Y.name(),officeDto.getId());
            }
            searchPostsConditionDsfDto.setSelfCondition(selfCondition);
            List<BasePostDto> list = this.wrapDtos(basePostPoMapper.searchBasePosts(searchPostsConditionDsfDto));
            return new PageResultDto(list, this.wrapPage(p));
        }
        // 公共 + 用户岗位所在机构及以下机构
        if(DictEnum.PostDataScope.publics_userroleofficedown.name().equals(defineDto.getType())){
            Page p = super.pageAndOrderbyStart(pageAndOrderbyParamDto);

            BaseOfficeDto officeDto = apiBaseOfficePoService.selectOfficeByPostId(conditionDto.getCurrentPostId(),false);
            String selfCondition = null;
            if (officeDto == null) {
                selfCondition = com.feihua.utils.string.StringUtils.messageFormat(" and is_public = ''{0}'' ",BasePo.YesNo.Y.name());
            }else {


                List<BaseOfficePo> officePoList = new ArrayList<>();
                // 查询子机构

                List<BaseOfficePo> officePos = apiBaseOfficePoService.getChildrenAll(officeDto.getId());
                if(CollectionUtils.isNotEmpty(officePos)){
                    officePoList.addAll(officePos);
                }
                if(CollectionUtils.isEmpty(officePoList)){
                    return new PageResultDto();
                }
                // 机构查询完，根据机构id查询

                StringBuffer sb = new StringBuffer("and (is_public = '"+ BasePo.YesNo.Y.name() +"' or data_office_id ='"+ officeDto.getId() +"' ");
                for (BaseOfficePo officePo : officePoList) {
                    sb.append(" or data_office_id = '").append(officePo.getId()).append("'");
                }
                sb.append(")");

                selfCondition = sb.toString();
            }
            searchPostsConditionDsfDto.setSelfCondition(selfCondition);
            List<BasePostDto> list = this.wrapDtos(basePostPoMapper.searchBasePosts(searchPostsConditionDsfDto));
            return new PageResultDto(list, this.wrapPage(p));
        }
        // 用户岗位所在机构
        if(DictEnum.PostDataScope.userroleoffice.name().equals(defineDto.getType())){
            Page p = super.pageAndOrderbyStart(pageAndOrderbyParamDto);

            BaseOfficeDto officeDto = apiBaseOfficePoService.selectOfficeByPostId(conditionDto.getCurrentPostId(),false);
            // 如果机构不存在，直接返回空
            if(officeDto == null){
                return new PageResultDto(null, null);
            }
            String selfCondition = com.feihua.utils.string.StringUtils.messageFormat("and data_office_id = ''{0}'' ",officeDto.getId());

            searchPostsConditionDsfDto.setSelfCondition(selfCondition);
            List<BasePostDto> list = this.wrapDtos(basePostPoMapper.searchBasePosts(searchPostsConditionDsfDto));
            return new PageResultDto(list, this.wrapPage(p));
        }
        // 用户岗位所在机构及以下机构
        if(DictEnum.PostDataScope.userroleoffice.name().equals(defineDto.getType())){
            Page p = super.pageAndOrderbyStart(pageAndOrderbyParamDto);

            BaseOfficeDto officeDto = apiBaseOfficePoService.selectOfficeByPostId(conditionDto.getCurrentPostId(),false);
            // 如果机构不存在，直接返回空
            if(officeDto == null){
                return new PageResultDto(null, null);
            }

            List<BaseOfficePo> officePoList = new ArrayList<>();
            // 查询子机构

            List<BaseOfficePo> officePos = apiBaseOfficePoService.getChildrenAll(officeDto.getId());
            if(CollectionUtils.isNotEmpty(officePos)){
                officePoList.addAll(officePos);
            }
            if(CollectionUtils.isEmpty(officePoList)){
                return new PageResultDto();
            }
            // 机构查询完，根据机构id查询

            StringBuffer sb = new StringBuffer("and (data_office_id ='"+ officeDto.getId() +"' ");
            for (BaseOfficePo officePo : officePoList) {
                sb.append(" or data_office_id = '").append(officePo.getId()).append("'");
            }
            sb.append(")");
            String selfCondition = sb.toString();

            searchPostsConditionDsfDto.setSelfCondition(selfCondition);
            List<BasePostDto> list = this.wrapDtos(basePostPoMapper.searchBasePosts(searchPostsConditionDsfDto));
            return new PageResultDto(list, this.wrapPage(p));
        }
        // 分配的岗位
        if(DictEnum.PostDataScope.assign.name().equals(defineDto.getType())){
            Set<String> postIds = new HashSet<>();
            // 查询当前用户分配的岗位
            List<BaseUserPostRelDto> userPostRelDtos = apiBaseUserPostRelPoService.selectByUserId(conditionDto.getCurrentUserId());
            if(CollectionUtils.isNotEmpty(userPostRelDtos)){
                for (BaseUserPostRelDto baseUserPostRelDto : userPostRelDtos) {
                    postIds.add(baseUserPostRelDto.getPostId());
                }
            }

            if (postIds.isEmpty()) {
                return new PageResultDto();
            }

            StringBuffer sb = new StringBuffer("and (1!=1 ");
            for (String postId : postIds) {
                sb.append(" or id = '").append(postId).append("'");
            }
            sb.append(")");

            Page p = super.pageAndOrderbyStart(pageAndOrderbyParamDto);
            searchPostsConditionDsfDto.setSelfCondition(sb.toString());
            List<BasePostPo> list = basePostPoMapper.searchBasePosts(searchPostsConditionDsfDto);
            return new PageResultDto(this.wrapDtos(list), this.wrapPage(p));
        }
        // 分配的岗位及以下岗位
        if(DictEnum.PostDataScope.assigndown.name().equals(defineDto.getType())){

            Set<String> postIds = new HashSet<>();
            // 查询当前用户分配的岗位
            List<BaseUserPostRelDto> userPostRelDtos = apiBaseUserPostRelPoService.selectByUserId(conditionDto.getCurrentUserId());
            if(CollectionUtils.isEmpty(userPostRelDtos)){
            }else {
                for (BaseUserPostRelDto baseUserPostRelDto : userPostRelDtos) {
                    postIds.add(baseUserPostRelDto.getPostId());
                }
            }
            if (postIds.isEmpty()) {
                return new PageResultDto();
            }
            // 子岗位
            BasePostDto basePostDto = null;
            StringBuffer sb = new StringBuffer("and (1!=1 ");
            for (String postId : postIds) {
                basePostDto = this.selectByPrimaryKey(postId,false);
                sb.append(" or id = '").append(postId).append("'");
                sb.append(" or parent_id").append(basePostDto.getLevel()).append(" = '").append(postId).append("'");
            }
            sb.append(")");

            super.pageAndOrderbyStart(pageAndOrderbyParamDto);
            Page p = PageHelper.getLocalPage();
            searchPostsConditionDsfDto.setSelfCondition(sb.toString());
            List<BasePostPo> list = basePostPoMapper.searchBasePosts(searchPostsConditionDsfDto);
            return new PageResultDto(this.wrapDtos(list), this.wrapPage(p));

        }

        return new PageResultDto(null, null);

    }

    @Override
    public List<BasePostDto> selectByUserId(String userId,boolean includeDisabled) {
        // 角色信息,目前一个用户只有一个角色
        List<BaseUserPostRelDto> userPostRelDtos =  apiBaseUserPostRelPoService.selectByUserId(userId);
        if(userPostRelDtos != null && !userPostRelDtos.isEmpty()) {
            List<String> postIds = new ArrayList<>(userPostRelDtos.size());
            for (BaseUserPostRelDto userPostRelDto : userPostRelDtos) {
                postIds.add(userPostRelDto.getPostId());
            }
            List<BasePostDto> postDtos = this.selectByPrimaryKeys(postIds, false);
            Iterator<BasePostDto> iterator = postDtos.iterator();
            while (iterator.hasNext()){
                BasePostDto postDto = iterator.next();
                if(BasePo.YesNo.Y.name().equals(postDto.getDisabled())){
                    iterator.remove();
                }
            }
            return postDtos;
        }
        return null;
    }

    @Override
    public BasePostDto wrapDto(BasePostPo po) {
        if (po == null) { return null; }
        BasePostDto dto = new BasePostDto();
        dto.setId(po.getId());
        dto.setName(po.getName());
        dto.setCode(po.getCode());
        dto.setType(po.getType());
        dto.setPostJobId(po.getPostJobId());
        dto.setDisabled(po.getDisabled());
        dto.setLevel(po.getLevel());
        dto.setParentId(po.getParentId());
        dto.setDataUserId(po.getDataUserId());
        dto.setDataOfficeId(po.getDataOfficeId());
        dto.setDataType(po.getDataType());
        dto.setDataAreaId(po.getDataAreaId());
        dto.setUpdateAt(po.getUpdateAt());
        dto.setIsPublic(po.getIsPublic());
        return dto;
    }
}