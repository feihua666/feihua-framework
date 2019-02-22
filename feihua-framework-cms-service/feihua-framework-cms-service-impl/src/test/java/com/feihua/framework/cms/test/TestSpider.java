package com.feihua.framework.cms.test;

import com.feihua.framework.cms.api.*;
import com.feihua.framework.cms.po.*;
import com.feihua.framework.constants.DictEnum;
import com.feihua.framework.spider.JsoupUtils;
import com.feihua.framework.spider.WebDriverFactory;
import com.feihua.framework.spider.impl.DefaultSpiderServiceImpl;
import com.feihua.utils.calendar.CalendarUtils;
import feihua.jdbc.api.pojo.BasePo;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by yangwei
 * Created at 2018/12/20 16:19
 */
public class TestSpider {
    private static final Logger logger = LoggerFactory.getLogger(TestSpider.class);

    static String domain = "";
    static String siteId = "";
    static String channelId = "";
    static String contentCategoryId = "";

    static String entryUrl = "";
    static String  scheme = "";


   static String cssSelector = "";

   static String cssSelectorTile = "";
   static String cssSelectorTileAttr = "";
   static String cssSelectorPublishAt = "";
   static String cssSelectorOriginal = "";
   static String cssSelectorContent = "";
   static String cssSelectorContentImg = "";
   static String cssSelectorProfile = "";

   //video 相关
    // 导演
   static String cssSelectorDirector = "";
   // 主演
   static String cssSelectorPerformer = "";
   // 国家/地区
   static String cssSelectorRegion = "";
   // 年代
   static String cssSelectorYears = "";

   //音频
   static String cssSelectorAlbum = "";
   static String cssSelectorLrc = "";
   static String cssSelectorAudioUrl = "";

   // 下载
   static String cssSelectorDownloadImages = "";
   static String cssSelectorDownloadUrl = "";
   static String cssSelectorDownloadOs = "";
   static String cssSelectorDownloadOfficialName = "";
   static String cssSelectorDownloadOfficialUrl = "";
   static String cssSelectorDownloadUpdateTime = "";
   static String cssSelectorDownloadLanguage = "";
   static String cssSelectorDownloadSize = "";

   // 文库
   static String cssSelectorLibraryImages = "";

   //图库

    static String cssSelectorGalleryImages = "";




    public static void main(String[] args) throws Exception {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext-cms-service-test.xml");
        WebDriverFactory webDriverFactory = applicationContext.getBean(WebDriverFactory.class);
        WebDriver webDriver = webDriverFactory.newWebDrive(null);
        //抓取科技内容
        //testSpide1(webDriver,applicationContext);
        //抓取汽车内容
        //testSpide2(webDriver,applicationContext);
        // 喜剧电影
        //testSpide4(webDriver,applicationContext);
        // 音乐
        //testSpide5(webDriver,applicationContext);
        // 下载
        //testSpide6(webDriver,applicationContext);
        // 文库
        //testSpide7(webDriver,applicationContext);
        // 图库
        testSpide8(webDriver,applicationContext);
        webDriver.quit();
    }
    // 图库
    public static void testSpide8(WebDriver webDriver,ApplicationContext applicationContext) throws InterruptedException {
        domain = "https://www.veer.com";
        siteId = "5aa50e0be66011e889a74439c4325934";
        channelId = "8802f650f78811e8be274439c4325934";

        contentCategoryId = "";

        entryUrl = "https://www.veer.com/photo";
        scheme = "https";


        cssSelector = "html body div#root div.content_wrapper div.main_body div.site_width section.fancy_grid div.clipper article a";

        cssSelectorTile = ".board-name";
        cssSelectorTileAttr = "value";
        cssSelectorOriginal = "";
        cssSelectorPublishAt = "";
        cssSelectorProfile = "";
        cssSelectorContent = "";
        cssSelectorContentImg = "section.board-item:nth-child(1) > div:nth-child(1) > div:nth-child(1) > a:nth-child(1) > img:nth-child(1)";
        cssSelectorGalleryImages = "html body div#root div.content_wrapper div.board div.board-content article.board-article.loaded section.board-details div.asset-grid.board-comments-closed.show-details.grid div.asset-container div section.board-item.mode-grid div.board-item-content div.board-item-image a.image img.board-asset";
        ApiCmsContentCategoryPoService apiCmsContentCategoryPoService = applicationContext.getBean(ApiCmsContentCategoryPoService.class);
        String root = fetchDynamic("https://www.veer.com/photo",10,webDriver);
        Document rootDoc = JsoupUtils.htmlStringToDoc(root);
        //添加分类
        List<Element> links = JsoupUtils.selectList(cssSelector,rootDoc);
        for (Element link : links) {
            if(!link.attr("href").contains("favorites")) continue;
            CmsContentCategoryPo second = new CmsContentCategoryPo();
            second.setChannelId(channelId);
            second.setSiteId(siteId);
            second.setName(link.attr("title"));
            second.setSequence(0);
            second = apiCmsContentCategoryPoService.preInsert(second,BasePo.DEFAULT_USER_ID);
            second = apiCmsContentCategoryPoService.insertSimple(second);

            contentCategoryId = second.getId();
            testSpide( webDriver,DictEnum.CmsContentType.gallery,0, applicationContext);


        }

    }
    // 文库
    public static void testSpide7(WebDriver webDriver,ApplicationContext applicationContext) throws InterruptedException {
        domain = "http://wenku.it168.com";
        siteId = "5aa50e0be66011e889a74439c4325934";
        channelId = "8802f650f78811e8be274439c4325934";

        contentCategoryId = "";

        entryUrl = "";
        scheme = "http";


        cssSelector = "html body div.w1200.clearfix div.l2 div.padd ul.wendang_list5 li div.n1 div.title a";

        cssSelectorTile = ".title2 > span:nth-child(3)";
        cssSelectorOriginal = "";
        cssSelectorPublishAt = ".time_box > span:nth-child(2)";
        cssSelectorProfile = "";
        cssSelectorContent = "";
        cssSelectorContentImg = "";
        cssSelectorLibraryImages = ".imgs img";
        ApiCmsContentCategoryPoService apiCmsContentCategoryPoService = applicationContext.getBean(ApiCmsContentCategoryPoService.class);
        String root = fetchDynamic("http://wenku.it168.com/list/language_0_0_0_0_0_0_0_0_1.shtml",10,webDriver);
        Document rootDoc = JsoupUtils.htmlStringToDoc(root);
        //添加分类
        List<Element> links = JsoupUtils.selectList(".cate a",rootDoc);
        for (Element link : links) {
            if("全部".equals(link.text())) continue;
            CmsContentCategoryPo second = new CmsContentCategoryPo();
            second.setChannelId(channelId);
            second.setSiteId(siteId);
            second.setName(link.text());
            second.setSequence(0);
            second = apiCmsContentCategoryPoService.preInsert(second,BasePo.DEFAULT_USER_ID);
            second = apiCmsContentCategoryPoService.insertSimple(second);

            contentCategoryId = second.getId();
            entryUrl = effectiveUrl(link.attr("href"),scheme);
            testSpide( webDriver,DictEnum.CmsContentType.library,0, applicationContext);


        }

    }
    // 下载
    public static void testSpide6(WebDriver webDriver,ApplicationContext applicationContext) throws InterruptedException {
        domain = "http://xiazai.zol.com.cn";
        siteId = "5aa50e0be66011e889a74439c4325934";
        channelId = "80ec3242f78811e8be274439c4325934";

        contentCategoryId = "";

        entryUrl = "";
        scheme = "http";


        cssSelector = "html body div#cateInfo.wrapper.clearfix div.main.list-main div.section.soft-tabs ul.soft-list li.item div.soft-header h4.soft-title a";

        cssSelectorTile = "div.soft-title > h1:nth-child(1) > label:nth-child(1)";
        cssSelectorOriginal = "";
        cssSelectorPublishAt = "";
        cssSelectorProfile = ".text-wrap-mt";
        cssSelectorContent = ".text-wrap-mt";
        cssSelectorContentImg = "span.pic:nth-child(3) > img:nth-child(1)";
        cssSelectorDownloadImages = ".screenshot-items img";
        cssSelectorDownloadUrl = ".downLoad-button";
        cssSelectorDownloadOs = ".system-text";
        cssSelectorDownloadOfficialName = "ul.soft-text:nth-child(2) > li:nth-child(7) > a:nth-child(2)";
        cssSelectorDownloadOfficialUrl = "ul.soft-text:nth-child(2) > li:nth-child(7) > a:nth-child(2)";
        cssSelectorDownloadUpdateTime = "ul.soft-text:nth-child(2) > li:nth-child(5)";
        cssSelectorDownloadLanguage = "ul.soft-text:nth-child(2) > li:nth-child(4)";
        cssSelectorDownloadSize="ul.soft-text:nth-child(2) > li:nth-child(2)";
        ApiCmsContentCategoryPoService apiCmsContentCategoryPoService = applicationContext.getBean(ApiCmsContentCategoryPoService.class);
        String root = fetchDynamic("http://xiazai.zol.com.cn/word_index.html",3,webDriver);
        Document rootDoc = JsoupUtils.htmlStringToDoc(root);
        //添加分类
        List<Element> links = JsoupUtils.selectList("#pnanel ul.links li.item",rootDoc);
        for (Element link : links) {
            //一级分类
            String subTitle = JsoupUtils.getText("a.sub-title",link);
            //添加一级分类
            CmsContentCategoryPo first = new CmsContentCategoryPo();
            first.setChannelId(channelId);
            first.setSiteId(siteId);
            first.setName(subTitle);
            first.setSequence(0);
            first = apiCmsContentCategoryPoService.preInsert(first,BasePo.DEFAULT_USER_ID);
            first = apiCmsContentCategoryPoService.insertSimple(first);


            // 二级分类
            List<Element> linksSecond = JsoupUtils.selectList("a",link);
            for (Element element : linksSecond) {
                if(element.hasClass("sub-title")) continue;
                CmsContentCategoryPo second = new CmsContentCategoryPo();
                second.setChannelId(channelId);
                second.setSiteId(siteId);
                second.setName(element.text());
                second.setParentId(first.getId());
                second.setSequence(0);
                second = apiCmsContentCategoryPoService.preInsert(second,BasePo.DEFAULT_USER_ID);
                second = apiCmsContentCategoryPoService.insertSimple(second);

                contentCategoryId = second.getId();
                entryUrl = effectiveUrl(element.attr("href"),scheme);
                testSpide( webDriver,DictEnum.CmsContentType.download,0, applicationContext);

            }

        }

    }
    // 音频
    public static void testSpide5(WebDriver webDriver,ApplicationContext applicationContext) throws InterruptedException {
        domain = "http://www.9ku.com";
        siteId = "5aa50e0be66011e889a74439c4325934";
        channelId = "26880ce3feaa11e8be274439c4325934";

        contentCategoryId = "6fa54af9081f11e9be274439c4325934";

        entryUrl = "http://www.9ku.com/laoge/500shou.htm";
        scheme = "http";


        cssSelector = "div#fall.mdBoxBd li a.songName";

        cssSelectorTile = ".playingTit > h1:nth-child(3)";
        cssSelectorOriginal = "";
        cssSelectorPublishAt = "";
        cssSelectorProfile = "";
        cssSelectorContent = "";
        cssSelectorContentImg = "";

        // 导演
        cssSelectorDirector = "";
        // 主演
        cssSelectorPerformer = ".playingTit > h2:nth-child(4) > a:nth-child(1)";
        // 国家/地区
        cssSelectorRegion = "";
        // 年代
        cssSelectorYears = "";


        cssSelectorAlbum = "";
        cssSelectorLrc = "#lrctext";
        cssSelectorAudioUrl = "#kuPlayer audio";
        testSpide( webDriver,DictEnum.CmsContentType.audio,0, applicationContext);

    }
    // 喜剧电影
    public static void testSpide4(WebDriver webDriver,ApplicationContext applicationContext) throws InterruptedException {
        domain = "http://v.hao123.baidu.com";
        siteId = "5aa50e0be66011e889a74439c4325934";
        channelId = "2d8089b5feaa11e8be274439c4325934";

        contentCategoryId = "be391407050611e9be274439c4325934";

        entryUrl = "http://v.hao123.baidu.com/v/search?channel=movie&category=%E5%96%9C%E5%89%A7";
        scheme = "http";


        cssSelector = "html body div.wrapper div.content div.result.clearfix ul.wg-c-list.clearfix li.card.s190x254 a.link";

        cssSelectorTile = "h1.title";
        cssSelectorOriginal = "";
        cssSelectorPublishAt = "";
        cssSelectorProfile = ".abstract > em:nth-child(2)";
        cssSelectorContent = ".abstract > em:nth-child(2)";
        cssSelectorContentImg = ".poster > a:nth-child(1) > img:nth-child(1)";

        // 导演
        cssSelectorDirector = "div.info > p:nth-child(2) > span:nth-child(1) > a:nth-child(2)";
        // 主演
        cssSelectorPerformer = "html body div.detail-wrapper div.detail.clearfix div.info p span a";
        // 国家/地区
        cssSelectorRegion = "div.info > p:nth-child(2) > span:nth-child(2) > a:nth-child(2)";
        // 年代
        cssSelectorYears = "div.info > p:nth-child(2) > span:nth-child(3) > a:nth-child(2)";

        testSpide( webDriver,DictEnum.CmsContentType.video,0, applicationContext);

        entryUrl = "http://v.hao123.baidu.com/v/search?channel=movie&category=%E5%96%9C%E5%89%A7&pn=2";
        testSpide( webDriver,DictEnum.CmsContentType.video,0, applicationContext);
        entryUrl = "http://v.hao123.baidu.com/v/search?channel=movie&category=%E5%96%9C%E5%89%A7&pn=3";
        testSpide( webDriver,DictEnum.CmsContentType.video,0, applicationContext);
    }

    // 娱乐新闻
    public static void testSpide3(WebDriver webDriver,ApplicationContext applicationContext) throws InterruptedException {
        domain = "https://www.toutiao.com";
        siteId = "5aa50e0be66011e889a74439c4325934";
        channelId = "2ea8f89af78711e8be274439c4325934";

        contentCategoryId = "a896ae6504c811e9be274439c4325934";

        entryUrl = "https://www.toutiao.com/ch/news_entertainment/";
        scheme = "https";


        cssSelector = "html body div.y-wrap div.y-box.container div.y-left.index-content div div.feedBox div div.wcommonFeed ul li.item div.item-inner.y-box div.normal.rbox div.rbox-inner div.title-box a.link.title";

        cssSelectorTile = ".article-title";
        cssSelectorOriginal = ".article-sub > span:nth-last-child(2)";
        cssSelectorPublishAt = ".article-sub > span:nth-last-child(1)";
        cssSelectorContent = ".article-content";
        testSpide( webDriver,DictEnum.CmsContentType.article,15, applicationContext);
    }
    // 汽车新闻
    public static void testSpide2(WebDriver webDriver,ApplicationContext applicationContext) throws InterruptedException {
        domain = "https://www.toutiao.com";
        siteId = "5aa50e0be66011e889a74439c4325934";
        channelId = "2ea8f89af78711e8be274439c4325934";

        contentCategoryId = "08c2a585043c11e9be274439c4325934";

        entryUrl = "https://www.toutiao.com/ch/news_car/";
        scheme = "https";


        cssSelector = "html body div.y-wrap div.y-box.container div.y-left.index-content div div.feedBox div div.wcommonFeed ul li.item div.item-inner.y-box div.normal.rbox div.rbox-inner div.title-box a.link.title";

        cssSelectorTile = ".article-title";
        cssSelectorOriginal = ".article-sub > span:nth-last-child(2)";
        cssSelectorPublishAt = ".article-sub > span:nth-last-child(1)";
        cssSelectorContent = ".article-content";
        testSpide( webDriver,DictEnum.CmsContentType.article,15, applicationContext);
    }
    // 科技新闻
    public static void testSpide1(WebDriver webDriver,ApplicationContext applicationContext) throws InterruptedException {
        domain = "https://www.toutiao.com";
        siteId = "5aa50e0be66011e889a74439c4325934";
        channelId = "2ea8f89af78711e8be274439c4325934";
        contentCategoryId = "9c10058c042f11e9be274439c4325934";

        entryUrl = "https://www.toutiao.com/ch/news_tech/";
         scheme = "https";


        cssSelector = "html body div.y-wrap div.y-box.container div.y-left.index-content div div.feedBox div div.wcommonFeed ul li.item div.item-inner.y-box div.normal.rbox div.rbox-inner div.title-box a.link.title";

        cssSelectorTile = ".article-title";
        cssSelectorOriginal = ".article-sub > span:nth-last-child(2)";
        cssSelectorPublishAt = ".article-sub > span:nth-last-child(1)";
        cssSelectorContent = ".article-content";

        testSpide( webDriver,DictEnum.CmsContentType.article,15, applicationContext);
    }
    // 文章内容抓取
    public static void testSpide(WebDriver webDriver,DictEnum.CmsContentType cmsContentType,int scrollCount,ApplicationContext applicationContext) throws InterruptedException {


        ApiCmsContentPoService apiCmsContentPoService = applicationContext.getBean(ApiCmsContentPoService.class);
        ApiCmsContentCategoryRelPoService apiCmsContentCategoryPoService = applicationContext.getBean(ApiCmsContentCategoryRelPoService.class);


        String docHtml = fetchDynamicScroll(entryUrl,3,scrollCount,webDriver);
        Document doc = JsoupUtils.htmlStringToDoc(docHtml);

        List<Element> contentList = JsoupUtils.selectList(cssSelector,doc);
        for (Element element : contentList) {
            try {
                String url = element.attr("href");
                url = effectiveUrl(url,scheme);
                if(StringUtils.isNotEmpty(url)){
                    String contentDoc = fetchDynamic(url,3,webDriver);
                    doc = JsoupUtils.htmlStringToDoc(contentDoc);
                    String title = JsoupUtils.getText(cssSelectorTile,doc);
                    if (title == null && StringUtils.isNotEmpty(cssSelectorTileAttr)) {
                        title = JsoupUtils.getAttr(cssSelectorTile,cssSelectorTileAttr,doc);
                    }
                    if(title == null) continue;
                    String publishAt = JsoupUtils.getText(cssSelectorPublishAt,doc);
                    String original = JsoupUtils.getText(cssSelectorOriginal,doc);
                    String content = JsoupUtils.selectInnerHtml(cssSelectorContent,doc);
                    String profile = JsoupUtils.getText(cssSelectorProfile,doc);
                    Element contentImage = JsoupUtils.selectOne(cssSelectorContentImg,doc);

                    // 添加内容
                    CmsContentPo cmsContentPo = new CmsContentPo();
                    cmsContentPo.setContentType(cmsContentType.name());
                    if(StringUtils.isEmpty(content)){
                        content = "暂无点位";
                    }
                    cmsContentPo.setContent(content);
                    cmsContentPo.setProfile(profile);
                    cmsContentPo.setOriginal(url);
                    if(StringUtils.isEmpty(publishAt)){
                        cmsContentPo.setPublishAt(new Date());

                    }else{

                        cmsContentPo.setPublishAt(CalendarUtils.stringToDate(publishAt));
                    }
                    if(StringUtils.isEmpty(original)){
                        cmsContentPo.setAuthor("未知");
                    }else {
                        cmsContentPo.setAuthor(original);
                    }
                    // 内容状态
                    cmsContentPo.setStatus("audit");
                    cmsContentPo.setTitle(title);
                    cmsContentPo.setIv(0);
                    cmsContentPo.setPv(0);
                    cmsContentPo.setUv(0);
                    cmsContentPo.setChannelId(channelId);
                    cmsContentPo.setSiteId(siteId);
                    //取第一张图做为封面图

                    if(contentImage != null && StringUtils.isNotEmpty(contentImage.attr("src"))){
                        cmsContentPo.setImageUrl(contentImage.attr("src"));
                    }


                    apiCmsContentPoService.preInsert(cmsContentPo, BasePo.DEFAULT_USER_ID);
                    cmsContentPo = apiCmsContentPoService.insertSimple(cmsContentPo);
                    // 关联分类

                    CmsContentCategoryRelPo cmsContentCategoryRelPo = new CmsContentCategoryRelPo();
                    cmsContentCategoryRelPo.setContentCategoryId(contentCategoryId);
                    cmsContentCategoryRelPo.setContentId(cmsContentPo.getId());
                    cmsContentCategoryRelPo.setSiteId(cmsContentPo.getSiteId());
                    apiCmsContentCategoryPoService.preInsert(cmsContentCategoryRelPo,BasePo.DEFAULT_USER_ID);
                    cmsContentCategoryRelPo = apiCmsContentCategoryPoService.insertSimple(cmsContentCategoryRelPo);



                    // 根据类型添加其它信息
                    //视频
                    if (DictEnum.CmsContentType.video.name().equals(cmsContentType.name())) {
                        String director = JsoupUtils.getText(cssSelectorDirector,doc);
                        String performer = JsoupUtils.getTextForList(cssSelectorPerformer," ",doc);
                        String region = JsoupUtils.getText(cssSelectorRegion,doc);
                        String years = JsoupUtils.getText(cssSelectorYears,doc);
                        CmsContentVideoPo cmsContentVideoPo = new CmsContentVideoPo();
                        cmsContentVideoPo.setContentId(cmsContentPo.getId());
                        cmsContentVideoPo.setSiteId(cmsContentPo.getSiteId());
                        cmsContentVideoPo.setDirector(director);
                        cmsContentVideoPo.setPerformer(performer);
                        cmsContentVideoPo.setRegion(region);
                        cmsContentVideoPo.setYears((years));


                        ApiCmsContentVideoPoService apiCmsContentVideoPoService = applicationContext.getBean(ApiCmsContentVideoPoService.class);
                        apiCmsContentVideoPoService.preInsert(cmsContentVideoPo,BasePo.DEFAULT_USER_ID);
                        cmsContentVideoPo = apiCmsContentVideoPoService.insertSimple(cmsContentVideoPo);
                        String css = "#linkBtn a";
                        List<Element> elements = JsoupUtils.selectList(css,doc);
                        for (Element element1 : elements) {
                            //添加三方播放
                            CmsContentVideoOtherPlayerPo cmsContentVideoOtherPlayerPo = new CmsContentVideoOtherPlayerPo();
                            cmsContentVideoOtherPlayerPo.setContentId(cmsContentPo.getId());
                            cmsContentVideoOtherPlayerPo.setSiteId(cmsContentPo.getSiteId());
                            cmsContentVideoOtherPlayerPo.setVideoId(cmsContentVideoPo.getId());
                            String _title = element1.attr("title");
                            if (StringUtils.isEmpty(_title)) {
                                _title = element1.attr("alog-text");
                            }
                            cmsContentVideoOtherPlayerPo.setPlayer((_title));
                            cmsContentVideoOtherPlayerPo.setUrl(element1.attr("href"));

                            ApiCmsContentVideoOtherPlayerPoService apiCmsContentVideoOtherPlayerPoService = applicationContext.getBean(ApiCmsContentVideoOtherPlayerPoService.class);
                            apiCmsContentVideoOtherPlayerPoService.preInsert(cmsContentVideoOtherPlayerPo,BasePo.DEFAULT_USER_ID);
                            apiCmsContentVideoOtherPlayerPoService.insert(cmsContentVideoOtherPlayerPo);
                        }
                    }
                    if (DictEnum.CmsContentType.audio.name().equals(cmsContentType.name())) {
                        String director = JsoupUtils.getText(cssSelectorDirector,doc);
                        String performer = JsoupUtils.getText(cssSelectorPerformer,doc);
                        String region = JsoupUtils.getText(cssSelectorRegion,doc);
                        String years = JsoupUtils.getText(cssSelectorYears,doc);
                        String lrc = JsoupUtils.getText(cssSelectorLrc,doc);
                        String audioUrl = JsoupUtils.getAttr(cssSelectorAudioUrl,"src",doc);

                        CmsContentAudioPo cmsContentAudioPo = new CmsContentAudioPo();
                        cmsContentAudioPo.setContentId(cmsContentPo.getId());
                        cmsContentAudioPo.setSiteId(cmsContentPo.getSiteId());
                        //cmsContentAudioPo.setDirector(director);
                        cmsContentAudioPo.setPerformer(performer);
                        //cmsContentAudioPo.setRegion(region);
                        //cmsContentAudioPo.setYears((years));
                        cmsContentAudioPo.setLrc((lrc));
                        if(StringUtils.isEmpty(audioUrl)) continue;
                        cmsContentAudioPo.setUrl(audioUrl);

                        ApiCmsContentAudioPoService apiCmsContentAudioPoService = applicationContext.getBean(ApiCmsContentAudioPoService.class);
                        apiCmsContentAudioPoService.preInsert(cmsContentAudioPo,BasePo.DEFAULT_USER_ID);
                        cmsContentAudioPo = apiCmsContentAudioPoService.insertSimple(cmsContentAudioPo);

                    }

                    //添加下载
                    if (DictEnum.CmsContentType.download.name().equals(cmsContentType.name())) {

                        String downloadUrl = JsoupUtils.getAttrHref(cssSelectorDownloadUrl,doc);
                        String downloadOs = JsoupUtils.getOwnText(cssSelectorDownloadOs,doc);
                        String officialName = JsoupUtils.getText(cssSelectorDownloadOfficialName,doc);
                        String officialUrl = JsoupUtils.getAttrHref(cssSelectorDownloadOfficialUrl,doc);
                        String updateTime = JsoupUtils.getOwnText(cssSelectorDownloadUpdateTime,doc);
                        String language = JsoupUtils.getOwnText(cssSelectorDownloadLanguage,doc);
                        String size = JsoupUtils.getOwnText(cssSelectorDownloadSize,doc);
                        CmsContentDownloadPo downloadPo = new CmsContentDownloadPo();
                        downloadPo.setSiteId(siteId);
                        downloadPo.setContentId(cmsContentPo.getId());
                        downloadPo.setDwonloadNum(0);
                        downloadPo.setUrl(downloadUrl);
                        downloadPo.setOs(downloadOs);
                        downloadPo.setOfficialName(officialName);
                        downloadPo.setOfficialUrl(officialUrl);
                        try {
                            downloadPo.setUpdateTime(CalendarUtils.stringToDate(updateTime));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        downloadPo.setLanguage(language);
                        downloadPo.setSize(size);
                        ApiCmsContentDownloadPoService apiCmsContentDownloadPoService = applicationContext.getBean(ApiCmsContentDownloadPoService.class);
                        downloadPo = apiCmsContentDownloadPoService.preInsert(downloadPo,BasePo.DEFAULT_USER_ID);
                        downloadPo = apiCmsContentDownloadPoService.insertSimple(downloadPo);
                        ApiCmsContentDownloadImagePoService apiCmsContentDownloadImagePoService = applicationContext.getBean(ApiCmsContentDownloadImagePoService.class);

                        //添加图片截图

                        List<Element> downloadImages = JsoupUtils.selectList(cssSelectorDownloadImages,doc);
                        if (downloadImages != null) {
                            for (Element downloadImage : downloadImages) {
                                CmsContentDownloadImagePo cmsContentDownloadImagePo = new CmsContentDownloadImagePo();
                                cmsContentDownloadImagePo.setSiteId(siteId);
                                cmsContentDownloadImagePo.setContentId(cmsContentPo.getId());
                                cmsContentDownloadImagePo.setDownloadId(downloadPo.getId());
                                cmsContentDownloadImagePo.setImageUrl(downloadImage.attr("loadsrc"));
                                cmsContentDownloadImagePo.setSequence(0);
                                cmsContentDownloadImagePo = apiCmsContentDownloadImagePoService.preInsert(cmsContentDownloadImagePo,BasePo.DEFAULT_USER_ID);
                                apiCmsContentDownloadImagePoService.insertSimple(cmsContentDownloadImagePo);
                            }
                        }


                    }
                    //添加文库
                    if (DictEnum.CmsContentType.library.name().equals(cmsContentType.name())) {


                        CmsContentLibraryPo libraryPo = new CmsContentLibraryPo();
                        libraryPo.setSiteId(siteId);
                        libraryPo.setContentId(cmsContentPo.getId());
                        libraryPo.setDwonloadNum(0);

                        ApiCmsContentLibraryPoService apiCmsContentLibraryPoService = applicationContext.getBean(ApiCmsContentLibraryPoService.class);
                        libraryPo = apiCmsContentLibraryPoService.preInsert(libraryPo,BasePo.DEFAULT_USER_ID);
                        libraryPo = apiCmsContentLibraryPoService.insertSimple(libraryPo);
                        ApiCmsContentLibraryImagePoService apiCmsContentLibraryImagePoService = applicationContext.getBean(ApiCmsContentLibraryImagePoService.class);

                        //添加图片

                        List<Element> libraryImages = JsoupUtils.selectList(cssSelectorLibraryImages,doc);
                        if (libraryImages != null) {
                            int sequence = 0;
                            for (Element libraryImage : libraryImages) {
                                CmsContentLibraryImagePo cmsContentLibraryImagePo = new CmsContentLibraryImagePo();
                                cmsContentLibraryImagePo.setSiteId(siteId);
                                cmsContentLibraryImagePo.setContentId(cmsContentPo.getId());
                                cmsContentLibraryImagePo.setLibraryId(libraryPo.getId());
                                cmsContentLibraryImagePo.setImageUrl(libraryImage.attr("src"));
                                cmsContentLibraryImagePo.setSequence(sequence++);
                                cmsContentLibraryImagePo = apiCmsContentLibraryImagePoService.preInsert(cmsContentLibraryImagePo,BasePo.DEFAULT_USER_ID);
                                apiCmsContentLibraryImagePoService.insertSimple(cmsContentLibraryImagePo);
                            }
                        }


                    }
                    if (DictEnum.CmsContentType.gallery.name().equals(cmsContentType.name())) {
                        ApiCmsContentGalleryPoService apiCmsContentGalleryPoService = applicationContext.getBean(ApiCmsContentGalleryPoService.class);
                        List<Element> galleryImages = JsoupUtils.selectList(cssSelectorGalleryImages,doc);
                        if (galleryImages != null) {
                            int sequence = 0;
                            for (Element galleryImage : galleryImages) {
                                CmsContentGalleryPo cmsContentGalleryImagePo = new CmsContentGalleryPo();
                                cmsContentGalleryImagePo.setSiteId(siteId);
                                cmsContentGalleryImagePo.setContentId(cmsContentPo.getId());
                                cmsContentGalleryImagePo.setImageUrl(galleryImage.attr("src"));
                                cmsContentGalleryImagePo.setSequence(sequence++);
                                cmsContentGalleryImagePo = apiCmsContentGalleryPoService.preInsert(cmsContentGalleryImagePo,BasePo.DEFAULT_USER_ID);
                                apiCmsContentGalleryPoService.insertSimple(cmsContentGalleryImagePo);
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(element.outerHtml());
            }
        }
    }

    private static String effectiveUrl(String url,String scheme){
        String r = null;
        if(StringUtils.isNotEmpty(url)){
            if(url.startsWith("//")){
                r = scheme + url;
            }else if(url.startsWith("/")){
                r = domain + url;
            }else if(url.startsWith(scheme)){
                r = url;
            }
        }
        return r;
    }
    public static String fetchDynamicScroll(String url, int wait, int scrollCount, WebDriver webDriver){
        String r = null;
        if(StringUtils.isEmpty(url) || !url.startsWith("http")) return  r;
        try {
            logger.info("fetchDynamicScroll url=" + url);
            webDriver.manage().timeouts().pageLoadTimeout(wait, TimeUnit.SECONDS);
            webDriver.get(url);
            int count = 0;
            while (true){
                ((JavascriptExecutor) webDriver).executeScript("window.scrollTo(0,window.scrollY + 5000)");

                count ++;
                if(count > scrollCount){
                    break;
                }
                Thread.sleep(3000);
            }

            WebElement myDynamicElement = webDriver.findElement(By.cssSelector("html"));
            if (myDynamicElement != null) {
                r = myDynamicElement.getAttribute("outerHTML");
            }
            logger.info("fetchDynamicScroll doc=" + r);

            logger.info("fetchDynamicScroll wait=" + (wait) + "s");

        } catch (Exception e) {
            logger.error(e.getMessage(),e);
        }

        return r;
    }
    public static String fetchDynamic(String url, int wait, WebDriver webDriver){
        String r = null;
        if(StringUtils.isEmpty(url) || !url.startsWith("http")) return  r;
        try {
            logger.info("fetchDynamic url=" + url);
            webDriver.manage().timeouts().pageLoadTimeout(wait, TimeUnit.SECONDS);
            try {
                webDriver.get(url);
            } catch (Exception e){
                ((JavascriptExecutor) webDriver).executeScript("window.stop()");

            }
            Thread.sleep(3 * 1000);

            WebElement myDynamicElement = webDriver.findElement(By.cssSelector("html"));
            if (myDynamicElement != null) {
                r = myDynamicElement.getAttribute("outerHTML");
            }
            logger.info("fetchDynamic doc=" + r);

            logger.info("fetchDynamic wait=" + (wait) + "s");

        } catch (Exception e) {
            logger.error(e.getMessage(),e);
        }

        return r;
    }
}
