package com.feihua.framework.rest.modules.office.mvc;

import com.feihua.exception.DataConflictException;
import com.feihua.framework.constants.DictEnum;
import com.feihua.framework.base.modules.office.api.ApiBaseOfficeDataScopeDefinePoService;
import com.feihua.framework.base.modules.office.api.ApiBaseOfficeDataScopeDefineSelfPoService;
import com.feihua.framework.base.modules.office.dto.BaseOfficeDataScopeDefineDto;
import com.feihua.framework.base.modules.office.dto.OfficeDataScopeParamDto;
import com.feihua.framework.rest.ResponseJsonRender;
import com.feihua.framework.rest.interceptor.RepeatFormValidator;
import com.feihua.framework.rest.modules.common.mvc.BaseController;
import com.feihua.framework.rest.modules.office.dto.OfficeDataScopeDefineFormDto;
import com.feihua.utils.http.httpServletResponse.ResponseCode;
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

/**
 * 机构数据范围定义接口
 * Created by yangwei
 * Created at 2018/3/16 15:23
 */
@RestController
@RequestMapping("/base")
public class BaseOfficeDataScopeDefineController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(BaseOfficeDataScopeDefineController.class);

    @Autowired
    private ApiBaseOfficeDataScopeDefinePoService apiBaseOfficeDataScopeDefinePoService;

    @Autowired
    private ApiBaseOfficeDataScopeDefineSelfPoService apiBaseOfficeDataScopeDefineSelfPoService;

    /**
     * 机构数据范围定义
     * @param dataScopeId  // 数据范围id
     * @param officeDataScopeDefineFormDto
     * @return
     */
    @RepeatFormValidator
    @RequiresPermissions("base:office:dataScope:define")
    @RequestMapping(value = "/dataScope/{dataScopeId}/office/define",method = RequestMethod.POST)
    public ResponseEntity officeDataScopedDefine(@PathVariable String dataScopeId, OfficeDataScopeDefineFormDto officeDataScopeDefineFormDto){
        logger.info("设置机构数据范围定义开始");
        logger.info("当前登录用户id:{}",getLoginUser().getId());
        ResponseJsonRender resultData=new ResponseJsonRender();

        OfficeDataScopeParamDto officeDataScopeParamDto = new OfficeDataScopeParamDto();
        officeDataScopeParamDto.setCurrentUserId(getLoginUser().getId());
        officeDataScopeParamDto.setDataScopeId(dataScopeId);
        officeDataScopeParamDto.setType(officeDataScopeDefineFormDto.getType());
        officeDataScopeParamDto.setOfficeIds(officeDataScopeDefineFormDto.getOfficeIds());

        BaseOfficeDataScopeDefineDto r = null;
        try {
            r = apiBaseOfficeDataScopeDefinePoService.setOfficeDataScope(officeDataScopeParamDto);
        }catch (DataConflictException e){
            resultData.setCode(e.getCode());
            resultData.setMsg(e.getMessage());
            logger.info("code:{},msg:{}",resultData.getCode(),resultData.getMsg());
            logger.info("设置机构数据范围定义结束，失败");
            return new ResponseEntity(resultData, HttpStatus.CONFLICT);
        }
        if (r == null) {
            // 添加失败
            resultData.setCode(ResponseCode.E404_100001.getCode());
            resultData.setMsg(ResponseCode.E404_100001.getMsg());
            logger.info("code:{},msg:{}",resultData.getCode(),resultData.getMsg());
            logger.info("设置机构数据范围定义结束，失败");
            return new ResponseEntity(resultData,HttpStatus.NOT_FOUND);
        }else{
            // 添加成功，返回添加的数据
            resultData.setData(r);
            logger.info("数据范围id:{}",dataScopeId);
            logger.info("设置机构数据范围定义id:{}",r.getId());
            logger.info("设置机构数据范围定义结束，成功");
            return new ResponseEntity(resultData, HttpStatus.CREATED);
        }
    }
    /**
     * 机构数据范围定义
     * @param dataScopeId  // 数据范围id
     * @return
     */
    @RepeatFormValidator
    @RequiresPermissions("base:office:dataScope:define:get")
    @RequestMapping(value = "/dataScope/{dataScopeId}/office/define",method = RequestMethod.GET)
    public ResponseEntity getOfficeDataScopedDefine(@PathVariable String dataScopeId){

        ResponseJsonRender resultData=new ResponseJsonRender();
        BaseOfficeDataScopeDefineDto r = apiBaseOfficeDataScopeDefinePoService.selectByDataScopeId(dataScopeId);

        if(r != null){
            resultData.setData(r);
            // 如果是自定义，则查询自定义结果
            if(DictEnum.OfficeDataScope.self.name().equals(r.getType())){
                resultData.addData("self",apiBaseOfficeDataScopeDefineSelfPoService.selectByOfficeDataScopeDefineId(r.getId()));
            }
            return new ResponseEntity(resultData, HttpStatus.OK);
        }else{
            resultData.setCode(ResponseCode.E404_100001.getCode());
            resultData.setMsg(ResponseCode.E404_100001.getMsg());
            return new ResponseEntity(resultData,HttpStatus.NOT_FOUND);
        }
    }
}
