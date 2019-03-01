package com.wwd.web.modules.wwd.mvc;

import com.feihua.exception.DataNotFoundException;
import com.feihua.framework.base.modules.area.api.ApiBaseAreaPoService;
import com.feihua.framework.base.modules.area.dto.BaseAreaDto;
import com.feihua.framework.base.modules.dict.api.ApiBaseDictPoService;
import com.feihua.framework.base.modules.dict.po.BaseDictPo;
import com.feihua.framework.base.modules.rel.api.ApiBaseUserRoleRelPoService;
import com.feihua.framework.base.modules.rel.dto.BaseUserRoleRelDto;
import com.feihua.framework.base.modules.rel.dto.UserBindRolesParamDto;
import com.feihua.framework.base.modules.role.api.ApiBaseRolePoService;
import com.feihua.framework.base.modules.role.dto.BaseRoleDto;
import com.feihua.framework.base.modules.user.api.ApiBaseUserPoService;
import com.feihua.framework.base.modules.user.dto.BaseUserDto;
import com.feihua.framework.base.modules.user.po.BaseUserPo;
import com.feihua.framework.constants.DictEnum;
import com.feihua.framework.utils.AliOssClientHelper;
import com.feihua.utils.calendar.CalendarUtils;
import com.feihua.utils.graphic.ImageUtils;
import com.feihua.utils.http.httpServletRequest.RequestUtils;
import com.feihua.utils.http.httpclient.HttpClientUtils;
import com.feihua.wechat.common.api.ApiWeixinUserPoService;
import com.feihua.wechat.common.dto.WeixinUserDto;
import com.feihua.wechat.common.po.WeixinUserPo;
import com.wwd.Constants;
import com.wwd.frameworksupport.UserAuthHelper;
import com.wwd.service.modules.wwd.api.*;
import com.wwd.service.modules.wwd.dto.*;
import com.wwd.service.modules.wwd.po.WwdUserAreaPo;
import com.wwd.service.modules.wwd.po.WwdUserInvitationPo;
import com.wwd.service.modules.wwd.po.WwdUserPicPo;
import com.wwd.service.modules.wwd.po.WwdUserPo;
import com.feihua.framework.rest.ResponseJsonRender;
import com.feihua.framework.rest.interceptor.RepeatFormValidator;
import com.feihua.framework.rest.modules.common.mvc.BaseController;
import com.feihua.utils.http.httpServletResponse.ResponseCode;
import com.wwd.web.modules.wwd.dto.UpdateWwdUserFormDto;
import feihua.jdbc.api.pojo.BasePo;
import feihua.jdbc.api.pojo.PageAndOrderbyParamDto;
import feihua.jdbc.api.pojo.PageResultDto;
import feihua.jdbc.api.utils.OrderbyUtils;
import feihua.jdbc.api.utils.PageUtils;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.shiro.SecurityUtils;
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

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 汪汪队用户管理
 * Created by yangwei
 */
@RestController
@RequestMapping("/wwd")
public class WwdUserController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(WwdUserController.class);

    @Autowired
    private ApiWwdUserPoService apiWwdUserPoService;
    @Autowired
    private ApiWwdUserAreaPoService apiWwdUserAreaPoService;
    @Autowired
    private ApiWwdUserPicPoService apiWwdUserPicPoService;
    @Autowired
    private ApiWwdUserInvitationPoService apiWwdUserInvitationPoService;
    @Autowired
    private ApiBaseAreaPoService apiBaseAreaPoService;
    @Autowired
    private ApiBaseUserPoService apiBaseUserPoService;
    @Autowired
    private ApiWwdUserEnjoyPoService apiWwdUserEnjoyPoService;
    @Autowired
    private ApiWeixinUserPoService apiWeixinUserPoService;
    @Autowired
    private UserAuthHelper userAuthHelper;
    @Autowired
    private ApiBaseRolePoService apiBaseRolePoService;

    @Autowired
    private ApiBaseUserRoleRelPoService apiBaseUserRoleRelPoService;

    /**
     * 单资源，更新
     * @param id
     * @param updateFormDto
     * @return
     */
    @RepeatFormValidator
    @RequiresPermissions("wwd:user:update")
    @RequestMapping(value = "/user/{id}",method = RequestMethod.PUT)
    public ResponseEntity update(@PathVariable String id, UpdateWwdUserFormDto updateFormDto){
        logger.info("更新汪汪队用户开始");
        logger.info("当前登录用户id:{}",getLoginUser().getId());
        logger.info("汪汪队用户id:{}",id);
        ResponseJsonRender resultData=new ResponseJsonRender();
        // 表单值设置
        WwdUserPo basePo = new WwdUserPo();
        // id
        basePo.setId(id);
        //basePo.setDataOfficeId(updateFormDto.getDataOfficeId());
        //todo

        // 用条件更新，乐观锁机制
        WwdUserPo basePoCondition = new WwdUserPo();
        basePoCondition.setId(id);
        basePoCondition.setDelFlag(BasePo.YesNo.N.name());
        basePoCondition.setUpdateAt(updateFormDto.getUpdateTime());
        apiWwdUserPoService.preUpdate(basePo,getLoginUser().getId());
        int r = apiWwdUserPoService.updateSelective(basePo,basePoCondition);
        if (r <= 0) {
            // 更新失败，资源不存在
            resultData.setCode(ResponseCode.E404_100001.getCode());
            resultData.setMsg(ResponseCode.E404_100001.getMsg());
            logger.info("code:{},msg:{}",resultData.getCode(),resultData.getMsg());
            logger.info("更新汪汪队用户结束，失败");
            return new ResponseEntity(resultData,HttpStatus.NOT_FOUND);
        }else{
            // 更新成功，已被成功创建
            logger.info("更新的汪汪队用户id:{}",id);
            logger.info("更新汪汪队用户结束，成功");
            return new ResponseEntity(resultData, HttpStatus.CREATED);
        }
    }
    /**
     * 单资源，更新当前用户资料
     * @param updateFormDto
     * @return
     */
    @RepeatFormValidator
    @RequiresPermissions("wwd:user:current:update")
    @RequestMapping(value = "/user/current",method = RequestMethod.PUT)
    public ResponseEntity updateCurrentUser(UpdateWwdUserFormDto updateFormDto){
        logger.info("更新当前汪汪队用户开始");
        logger.info("当前登录用户id:{}",getLoginUser().getId());
        ResponseJsonRender resultData=new ResponseJsonRender();

        WwdUserDto wwdUserDto = apiWwdUserPoService.selectByUserId(getLoginUserId());

        // 表单值设置
        WwdUserPo basePo = new WwdUserPo();
        // id
        basePo.setId(wwdUserDto.getId());

        basePo.setName(updateFormDto.getName());
        basePo.setMajor(updateFormDto.getMajor());
        basePo.setCollege(updateFormDto.getCollege());
        basePo.setLooks(updateFormDto.getLooks());
        basePo.setBloodType(updateFormDto.getBloodType());
        basePo.setNickname(updateFormDto.getNickname());
        basePo.setAcademic(updateFormDto.getAcademic());
        basePo.setGender(updateFormDto.getGender());
        basePo.setMobile(updateFormDto.getMobile());
        basePo.setSmoking(updateFormDto.getSmoking());
        basePo.setShape(updateFormDto.getShape());
        basePo.setDrinking(updateFormDto.getDrinking());
        basePo.setHeight(updateFormDto.getHeight());
        basePo.setHasHourse(updateFormDto.getHasHourse());
        basePo.setYearSalary(updateFormDto.getYearSalary());
        basePo.setProfession(updateFormDto.getProfession());
        basePo.setWeight(updateFormDto.getWeight());
        basePo.setHasCar(updateFormDto.getHasCar());
        //默认只要修改，就已认证
        basePo.setIsverified(BasePo.YesNo.Y.name());
        // 设置完成，默认展示
        basePo.setShowInList(BasePo.YesNo.Y.name());
        basePo.setStandard(updateFormDto.getStandard());
        basePo.setEducation(updateFormDto.getEducation());
        basePo.setCardNo(updateFormDto.getCardNo());
        basePo.setConstellation(updateFormDto.getConstellation());
        basePo.setMonthSalary(updateFormDto.getMonthSalary());
        basePo.setDescription(updateFormDto.getDescription());
        basePo.setWechatNumber(updateFormDto.getWechatNumber());
        basePo.setMaritalStatus(updateFormDto.getMaritalStatus());
        basePo.setBirthDay(updateFormDto.getBirthDay());

        // 用条件更新，乐观锁机制
        WwdUserPo basePoCondition = new WwdUserPo();
        basePoCondition.setId(wwdUserDto.getId());
        basePoCondition.setDelFlag(BasePo.YesNo.N.name());
        basePoCondition.setUpdateAt(updateFormDto.getUpdateTime());
        apiWwdUserPoService.preUpdate(basePo,getLoginUser().getId());
        int r = apiWwdUserPoService.updateSelective(basePo,basePoCondition);

        // 更新区域
        WwdUserAreaPo wwdUserAreaPo = new WwdUserAreaPo();
        WwdUserAreaDto wwdUserAreaDto = apiWwdUserAreaPoService.selectByWwdUserId(wwdUserDto.getId());
        wwdUserAreaPo.setId(wwdUserAreaDto.getId());
        List<String> nowAreaIds = updateFormDto.getNowAreaIds();
        if (nowAreaIds != null) {
            wwdUserAreaPo.setNowProvinceId(nowAreaIds.get(0));
            wwdUserAreaPo.setNowCityId(nowAreaIds.get(1));
            wwdUserAreaPo.setNowDistrictId(nowAreaIds.get(2));

            BaseAreaDto nowProvince = apiBaseAreaPoService.selectByPrimaryKey(nowAreaIds.get(0));
            if(nowProvince != null){
                wwdUserAreaPo.setNowProvinceName(nowProvince.getName());
            }
            BaseAreaDto nowCity = apiBaseAreaPoService.selectByPrimaryKey(nowAreaIds.get(1));
            if(nowCity != null){
                wwdUserAreaPo.setNowCityName(nowCity.getName());
            }
            BaseAreaDto nowDistrict = apiBaseAreaPoService.selectByPrimaryKey(nowAreaIds.get(2));
            if(nowDistrict != null){
                wwdUserAreaPo.setNowDistrictName(nowDistrict.getName());
            }
        }
        List<String> homeAreaIds = updateFormDto.getHomeAreaIds();
        if (homeAreaIds != null) {
            wwdUserAreaPo.setHomeProvinceId(homeAreaIds.get(0));
            wwdUserAreaPo.setHomeCityId(homeAreaIds.get(1));
            wwdUserAreaPo.setHomeDistrictId(homeAreaIds.get(2));

            BaseAreaDto homeProvince = apiBaseAreaPoService.selectByPrimaryKey(homeAreaIds.get(0));
            if(homeProvince != null){
                wwdUserAreaPo.setHomeProvinceName(homeProvince.getName());
            }
            BaseAreaDto homeCity = apiBaseAreaPoService.selectByPrimaryKey(homeAreaIds.get(1));
            if(homeCity != null){
                wwdUserAreaPo.setHomeCityName(homeCity.getName());
            }
            BaseAreaDto homeDistrict = apiBaseAreaPoService.selectByPrimaryKey(homeAreaIds.get(2));
            if(homeDistrict != null){
                wwdUserAreaPo.setHomeDistrictName(homeDistrict.getName());
            }
        }
        apiWwdUserAreaPoService.preUpdate(wwdUserAreaPo,getLoginUserId());
        apiWwdUserAreaPoService.updateByPrimaryKeySelective(wwdUserAreaPo);

        if (r <= 0) {
            // 更新失败，资源不存在
            resultData.setCode(ResponseCode.E404_100001.getCode());
            resultData.setMsg(ResponseCode.E404_100001.getMsg());
            logger.info("code:{},msg:{}",resultData.getCode(),resultData.getMsg());
            logger.info("更新当前汪汪队用户结束，失败");
            return new ResponseEntity(resultData,HttpStatus.NOT_FOUND);
        }else{
            // 更新成功，已被成功创建
            logger.info("更新的汪汪队用户id:{}",wwdUserDto.getId());
            logger.info("更新当前汪汪队用户结束，成功");
            return new ResponseEntity(resultData, HttpStatus.CREATED);
        }
    }

    /**
     * 单资源，更新当前用户资料
     * @return
     */
    @RepeatFormValidator
    @RequiresPermissions("wwd:user:current:showinlist:update")
    @RequestMapping(value = "/user/current/showinlist",method = RequestMethod.PUT)
    public ResponseEntity updateCurrentUserShowInList(String showInList){
        logger.info("更新汪汪队用户显示到列表开始");
        logger.info("当前登录用户id:{}",getLoginUser().getId());
        ResponseJsonRender resultData=new ResponseJsonRender();

        WwdUserDto wwdUserDto = apiWwdUserPoService.selectByUserId(getLoginUserId());
        if(BasePo.YesNo.N.name().equals(wwdUserDto.getIsverified())){
            // 更新失败，先完善资料
            resultData.setCode(ResponseCode.E409_100001.getCode());
            resultData.setMsg(ResponseCode.E409_100001.getMsg());
            logger.info("code:{},msg:{}",resultData.getCode(),resultData.getMsg());
            logger.info("更新汪汪队用户显示到列表结束，失败");
            return new ResponseEntity(resultData,HttpStatus.NOT_FOUND);
        }
        // 表单值设置
        WwdUserPo basePo = new WwdUserPo();
        // id
        basePo.setId(wwdUserDto.getId());
        basePo.setShowInList(showInList);
        apiWwdUserPoService.preUpdate(basePo,getLoginUserId());
        int r = apiWwdUserPoService.updateByPrimaryKeySelective(basePo);

        if (r <= 0) {
            // 更新失败，资源不存在
            resultData.setCode(ResponseCode.E404_100001.getCode());
            resultData.setMsg(ResponseCode.E404_100001.getMsg());
            logger.info("code:{},msg:{}",resultData.getCode(),resultData.getMsg());
            logger.info("更新汪汪队用户显示到列表结束，失败");
            return new ResponseEntity(resultData,HttpStatus.NOT_FOUND);
        }else{
            // 更新成功，已被成功创建
            logger.info("更新的汪汪队用户id:{}",wwdUserDto.getId());
            logger.info("更新汪汪队用户显示到列表结束，成功");
            return new ResponseEntity(resultData, HttpStatus.CREATED);
        }
    }
    /**
     * 单资源，获取id汪汪队用户
     * @param id
     * @return
     */
    @RepeatFormValidator
    @RequiresPermissions("wwd:user:getById")
    @RequestMapping(value = "/user/{id}",method = RequestMethod.GET)
    public ResponseEntity getById(@PathVariable String id){

        ResponseJsonRender resultData=new ResponseJsonRender();
        WwdUserDto wwdUserDto = apiWwdUserPoService.selectByPrimaryKey(id,false);
        if (wwdUserDto != null) {
            BaseUserDto userDto = apiBaseUserPoService.selectByPrimaryKey(wwdUserDto.getUserId());
            wwdUserDto.setWechatNumber(null);
            resultData.addData("photo",userDto.getPhoto());
        }
        return super.returnDto(wwdUserDto,resultData);
    }
    /**
     * 单资源，获取当前用户
     * @return
     */
    @RepeatFormValidator
    @RequiresPermissions("wwd:user:current:getById")
    @RequestMapping(value = "/user/current",method = RequestMethod.GET)
    public ResponseEntity getCurrentUserById(){

        ResponseJsonRender resultData=new ResponseJsonRender();
        String userId = getLoginUser().getId();
        WwdUserDto userDto = apiWwdUserPoService.selectByUserId(userId);
        WwdUserDto baseDataScopeDto = apiWwdUserPoService.selectByPrimaryKey(userDto.getId(),false);
        return super.returnDto(baseDataScopeDto,resultData);
    }


    /**
     * 单资源，获取id汪汪队用户未使用的邀请码
     * @param id wwd_user_id
     * @return
     */
    @RepeatFormValidator
    @RequiresPermissions("wwd:user:invitation:getById")
    @RequestMapping(value = "/user/{id}/invitation",method = RequestMethod.GET)
    public ResponseEntity getInvitationCode(@PathVariable String id){

        ResponseJsonRender resultData=new ResponseJsonRender();
        List<WwdUserInvitationDto> list = apiWwdUserInvitationPoService.selectUnUsedByWwdUserId(id);
        return super.returnList(list,resultData);
    }
    /**
     * 单资源，获取当前用户未使用的邀请码
     * @return
     */
    @RepeatFormValidator
    @RequiresPermissions("wwd:user:invitation:current:getById")
    @RequestMapping(value = "/user/current/invitation",method = RequestMethod.GET)
    public ResponseEntity getCurrentUserInvitationCode(){

        ResponseJsonRender resultData=new ResponseJsonRender();

        String userId = getLoginUser().getId();
        WwdUserDto userDto = apiWwdUserPoService.selectByUserId(userId);
        List<WwdUserInvitationDto> list = apiWwdUserInvitationPoService.selectUnUsedByWwdUserId(userDto.getId());
        //如果没有生成一个
        if (list == null || list.isEmpty()) {
            list = new ArrayList<>();
            list.add(apiWwdUserInvitationPoService.generateForWwdUserId(userDto.getId()));
        }
        return super.returnList(list,resultData);
    }


    /**
     * 复数资源，搜索汪汪队用户
     * @param dto
     * @return
     */
    @RepeatFormValidator
    @RequiresPermissions("wwd:user:search")
    @RequestMapping(value = "/users",method = RequestMethod.GET)
    public ResponseEntity search(SearchWwdUsersConditionDto dto,boolean includePic){

        ResponseJsonRender resultData=new ResponseJsonRender();
        PageAndOrderbyParamDto pageAndOrderbyParamDto = new PageAndOrderbyParamDto(PageUtils.getPageFromThreadLocal(), OrderbyUtils.getOrderbyFromThreadLocal());
        // 设置当前登录用户id
        dto.setCurrentUserId(getLoginUser().getId());
        dto.setCurrentRoleId(getLoginUserRoleId());
        PageResultDto<WwdUserPageDto> list = apiWwdUserPoService.searchWwdUsersDsfMultiTable(dto,pageAndOrderbyParamDto);
        if (list != null && list.getData() != null && !list.getData().isEmpty()) {
            List<String> ids = new ArrayList<>(list.getData().size());
            for (WwdUserPageDto wwdUserPageDto : list.getData()) {
                wwdUserPageDto.getWwdUserDto().setWechatNumber(null);
                ids.add(wwdUserPageDto.getWwdUserDto().getUserId());
            }
            List<BaseUserDto> userList = apiBaseUserPoService.selectByPrimaryKeys(ids,false);
            Map<String,Object> photos = new HashMap<>();
            for (BaseUserDto userDto : userList) {
                photos.put(userDto.getId(),userDto.getPhoto());
            }

            resultData.addData("photo",photos);
        }
        return super.returnPageResultDto(list,resultData);
    }
    /**
     * 单资源，获取id汪汪队用户未使用的邀请码
     * @param id wwd_user_id
     * @return
     */
    @RepeatFormValidator
    @RequiresPermissions("wwd:user:wechatNumber:getById")
    @RequestMapping(value = "/user/{id}/wechatNumber",method = RequestMethod.GET)
    public ResponseEntity getWechatNumber(@PathVariable String id){

        ResponseJsonRender resultData=new ResponseJsonRender();
        WwdUserDto userDto = apiWwdUserPoService.selectByUserId(getLoginUserId());
        // 我是否对他有意思
        WwdUserEnjoyDto wwdUserEnjoyDtoMy = apiWwdUserEnjoyPoService.selectEnjoyedFromTo(userDto.getId(), id);
        // 他是否对我有意思
        WwdUserEnjoyDto wwdUserEnjoyDtoIt = apiWwdUserEnjoyPoService.selectEnjoyedFromTo( id,userDto.getId());
        Map<String,Object> r = null;
        if (wwdUserEnjoyDtoIt != null && wwdUserEnjoyDtoMy != null) {
            r = new HashMap<>();
            userDto = apiWwdUserPoService.selectByPrimaryKey(id);
            r.put("wechatNumber",userDto.getWechatNumber());
        }
        return super.returnDto( r,resultData);
    }
    /**
     * 复数资源，搜索我邀请的朋友-汪汪队用户
     *
     * @return
     */
    @RepeatFormValidator
    @RequiresPermissions("wwd:user:myfriends")
    @RequestMapping(value = "/myfriends", method = RequestMethod.GET)
    public ResponseEntity myfriends() {
        String userId = getLoginUser().getId();
        WwdUserDto userDto = apiWwdUserPoService.selectByUserId(userId);
        ResponseJsonRender resultData = new ResponseJsonRender();
        List<WwdUserDto> wwdUserDtos = null;
        PageAndOrderbyParamDto pageAndOrderbyParamDto = new PageAndOrderbyParamDto(PageUtils.getPageFromThreadLocal(), OrderbyUtils.getOrderbyFromThreadLocal());

        PageResultDto<WwdUserInvitationDto> wwdUserInvitationPageDtos = apiWwdUserInvitationPoService.selectUsedByWwdUserId(userDto.getId(),pageAndOrderbyParamDto);
        resultData.setPage(wwdUserInvitationPageDtos.getPage());
        List<WwdUserInvitationDto> wwdUserInvitationDtos = wwdUserInvitationPageDtos.getData();
        if (wwdUserInvitationDtos!=null && wwdUserInvitationDtos.size()>0) {
            List<String> keys = new ArrayList<>();
            for (WwdUserInvitationDto wwdUserInvitationDto : wwdUserInvitationDtos) {
                keys.add(wwdUserInvitationDto.getInvitedWwdUserId());
            }
            wwdUserDtos = apiWwdUserPoService.selectByPrimaryKeys(keys, false);
            if (wwdUserDtos!=null && wwdUserDtos.size()>0) {
                Map<String, String> picMap = null;
                List<Map<String, String>> picList = new ArrayList<>();
                for (WwdUserDto dto : wwdUserDtos) {
                    dto.setWechatNumber(null);
                    List<WwdUserPicDto> picDtos = apiWwdUserPicPoService.selectByWwdUserId(dto.getId());
                    if (picDtos != null && !picDtos.isEmpty()) {
                        for (WwdUserPicDto picDto : picDtos) {
                            if ("main".equals(picDto.getType())) {
                                picMap = new HashMap<>();
                                picMap.put("picUrl", picDto.getPicOriginUrl());
                                picMap.put("wwdUserId", dto.getId());
                                picList.add(picMap);
                                break;
                            }
                        }
                    }
                }
                resultData.addData("pic",picList);
            }
        }
        return super.returnList(wwdUserDtos, resultData);
    }




    /**
     * 接受邀请
     * @param inviteCode
     * @return
     */
    @RepeatFormValidator
    @RequestMapping(value = "/user/acceptInvited",method = RequestMethod.POST)
    public ResponseEntity acceptInvited(String inviteCode,String type,String which){
        ResponseJsonRender resultData=new ResponseJsonRender();
        WwdUserInvitationDto invitationDto = apiWwdUserInvitationPoService.selectUnUsedByCode(inviteCode);
        // 邀请码不存在
        if(invitationDto == null) {
            throw new DataNotFoundException("inviteCode not exists","E404_invalidcode",404);
        }

        String openid = (String) SecurityUtils.getSubject().getSession().getAttribute("publickplatform_openid_" + which);
        if(StringUtils.isEmpty(openid)){
            throw new DataNotFoundException("can not get user openid","E404_no_openid",404);
        }

        // 根据openid查询是否存在数据
        WeixinUserPo weixinUserPoCondition = new WeixinUserPo();
        weixinUserPoCondition.setOpenid(openid);
        // DictEnum.WeixinType.publicplatform.name()
        weixinUserPoCondition.setType(type);
        weixinUserPoCondition.setWhich(which);
        weixinUserPoCondition.setDelFlag(BasePo.YesNo.N.name());

        // 这里一定是有值的，没有，请在页面重新获取用户信息
        WeixinUserDto weixinUserDtoDb = apiWeixinUserPoService.selectOne(weixinUserPoCondition);
        BaseUserPo baseUserPo = null;
        if(StringUtils.isEmpty(weixinUserDtoDb.getUserId())){
            baseUserPo = userAuthHelper.generateUserAuth(weixinUserDtoDb);

            // 添加wwduser
            WwdUserPo wwdUserPo = new WwdUserPo();
            wwdUserPo.setUserId(baseUserPo.getId());
            wwdUserPo.setNickname(baseUserPo.getNickname());
            wwdUserPo.setName(baseUserPo.getNickname());
            wwdUserPo.setGender(baseUserPo.getGender());
            wwdUserPo.setIsverified(BasePo.YesNo.N.name());
            wwdUserPo.setShowInList(BasePo.YesNo.N.name());
            apiWwdUserPoService.preInsert(wwdUserPo,BasePo.DEFAULT_USER_ID);
            WwdUserDto wwdUserDto = apiWwdUserPoService.insertSelective(wwdUserPo);
            // 添加一个图片，默认以头像做为主图
            WwdUserPicPo wwdUserPicPo = new WwdUserPicPo();
            wwdUserPicPo.setWwdUserId(wwdUserDto.getId());
            wwdUserPicPo.setSequence(1);
            wwdUserPicPo.setType(Constants.WwdUserPicType.main.name());
            wwdUserPicPo.setPicOriginUrl(baseUserPo.getPhoto());
            wwdUserPicPo.setPicThumbUrl(baseUserPo.getPhoto());
            apiWwdUserPicPoService.preInsert(wwdUserPicPo,BasePo.DEFAULT_USER_ID);
            apiWwdUserPicPoService.insertSelective(wwdUserPicPo);



            // 正常情况
            //将邀请码标记为已使用
            WwdUserInvitationPo wwdUserInvitationPo = new WwdUserInvitationPo();
            wwdUserInvitationPo.setId(invitationDto.getId());
            wwdUserInvitationPo.setIsUsed(BasePo.YesNo.Y.name());
            wwdUserInvitationPo.setInvitedWwdUserId(wwdUserDto.getId());
            apiWwdUserInvitationPoService.preUpdate(wwdUserInvitationPo,BasePo.DEFAULT_USER_ID);
            apiWwdUserInvitationPoService.updateByPrimaryKeySelective(wwdUserInvitationPo);

            // 添加一条区域空数据，以备以后只做更新
            WwdUserAreaPo wwdUserAreaPo = new WwdUserAreaPo();
            wwdUserAreaPo.setWwdUserId(wwdUserDto.getId());
            apiWwdUserAreaPoService.preInsert(wwdUserAreaPo,BasePo.DEFAULT_USER_ID);
            apiWwdUserAreaPoService.insertSelective(wwdUserAreaPo);

        }


        if (baseUserPo != null) {
            //给用户分配小程序角色
            String roleCode = "wwd_mini_program_role";
            BaseRoleDto baseRoleDto = apiBaseRolePoService.selectByCode(roleCode);
            if(baseRoleDto != null){

                // 查询用户是否绑定了角色
                BaseUserRoleRelDto dbrel = apiBaseUserRoleRelPoService.selectByUserIdAndRoleId(baseUserPo.getId(),baseRoleDto.getId());

                if (dbrel == null) {
                    UserBindRolesParamDto userBindRolesParamDto = new UserBindRolesParamDto();
                    userBindRolesParamDto.setCurrentUserId(BasePo.DEFAULT_USER_ID);
                    userBindRolesParamDto.setUserId(baseUserPo.getId());
                    List<String> roleIds = new ArrayList<>(1);
                    roleIds.add(baseRoleDto.getId());
                    userBindRolesParamDto.setRoleIds(roleIds);
                    apiBaseUserRoleRelPoService.userBindRoles(userBindRolesParamDto);
                }

            }
        }
        return new ResponseEntity(resultData, HttpStatus.CREATED);
    }

}
