package com.feihua.framework.cms.directive;

import com.feihua.framework.cms.ParamsRequiredException;
import com.feihua.framework.cms.api.*;
import com.feihua.framework.cms.dto.CmsChannelDto;
import com.feihua.framework.cms.dto.CmsChannelTemplateModelDto;
import com.feihua.framework.cms.dto.CmsContentDto;
import com.feihua.framework.cms.dto.CmsContentTemplateModelDto;
import com.feihua.framework.cms.po.CmsContentCategoryRelPo;
import com.feihua.framework.cms.po.CmsContentPo;
import com.feihua.framework.constants.DictEnum;
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
import org.apache.commons.lang3.StringUtils;
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
public class ContentListDirective extends AbstractDirective {

    @Autowired
    private ApiCmsContentPoService apiCmsContentPoService;
    @Autowired
    private ApiCmsChannelPoService apiCmsChannelPoService;
    @Autowired
    private ApiCmsContentCategoryRelPoService apiCmsContentCategoryRelPoService;
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

    private final static String varName = "contentList";
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
        
        String channelId = getChannelId(params);

        String contentId = getContentId(params);
        String categoryId = getParam("categoryId",params);

        String contentType = getParam("contentType",params);

        String iteratorType = getIteratorType(params);

        if(StringUtils.isEmpty(siteId) && StringUtils.isEmpty(channelId) && StringUtils.isEmpty(contentId)){
            throw new ParamsRequiredException(param_site_id + " or "+ param_channel_id + " or "+ param_content_id);
        }


        if (body != null) {

            PageAndOrderbyParamDto pageAndOrderbyParamDto = new PageAndOrderbyParamDto(PageUtils.getPageFromThreadLocal(), OrderbyUtils.getOrderbyFromThreadLocal());

            PageResultDto<CmsContentDto> pageResultDto = null;
            if(StringUtils.isNotEmpty(categoryId)){
                List<CmsContentCategoryRelPo>  contentCategoryRelPos = apiCmsContentCategoryRelPoService.selectBySiteIdAndCategoryId(siteId,categoryId);
                if (contentCategoryRelPos != null && !contentCategoryRelPos.isEmpty()) {
                    List<String> contentIds = new ArrayList<>(contentCategoryRelPos.size());
                    for (CmsContentCategoryRelPo contentCategoryRelPo : contentCategoryRelPos) {
                        contentIds.add(contentCategoryRelPo.getContentId());
                    }
                    if(StringUtils.isNotEmpty(contentId)){
                        if(contentIds.contains(contentId)){
                            contentIds.clear();
                            contentIds.add(contentId);
                            pageResultDto = apiCmsContentPoService.selectByPrimaryKeys(contentIds,false,pageAndOrderbyParamDto);
                        }

                    }else
                    pageResultDto = apiCmsContentPoService.selectByPrimaryKeys(contentIds,false,pageAndOrderbyParamDto);
                }
            } else{
                CmsContentPo cmsContentPoConditionPo = new CmsContentPo();
                cmsContentPoConditionPo.setDelFlag(BasePo.YesNo.N.name());
                cmsContentPoConditionPo.setSiteId(siteId);
                cmsContentPoConditionPo.setChannelId(channelId);
                cmsContentPoConditionPo.setId(contentId);
                cmsContentPoConditionPo.setContentType(contentType);
                pageResultDto = apiCmsContentPoService.selectList(cmsContentPoConditionPo,pageAndOrderbyParamDto);
            }
            if (pageResultDto == null) {
                body.render(env.getOut());
                return;
            }
            List<CmsContentDto> cmsContentDtos = pageResultDto.getData();
            if (cmsContentDtos != null && !cmsContentDtos.isEmpty()) {


                if(param_iterator_type_value_default.equals(iteratorType)){
                    for (int i = 0; i < cmsContentDtos.size(); i++) {
                        CmsChannelDto cmsChannelDto  = apiCmsChannelPoService.selectByPrimaryKey(cmsContentDtos.get(i).getChannelId(),false);
                        CmsContentTemplateModelDto cmsContentTemplateModelDto = new CmsContentTemplateModelDto( cmsContentDtos.get(i),new CmsChannelTemplateModelDto(cmsChannelDto,getContextDto()),getContextDto());
                        cmsContentTemplateModelDto.setAttachments(apiCmsContentAttachmentPoService.wrapDtos(apiCmsContentAttachmentPoService.selectBySiteIdAndContentId(siteId,cmsContentTemplateModelDto.getId())));
                        setAddionalByContentType(cmsContentTemplateModelDto);
                        TemplateModel item =  wrapTemplateModel(cmsContentTemplateModelDto);
                        TemplateModel index = new SimpleNumber(i);
                        bindLoopVars(0,item,loopVars);
                        bindLoopVars(1,index,loopVars);
                        body.render(env.getOut());
                    }
                } else if(param_iterator_type_value_var.equals(iteratorType)){
                    List<CmsContentTemplateModelDto> siteTemplateModelDtos = new ArrayList<>(cmsContentDtos.size());
                    for (int i = 0; i < cmsContentDtos.size(); i++) {
                        CmsChannelDto cmsChannelDto  = apiCmsChannelPoService.selectByPrimaryKey(cmsContentDtos.get(i).getChannelId(),false);
                        CmsContentTemplateModelDto cmsContentTemplateModelDto = new CmsContentTemplateModelDto( cmsContentDtos.get(i),new CmsChannelTemplateModelDto(cmsChannelDto,getContextDto()),getContextDto());
                        cmsContentTemplateModelDto.setAttachments(apiCmsContentAttachmentPoService.wrapDtos(apiCmsContentAttachmentPoService.selectBySiteIdAndContentId(siteId,cmsContentTemplateModelDto.getId())));
                        setAddionalByContentType(cmsContentTemplateModelDto);
                        siteTemplateModelDtos.add(cmsContentTemplateModelDto);
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
