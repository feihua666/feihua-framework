package com.feihua.framework.base.generator;

import com.feihua.framework.base.pojo.ControllerModel;
import com.feihua.utils.io.FileUtils;
import com.feihua.utils.spring.SpringContextHolder;
import com.github.pagehelper.Page;
import feihua.jdbc.api.pojo.BaseConditionDto;
import feihua.jdbc.api.pojo.PageResultDto;
import feihua.jdbc.api.service.ApiBaseTreeService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.lang3.StringUtils;
import org.mybatis.generator.api.*;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.Element;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.AbstractXmlElementGenerator;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.SimpleSelectAllElementGenerator;
import org.mybatis.generator.config.PropertyRegistry;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.mybatis.generator.internal.util.JavaBeansUtil.getJavaBeansField;
import static org.mybatis.generator.internal.util.JavaBeansUtil.getJavaBeansGetter;
import static org.mybatis.generator.internal.util.JavaBeansUtil.getJavaBeansSetter;

/**
 * Created by yangwei
 * Created at 2017/8/17 10:20
 */
public class MyBatisGeneratorPlugin extends BaseMyBatisGeneratorPlugin {

    public static String baseTreePoFullPath = "feihua.jdbc.api.pojo.BaseTreePo";
    public static String springContextHolderFullPath = "com.feihua.utils.spring.SpringContextHolder";
    public static String baseConditionDtoFullPath = "feihua.jdbc.api.pojo.BaseConditionDto";

    public static String pageAndOrderbyParamDtoFullPath = "feihua.jdbc.api.pojo.PageAndOrderbyParamDto";

    public static String clientSearchDsfMethodNameFullPathKey = "clientSearchDsfMethodNameFullPath";
    public static String serviceSearchConditonDtoFullPathKey = "serviceSearchConditonDtoFullPath";
    public static String dtoFullPathKey = "dtoFullPath";
    public static String serviceSearchDsfMethodNameFullPathKey = "serviceSearchDsfMethodNameFullPath";
    public static String serviceInterfaceFullPathKey = "serviceInterfaceFullPath";

    public static String controllerAddFormFullPathKey = "controllerAddFormFullPath";
    public static String controllerAddUpdateFullPathKey = "controllerAddUpdateFullPath";

    @Override
    public boolean sqlMapInsertSelectiveElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        List<Element> children = element.getElements();
        for(Element cElement:children){
            if(cElement instanceof  XmlElement && ((XmlElement) cElement).getName().equals("selectKey")){
                ((XmlElement) cElement).getElements().clear();
                ((XmlElement) cElement).addElement(new TextElement("select replace(uuid(),'-','') from dual"));
                break;
            }
        }
        return super.sqlMapInsertSelectiveElementGenerated(element, introspectedTable);
    }

    @Override
    public boolean sqlMapInsertElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        List<Element> children = element.getElements();
        for(Element cElement:children){
            if(cElement instanceof  XmlElement && ((XmlElement) cElement).getName().equals("selectKey")){
                ((XmlElement) cElement).getElements().clear();
                ((XmlElement) cElement).addElement(new TextElement("select replace(uuid(),'-','') from dual"));
                break;
            }
        }
        return super.sqlMapInsertElementGenerated(element, introspectedTable);
    }
    public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass,
                                                 IntrospectedTable introspectedTable) {
        FullyQualifiedJavaType superClass = topLevelClass.getSuperClass();
        if(superClass != null){

            //如果是树表，添加父级为树basetreepo
            if(MybatisGeneratorConfig.getBoolean("treetable")){
                superClass = new FullyQualifiedJavaType(baseTreePoFullPath);
                superClass.addTypeArgument(new FullyQualifiedJavaType("String"));
                topLevelClass.setSuperClass(superClass);
            }else{
                superClass.addTypeArgument(new FullyQualifiedJavaType("String"));
            }
        }

        /*if(MybatisGeneratorConfig.getBoolean("generator.service")){*/
            //添加一个获取service的方法
            String modelName = introspectedTable.getTableConfiguration().getDomainObjectName();
            //生成api addService 接口
            FullyQualifiedJavaType apiServiceInterfaceType = new FullyQualifiedJavaType(MybatisGeneratorConfig.getProperty("service.targetPackage")+".Api"+MybatisGeneratorConfig.getProperty("api.service.prifix","")+modelName+"Service");

            Method serviceMethod = new Method();
            serviceMethod.setName("service");
            serviceMethod.setVisibility(JavaVisibility.PUBLIC);
            serviceMethod.setReturnType(new FullyQualifiedJavaType(apiServiceInterfaceType.getFullyQualifiedName()));
            serviceMethod.addBodyLine("return "+ springContextHolderFullPath +".getBean("+ apiServiceInterfaceType.getFullyQualifiedName() +".class);");
            topLevelClass.addMethod(serviceMethod);
       /* }*/

        // 如果为树显示的表，删除树
        if(MybatisGeneratorConfig.getBoolean("treetable")){
            List<Field> newField = new ArrayList<Field>();
            for (Field field : topLevelClass.getFields()) {
                if(StringUtils.containsAny(field.getName(),"parentId","level")){
                }else{
                    newField.add(field);
                }
            }
            topLevelClass.getFields().clear();
            topLevelClass.getFields().addAll(newField);

            List<Method> newMethod = new ArrayList<Method>();
            for (Method method : topLevelClass.getMethods()) {
                if(StringUtils.containsAny(method.getName(),"ParentId","Level")){
                }else{
                    newMethod.add(method);
                }
            }
            topLevelClass.getMethods().clear();
            topLevelClass.getMethods().addAll(newMethod);
        }
        initGlobalParam(introspectedTable);

        return true;
    }

    /**
     * 初始化一些全局的变量
     * @param introspectedTable
     */
    private void initGlobalParam(IntrospectedTable introspectedTable){
        //context.addProperty();
    }
    public boolean clientGenerated(Interface interfaze,
                                   TopLevelClass topLevelClass,
                                   IntrospectedTable introspectedTable) {

        String modelName = introspectedTable.getTableConfiguration().getDomainObjectName();
        Set<FullyQualifiedJavaType> set = interfaze.getSuperInterfaceTypes();
        for(FullyQualifiedJavaType  type:set){
            if(type.getFullyQualifiedName().equals(MybatisGeneratorConfig.getProperty("client.rootInterface"))){
                interfaze.addImportedType(new FullyQualifiedJavaType(introspectedTable.getBaseRecordType()));
                interfaze.addImportedType(new FullyQualifiedJavaType(MybatisGeneratorConfig.getProperty("client.rootInterface")));
                type.addTypeArgument(new FullyQualifiedJavaType(modelName));
                type.addTypeArgument(new FullyQualifiedJavaType("String"));
                break;
            }
        }

            Method searchDsfMethod = new Method();
            searchDsfMethod.setName(SearchDsfElementGenerator.selectAllStatementId.replace("#{param}",modelName.replace("Po","")));
            searchDsfMethod.setVisibility(JavaVisibility.PUBLIC);
            // 返回类型
            FullyQualifiedJavaType returnType = new FullyQualifiedJavaType(List.class.getName());
            returnType.addTypeArgument(new FullyQualifiedJavaType(modelName));
            searchDsfMethod.setReturnType(returnType);
            // 参数
            FullyQualifiedJavaType dtoType = new FullyQualifiedJavaType(
                    context.getJavaModelGeneratorConfiguration().getTargetPackage()
                            .substring(0,context.getJavaModelGeneratorConfiguration().getTargetPackage().lastIndexOf("."))
                            +".dto.Search"+modelName.replace("Po","")+"sConditionDto");
            FullyQualifiedJavaType parameterType = dtoType;
            searchDsfMethod.addParameter(new Parameter(parameterType,"dto"));
        // 添加searchDsf方法
        if(MybatisGeneratorConfig.getBoolean("generator.service.searchDsfMethod")){
            interfaze.addMethod(searchDsfMethod);

        }
        introspectedTable.getContext().addProperty(serviceSearchConditonDtoFullPathKey,dtoType.getFullyQualifiedName());
        introspectedTable.getContext().addProperty(clientSearchDsfMethodNameFullPathKey,interfaze.getType().getFullyQualifiedName() + "." + searchDsfMethod.getName());
        return true;
    }

    @Override
    public boolean sqlMapDocumentGenerated(Document document, IntrospectedTable introspectedTable) {

        //selectAll 方法
        addSelectAllElement(document.getRootElement());
        //DeleteByPrimaryKeys 方法
        addDeleteByPrimaryKeysElement(document.getRootElement());
        //DeleteFlagByPrimaryKey 方法
        addDeleteFlagByPrimaryKeyElement(document.getRootElement());
        //DeleteFlagByPrimaryKeys 方法
        addDeleteFlagByPrimaryKeysElement(document.getRootElement());
        //DeleteFlagByPrimaryKeysWithUpdateUser 方法
        addDeleteFlagByPrimaryKeysWithUpdateUserElement(document.getRootElement());
        //DeleteFlagSelective 方法
        addDeleteFlagSelectiveElement(document.getRootElement());
        //DeleteFlagSelectiveWithUpdateUser 方法
        addDeleteFlagSelectiveWithUpdateUserElement(document.getRootElement());
        //DeleteSelective 方法
        addDeleteSelectiveElement(document.getRootElement());
        //SelectOne 方法
        addSelectOneElement(document.getRootElement());
        // SelectByPrimaryKeyWithoutDeleted 方法
        addSelectByPrimaryKeyWithoutDeletedElement(document.getRootElement());
        //SelectList 方法
        addSelectListElement(document.getRootElement());
        //Select 方法
        addSelectElement(document.getRootElement());
        //Count 方法
        addCountElement(document.getRootElement());
        //Counts 方法
        addCountsElement(document.getRootElement());
        //selectByPrimaryKeys 方法，根据主键查询
        addSelectByPrimaryKeysElement(document.getRootElement());
        //insertWithPrimaryKey 方法，插入方法
        addInsertWithPrimaryKeyElement(document.getRootElement());
        //insertSelectiveWithPrimaryKey 方法，
        addInsertSelectiveWithPrimaryKeyElement(document.getRootElement());
        //insertBatch 方法
        addInsertBatchElement(document.getRootElement());
        //insertBatchWithPrimaryKey 方法
        addInsertBatchWithPrimaryKeyElement(document.getRootElement());
        //update 方法
        addUpdateElement(document.getRootElement());
        //updateSelective 方法
        addUpdateSelectiveElement(document.getRootElement());
        //updateBatchByPrimaryKeys 方法
        addUpdateBatchByPrimaryKeysElement(document.getRootElement());
        //updateBatchByPrimaryKey 方法
        addUpdateBatchByPrimaryKeyElement(document.getRootElement());
        //updateBatchByPrimaryKeySelective 方法
        addUpdateBatchByPrimarySelectiveKeyElement(document.getRootElement());

        //updateBatchByPrimaryKeysSelective 方法
        addUpdateBatchByPrimaryKeysSelectiveElement(document.getRootElement());

        //SearchDsfElement 方法
        if(MybatisGeneratorConfig.getBoolean("generator.service.searchDsfMethod")) {
            addSearchDsfElement(document.getRootElement());
        }

        return super.sqlMapDocumentGenerated(document, introspectedTable);
    }
    /**
     * 生成service和dto
     * @param introspectedTable
     * @return
     */
    public List<GeneratedJavaFile> contextGenerateAdditionalJavaFiles(
            IntrospectedTable introspectedTable) {

        List<GeneratedJavaFile> result = new ArrayList<GeneratedJavaFile>();
            result.add(addSearchDto(introspectedTable));
        result.add(addDto(introspectedTable));
        result.add(addService(introspectedTable));
        result.add(addServiceImpl(introspectedTable));
        //生成api addService 接口实现

        if(MybatisGeneratorConfig.getBoolean("generator.controller")){
            try {
                result.add(addAddFormDto(introspectedTable));
                result.add(addUpdateFormDto(introspectedTable));
                addController(introspectedTable);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (TemplateException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    private GeneratedJavaFile addServiceImpl(IntrospectedTable introspectedTable){

        String modelName = introspectedTable.getTableConfiguration().getDomainObjectName();
        //生成api addService 接口
        FullyQualifiedJavaType apiServiceImplClassType = new FullyQualifiedJavaType(MybatisGeneratorConfig.getProperty("serviceimpl.targetPackage")+".Api"+MybatisGeneratorConfig.getProperty("api.service.prifix","")+modelName+"ServiceImpl");
        TopLevelClass apiServiceInterface = new TopLevelClass(apiServiceImplClassType);
        apiServiceInterface.setVisibility(JavaVisibility.PUBLIC);
        apiServiceInterface.addAnnotation("@Service");
        apiServiceInterface.addImportedType("org.springframework.stereotype.Service");


        //添加构造方法
        Method constructor = new Method();
        constructor.setName(apiServiceImplClassType.getShortName());
        constructor.setVisibility(JavaVisibility.PUBLIC);
        constructor.setConstructor(true);
        constructor.addBodyLine("super("+ new FullyQualifiedJavaType(context.getProperty(dtoFullPathKey)).getShortName()  +".class);");
        apiServiceInterface.addMethod(constructor);

        FullyQualifiedJavaType apiServiceImplSuperInterfaceType = new FullyQualifiedJavaType(context.getProperty(serviceInterfaceFullPathKey));
        apiServiceInterface.addSuperInterface(new FullyQualifiedJavaType(apiServiceImplSuperInterfaceType.getShortName()));
        apiServiceInterface.addImportedType(apiServiceImplSuperInterfaceType);

        FullyQualifiedJavaType baseServie = new FullyQualifiedJavaType(MybatisGeneratorConfig.getProperty("abstractBaseService"));

        baseServie.addTypeArgument(new FullyQualifiedJavaType(modelName));
        baseServie.addTypeArgument(new FullyQualifiedJavaType(introspectedTable.getContext().getProperty(dtoFullPathKey)));
        baseServie.addTypeArgument(new FullyQualifiedJavaType("String"));
        apiServiceInterface.addImportedType(new FullyQualifiedJavaType(introspectedTable.getBaseRecordType()));
        apiServiceInterface.addImportedType(new FullyQualifiedJavaType(introspectedTable.getContext().getProperty(dtoFullPathKey)));
        apiServiceInterface.addImportedType(baseServie);


        apiServiceInterface.setSuperClass(new FullyQualifiedJavaType(baseServie.getShortName()));

        // 注入mapper
        String mapperFullName = introspectedTable.getDAOInterfaceType().replace("DAO","Mapper");
        String name = mapperFullName.substring(mapperFullName.lastIndexOf(".") + 1);
        Field fieldMapper = new Field(name,new FullyQualifiedJavaType(mapperFullName));
        fieldMapper.addAnnotation("@Autowired");
        apiServiceInterface.addImportedType("org.springframework.beans.factory.annotation.Autowired");
        apiServiceInterface.addField(fieldMapper);

        //添加searchDsf方法
        //添加searchDsf方法
        if(MybatisGeneratorConfig.getBoolean("generator.service.searchDsfMethod")){
            Method searchDsfMethod = new Method();
            searchDsfMethod.addAnnotation("@Override");
            searchDsfMethod.setName(StringUtils.substringAfterLast(introspectedTable.getContext().getProperty(serviceSearchDsfMethodNameFullPathKey),"."));
            searchDsfMethod.setVisibility(JavaVisibility.PUBLIC);
            // 返回类型
            FullyQualifiedJavaType returnType = new FullyQualifiedJavaType(PageResultDto.class.getSimpleName());
            returnType.addTypeArgument(new FullyQualifiedJavaType(introspectedTable.getContext().getProperty(dtoFullPathKey)));
            searchDsfMethod.setReturnType(returnType);
            // 参数
            FullyQualifiedJavaType parameterType = new FullyQualifiedJavaType(introspectedTable.getContext().getProperty(serviceSearchConditonDtoFullPathKey));
            searchDsfMethod.addParameter(new Parameter(parameterType,"dto"));
            FullyQualifiedJavaType parameterType1 = new FullyQualifiedJavaType(pageAndOrderbyParamDtoFullPath);

            searchDsfMethod.addParameter(new Parameter(parameterType1,"pageAndOrderbyParamDto"));

            List<String> lines = new ArrayList<String>();
            lines.add("Page p = super.pageAndOrderbyStart(pageAndOrderbyParamDto);");
            lines.add("List<"+ introspectedTable.getContext().getProperty(dtoFullPathKey) +"> list = this.wrapDtos("+ name +"."+
                    StringUtils.substringAfterLast(introspectedTable.getContext().getProperty(clientSearchDsfMethodNameFullPathKey),".") +"(dto));");

            lines.add("return new PageResultDto(list, this.wrapPage(p));");
            searchDsfMethod.addBodyLines(lines);

            apiServiceInterface.addImportedType("java.util.List");
            apiServiceInterface.addImportedType(PageResultDto.class.getName());
            apiServiceInterface.addImportedType(Page.class.getName());
            apiServiceInterface.addMethod(searchDsfMethod);
        }

        // 重写wrapDto方法
        {
            Method searchWrapDtoMethod = new Method();
            searchWrapDtoMethod.addAnnotation("@Override");
            searchWrapDtoMethod.setName("wrapDto");
            searchWrapDtoMethod.setVisibility(JavaVisibility.PUBLIC);
            searchWrapDtoMethod.addParameter(new Parameter(new FullyQualifiedJavaType(modelName),"po"));
            List<String> lines = new ArrayList<String>();
            lines.add("if (po == null) { return null; }");
            String dto = new FullyQualifiedJavaType(introspectedTable.getContext().getProperty(dtoFullPathKey)).getShortName();
            lines.add(dto + " dto = new " + dto + "();");

            //添加属性
            List<IntrospectedColumn> introspectedColumns = introspectedTable.getAllColumns();
            for (IntrospectedColumn introspectedColumn : introspectedColumns) {

                Method methodGet = getJavaBeansGetter(introspectedColumn, context, introspectedTable);
                Method methodSet = getJavaBeansSetter(introspectedColumn, context, introspectedTable);


                String methodSetName = methodSet.getName();
                String parentIdXPrefix = "setParentId";
                if(methodSetName.startsWith(parentIdXPrefix) && methodSetName.length() > parentIdXPrefix.length()){
                    continue;
                }
                if(methodSetName.startsWith("setDelFlag")
                        || methodSetName.startsWith("setCreateBy")
                        || methodSetName.startsWith("setCreateAt")
                        ||methodSetName.startsWith("setUpdateBy")){
                    continue;
                }

                if(methodSetName.startsWith("set")){

                    lines.add("dto." + methodSetName + "(po."+ methodGet.getName() +"());");
                }

            }
            lines.add("return dto;");

            searchWrapDtoMethod.addBodyLines(lines);
            searchWrapDtoMethod.setReturnType(new FullyQualifiedJavaType(dto));
            apiServiceInterface.addMethod(searchWrapDtoMethod);
        }

        context.getCommentGenerator().addJavaFileComment(apiServiceInterface);
        GeneratedJavaFile apiServiceFile =  new GeneratedJavaFile(apiServiceInterface,
                context.getJavaClientGeneratorConfiguration()
                        .getTargetProject(),
                context.getProperty(PropertyRegistry.CONTEXT_JAVA_FILE_ENCODING),
                context.getJavaFormatter());


        return apiServiceFile;
    }

    /**
     * 添加控制器文件
     * @param introspectedTable
     * @return
     */
    private void addController(IntrospectedTable introspectedTable) throws IOException, TemplateException {
        String modelName = introspectedTable.getTableConfiguration().getDomainObjectName();


        Configuration cfg = new Configuration(Configuration.VERSION_2_3_28);
        cfg.setClassForTemplateLoading(this.getClass(),"/");
        cfg.setDefaultEncoding("UTF-8");
        cfg.unsetCacheStorage();
        Template template = cfg.getTemplate("controller.ftl");

        ControllerModel controllerModel = new ControllerModel();
        controllerModel.setModelName(modelName);
        controllerModel.setModuleComment(MybatisGeneratorConfig.getProperty("controller.moduleComment"));
        controllerModel.setModuleName(MybatisGeneratorConfig.getProperty("controller.moduleName"));
        controllerModel.setClassMappingPath(MybatisGeneratorConfig.getProperty("controller.classMappingPath"));
        controllerModel.setControllerComment(MybatisGeneratorConfig.getProperty("controller.controllerComment"));
        controllerModel.setControllerName(modelName.replace("Po","") + "Controller");
        controllerModel.setDtoName(StringUtils.substringAfterLast(introspectedTable.getContext().getProperty(dtoFullPathKey),"."));
        controllerModel.setMethodMappingPath(MybatisGeneratorConfig.getProperty("controller.methodMappingPath"));
        controllerModel.setMethodRequiresPermissionsPre(MybatisGeneratorConfig.getProperty("controller.methodRequiresPermissionsPre"));
        controllerModel.setSearchConditionDtoName(StringUtils.substringAfterLast(introspectedTable.getContext().getProperty(serviceSearchConditonDtoFullPathKey),"."));
        controllerModel.setSearchDsfMethodName(StringUtils.substringAfterLast(introspectedTable.getContext().getProperty(serviceSearchDsfMethodNameFullPathKey),"."));
        controllerModel.setServiceApiName(StringUtils.substringAfterLast(introspectedTable.getContext().getProperty(serviceInterfaceFullPathKey),"."));
        controllerModel.setAddFormDto(StringUtils.substringAfterLast(introspectedTable.getContext().getProperty(controllerAddFormFullPathKey),"."));
        controllerModel.setUpdateFormDto(StringUtils.substringAfterLast(introspectedTable.getContext().getProperty(controllerAddUpdateFullPathKey),"."));
        // 自动转换了首字母大小写
        controllerModel.setServiceApiVarName(StringUtils.substringAfterLast(introspectedTable.getContext().getProperty(serviceInterfaceFullPathKey),"."));
        List<String> imports = new ArrayList<String>();
        imports.add(introspectedTable.getContext().getProperty(dtoFullPathKey));
        imports.add(introspectedTable.getContext().getProperty(serviceSearchConditonDtoFullPathKey));
        imports.add(introspectedTable.getContext().getProperty(serviceInterfaceFullPathKey));
        imports.add(introspectedTable.getContext().getProperty(controllerAddFormFullPathKey));
        imports.add(introspectedTable.getContext().getProperty(controllerAddUpdateFullPathKey));
        imports.add(introspectedTable.getBaseRecordType());
        controllerModel.setImportList(imports);

        // form
        List<String> formAttrSet = new ArrayList<>();
        List<IntrospectedColumn> introspectedColumns = introspectedTable.getAllColumns();
        for (IntrospectedColumn introspectedColumn : introspectedColumns) {
            Method methodGet = getJavaBeansGetter(introspectedColumn, context, introspectedTable);
            Method methodSet = getJavaBeansSetter(introspectedColumn, context, introspectedTable);
            formAttrSet.add("basePo." + methodSet.getName() + "(dto."+ methodGet.getName() +"());");
            /*Method method = getJavaBeansGetter(introspectedColumn, context, introspectedTable);
            method.getJavaDocLines().clear();
            dtoClass.addMethod(method);
            method = getJavaBeansSetter(introspectedColumn, context, introspectedTable);
            dtoClass.addMethod(method);*/
        }
        controllerModel.setFormAttrSet(formAttrSet);
        String r = FreeMarkerTemplateUtils.processTemplateIntoString(template,controllerModel);

        String filePath = MybatisGeneratorConfig.getProperty("controller.targetProject")
                + File.separator
                + MybatisGeneratorConfig.getProperty("controller.targetPackage").replace(".",File.separator)
                + File.separator;

        FileUtils.createFolder(filePath);
        FileUtils.writeString(FileUtils.createFile(filePath +  controllerModel.getControllerName() + ".java" ),r);
    }
    private GeneratedJavaFile addAddFormDto(IntrospectedTable introspectedTable){
        String modelName = introspectedTable.getTableConfiguration().getDomainObjectName();

        FullyQualifiedJavaType dtoType = new FullyQualifiedJavaType(MybatisGeneratorConfig.getProperty("controller.dto.targetPackage")
                + ".Add"+modelName.replace("Po","FormDto"));

        TopLevelClass dtoClass = new TopLevelClass(dtoType);
        dtoClass.setVisibility(JavaVisibility.PUBLIC);

        //添加属性
        List<IntrospectedColumn> introspectedColumns = introspectedTable.getAllColumns();
        for (IntrospectedColumn introspectedColumn : introspectedColumns) {
            Field field = getJavaBeansField(introspectedColumn, context, introspectedTable);
            field.getJavaDocLines().clear();
            dtoClass.addField(field);
            dtoClass.addImportedType(field.getType());

            /*Method method = getJavaBeansGetter(introspectedColumn, context, introspectedTable);
            method.getJavaDocLines().clear();
            dtoClass.addMethod(method);
            method = getJavaBeansSetter(introspectedColumn, context, introspectedTable);
            dtoClass.addMethod(method);*/
        }
        introspectedTable.getContext().addProperty(controllerAddFormFullPathKey,dtoClass.getType().getFullyQualifiedName());
        context.getCommentGenerator().addClassComment(dtoClass,introspectedTable);
        GeneratedJavaFile apiServiceFile =  new GeneratedJavaFile(dtoClass,
                MybatisGeneratorConfig.getProperty("controller.targetProject"),
                context.getProperty(PropertyRegistry.CONTEXT_JAVA_FILE_ENCODING),
                context.getJavaFormatter());

        return apiServiceFile;
    }
    private GeneratedJavaFile addUpdateFormDto(IntrospectedTable introspectedTable){
        String modelName = introspectedTable.getTableConfiguration().getDomainObjectName();

        FullyQualifiedJavaType dtoType = new FullyQualifiedJavaType(MybatisGeneratorConfig.getProperty("controller.dto.targetPackage")
        + ".Update"+modelName.replace("Po","FormDto"));

        TopLevelClass dtoClass = new TopLevelClass(dtoType);
        dtoClass.setVisibility(JavaVisibility.PUBLIC);
        FullyQualifiedJavaType dtosuperClass = new FullyQualifiedJavaType(MybatisGeneratorConfig.getProperty("controller.updateFormSuperClass"));
        dtoClass.setSuperClass(new FullyQualifiedJavaType(dtosuperClass.getShortName()));
        dtoClass.addImportedType(dtosuperClass);



        //添加属性
        List<IntrospectedColumn> introspectedColumns = introspectedTable.getAllColumns();
        for (IntrospectedColumn introspectedColumn : introspectedColumns) {
            Field field = getJavaBeansField(introspectedColumn, context, introspectedTable);
            field.getJavaDocLines().clear();
            dtoClass.addField(field);
            dtoClass.addImportedType(field.getType());

            /*Method method = getJavaBeansGetter(introspectedColumn, context, introspectedTable);
            method.getJavaDocLines().clear();
            dtoClass.addMethod(method);
            method = getJavaBeansSetter(introspectedColumn, context, introspectedTable);
            dtoClass.addMethod(method);*/
        }
        introspectedTable.getContext().addProperty(controllerAddUpdateFullPathKey,dtoClass.getType().getFullyQualifiedName());
        context.getCommentGenerator().addClassComment(dtoClass,introspectedTable);
        GeneratedJavaFile apiServiceFile =  new GeneratedJavaFile(dtoClass,
                MybatisGeneratorConfig.getProperty("controller.targetProject"),
                context.getProperty(PropertyRegistry.CONTEXT_JAVA_FILE_ENCODING),
                context.getJavaFormatter());

        return apiServiceFile;
    }
    /**
     * service 接口
     * @param introspectedTable
     * @return
     */
    private GeneratedJavaFile addService(IntrospectedTable introspectedTable){

        String modelName = introspectedTable.getTableConfiguration().getDomainObjectName();
        //生成api addService 接口
        FullyQualifiedJavaType apiServiceInterfaceType = new FullyQualifiedJavaType(MybatisGeneratorConfig.getProperty("service.targetPackage")+".Api"+MybatisGeneratorConfig.getProperty("api.service.prifix","")+modelName+"Service");
        Interface apiServiceInterface = new Interface(apiServiceInterfaceType);
        apiServiceInterface.setVisibility(JavaVisibility.PUBLIC);
        FullyQualifiedJavaType apiServiceSuperInterfaceType = new FullyQualifiedJavaType(MybatisGeneratorConfig.getProperty("interfaceBaseService"));
        apiServiceSuperInterfaceType.addTypeArgument(new FullyQualifiedJavaType(modelName));
        apiServiceSuperInterfaceType.addTypeArgument(new FullyQualifiedJavaType(introspectedTable.getContext().getProperty(dtoFullPathKey)));
        apiServiceSuperInterfaceType.addTypeArgument(new FullyQualifiedJavaType("String"));

        apiServiceInterface.addImportedType(new FullyQualifiedJavaType(introspectedTable.getBaseRecordType()));
        apiServiceInterface.addImportedType(new FullyQualifiedJavaType(introspectedTable.getContext().getProperty(dtoFullPathKey)));

        apiServiceInterface.addSuperInterface(apiServiceSuperInterfaceType);


            Method searchDsfMethod = new Method();
            searchDsfMethod.setName("search" + modelName.replace("Po","") + "sDsf");
            searchDsfMethod.setVisibility(JavaVisibility.PUBLIC);
            // 返回类型
            FullyQualifiedJavaType returnType = new FullyQualifiedJavaType(PageResultDto.class.getSimpleName());
            returnType.addTypeArgument(new FullyQualifiedJavaType(introspectedTable.getContext().getProperty(dtoFullPathKey)));
            searchDsfMethod.setReturnType(returnType);
            // 参数
            FullyQualifiedJavaType parameterType = new FullyQualifiedJavaType(introspectedTable.getContext().getProperty(serviceSearchConditonDtoFullPathKey));
            searchDsfMethod.addParameter(new Parameter(parameterType,"dto"));
            FullyQualifiedJavaType parameterType1 = new FullyQualifiedJavaType(pageAndOrderbyParamDtoFullPath);

            searchDsfMethod.addParameter(new Parameter(parameterType1,"pageAndOrderbyParamDto"));
        //添加searchDsf方法
        if(MybatisGeneratorConfig.getBoolean("generator.service.searchDsfMethod")){
            apiServiceInterface.addMethod(searchDsfMethod);
            apiServiceInterface.addImportedType(new FullyQualifiedJavaType(PageResultDto.class.getName()));
        }
        introspectedTable.getContext().addProperty(serviceSearchDsfMethodNameFullPathKey,apiServiceInterface.getType().getFullyQualifiedName() + "." + searchDsfMethod.getName());



        context.addProperty(serviceInterfaceFullPathKey,apiServiceInterfaceType.getFullyQualifiedName());
        context.getCommentGenerator().addJavaFileComment(apiServiceInterface);

        GeneratedJavaFile apiServiceFile =  new GeneratedJavaFile(apiServiceInterface,
                context.getJavaModelGeneratorConfiguration()
                        .getTargetProject(),
                context.getProperty(PropertyRegistry.CONTEXT_JAVA_FILE_ENCODING),
                context.getJavaFormatter());

        return apiServiceFile;
    }
    private GeneratedJavaFile addSearchDto(IntrospectedTable introspectedTable){
        String modelName = introspectedTable.getTableConfiguration().getDomainObjectName();

        FullyQualifiedJavaType dtoType = new FullyQualifiedJavaType(introspectedTable.getContext().getProperty(serviceSearchConditonDtoFullPathKey));

        TopLevelClass dtoClass = new TopLevelClass(dtoType);
        dtoClass.setVisibility(JavaVisibility.PUBLIC);
        FullyQualifiedJavaType dtosuperClass = new FullyQualifiedJavaType(baseConditionDtoFullPath);
        dtoClass.setSuperClass(new FullyQualifiedJavaType(dtosuperClass.getShortName()));
        dtoClass.addImportedType(dtosuperClass);


        //context.getCommentGenerator().addJavaFileComment(dtoClass);

        //添加属性
        List<IntrospectedColumn> introspectedColumns = introspectedTable.getAllColumns();
        for (IntrospectedColumn introspectedColumn : introspectedColumns) {
            Field field = getJavaBeansField(introspectedColumn, context, introspectedTable);
            field.getJavaDocLines().clear();
            dtoClass.addField(field);
            dtoClass.addImportedType(field.getType());

            Method method = getJavaBeansGetter(introspectedColumn, context, introspectedTable);
            method.getJavaDocLines().clear();
            dtoClass.addMethod(method);
            method = getJavaBeansSetter(introspectedColumn, context, introspectedTable);
            dtoClass.addMethod(method);
        }
        context.getCommentGenerator().addClassComment(dtoClass,introspectedTable);
        GeneratedJavaFile apiServiceFile =  new GeneratedJavaFile(dtoClass,
                context.getJavaModelGeneratorConfiguration()
                        .getTargetProject(),
                context.getProperty(PropertyRegistry.CONTEXT_JAVA_FILE_ENCODING),
                context.getJavaFormatter());

        return apiServiceFile;
    }
    private GeneratedJavaFile addDto(IntrospectedTable introspectedTable){
        String modelName = introspectedTable.getTableConfiguration().getDomainObjectName();

        FullyQualifiedJavaType dtoType = new FullyQualifiedJavaType(context.getJavaModelGeneratorConfiguration().getTargetPackage().substring(0,context.getJavaModelGeneratorConfiguration().getTargetPackage().lastIndexOf("."))+".dto."+modelName.replace("Po","")+"Dto");

        TopLevelClass dtoClass = new TopLevelClass(dtoType);
        dtoClass.setVisibility(JavaVisibility.PUBLIC);
        FullyQualifiedJavaType dtosuperClass = new FullyQualifiedJavaType(MybatisGeneratorConfig.getProperty("pojo.basedto"));
        dtosuperClass.addTypeArgument(new FullyQualifiedJavaType("String"));
        dtoClass.setSuperClass(new FullyQualifiedJavaType(dtosuperClass.getShortName()));
        dtoClass.addImportedType(dtosuperClass);
        introspectedTable.getContext().addProperty(dtoFullPathKey,dtoType.getFullyQualifiedName());

        //context.getCommentGenerator().addJavaFileComment(dtoClass);

        //添加属性
        List<IntrospectedColumn> introspectedColumns = introspectedTable.getAllColumns();
        for (IntrospectedColumn introspectedColumn : introspectedColumns) {
            Field field = getJavaBeansField(introspectedColumn, context, introspectedTable);
            field.getJavaDocLines().clear();
            dtoClass.addField(field);
            dtoClass.addImportedType(field.getType());

            Method method = getJavaBeansGetter(introspectedColumn, context, introspectedTable);
            method.getJavaDocLines().clear();
            dtoClass.addMethod(method);
            method = getJavaBeansSetter(introspectedColumn, context, introspectedTable);
            dtoClass.addMethod(method);
        }
        context.getCommentGenerator().addClassComment(dtoClass,introspectedTable);
        GeneratedJavaFile apiServiceFile =  new GeneratedJavaFile(dtoClass,
                context.getJavaModelGeneratorConfiguration()
                        .getTargetProject(),
                context.getProperty(PropertyRegistry.CONTEXT_JAVA_FILE_ENCODING),
                context.getJavaFormatter());

        return apiServiceFile;
    }

    protected void addSelectAllElement(XmlElement parentElement) {
        AbstractXmlElementGenerator elementGenerator = new SelectAllElementGenerator();
        initializeAndExecuteGenerator(elementGenerator, parentElement);
    }

    protected void addDeleteByPrimaryKeysElement(XmlElement parentElement) {
        DeleteByPrimaryKeysElementGenerator elementGenerator = new DeleteByPrimaryKeysElementGenerator();
        initializeAndExecuteGenerator(elementGenerator, parentElement);
    }
    protected void addDeleteFlagByPrimaryKeyElement(XmlElement parentElement) {
        DeleteFlagByPrimaryKeyElementGenerator elementGenerator = new DeleteFlagByPrimaryKeyElementGenerator();
        initializeAndExecuteGenerator(elementGenerator, parentElement);
    }
    protected void addDeleteFlagByPrimaryKeysElement(XmlElement parentElement) {
        DeleteFlagByPrimaryKeysElementGenerator elementGenerator = new DeleteFlagByPrimaryKeysElementGenerator();
        initializeAndExecuteGenerator(elementGenerator, parentElement);
    }
    protected void addDeleteFlagByPrimaryKeysWithUpdateUserElement(XmlElement parentElement) {
        DeleteFlagByPrimaryKeysWithUpdateUserElementGenerator elementGenerator = new DeleteFlagByPrimaryKeysWithUpdateUserElementGenerator();
        initializeAndExecuteGenerator(elementGenerator, parentElement);
    }
    protected void addDeleteFlagSelectiveElement(XmlElement parentElement) {
        DeleteFlagSelectiveElementGenerator elementGenerator = new DeleteFlagSelectiveElementGenerator();
        initializeAndExecuteGenerator(elementGenerator, parentElement);
    }
    protected void addDeleteFlagSelectiveWithUpdateUserElement(XmlElement parentElement) {
        DeleteFlagSelectiveWithUpdateUserElementGenerator elementGenerator = new DeleteFlagSelectiveWithUpdateUserElementGenerator();
        initializeAndExecuteGenerator(elementGenerator, parentElement);
    }
    protected void addDeleteSelectiveElement(XmlElement parentElement) {
        DeleteSelectiveElementGenerator elementGenerator = new DeleteSelectiveElementGenerator();
        initializeAndExecuteGenerator(elementGenerator, parentElement);
    }
    protected void addSelectByPrimaryKeyWithoutDeletedElement(XmlElement parentElement) {
        SelectByPrimaryKeyWithoutDeletedElementGenerator elementGenerator = new SelectByPrimaryKeyWithoutDeletedElementGenerator();
        initializeAndExecuteGenerator(elementGenerator, parentElement);
    }
    protected void addSelectOneElement(XmlElement parentElement) {
        SelectOneElementGenerator elementGenerator = new SelectOneElementGenerator();
        initializeAndExecuteGenerator(elementGenerator, parentElement);
    }
    protected void addSelectListElement(XmlElement parentElement) {
        SelectListElementGenerator elementGenerator = new SelectListElementGenerator();
        initializeAndExecuteGenerator(elementGenerator, parentElement);
    }
    protected void addSelectElement(XmlElement parentElement) {
        SelectElementGenerator elementGenerator = new SelectElementGenerator();
        initializeAndExecuteGenerator(elementGenerator, parentElement);
    }
    protected void addCountElement(XmlElement parentElement) {
        CountElementGenerator elementGenerator = new CountElementGenerator();
        initializeAndExecuteGenerator(elementGenerator, parentElement);
    }
    protected void addCountsElement(XmlElement parentElement) {
        CountsElementGenerator elementGenerator = new CountsElementGenerator();
        initializeAndExecuteGenerator(elementGenerator, parentElement);
    }
    protected void addSelectByPrimaryKeysElement(XmlElement parentElement) {
        SelectByPrimaryKeysElementGenerator elementGenerator = new SelectByPrimaryKeysElementGenerator();
        initializeAndExecuteGenerator(elementGenerator, parentElement);
    }
    protected void addInsertWithPrimaryKeyElement(XmlElement parentElement) {
        InsertWithPrimaryKeyElementGenerator elementGenerator = new InsertWithPrimaryKeyElementGenerator();
        initializeAndExecuteGenerator(elementGenerator, parentElement);
    }
    protected void addInsertSelectiveWithPrimaryKeyElement(XmlElement parentElement) {
        InsertSelectiveWithPrimaryKeyElementGenerator elementGenerator = new InsertSelectiveWithPrimaryKeyElementGenerator();
        initializeAndExecuteGenerator(elementGenerator, parentElement);
    }
    protected void addInsertBatchElement(XmlElement parentElement) {
        InsertBatchElementGenerator elementGenerator = new InsertBatchElementGenerator();
        initializeAndExecuteGenerator(elementGenerator, parentElement);
    }
    protected void addInsertBatchWithPrimaryKeyElement(XmlElement parentElement) {
        InsertBatchWithPrimaryKeyElementGenerator elementGenerator = new InsertBatchWithPrimaryKeyElementGenerator();
        initializeAndExecuteGenerator(elementGenerator, parentElement);
    }
    protected void addUpdateElement(XmlElement parentElement) {
        UpdateElementGenerator elementGenerator = new UpdateElementGenerator();
        initializeAndExecuteGenerator(elementGenerator, parentElement);
    }
    protected void addUpdateSelectiveElement(XmlElement parentElement) {
        UpdateSelectiveElementGenerator elementGenerator = new UpdateSelectiveElementGenerator();
        initializeAndExecuteGenerator(elementGenerator, parentElement);
    }
    protected void addUpdateBatchByPrimaryKeysElement(XmlElement parentElement) {
        UpdateBatchByPrimaryKeysElementGenerator elementGenerator = new UpdateBatchByPrimaryKeysElementGenerator();
        initializeAndExecuteGenerator(elementGenerator, parentElement);
    }
    protected void addUpdateBatchByPrimaryKeyElement(XmlElement parentElement) {
        UpdateBatchByPrimaryKeyElementGenerator elementGenerator = new UpdateBatchByPrimaryKeyElementGenerator();
        initializeAndExecuteGenerator(elementGenerator, parentElement);
    }
    protected void addUpdateBatchByPrimarySelectiveKeyElement(XmlElement parentElement) {
        UpdateBatchByPrimaryKeySelectiveElementGenerator elementGenerator = new UpdateBatchByPrimaryKeySelectiveElementGenerator();
        initializeAndExecuteGenerator(elementGenerator, parentElement);
    }
    protected void addUpdateBatchByPrimaryKeysSelectiveElement(XmlElement parentElement) {
        UpdateBatchByPrimaryKeysSelectiveElementGenerator elementGenerator = new UpdateBatchByPrimaryKeysSelectiveElementGenerator();
        initializeAndExecuteGenerator(elementGenerator, parentElement);
    }
    protected void addSearchDsfElement(XmlElement parentElement) {
        SearchDsfElementGenerator elementGenerator = new SearchDsfElementGenerator();
        initializeAndExecuteGenerator(elementGenerator, parentElement);
    }
}
