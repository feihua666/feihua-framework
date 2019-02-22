package com.feihua.framework.cms.front.web.mvc;

import com.feihua.exception.PageNotFoundException;
import com.feihua.framework.cms.CmsConstants;
import com.feihua.framework.cms.api.*;
import com.feihua.framework.cms.dto.CmsChannelTemplateModelDto;
import com.feihua.framework.cms.dto.CmsContentTemplateModelDto;
import com.feihua.framework.cms.dto.CmsSiteTemplateModelDto;
import com.feihua.framework.cms.po.CmsChannelPo;
import com.feihua.framework.cms.po.CmsContentPo;
import com.feihua.framework.cms.po.CmsSitePo;
import com.feihua.framework.constants.DictEnum;
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
public class DynamicContentPageController extends DynamicBaseController {

    private static Logger logger = LoggerFactory.getLogger(DynamicContentPageController.class);
    @Autowired
    private ApiCmsSitePoService apiCmsSitePoService;
    @Autowired
    private ApiCmsChannelPoService apiCmsChannelPoService;
    @Autowired
    private ApiCmsContentPoService apiCmsContentPoService;
    @Autowired
    private ApiCmsContentAttachmentPoService apiCmsContentAttachmentPoService;
    @Autowired
    private ApiCmsContentAudioPoService apiCmsContentAudioPoService;
    @Autowired
    private ApiCmsContentVideoPoService apiCmsContentVideoPoService;
    @Autowired
    private ApiCmsContentVideoOtherPlayerPoService apiCmsContentVideoOtherPlayerPoService;
    @Autowired
    private ApiCmsContentGalleryPoService apiCmsContentGalleryPoService;
    @Autowired
    private ApiCmsContentLibraryPoService apiCmsContentLibraryPoService;
    @Autowired
    private ApiCmsContentLibraryImagePoService apiCmsContentLibraryImagePoService;
    @Autowired
    private ApiCmsContentDownloadPoService apiCmsContentDownloadPoService;
    @Autowired
    private ApiCmsContentDownloadImagePoService apiCmsContentDownloadImagePoService;


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
            CmsChannelTemplateModelDto cmsChannelTemplateModelDto =  new CmsChannelTemplateModelDto(apiCmsChannelPoService.wrapDto(cmsChannelPo),getContextDto());
            model.addAttribute(CmsConstants.model_channel,cmsChannelTemplateModelDto);
            model.addAttribute(CmsConstants.model_site,new CmsSiteTemplateModelDto(apiCmsSitePoService.wrapDto(cmsSitePo),getContextDto()));
            CmsContentTemplateModelDto cmsContentTemplateModelDto = new CmsContentTemplateModelDto(apiCmsContentPoService.wrapDto(cmsContentPo),cmsChannelTemplateModelDto,getContextDto());
            cmsContentTemplateModelDto.setAttachments(apiCmsContentAttachmentPoService.wrapDtos(apiCmsContentAttachmentPoService.selectBySiteIdAndContentId(cmsContentTemplateModelDto.getSiteId(),cmsContentTemplateModelDto.getId())));
            setAddionalByContentType(cmsContentTemplateModelDto);
            model.addAttribute(CmsConstants.model_content,cmsContentTemplateModelDto);

            String template = CmsConstants.indexHtml;
            if(!StringUtils.isEmpty(cmsContentPo.getTemplate())){
                template = cmsContentPo.getTemplate();
            }
            String templatePath = getTemplatePathForViewResolver(cmsSitePo) + CmsConstants.templateContentPath + RequestUtils.wrapStartSlash(template);
            return templatePath;
        }else{
            // 不存在栏目，直接抛出异常
            throw new PageNotFoundException("");
        }
    }
    private void setAddionalByContentType(CmsContentTemplateModelDto dto){
        String siteId = dto.getSiteId();
        String contentId = dto.getId();
        if (DictEnum.CmsContentType.audio.name().equals(dto.getContentType())) {
            dto.setAudio(apiCmsContentAudioPoService.wrapDto(apiCmsContentAudioPoService.selectBySiteIdAndContentId(siteId,contentId)));
        } else if (DictEnum.CmsContentType.video.name().equals(dto.getContentType())) {
            dto.setVideo(apiCmsContentVideoPoService.wrapDto(apiCmsContentVideoPoService.selectBySiteIdAndContentId(siteId,contentId)));
            dto.setVideoOtherPlayers(apiCmsContentVideoOtherPlayerPoService.wrapDtos( apiCmsContentVideoOtherPlayerPoService.selectBySiteIdAndContentIdAndVideoId(siteId,contentId,dto.getVideo().getId())));

        }else if (DictEnum.CmsContentType.gallery.name().equals(dto.getContentType())) {
            dto.setGallerys(apiCmsContentGalleryPoService.wrapDtos(apiCmsContentGalleryPoService.selectBySiteIdAndContentId(siteId,contentId)));
        } else if (DictEnum.CmsContentType.library.name().equals(dto.getContentType())) {
            dto.setLibrary(apiCmsContentLibraryPoService.wrapDto(apiCmsContentLibraryPoService.selectBySiteIdAndContentId(siteId,contentId)));
            dto.setLibraryImages(apiCmsContentLibraryImagePoService.wrapDtos( apiCmsContentLibraryImagePoService.selectBySiteIdAndContentIdAndLibraryId(siteId,contentId,dto.getLibrary().getId())));

        } else if (DictEnum.CmsContentType.download.name().equals(dto.getContentType())) {
            dto.setDownload(apiCmsContentDownloadPoService.wrapDto(apiCmsContentDownloadPoService.selectBySiteIdAndContentId(siteId,contentId)));
            dto.setDownloadImages(apiCmsContentDownloadImagePoService.wrapDtos( apiCmsContentDownloadImagePoService.selectBySiteIdAndContentIdAndDownloadId(siteId,contentId,dto.getDownload().getId())));

        }
    }
}
