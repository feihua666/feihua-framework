package com.feihua.framework.rest.modules.file.mvc;

import com.feihua.framework.log.comm.annotation.OperationLog;
import com.feihua.framework.rest.ResponseJsonRender;
import com.feihua.framework.rest.interceptor.RepeatFormValidator;
import com.feihua.framework.rest.modules.common.mvc.BaseController;
import com.feihua.framework.utils.FileHelper;
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
import com.feihua.framework.base.modules.file.dto.BaseFileDto;
import com.feihua.framework.base.modules.file.dto.SearchBaseFilesConditionDto;
import com.feihua.framework.base.modules.file.api.ApiBaseFilePoService;
import com.feihua.framework.rest.modules.file.dto.AddBaseFileFormDto;
import com.feihua.framework.rest.modules.file.dto.UpdateBaseFileFormDto;
import com.feihua.framework.base.modules.file.po.BaseFilePo;

/**
 * 文件管理接口
 * Created by yangwei
 */
@RestController
@RequestMapping("/base")
public class BaseFileController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(BaseFileController.class);

    @Autowired
    private ApiBaseFilePoService apiBaseFilePoService;

    /**
     * 单资源，添加
     *
     * @param addFormDto
     *
     * @return
     */
    @OperationLog(operation = "文件管理接口", content = "单资源，添加")
    @RepeatFormValidator
    @RequiresPermissions("base:file:add")
    @RequestMapping(value = "/file", method = RequestMethod.POST)
    public ResponseEntity add(AddBaseFileFormDto addFormDto) {
        logger.info("添加文件开始");
        logger.info("当前登录用户id:{}", getLoginUser().getId());
        ResponseJsonRender resultData = new ResponseJsonRender();
        // 表单值设置
        BaseFilePo basePo = new BaseFilePo();
        //todo

        basePo = apiBaseFilePoService.preInsert(basePo, getLoginUser().getId());
        BaseFileDto r = apiBaseFilePoService.insert(basePo);
        if (r == null) {
            // 添加失败
            resultData.setCode(ResponseCode.E404_100001.getCode());
            resultData.setMsg(ResponseCode.E404_100001.getMsg());
            logger.info("code:{},msg:{}", resultData.getCode(), resultData.getMsg());
            logger.info("添加文件结束，失败");
            return new ResponseEntity(resultData, HttpStatus.NOT_FOUND);
        } else {
            // 添加成功，返回添加的数据
            resultData.setData(r);
            logger.info("添加文件id:{}", r.getId());
            logger.info("添加文件结束，成功");
            return new ResponseEntity(resultData, HttpStatus.CREATED);
        }
    }

    /**
     * 单资源，删除
     *
     * @param id
     *
     * @return
     */
    @OperationLog(operation = "文件管理接口", content = "单资源，删除")
    @RepeatFormValidator
    @RequiresPermissions("base:file:delete")
    @RequestMapping(value = "/file/{id}", method = RequestMethod.DELETE)
    public ResponseEntity delete(@PathVariable String id) {
        logger.info("删除文件开始");
        logger.info("用户id:{}", getLoginUser().getId());
        logger.info("文件id:{}", id);
        ResponseJsonRender resultData = new ResponseJsonRender();

        BaseFileDto dto = apiBaseFilePoService.selectByPrimaryKey(id);
        int r = apiBaseFilePoService.deleteFlagByPrimaryKeyWithUpdateUser(id, getLoginUser().getId());


        if (r <= 0) {
            // 删除失败，可能没有找到资源
            resultData.setCode(ResponseCode.E404_100001.getCode());
            resultData.setMsg(ResponseCode.E404_100001.getMsg());
            logger.info("code:{},msg:{}", resultData.getCode(), resultData.getMsg());
            logger.info("删除文件结束，失败");
            return new ResponseEntity(resultData, HttpStatus.NOT_FOUND);
        } else {
            // 删除文件
            FileHelper.deleteDiskFile(dto.getFilePath());

            // 删除成功
            logger.info("删除的文件id:{}", id);
            logger.info("删除文件结束，成功");
            return new ResponseEntity(resultData, HttpStatus.NO_CONTENT);
        }
    }

    /**
     * 单资源，更新名称
     *
     * @param id
     * @param updateFormDto
     *
     * @return
     */
    @OperationLog(operation = "文件管理接口", content = "单资源，更新名称")
    @RepeatFormValidator
    @RequiresPermissions("base:file:update")
    @RequestMapping(value = "/file/{id}", method = RequestMethod.PUT)
    public ResponseEntity update(@PathVariable String id, UpdateBaseFileFormDto updateFormDto) {
        logger.info("更新文件开始");
        logger.info("当前登录用户id:{}", getLoginUser().getId());
        logger.info("文件id:{}", id);
        ResponseJsonRender resultData = new ResponseJsonRender();
        // 表单值设置
        BaseFilePo basePo = new BaseFilePo();
        // id
        basePo.setId(id);
        //
        basePo.setName(updateFormDto.getName());

        // 用条件更新，乐观锁机制
        BaseFilePo basePoCondition = new BaseFilePo();
        basePoCondition.setId(id);
        basePoCondition.setDelFlag(BasePo.YesNo.N.name());
        basePoCondition.setUpdateAt(updateFormDto.getUpdateTime());
        basePo = apiBaseFilePoService.preUpdate(basePo, getLoginUser().getId());
        int r = apiBaseFilePoService.updateSelective(basePo, basePoCondition);
        if (r <= 0) {
            // 更新失败，资源不存在
            resultData.setCode(ResponseCode.E404_100001.getCode());
            resultData.setMsg(ResponseCode.E404_100001.getMsg());
            logger.info("code:{},msg:{}", resultData.getCode(), resultData.getMsg());
            logger.info("更新文件结束，失败");
            return new ResponseEntity(resultData, HttpStatus.NOT_FOUND);
        } else {
            // 更新成功，已被成功创建
            logger.info("更新的文件id:{}", id);
            logger.info("更新文件结束，成功");
            return new ResponseEntity(resultData, HttpStatus.CREATED);
        }
    }

    /**
     * 单资源，下载
     *
     * @param id
     *
     * @return
     */
    @OperationLog(operation = "文件管理接口", content = "单资源，下载")
    @RepeatFormValidator
    @RequiresPermissions("base:file:download")
    @RequestMapping(value = "/file/{id}/download", method = RequestMethod.PUT)
    public ResponseEntity updateDownloadNum(@PathVariable String id) {
        logger.info("更新文件下载次数开始");
        logger.info("当前登录用户id:{}", getLoginUser().getId());
        logger.info("文件id:{}", id);
        ResponseJsonRender resultData = new ResponseJsonRender();
        BaseFileDto dto = apiBaseFilePoService.selectByPrimaryKey(id);

        if (dto == null) {
            // 更新失败，资源不存在
            resultData.setCode(ResponseCode.E404_100001.getCode());
            resultData.setMsg(ResponseCode.E404_100001.getMsg());
            logger.info("code:{},msg:{}", resultData.getCode(), resultData.getMsg());
            logger.info("更新文件下载次数结束，失败");
            return new ResponseEntity(resultData, HttpStatus.NOT_FOUND);
        } else {
            BaseFilePo basePo = new BaseFilePo();
            basePo.setId(id);
            basePo.setDownloadNum(dto.getDownloadNum().intValue() + 1);
            int r = apiBaseFilePoService.updateByPrimaryKeySelective(basePo);
            // 更新成功，已被成功创建
            logger.info("更新的文件id:{}", id);
            logger.info("更新文件下载次数结束，成功");
            return new ResponseEntity(resultData, HttpStatus.CREATED);
        }
    }

    /**
     * 单资源，获取id文件
     *
     * @param id
     *
     * @return
     */
    @OperationLog(operation = "文件管理接口", content = "单资源，获取id文件")
    @RepeatFormValidator
    @RequiresPermissions("base:file:getById")
    @RequestMapping(value = "/file/{id}", method = RequestMethod.GET)
    public ResponseEntity getById(@PathVariable String id) {

        ResponseJsonRender resultData = new ResponseJsonRender();
        BaseFileDto baseDataScopeDto = apiBaseFilePoService.selectByPrimaryKey(id, false);
        if (baseDataScopeDto != null) {
            resultData.setData(baseDataScopeDto);
            return new ResponseEntity(resultData, HttpStatus.OK);
        } else {
            resultData.setCode(ResponseCode.E404_100001.getCode());
            resultData.setMsg(ResponseCode.E404_100001.getMsg());
            return new ResponseEntity(resultData, HttpStatus.NOT_FOUND);
        }
    }

    /**
     * 复数资源，搜索文件
     *
     * @param dto
     *
     * @return
     */
    @OperationLog(operation = "文件管理接口", content = "复数资源，搜索文件")
    @RepeatFormValidator
    @RequiresPermissions("base:file:search")
    @RequestMapping(value = "/files", method = RequestMethod.GET)
    public ResponseEntity search(SearchBaseFilesConditionDto dto) {

        ResponseJsonRender resultData = new ResponseJsonRender();
        PageAndOrderbyParamDto pageAndOrderbyParamDto = new PageAndOrderbyParamDto(PageUtils.getPageFromThreadLocal(), OrderbyUtils.getOrderbyFromThreadLocal());
        // 设置当前登录用户id
        dto.setCurrentUserId(getLoginUser().getId());
        dto.setCurrentRoleId(getLoginUserRoleId());
        PageResultDto<BaseFileDto> list = apiBaseFilePoService.searchBaseFilesDsf(dto, pageAndOrderbyParamDto);

        if (CollectionUtils.isNotEmpty(list.getData())) {
            resultData.setData(list.getData());
            resultData.setPage(list.getPage());
            return new ResponseEntity(resultData, HttpStatus.OK);
        } else {
            resultData.setCode(ResponseCode.E404_100001.getCode());
            resultData.setMsg(ResponseCode.E404_100001.getMsg());
            return new ResponseEntity(resultData, HttpStatus.NOT_FOUND);
        }
    }
}
