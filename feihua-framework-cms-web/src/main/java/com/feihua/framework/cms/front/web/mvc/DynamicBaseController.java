package com.feihua.framework.cms.front.web.mvc;

import com.feihua.framework.cms.CmsConstants;
import com.feihua.framework.cms.api.ApiCmsChannelPoService;
import com.feihua.framework.cms.api.ApiCmsContentPoService;
import com.feihua.framework.cms.api.ApiCmsSitePoService;
import com.feihua.framework.cms.dto.CmsTemplateModelContextDto;
import com.feihua.framework.cms.po.CmsChannelPo;
import com.feihua.framework.cms.po.CmsContentPo;
import com.feihua.framework.cms.po.CmsSitePo;
import com.feihua.framework.rest.mvc.SuperController;
import com.feihua.utils.http.httpServletRequest.RequestUtils;
import com.feihua.utils.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.List;

/**
 * cms前端页面访问入口
 * Created by yangwei
 */
@Controller
public class DynamicBaseController extends SuperController {
    private static Logger logger = LoggerFactory.getLogger(DynamicBaseController.class);


    @Autowired
    private ApiCmsSitePoService apiCmsSitePoService;
    @Autowired
    private ApiCmsChannelPoService apiCmsChannelPoService;
    @Autowired
    private ApiCmsContentPoService apiCmsContentPoService;

    protected CmsTemplateModelContextDto getContextDto(){
        CmsTemplateModelContextDto cmsTemplateModelContextDto = new CmsTemplateModelContextDto(true);
        return cmsTemplateModelContextDto;
    }

    protected String prependRequestPathPrefix(String param){
        return CmsConstants.requestPathPrefix + RequestUtils.wrapStartSlash(param);
    }

    /**
     * 获取站点的模板存在根目录
     * @param cmsSitePo
     * @return
     */
    protected String getTemplatePathForViewResolver(CmsSitePo cmsSitePo){
        String _templatePathDefault = CmsConstants.templatePathDefault;
        if(!StringUtils.isEmpty(cmsSitePo.getTemplatePath())){
            _templatePathDefault = cmsSitePo.getTemplatePath();
        }

        return CmsConstants.webinfPath + CmsConstants.templateRootPath + RequestUtils.wrapStartSlash(_templatePathDefault);
    }

    protected CmsSitePo getMainSiteByDomain(){
        HttpServletRequest request = RequestUtils.getRequest();
        String domain = RequestUtils.getDomain(request,true,true);
        // 根据域名查找主站点
        CmsSitePo cmsSitePo = apiCmsSitePoService.selecltMainSiteByDomain(domain);
        // 如果没有设置主站点，则取站点其中一个作为站点入口
        if (cmsSitePo == null) {
            List<CmsSitePo> cmsSitePos = apiCmsSitePoService.selecltByDomain(domain);
            if (cmsSitePos != null && !cmsSitePos.isEmpty()) {
                cmsSitePo = cmsSitePos.get(0);
            }
        }
        return cmsSitePo;
    }
    protected CmsSitePo getSiteByPath(String siteContextPath){
        CmsSitePo cmsSitePo = apiCmsSitePoService.selecltByPath(RequestUtils.wrapStartSlash(siteContextPath));

        return cmsSitePo;
    }

    protected CmsChannelPo getChannelById(String id,String siteId){
        CmsChannelPo cmsChannelPo =  apiCmsChannelPoService.selectByPrimaryKeySimple(id);
        if (cmsChannelPo != null && cmsChannelPo.getSiteId().equals(siteId)) {
            return cmsChannelPo;
        }
        return null;
    }

    protected CmsChannelPo getChannelByIdAndPath(String id,String siteId,String channelPath){
        CmsChannelPo cmsChannelPo =  apiCmsChannelPoService.selectByPrimaryKeySimple(id);
        if (cmsChannelPo != null && cmsChannelPo.getSiteId().equals(siteId) && channelPath.equals(cmsChannelPo.getPath())) {
            return cmsChannelPo;
        }
        return null;
    }
    protected CmsChannelPo getChannelByPathAndSiteId(String channelPath,String siteId){
        return apiCmsChannelPoService.selectByPathAndSiteId(channelPath,siteId);
    }
    protected CmsContentPo getContentByContentId(String contentId,String channelId,String siteId){
        return apiCmsContentPoService.selectByContentIdAndChannelIdAndSiteId(contentId,channelId,siteId);
    }
}
