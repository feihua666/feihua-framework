package com.feihua.framework.base.pojo;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * Created by yangwei
 * Created at 2018/4/18 11:42
 */
public class ControllerModel extends Model{

    /**
     * 类名称注释
     */
    private String controllerComment;
    /**
     * 类名前缀
     */
    private String controllerName;
    /**
     * 类requestMapping路径
     */
    private String classMappingPath;
    /**
     * 方法requestMapping路径
     */
    private String methodMappingPath;
    /**
     * 模型名
     */
    private String modelName;
    /**
     * 模块注释
     */
    private String moduleComment;
    /**
     * shiro权限前缀
     */
    private String methodRequiresPermissionsPre;
    /**
     * servicename
     */
    private String serviceApiName;
    /**
     * service变量name
     */
    private String serviceApiVarName;
    /**
     * 搜索方法名
     */
    private String searchDsfMethodName;
    /**
     * 搜索dto名称
     */
    private String searchConditionDtoName;
    /**
     * dtoname
     */
    private String dtoName;

    /**
     * 模块名
     */
    private String moduleName;

    private String addFormDto;
    private String updateFormDto;

    private List<String> formAttrSet;

    public String getControllerComment() {
        return controllerComment;
    }

    public void setControllerComment(String controllerComment) {
        this.controllerComment = controllerComment;
    }

    public String getControllerName() {
        return controllerName;
    }

    public void setControllerName(String controllerName) {
        this.controllerName = controllerName;
    }

    public String getClassMappingPath() {
        return classMappingPath;
    }

    public void setClassMappingPath(String classMappingPath) {
        this.classMappingPath = classMappingPath;
    }

    public String getMethodMappingPath() {
        return methodMappingPath;
    }

    public void setMethodMappingPath(String methodMappingPath) {
        this.methodMappingPath = methodMappingPath;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getModuleComment() {
        return moduleComment;
    }

    public void setModuleComment(String moduleComment) {
        this.moduleComment = moduleComment;
    }

    public String getMethodRequiresPermissionsPre() {
        return methodRequiresPermissionsPre;
    }

    public void setMethodRequiresPermissionsPre(String methodRequiresPermissionsPre) {
        this.methodRequiresPermissionsPre = methodRequiresPermissionsPre;
    }

    public String getServiceApiName() {
        return serviceApiName;
    }

    public void setServiceApiName(String serviceApiName) {
        this.serviceApiName = serviceApiName;
    }

    public String getServiceApiVarName() {
        if(StringUtils.isNotEmpty(serviceApiVarName)){
            return serviceApiVarName.substring(0,1).toLowerCase() + serviceApiVarName.substring(1);
        }
        return serviceApiVarName;
    }

    public void setServiceApiVarName(String serviceApiVarName) {
        this.serviceApiVarName = serviceApiVarName;
    }

    public String getSearchDsfMethodName() {
        return searchDsfMethodName;
    }

    public void setSearchDsfMethodName(String searchDsfMethodName) {
        this.searchDsfMethodName = searchDsfMethodName;
    }

    public String getSearchConditionDtoName() {
        return searchConditionDtoName;
    }

    public void setSearchConditionDtoName(String searchConditionDtoName) {
        this.searchConditionDtoName = searchConditionDtoName;
    }

    public String getDtoName() {
        return dtoName;
    }

    public void setDtoName(String dtoName) {
        this.dtoName = dtoName;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getAddFormDto() {
        return addFormDto;
    }

    public void setAddFormDto(String addFormDto) {
        this.addFormDto = addFormDto;
    }

    public String getUpdateFormDto() {
        return updateFormDto;
    }

    public void setUpdateFormDto(String updateFormDto) {
        this.updateFormDto = updateFormDto;
    }

    public List<String> getFormAttrSet() {
        return formAttrSet;
    }

    public void setFormAttrSet(List<String> formAttrSet) {
        this.formAttrSet = formAttrSet;
    }
}
