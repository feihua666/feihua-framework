package com.feihua.wechat.rest.publicplatform.mvc;

import com.feihua.framework.base.modules.role.dto.BaseRoleDto;
import com.feihua.framework.rest.ResponseJsonRender;
import com.feihua.framework.rest.interceptor.RepeatFormValidator;
import com.feihua.framework.rest.mvc.SuperController;
import com.feihua.framework.shiro.utils.ShiroUtils;
import com.feihua.utils.http.httpServletResponse.ResponseCode;
import com.feihua.wechat.publicplatform.PublicUtils;
import com.feihua.wechat.publicplatform.dto.*;
import feihua.jdbc.api.pojo.*;
import feihua.jdbc.api.utils.OrderbyUtils;
import feihua.jdbc.api.utils.PageUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
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
import com.feihua.wechat.publicplatform.api.ApiWeixinMenuPoService;
import com.feihua.wechat.rest.publicplatform.dto.AddWeixinMenuFormDto;
import com.feihua.wechat.rest.publicplatform.dto.UpdateWeixinMenuFormDto;
import com.feihua.wechat.publicplatform.po.WeixinMenuPo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 微信菜单管理
 * Created by yangwei
 */
@RestController
@RequestMapping("/weixinmenu")
public class WeixinMenuController extends SuperController {

    private static Logger logger = LoggerFactory.getLogger(WeixinMenuController.class);

    @Autowired
    private ApiWeixinMenuPoService apiWeixinMenuPoService;

    /**
     * 单资源，添加
     * @param addFormDto
     * @return
     */
    @RepeatFormValidator
    @RequiresPermissions("weixinmenu:add")
    @RequestMapping(value = "/menu",method = RequestMethod.POST)
    public ResponseEntity add(AddWeixinMenuFormDto addFormDto){
        logger.info("添加微信菜单开始");
        logger.info("当前登录用户id:{}",getLoginUser().getId());
        ResponseJsonRender resultData=new ResponseJsonRender();
        // 表单值设置
        WeixinMenuPo basePo = new WeixinMenuPo();
        basePo.setName(addFormDto.getName());
        basePo.setKey(addFormDto.getKey());
        basePo.setType(addFormDto.getType());
        basePo.setUrl(addFormDto.getUrl());
        basePo.setMediaId(addFormDto.getMediaId());
        basePo.setAppid(addFormDto.getAppid());
        basePo.setPagepath(addFormDto.getPagepath());
        basePo.setWhich(addFormDto.getWhich());
        basePo.setParentId(addFormDto.getParentId());
        basePo.setSequence(addFormDto.getSequence());

        basePo = apiWeixinMenuPoService.preInsert(basePo,getLoginUser().getId());
        WeixinMenuDto r = apiWeixinMenuPoService.insert(basePo);
        if (r == null) {
            // 添加失败
            resultData.setCode(ResponseCode.E404_100001.getCode());
            resultData.setMsg(ResponseCode.E404_100001.getMsg());
            logger.info("code:{},msg:{}",resultData.getCode(),resultData.getMsg());
            logger.info("添加微信菜单结束，失败");
            return new ResponseEntity(resultData,HttpStatus.NOT_FOUND);
        }else{
            // 添加成功，返回添加的数据
            resultData.setData(r);
            logger.info("添加微信菜单id:{}",r.getId());
            logger.info("添加微信菜单结束，成功");
            return new ResponseEntity(resultData, HttpStatus.CREATED);
        }
    }

    /**
     * 单资源，删除
     * @param id
     * @return
     */
    @RepeatFormValidator
    @RequiresPermissions("weixinmenu:delete")
    @RequestMapping(value = "/menu/{id}",method = RequestMethod.DELETE)
    public ResponseEntity delete(@PathVariable String id){
        logger.info("删除微信菜单开始");
        logger.info("用户id:{}",getLoginUser().getId());
        logger.info("微信菜单id:{}",id);
        ResponseJsonRender resultData=new ResponseJsonRender();

            int r = apiWeixinMenuPoService.deleteFlagByPrimaryKeyWithUpdateUser(id,getLoginUser().getId());
            if (r <= 0) {
                // 删除失败，可能没有找到资源
                resultData.setCode(ResponseCode.E404_100001.getCode());
                resultData.setMsg(ResponseCode.E404_100001.getMsg());
                logger.info("code:{},msg:{}",resultData.getCode(),resultData.getMsg());
                logger.info("删除微信菜单结束，失败");
                return new ResponseEntity(resultData,HttpStatus.NOT_FOUND);
            }else{
                // 删除成功
                logger.info("删除的微信菜单id:{}",id);
                logger.info("删除微信菜单结束，成功");
                return new ResponseEntity(resultData,HttpStatus.NO_CONTENT);
            }
    }

    /**
     * 单资源，更新
     * @param id
     * @param updateFormDto
     * @return
     */
    @RepeatFormValidator
    @RequiresPermissions("weixinmenu:update")
    @RequestMapping(value = "/menu/{id}",method = RequestMethod.PUT)
    public ResponseEntity update(@PathVariable String id, UpdateWeixinMenuFormDto updateFormDto){
        logger.info("更新微信菜单开始");
        logger.info("当前登录用户id:{}",getLoginUser().getId());
        logger.info("微信菜单id:{}",id);
        ResponseJsonRender resultData=new ResponseJsonRender();
        // 表单值设置
        WeixinMenuPo basePo = new WeixinMenuPo();
        // id
        basePo.setId(id);
        basePo.setName(updateFormDto.getName());
        basePo.setKey(updateFormDto.getKey());
        basePo.setType(updateFormDto.getType());
        basePo.setUrl(updateFormDto.getUrl());
        basePo.setMediaId(updateFormDto.getMediaId());
        basePo.setAppid(updateFormDto.getAppid());
        basePo.setPagepath(updateFormDto.getPagepath());
        basePo.setWhich(updateFormDto.getWhich());
        basePo.setParentId(updateFormDto.getParentId());
        basePo.setSequence(updateFormDto.getSequence());

        // 用条件更新，乐观锁机制
        WeixinMenuPo basePoCondition = new WeixinMenuPo();
        basePoCondition.setId(id);
        basePoCondition.setDelFlag(BasePo.YesNo.N.name());
        basePoCondition.setUpdateAt(updateFormDto.getUpdateTime());
        basePo = apiWeixinMenuPoService.preUpdate(basePo,getLoginUser().getId());
        int r = apiWeixinMenuPoService.updateSelective(basePo,basePoCondition);
        if (r <= 0) {
            // 更新失败，资源不存在
            resultData.setCode(ResponseCode.E404_100001.getCode());
            resultData.setMsg(ResponseCode.E404_100001.getMsg());
            logger.info("code:{},msg:{}",resultData.getCode(),resultData.getMsg());
            logger.info("更新微信菜单结束，失败");
            return new ResponseEntity(resultData,HttpStatus.NOT_FOUND);
        }else{
            // 更新成功，已被成功创建
            logger.info("更新的微信菜单id:{}",id);
            logger.info("更新微信菜单结束，成功");
            return new ResponseEntity(resultData, HttpStatus.CREATED);
        }
    }

    /**
     * 单资源，获取id微信菜单
     * @param id
     * @return
     */
    @RequiresPermissions("weixinmenu:getById")
    @RequestMapping(value = "/menu/{id}",method = RequestMethod.GET)
    public ResponseEntity getById(@PathVariable String id){

        ResponseJsonRender resultData=new ResponseJsonRender();
        WeixinMenuDto baseDataScopeDto = apiWeixinMenuPoService.selectByPrimaryKey(id,false);
        if(baseDataScopeDto != null){
            resultData.setData(baseDataScopeDto);
            return new ResponseEntity(resultData, HttpStatus.OK);
        }else{
            resultData.setCode(ResponseCode.E404_100001.getCode());
            resultData.setMsg(ResponseCode.E404_100001.getMsg());
            return new ResponseEntity(resultData,HttpStatus.NOT_FOUND);
        }
    }

    /**
     * 复数资源，搜索微信菜单
     * @param dto
     * @return
     */
    @RequiresPermissions("weixinmenu:search")
    @RequestMapping(value = "/menus",method = RequestMethod.GET)
    public ResponseEntity search(SearchWeixinMenusConditionDto dto){

        ResponseJsonRender resultData=new ResponseJsonRender();
        PageAndOrderbyParamDto pageAndOrderbyParamDto = new PageAndOrderbyParamDto(PageUtils.getPageFromThreadLocal(), OrderbyUtils.getOrderbyFromThreadLocal());
        // 设置当前登录用户id
        dto.setCurrentUserId(getLoginUser().getId());
        BaseRoleDto roleDto = (BaseRoleDto) getLoginUser().getRole();
        dto.setCurrentRoleId(roleDto.getId());
        PageResultDto<WeixinMenuDto> list = apiWeixinMenuPoService.searchWeixinMenusDsf(dto,pageAndOrderbyParamDto);

        if(CollectionUtils.isNotEmpty(list.getData())){
            resultData.setData(list.getData());
            resultData.setPage(list.getPage());
            return new ResponseEntity(resultData, HttpStatus.OK);
        }else{
            resultData.setCode(ResponseCode.E404_100001.getCode());
            resultData.setMsg(ResponseCode.E404_100001.getMsg());
            return new ResponseEntity(resultData,HttpStatus.NOT_FOUND);
        }
    }


    @RequestMapping(value = "synToWeixinServer", method = RequestMethod.POST)
    @RequiresPermissions("weixinmenu:synToWeixinServer")
    public ResponseEntity synToWeixinServer(String which){
        ResponseJsonRender resultData=new ResponseJsonRender("同步成功");
        Orderby orderby = OrderbyUtils.getOrderbyFromThreadLocal();
        Menu menu = generateMenu(which,orderby);

        if(menu == null){
            throw new RuntimeException("同步失败，没有可用菜单");
        }
        boolean flag = PublicUtils.createMenu(which,menu);
        if(flag){
            return new ResponseEntity(resultData,HttpStatus.OK);
        }else{
            resultData.setMsg("同步失败");
            resultData.setCode(ResponseCode.E400_100000.getCode());

            return new ResponseEntity(resultData,HttpStatus.BAD_REQUEST);
        }
    }

    private Menu generateMenu(String which,Orderby orderby ){
        Menu menu = null;
        //先查询根节点
        WeixinMenuPo condition = new WeixinMenuPo();
        condition.setParentId(BaseTreePo.defaultRootParentId);
        condition.setLevel(BaseTreePo.defaultRootLevel);
        condition.setWhich(which);
        condition.setDelFlag(BasePo.YesNo.N.name());
        List<WeixinMenuDto> firstWeixinMenuDtos = apiWeixinMenuPoService.selectList(condition,orderby);
        // 注意，这里微信只允许两级菜单，所以暂时添加两级
        if (firstWeixinMenuDtos != null && !firstWeixinMenuDtos.isEmpty()) {
            menu = new Menu();
            MenuItem firstMenuItem = null;
            MenuItem secondMenuItem = null;
            for (WeixinMenuDto rootWeixinMenuDto : firstWeixinMenuDtos) {
                List<WeixinMenuDto> secondWeixinMenuDtos = apiWeixinMenuPoService.wrapDtos(apiWeixinMenuPoService.getChildren(rootWeixinMenuDto.getId(),orderby));
                firstMenuItem = MenuItemFactory.createMenuItem(rootWeixinMenuDto);
                if ((secondWeixinMenuDtos != null && !secondWeixinMenuDtos.isEmpty())) {
                    for (WeixinMenuDto secondWeixinMenuDto : secondWeixinMenuDtos) {
                        if(StringUtils.isNotEmpty(secondWeixinMenuDto.getType())){
                            secondMenuItem = MenuItemFactory.createMenuItem(secondWeixinMenuDto);
                            firstMenuItem.addSubButton(secondMenuItem);
                        }
                    }
                }
                menu.addMenuItem(firstMenuItem);
            }

        }
        return menu;
    }
}
