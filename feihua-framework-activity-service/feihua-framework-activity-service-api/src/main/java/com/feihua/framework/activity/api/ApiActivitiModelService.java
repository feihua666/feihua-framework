package com.feihua.framework.activity.api;

import com.feihua.framework.activity.dto.ExportModelDto;
import feihua.jdbc.api.pojo.Page;
import org.activiti.engine.repository.Model;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by yangwei
 * Created at 2018/4/9 9:03
 */
public interface ApiActivitiModelService {
    /**
     * 创建模型
     * @param name
     * @param key
     * @param description
     * @param category
     * @return
     * @throws UnsupportedEncodingException
     */
    public Model create(String name, String key, String description, String category) throws UnsupportedEncodingException;

    /**
     * 查询模型列表，模糊查询
     * @param name
     * @param category
     * @param page
     * @return
     */
    public List<Model> searchModel(String name, String category, Page page);

    /**
     * 根据Model部署流程
     * @param id
     * @return 返回流程定义id
     */
    public String deploy(String id) throws IOException;

    /**
     * 导出model的xml文件
     * @param id
     * @return
     */
    public ExportModelDto export(String id) throws IOException;

    /**
     * 更新Model分类
     * @param id
     * @param category
     */
    public void updateCategory(String id, String category);
}
