package com.feihua.framework.activity.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.feihua.exception.BaseException;
import com.feihua.framework.activity.api.ApiActivitiProcessService;
import com.feihua.framework.activity.dto.ProcessDefinitionDto;
import com.feihua.framework.activity.ActUtils;
import feihua.jdbc.api.pojo.BasePo;
import feihua.jdbc.api.pojo.Page;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.editor.constants.ModelDataJsonConstants;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.runtime.ProcessInstanceQuery;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipInputStream;

/**
 * Created by yw on 2017/3/15.
 */
@Service
@Transactional(readOnly = true)
public class ApiActivitiProcessServiceImpl implements ApiActivitiProcessService{

    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private RuntimeService runtimeService;

    /**
     * 流程定义列表
     */
    public List<ProcessDefinitionDto> searchProcessDefinition(String category, Page page) {

        List<ProcessDefinition> processDefinitionList = null;
        List<ProcessDefinitionDto> result = new ArrayList<>();
        ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery()
                .latestVersion().orderByProcessDefinitionKey().asc();

        if (StringUtils.isNotEmpty(category)){
            processDefinitionQuery.processDefinitionCategory(category);
        }

        processDefinitionList = ActUtils.queryListPage(processDefinitionQuery,page);
        for (ProcessDefinition processDefinition : processDefinitionList) {
            String deploymentId = processDefinition.getDeploymentId();
            Deployment deployment = repositoryService.createDeploymentQuery().deploymentId(deploymentId).singleResult();

            ProcessDefinitionDto processDefinitionDto = new ProcessDefinitionDto();
            processDefinitionDto.setCategory(processDefinition.getCategory());
            processDefinitionDto.setId(processDefinition.getId());
            processDefinitionDto.setKey(processDefinition.getKey());
            processDefinitionDto.setName(processDefinition.getName());
            processDefinitionDto.setVersion(processDefinition.getVersion());
            processDefinitionDto.setResourceName(processDefinition.getResourceName());
            processDefinitionDto.setDiagramResourceName(processDefinition.getDiagramResourceName());
            processDefinitionDto.setSuspended(processDefinition.isSuspended()? BasePo.YesNo.Y.name() : BasePo.YesNo.N.name());
            processDefinitionDto.setDeploymentId(deployment.getId());
            processDefinitionDto.setDeploymentTime(deployment.getDeploymentTime());
            result.add(processDefinitionDto);
        }

        return result;
    }
    /**
     * 流程定义实例列表
     */
    public List<ProcessInstance> searchProcessInstance(String procInsId, String procDefKey, Page page) {

        ProcessInstanceQuery processInstanceQuery = runtimeService.createProcessInstanceQuery();

        if (StringUtils.isNotBlank(procInsId)){
            processInstanceQuery.processInstanceId(procInsId);
        }

        if (StringUtils.isNotBlank(procDefKey)){
            processInstanceQuery.processDefinitionKey(procDefKey);
        }

        return ActUtils.queryListPage(processInstanceQuery,page);
    }
    /**
     * 将部署的流程转换为模型
     * @param procDefId
     * @throws UnsupportedEncodingException
     * @throws XMLStreamException
     */
    @Transactional(readOnly = false)
    public org.activiti.engine.repository.Model convertProcessDefinitionToModel(String procDefId) throws UnsupportedEncodingException, XMLStreamException {

        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(procDefId).singleResult();
        InputStream bpmnStream = repositoryService.getResourceAsStream(processDefinition.getDeploymentId(),
                processDefinition.getResourceName());
        XMLInputFactory xif = XMLInputFactory.newInstance();
        InputStreamReader in = new InputStreamReader(bpmnStream, "UTF-8");
        XMLStreamReader xtr = xif.createXMLStreamReader(in);
        BpmnModel bpmnModel = new BpmnXMLConverter().convertToBpmnModel(xtr);

        BpmnJsonConverter converter = new BpmnJsonConverter();
        ObjectNode modelNode = converter.convertToJson(bpmnModel);
        org.activiti.engine.repository.Model modelData = repositoryService.newModel();
        modelData.setKey(processDefinition.getKey());
        modelData.setName(processDefinition.getResourceName());
        modelData.setCategory(processDefinition.getCategory());
        modelData.setDeploymentId(processDefinition.getDeploymentId());
        modelData.setVersion(Integer.parseInt(String.valueOf(repositoryService.createModelQuery().modelKey(modelData.getKey()).count()+1)));

        ObjectNode modelObjectNode = new ObjectMapper().createObjectNode();
        modelObjectNode.put(ModelDataJsonConstants.MODEL_NAME, processDefinition.getName());
        modelObjectNode.put(ModelDataJsonConstants.MODEL_REVISION, modelData.getVersion());
        modelObjectNode.put(ModelDataJsonConstants.MODEL_DESCRIPTION, processDefinition.getDescription());
        modelData.setMetaInfo(modelObjectNode.toString());
        repositoryService.saveModel(modelData);
        repositoryService.addModelEditorSource(modelData.getId(), modelNode.toString().getBytes("utf-8"));

        return modelData;
    }
    /**
     * 部署流程 - 保存
     * @param file
     * @return
     */
    @Transactional(readOnly = false)
    public String processDeploy(String category, MultipartFile file) throws IOException {

        String fileName = file.getOriginalFilename();

            InputStream fileInputStream = file.getInputStream();
            Deployment deployment = null;
            String extension = FilenameUtils.getExtension(fileName);
            if (extension.equals("zip") || extension.equals("bar")) {
                ZipInputStream zip = new ZipInputStream(fileInputStream);
                deployment = repositoryService.createDeployment().addZipInputStream(zip).deploy();
            } else if (extension.equals("png")) {
                deployment = repositoryService.createDeployment().addInputStream(fileName, fileInputStream).deploy();
            } else if (fileName.indexOf("bpmn20.xml") != -1) {
                deployment = repositoryService.createDeployment().addInputStream(fileName, fileInputStream).deploy();
            } else if (extension.equals("bpmn")) { // bpmn扩展名特殊处理，转换为bpmn20.xml
                String baseName = FilenameUtils.getBaseName(fileName);
                deployment = repositoryService.createDeployment().addInputStream(baseName + ".bpmn20.xml", fileInputStream).deploy();
            } else {
                throw new BaseException("不支持的文件类型：" + extension);
            }
            List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId()).list();

            // 设置流程分类
            for (ProcessDefinition processDefinition : list) {
                repositoryService.setProcessDefinitionCategory(processDefinition.getId(), category);
                return processDefinition.getId();
            }

        return null;
    }
}
