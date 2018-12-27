package com.feihua.framework.cms.front.web.mvc;

import com.feihua.exception.PageNotFoundException;
import com.feihua.framework.cms.CmsConstants;
import com.feihua.framework.cms.api.ApiCmsChannelPoService;
import com.feihua.framework.cms.api.ApiCmsSitePoService;
import com.feihua.framework.cms.dto.CmsChannelTemplateModelDto;
import com.feihua.framework.cms.dto.CmsSiteTemplateModelDto;
import com.feihua.framework.cms.po.CmsChannelPo;
import com.feihua.framework.cms.po.CmsSitePo;
import com.feihua.utils.http.httpServletRequest.RequestUtils;
import com.feihua.utils.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * cms前端页面访问入口
 * Created by yangwei
 */
@Controller
@RequestMapping(CmsConstants.requestPathPrefix)
public class DynamicChannelPageController extends DynamicBaseController {

    private static Logger logger = LoggerFactory.getLogger(DynamicChannelPageController.class);
    @Autowired
    private ApiCmsSitePoService apiCmsSitePoService;
    @Autowired
    private ApiCmsChannelPoService apiCmsChannelPoService;

    @RequestMapping(value = {"/{siteContextPath}/channel/{channelId}/index.htm"},method = RequestMethod.GET)
    public String channelStandardWithSiteContext(@PathVariable String siteContextPath,@PathVariable String channelId, Model model){
        CmsSitePo cmsSitePo = getSiteByPath(RequestUtils.wrapStartSlash(siteContextPath));
        if (cmsSitePo != null) {

            CmsChannelPo cmsChannelPo = getChannelById(channelId,cmsSitePo.getId());
            return channelDynamic(cmsSitePo,cmsChannelPo,model);

        }else{
            // 不存在站点，直接抛出异常
            throw new PageNotFoundException("");
        }
    }

    @RequestMapping(value = {"/{siteContextPath}/channel/{channelId}.htm"},method = RequestMethod.GET)
    public String channelSimpleWithSiteContext(@PathVariable String siteContextPath,@PathVariable String channelId,Model model){
        return channelStandardWithSiteContext(siteContextPath,channelId,model);
    }
    @RequestMapping(value = {"/channel/{channelId}/index.htm"},method = RequestMethod.GET)
    public String channelStandard(@PathVariable String channelId,Model model){
        CmsSitePo cmsSitePo = getMainSiteByDomain();
        if (cmsSitePo != null) {
            CmsChannelPo cmsChannelPo = getChannelById(channelId,cmsSitePo.getId());
            return channelDynamic(cmsSitePo,cmsChannelPo,model);
        }else{
            // 不存在站点，直接抛出异常
            throw new PageNotFoundException("");
        }
    }

    @RequestMapping(value = {"/channel/{channelId}.htm"},method = RequestMethod.GET)
    public String channelSimple(@PathVariable String channelId,Model model){
        return channelStandard(channelId,model);
    }

    // ******************************* 对栏目访问路径的支持

    @RequestMapping(value = {"/{siteContextPath}/{channelPath}/{channelId}/index.htm"},method = RequestMethod.GET)
    public String channelStandardWithSiteContextSupportPath(@PathVariable String siteContextPath,@PathVariable String channelPath,@PathVariable String channelId, Model model){
        CmsSitePo cmsSitePo = getSiteByPath(RequestUtils.wrapStartSlash(siteContextPath));
        if (cmsSitePo != null) {

            CmsChannelPo cmsChannelPo = getChannelByIdAndPath(channelId,cmsSitePo.getId(),channelPath);
            return channelDynamic(cmsSitePo,cmsChannelPo,model);

        }else{
            // 不存在站点，直接抛出异常
            throw new PageNotFoundException("");
        }
    }
    @RequestMapping(value = {"/{siteContextPath}/{channelPath}/{channelId}.htm"},method = RequestMethod.GET)
    public String channelSimpleWithSiteContextSupportPath(@PathVariable String siteContextPath,@PathVariable String channelPath,@PathVariable String channelId,Model model){
        return channelStandardWithSiteContextSupportPath(siteContextPath,channelPath,channelId,model);
    }
    @RequestMapping(value = {"/{channelPath}/{channelId}/index.htm"},method = RequestMethod.GET)
    public String channelStandardSupportPath(@PathVariable String channelPath,@PathVariable String channelId,Model model){
        CmsSitePo cmsSitePo = getMainSiteByDomain();
        if (cmsSitePo != null) {
            CmsChannelPo cmsChannelPo = getChannelByIdAndPath(channelId,cmsSitePo.getId(),channelPath);
            return channelDynamic(cmsSitePo,cmsChannelPo,model);
        }else{
            // 不存在站点，直接抛出异常
            throw new PageNotFoundException("");
        }
    }

    @RequestMapping(value = {"/{channelPath}/{channelId}.htm"},method = RequestMethod.GET)
    public String channelSimpleSupportPath(@PathVariable String channelPath,@PathVariable String channelId,Model model){
        return channelStandardSupportPath(channelPath,channelId,model);
    }

    // ******************************* 栏目访问目录的独特访问支持

    @RequestMapping(value = {"/{siteContextPath}/{channelPath}/index.htm"},method = RequestMethod.GET)
    public String channelStandardWithSiteContextSupportPathSpecial(@PathVariable String siteContextPath,@PathVariable String channelPath, Model model){
        CmsSitePo cmsSitePo = getSiteByPath(RequestUtils.wrapStartSlash(siteContextPath));
        if (cmsSitePo != null) {

            CmsChannelPo cmsChannelPo = getChannelByPathAndSiteId(RequestUtils.wrapStartSlash(channelPath),cmsSitePo.getId());
            return channelDynamic(cmsSitePo,cmsChannelPo,model);

        }else{
            // 不存在站点，直接抛出异常
            throw new PageNotFoundException("");
        }
    }
    @RequestMapping(value = {"/{siteContextPath}/{channelPath}"},method = RequestMethod.GET)
    public String channelSimpleWithSiteContextSupportPathSpecial(@PathVariable String siteContextPath,@PathVariable String channelPath,@PathVariable String channelId,Model model){
        return channelStandardWithSiteContextSupportPathSpecial(siteContextPath,channelPath,model);
    }
    @RequestMapping(value = {"/{siteContextPath}/{channelPath}/"},method = RequestMethod.GET)
    public String channelSimpleWithSiteContextSupportPathSpecial_1(@PathVariable String siteContextPath,@PathVariable String channelPath,@PathVariable String channelId,Model model){
        return channelStandardWithSiteContextSupportPathSpecial(siteContextPath,channelPath,model);
    }

/* 由于该路径与站点首页路径冲突，暂不支持，因为一样的路径取不到栏目，请使用站点上下文的方式访问
   @RequestMapping(value = {"/{channelPath}/index.htm"},method = RequestMethod.GET)
    public String channelStandardSupportPathSpecial(@PathVariable String channelPath,Model model){
        CmsSitePo cmsSitePo = getMainSiteByDomain();
        if (cmsSitePo != null) {
            CmsChannelPo cmsChannelPo = getChannelByPathAndSiteId(RequestUtils.wrapStartSlash(channelPath),cmsSitePo.getId());
            return channelDynamic(cmsSitePo,cmsChannelPo,model);
        }else{
            // 不存在站点，直接抛出异常
            throw new PageNotFoundException("");
        }
    }
    @RequestMapping(value = {"/{channelPath}"},method = RequestMethod.GET)
    public String channelSimpleSupportPathSpecial(@PathVariable String channelPath,Model model){
        return channelStandardSupportPathSpecial(channelPath,model);
    }
    @RequestMapping(value = {"/{channelPath}/"},method = RequestMethod.GET)
    public String channelSimpleSupportPathSpecial_1(@PathVariable String channelPath,Model model){
        return channelStandardSupportPathSpecial(channelPath,model);
    }*/



    private String channelDynamic(CmsSitePo cmsSitePo, CmsChannelPo cmsChannelPo,Model model){
        if (cmsChannelPo != null) {
            // 查找栏目模板的过程
            model.addAttribute(CmsConstants.model_channel,new CmsChannelTemplateModelDto(apiCmsChannelPoService.wrapDto(cmsChannelPo),getContextDto()));
            model.addAttribute(CmsConstants.model_site,new CmsSiteTemplateModelDto(apiCmsSitePoService.wrapDto(cmsSitePo),getContextDto()));


            String template = CmsConstants.indexHtml;
            if(!StringUtils.isEmpty(cmsChannelPo.getTemplate())){
                template = cmsChannelPo.getTemplate();
            }
            String templatePath = getTemplatePathForViewResolver(cmsSitePo) + CmsConstants.templateChannelPath + RequestUtils.wrapStartSlash(template);
            return templatePath;
        }else{
            // 不存在栏目，直接抛出异常
            throw new PageNotFoundException("");
        }
    }
}
