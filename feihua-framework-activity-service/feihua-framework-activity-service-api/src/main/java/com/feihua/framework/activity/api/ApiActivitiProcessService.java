package com.feihua.framework.activity.api;

import com.feihua.framework.activity.dto.ProcessDefinitionDto;
import feihua.jdbc.api.pojo.Page;
import org.activiti.engine.runtime.ProcessInstance;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by yangwei
 * Created at 2018/4/9 9:11
 */
public interface ApiActivitiProcessService {
    /**
     * 流程定义列表查询
     * @param category
     * @param page
     * @return
     */
    public List<ProcessDefinitionDto> searchProcessDefinition(String category, Page page);

    /**
     * 流程定义实例列表
     * @param procInsId
     * @param procDefKey
     * @param page
     * @return
     */
    public List<ProcessInstance> searchProcessInstance(String procInsId, String procDefKey, Page page);

    /**
     * 将部署的流程转换为模型
     * @param procDefId
     * @return
     * @throws UnsupportedEncodingException
     * @throws XMLStreamException
     */
    public org.activiti.engine.repository.Model convertProcessDefinitionToModel(String procDefId) throws UnsupportedEncodingException, XMLStreamException;

    /**
     * 部署流程
     * @param category
     * @param file
     * @return 返回部门的流程定义id
     * @throws IOException
     */
    public String processDeploy(String category, MultipartFile file) throws IOException;
}
