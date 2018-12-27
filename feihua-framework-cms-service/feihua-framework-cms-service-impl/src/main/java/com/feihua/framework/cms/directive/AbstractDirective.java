package com.feihua.framework.cms.directive;

import com.feihua.framework.cms.DefaultObjectWrapperBuilderFactory;
import com.feihua.framework.cms.ParamsRequiredException;
import com.feihua.framework.cms.dto.CmsTemplateModelContextDto;
import feihua.jdbc.api.utils.OrderbyUtils;
import feihua.jdbc.api.utils.PageUtils;
import freemarker.core.Environment;
import freemarker.template.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

/**
 * Created by yangwei
 * Created at 2018/11/19 17:39
 */
public abstract class AbstractDirective implements
        TemplateDirectiveModel {

    protected static final String param_site_id = "siteId";
    protected static final String param_channel_id = "channelId";
    protected static final String param_content_id = "contentId";
    protected static final String param_channel_parent_id = "parentId";


    protected static final String param_iterator_type = "iteratorType";

    protected static final String param_iterator_type_value_default = "default";
    protected static final String param_iterator_type_value_var = "var";

    protected CmsTemplateModelContextDto getContextDto(){
        CmsTemplateModelContextDto cmsTemplateModelContextDto = new CmsTemplateModelContextDto(true);
        return cmsTemplateModelContextDto;
    }

    @Override
    public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
        //分页
        PageUtils.putPageToThreadLocal(PageUtils.getPageFromMap(params));

        //排序
        OrderbyUtils.putOrderbyToThreadLocal(OrderbyUtils.getOrderbyFromMap(params));
        doExecute(env,params,loopVars,body);

    }

     public abstract void doExecute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException;

    protected String getSiteId(Map params){
        return getParam(param_site_id,params);
    }
    protected String getChannelId(Map params){
        return getParam(param_channel_id,params);
    }
    protected String getContentId(Map params){
        return getParam(param_content_id,params);
    }
    protected String getParentId(Map params){
        return getParam(param_channel_parent_id,params);
    }
    protected String getParentId1(Map params){
        return getParam(param_channel_parent_id + "1",params);
    }
    protected String getParentId2(Map params){
        return getParam(param_channel_parent_id + "2",params);
    }
    protected String getParentId3(Map params){
        return getParam(param_channel_parent_id + "3",params);
    }
    protected String getParentId4(Map params){
        return getParam(param_channel_parent_id + "4",params);
    }
    protected String getParentId5(Map params){
        return getParam(param_channel_parent_id + "5",params);
    }
    protected String getParentId6(Map params){
        return getParam(param_channel_parent_id + "6",params);
    }
    protected String getParentId7(Map params){
        return getParam(param_channel_parent_id + "7",params);
    }
    protected String getParentId8(Map params){
        return getParam(param_channel_parent_id + "8",params);
    }
    protected String getParentId9(Map params){
        return getParam(param_channel_parent_id + "9",params);
    }
    protected String getParentId10(Map params){
        return getParam(param_channel_parent_id + "10",params);
    }
    protected String getIteratorType(Map params){
        String type =  getParam(param_iterator_type,params);
        if (!param_iterator_type_value_var.equals(type)){
            return param_iterator_type_value_default;
        }

        return type;
    }

    protected String getParam(String paramName,Map params){
        Object obj = params.get(paramName);
        if (obj != null) {
            return obj.toString();
        }
        return null;
    }

    void assertStringParamNotEmpty(String val,String paramName) throws ParamsRequiredException {
        if (StringUtils.isEmpty(val)) {
            throw new ParamsRequiredException(paramName);
        }
    }
    void bindLoopVars(int index,TemplateModel loopVar,TemplateModel[] loopVars){
        if (loopVars != null && index >= 0 && loopVars.length > index ) {
            loopVars[index] = loopVar;
        }
    }

    protected TemplateModel wrapTemplateModel(Object obj) throws TemplateModelException {
        return DefaultObjectWrapperBuilderFactory.getDefaultObjectWrapper().wrap(obj);
    }
}
