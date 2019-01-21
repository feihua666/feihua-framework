package com.feihua.framework.rest.modules.datascope.mvc;


import com.feihua.exception.DataConflictException;
import com.feihua.framework.base.modules.datascope.api.ApiBaseDataScopeDataScopeDefinePoService;
import com.feihua.framework.base.modules.datascope.api.ApiBaseDataScopeDataScopeDefineSelfPoService;
import com.feihua.framework.base.modules.datascope.dto.BaseDataScopeDataScopeDefineDto;
import com.feihua.framework.base.modules.datascope.dto.DataScopeDataScopeParamDto;
import com.feihua.framework.constants.DictEnum;
import com.feihua.framework.log.comm.annotation.OperationLog;
import com.feihua.framework.rest.ResponseJsonRender;
import com.feihua.framework.rest.interceptor.RepeatFormValidator;
import com.feihua.framework.rest.modules.common.mvc.BaseController;
import com.feihua.framework.rest.modules.datascope.dto.DataScopeDataScopeDefineFormDto;
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
 * 数据范围的数据范围定义接口
 * Created by yangwei
 * Created at 2018/3/16 15:23
 */
@RestController
@RequestMapping("/base")
public class BaseDataScopeDataScopeDefineController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(BaseDataScopeDataScopeDefineController.class);

    @Autowired
    private ApiBaseDataScopeDataScopeDefinePoService apiBaseDataScopeDataScopeDefinePoService;

    @Autowired
    private ApiBaseDataScopeDataScopeDefineSelfPoService apiBaseDataScopeDataScopeDefineSelfPoService;

    /**
     * 数据范围的数据范围定义
     * @param dataScopeId  // 数据范围id
     * @param dataScopeDataScopeDefineFormDto
     * @return
     */
    @OperationLog(operation = "数据范围的数据范围定义接口", content = "数据范围的数据范围定义")
    @RepeatFormValidator
    @RequiresPermissions("base:dataScope:dataScope:define")
    @RequestMapping(value = "/dataScope/{dataScopeId}/dataScope/define",method = RequestMethod.POST)
    public ResponseEntity dataScopeDataScopedDefine(@PathVariable String dataScopeId, DataScopeDataScopeDefineFormDto dataScopeDataScopeDefineFormDto){
        logger.info("设置数据范围的数据范围定义开始");
        logger.info("当前登录用户id:{}",getLoginUser().getId());
        ResponseJsonRender resultData=new ResponseJsonRender();

        DataScopeDataScopeParamDto dataScopeDataScopeParamDto = new DataScopeDataScopeParamDto();
        dataScopeDataScopeParamDto.setCurrentUserId(getLoginUser().getId());
        dataScopeDataScopeParamDto.setDataScopeId(dataScopeId);
        dataScopeDataScopeParamDto.setType(dataScopeDataScopeDefineFormDto.getType());
        dataScopeDataScopeParamDto.setDataScopeIds(dataScopeDataScopeDefineFormDto.getDataScopeIds());

        BaseDataScopeDataScopeDefineDto r = null;
        try {
            r = apiBaseDataScopeDataScopeDefinePoService.setDataScopeDataScope(dataScopeDataScopeParamDto);
        }catch (DataConflictException e){
            resultData.setCode(e.getCode());
            resultData.setMsg(e.getMessage());
            logger.info("code:{},msg:{}",resultData.getCode(),resultData.getMsg());
            logger.info("设置数据范围的数据范围定义结束，失败");
            return new ResponseEntity(resultData, HttpStatus.CONFLICT);
        }
        if (r == null) {
            // 添加失败
            resultData.setCode(ResponseCode.E404_100001.getCode());
            resultData.setMsg(ResponseCode.E404_100001.getMsg());
            logger.info("code:{},msg:{}",resultData.getCode(),resultData.getMsg());
            logger.info("设置数据范围的数据范围定义结束，失败");
            return new ResponseEntity(resultData,HttpStatus.NOT_FOUND);
        }else{
            // 添加成功，返回添加的数据
            resultData.setData(r);
            logger.info("数据范围id:{}",dataScopeId);
            logger.info("设置数据范围的数据范围定义id:{}",r.getId());
            logger.info("设置数据范围的数据范围定义结束，成功");
            return new ResponseEntity(resultData, HttpStatus.CREATED);
        }
    }
    /**
     * 数据范围的数据范围定义
     * @param dataScopeId  // 数据范围id
     * @return
     */
    @OperationLog(operation = "数据范围的数据范围定义接口", content = "获取数据范围的数据范围定义")
    @RepeatFormValidator
    @RequiresPermissions("base:dataScope:dataScope:define:get")
    @RequestMapping(value = "/dataScope/{dataScopeId}/dataScope/define",method = RequestMethod.GET)
    public ResponseEntity getDataScopeDataScopedDefine(@PathVariable String dataScopeId){

        ResponseJsonRender resultData=new ResponseJsonRender();
        BaseDataScopeDataScopeDefineDto r = apiBaseDataScopeDataScopeDefinePoService.selectByDataScopeId(dataScopeId);

        if(r != null){
            resultData.setData(r);
            // 如果是自定义，则查询自定义结果
            if(DictEnum.DataScopeDataScope.self.name().equals(r.getType())){
                resultData.addData("self",apiBaseDataScopeDataScopeDefineSelfPoService.selectByDataScopeDataScopeDefineId(r.getId()));
            }
            return new ResponseEntity(resultData, HttpStatus.OK);
        }else{
            resultData.setCode(ResponseCode.E404_100001.getCode());
            resultData.setMsg(ResponseCode.E404_100001.getMsg());
            return new ResponseEntity(resultData,HttpStatus.NOT_FOUND);
        }
    }
}
