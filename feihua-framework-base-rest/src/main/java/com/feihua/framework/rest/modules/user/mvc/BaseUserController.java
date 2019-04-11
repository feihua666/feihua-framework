package com.feihua.framework.rest.modules.user.mvc;

import com.feihua.framework.base.modules.datascope.api.ApiBaseDataScopePoService;
import com.feihua.framework.base.modules.datascope.dto.BaseDataScopeDto;
import com.feihua.framework.base.modules.office.api.ApiBaseOfficePoService;
import com.feihua.framework.base.modules.office.dto.BaseOfficeDto;
import com.feihua.framework.base.modules.rel.api.ApiBaseUserDataScopeRelPoService;
import com.feihua.framework.base.modules.rel.api.ApiBaseUserRoleRelPoService;
import com.feihua.framework.base.modules.rel.dto.BaseUserDataScopeRelDto;
import com.feihua.framework.base.modules.rel.dto.BaseUserRoleRelDto;
import com.feihua.framework.base.modules.role.api.ApiBaseRolePoService;
import com.feihua.framework.base.modules.role.dto.BaseRoleDto;
import com.feihua.framework.base.modules.user.api.ApiBaseUserAuthPoService;
import com.feihua.framework.base.modules.user.api.ApiBaseUserPoService;
import com.feihua.framework.base.modules.user.dto.BaseUserAddParamDto;
import com.feihua.framework.base.modules.user.dto.BaseUserAuthDto;
import com.feihua.framework.base.modules.user.dto.BaseUserDto;
import com.feihua.framework.base.modules.user.dto.SearchBaseUsersConditionDto;
import com.feihua.framework.base.modules.user.po.BaseUserAuthPo;
import com.feihua.framework.base.modules.user.po.BaseUserPo;
import com.feihua.framework.constants.DictEnum;
import com.feihua.framework.log.comm.annotation.OperationLog;
import com.feihua.framework.jedis.utils.JedisUtils;
import com.feihua.framework.rest.ResponseJsonRender;
import com.feihua.framework.rest.interceptor.RepeatFormValidator;
import com.feihua.framework.rest.modules.common.mvc.BaseController;
import com.feihua.framework.rest.modules.user.dto.AddUserFormDto;
import com.feihua.framework.rest.modules.user.dto.UpdateUserFormDto;
import com.feihua.framework.rest.modules.user.dto.UpdateUserMobileFormDto;
import com.feihua.framework.rest.modules.user.dto.UserRegistFormDto;
import com.feihua.framework.rest.modules.user.vo.BaseUserVo;
import com.feihua.framework.shiro.pojo.PasswordAndSalt;
import com.feihua.framework.shiro.pojo.ShiroUser;
import com.feihua.framework.shiro.utils.ShiroUtils;
import com.feihua.utils.EmailUtils.EmailUtils;
import com.feihua.utils.EmailUtils.Mail;
import com.feihua.utils.http.httpServletResponse.ResponseCode;
import feihua.jdbc.api.pojo.BasePo;
import feihua.jdbc.api.pojo.PageAndOrderbyParamDto;
import feihua.jdbc.api.pojo.PageResultDto;
import feihua.jdbc.api.utils.OrderbyUtils;
import feihua.jdbc.api.utils.PageUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import javax.mail.Session;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户接口
 * Created by yangwei
 * Created at 2017/8/2 14:52
 */
@RestController
@RequestMapping("/base")
public class BaseUserController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(BaseUserController.class);
    @Autowired
    private ApiBaseUserPoService apiBaseUserPoService;
    @Autowired
    private ApiBaseUserAuthPoService apiBaseUserAuthPoService;
    @Autowired
    private ApiBaseOfficePoService apiBaseOfficePoService;
    @Autowired
    private ApiBaseUserRoleRelPoService apiBaseUserRoleRelPoService;
    @Autowired
    private ApiBaseRolePoService apiBaseRolePoService;
    @Autowired
    private ApiBaseUserDataScopeRelPoService apiBaseUserDataScopeRelPoService;
    @Autowired
    private ApiBaseDataScopePoService apiBaseDataScopePoService;


    /**
     * 单资源，添加用户
     *
     * @param addUserFormDto
     *
     * @return
     */
    @OperationLog(operation = "用户接口", content = "单资源，添加用户")
    @RepeatFormValidator
    @RequiresPermissions("base:user:add")
    @RequestMapping(value = "/user", method = RequestMethod.POST)
    public ResponseEntity addUser(AddUserFormDto addUserFormDto) {
        logger.info("添加用户开始");
        logger.info("当前登录用户id:{}", getLoginUser().getId());
        ResponseJsonRender resultData = new ResponseJsonRender();
        // 表单值设置
        BaseUserAddParamDto baseUserAddParamDto = new BaseUserAddParamDto();

        baseUserAddParamDto.setCurrentUserId(getLoginUser().getId());

        baseUserAddParamDto.setIdentifier(addUserFormDto.getAccount());
        baseUserAddParamDto.setSerialNo(addUserFormDto.getSerialNo());
        baseUserAddParamDto.setLocked(addUserFormDto.getLocked());
        baseUserAddParamDto.setDataOfficeId(addUserFormDto.getDataOfficeId());
        baseUserAddParamDto.setGender(addUserFormDto.getGender());
        baseUserAddParamDto.setIdentityType(DictEnum.LoginType.ACCOUNT.name());
        baseUserAddParamDto.setNickname(addUserFormDto.getNickname());

        PasswordAndSalt ps = PasswordAndSalt.entryptPassword(addUserFormDto.getPassword());

        baseUserAddParamDto.setPassword(ps.getPassword() + "_" + ps.getSalt());

        BaseUserPo r = apiBaseUserPoService.addUser(baseUserAddParamDto);
        if (r == null) {
            // 添加失败
            resultData.setCode(ResponseCode.E404_100001.getCode());
            resultData.setMsg(ResponseCode.E404_100001.getMsg());
            logger.info("code:{},msg:{}", resultData.getCode(), resultData.getMsg());
            logger.info("添加用户结束，失败");
            return new ResponseEntity(resultData, HttpStatus.NOT_FOUND);
        } else {
            BaseUserVo baseUserVo = new BaseUserVo(apiBaseUserPoService.wrapDto(r));
            baseUserVo.setAccount(addUserFormDto.getAccount());
            // 添加成功，返回添加的数据
            resultData.setData(baseUserVo);
            logger.info("添加用户id:{}", r.getId());
            logger.info("添加用户结束，成功");
            return new ResponseEntity(resultData, HttpStatus.CREATED);
        }
    }

    /**
     * 单资源，添加手机号
     *
     * @param id
     * @param mobile
     *
     * @return
     */
    @OperationLog(operation = "用户接口", content = "单资源，添加手机号")
    @RepeatFormValidator
    @RequiresPermissions("base:user:add:mobile")
    @RequestMapping(value = "/user/{id}/mobile", method = RequestMethod.POST)
    public ResponseEntity addUserMobile(@PathVariable String id, String mobile) {
        logger.info("添加用户手机号开始");
        logger.info("当前登录用户id:{}", getLoginUser().getId());
        logger.info("用户id:{}", id);
        ResponseJsonRender resultData = new ResponseJsonRender();
        // 查询手机号是否存在
        BaseUserAuthDto userAuthDto = apiBaseUserAuthPoService.selectByUserIdAndType(id, DictEnum.LoginType.MOBILE.name());
        if (userAuthDto != null) {
            resultData.setCode(ResponseCode.E409_100001.getCode());
            resultData.setMsg(ResponseCode.E409_100001.getMsg());
            logger.info("code:{},msg:{}", resultData.getCode(), resultData.getMsg());
            logger.info("添加用户手机结束，失败");
            return new ResponseEntity(resultData, HttpStatus.CONFLICT);
        }
        // 如果不存在，要添加的数据
        BaseUserAuthPo baseUserAuthPo = new BaseUserAuthPo();
        baseUserAuthPo.setId(userAuthDto.getId());
        baseUserAuthPo.setUserId(id);
        baseUserAuthPo.setVerified(BasePo.YesNo.Y.name());
        baseUserAuthPo.setIdentityType(DictEnum.LoginType.MOBILE.name());
        baseUserAuthPo.setIdentifier(mobile);
        baseUserAuthPo.setCredential(apiBaseUserAuthPoService.selectCredential(id, DictEnum.LoginType.MOBILE.name()));

        baseUserAuthPo = apiBaseUserAuthPoService.preInsert(baseUserAuthPo, getLoginUser().getId());
        BaseUserAuthPo insertBaseUserAuthPo = apiBaseUserAuthPoService.insertSelectiveSimple(baseUserAuthPo);

        if (insertBaseUserAuthPo == null) {
            // 更新失败，资源不存在
            resultData.setCode(ResponseCode.E404_100001.getCode());
            resultData.setMsg(ResponseCode.E404_100001.getMsg());
            logger.info("code:{},msg:{}", resultData.getCode(), resultData.getMsg());
            logger.info("添加用户手机结束，失败");
            return new ResponseEntity(resultData, HttpStatus.NOT_FOUND);
        } else {
            // 更新成功，已被成功创建
            logger.info("添加的用户id:{}", id);
            logger.info("添加用户手机结束，成功");
            return new ResponseEntity(resultData, HttpStatus.CREATED);
        }
    }

    /**
     * 单资源，删除用户
     *
     * @param id
     *
     * @return
     */
    @OperationLog(operation = "用户接口", content = "单资源，删除用户")
    @RepeatFormValidator
    @RequiresPermissions("base:user:delete")
    @RequestMapping(value = "/user/{id}", method = RequestMethod.DELETE)
    public ResponseEntity deleteUser(@PathVariable String id) {
        logger.info("删除用户开始");
        logger.info("当前登录用户id:{}", getLoginUser().getId());
        logger.info("删除的用户id:{}", id);
        ResponseJsonRender resultData = new ResponseJsonRender();

        int r = apiBaseUserPoService.deleteFlagByPrimaryKeyWithUpdateUser(id, getLoginUser().getId());
        if (r <= 0) {
            // 删除失败，可能没有找到资源
            resultData.setCode(ResponseCode.E404_100001.getCode());
            resultData.setMsg(ResponseCode.E404_100001.getMsg());
            logger.info("code:{},msg:{}", resultData.getCode(), resultData.getMsg());
            logger.info("删除用户结束，失败");
            return new ResponseEntity(resultData, HttpStatus.NOT_FOUND);
        } else {
            // 删除成功
            logger.info("删除的用户id:{}", id);
            logger.info("删除用户结束，成功");
            return new ResponseEntity(resultData, HttpStatus.NO_CONTENT);
        }
    }

    /**
     * 单资源，更新用户
     *
     * @param updateUserFormDto
     *
     * @return
     */
    @OperationLog(operation = "用户接口", content = "单资源，更新用户")
    @RepeatFormValidator
    @RequiresPermissions("base:user:update")
    @RequestMapping(value = "/user/{id}", method = RequestMethod.PUT)
    public ResponseEntity updateUser(@PathVariable String id, UpdateUserFormDto updateUserFormDto) {
        logger.info("更新用户开始");
        logger.info("当前登录用户id:{}", getLoginUser().getId());
        ResponseJsonRender resultData = new ResponseJsonRender();
        // 表单值设置
        BaseUserPo baseUserPo = new BaseUserPo();
        baseUserPo.setSerialNo(updateUserFormDto.getSerialNo());
        baseUserPo.setLocked(updateUserFormDto.getLocked());
        baseUserPo.setDataOfficeId(updateUserFormDto.getDataOfficeId());
        baseUserPo.setGender(updateUserFormDto.getGender());
        baseUserPo.setNickname(updateUserFormDto.getNickname());
        baseUserPo = apiBaseUserPoService.preUpdate(baseUserPo, getLoginUser().getId());

        BaseUserPo baseUserCondition = new BaseUserPo();
        baseUserCondition.setId(id);
        baseUserCondition.setUpdateAt(updateUserFormDto.getUpdateTime());
        baseUserCondition.setDelFlag(BasePo.YesNo.N.name());

        int r = apiBaseUserPoService.updateSelective(baseUserPo, baseUserCondition);
        if (r == 0) {
            // 更新失败
            resultData.setCode(ResponseCode.E404_100001.getCode());
            resultData.setMsg(ResponseCode.E404_100001.getMsg());
            logger.info("code:{},msg:{}", resultData.getCode(), resultData.getMsg());
            logger.info("更新用户结束，失败");
            return new ResponseEntity(resultData, HttpStatus.NOT_FOUND);
        } else {
            logger.info("更新用户id:{}", id);
            logger.info("更新用户结束，成功");
            return new ResponseEntity(resultData, HttpStatus.CREATED);
        }
    }

    /**
     * 单资源，更新手机号
     *
     * @param id
     * @param updateUserMobileFormDto
     *
     * @return
     */
    @OperationLog(operation = "用户接口", content = "单资源，更新手机号")
    @RepeatFormValidator
    @RequiresPermissions("base:user:update:mobile")
    @RequestMapping(value = "/user/{id}/mobile", method = RequestMethod.PUT)
    public ResponseEntity updateUserMobile(@PathVariable String id, UpdateUserMobileFormDto updateUserMobileFormDto) {
        logger.info("更新用户手机号开始");
        logger.info("当前登录用户id:{}", getLoginUser().getId());
        logger.info("用户id:{}", id);
        ResponseJsonRender resultData = new ResponseJsonRender();
        // 要更新的数据
        BaseUserAuthPo baseUserAuthPo = new BaseUserAuthPo();


        int r = 0;
        if (r <= 0) {
            // 更新失败，资源不存在
            resultData.setCode(ResponseCode.E404_100001.getCode());
            resultData.setMsg(ResponseCode.E404_100001.getMsg());
            logger.info("code:{},msg:{}", resultData.getCode(), resultData.getMsg());
            logger.info("更新用户手机结束，失败");
            return new ResponseEntity(resultData, HttpStatus.NOT_FOUND);
        } else {
            // 更新成功，已被成功创建
            logger.info("更新的用户id:{}", id);
            logger.info("更新用户手机结束，成功");
            return new ResponseEntity(resultData, HttpStatus.CREATED);
        }
    }

    /**
     * 单资源，更新用户头像
     *
     * @param id
     * @param photoUrl
     *
     * @return
     */
    @OperationLog(operation = "用户接口", content = "单资源，更新用户头像")
    @RepeatFormValidator
    @RequiresPermissions("base:user:update:photo")
    @RequestMapping(value = "/user/{id}/photo", method = RequestMethod.PUT)
    public ResponseEntity updateUserPhoto(@PathVariable String id, String photoUrl) {
        logger.info("更新用户头像开始");
        logger.info("当前登录用户id:{}", getLoginUser().getId());
        logger.info("用户id:{}", id);
        ResponseJsonRender resultData = new ResponseJsonRender();

        BaseUserPo baseUserPo = new BaseUserPo();
        baseUserPo.setId(id);
        baseUserPo.setPhoto(photoUrl);

        baseUserPo = apiBaseUserPoService.preUpdate(baseUserPo, getLoginUser().getId());
        int r = apiBaseUserPoService.updateByPrimaryKeySelective(baseUserPo);

        if (r <= 0) {
            // 更新失败，资源不存在
            resultData.setCode(ResponseCode.E404_100001.getCode());
            resultData.setMsg(ResponseCode.E404_100001.getMsg());
            logger.info("code:{},msg:{}", resultData.getCode(), resultData.getMsg());
            logger.info("更新用户头像结束，失败");
            return new ResponseEntity(resultData, HttpStatus.NOT_FOUND);
        } else {
            // 更新成功，已被成功创建
            logger.info("更新的用户id:{}", id);
            logger.info("更新用户头像结束，成功");
            return new ResponseEntity(resultData, HttpStatus.CREATED);
        }
    }

    /**
     * 单资源，重置用户密码
     *
     * @param id
     * @param password
     *
     * @return
     */
    @OperationLog(operation = "用户接口", content = "单资源，重置用户密码")
    @RepeatFormValidator
    @RequiresPermissions("base:user:reset:password")
    @RequestMapping(value = "/user/{id}/password", method = RequestMethod.PUT)
    public ResponseEntity updateUserPassword(@PathVariable String id, String password) {
        logger.info("重置用户密码开始");
        logger.info("当前登录用户id:{}", getLoginUser().getId());
        logger.info("用户id:{}", id);
        ResponseJsonRender resultData = new ResponseJsonRender();
        PasswordAndSalt ps = PasswordAndSalt.entryptPassword(password);
        int r = apiBaseUserAuthPoService.updateCredential(ps.getPassword() + "_" + ps.getSalt(), id, getLoginUser().getId(),
                DictEnum.LoginType.ACCOUNT.name(), DictEnum.LoginType.MOBILE.name(), DictEnum.LoginType.EMAIL.name());

        if (r <= 0) {
            // 更新失败，资源不存在
            resultData.setCode(ResponseCode.E404_100001.getCode());
            resultData.setMsg(ResponseCode.E404_100001.getMsg());
            logger.info("code:{},msg:{}", resultData.getCode(), resultData.getMsg());
            logger.info("重置用户密码结束，失败");
            return new ResponseEntity(resultData, HttpStatus.NOT_FOUND);
        } else {
            // 更新成功，已被成功创建
            logger.info("重置用户密码的用户id:{}", id);
            logger.info("重置用户密码结束，成功");
            return new ResponseEntity(resultData, HttpStatus.CREATED);
        }
    }

    /**
     * 单资源，获取id用户
     *
     * @param id
     *
     * @return
     */
    @OperationLog(operation = "用户接口", content = "单资源，获取id用户")
    @RequiresPermissions("base:user:getById")
    @RequestMapping(value = "/user/{id}", method = RequestMethod.GET)
    public ResponseEntity getById(@PathVariable String id) {

        ResponseJsonRender resultData = new ResponseJsonRender();
        BaseUserDto baseUserDto = apiBaseUserPoService.selectByPrimaryKey(id, false);
        if (baseUserDto != null) {
            BaseUserVo vo = new BaseUserVo(baseUserDto);
            BaseUserAuthDto userAuthDto = apiBaseUserAuthPoService.selectByUserIdAndType(id, DictEnum.LoginType.ACCOUNT.name());
            if (userAuthDto != null) {
                vo.setAccount(userAuthDto.getIdentifier());
            }
            resultData.setData(vo);
            return new ResponseEntity(resultData, HttpStatus.OK);
        } else {
            resultData.setCode(ResponseCode.E404_100001.getCode());
            resultData.setMsg(ResponseCode.E404_100001.getMsg());
            return new ResponseEntity(resultData, HttpStatus.NOT_FOUND);
        }
    }
    /**
     * 单资源，获取详情
     *
     * @param id
     *
     * @return
     */
    @OperationLog(operation = "用户接口", content = "单资源，获取登录信息")
    @RequiresPermissions("base:user:detail")
    @RequestMapping(value = "/user/{id}/detail", method = RequestMethod.GET)
    public ResponseEntity userDetail(@PathVariable String id) {

        ResponseJsonRender resultData = new ResponseJsonRender();
        BaseUserDto baseUserDto = apiBaseUserPoService.selectByPrimaryKey(id, false);
        if (baseUserDto != null) {
            BaseUserVo vo = new BaseUserVo(baseUserDto);
            BaseUserAuthDto userAuthDto = apiBaseUserAuthPoService.selectByUserIdAndType(id, DictEnum.LoginType.ACCOUNT.name());
            if (userAuthDto != null) {
                vo.setAccount(userAuthDto.getIdentifier());
            }
            // 所在机构
            BaseOfficeDto officeDto = apiBaseOfficePoService.selectOfficeByUserId(id);
            if (officeDto != null) {
                vo.setDataOfficeName(officeDto.getName());
            }
            resultData.setData(vo);
            // 用户角色
            BaseRoleDto role = apiBaseRolePoService.selectByUserId(id);
            List<BaseRoleDto> roles = new ArrayList<>();
            if(role != null){
                roles.add(role);
            }
            resultData.addData("roles",roles);

            // 个人数据权限
            List<BaseUserDataScopeRelDto> userDataScopeRelDtos = apiBaseUserDataScopeRelPoService.selectByUserId(id);
            if (userDataScopeRelDtos != null) {
                List<String> dataScopeIds = new ArrayList<>(userDataScopeRelDtos.size());
                for (BaseUserDataScopeRelDto userDataScopeRelDto : userDataScopeRelDtos) {
                    dataScopeIds.add(userDataScopeRelDto.getDataScopeId());
                }
                List<BaseDataScopeDto> dataScopeDtos = apiBaseDataScopePoService.selectByPrimaryKeys(dataScopeIds,false);
                resultData.addData("personalDataScopes",dataScopeDtos);
            }

            // 登录情况
            List<org.apache.shiro.session.Session> sessions = ShiroUtils.getSessionsByUserId(id);
            if (sessions != null) {
                List<Map<String,Object>> loginInfo = new ArrayList<>();
                for (org.apache.shiro.session.Session session : sessions) {
                    Map<String,Object> item = new HashMap<>();
                    item.put("loginType",ShiroUtils.getLoginType(session));
                    item.put("loginClient",ShiroUtils.getLoginClient(session));
                    item.put("loginTime",ShiroUtils.getLoginTime(session));
                    item.put("lastAccessTime",ShiroUtils.getLastAccessTime(session));
                    item.put("host",ShiroUtils.getHost(session));
                    item.put("kickout",ShiroUtils.getKickout(session));
                    loginInfo.add(item);
                }
                resultData.addData("loginInfo",loginInfo);
            }

            return new ResponseEntity(resultData, HttpStatus.OK);
        } else {
            resultData.setCode(ResponseCode.E404_100001.getCode());
            resultData.setMsg(ResponseCode.E404_100001.getMsg());
            return new ResponseEntity(resultData, HttpStatus.NOT_FOUND);
        }
    }
    /**
     * 多资源，查询
     *
     * @param dto
     *
     * @return
     */
    @OperationLog(operation = "用户接口", content = "多资源，查询")
    @RequiresPermissions("base:user:search")
    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public ResponseEntity searchUsers(SearchBaseUsersConditionDto dto, boolean includeOfficeName) {

        ResponseJsonRender resultData = new ResponseJsonRender();
        PageAndOrderbyParamDto pageAndOrderbyParamDto = new PageAndOrderbyParamDto(PageUtils.getPageFromThreadLocal(), OrderbyUtils.getOrderbyFromThreadLocal());
        // 设置当前登录用户id
        dto.setCurrentUserId(getLoginUser().getId());
        dto.setCurrentRoleId(getLoginUserRoleId());
        PageResultDto<BaseUserDto> pageResultDto = apiBaseUserPoService.searchBaseUsersDsf(dto, pageAndOrderbyParamDto);

        if (CollectionUtils.isNotEmpty(pageResultDto.getData())) {
            // 添加帐号
            List<BaseUserVo> result = new ArrayList<>(pageResultDto.getData().size());
            for (BaseUserDto userDto : pageResultDto.getData()) {
                BaseUserAuthDto userAuthDto = apiBaseUserAuthPoService.selectByUserIdAndType(userDto.getId(), DictEnum.LoginType.ACCOUNT.name());
                BaseUserVo vo = new BaseUserVo(userDto);
                if (userAuthDto != null) {
                    vo.setAccount(userAuthDto.getIdentifier());
                }
                //机构名称
                if (includeOfficeName && StringUtils.isNotEmpty(userDto.getDataOfficeId())) {
                    BaseOfficeDto baseOfficeDto = apiBaseOfficePoService.selectByPrimaryKey(userDto.getDataOfficeId());
                    if (baseOfficeDto != null) {
                        vo.setDataOfficeName(baseOfficeDto.getName());
                    }
                }
                result.add(vo);
            }
            resultData.setData(result);
            resultData.setPage(pageResultDto.getPage());
            return new ResponseEntity(resultData, HttpStatus.OK);
        } else {
            resultData.setCode(ResponseCode.E404_100001.getCode());
            resultData.setMsg(ResponseCode.E404_100001.getMsg());
            return new ResponseEntity(resultData, HttpStatus.NOT_FOUND);
        }
    }

    // 以下是当前用户相关
    @RequiresPermissions("user")
    @RequestMapping(value = "/user/current", method = RequestMethod.GET)
    public ResponseEntity currentUserInfo() {

        ResponseJsonRender resultData = new ResponseJsonRender();
        ShiroUser su = getLoginUser();
        if (su != null) {
            resultData.setData(su);
            return new ResponseEntity(resultData, HttpStatus.OK);
        } else {
            resultData.setCode(ResponseCode.E404_100001.getCode());
            resultData.setMsg(ResponseCode.E404_100001.getMsg());
            return new ResponseEntity(resultData, HttpStatus.NOT_FOUND);
        }
    }

    /**
     * 单资源，更新当前用户头像
     *
     * @param photoUrl
     *
     * @return
     */
    @OperationLog(operation = "用户接口", content = "单资源，更新当前用户头像")
    @RepeatFormValidator
    @RequiresPermissions("user")
    @RequestMapping(value = "/user/photo/current", method = RequestMethod.PUT)
    public ResponseEntity updateCurrentUserPhoto(String photoUrl) {
        logger.info("更新当前用户头像开始");
        logger.info("当前登录用户id:{}", getLoginUser().getId());
        ResponseJsonRender resultData = new ResponseJsonRender();

        BaseUserPo baseUserPo = new BaseUserPo();
        baseUserPo.setId(getLoginUser().getId());
        baseUserPo.setPhoto(photoUrl);

        baseUserPo = apiBaseUserPoService.preUpdate(baseUserPo, getLoginUser().getId());
        int r = apiBaseUserPoService.updateByPrimaryKeySelective(baseUserPo);

        if (r <= 0) {
            // 更新失败，资源不存在
            resultData.setCode(ResponseCode.E404_100001.getCode());
            resultData.setMsg(ResponseCode.E404_100001.getMsg());
            logger.info("code:{},msg:{}", resultData.getCode(), resultData.getMsg());
            logger.info("更新当前用户头像结束，失败");
            return new ResponseEntity(resultData, HttpStatus.NOT_FOUND);
        } else {
            // 刷新用户信息
            ShiroUtils.refreshShiroUserInfo();
            // 更新成功，已被成功创建
            logger.info("更新当前用户头像结束，成功");
            return new ResponseEntity(resultData, HttpStatus.CREATED);
        }
    }

    /**
     * 踢除用户下线，使用户下线
     *
     * @param id
     *
     * @return
     */
    @OperationLog(operation = "用户接口", content = "使用户下线")
    @RepeatFormValidator
    @RequiresPermissions("base:user:kickoutClient")
    @RequestMapping(value = "/user/{id}/kickoutClient", method = RequestMethod.POST)
    public ResponseEntity kickoutClient(@PathVariable String id,String loginClient) {
        ResponseJsonRender resultData = new ResponseJsonRender();

        ShiroUtils.kickoutClient(id,loginClient);
        return new ResponseEntity(resultData, HttpStatus.OK);
    }
    /**
     * 修改当前用户密码
     *
     * @param password
     *
     * @return
     */
    @OperationLog(operation = "用户接口", content = "修改当前用户密码")
    @RepeatFormValidator
    @RequiresPermissions("user")
    @RequestMapping(value = "/user/password/current", method = RequestMethod.PUT)
    public ResponseEntity updateCurrentUserPassword(String oldPassword, String password) {
        logger.info("修改当前用户密码开始");
        logger.info("当前登录用户id:{}", getLoginUser().getId());
        ResponseJsonRender resultData = new ResponseJsonRender();

        BaseUserAuthDto userAuthDto = apiBaseUserAuthPoService.selectByUserIdAndType(getLoginUser().getId(), DictEnum.LoginType.ACCOUNT.name());
        if (userAuthDto == null) {
            // 更新失败，资源不存在
            resultData.setCode(ResponseCode.E404_100001.getCode());
            resultData.setMsg(ResponseCode.E404_100001.getMsg());
            logger.info("code:{},msg:{}", resultData.getCode(), resultData.getMsg());
            logger.info("修改当前用户密码结束，失败");
            return new ResponseEntity(resultData, HttpStatus.NOT_FOUND);
        }
        // 验证旧密码是否正确
        PasswordAndSalt oldPs = new PasswordAndSalt();
        String credentials[] = userAuthDto.getCredential().split("_");
        oldPs.setPassword(credentials[0]);
        oldPs.setSalt(credentials[1]);
        if (!ShiroUtils.validatePassword(oldPassword, oldPs)) {
            resultData.setCode(ResponseCode.E400_100001.getCode());
            resultData.setMsg(ResponseCode.E400_100001.getMsg());
            logger.info("code:{},msg:{}", resultData.getCode(), resultData.getMsg());
            logger.info("原密码不正确");
            logger.info("修改当前用户密码结束，失败");
            return new ResponseEntity(resultData, HttpStatus.BAD_REQUEST);
        }
        PasswordAndSalt ps = PasswordAndSalt.entryptPassword(password);
        int r = apiBaseUserAuthPoService.updateCredential(ps.getPassword() + "_" + ps.getSalt(), getLoginUser().getId(), getLoginUser().getId(),
                DictEnum.LoginType.ACCOUNT.name(), DictEnum.LoginType.MOBILE.name(), DictEnum.LoginType.EMAIL.name());

        if (r <= 0) {
            // 更新失败，资源不存在
            resultData.setCode(ResponseCode.E404_100001.getCode());
            resultData.setMsg(ResponseCode.E404_100001.getMsg());
            logger.info("code:{},msg:{}", resultData.getCode(), resultData.getMsg());
            logger.info("修改当前用户密码结束，失败");
            return new ResponseEntity(resultData, HttpStatus.NOT_FOUND);
        } else {
            // 更新成功，已被成功创建
            logger.info("修改当前用户密码结束，成功");
            return new ResponseEntity(resultData, HttpStatus.CREATED);
        }
    }


    /**
     * 用户注册
     * @param dto
     * @return
     */
    @RepeatFormValidator
    @RequestMapping(value = "/user/regist",method = RequestMethod.POST)
    public ResponseEntity userRegist(UserRegistFormDto dto){
        logger.info("用户注册开始");
        logger.info("用户注册信息参数:{}", ToStringBuilder.reflectionToString(dto, ToStringStyle.SHORT_PREFIX_STYLE));
        ResponseJsonRender resultData=new ResponseJsonRender();
        boolean mobile = false,email = false,account = false;
        if(StringUtils.isNotEmpty(dto.getMobile())){
            BaseUserAuthDto userAuthDto = apiBaseUserAuthPoService.selectByUserIdAndType(dto.getMobile(),DictEnum.LoginType.MOBILE.name());
            if(userAuthDto != null){
                resultData.setCode(ResponseCode.E409_100004.getCode());
                resultData.setMsg(ResponseCode.E409_100004.getMsg());
                logger.info("code:{},msg:{}",resultData.getCode(),resultData.getMsg());
                logger.info("用户注册结束，失败");
                return new ResponseEntity(resultData,HttpStatus.CONFLICT);
            }
            mobile = true;
        }
        if(StringUtils.isNotEmpty(dto.getEmail())){
            BaseUserAuthDto userAuthDto = apiBaseUserAuthPoService.selectByUserIdAndType(dto.getEmail(),DictEnum.LoginType.EMAIL.name());
            if(userAuthDto != null){
                resultData.setCode(ResponseCode.E409_100003.getCode());
                resultData.setMsg(ResponseCode.E409_100003.getMsg());
                logger.info("code:{},msg:{}",resultData.getCode(),resultData.getMsg());
                logger.info("用户注册结束，失败");
                return new ResponseEntity(resultData,HttpStatus.CONFLICT);
            }
            email = true;
        }
        if(StringUtils.isNotEmpty(dto.getAccount())){
            BaseUserAuthDto userAuthDto = apiBaseUserAuthPoService.selectByUserIdAndType(dto.getAccount(),DictEnum.LoginType.ACCOUNT.name());
            if(userAuthDto != null){
                resultData.setCode(ResponseCode.E409_100002.getCode());
                resultData.setMsg(ResponseCode.E409_100002.getMsg());
                logger.info("code:{},msg:{}",resultData.getCode(),resultData.getMsg());
                logger.info("用户注册结束，失败");
                return new ResponseEntity(resultData,HttpStatus.CONFLICT);
            }
            account = true;
        }
        //都不可以，
        if(!mobile && !email && !account){
            resultData.setCode(ResponseCode.E400_100000.getCode());
            resultData.setMsg(ResponseCode.E400_100000.getMsg());
            logger.info("code:{},msg:{}",resultData.getCode(),resultData.getMsg());
            logger.info("用户注册结束，失败");
            return new ResponseEntity(resultData,HttpStatus.CONFLICT);
        }

        BaseUserPo baseUserPo = new BaseUserPo();
        String nickname = dto.getNickname();
        if(StringUtils.isEmpty(nickname)){
            nickname = dto.getAccount();
        }
        if(StringUtils.isEmpty(nickname)){
            nickname = dto.getMobile();
        }
        if(StringUtils.isEmpty(nickname)){
            nickname = dto.getEmail();
        }
        if(StringUtils.isEmpty(nickname)){
            nickname = "昵称";
        }
        baseUserPo.setNickname(nickname);
        baseUserPo.setLocked(BasePo.YesNo.N.name());
        baseUserPo.setGender(DictEnum.Gender.unknown.name());
        baseUserPo = apiBaseUserPoService.preInsert(baseUserPo,BasePo.DEFAULT_USER_ID);
        baseUserPo = apiBaseUserPoService.insertSimple(baseUserPo);

        PasswordAndSalt ps = PasswordAndSalt.entryptPassword(dto.getPassword());
        String password = ps.getPassword() + "_" + ps.getSalt();
        BaseUserAuthPo baseUserAuthPo = null;
        if(mobile){
            baseUserAuthPo = new BaseUserAuthPo();
            baseUserAuthPo.setUserId(baseUserPo.getId());
            baseUserAuthPo.setIdentityType(DictEnum.LoginType.MOBILE.name());
            baseUserAuthPo.setIdentifier(dto.getMobile());
            baseUserAuthPo.setCredential(password);
            baseUserAuthPo.setVerified(BasePo.YesNo.Y.name());
            baseUserAuthPo = apiBaseUserAuthPoService.preInsert(baseUserAuthPo,BasePo.DEFAULT_USER_ID);
            apiBaseUserAuthPoService.insertSimple(baseUserAuthPo);
        }
        if(email){
            baseUserAuthPo = new BaseUserAuthPo();
            baseUserAuthPo.setUserId(baseUserPo.getId());
            baseUserAuthPo.setIdentityType(DictEnum.LoginType.EMAIL.name());
            baseUserAuthPo.setIdentifier(dto.getEmail());
            baseUserAuthPo.setCredential(password);
            baseUserAuthPo.setVerified(BasePo.YesNo.Y.name());
            baseUserAuthPo = apiBaseUserAuthPoService.preInsert(baseUserAuthPo,BasePo.DEFAULT_USER_ID);
            apiBaseUserAuthPoService.insertSimple(baseUserAuthPo);
        }
        if(account){
            baseUserAuthPo = new BaseUserAuthPo();
            baseUserAuthPo.setUserId(baseUserPo.getId());
            baseUserAuthPo.setIdentityType(DictEnum.LoginType.ACCOUNT.name());
            baseUserAuthPo.setIdentifier(dto.getAccount());
            baseUserAuthPo.setCredential(password);
            baseUserAuthPo.setVerified(BasePo.YesNo.Y.name());
            baseUserAuthPo = apiBaseUserAuthPoService.preInsert(baseUserAuthPo,BasePo.DEFAULT_USER_ID);
            apiBaseUserAuthPoService.insertSimple(baseUserAuthPo);
        }

        // 已被成功创建
        logger.info("注册用户id:{}",baseUserPo.getId());
        logger.info("用户注册结束，成功");
        return new ResponseEntity(resultData, HttpStatus.CREATED);
    }

    /**
     * 发送验证码
     * @param type 方式，mobile=手机短信验证，email=邮箱验证
     * @param identifier 手机号还是邮箱
     * @return
     */
    @RepeatFormValidator
    @RequestMapping(value = "/user/sendcode",method = RequestMethod.POST)
    public ResponseEntity sendCode(String type,String identifier){
        logger.info("发送找回密码验证码开始");
        logger.info("参数:type={},identifier={}",type,identifier);
        ResponseJsonRender resultData=new ResponseJsonRender();
        Session session  = EmailUtils.createSession("smtp.sina.com","feihua666@sina.com","xiaobudian123",true,false,"25");
        Mail mail = new Mail();
        mail.addToAddress(identifier);
        mail.setSubject("忘记密码找回");
        mail.setFrom("feihua666@sina.com");
        String random = RandomStringUtils.randomNumeric(6);
        int expireSecond = 600;
        mail.setContent("您用于密码找回的验证码为" + random + "。有效时间为" + expireSecond/60 + "分钟");
        JedisUtils.set(type + identifier,random,expireSecond);
        try {
            if (DictEnum.LoginType.EMAIL.name().equalsIgnoreCase(type)) {
                EmailUtils.send(session,mail);
            }
        }catch (Exception e) {
            // 可能输入邮箱不 正确
            resultData.setCode(ResponseCode.E500_100000.getCode());
            resultData.setMsg(ResponseCode.E500_100000.getMsg());
            logger.info("code:{},msg:{}",resultData.getCode(),resultData.getMsg());
            logger.info("发送找回密码验证码结束，失败");
            return new ResponseEntity(resultData,HttpStatus.NOT_FOUND);
        }
        // 更新成功，已被成功创建
        logger.info("发送的验证码为:{}",random);
        logger.info("发送找回密码验证码结束，成功");
        return new ResponseEntity(resultData, HttpStatus.CREATED);
    }


    /**
     * 忘记密码，更新密码
     * @param password
     * @return
     */
    @RepeatFormValidator
    @RequestMapping(value = "/user/password/forget/find",method = RequestMethod.PUT)
    public ResponseEntity updateForgetPassword(String type,String identifier,String password,String code){
        logger.info("忘记密码修改密码开始");
        ResponseJsonRender resultData=new ResponseJsonRender();
        BaseUserAuthDto userAuthDto = null;
        if (StringUtils.equals(type, DictEnum.LoginType.EMAIL.name())) {
            userAuthDto = apiBaseUserAuthPoService.selectByIdentifierAndType(identifier,DictEnum.LoginType.EMAIL.name());

        }else  if (StringUtils.equals(type, DictEnum.LoginType.MOBILE.name())) {
            userAuthDto = apiBaseUserAuthPoService.selectByIdentifierAndType(identifier,DictEnum.LoginType.MOBILE.name());

        }

        if (userAuthDto == null) {
            // 更新失败，资源不存在
            resultData.setCode(ResponseCode.E404_100001.getCode());
            resultData.setMsg(ResponseCode.E404_100001.getMsg());
            logger.info("code:{},msg:{}",resultData.getCode(),resultData.getMsg());
            logger.info("忘记密码修改密码结束，失败");
            return new ResponseEntity(resultData,HttpStatus.NOT_FOUND);
        }
        // 验证验证码是否正确
        String sendedCode = JedisUtils.get(type + identifier);
        if(StringUtils.isEmpty(code) || !StringUtils.equals(sendedCode,code)){
            resultData.setCode(ResponseCode.E400_100001.getCode());
            resultData.setMsg(ResponseCode.E400_100001.getMsg());
            logger.info("code:{},msg:{}",resultData.getCode(),resultData.getMsg());
            logger.info("验证码不正确");
            logger.info("忘记密码修改密码结束，失败");
            return new ResponseEntity(resultData,HttpStatus.BAD_REQUEST);
        }
        PasswordAndSalt ps = PasswordAndSalt.entryptPassword(password);
        int r = apiBaseUserAuthPoService.updateCredential(ps.getPassword() + "_" + ps.getSalt(),getLoginUser().getId(),getLoginUser().getId(),
                DictEnum.LoginType.ACCOUNT.name(),DictEnum.LoginType.MOBILE.name(),DictEnum.LoginType.EMAIL.name());

        JedisUtils.del(type + identifier);
        if (r <= 0) {
            // 更新失败，资源不存在
            resultData.setCode(ResponseCode.E404_100001.getCode());
            resultData.setMsg(ResponseCode.E404_100001.getMsg());
            logger.info("code:{},msg:{}",resultData.getCode(),resultData.getMsg());
            logger.info("忘记密码修改密码结束，失败");
            return new ResponseEntity(resultData,HttpStatus.NOT_FOUND);
        }else{
            // 更新成功，已被成功创建
            logger.info("忘记密码修改密码结束，成功");
            return new ResponseEntity(resultData, HttpStatus.CREATED);
        }
    }
}
