package com.feihua.framework.rest.service;


import com.feihua.framework.base.modules.area.api.ApiBaseAreaPoService;
import com.feihua.framework.base.modules.area.dto.BaseAreaDto;
import com.feihua.framework.base.modules.function.api.ApiBaseFunctionResourcePoService;
import com.feihua.framework.base.modules.function.dto.BaseFunctionResourceDto;
import com.feihua.framework.base.modules.function.dto.SearchFunctionResourcesConditionDto;
import com.feihua.framework.base.modules.loginclient.api.ApiBaseLoginClientPoService;
import com.feihua.framework.base.modules.loginclient.dto.BaseLoginClientDto;
import com.feihua.framework.base.modules.loginclient.po.BaseLoginClientPo;
import com.feihua.framework.base.modules.office.api.ApiBaseOfficePoService;
import com.feihua.framework.base.modules.office.dto.BaseOfficeDto;
import com.feihua.framework.base.modules.postjob.api.ApiBasePostPoService;
import com.feihua.framework.base.modules.postjob.dto.BasePostDto;
import com.feihua.framework.base.modules.rel.api.ApiBaseUserRolePostSwitchPoService;
import com.feihua.framework.base.modules.role.api.ApiBaseRolePoService;
import com.feihua.framework.base.modules.role.dto.BaseRoleDto;
import com.feihua.framework.base.modules.role.dto.SearchRolesConditionDto;
import com.feihua.framework.base.modules.user.api.ApiBaseRecordUserLoginPoService;
import com.feihua.framework.base.modules.user.api.ApiBaseUserAuthPoService;
import com.feihua.framework.base.modules.user.api.ApiBaseUserPoService;
import com.feihua.framework.base.modules.user.dto.BaseUserAuthDto;
import com.feihua.framework.base.modules.user.dto.BaseUserDto;
import com.feihua.framework.base.modules.user.po.BaseRecordUserLoginPo;
import com.feihua.framework.base.modules.user.po.BaseUserAuthPo;
import com.feihua.framework.base.modules.user.po.BaseUserRolePostSwitchPo;
import com.feihua.framework.constants.DictEnum;
import com.feihua.framework.rest.utils.Utils;
import com.feihua.framework.shiro.pojo.AuthenticationInfo;
import com.feihua.framework.shiro.pojo.PasswordAndSalt;
import com.feihua.framework.shiro.pojo.ShiroUser;
import com.feihua.framework.shiro.pojo.token.*;
import com.feihua.framework.shiro.service.AbstractAccountServiceImpl;
import com.feihua.framework.shiro.utils.ShiroUtils;
import com.feihua.utils.string.StringUtils;
import feihua.jdbc.api.pojo.BasePo;
import feihua.jdbc.api.pojo.PageResultDto;
import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created by yangwei
 * Created at 2017/7/26 16:58
 */
public class AccountServiceImpl extends AbstractAccountServiceImpl {

    @Autowired
    private ApiBaseUserPoService apiBaseUserPoService;
    @Resource
    private ApiBaseUserAuthPoService apiBaseUserAuthPoService;
    @Autowired
    private ApiBaseRolePoService apiBaseRolePoService;
    @Autowired
    private ApiBaseFunctionResourcePoService apiBaseFunctionResourcePoService;
    @Autowired
    private ApiBaseOfficePoService apiBaseOfficePoService;
    @Autowired
    private ApiBaseAreaPoService apiBaseAreaPoService;
    @Autowired
    private ApiBaseRecordUserLoginPoService apiBaseRecordUserLoginPoService;
    @Autowired
    private ApiBaseLoginClientPoService apiBaseLoginClientPoService;
    @Autowired
    private ApiBasePostPoService apiBasePostPoService;
    @Autowired
    private ApiBaseUserRolePostSwitchPoService apiBaseUserRolePostSwitchPoService;


    @Override
    public AuthenticationInfo findUserByToken(AuthenticationToken authcToken) {

        BaseUserAuthDto userAuthDto = findUserAuthByToken(authcToken);
        //生成认证信息
        AuthenticationInfo info = null;
        if(userAuthDto != null){
            info = new AuthenticationInfo();
            BaseUserDto userDto = apiBaseUserPoService.selectByPrimaryKey(userAuthDto.getUserId());
            info.setUserId(userAuthDto.getUserId());
            info.setLocked(Utils.toBoolean(userDto.getLocked()));
            if(validatePasswordWhenLogin(authcToken)){
                PasswordAndSalt ps = new PasswordAndSalt();
                String credentials[] = userAuthDto.getCredential().split("_");
                if (credentials != null) {
                    if(credentials.length == 2){
                        ps.setPassword(credentials[0]);
                        ps.setSalt(credentials[1]);
                    }else if(credentials.length == 1)
                    {
                        ps.setPassword(credentials[0]);
                    }
                }

                info.setPasswordAndSalt(ps);
            }

        }
        return info;
    }

    @Override
    public Object findPrincipalByToken(AuthenticationToken authenticationToken) {
        BaseUserAuthDto userAuthDto = findUserAuthByToken(authenticationToken);
        if (userAuthDto != null) {
            return userAuthDto.getUserId();
        }
        return null;
    }

    @Override
    public String getClientIdByClientCode(String clientCode) {

        BaseLoginClientPo condition = apiBaseLoginClientPoService.selectByClientCode(clientCode);
        if (condition != null) {
            return condition.getId();
        }

        return null;
    }

    @Override
    public String getClientNameByClientCode(String clientCode) {
        BaseLoginClientPo condition = apiBaseLoginClientPoService.selectByClientCode(clientCode);
        if (condition != null) {
            return condition.getName();
        }

        return null;
    }

    @Override
    public boolean validatePasswordWhenLogin(AuthenticationToken authenticationToken) {
        return ((authenticationToken instanceof AccountPasswordToken) || (authenticationToken instanceof MobilePasswordToken) || (authenticationToken instanceof EmailPasswordToken));
    }

    @Override
    public boolean validateCaptchaWhenLogin(ServletRequest servletRequest, ServletResponse servletResponse) {
        return true;
    }

    public BaseUserAuthDto findUserAuthByToken(AuthenticationToken authcToken){
        BaseUserAuthDto userAuthDto = null;
        if(authcToken instanceof AccountPasswordToken){
            if(org.apache.commons.lang3.StringUtils.isNotEmpty(((AccountPasswordToken) authcToken).getAccount())) {
                //根据用户帐号查询用户
                userAuthDto = apiBaseUserAuthPoService.selectByIdentifierAndType(((AccountPasswordToken) authcToken).getAccount(),DictEnum.LoginType.ACCOUNT.name());
            }
        }else if(authcToken instanceof MobilePasswordToken){
            if(org.apache.commons.lang3.StringUtils.isNotEmpty(((MobilePasswordToken) authcToken).getMobile())) {
                //根据用户手机号
                userAuthDto = apiBaseUserAuthPoService.selectByIdentifierAndType(((MobilePasswordToken) authcToken).getMobile(),DictEnum.LoginType.MOBILE.name());
            }
        }else if(authcToken instanceof EmailPasswordToken){
            if(org.apache.commons.lang3.StringUtils.isNotEmpty(((EmailPasswordToken) authcToken).getEmail())) {
                // 根据邮箱查询
                userAuthDto = apiBaseUserAuthPoService.selectByIdentifierAndType(((EmailPasswordToken) authcToken).getEmail(), DictEnum.LoginType.EMAIL.name());
            }
        }else if(authcToken instanceof QrcodeToken){
            if(org.apache.commons.lang3.StringUtils.isNotEmpty(((QrcodeToken) authcToken).getUserId())) {
                // 二维码扫码登录
                // 实际转化为帐号登录
                ShiroUser su = findUserInfo(((QrcodeToken) authcToken).getUserId());
                if(su != null)
                userAuthDto = apiBaseUserAuthPoService.selectByIdentifierAndType(su.getAccount(),DictEnum.LoginType.ACCOUNT.name());
            }
        }
        else if(authcToken instanceof WxMiniProgramToken){
            if(org.apache.commons.lang3.StringUtils.isNotEmpty(((WxMiniProgramToken) authcToken).getOpenid())){
                // 小程序opendid 查询
                userAuthDto = apiBaseUserAuthPoService.selectByIdentifierAndType(((WxMiniProgramToken) authcToken).getOpenid(),DictEnum.LoginType.WX_MINIPROGRAM.name());
            }
        }else if(authcToken instanceof WxPlatformToken){
            if(org.apache.commons.lang3.StringUtils.isNotEmpty(((WxPlatformToken) authcToken).getOpenid())) {
                // 公众平台opendid 查询
                userAuthDto = apiBaseUserAuthPoService.selectByIdentifierAndType(((WxPlatformToken) authcToken).getOpenid(), DictEnum.LoginType.WX_PLATFORM.name());
            }
        }
        return userAuthDto;
    }
    @Override
    public ShiroUser findUserInfo(String userId) {
        ShiroUser user = new ShiroUser();
        user.setId(userId);

        BaseUserDto userDto = apiBaseUserPoService.selectByPrimaryKey(userId);
        BaseUserAuthDto userAuthAccountDto = apiBaseUserAuthPoService.selectByUserIdAndType(userId,DictEnum.LoginType.ACCOUNT.name());

        // 帐号信息
        if (userAuthAccountDto != null) {
            user.setAccount(userAuthAccountDto.getIdentifier());
        }else {
            user.setAccount(null);
        }
        user.setLocked(Utils.toBoolean(userDto.getLocked()));
        user.setPhoto(userDto.getPhoto());
        // 手机号信息
        BaseUserAuthDto userAuthMobileDto = apiBaseUserAuthPoService.selectByUserIdAndType(userId,DictEnum.LoginType.MOBILE.name());
        if (userAuthMobileDto != null) {
            user.setMobile(userAuthMobileDto.getIdentifier());
        }else{
            user.setMobile(null);
        }
        user.setSerialNo(userDto.getSerialNo());
        user.setNickname(userDto.getNickname());
        user.setGender(userDto.getGender());
        user.setFromClientId(userDto.getFromClientId());
        //
        if (org.apache.commons.lang3.StringUtils.isNotEmpty(userDto.getFromClientId())) {
            BaseLoginClientDto loginClientDto = apiBaseLoginClientPoService.selectByPrimaryKey(userDto.getFromClientId());
            if (loginClientDto != null) {
                user.setFromClientCode(loginClientDto.getCode());
                user.setFromClientName(loginClientDto.getName());
            }

        }


        BaseUserRolePostSwitchPo userRolePostSwitchPo = apiBaseUserRolePostSwitchPoService.selectByUserId(userId);
        // 角色信息,默认使用上一次切换的
        List<BaseRoleDto> roleDtos = apiBaseRolePoService.selectByUserId(userId,false);
        if(roleDtos != null && !roleDtos.isEmpty()){
            BaseRoleDto roleDto = null;
            // 如果上一次没有切换，取第一个
            if (roleDtos.size() == 1 || userRolePostSwitchPo == null || org.apache.commons.lang3.StringUtils.isEmpty(userRolePostSwitchPo.getRoleId())) {
                roleDto = roleDtos.get(0);
            }else {
                for (BaseRoleDto role : roleDtos) {
                    if(role.getId().equals(userRolePostSwitchPo.getRoleId())){
                        roleDto = role;
                        break;
                    }
                }
            }

            if (roleDto != null) {
                user.setRole(roleDto);
            }
            user.setRoles(roleDtos);
        }else{
            user.setRole(null);
            user.setRoles(null);
        }
        // 岗位信息,默认使用上一次切换的
        List<BasePostDto> postDtos = apiBasePostPoService.selectByUserId(userId,false);
        if (postDtos != null && !postDtos.isEmpty()) {
            BasePostDto postDto = null;
            // 如果上一次没有切换，取第一个
            if (postDtos.size() == 1 || userRolePostSwitchPo == null || org.apache.commons.lang3.StringUtils.isEmpty(userRolePostSwitchPo.getPostId())) {
                postDto = postDtos.get(0);
            }else {
                for (BasePostDto post : postDtos) {
                    if(post.getId().equals(userRolePostSwitchPo.getPostId())){
                        postDto = post;
                        break;
                    }
                }
            }

            if (postDto != null) {
                user.setPost(postDto);
            }
            user.setPosts(postDtos);
        }else {
            user.setPost(null);
            user.setPosts(null);
        }

        //机构信息
        BaseOfficeDto officeDto = apiBaseOfficePoService.selectOfficeByUserId(userId);
        user.setOffice(officeDto);
        // 区域信息
        BaseAreaDto areaDto = apiBaseAreaPoService.wrapDto( apiBaseAreaPoService.selectAreaByUserId(userId));
        user.setArea(areaDto);

        // 添加其它额外信息
        Map<String,Object> addtionalAttr = getAddtionalAttr(user);
        if (addtionalAttr != null) {
            user.getAdditionalAttr().putAll(addtionalAttr);
        }else {
            user.getAdditionalAttr().clear();
        }

        return user;
    }

    @Override
    public AuthenticationToken createToken(ServletRequest servletRequest, DictEnum.LoginType loginType, String loginClient) {
        return null;
    }

    @Override
    public Set<String> findStringPermissions(String userId) {
        SearchFunctionResourcesConditionDto searchFunctionResourcesConditionDto = new SearchFunctionResourcesConditionDto();
        searchFunctionResourcesConditionDto.setCurrentUserId(userId);
        BasePostDto postDto = (BasePostDto) ShiroUtils.getCurrentUser().getPost();
        if (postDto != null) {
            searchFunctionResourcesConditionDto.setCurrentPostId(postDto.getId());

        }
        BaseRoleDto roleDto = (BaseRoleDto) ShiroUtils.getCurrentUser().getRole();
        if (roleDto != null) {
            searchFunctionResourcesConditionDto.setCurrentRoleId(roleDto.getId());
        }

        // 不分页，查询全部权限范围内数据
        PageResultDto<BaseFunctionResourceDto> functionResourceDtoPageResultDto = apiBaseFunctionResourcePoService.searchFunctionResourcesDsf(searchFunctionResourcesConditionDto,null);
        List<BaseFunctionResourceDto> functionResourceDtos = functionResourceDtoPageResultDto.getData();
        Set<String> permissions = new HashSet<>();
        permissions.add("user");
        if(functionResourceDtos != null && !functionResourceDtos.isEmpty()){
            for (BaseFunctionResourceDto functionResourceDto : functionResourceDtos) {
                if(!StringUtils.isEmpty(functionResourceDto.getPermissions())){
                    // 支持逗号分隔，添加多个
                    String permission[] = functionResourceDto.getPermissions().split(",");
                    for (String p : permission) {
                        permissions.add(p);
                    }
                }
            }
        }
        return permissions;
    }

    @Override
    public Set<String> findStringRoles(String userId) {
        Set<String> roles = new HashSet<>();
        // 角色信息
        SearchRolesConditionDto searchRolesConditionDto = new SearchRolesConditionDto();
        searchRolesConditionDto.setCurrentUserId(userId);
        BasePostDto postDto = (BasePostDto) ShiroUtils.getCurrentUser().getPost();
        if (postDto != null) {
            searchRolesConditionDto.setCurrentPostId(postDto.getId());

        }
        BaseRoleDto roleDto = (BaseRoleDto) ShiroUtils.getCurrentUser().getRole();
        if (roleDto != null) {
            searchRolesConditionDto.setCurrentRoleId(roleDto.getId());
        }
        PageResultDto<BaseRoleDto> roleDtoPageResultDto = apiBaseRolePoService.searchRolesDsf(searchRolesConditionDto,null);
        List<BaseRoleDto> roleDtos = roleDtoPageResultDto.getData();
        if(roleDtos != null && !roleDtos.isEmpty()){
            for (BaseRoleDto dto : roleDtos) {
                roles.add(dto.getCode());
            }
        }
        return roles;
    }

    @Override
    public void onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request, ServletResponse response) {
        super.onLoginSuccess(token, subject, request, response);
        // 修改用户最新登录信息
        ShiroUser su = ShiroUtils.getCurrentUser();
        Date now = new Date();
        BaseUserAuthPo baseUserAuthPoCondition = new BaseUserAuthPo();
        baseUserAuthPoCondition.setUserId(su.getId());
        baseUserAuthPoCondition.setDelFlag(BasePo.YesNo.N.name());
        baseUserAuthPoCondition.setIdentityType(su.getLoginType());

        BaseUserAuthPo baseUserAuthPo = new BaseUserAuthPo();
        baseUserAuthPo.setLastTime(now);
        baseUserAuthPo.setLastIp(su.getHost());

        baseUserAuthPo = apiBaseUserAuthPoService.preUpdate(baseUserAuthPo,su.getId());
        apiBaseUserAuthPoService.updateSelective(baseUserAuthPo,baseUserAuthPoCondition);

        // 添加登录记录信息
        Session session = SecurityUtils.getSubject().getSession();
        BaseRecordUserLoginPo baseRecordUserLoginPo = new BaseRecordUserLoginPo();
        baseRecordUserLoginPo.setUserId(su.getId());
        baseRecordUserLoginPo.setUserNickname(su.getNickname());
        baseRecordUserLoginPo.setClientCode(ShiroUtils.getLoginClientId(session));
        baseRecordUserLoginPo.setLoginIp(su.getHost());
        baseRecordUserLoginPo.setLoginTime(now);
        HttpServletRequest r = ((HttpServletRequest)request);
        baseRecordUserLoginPo.setUserAgent(r.getHeader("user-agent"));
        baseRecordUserLoginPo.setAppversion(r.getHeader("appversion"));
        baseRecordUserLoginPo.setOsversion(r.getHeader("osversion"));
        baseRecordUserLoginPo.setScreenwidth(r.getHeader("screenwidth"));
        baseRecordUserLoginPo.setScreenheight(r.getHeader("screenheight"));
        baseRecordUserLoginPo.setScreenscale(r.getHeader("screenscale"));
        baseRecordUserLoginPo.setDeviceId(r.getHeader("deviceid"));
        baseRecordUserLoginPo.setDevicetype(r.getHeader("devicetype"));
        baseRecordUserLoginPo = apiBaseRecordUserLoginPoService.preInsert(baseRecordUserLoginPo,su.getId());
        apiBaseRecordUserLoginPoService.insert(baseRecordUserLoginPo);
    }
}
