package com.feihua.framework.cms.directive;

import com.feihua.framework.cms.api.ApiCmsContentCategoryPoService;
import com.feihua.framework.cms.api.ApiCmsContentCategoryPoService;
import com.feihua.framework.cms.dto.CmsContentCategoryDto;
import com.feihua.framework.cms.dto.CmsContentCategoryTemplateModelDto;
import com.feihua.framework.cms.po.CmsContentCategoryPo;
import feihua.jdbc.api.pojo.BasePo;
import feihua.jdbc.api.pojo.PageAndOrderbyParamDto;
import feihua.jdbc.api.pojo.PageResultDto;
import feihua.jdbc.api.utils.OrderbyUtils;
import feihua.jdbc.api.utils.PageUtils;
import freemarker.core.Environment;
import freemarker.template.SimpleNumber;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by yangwei
 * Created at 2018/11/19 17:39
 */
@Component
public class CategoryListDirective extends AbstractDirective {

    @Autowired
    private ApiCmsContentCategoryPoService apiCmsContentCategoryPoService;
    private final static String varName = "categoryList";

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
        
        String channelId = getChannelId(params);

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

        String iteratorType = getIteratorType(params);
        if (body != null) {
            CmsContentCategoryPo cmsContentCategoryConditionPo = new CmsContentCategoryPo();
            cmsContentCategoryConditionPo.setDelFlag(BasePo.YesNo.N.name());
            cmsContentCategoryConditionPo.setSiteId(siteId);
            cmsContentCategoryConditionPo.setId(channelId);
            cmsContentCategoryConditionPo.setParentId(parentId);
            cmsContentCategoryConditionPo.setParentId1(parentId1 );
            cmsContentCategoryConditionPo.setParentId2(parentId2 );
            cmsContentCategoryConditionPo.setParentId3(parentId3 );
            cmsContentCategoryConditionPo.setParentId4(parentId4 );
            cmsContentCategoryConditionPo.setParentId5(parentId5 );
            cmsContentCategoryConditionPo.setParentId6(parentId6 );
            cmsContentCategoryConditionPo.setParentId7(parentId7 );
            cmsContentCategoryConditionPo.setParentId8(parentId8 );
            cmsContentCategoryConditionPo.setParentId9(parentId9 );
            cmsContentCategoryConditionPo.setParentId10(parentId10);

            PageAndOrderbyParamDto pageAndOrderbyParamDto = new PageAndOrderbyParamDto(PageUtils.getPageFromThreadLocal(), OrderbyUtils.getOrderbyFromThreadLocal());

            PageResultDto<CmsContentCategoryDto> pageResultDto = apiCmsContentCategoryPoService.selectList(cmsContentCategoryConditionPo,pageAndOrderbyParamDto);
            List<CmsContentCategoryDto> cmsContentCategoryDtos = pageResultDto.getData();
            if (cmsContentCategoryDtos != null && !cmsContentCategoryDtos.isEmpty()) {
                if(param_iterator_type_value_default.equals(iteratorType)){
                    for (int i = 0; i < cmsContentCategoryDtos.size(); i++) {
                        TemplateModel item =  wrapTemplateModel(new CmsContentCategoryTemplateModelDto(cmsContentCategoryDtos.get(i),getContextDto()));
                        TemplateModel index = new SimpleNumber(i);
                        bindLoopVars(0,item,loopVars);
                        bindLoopVars(1,index,loopVars);
                        body.render(env.getOut());
                    }
                } else if(param_iterator_type_value_var.equals(iteratorType)){
                    List<CmsContentCategoryTemplateModelDto> siteTemplateModelDtos = new ArrayList<>(cmsContentCategoryDtos.size());
                    for (int i = 0; i < cmsContentCategoryDtos.size(); i++) {
                        siteTemplateModelDtos.add(new CmsContentCategoryTemplateModelDto( cmsContentCategoryDtos.get(i),getContextDto()));
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
