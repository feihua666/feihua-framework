package com.feihua.framework.cms.admin.rest.mvc;

import com.feihua.framework.base.modules.role.dto.BaseRoleDto;
import com.feihua.framework.cms.CmsConstants;
import com.feihua.framework.cms.admin.rest.dto.*;
import com.feihua.framework.cms.api.*;
import com.feihua.framework.cms.dto.*;
import com.feihua.framework.cms.po.*;
import com.feihua.framework.constants.DictEnum;
import com.feihua.framework.rest.ResponseJsonRender;
import com.feihua.framework.rest.interceptor.RepeatFormValidator;
import com.feihua.utils.http.httpServletResponse.ResponseCode;
import feihua.jdbc.api.pojo.BasePo;
import feihua.jdbc.api.pojo.PageAndOrderbyParamDto;
import feihua.jdbc.api.pojo.PageResultDto;
import feihua.jdbc.api.utils.OrderbyUtils;
import feihua.jdbc.api.utils.PageUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 内容管理
 * Created by yangwei
 */
@RestController
@RequestMapping("/cms")
public class CmsContentController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(CmsContentController.class);

    @Autowired
    private ApiCmsContentPoService apiCmsContentPoService;
    @Autowired
    private ApiCmsChannelPoService apiCmsChannelPoService;
    @Autowired
    private ApiCmsSitePoService apiCmsSitePoService;
    @Autowired
    private ApiCmsContentAttachmentPoService apiCmsContentAttachmentPoService;
    @Autowired
    private ApiCmsContentCategoryPoService apiCmsContentCategoryPoService;
    @Autowired
    private ApiCmsContentCategoryRelPoService apiCmsContentCategoryRelPoService;
    @Autowired
    private ApiCmsContentGalleryPoService apiCmsContentGalleryPoService;
    @Autowired
    private ApiCmsContentLibraryPoService apiCmsContentLibraryPoService;
    @Autowired
    private ApiCmsContentLibraryImagePoService apiCmsContentLibraryImagePoService;
    @Autowired
    private ApiCmsContentDownloadPoService apiCmsContentDownloadPoService;
    @Autowired
    private ApiCmsContentAudioPoService apiCmsContentAudioPoService;
    @Autowired
    private ApiCmsContentVideoPoService apiCmsContentVideoPoService;
    @Autowired
    private ApiCmsContentVideoOtherPlayerPoService apiCmsContentVideoOtherPlayerPoService;
    @Autowired
    private ApiCmsContentDownloadImagePoService apiCmsContentDownloadImagePoService;

    /**
     * 单资源，添加
     * @param dto
     * @return
     */
    @RepeatFormValidator
    @RequiresPermissions("content:add")
    @RequestMapping(value = "/content",method = RequestMethod.POST)
    public ResponseEntity add(AddCmsContentFormDto dto){
        logger.info("添加内容开始");
        logger.info("当前登录用户id:{}",getLoginUser().getId());
        ResponseJsonRender resultData=new ResponseJsonRender();
        // 表单值设置
        CmsContentPo basePo = new CmsContentPo();
        basePo.setTitle(dto.getTitle());
        basePo.setAuthor(dto.getAuthor());
        basePo.setStatus(dto.getStatus());
        basePo.setSiteId(dto.getSiteId());
        basePo.setChannelId(dto.getChannelId());
        basePo.setContent(dto.getContent());
        basePo.setContentType(dto.getContentType());
        basePo.setTemplate(dto.getTemplate());
        basePo.setPublishAt(dto.getPublishAt());
        basePo.setProfile(dto.getProfile());
        basePo.setOriginal(dto.getOriginal());
        basePo.setImageUrl(dto.getImageUrl());
        basePo.setImageDes(dto.getImageDes());
        basePo.setIv(0);
        basePo.setUv(0);
        basePo.setPv(0);

        basePo = apiCmsContentPoService.preInsert(basePo,getLoginUser().getId());
        CmsContentDto r = apiCmsContentPoService.insert(basePo);
        if (r == null) {
            // 添加失败
            resultData.setCode(ResponseCode.E404_100001.getCode());
            resultData.setMsg(ResponseCode.E404_100001.getMsg());
            logger.info("code:{},msg:{}",resultData.getCode(),resultData.getMsg());
            logger.info("添加内容结束，失败");
            return new ResponseEntity(resultData,HttpStatus.NOT_FOUND);
        }else{
            // 添加成功，返回添加的数据
            // 添加附件信息
            if(DictEnum.CmsContentType.article.name().equals(r.getContentType())){
                List<AddCmsContentAttachmentFormDto> attachmentFormDtos = dto.getAttachments();
                if(attachmentFormDtos != null && !attachmentFormDtos.isEmpty()){
                    CmsContentAttachmentPo cmsContentAttachmentPo = null;
                    List<CmsContentAttachmentPo> toBeInsertContentAttachmentPos = new ArrayList<>(attachmentFormDtos.size());
                    for (AddCmsContentAttachmentFormDto attachmentFormDto : attachmentFormDtos) {
                        if(StringUtils.isNotEmpty(attachmentFormDto.getUrl())){
                            cmsContentAttachmentPo = new CmsContentAttachmentPo();
                            cmsContentAttachmentPo.setContentId(r.getId());
                            cmsContentAttachmentPo.setSiteId(r.getSiteId());
                            cmsContentAttachmentPo.setDescription(attachmentFormDto.getDescription());
                            cmsContentAttachmentPo.setUrl(attachmentFormDto.getUrl());
                            cmsContentAttachmentPo.setDwonloadNum(0);
                            cmsContentAttachmentPo.setExt(attachmentFormDto.getExt());
                            cmsContentAttachmentPo.setFilename(attachmentFormDto.getFilename());
                            cmsContentAttachmentPo.setSize(attachmentFormDto.getSize());
                            cmsContentAttachmentPo.setImageUrl(attachmentFormDto.getImageUrl());
                            cmsContentAttachmentPo.setType(attachmentFormDto.getType());
                            cmsContentAttachmentPo.setImageDes(attachmentFormDto.getImageDes());
                            cmsContentAttachmentPo.setSequence(attachmentFormDto.getSequence());
                            cmsContentAttachmentPo = apiCmsContentAttachmentPoService.preInsert(cmsContentAttachmentPo,getLoginUser().getId());
                            toBeInsertContentAttachmentPos.add(cmsContentAttachmentPo);
                        }
                    }
                    apiCmsContentAttachmentPoService.insertBatch(toBeInsertContentAttachmentPos);

                }
            }
            // 添加关联分类
            List<String> categoryIds = dto.getCategoryIds();
            if (categoryIds != null && !categoryIds.isEmpty()) {
                CmsContentCategoryRelPo cmsContentCategoryRelPo = null;
                List<CmsContentCategoryRelPo> toBeInsertCategoryRelPos = new ArrayList<>(categoryIds.size());
                for (String categoryId : categoryIds) {
                    cmsContentCategoryRelPo = new CmsContentCategoryRelPo();
                    cmsContentCategoryRelPo.setSiteId(r.getSiteId());
                    cmsContentCategoryRelPo.setContentId(r.getId());
                    cmsContentCategoryRelPo.setContentCategoryId(categoryId);
                    cmsContentCategoryRelPo = apiCmsContentCategoryRelPoService.preInsert(cmsContentCategoryRelPo,getLoginUserId());
                    toBeInsertCategoryRelPos.add(cmsContentCategoryRelPo);

                }
                apiCmsContentCategoryRelPoService.insertBatch(toBeInsertCategoryRelPos);
            }

            // 添加图库信息
            if(DictEnum.CmsContentType.gallery.name().equals(r.getContentType())){
                List<AddCmsContentGalleryFormDto> galleryFormDtos = dto.getGallerys();
                if(galleryFormDtos != null && !galleryFormDtos.isEmpty()){
                    CmsContentGalleryPo cmsContentGalleryPo = null;
                    List<CmsContentGalleryPo> toBeInsertContentGalleryPos = new ArrayList<>(galleryFormDtos.size());
                    for (AddCmsContentGalleryFormDto galleryFormDto : galleryFormDtos) {
                        if(StringUtils.isNotEmpty(galleryFormDto.getImageUrl())){
                            cmsContentGalleryPo = new CmsContentGalleryPo();
                            cmsContentGalleryPo.setContentId(r.getId());
                            cmsContentGalleryPo.setSiteId(r.getSiteId());
                            cmsContentGalleryPo.setImageUrl(galleryFormDto.getImageUrl());
                            cmsContentGalleryPo.setImageThumbnailUrl(galleryFormDto.getImageThumbnailUrl());
                            cmsContentGalleryPo.setImageDes(galleryFormDto.getImageDes());
                            cmsContentGalleryPo.setSequence(galleryFormDto.getSequence());
                            cmsContentGalleryPo = apiCmsContentGalleryPoService.preInsert(cmsContentGalleryPo,getLoginUser().getId());
                            toBeInsertContentGalleryPos.add(cmsContentGalleryPo);
                        }
                    }
                    apiCmsContentGalleryPoService.insertBatch(toBeInsertContentGalleryPos);

                }
            }


            // 添加文库
            if(DictEnum.CmsContentType.library.name().equals(r.getContentType())){
                AddCmsContentLibraryFormDto libraryFormDto = dto.getLibrary();
                if(libraryFormDto != null){
                    CmsContentLibraryPo cmsContentLibraryPo = null;
                    if(StringUtils.isNotEmpty(libraryFormDto.getUrl())){
                        cmsContentLibraryPo = new CmsContentLibraryPo();
                        cmsContentLibraryPo.setContentId(r.getId());
                        cmsContentLibraryPo.setSiteId(r.getSiteId());
                        cmsContentLibraryPo.setDescription(libraryFormDto.getDescription());
                        cmsContentLibraryPo.setUrl(libraryFormDto.getUrl());
                        cmsContentLibraryPo.setDwonloadNum(0);
                        cmsContentLibraryPo.setExt(libraryFormDto.getExt());
                        cmsContentLibraryPo.setFilename(libraryFormDto.getFilename());
                        cmsContentLibraryPo.setSize(libraryFormDto.getSize());
                        cmsContentLibraryPo.setImageUrl(libraryFormDto.getImageUrl());
                        cmsContentLibraryPo.setImageDes(libraryFormDto.getImageDes());
                        cmsContentLibraryPo.setSequence(libraryFormDto.getSequence());
                        cmsContentLibraryPo = apiCmsContentLibraryPoService.preInsert(cmsContentLibraryPo,getLoginUser().getId());
                        cmsContentLibraryPo = apiCmsContentLibraryPoService.insertSimple(cmsContentLibraryPo);
                        // 如果添加成功
                        if (cmsContentLibraryPo != null && StringUtils.isNotEmpty(cmsContentLibraryPo.getId())) {
                            // 添加图片
                            List<AddCmsContentLibraryImageFormDto> libraryImageFormDtos = dto.getLibraryImages();
                            if (libraryImageFormDtos != null && !libraryImageFormDtos.isEmpty()){
                                CmsContentLibraryImagePo cmsContentLibraryImagePo = null;

                                List<CmsContentLibraryImagePo> toBeInsertLibraryImagePos = new ArrayList<>(libraryImageFormDtos.size());
                                for (AddCmsContentLibraryImageFormDto libraryImageFormDto : libraryImageFormDtos) {
                                    cmsContentLibraryImagePo = new CmsContentLibraryImagePo();
                                    cmsContentLibraryImagePo.setContentId(r.getId());
                                    cmsContentLibraryImagePo.setSiteId(r.getSiteId());
                                    cmsContentLibraryImagePo.setLibraryId(cmsContentLibraryPo.getId());
                                    cmsContentLibraryImagePo.setImageUrl(libraryImageFormDto.getImageUrl());
                                    cmsContentLibraryImagePo.setImageDes(libraryImageFormDto.getImageDes());
                                    cmsContentLibraryImagePo.setImageThumbnailUrl(libraryImageFormDto.getImageThumbnailUrl());
                                    cmsContentLibraryImagePo.setSequence(libraryImageFormDto.getSequence());
                                    cmsContentLibraryImagePo = apiCmsContentLibraryImagePoService.preInsert(cmsContentLibraryImagePo,getLoginUserId());
                                    toBeInsertLibraryImagePos.add(cmsContentLibraryImagePo);
                                }
                                apiCmsContentLibraryImagePoService.insertBatch(toBeInsertLibraryImagePos);
                            }
                        }
                    }
                }
            }

            // 添加下载
            if(DictEnum.CmsContentType.download.name().equals(r.getContentType())){
                AddCmsContentDownloadFormDto downloadFormDto = dto.getDownload();
                if(downloadFormDto != null){
                    CmsContentDownloadPo cmsContentDownloadPo = null;
                    if(StringUtils.isNotEmpty(downloadFormDto.getUrl())){
                        cmsContentDownloadPo = new CmsContentDownloadPo();
                        cmsContentDownloadPo.setContentId(r.getId());
                        cmsContentDownloadPo.setSiteId(r.getSiteId());
                        cmsContentDownloadPo.setDescription(downloadFormDto.getDescription());
                        cmsContentDownloadPo.setUrl(downloadFormDto.getUrl());
                        cmsContentDownloadPo.setDwonloadNum(0);
                        cmsContentDownloadPo.setExt(downloadFormDto.getExt());
                        cmsContentDownloadPo.setFilename(downloadFormDto.getFilename());
                        cmsContentDownloadPo.setSize(downloadFormDto.getSize());
                        cmsContentDownloadPo.setImageUrl(downloadFormDto.getImageUrl());
                        cmsContentDownloadPo.setImageDes(downloadFormDto.getImageDes());
                        cmsContentDownloadPo.setSequence(downloadFormDto.getSequence());
                        cmsContentDownloadPo.setOs(downloadFormDto.getOs());
                        cmsContentDownloadPo.setOfficialName(downloadFormDto.getOfficialName());
                        cmsContentDownloadPo.setOfficialUrl(downloadFormDto.getOfficialUrl());
                        cmsContentDownloadPo.setUpdateTime(downloadFormDto.getUpdateTime());
                        cmsContentDownloadPo.setLanguage(downloadFormDto.getLanguage());

                        cmsContentDownloadPo = apiCmsContentDownloadPoService.preInsert(cmsContentDownloadPo,getLoginUser().getId());
                        apiCmsContentDownloadPoService.insertSimple(cmsContentDownloadPo);

                        // 如果添加成功
                        if (cmsContentDownloadPo != null && StringUtils.isNotEmpty(cmsContentDownloadPo.getId())) {
                            // 添加图片
                            List<AddCmsContentDownloadImageFormDto> downloadImageFormDtos = dto.getDownloadImages();
                            if (downloadImageFormDtos != null && !downloadImageFormDtos.isEmpty()){
                                CmsContentDownloadImagePo cmsContentDownloadImagePo = null;

                                List<CmsContentDownloadImagePo> toBeInsertDownloadImagePos = new ArrayList<>(downloadImageFormDtos.size());
                                for (AddCmsContentDownloadImageFormDto downloadImageFormDto : downloadImageFormDtos) {
                                    cmsContentDownloadImagePo = new CmsContentDownloadImagePo();
                                    cmsContentDownloadImagePo.setContentId(r.getId());
                                    cmsContentDownloadImagePo.setSiteId(r.getSiteId());
                                    cmsContentDownloadImagePo.setDownloadId(cmsContentDownloadPo.getId());
                                    cmsContentDownloadImagePo.setImageUrl(downloadImageFormDto.getImageUrl());
                                    cmsContentDownloadImagePo.setImageDes(downloadImageFormDto.getImageDes());
                                    cmsContentDownloadImagePo.setImageThumbnailUrl(downloadImageFormDto.getImageThumbnailUrl());
                                    cmsContentDownloadImagePo.setSequence(downloadImageFormDto.getSequence());
                                    cmsContentDownloadImagePo = apiCmsContentDownloadImagePoService.preInsert(cmsContentDownloadImagePo,getLoginUserId());
                                    toBeInsertDownloadImagePos.add(cmsContentDownloadImagePo);
                                }
                                apiCmsContentDownloadImagePoService.insertBatch(toBeInsertDownloadImagePos);
                            }
                        }
                    }
                }
            }

            // 音频
            if(DictEnum.CmsContentType.audio.name().equals(r.getContentType())){
                AddCmsContentAudioFormDto audioFormDto = dto.getAudio();
                if(audioFormDto != null){
                    CmsContentAudioPo cmsContentAudioPo = null;
                    if(StringUtils.isNotEmpty(audioFormDto.getUrl())){
                        cmsContentAudioPo = new CmsContentAudioPo();
                        cmsContentAudioPo.setContentId(r.getId());
                        cmsContentAudioPo.setSiteId(r.getSiteId());
                        cmsContentAudioPo.setDescription(audioFormDto.getDescription());
                        cmsContentAudioPo.setUrl(audioFormDto.getUrl());
                        cmsContentAudioPo.setDwonloadNum(0);
                        cmsContentAudioPo.setExt(audioFormDto.getExt());
                        cmsContentAudioPo.setFilename(audioFormDto.getFilename());
                        cmsContentAudioPo.setSize(audioFormDto.getSize());
                        cmsContentAudioPo.setImageUrl(audioFormDto.getImageUrl());
                        cmsContentAudioPo.setImageDes(audioFormDto.getImageDes());
                        cmsContentAudioPo.setSequence(audioFormDto.getSequence());
                        cmsContentAudioPo.setDuration(audioFormDto.getDuration());
                        cmsContentAudioPo.setPlayer(audioFormDto.getPlayer());
                        cmsContentAudioPo.setDirector(audioFormDto.getDirector());
                        cmsContentAudioPo.setPerformer(audioFormDto.getPerformer());
                        cmsContentAudioPo.setLanguage(audioFormDto.getLanguage());
                        cmsContentAudioPo.setAlbum(audioFormDto.getAlbum());
                        cmsContentAudioPo.setLrc(audioFormDto.getLrc());
                        cmsContentAudioPo.setRegion(audioFormDto.getRegion());
                        cmsContentAudioPo.setYears(audioFormDto.getYears());
                        cmsContentAudioPo = apiCmsContentAudioPoService.preInsert(cmsContentAudioPo,getLoginUser().getId());
                        apiCmsContentAudioPoService.insertSimple(cmsContentAudioPo);
                    }
                }
            }
            // 视频
            if(DictEnum.CmsContentType.video.name().equals(r.getContentType())){
                AddCmsContentVideoFormDto videoFormDto = dto.getVideo();
                if(videoFormDto != null){
                    CmsContentVideoPo cmsContentVideoPo = null;
                        cmsContentVideoPo = new CmsContentVideoPo();
                        cmsContentVideoPo.setContentId(r.getId());
                        cmsContentVideoPo.setSiteId(r.getSiteId());
                        cmsContentVideoPo.setDescription(videoFormDto.getDescription());
                        cmsContentVideoPo.setUrl(videoFormDto.getUrl());
                        cmsContentVideoPo.setDwonloadNum(0);
                        cmsContentVideoPo.setExt(videoFormDto.getExt());
                        cmsContentVideoPo.setFilename(videoFormDto.getFilename());
                        cmsContentVideoPo.setSize(videoFormDto.getSize());
                        cmsContentVideoPo.setImageUrl(videoFormDto.getImageUrl());
                        cmsContentVideoPo.setImageDes(videoFormDto.getImageDes());
                        cmsContentVideoPo.setSequence(videoFormDto.getSequence());
                        cmsContentVideoPo.setDuration(videoFormDto.getDuration());
                        cmsContentVideoPo.setPlayer(videoFormDto.getPlayer());
                        cmsContentVideoPo.setDirector(videoFormDto.getDirector());
                        cmsContentVideoPo.setPerformer(videoFormDto.getPerformer());
                        cmsContentVideoPo.setLanguage(videoFormDto.getLanguage());
                        cmsContentVideoPo.setRegion(videoFormDto.getRegion());
                        cmsContentVideoPo.setSeason(videoFormDto.getSeason());
                        cmsContentVideoPo.setSeasonCount(videoFormDto.getSeasonCount());
                        cmsContentVideoPo.setSpisode(videoFormDto.getSpisode());
                        cmsContentVideoPo.setSpisodeCount(videoFormDto.getSpisodeCount());
                        cmsContentVideoPo.setYears(videoFormDto.getYears());
                        cmsContentVideoPo = apiCmsContentVideoPoService.preInsert(cmsContentVideoPo,getLoginUser().getId());
                        cmsContentVideoPo = apiCmsContentVideoPoService.insertSimple(cmsContentVideoPo);

                        // 三方播放
                        List<AddCmsContentVideoOtherPlayerFormDto> videoOtherPlayerFormDtos = dto.getVideoOtherPlayer();
                        if (videoOtherPlayerFormDtos != null && !videoOtherPlayerFormDtos.isEmpty()) {
                            List<CmsContentVideoOtherPlayerPo> toBeInsertVideoOtherPlayerPos = new ArrayList<>(videoOtherPlayerFormDtos.size());
                            CmsContentVideoOtherPlayerPo toBeInsertVideoOtherPlayerPo = null;
                            for (AddCmsContentVideoOtherPlayerFormDto videoOtherPlayerFormDto : videoOtherPlayerFormDtos) {
                                if (StringUtils.isEmpty(videoOtherPlayerFormDto.getUrl())) {
                                    continue;
                                }
                                toBeInsertVideoOtherPlayerPo = new CmsContentVideoOtherPlayerPo();
                                toBeInsertVideoOtherPlayerPo.setVideoId(cmsContentVideoPo.getId());
                                toBeInsertVideoOtherPlayerPo.setContentId(r.getId());
                                toBeInsertVideoOtherPlayerPo.setSiteId(r.getSiteId());
                                toBeInsertVideoOtherPlayerPo.setUrl(videoOtherPlayerFormDto.getUrl());
                                toBeInsertVideoOtherPlayerPo.setPlayer(videoOtherPlayerFormDto.getPlayer());
                                toBeInsertVideoOtherPlayerPo = apiCmsContentVideoOtherPlayerPoService.preInsert(toBeInsertVideoOtherPlayerPo,getLoginUserId());
                                toBeInsertVideoOtherPlayerPos.add(toBeInsertVideoOtherPlayerPo);
                            }
                            apiCmsContentVideoOtherPlayerPoService.insertBatch(toBeInsertVideoOtherPlayerPos);
                        }

                }
            }
            resultData.setData(r);
            logger.info("添加内容id:{}",r.getId());
            logger.info("添加内容结束，成功");
            return new ResponseEntity(resultData, HttpStatus.CREATED);
        }
    }
    /**
     * 单资源，删除
     * @param id
     * @return
     */
    @RepeatFormValidator
    @RequiresPermissions("content:delete")
    @RequestMapping(value = "/content/{id}",method = RequestMethod.DELETE)
    public ResponseEntity delete(@PathVariable String id){
        logger.info("删除内容开始");
        logger.info("用户id:{}",getLoginUser().getId());
        logger.info("内容id:{}",id);
        ResponseJsonRender resultData=new ResponseJsonRender();

            int r = apiCmsContentPoService.deleteFlagByPrimaryKeyWithUpdateUser(id,getLoginUser().getId());
            if (r <= 0) {
                // 删除失败，可能没有找到资源
                resultData.setCode(ResponseCode.E404_100001.getCode());
                resultData.setMsg(ResponseCode.E404_100001.getMsg());
                logger.info("code:{},msg:{}",resultData.getCode(),resultData.getMsg());
                logger.info("删除内容结束，失败");
                return new ResponseEntity(resultData,HttpStatus.NOT_FOUND);
            }else{
                // 删除成功
                logger.info("删除的内容id:{}",id);
                logger.info("删除内容结束，成功");
                return new ResponseEntity(resultData,HttpStatus.NO_CONTENT);
            }
    }

    /**
     * 单资源，更新
     * @param id
     * @param dto
     * @return
     */
    @RepeatFormValidator
    @RequiresPermissions("content:update")
    @RequestMapping(value = "/content/{id}",method = RequestMethod.PUT)
    public ResponseEntity update(@PathVariable String id, UpdateCmsContentFormDto dto){
        logger.info("更新内容开始");
        logger.info("当前登录用户id:{}",getLoginUser().getId());
        logger.info("内容id:{}",id);
        ResponseJsonRender resultData=new ResponseJsonRender();
        // 表单值设置
        CmsContentPo basePo = new CmsContentPo();
        // id
        basePo.setId(id);
        basePo.setTitle(dto.getTitle());
        basePo.setAuthor(dto.getAuthor());
        basePo.setStatus(dto.getStatus());
        basePo.setSiteId(dto.getSiteId());
        basePo.setChannelId(dto.getChannelId());
        basePo.setContentType(dto.getContentType());
        basePo.setContent(dto.getContent());
        basePo.setTemplate(dto.getTemplate());
        basePo.setPublishAt(dto.getPublishAt());
        basePo.setProfile(dto.getProfile());
        basePo.setOriginal(dto.getOriginal());
        basePo.setImageUrl(dto.getImageUrl());
        basePo.setImageDes(dto.getImageDes());

        // 用条件更新，乐观锁机制
        CmsContentPo basePoCondition = new CmsContentPo();
        basePoCondition.setId(id);
        basePoCondition.setDelFlag(BasePo.YesNo.N.name());
        basePoCondition.setUpdateAt(dto.getUpdateTime());
        basePo = apiCmsContentPoService.preUpdate(basePo,getLoginUser().getId());
        int r = apiCmsContentPoService.updateSelective(basePo,basePoCondition);
        if (r <= 0) {
            // 更新失败，资源不存在
            resultData.setCode(ResponseCode.E404_100001.getCode());
            resultData.setMsg(ResponseCode.E404_100001.getMsg());
            logger.info("code:{},msg:{}",resultData.getCode(),resultData.getMsg());
            logger.info("更新内容结束，失败");
            return new ResponseEntity(resultData,HttpStatus.NOT_FOUND);
        }else{
            // 更新成功，已被成功创建
            // 更新附件信息
            if(DictEnum.CmsContentType.download.name().equals(basePo.getContentType())){
                List<UpdateCmsContentAttachmentFormDto> attachmentFormDtos = dto.getAttachments();
                if(attachmentFormDtos != null && !attachmentFormDtos.isEmpty()){
                    List<CmsContentAttachmentPo> toBeSaveContentAttachmentPos = new ArrayList<>();
                    CmsContentAttachmentPo cmsContentAttachmentPo = null;
                    for (UpdateCmsContentAttachmentFormDto attachmentFormDto : attachmentFormDtos) {
                        if(StringUtils.isNotEmpty(attachmentFormDto.getUrl())){
                            cmsContentAttachmentPo = new CmsContentAttachmentPo();
                            cmsContentAttachmentPo.setContentId(basePo.getId());
                            cmsContentAttachmentPo.setSiteId(basePo.getSiteId());
                            cmsContentAttachmentPo.setDescription(attachmentFormDto.getDescription());
                            cmsContentAttachmentPo.setUrl(attachmentFormDto.getUrl());
                            cmsContentAttachmentPo.setExt(attachmentFormDto.getExt());
                            cmsContentAttachmentPo.setFilename(attachmentFormDto.getFilename());
                            cmsContentAttachmentPo.setSize(attachmentFormDto.getSize());
                            cmsContentAttachmentPo.setImageUrl(attachmentFormDto.getImageUrl());
                            cmsContentAttachmentPo.setType(attachmentFormDto.getType());
                            cmsContentAttachmentPo.setImageDes(attachmentFormDto.getImageDes());
                            cmsContentAttachmentPo.setSequence(attachmentFormDto.getSequence());
                            // 存在id更新，不存在id添加
                            if (StringUtils.isNotEmpty(attachmentFormDto.getId())){
                                // 需要更新的
                                cmsContentAttachmentPo.setId(attachmentFormDto.getId());
                                cmsContentAttachmentPo = apiCmsContentAttachmentPoService.preUpdate(cmsContentAttachmentPo,getLoginUser().getId());
                                toBeSaveContentAttachmentPos.add(cmsContentAttachmentPo);
                            }else{
                                // 需要添加的
                                cmsContentAttachmentPo.setDwonloadNum(0);
                                cmsContentAttachmentPo = apiCmsContentAttachmentPoService.preInsert(cmsContentAttachmentPo,getLoginUser().getId());
                                toBeSaveContentAttachmentPos.add(cmsContentAttachmentPo);
                            }
                        }
                    }
                    List<CmsContentAttachmentPo> contentAttachmentPosInDb = apiCmsContentAttachmentPoService.selectBySiteIdAndContentId(basePo.getSiteId(),basePo.getId());

                    apiCmsContentAttachmentPoService.batchSave(toBeSaveContentAttachmentPos,contentAttachmentPosInDb,getLoginUserId());
                }
            }


            // 修改关联分类
            List<String> categoryIds = dto.getCategoryIds();
            List<CmsContentCategoryRelPo>  categoryRelPosIndb = apiCmsContentCategoryRelPoService.selectBySiteIdAndContentId(basePo.getSiteId(),basePo.getId());
            boolean categoryRelPosExistIndb = categoryRelPosIndb != null && !categoryRelPosIndb.isEmpty();
            if (categoryIds != null &&  !categoryIds.isEmpty()) {
                List<CmsContentCategoryRelPo> toBeSaveCmsContentCategoryRelPos = new ArrayList<>();
                CmsContentCategoryRelPo cmsContentCategoryRelPo = null;
                for (String categoryId : categoryIds) {
                    cmsContentCategoryRelPo = new CmsContentCategoryRelPo();
                    cmsContentCategoryRelPo.setSiteId(basePo.getSiteId());
                    cmsContentCategoryRelPo.setContentId(basePo.getId());
                    cmsContentCategoryRelPo.setContentCategoryId(categoryId);
                    if (categoryRelPosExistIndb) {
                        for (CmsContentCategoryRelPo contentCategoryRelPo : categoryRelPosIndb) {
                            if(categoryId.equals(contentCategoryRelPo.getContentCategoryId())){
                                cmsContentCategoryRelPo.setId(contentCategoryRelPo.getId());
                                break;
                            }
                        }
                    }
                    if(StringUtils.isEmpty(cmsContentCategoryRelPo.getId())){
                        cmsContentCategoryRelPo = apiCmsContentCategoryRelPoService.preInsert(cmsContentCategoryRelPo,getLoginUserId());

                    }else{
                        cmsContentCategoryRelPo = apiCmsContentCategoryRelPoService.preUpdate(cmsContentCategoryRelPo,getLoginUserId());
                    }
                    toBeSaveCmsContentCategoryRelPos.add(cmsContentCategoryRelPo);
                }
                apiCmsContentCategoryRelPoService.batchSave(toBeSaveCmsContentCategoryRelPos,categoryRelPosIndb,getLoginUserId());
            }


            if(DictEnum.CmsContentType.gallery.name().equals(basePo.getContentType())){
                // 修改图库
                List<UpdateCmsContentGalleryFormDto> galleryFormDtos = dto.getGallerys();
                if(galleryFormDtos != null && !galleryFormDtos.isEmpty()){
                    List<CmsContentGalleryPo> toBeSaveContentGalleryPos = new ArrayList<>();
                    CmsContentGalleryPo cmsContentGalleryPo = null;
                    for (UpdateCmsContentGalleryFormDto galleryFormDto : galleryFormDtos) {
                        if(StringUtils.isNotEmpty(galleryFormDto.getImageUrl())){
                            cmsContentGalleryPo = new CmsContentGalleryPo();
                            cmsContentGalleryPo.setContentId(basePo.getId());
                            cmsContentGalleryPo.setSiteId(basePo.getSiteId());
                            cmsContentGalleryPo.setImageUrl(galleryFormDto.getImageUrl());
                            cmsContentGalleryPo.setImageThumbnailUrl(galleryFormDto.getImageThumbnailUrl());
                            cmsContentGalleryPo.setImageDes(galleryFormDto.getImageDes());
                            cmsContentGalleryPo.setSequence(galleryFormDto.getSequence());
                            // 存在id更新，不存在id添加
                            if (StringUtils.isNotEmpty(galleryFormDto.getId())){
                                // 需要更新的
                                cmsContentGalleryPo.setId(galleryFormDto.getId());
                                cmsContentGalleryPo = apiCmsContentGalleryPoService.preUpdate(cmsContentGalleryPo,getLoginUser().getId());
                                toBeSaveContentGalleryPos.add(cmsContentGalleryPo);
                            }else{
                                // 需要添加的
                                cmsContentGalleryPo = apiCmsContentGalleryPoService.preInsert(cmsContentGalleryPo,getLoginUser().getId());
                                toBeSaveContentGalleryPos.add(cmsContentGalleryPo);
                            }
                        }
                    }
                    List<CmsContentGalleryPo> contentGalleryPosInDb = apiCmsContentGalleryPoService.selectBySiteIdAndContentId(basePo.getSiteId(),basePo.getId());

                    apiCmsContentGalleryPoService.batchSave(toBeSaveContentGalleryPos,contentGalleryPosInDb,getLoginUserId());
                }

            }
            if(DictEnum.CmsContentType.library.name().equals(basePo.getContentType())){
                // 更新文库
                UpdateCmsContentLibraryFormDto libraryFormDto = dto.getLibrary();
                // 如果参数中有url与db中的url相同则默认表示没有更新数据，不再更新
                if(libraryFormDto != null && StringUtils.isNotEmpty(libraryFormDto.getUrl())){
                    CmsContentLibraryPo cmsContentLibraryPo = new CmsContentLibraryPo();
                    cmsContentLibraryPo.setId(libraryFormDto.getId());
                    cmsContentLibraryPo.setContentId(basePo.getId());
                    cmsContentLibraryPo.setSiteId(basePo.getSiteId());
                    cmsContentLibraryPo.setDescription(libraryFormDto.getDescription());
                    cmsContentLibraryPo.setUrl(libraryFormDto.getUrl());
                    cmsContentLibraryPo.setDwonloadNum(0);
                    cmsContentLibraryPo.setExt(libraryFormDto.getExt());
                    cmsContentLibraryPo.setFilename(libraryFormDto.getFilename());
                    cmsContentLibraryPo.setSize(libraryFormDto.getSize());
                    cmsContentLibraryPo.setImageUrl(libraryFormDto.getImageUrl());
                    cmsContentLibraryPo.setImageDes(libraryFormDto.getImageDes());
                    cmsContentLibraryPo.setSequence(libraryFormDto.getSequence());
                    cmsContentLibraryPo = apiCmsContentLibraryPoService.preUpdate(cmsContentLibraryPo,getLoginUser().getId());
                    int libraryUpdateResult = apiCmsContentLibraryPoService.updateByPrimaryKeySelective(cmsContentLibraryPo);
                    // 如果更新成功
                    // 添加图片
                    List<UpdateCmsContentLibraryImageFormDto> libraryImageFormDtos = dto.getLibraryImages();
                    if (libraryImageFormDtos != null && !libraryImageFormDtos.isEmpty()){
                        CmsContentLibraryImagePo cmsContentLibraryImagePo = null;

                        List<CmsContentLibraryImagePo> toBeInsertLibraryImagePos = new ArrayList<>(libraryImageFormDtos.size());
                        for (UpdateCmsContentLibraryImageFormDto libraryImageFormDto : libraryImageFormDtos) {
                            cmsContentLibraryImagePo = new CmsContentLibraryImagePo();
                            cmsContentLibraryImagePo.setContentId(basePo.getId());
                            cmsContentLibraryImagePo.setSiteId(basePo.getSiteId());
                            cmsContentLibraryImagePo.setLibraryId(cmsContentLibraryPo.getId());
                            cmsContentLibraryImagePo.setImageUrl(libraryImageFormDto.getImageUrl());
                            cmsContentLibraryImagePo.setImageDes(libraryImageFormDto.getImageDes());
                            cmsContentLibraryImagePo.setImageThumbnailUrl(libraryImageFormDto.getImageThumbnailUrl());
                            cmsContentLibraryImagePo.setSequence(libraryImageFormDto.getSequence());
                            cmsContentLibraryImagePo = apiCmsContentLibraryImagePoService.preInsert(cmsContentLibraryImagePo,getLoginUserId());
                            toBeInsertLibraryImagePos.add(cmsContentLibraryImagePo);
                        }
                        // 先删除
                        apiCmsContentLibraryImagePoService.deleteBySiteIdAndContentIdAndLibraryId(basePo.getSiteId(),basePo.getId(),cmsContentLibraryPo.getId(),getLoginUserId());
                        apiCmsContentLibraryImagePoService.insertBatch(toBeInsertLibraryImagePos);
                    }

                }

            }

            if(DictEnum.CmsContentType.download.name().equals(basePo.getContentType())){
                // 修改下载
                UpdateCmsContentDownloadFormDto downloadFormDto = dto.getDownload();
                if(downloadFormDto != null && StringUtils.isNotEmpty(downloadFormDto.getUrl())){
                    CmsContentDownloadPo cmsContentDownloadPo = null;
                    cmsContentDownloadPo = new CmsContentDownloadPo();
                    cmsContentDownloadPo.setId(downloadFormDto.getId());
                    cmsContentDownloadPo.setContentId(basePo.getId());
                    cmsContentDownloadPo.setSiteId(basePo.getSiteId());
                    cmsContentDownloadPo.setDescription(downloadFormDto.getDescription());
                    cmsContentDownloadPo.setUrl(downloadFormDto.getUrl());
                    cmsContentDownloadPo.setDwonloadNum(0);
                    cmsContentDownloadPo.setExt(downloadFormDto.getExt());
                    cmsContentDownloadPo.setFilename(downloadFormDto.getFilename());
                    cmsContentDownloadPo.setSize(downloadFormDto.getSize());
                    cmsContentDownloadPo.setImageUrl(downloadFormDto.getImageUrl());
                    cmsContentDownloadPo.setImageDes(downloadFormDto.getImageDes());
                    cmsContentDownloadPo.setSequence(downloadFormDto.getSequence());
                    cmsContentDownloadPo.setOs(downloadFormDto.getOs());
                    cmsContentDownloadPo.setOfficialName(downloadFormDto.getOfficialName());
                    cmsContentDownloadPo.setOfficialUrl(downloadFormDto.getOfficialUrl());
                    cmsContentDownloadPo.setUpdateTime(downloadFormDto.getUpdateTime());
                    cmsContentDownloadPo.setLanguage(downloadFormDto.getLanguage());

                    cmsContentDownloadPo = apiCmsContentDownloadPoService.preUpdate(cmsContentDownloadPo,getLoginUser().getId());
                    apiCmsContentDownloadPoService.updateByPrimaryKeySelective(cmsContentDownloadPo);
                    // 添加图片
                    List<UpdateCmsContentDownloadImageFormDto> downloadImageFormDtos = dto.getDownloadImages();
                    if (downloadImageFormDtos != null && !downloadImageFormDtos.isEmpty()){
                        CmsContentDownloadImagePo cmsContentDownloadImagePo = null;

                        List<CmsContentDownloadImagePo> toBeInsertDownloadImagePos = new ArrayList<>(downloadImageFormDtos.size());
                        for (UpdateCmsContentDownloadImageFormDto downloadImageFormDto : downloadImageFormDtos) {
                            cmsContentDownloadImagePo = new CmsContentDownloadImagePo();
                            cmsContentDownloadImagePo.setId(downloadImageFormDto.getId());
                            cmsContentDownloadImagePo.setContentId(basePo.getId());
                            cmsContentDownloadImagePo.setSiteId(basePo.getSiteId());
                            cmsContentDownloadImagePo.setDownloadId(cmsContentDownloadPo.getId());
                            cmsContentDownloadImagePo.setImageUrl(downloadImageFormDto.getImageUrl());
                            cmsContentDownloadImagePo.setImageDes(downloadImageFormDto.getImageDes());
                            cmsContentDownloadImagePo.setImageThumbnailUrl(downloadImageFormDto.getImageThumbnailUrl());
                            cmsContentDownloadImagePo.setSequence(downloadImageFormDto.getSequence());
                            cmsContentDownloadImagePo = apiCmsContentDownloadImagePoService.preSave(cmsContentDownloadImagePo,getLoginUserId());
                            toBeInsertDownloadImagePos.add(cmsContentDownloadImagePo);
                        }
                        CmsContentDownloadImagePo cmsContentDownloadImagePoDbCondition = new CmsContentDownloadImagePo();
                        cmsContentDownloadImagePoDbCondition.setSiteId(basePo.getSiteId());
                        cmsContentDownloadImagePoDbCondition.setContentId(basePo.getId());
                        cmsContentDownloadImagePoDbCondition.setDownloadId(cmsContentDownloadPo.getId());
                        cmsContentDownloadImagePoDbCondition.setDelFlag(BasePo.YesNo.N.name());
                        apiCmsContentDownloadImagePoService.batchSave(toBeInsertDownloadImagePos,cmsContentDownloadImagePoDbCondition,getLoginUserId());
                    }
                }
            }
            // 音频
            if(DictEnum.CmsContentType.audio.name().equals(basePo.getContentType())){
                UpdateCmsContentAudioFormDto audioFormDto = dto.getAudio();
                if(audioFormDto != null && StringUtils.isNotEmpty(audioFormDto.getUrl())){
                    CmsContentAudioPo cmsContentAudioPo = null;
                    cmsContentAudioPo = new CmsContentAudioPo();
                    cmsContentAudioPo.setId(audioFormDto.getId());
                    cmsContentAudioPo.setContentId(basePo.getId());
                    cmsContentAudioPo.setSiteId(basePo.getSiteId());
                    cmsContentAudioPo.setDescription(audioFormDto.getDescription());
                    cmsContentAudioPo.setUrl(audioFormDto.getUrl());
                    cmsContentAudioPo.setDwonloadNum(0);
                    cmsContentAudioPo.setExt(audioFormDto.getExt());
                    cmsContentAudioPo.setFilename(audioFormDto.getFilename());
                    cmsContentAudioPo.setSize(audioFormDto.getSize());
                    cmsContentAudioPo.setImageUrl(audioFormDto.getImageUrl());
                    cmsContentAudioPo.setImageDes(audioFormDto.getImageDes());
                    cmsContentAudioPo.setSequence(audioFormDto.getSequence());

                    cmsContentAudioPo.setDuration(audioFormDto.getDuration());
                    cmsContentAudioPo.setPlayer(audioFormDto.getPlayer());
                    cmsContentAudioPo.setDirector(audioFormDto.getDirector());
                    cmsContentAudioPo.setPerformer(audioFormDto.getPerformer());
                    cmsContentAudioPo.setLanguage(audioFormDto.getLanguage());
                    cmsContentAudioPo.setAlbum(audioFormDto.getAlbum());
                    cmsContentAudioPo.setLrc(audioFormDto.getLrc());
                    cmsContentAudioPo.setRegion(audioFormDto.getRegion());
                    cmsContentAudioPo.setYears(audioFormDto.getYears());
                    cmsContentAudioPo = apiCmsContentAudioPoService.preUpdate(cmsContentAudioPo,getLoginUser().getId());
                    apiCmsContentAudioPoService.updateByPrimaryKeySelective(cmsContentAudioPo);
                }
            }
            // 视频
            if(DictEnum.CmsContentType.video.name().equals(basePo.getContentType())){
                UpdateCmsContentVideoFormDto videoFormDto = dto.getVideo();
                if(videoFormDto != null){
                    CmsContentVideoPo cmsContentVideoPo = null;
                    cmsContentVideoPo = new CmsContentVideoPo();
                    cmsContentVideoPo.setId(videoFormDto.getId());
                    cmsContentVideoPo.setContentId(basePo.getId());
                    cmsContentVideoPo.setSiteId(basePo.getSiteId());
                    cmsContentVideoPo.setDescription(videoFormDto.getDescription());
                    cmsContentVideoPo.setUrl(videoFormDto.getUrl());
                    //cmsContentVideoPo.setDwonloadNum(0);
                    cmsContentVideoPo.setExt(videoFormDto.getExt());
                    cmsContentVideoPo.setFilename(videoFormDto.getFilename());
                    cmsContentVideoPo.setSize(videoFormDto.getSize());
                    cmsContentVideoPo.setImageUrl(videoFormDto.getImageUrl());
                    cmsContentVideoPo.setImageDes(videoFormDto.getImageDes());
                    cmsContentVideoPo.setSequence(videoFormDto.getSequence());
                    cmsContentVideoPo.setDuration(videoFormDto.getDuration());
                    cmsContentVideoPo.setPlayer(videoFormDto.getPlayer());
                    cmsContentVideoPo.setDirector(videoFormDto.getDirector());
                    cmsContentVideoPo.setPerformer(videoFormDto.getPerformer());
                    cmsContentVideoPo.setLanguage(videoFormDto.getLanguage());
                    cmsContentVideoPo.setRegion(videoFormDto.getRegion());
                    cmsContentVideoPo.setSeason(videoFormDto.getSeason());
                    cmsContentVideoPo.setSeasonCount(videoFormDto.getSeasonCount());
                    cmsContentVideoPo.setSpisode(videoFormDto.getSpisode());
                    cmsContentVideoPo.setSpisodeCount(videoFormDto.getSpisodeCount());
                    cmsContentVideoPo.setYears(videoFormDto.getYears());
                    cmsContentVideoPo = apiCmsContentVideoPoService.preUpdate(cmsContentVideoPo,getLoginUser().getId());
                    apiCmsContentVideoPoService.updateByPrimaryKeySelective(cmsContentVideoPo);

                    // 三方播放
                    List<UpdateCmsContentVideoOtherPlayerFormDto> videoOtherPlayerFormDtos = dto.getVideoOtherPlayer();
                    if (videoOtherPlayerFormDtos != null && !videoOtherPlayerFormDtos.isEmpty()) {
                        List<CmsContentVideoOtherPlayerPo> toBeUpdateVideoOtherPlayerPos = new ArrayList<>(videoOtherPlayerFormDtos.size());
                        CmsContentVideoOtherPlayerPo toBeInsertVideoOtherPlayerPo = null;
                        for (UpdateCmsContentVideoOtherPlayerFormDto videoOtherPlayerFormDto : videoOtherPlayerFormDtos) {
                            if (StringUtils.isEmpty(videoOtherPlayerFormDto.getUrl())) {
                                continue;
                            }
                            toBeInsertVideoOtherPlayerPo = new CmsContentVideoOtherPlayerPo();
                            toBeInsertVideoOtherPlayerPo.setVideoId(cmsContentVideoPo.getId());
                            toBeInsertVideoOtherPlayerPo.setContentId(basePo.getId());
                            toBeInsertVideoOtherPlayerPo.setSiteId(basePo.getSiteId());
                            toBeInsertVideoOtherPlayerPo.setUrl(videoOtherPlayerFormDto.getUrl());
                            toBeInsertVideoOtherPlayerPo.setPlayer(videoOtherPlayerFormDto.getPlayer());
                            toBeInsertVideoOtherPlayerPo = apiCmsContentVideoOtherPlayerPoService.preSave(toBeInsertVideoOtherPlayerPo,getLoginUserId());

                            toBeUpdateVideoOtherPlayerPos.add(toBeInsertVideoOtherPlayerPo);
                        }
                        CmsContentVideoOtherPlayerPo videoOtherPlayerPoDbCondition = new CmsContentVideoOtherPlayerPo();
                        videoOtherPlayerPoDbCondition.setVideoId(cmsContentVideoPo.getId());
                        videoOtherPlayerPoDbCondition.setContentId(basePo.getId());
                        videoOtherPlayerPoDbCondition.setSiteId(basePo.getSiteId());
                        videoOtherPlayerPoDbCondition.setDelFlag(BasePo.YesNo.N.name());
                        apiCmsContentVideoOtherPlayerPoService.batchSave(toBeUpdateVideoOtherPlayerPos,videoOtherPlayerPoDbCondition,getLoginUserId());
                    }
                }
            }
            logger.info("更新的内容id:{}",id);
            logger.info("更新内容结束，成功");
            return new ResponseEntity(resultData, HttpStatus.CREATED);
        }
    }

    /**
     * 单资源，获取id内容
     * @param id
     * @return
     */
    @RepeatFormValidator
    @RequiresPermissions("content:getById")
    @RequestMapping(value = "/content/{id}",method = RequestMethod.GET)
    public ResponseEntity getById(@PathVariable String id){

        ResponseJsonRender resultData=new ResponseJsonRender();
        CmsContentDto cmsContentDto = apiCmsContentPoService.selectByPrimaryKey(id,false);
        if(cmsContentDto != null){
            resultData.setData(cmsContentDto);
            // 已添加的附件
            if(DictEnum.CmsContentType.article.name().equals(cmsContentDto.getContentType())){
                List<CmsContentAttachmentPo> attachmentPos = apiCmsContentAttachmentPoService.selectBySiteIdAndContentId(cmsContentDto.getSiteId(),cmsContentDto.getId());
                resultData.addData("attachments",apiCmsContentAttachmentPoService.wrapDtos(attachmentPos));
            }

            // 已关联的分类
            List<CmsContentCategoryPo>  categoryPos = apiCmsContentCategoryPoService.selectCategoryBySiteIdAndContentId(cmsContentDto.getSiteId(),cmsContentDto.getId());
            resultData.addData("categoryIds",apiCmsContentCategoryPoService.toPrimaryKeysSimple(categoryPos));
            //图库
            if(DictEnum.CmsContentType.gallery.name().equals(cmsContentDto.getContentType())){
                List<CmsContentGalleryPo>  galleryPos = apiCmsContentGalleryPoService.selectBySiteIdAndContentId(cmsContentDto.getSiteId(),cmsContentDto.getId());
                resultData.addData("gallerys",apiCmsContentGalleryPoService.wrapDtos(galleryPos));
            }

            // 文库
            if(DictEnum.CmsContentType.library.name().equals(cmsContentDto.getContentType())){
                CmsContentLibraryPo libraryPo = apiCmsContentLibraryPoService.selectBySiteIdAndContentId(cmsContentDto.getSiteId(),cmsContentDto.getId());
                resultData.addData("library",apiCmsContentLibraryPoService.wrapDto(libraryPo));
                if (libraryPo != null) {
                    List<CmsContentLibraryImagePo> libraryImagePos = apiCmsContentLibraryImagePoService.selectBySiteIdAndContentIdAndLibraryId(cmsContentDto.getSiteId(),cmsContentDto.getId(),libraryPo.getId());
                    resultData.addData("libraryImages",apiCmsContentLibraryImagePoService.wrapDtos(libraryImagePos));
                }
            }

            // 下载
            if(DictEnum.CmsContentType.download.name().equals(cmsContentDto.getContentType())){
                CmsContentDownloadPo downloadPo = apiCmsContentDownloadPoService.selectBySiteIdAndContentId(cmsContentDto.getSiteId(),cmsContentDto.getId());
                resultData.addData("download",apiCmsContentDownloadPoService.wrapDto(downloadPo));
                if (downloadPo != null) {
                    List<CmsContentDownloadImagePo> downloadImagePos = apiCmsContentDownloadImagePoService.selectBySiteIdAndContentIdAndDownloadId(cmsContentDto.getSiteId(),cmsContentDto.getId(),downloadPo.getId());
                    resultData.addData("downloadImages",apiCmsContentDownloadImagePoService.wrapDtos(downloadImagePos));
                }
            }
            // 音频
            if(DictEnum.CmsContentType.audio.name().equals(cmsContentDto.getContentType())){
                CmsContentAudioPo audioPo = apiCmsContentAudioPoService.selectBySiteIdAndContentId(cmsContentDto.getSiteId(),cmsContentDto.getId());
                resultData.addData("audio",apiCmsContentAudioPoService.wrapDto(audioPo));
            }
            // 视频
            if(DictEnum.CmsContentType.video.name().equals(cmsContentDto.getContentType())){
                CmsContentVideoPo videoPo = apiCmsContentVideoPoService.selectBySiteIdAndContentId(cmsContentDto.getSiteId(),cmsContentDto.getId());
                resultData.addData("video",apiCmsContentVideoPoService.wrapDto(videoPo));

                List<CmsContentVideoOtherPlayerPo> videoOtherPlayerPos = apiCmsContentVideoOtherPlayerPoService.selectBySiteIdAndContentIdAndVideoId(cmsContentDto.getSiteId(),cmsContentDto.getId(),videoPo.getId());
                if(videoOtherPlayerPos != null && !videoOtherPlayerPos.isEmpty())
                resultData.addData("videoOtherPlayer",apiCmsContentVideoOtherPlayerPoService.wrapDtos(videoOtherPlayerPos));
            }

            return new ResponseEntity(resultData, HttpStatus.OK);
        }else{
            resultData.setCode(ResponseCode.E404_100001.getCode());
            resultData.setMsg(ResponseCode.E404_100001.getMsg());
            return new ResponseEntity(resultData,HttpStatus.NOT_FOUND);
        }
    }

    /**
     * 复数资源，搜索内容
     * @param dto
     * @return
     */
    @RepeatFormValidator
    @RequiresPermissions("content:search")
    @RequestMapping(value = "/contents",method = RequestMethod.GET)
    public ResponseEntity search(SearchCmsContentsConditionDto dto,boolean includeSite,boolean includeChannel){

        ResponseJsonRender resultData=new ResponseJsonRender();
        PageAndOrderbyParamDto pageAndOrderbyParamDto = new PageAndOrderbyParamDto(PageUtils.getPageFromThreadLocal(), OrderbyUtils.getOrderbyFromThreadLocal());
        // 设置当前登录用户id
        dto.setCurrentUserId(getLoginUser().getId());
        dto.setCurrentRoleId(((BaseRoleDto) getLoginUser().getRole()).getId());
        PageResultDto<CmsContentDto> list = apiCmsContentPoService.searchCmsContentsDsf(dto,pageAndOrderbyParamDto);

        if(CollectionUtils.isNotEmpty(list.getData())){

            if (includeChannel || includeSite) {
                Map<String,CmsChannelDto> channelDtoMap = new HashMap<>();
                CmsChannelDto channelDto = null;
                Map<String,CmsSiteDto> siteDtoMap = new HashMap<>();
                CmsSiteDto siteDto = null;
                for (CmsContentDto cmsContentDto : list.getData()) {
                    if(includeChannel && StringUtils.isNotEmpty(cmsContentDto.getChannelId())){
                        channelDto = apiCmsChannelPoService.selectByPrimaryKey(cmsContentDto.getChannelId());
                        if (channelDto != null) {
                            channelDtoMap.put(cmsContentDto.getChannelId(),channelDto);
                        }
                    }
                    if(includeSite && StringUtils.isNotEmpty(cmsContentDto.getSiteId())){
                        siteDto = apiCmsSitePoService.selectByPrimaryKey(cmsContentDto.getSiteId());
                        if (siteDto != null) {
                            siteDtoMap.put(cmsContentDto.getSiteId(),siteDto);
                        }
                    }
                }

                if (!channelDtoMap.isEmpty()) {
                    resultData.addData("channel",channelDtoMap);
                }
                if (!siteDtoMap.isEmpty()) {
                    resultData.addData("site",siteDtoMap);
                }
            }

            resultData.setData(list.getData());
            resultData.setPage(list.getPage());
            return new ResponseEntity(resultData, HttpStatus.OK);
        }else{
            resultData.setCode(ResponseCode.E404_100001.getCode());
            resultData.setMsg(ResponseCode.E404_100001.getMsg());
            return new ResponseEntity(resultData,HttpStatus.NOT_FOUND);
        }
    }

    /**
     * 栏目首页地址
     * @param id
     * @return
     */
    @RepeatFormValidator
    @RequestMapping(value = "/content/{id}/address",method = RequestMethod.GET)
    public ResponseEntity indexAddress(@PathVariable String id){

        ResponseJsonRender resultData=new ResponseJsonRender();
        CmsContentDto cmsContentDto = apiCmsContentPoService.selectByPrimaryKey(id,false);

        if(cmsContentDto != null){
            cmsContentDto.setContent(null);
            CmsChannelDto cmsChannelDto = apiCmsChannelPoService.selectByPrimaryKey(cmsContentDto.getChannelId(),false);

            CmsSiteDto cmsSiteDto = apiCmsSitePoService.selectByPrimaryKey(cmsContentDto.getSiteId(),false);
            CmsTemplateModelContextDto cmsTemplateModelContextDto = new CmsTemplateModelContextDto(true);
            CmsSiteTemplateModelDto cmsSiteTemplateModelDto = new CmsSiteTemplateModelDto(cmsSiteDto,cmsTemplateModelContextDto);
            CmsChannelTemplateModelDto cmsChannelTemplateModelDto = new CmsChannelTemplateModelDto(cmsChannelDto, cmsTemplateModelContextDto);
            CmsContentTemplateModelDto cmsContentTemplateModelDto = new CmsContentTemplateModelDto(cmsContentDto, cmsTemplateModelContextDto);

            resultData.setData(cmsSiteTemplateModelDto);
            resultData.addData("channel",cmsChannelTemplateModelDto);
            resultData.addData("contentc",cmsContentTemplateModelDto);
            return new ResponseEntity(resultData, HttpStatus.OK);
        }else{
            resultData.setCode(ResponseCode.E404_100001.getCode());
            resultData.setMsg(ResponseCode.E404_100001.getMsg());
            return new ResponseEntity(resultData,HttpStatus.NOT_FOUND);
        }
    }


    /**
     * 单资源,内容模板
     * @return
     */
    @RepeatFormValidator
    @RequestMapping(value = "/content/template",method = RequestMethod.GET)
    public ResponseEntity template(String siteId){

        ResponseJsonRender resultData=new ResponseJsonRender();
        CmsSiteDto cmsSiteDto = apiCmsSitePoService.selectByPrimaryKey(siteId,false);
        if (cmsSiteDto == null) {
            resultData.setCode(ResponseCode.E404_100001.getCode());
            resultData.setMsg(ResponseCode.E404_100001.getMsg());
            return new ResponseEntity(resultData,HttpStatus.NOT_FOUND);
        }
        String siteTemplatePath = cmsSiteDto.getTemplatePath();
        if(StringUtils.isEmpty(siteTemplatePath)){
            siteTemplatePath = CmsConstants.templatePathDefault;
        }
        List<String> templatePathStr = super.getFileNames(siteTemplatePath + CmsConstants.templateContentPath,false);
        if(templatePathStr != null && !templatePathStr.isEmpty()){
            resultData.setData(templatePathStr);
            return new ResponseEntity(resultData, HttpStatus.OK);
        }else{
            resultData.setCode(ResponseCode.E404_100001.getCode());
            resultData.setMsg(ResponseCode.E404_100001.getMsg());
            return new ResponseEntity(resultData,HttpStatus.NOT_FOUND);
        }
    }
}
