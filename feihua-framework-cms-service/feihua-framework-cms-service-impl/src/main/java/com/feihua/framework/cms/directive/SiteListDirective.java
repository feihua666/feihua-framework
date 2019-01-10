package com.feihua.framework.cms.directive;

import com.feihua.framework.cms.DefaultObjectWrapperBuilderFactory;
import com.feihua.framework.cms.api.ApiCmsSitePoService;
import com.feihua.framework.cms.dto.CmsSiteDto;
import com.feihua.framework.cms.dto.CmsSiteTemplateModelDto;
import com.feihua.framework.cms.po.CmsSitePo;
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
public class SiteListDirective extends AbstractDirective {

    @Autowired
    private ApiCmsSitePoService apiCmsSitePoService;

    private final static String varName = "siteList";

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
        String isMain = getParam("isMain",params);

        String iteratorType = getIteratorType(params);


        if (body != null) {
            CmsSitePo cmsSitePoConditionPo = new CmsSitePo();
            cmsSitePoConditionPo.setDelFlag(BasePo.YesNo.N.name());
            cmsSitePoConditionPo.setId(siteId);
            cmsSitePoConditionPo.setIsMain(isMain);

            PageAndOrderbyParamDto pageAndOrderbyParamDto = new PageAndOrderbyParamDto(PageUtils.getPageFromThreadLocal(), OrderbyUtils.getOrderbyFromThreadLocal());

            PageResultDto<CmsSiteDto> pageResultDto = apiCmsSitePoService.selectList(cmsSitePoConditionPo,pageAndOrderbyParamDto);
            List<CmsSiteDto> cmsSiteDtos = pageResultDto.getData();
            if (cmsSiteDtos != null && !cmsSiteDtos.isEmpty()) {
                if(param_iterator_type_value_default.equals(iteratorType)){

                    for (int i = 0; i < cmsSiteDtos.size(); i++) {
                        TemplateModel item =  wrapTemplateModel(new CmsSiteTemplateModelDto( cmsSiteDtos.get(i),getContextDto()));
                        TemplateModel index = new SimpleNumber(i);
                        bindLoopVars(0,item,loopVars);
                        bindLoopVars(1,index,loopVars);
                        body.render(env.getOut());
                    }
                } else if(param_iterator_type_value_var.equals(iteratorType)){
                    List<CmsSiteTemplateModelDto> siteTemplateModelDtos = new ArrayList<>(cmsSiteDtos.size());
                    for (int i = 0; i < cmsSiteDtos.size(); i++) {
                        siteTemplateModelDtos.add(new CmsSiteTemplateModelDto( cmsSiteDtos.get(i),getContextDto()));
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
