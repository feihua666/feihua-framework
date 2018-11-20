package com.feihua.framework.cms.directive;

import com.feihua.framework.cms.ParamsRequiredException;
import feihua.jdbc.api.utils.OrderbyUtils;
import feihua.jdbc.api.utils.PageUtils;
import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
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
}
