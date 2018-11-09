package com.feihua.framework.rest.service;


import com.feihua.framework.base.modules.function.api.ApiBaseFunctionResourcePoService;
import com.feihua.framework.base.modules.function.dto.BaseFunctionResourceDto;
import com.feihua.framework.base.modules.function.dto.SearchFunctionResourcesConditionDto;
import com.feihua.framework.base.modules.office.api.ApiBaseOfficePoService;
import com.feihua.framework.base.modules.office.dto.BaseOfficeDto;
import com.feihua.framework.base.modules.role.api.ApiBaseRolePoService;
import com.feihua.framework.base.modules.role.dto.BaseRoleDto;
import com.feihua.framework.base.modules.user.api.ApiBaseUserAuthPoService;
import com.feihua.framework.base.modules.user.api.ApiBaseUserPoService;
import com.feihua.framework.base.modules.user.dto.BaseUserAuthDto;
import com.feihua.framework.base.modules.user.dto.BaseUserDto;
import com.feihua.framework.base.modules.user.po.BaseUserAuthPo;
import com.feihua.framework.constants.DictEnum;
import com.feihua.framework.rest.utils.Utils;
import com.feihua.framework.shiro.ShiroFormAuthenticationFilter;
import com.feihua.framework.shiro.pojo.AuthenticationInfo;
import com.feihua.framework.shiro.pojo.PasswordAndSalt;
import com.feihua.framework.shiro.pojo.ShiroUser;
import com.feihua.framework.shiro.pojo.token.*;
import com.feihua.framework.shiro.service.AbstractAccountServiceImpl;
import com.feihua.framework.shiro.service.AccountService;
import com.feihua.framework.shiro.utils.ShiroUtils;
import com.feihua.utils.string.StringUtils;
import feihua.jdbc.api.pojo.BasePo;
import feihua.jdbc.api.pojo.PageResultDto;
import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
        }
        user.setLocked(Utils.toBoolean(userDto.getLocked()));
        user.setPhoto(userDto.getPhoto());
        // 手机号信息
        BaseUserAuthDto userAuthMobileDto = apiBaseUserAuthPoService.selectByUserIdAndType(userId,DictEnum.LoginType.MOBILE.name());
        if (userAuthMobileDto != null) {
            user.setMobile(userAuthMobileDto.getIdentifier());
        }
        user.setSerialNo(userDto.getSerialNo());
        user.setNickname(userDto.getNickname());
        user.setGender(userDto.getGender());
        // 角色信息
        BaseRoleDto roleDto = apiBaseRolePoService.selectByUserId(userId);
        if(roleDto != null && BasePo.YesNo.N.name().equals(roleDto.getDisabled())){
            user.setRole(roleDto);
        }
        //机构信息
        BaseOfficeDto officeDto = apiBaseOfficePoService.selectOfficeByUserId(userId);
        if (officeDto != null) {
            user.setOffice(officeDto);
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
        BaseRoleDto roleDto = (BaseRoleDto) ShiroUtils.getCurrentUser().getRole();
        if (roleDto != null) {
            searchFunctionResourcesConditionDto.setCurrentRoleId(roleDto.getId());
        }

        // 不分页，查询全部权限范围内数据
        PageResultDto<BaseFunctionResourceDto> functionResourceDtoPageResultDto = apiBaseFunctionResourcePoService.searchFunctionResourcesDsf(searchFunctionResourcesConditionDto,null);
        List<BaseFunctionResourceDto> functionResourceDtos = functionResourceDtoPageResultDto.getData();
        Set<String> permissions = new HashSet<>();
        permissions.add("user");
        if(CollectionUtils.isNotEmpty(functionResourceDtos)){
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
        BaseRoleDto roleDto = apiBaseRolePoService.selectByPrimaryKey(userId);
        if(roleDto != null && !StringUtils.isEmpty(roleDto.getCode())){
            roles.add(roleDto.getCode());
        }
        return roles;
    }

    @Override
    public void onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request, ServletResponse response) {
        super.onLoginSuccess(token, subject, request, response);
        // 修改用户最新登录信息
        ShiroUser su = ShiroUtils.getCurrentUser();
        BaseUserAuthPo baseUserAuthPoCondition = new BaseUserAuthPo();
        baseUserAuthPoCondition.setUserId(su.getId());
        baseUserAuthPoCondition.setDelFlag(BasePo.YesNo.N.name());
        baseUserAuthPoCondition.setIdentityType(su.getLoginType());

        BaseUserAuthPo baseUserAuthPo = new BaseUserAuthPo();
        baseUserAuthPo.setLastTime(new Date());
        baseUserAuthPo.setLastIp(su.getHost());

        apiBaseUserAuthPoService.preUpdate(baseUserAuthPo,su.getId());
        apiBaseUserAuthPoService.updateSelective(baseUserAuthPo,baseUserAuthPoCondition);
    }
}
