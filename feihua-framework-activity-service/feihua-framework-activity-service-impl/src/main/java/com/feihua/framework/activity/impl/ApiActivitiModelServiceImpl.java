
package com.feihua.framework.activity.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.feihua.framework.activity.api.ApiActivitiModelService;
import com.feihua.framework.activity.dto.ExportModelDto;
import com.feihua.framework.activity.ActUtils;
import feihua.jdbc.api.pojo.Page;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.editor.constants.ModelDataJsonConstants;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.Model;
import org.activiti.engine.repository.ModelQuery;
import org.activiti.engine.repository.ProcessDefinition;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;


/**
 *
 */
@Service
@Transactional(readOnly = true)
public class ApiActivitiModelServiceImpl implements ApiActivitiModelService{

	@Autowired
	private RepositoryService repositoryService;
	/**
	 * 创建模型
	 * @throws UnsupportedEncodingException
	 */
	@Transactional(readOnly = false)
	public Model create(String name, String key, String description, String category) throws UnsupportedEncodingException {
		ObjectMapper objectMapper = new ObjectMapper();
		ObjectNode editorNode = objectMapper.createObjectNode();
		editorNode.put("id", "canvas");
		editorNode.put("resourceId", "canvas");
		ObjectNode stencilSetNode = objectMapper.createObjectNode();
		stencilSetNode.put("namespace", "http://b3mn.org/stencilset/bpmn2.0#");
		editorNode.put("stencilset", stencilSetNode);
		org.activiti.engine.repository.Model modelData = repositoryService.newModel();

		description = StringUtils.defaultString(description);
		modelData.setKey(StringUtils.defaultString(key));
		modelData.setName(name);
		modelData.setCategory(category);
		modelData.setVersion(Integer.parseInt(String.valueOf(repositoryService.createModelQuery().modelKey(modelData.getKey()).count()+1)));

		ObjectNode modelObjectNode = objectMapper.createObjectNode();
		modelObjectNode.put(ModelDataJsonConstants.MODEL_NAME, name);
		modelObjectNode.put(ModelDataJsonConstants.MODEL_REVISION, modelData.getVersion());
		modelObjectNode.put(ModelDataJsonConstants.MODEL_DESCRIPTION, description);
		modelData.setMetaInfo(modelObjectNode.toString());

		repositoryService.saveModel(modelData);
		repositoryService.addModelEditorSource(modelData.getId(), editorNode.toString().getBytes("utf-8"));

		return modelData;
	}

	/**
	 * 查询模型列表
	 * @param name
	 * @param category
	 * @return
	 */
	public List<Model> searchModel(String name, String category, Page page) {

		ModelQuery modelQuery = repositoryService.createModelQuery().latestVersion().orderByLastUpdateTime().desc();

		if (StringUtils.isNotEmpty(name)){
			modelQuery.modelNameLike(name);
		}
		if (StringUtils.isNotEmpty(category)){
			modelQuery.modelCategory(category);
		}
		return ActUtils.queryListPage(modelQuery,page);
	}

	/**
	 * 根据Model部署流程
	 * @param id
	 * @return 返回流程定义id
	 */
	@Transactional(readOnly = false)
	public String deploy(String id) throws IOException {
			org.activiti.engine.repository.Model modelData = repositoryService.getModel(id);
			BpmnJsonConverter jsonConverter = new BpmnJsonConverter();
			JsonNode editorNode = new ObjectMapper().readTree(repositoryService.getModelEditorSource(modelData.getId()));
			BpmnModel bpmnModel = jsonConverter.convertToBpmnModel(editorNode);
			BpmnXMLConverter xmlConverter = new BpmnXMLConverter();
			byte[] bpmnBytes = xmlConverter.convertToXML(bpmnModel);

			String processName = modelData.getName();
			if (!StringUtils.endsWith(processName, ".bpmn20.xml")){
				processName += ".bpmn20.xml";
			}
			ByteArrayInputStream in = new ByteArrayInputStream(bpmnBytes);
			Deployment deployment = repositoryService.createDeployment().name(modelData.getName())
					.addInputStream(processName, in).deploy();
			// 设置流程分类
			List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId()).list();
			for (ProcessDefinition processDefinition : list) {
				repositoryService.setProcessDefinitionCategory(processDefinition.getId(), modelData.getCategory());
				return processDefinition.getId();
			}
		return null;
	}
	/**
	 * 导出model的xml文件
	 * @throws IOException
	 */
	@Transactional(readOnly = false)
	public ExportModelDto export(String id) throws IOException {
		org.activiti.engine.repository.Model modelData = repositoryService.getModel(id);

		BpmnJsonConverter jsonConverter = new BpmnJsonConverter();
		JsonNode editorNode = new ObjectMapper().readTree(repositoryService.getModelEditorSource(modelData.getId()));

		BpmnModel bpmnModel = jsonConverter.convertToBpmnModel(editorNode);
		BpmnXMLConverter xmlConverter = new BpmnXMLConverter();
		byte[] bpmnBytes = xmlConverter.convertToXML(bpmnModel);
		String filename = bpmnModel.getMainProcess().getId() + ".bpmn20.xml";
		ByteArrayInputStream in = new ByteArrayInputStream(bpmnBytes);

        ExportModelDto exportModelDto = new ExportModelDto();
        exportModelDto.setFileName(filename);
        exportModelDto.setInputStream(in);
        return exportModelDto;

	}
	/**
	 * 更新Model分类
	 */
	@Transactional(readOnly = false)
	public void updateCategory(String id, String category) {
		org.activiti.engine.repository.Model modelData = repositoryService.getModel(id);
		modelData.setCategory(category);
		repositoryService.saveModel(modelData);
	}
}
