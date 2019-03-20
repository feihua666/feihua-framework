package com.feihua.framework.activity.rest.mvc;

import com.feihua.framework.activity.api.ApiActivitiModelService;
import com.feihua.framework.activity.dto.ExportModelDto;
import com.feihua.framework.base.modules.file.po.BaseFilePo;
import com.feihua.framework.base.modules.oss.cloud.api.ApiCloudStorageService;
import com.feihua.framework.base.modules.oss.cloud.impl.LocalStorageServiceImpl;
import com.feihua.framework.constants.DictEnum;
import com.feihua.framework.rest.ResponseJsonRender;
import com.feihua.framework.rest.mvc.SuperController;
import com.feihua.utils.http.httpServletResponse.ResponseCode;
import feihua.jdbc.api.pojo.Page;
import feihua.jdbc.api.utils.PageUtils;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Model;
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

import java.io.IOException;
import java.util.List;

/**
 * Created by yw on 2017/2/4.
 */
@RestController
@RequestMapping("/activity")
public class ActivityModelController  extends SuperController {
    private static Logger logger = LoggerFactory.getLogger(ActivityModelController.class);

    @Autowired
    private ApiActivitiModelService activityModelService;
    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private ApiCloudStorageService apiCloudStorageService;

    /**
     * 创建模型
     */
    @RequiresPermissions("activity:model:create")
    @RequestMapping(value = "/model", method = RequestMethod.POST)
    public ResponseEntity create() {
        logger.info("创建模型开始");
        logger.info("当前登录用户id:{}",getLoginUser().getId());
        ResponseJsonRender resultData=new ResponseJsonRender();
        String prefix = "process";
        long time = System.currentTimeMillis();
        String name = prefix + time;
        String key = name;
        String description = null;
        String category = null;
        Model modelData = null;
        try {
            modelData = activityModelService.create(name, key, description, category);
        } catch (Exception e) {
            logger.error("创建ACTIVITY模型失败：", e);
        }
        if(modelData == null){
            resultData.setCode(ResponseCode.E404_100001.getCode());
            resultData.setMsg(ResponseCode.E404_100001.getMsg());
            logger.info("code:{},msg:{}",resultData.getCode(),resultData.getMsg());
            logger.info("创建模型结束，失败");
            return new ResponseEntity(resultData,HttpStatus.NOT_FOUND);
        }else{
            resultData.setData(modelData);
            logger.info("创建模型id:{}",modelData.getId());
            logger.info("创建模型结束，成功");
            return new ResponseEntity(resultData, HttpStatus.CREATED);
        }
    }
    /**
     * 删除模型
     * @param id
     * @return
     */
    @RequiresPermissions("activity:model:delete")
    @RequestMapping(value = "/model/{id}", method = RequestMethod.DELETE)
    public ResponseEntity delete(@PathVariable String id) {
        logger.info("删除模型开始");
        logger.info("当前登录用户id:{}",getLoginUser().getId());

        ResponseJsonRender resultData=new ResponseJsonRender();

        repositoryService.deleteModel(id);
        // 删除成功
        logger.info("删除的模型id:{}",id);
        logger.info("删除模型结束，成功");

        return new ResponseEntity(resultData, HttpStatus.NO_CONTENT);
    }

    /**
     * 更新模型分类
     * @param id
     * @return
     */
    @RequiresPermissions("activity:model:updateCategory")
    @RequestMapping(value = "/model/{id}/category", method = RequestMethod.PUT)
    public ResponseEntity updateCategory(@PathVariable String id, String category) {
        logger.info("更新模型分类开始");
        logger.info("当前登录用户id:{}",getLoginUser().getId());
        ResponseJsonRender resultData=new ResponseJsonRender();

        activityModelService.updateCategory(id, category);

        // 更新成功，已被成功创建
        logger.info("模型id:{}",id);
        logger.info("更新模型分类结束，成功");
        return new ResponseEntity(resultData, HttpStatus.CREATED);
    }

    @RequiresPermissions("activity:model:list")
    @RequestMapping(value = "/models", method = RequestMethod.GET)
    public ResponseEntity list(String name, String category) {
        ResponseJsonRender resultData=new ResponseJsonRender();

        Page page  = PageUtils.getPageFromThreadLocal();
        List<Model> list = activityModelService.searchModel(name,category,page);
        resultData.setData(list);
        if(list != null && !list.isEmpty()){
            resultData.setData(list);
            return new ResponseEntity(resultData, HttpStatus.OK);
        }else{
            resultData.setCode(ResponseCode.E404_100001.getCode());
            resultData.setMsg(ResponseCode.E404_100001.getMsg());
            return new ResponseEntity(resultData,HttpStatus.NOT_FOUND);
        }
    }

    /**
     * 根据模型部署流程
     */
    @RequiresPermissions("activity:model:deploy")
    @RequestMapping(value = "/model/{id}/deploy",method = RequestMethod.POST)
    public ResponseEntity deploy(@PathVariable String id) {
        logger.info("根据模型部署流程开始");
        logger.info("当前登录用户id:{}",getLoginUser().getId());
        ResponseJsonRender resultData=new ResponseJsonRender();

        String proccessId = null;
        try {
            proccessId = activityModelService.deploy(id);
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            resultData.setCode(ResponseCode.E400_100001.getCode());
            resultData.setMsg("model view is invalid");
            logger.info("code:{},msg:{}",resultData.getCode(),resultData.getMsg());
            logger.info("根据模型部署流程结束，失败");
            return new ResponseEntity(resultData,HttpStatus.BAD_REQUEST);
        }

        if(StringUtils.isEmpty(proccessId)){
            resultData.setCode(ResponseCode.E404_100001.getCode());
            resultData.setMsg(ResponseCode.E404_100001.getMsg());
            logger.info("code:{},msg:{}",resultData.getCode(),resultData.getMsg());
            logger.info("根据模型部署流程结束，失败");
            return new ResponseEntity(resultData,HttpStatus.NOT_FOUND);
        }else{
            // 更新成功，已被成功创建
            logger.info("模型id:{}",id);
            logger.info("根据模型部署流程结束，成功");
            resultData.setData(proccessId);
            return new ResponseEntity(resultData, HttpStatus.CREATED);
        }
    }

    /**
     * 导出model的xml文件
     */
    @RequiresPermissions("activity:model:export")
    @RequestMapping(value = "/model/{id}/export",method = RequestMethod.GET)
    public ResponseEntity export(@PathVariable String id) throws IOException {
        ResponseJsonRender resultData = new ResponseJsonRender();

        long start = System.currentTimeMillis();
        ExportModelDto exportModelDto = activityModelService.export(id);
        String resultPath = "";
        if (exportModelDto != null) {

            ApiCloudStorageService localStorage = new LocalStorageServiceImpl(apiCloudStorageService.getConfig().getLocal());

            resultPath = localStorage.upload(exportModelDto.getInputStream(),"activiti-model-export/" + exportModelDto.getFileName());
            //为了兼容主架构的上传下载逻辑，插入上传下载数据
            try{
                BaseFilePo baseFilePo = new BaseFilePo();
                baseFilePo.setName(exportModelDto.getFileName());
                baseFilePo.setFilename(exportModelDto.getFileName());
                baseFilePo.setFilePath(resultPath);
                baseFilePo.setDownloadNum(Integer.valueOf(0));
                baseFilePo.setType(DictEnum.FileType.other.name());
                baseFilePo.setDuration(((System.currentTimeMillis() - start)/1000 ) + "");
                baseFilePo.setDataUserId(getLoginUser().getId());
                baseFilePo = baseFilePo.service().preInsert(baseFilePo, getLoginUser().getId());
                baseFilePo.service().insertSimple(baseFilePo);
            }catch (Exception e){
                logger.error(e.getMessage());
            }
            resultData.setData(resultPath);
        }else{
            resultData.setCode(ResponseCode.E404_100001.getCode());
            resultData.setMsg(ResponseCode.E404_100001.getMsg());
            return new ResponseEntity(resultData, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity(resultData, HttpStatus.OK);
    }

}
