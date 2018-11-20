package com.feihua.framework.cms.front.web.mvc;

import com.feihua.exception.PageNotFoundException;
import com.feihua.framework.cms.api.ApiCmsSitePoService;
import com.feihua.framework.cms.po.CmsChannelPo;
import com.feihua.framework.cms.po.CmsContentPo;
import com.feihua.framework.cms.po.CmsSitePo;
import com.feihua.framework.rest.mvc.SuperController;
import com.feihua.utils.http.httpServletRequest.RequestUtils;
import com.feihua.utils.io.FileUtils;
import feihua.jdbc.api.pojo.PageAndOrderbyParamDto;
import feihua.jdbc.api.utils.OrderbyUtils;
import feihua.jdbc.api.utils.PageUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.io.File;

import static com.feihua.framework.cms.front.web.mvc.DynamicBaseController.requestPathPrefix;

/**
 * cms前端页面访问入口
 * Created by yangwei
 */
@Controller
@RequestMapping(requestPathPrefix)
public class DynamicContentPageController extends DynamicBaseController {

    private static Logger logger = LoggerFactory.getLogger(DynamicContentPageController.class);

    @RequestMapping(value = {"/{siteContextPath}/channel/{channelId}/content/{contentId}/index.htm"},method = RequestMethod.GET)
    public String contentStandardWithSiteContext(@PathVariable String siteContextPath, @PathVariable String channelId,@PathVariable String contentId, Model model){
        CmsSitePo cmsSitePo = getSiteByPath(RequestUtils.wrapStartSlash(siteContextPath));
        if (cmsSitePo != null) {
            CmsChannelPo cmsChannelPo = getChannelById(channelId,cmsSitePo.getId());
            if (cmsChannelPo != null) {
                CmsContentPo cmsContentPo = getContentByContentId(contentId,channelId,cmsSitePo.getId());
                return contentDynamic(cmsSitePo,cmsChannelPo,cmsContentPo,model);
            }else{
                // 不存在栏目，直接抛出异常
                throw new PageNotFoundException("");
            }

        }else{
            // 不存在站点，直接抛出异常
            throw new PageNotFoundException("");
        }
    }

    @RequestMapping(value = {"/{siteContextPath}/channel/{channelId}/content/{contentId}.htm"},method = RequestMethod.GET)
    public String contentSimpleWithSiteContext(@PathVariable String siteContextPath,@PathVariable String channelId,@PathVariable String contentId,Model model){
        return contentStandardWithSiteContext(siteContextPath,channelId,contentId,model);
    }
    @RequestMapping(value = {"/channel/{channelId}/content/{contentId}/index.htm"},method = RequestMethod.GET)
    public String contentStandard(@PathVariable String channelId,@PathVariable String contentId,Model model){
        CmsSitePo cmsSitePo = getMainSiteByDomain();
        if (cmsSitePo != null) {
            CmsChannelPo cmsChannelPo = getChannelById(channelId,cmsSitePo.getId());
            if (cmsChannelPo != null) {

                CmsContentPo cmsContentPo = getContentByContentId(contentId,channelId,cmsSitePo.getId());
                return contentDynamic(cmsSitePo,cmsChannelPo,cmsContentPo,model);
            }else{
                // 不存在栏目，直接抛出异常
                throw new PageNotFoundException("");
            }
        }else{
            // 不存在站点，直接抛出异常
            throw new PageNotFoundException("");
        }
    }

    @RequestMapping(value = {"/channel/{channelId}/content/{contentId}.htm"},method = RequestMethod.GET)
    public String contentSimple(@PathVariable String channelId,@PathVariable String contentId,Model model){
        return contentStandard(channelId,contentId,model);
    }

    // ******************************* 对栏目访问路径的支持

    @RequestMapping(value = {"/{siteContextPath}/{channelPath}/{channelId}/content/{contentId}/index.htm"},method = RequestMethod.GET)
    public String contentStandardWithSiteContextSupportPath(@PathVariable String siteContextPath,@PathVariable String channelPath,@PathVariable String channelId,@PathVariable String contentId, Model model){
        CmsSitePo cmsSitePo = getSiteByPath(RequestUtils.wrapStartSlash(siteContextPath));
        if (cmsSitePo != null) {

            CmsChannelPo cmsChannelPo = getChannelByIdAndPath(channelId,cmsSitePo.getId(),channelPath);
            if (cmsChannelPo != null) {

                CmsContentPo cmsContentPo = getContentByContentId(contentId,channelId,cmsSitePo.getId());
                return contentDynamic(cmsSitePo,cmsChannelPo,cmsContentPo,model);
            }else{
                // 不存在栏目，直接抛出异常
                throw new PageNotFoundException("");
            }

        }else{
            // 不存在站点，直接抛出异常
            throw new PageNotFoundException("");
        }
    }
    @RequestMapping(value = {"/{siteContextPath}/{channelPath}/{channelId}/content/{contentId}.htm"},method = RequestMethod.GET)
    public String contentSimpleWithSiteContextSupportPath(@PathVariable String siteContextPath,@PathVariable String channelPath,@PathVariable String channelId, @PathVariable String contentId, Model model){
        return contentStandardWithSiteContextSupportPath(siteContextPath,channelPath,channelId,contentId,model);
    }
    @RequestMapping(value = {"/{channelPath}/{channelId}/content/{contentId}/index.htm"},method = RequestMethod.GET)
    public String contentStandardSupportPath(@PathVariable String channelPath,@PathVariable String channelId, @PathVariable String contentId, Model model){
        CmsSitePo cmsSitePo = getMainSiteByDomain();
        if (cmsSitePo != null) {
            CmsChannelPo cmsChannelPo = getChannelByIdAndPath(channelId,cmsSitePo.getId(),channelPath);
            if (cmsChannelPo != null) {

                CmsContentPo cmsContentPo = getContentByContentId(contentId,channelId,cmsSitePo.getId());
                return contentDynamic(cmsSitePo,cmsChannelPo,cmsContentPo,model);
            }else{
                // 不存在栏目，直接抛出异常
                throw new PageNotFoundException("");
            }
        }else{
            // 不存在站点，直接抛出异常
            throw new PageNotFoundException("");
        }
    }

    @RequestMapping(value = {"/{channelPath}/{channelId}/content/{contentId}.htm"},method = RequestMethod.GET)
    public String contentSimpleSupportPath(@PathVariable String channelPath,@PathVariable String channelId,@PathVariable String contentId, Model model){
        return contentStandardSupportPath(channelPath,channelId,contentId,model);
    }

    // ******************************* 栏目访问目录的独特访问支持

    @RequestMapping(value = {"/{siteContextPath}/{channelPath}/content/{contentId}/index.htm"},method = RequestMethod.GET)
    public String contentStandardWithSiteContextSupportPathSpecial(@PathVariable String siteContextPath,@PathVariable String channelPath,@PathVariable String contentId, Model model){
        CmsSitePo cmsSitePo = getSiteByPath(RequestUtils.wrapStartSlash(siteContextPath));
        if (cmsSitePo != null) {

            CmsChannelPo cmsChannelPo = getChannelByPathAndSiteId(RequestUtils.wrapStartSlash(channelPath),cmsSitePo.getId());
            if (cmsChannelPo != null) {

                CmsContentPo cmsContentPo = getContentByContentId(contentId,cmsChannelPo.getId(),cmsSitePo.getId());
                return contentDynamic(cmsSitePo,cmsChannelPo,cmsContentPo,model);
            }else{
                // 不存在栏目，直接抛出异常
                throw new PageNotFoundException("");
            }

        }else{
            // 不存在站点，直接抛出异常
            throw new PageNotFoundException("");
        }
    }
    @RequestMapping(value = {"/{siteContextPath}/{channelPath}/content/{contentId}.htm"},method = RequestMethod.GET)
    public String contentSimpleWithSiteContextSupportPathSpecial(@PathVariable String siteContextPath,@PathVariable String channelPath,@PathVariable String contentId,Model model){
        return contentStandardWithSiteContextSupportPathSpecial(siteContextPath,channelPath,contentId,model);
    }




    private String contentDynamic(CmsSitePo cmsSitePo, CmsChannelPo cmsChannelPo, CmsContentPo cmsContentPo, Model model){
        if (cmsChannelPo != null) {
            // 查找栏目模板的过程
            model.addAttribute("channel",cmsChannelPo);
            model.addAttribute("site",cmsSitePo);
            model.addAttribute("content",cmsContentPo);

            String template = indexHtml;
            if(!StringUtils.isEmpty(cmsContentPo.getTemplate())){
                template = cmsContentPo.getTemplate();
            }
            String templatePath = getTemplatePathForViewResolver(cmsSitePo) + templateContentPath + FileUtils.wrapStartFileSeparator(template);
            return templatePath;
        }else{
            // 不存在栏目，直接抛出异常
            throw new PageNotFoundException("");
        }
    }
}
