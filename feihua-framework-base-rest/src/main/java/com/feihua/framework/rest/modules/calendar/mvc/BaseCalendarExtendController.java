package com.feihua.framework.rest.modules.calendar.mvc;

import com.feihua.framework.rest.ResponseJsonRender;
import com.feihua.framework.rest.interceptor.RepeatFormValidator;
import com.feihua.framework.rest.modules.common.mvc.BaseController;
import com.feihua.utils.http.httpServletResponse.ResponseCode;
import feihua.jdbc.api.pojo.BasePo;
import feihua.jdbc.api.pojo.PageAndOrderbyParamDto;
import feihua.jdbc.api.pojo.PageResultDto;
import feihua.jdbc.api.utils.OrderbyUtils;
import feihua.jdbc.api.utils.PageUtils;
import org.apache.commons.collections.CollectionUtils;
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
import com.feihua.framework.base.modules.calendar.dto.BaseCalendarExtendDto;
import com.feihua.framework.base.modules.calendar.dto.SearchBaseCalendarExtendsConditionDto;
import com.feihua.framework.base.modules.calendar.api.ApiBaseCalendarExtendPoService;
import com.feihua.framework.rest.modules.calendar.dto.AddBaseCalendarExtendFormDto;
import com.feihua.framework.rest.modules.calendar.dto.UpdateBaseCalendarExtendFormDto;
import com.feihua.framework.base.modules.calendar.po.BaseCalendarExtendPo;

import java.util.List;

/**
 * 日历扩展管理
 * Created by yangwei
 */
@RestController
@RequestMapping("/base")
public class BaseCalendarExtendController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(BaseCalendarExtendController.class);

    @Autowired
    private ApiBaseCalendarExtendPoService apiBaseCalendarExtendPoService;

    /**
     * 单资源，添加
     * @param addFormDto
     * @return
     */
    @RepeatFormValidator
    @RequiresPermissions("calendar:extend:add")
    @RequestMapping(value = "/calendarextend",method = RequestMethod.POST)
    public ResponseEntity add(AddBaseCalendarExtendFormDto addFormDto){
        logger.info("添加日历扩展开始");
        logger.info("当前登录用户id:{}",getLoginUser().getId());
        ResponseJsonRender resultData=new ResponseJsonRender();

        // 检查
        BaseCalendarExtendPo baseCalendarExtendPoCheckCondition = new BaseCalendarExtendPo();
        baseCalendarExtendPoCheckCondition.setYear(addFormDto.getYear());
        baseCalendarExtendPoCheckCondition.setMonth(addFormDto.getMonth());
        baseCalendarExtendPoCheckCondition.setDay(addFormDto.getDay());
        baseCalendarExtendPoCheckCondition.setDelFlag(BasePo.YesNo.N.name());
        List dblist = apiBaseCalendarExtendPoService.selectListSimple(baseCalendarExtendPoCheckCondition);
        if (dblist != null && !dblist.isEmpty()) {
            // 添加失败
            resultData.setCode(ResponseCode.E409_100001.getCode());
            resultData.setMsg(ResponseCode.E409_100001.getMsg());
            logger.info("code:{},msg:{}",resultData.getCode(),resultData.getMsg());
            logger.info("添加日历扩展束，失败");
            return new ResponseEntity(resultData,HttpStatus.CONFLICT);
        }




        // 表单值设置
        BaseCalendarExtendPo basePo = new BaseCalendarExtendPo();
        //todo
        basePo.setYear(addFormDto.getYear());
        basePo.setMonth(addFormDto.getMonth());
        basePo.setDay(addFormDto.getDay());
        basePo.setWorkOrRest(addFormDto.getWorkOrRest());
        basePo.setHoliday(addFormDto.getHoliday());

        apiBaseCalendarExtendPoService.preInsert(basePo,getLoginUser().getId());
        BaseCalendarExtendDto r = apiBaseCalendarExtendPoService.insert(basePo);
        if (r == null) {
            // 添加失败
            resultData.setCode(ResponseCode.E404_100001.getCode());
            resultData.setMsg(ResponseCode.E404_100001.getMsg());
            logger.info("code:{},msg:{}",resultData.getCode(),resultData.getMsg());
            logger.info("添加日历扩展结束，失败");
            return new ResponseEntity(resultData,HttpStatus.NOT_FOUND);
        }else{
            // 添加成功，返回添加的数据
            resultData.setData(r);
            logger.info("添加日历扩展id:{}",r.getId());
            logger.info("添加日历扩展结束，成功");
            return new ResponseEntity(resultData, HttpStatus.CREATED);
        }
    }

    /**
     * 单资源，删除
     * @param id
     * @return
     */
    @RepeatFormValidator
    @RequiresPermissions("calendar:extend:delete")
    @RequestMapping(value = "/calendarextend/{id}",method = RequestMethod.DELETE)
    public ResponseEntity delete(@PathVariable String id){
        logger.info("删除日历扩展开始");
        logger.info("用户id:{}",getLoginUser().getId());
        logger.info("日历扩展id:{}",id);
        ResponseJsonRender resultData=new ResponseJsonRender();

            int r = apiBaseCalendarExtendPoService.deleteFlagByPrimaryKeyWithUpdateUser(id,getLoginUser().getId());
            if (r <= 0) {
                // 删除失败，可能没有找到资源
                resultData.setCode(ResponseCode.E404_100001.getCode());
                resultData.setMsg(ResponseCode.E404_100001.getMsg());
                logger.info("code:{},msg:{}",resultData.getCode(),resultData.getMsg());
                logger.info("删除日历扩展结束，失败");
                return new ResponseEntity(resultData,HttpStatus.NOT_FOUND);
            }else{
                // 删除成功
                logger.info("删除的日历扩展id:{}",id);
                logger.info("删除日历扩展结束，成功");
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
    @RequiresPermissions("calendar:extend:update")
    @RequestMapping(value = "/calendarextend/{id}",method = RequestMethod.PUT)
    public ResponseEntity update(@PathVariable String id, UpdateBaseCalendarExtendFormDto updateFormDto){
        logger.info("更新日历扩展开始");
        logger.info("当前登录用户id:{}",getLoginUser().getId());
        logger.info("日历扩展id:{}",id);
        ResponseJsonRender resultData=new ResponseJsonRender();

        // 检查
        BaseCalendarExtendPo baseCalendarExtendPoCheckCondition = new BaseCalendarExtendPo();
        baseCalendarExtendPoCheckCondition.setYear(updateFormDto.getYear());
        baseCalendarExtendPoCheckCondition.setMonth(updateFormDto.getMonth());
        baseCalendarExtendPoCheckCondition.setDay(updateFormDto.getDay());
        baseCalendarExtendPoCheckCondition.setDelFlag(BasePo.YesNo.N.name());
        List<BaseCalendarExtendPo> dblist = apiBaseCalendarExtendPoService.selectListSimple(baseCalendarExtendPoCheckCondition);
        if (dblist != null && !dblist.isEmpty() && !dblist.get(0).getId().equals(id)) {
            // 添加失败
            resultData.setCode(ResponseCode.E409_100001.getCode());
            resultData.setMsg(ResponseCode.E409_100001.getMsg());
            logger.info("code:{},msg:{}",resultData.getCode(),resultData.getMsg());
            logger.info("添加日历扩展束，失败");
            return new ResponseEntity(resultData,HttpStatus.CONFLICT);
        }


        // 表单值设置
        BaseCalendarExtendPo basePo = new BaseCalendarExtendPo();
        // id
        basePo.setId(id);
        basePo.setYear(updateFormDto.getYear());
        basePo.setMonth(updateFormDto.getMonth());
        basePo.setDay(updateFormDto.getDay());
        basePo.setWorkOrRest(updateFormDto.getWorkOrRest());
        basePo.setHoliday(updateFormDto.getHoliday());
        //todo

        // 用条件更新，乐观锁机制
        BaseCalendarExtendPo basePoCondition = new BaseCalendarExtendPo();
        basePoCondition.setId(id);
        basePoCondition.setDelFlag(BasePo.YesNo.N.name());
        basePoCondition.setUpdateAt(updateFormDto.getUpdateTime());
        apiBaseCalendarExtendPoService.preUpdate(basePo,getLoginUser().getId());
        int r = apiBaseCalendarExtendPoService.updateSelective(basePo,basePoCondition);
        if (r <= 0) {
            // 更新失败，资源不存在
            resultData.setCode(ResponseCode.E404_100001.getCode());
            resultData.setMsg(ResponseCode.E404_100001.getMsg());
            logger.info("code:{},msg:{}",resultData.getCode(),resultData.getMsg());
            logger.info("更新日历扩展结束，失败");
            return new ResponseEntity(resultData,HttpStatus.NOT_FOUND);
        }else{
            // 更新成功，已被成功创建
            logger.info("更新的日历扩展id:{}",id);
            logger.info("更新日历扩展结束，成功");
            return new ResponseEntity(resultData, HttpStatus.CREATED);
        }
    }

    /**
     * 单资源，获取id日历扩展
     * @param id
     * @return
     */
    @RepeatFormValidator
    @RequiresPermissions("calendar:extend:getById")
    @RequestMapping(value = "/calendarextend/{id}",method = RequestMethod.GET)
    public ResponseEntity getById(@PathVariable String id){

        ResponseJsonRender resultData=new ResponseJsonRender();
        BaseCalendarExtendDto baseDataScopeDto = apiBaseCalendarExtendPoService.selectByPrimaryKey(id,false);
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
     * 复数资源，搜索日历扩展
     * @param dto
     * @return
     */
    @RepeatFormValidator
    @RequiresPermissions("calendar:extend:search")
    @RequestMapping(value = "/calendarextends",method = RequestMethod.GET)
    public ResponseEntity search(SearchBaseCalendarExtendsConditionDto dto){

        ResponseJsonRender resultData=new ResponseJsonRender();
        PageAndOrderbyParamDto pageAndOrderbyParamDto = new PageAndOrderbyParamDto(PageUtils.getPageFromThreadLocal(), OrderbyUtils.getOrderbyFromThreadLocal());
        // 设置当前登录用户id
        dto.setCurrentUserId(getLoginUser().getId());
        dto.setCurrentRoleId(getLoginUserRoleId());
        PageResultDto<BaseCalendarExtendDto> list = apiBaseCalendarExtendPoService.searchBaseCalendarExtendsDsf(dto,pageAndOrderbyParamDto);

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
}
