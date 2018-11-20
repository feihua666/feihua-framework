package com.feihua.framework.cms.directive;

import com.feihua.framework.cms.DefaultObjectWrapperBuilderFactory;
import com.feihua.framework.cms.ParamsRequiredException;
import com.feihua.framework.cms.api.ApiCmsChannelPoService;
import com.feihua.framework.cms.dto.CmsChannelDto;
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
        if(StringUtils.isEmpty(parentId)){
            parentId = BaseTreePo.defaultRootParentId;
        }

        String channelType = (String) params.get("channelType");

        if (body != null) {
            CmsChannelPo cmsChannelConditionPo = new CmsChannelPo();
            cmsChannelConditionPo.setDelFlag(BasePo.YesNo.N.name());
            cmsChannelConditionPo.setSiteId(siteId);
            cmsChannelConditionPo.setParentId(parentId);
            cmsChannelConditionPo.setChannelType(channelType);

            PageAndOrderbyParamDto pageAndOrderbyParamDto = new PageAndOrderbyParamDto(PageUtils.getPageFromThreadLocal(), OrderbyUtils.getOrderbyFromThreadLocal());

            PageResultDto<CmsChannelDto> pageResultDto = apiCmsChannelPoService.selectList(cmsChannelConditionPo,pageAndOrderbyParamDto);
            List<CmsChannelDto> cmsChannelDtos = pageResultDto.getData();
            if (cmsChannelDtos != null && !cmsChannelDtos.isEmpty()) {
                for (int i = 0; i < cmsChannelDtos.size(); i++) {
                    TemplateModel item =  DefaultObjectWrapperBuilderFactory.getDefaultObjectWrapper().wrap(cmsChannelDtos.get(i));
                    TemplateModel index = new SimpleNumber(i);
                    bindLoopVars(0,item,loopVars);
                    bindLoopVars(1,index,loopVars);
                    body.render(env.getOut()); 
                }
            }

        }
    }
}
