package com.feihua.framework.cms.directive;

import com.feihua.framework.cms.DefaultObjectWrapperBuilderFactory;
import com.feihua.framework.cms.ParamsRequiredException;
import com.feihua.framework.cms.api.ApiCmsChannelPoService;
import com.feihua.framework.cms.dto.CmsChannelDto;
import com.feihua.framework.cms.dto.CmsChannelTemplateModelDto;
import com.feihua.framework.cms.po.CmsChannelPo;
import feihua.jdbc.api.pojo.BasePo;
import feihua.jdbc.api.pojo.BaseTreePo;
import feihua.jdbc.api.pojo.PageAndOrderbyParamDto;
import feihua.jdbc.api.pojo.PageResultDto;
import feihua.jdbc.api.utils.OrderbyUtils;
import feihua.jdbc.api.utils.PageUtils;
import freemarker.core.Environment;
import freemarker.template.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by yangwei
 * Created at 2018/11/19 17:39
 */
@Component
public class ChannelListDirective extends AbstractDirective {

    @Autowired
    private ApiCmsChannelPoService apiCmsChannelPoService;
    private final static String varName = "channelList";

    /**
     *
     * @param env
     * @param params
     * @param loopVars
     * @param body
     * @throws TemplateException
     * @throws IOException
     */
    @Override
    public void doExecute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
        String siteId = getSiteId(params);
        assertStringParamNotEmpty(siteId,param_site_id);
        
        String parentId = getParentId(params);
        String parentId1 = getParentId1(params);
        String parentId2 = getParentId2(params);
        String parentId3 = getParentId3(params);
        String parentId4 = getParentId4(params);
        String parentId5 = getParentId5(params);
        String parentId6 = getParentId6(params);
        String parentId7 = getParentId7(params);
        String parentId8 = getParentId8(params);
        String parentId9 = getParentId9(params);
        String parentId10 = getParentId10(params);

        String channelId = getChannelId(params);

        String channelType = getParam("channelType",params);

        String iteratorType = getIteratorType(params);
        if (body != null) {
            CmsChannelPo cmsChannelConditionPo = new CmsChannelPo();
            cmsChannelConditionPo.setDelFlag(BasePo.YesNo.N.name());
            cmsChannelConditionPo.setSiteId(siteId);
            cmsChannelConditionPo.setParentId(parentId);
            cmsChannelConditionPo.setParentId1(parentId1 );
            cmsChannelConditionPo.setParentId2(parentId2 );
            cmsChannelConditionPo.setParentId3(parentId3 );
            cmsChannelConditionPo.setParentId4(parentId4 );
            cmsChannelConditionPo.setParentId5(parentId5 );
            cmsChannelConditionPo.setParentId6(parentId6 );
            cmsChannelConditionPo.setParentId7(parentId7 );
            cmsChannelConditionPo.setParentId8(parentId8 );
            cmsChannelConditionPo.setParentId9(parentId9 );
            cmsChannelConditionPo.setParentId10(parentId10);
            cmsChannelConditionPo.setChannelType(channelType);
            cmsChannelConditionPo.setId(channelId);

            PageAndOrderbyParamDto pageAndOrderbyParamDto = new PageAndOrderbyParamDto(PageUtils.getPageFromThreadLocal(), OrderbyUtils.getOrderbyFromThreadLocal());

            PageResultDto<CmsChannelDto> pageResultDto = apiCmsChannelPoService.selectList(cmsChannelConditionPo,pageAndOrderbyParamDto);
            List<CmsChannelDto> cmsChannelDtos = pageResultDto.getData();
            if (cmsChannelDtos != null && !cmsChannelDtos.isEmpty()) {
                if(param_iterator_type_value_default.equals(iteratorType)){
                    for (int i = 0; i < cmsChannelDtos.size(); i++) {
                        TemplateModel item =  wrapTemplateModel(new CmsChannelTemplateModelDto(cmsChannelDtos.get(i),getContextDto()));
                        TemplateModel index = new SimpleNumber(i);
                        bindLoopVars(0,item,loopVars);
                        bindLoopVars(1,index,loopVars);
                        body.render(env.getOut());
                    }
                } else if(param_iterator_type_value_var.equals(iteratorType)){
                    List<CmsChannelTemplateModelDto> siteTemplateModelDtos = new ArrayList<>(cmsChannelDtos.size());
                    for (int i = 0; i < cmsChannelDtos.size(); i++) {
                        siteTemplateModelDtos.add(new CmsChannelTemplateModelDto( cmsChannelDtos.get(i),getContextDto()));
                    }
                    TemplateModel item =  wrapTemplateModel(siteTemplateModelDtos);
                    env.setVariable(varName,item);
                    body.render(env.getOut());
                    //清除变量
                    env.setVariable(varName,null);
                }else{
                    body.render(env.getOut());
                }

            }else{
                body.render(env.getOut());
            }

        }
    }
}
