package com.feihua.framework.spider.impl;

import com.feihua.framework.spider.JsoupUtils;
import com.feihua.framework.spider.WebDriverFactory;
import com.feihua.framework.spider.api.ApiSpiderConfigSourcePoService;
import com.feihua.framework.spider.api.ApiSpiderResultPoService;
import com.feihua.framework.spider.api.ApiSpiderResultUrlsPoService;
import com.feihua.framework.spider.po.SpiderConfigSourcePo;
import com.feihua.framework.spider.po.SpiderResultPo;
import com.feihua.framework.spider.po.SpiderResultUrlsPo;
import com.feihua.utils.io.FileUtils;
import feihua.jdbc.api.pojo.BasePo;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static com.feihua.framework.spider.WebDriverFactory.driverType_firefox;

/**
 * Created by yangwei
 * Created at 2018/12/17 16:03
 */

public class DefaultSpiderServiceImpl extends AbstractApiSpiderServiceImpl {

    private static final Logger logger = LoggerFactory.getLogger(DefaultSpiderServiceImpl.class);
    @Autowired
    private WebDriverFactory webDriverFactory;

    @Autowired
    private ApiSpiderConfigSourcePoService apiSpiderConfigSourceService;
    @Autowired
    private ApiSpiderResultPoService apiSpiderResultService;

    @Autowired
    private ApiSpiderResultUrlsPoService apiSpiderResultUrlsService;
    @Override
    public String fetchStatic(String url) {
        if(StringUtils.isEmpty(url) || !url.startsWith("http"))
        logger.info("fetchStatic url=" + url);
        String r = null;
        try {
            Document doc = Jsoup.connect(url).get();
            Element element = doc.selectFirst("html");
            if (element != null) {
                r = element.outerHtml();
            }

        } catch (Exception e) {
            logger.error(e.getMessage(),e);
        }
        logger.info("fetchStatic doc=" + r);

        return r;
    }

    @Override
    public String fetchDynamic(String url, int wait) {
        String r = null;
        WebDriver webDriver = null;
        try {
            webDriver = webDriverFactory.newWebDrive(driverType_firefox);

            r = fetchDynamic(url,wait,webDriver);
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return r;
        }finally {
            webDriver.quit();
        }

        return r;
    }

    public String fetchDynamic(String url, int wait, WebDriver webDriver){
        String r = null;
        if(StringUtils.isEmpty(url) || !url.startsWith("http")) return  r;
        try {
            logger.info("fetchDynamic url=" + url);
            webDriver.manage().timeouts().pageLoadTimeout(wait, TimeUnit.SECONDS);
            webDriver.get(url);
            //Thread.sleep(wait * 1000);

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
    private String _getDomain(String url){
        URI uri = null;
        String r = null;
        try {
            uri = new URI(url);
            uri = new URI(uri.getScheme(), uri.getUserInfo(), uri.getHost(), uri.getPort(), null, null, null);
            r =  uri.toString();
        } catch (Exception e) {
            logger.error("_getDomain error from url=" + url);
        }
        logger.info("spide domain=" + r);
        return r;
    }
    private String _getScheme(String url){
        URI uri = null;
        String r = null;
        try {
            uri = new URI(url);
            r =  uri.getScheme();
        } catch (Exception e) {
            logger.error("_getDomain error from url=" + url);
        }
        logger.info("spide domain=" + r);
        return r;
    }

    @Override
    public void spide(Set<String> urls) {
        if (urls != null) {
            WebDriver webDriver = null;
            try {
                webDriver = webDriverFactory.newWebDrive(driverType_firefox);
            } catch (Exception e) {
                logger.error(e.getMessage(),e);
                return;
            }
            _spide(urls,1,null,webDriver);
            webDriver.quit();
        }

    }

    private void _spide(Set<String> urls,int deep,String parentResultId,WebDriver webDriver){
        if (urls == null || urls.isEmpty()) {
            return;
        }
        if(deep >5) return;
        for (String url : urls) {
            logger.info("spide url=" + url);
            logger.info("spide deep=" + deep);
            String domain = _getDomain(url);
            boolean dynamic = true;
            int dynamicWait = 5;
            String configSourceId = null;
            if (domain != null) {
                SpiderConfigSourcePo spiderConfigSourcePo = apiSpiderConfigSourceService.selectByDomain(domain);

                if (spiderConfigSourcePo != null) {
                    logger.info("spide spiderConfigSource exist");
                    dynamic = BasePo.YesNo.N.name().equals(spiderConfigSourcePo.getIsDynamic());
                    if(spiderConfigSourcePo.getDynamicWait().intValue() > dynamicWait){
                        dynamicWait = spiderConfigSourcePo.getDynamicWait().intValue();
                    }
                    configSourceId = spiderConfigSourcePo.getId();
                }else{
                    logger.info("spide spiderConfigSource not exist");
                }
            }else {
                continue;
            }

            String doc = null;
            if (dynamic) {
                doc = fetchDynamic(url,dynamicWait,webDriver);
            }else {
                doc = fetchStatic(url);
            }
            if(StringUtils.isEmpty(doc)) continue;
            //保存result
            SpiderResultPo spiderResultPo = addResult(url,doc,configSourceId,parentResultId);
            if (spiderResultPo != null) {
                Set<String> docurls = addResultUrls(spiderResultPo,url,doc);
                // 查询刚添加的遍历下一深度 deep
                _spide(docurls,deep + 1,spiderResultPo.getId(),webDriver);
            }
        }
    }
    private Set<String> addResultUrls(SpiderResultPo spiderResultPo,String docUrl,String doc){
        Set<String> urls = null;

        List<SpiderResultUrlsPo> toBeInsert = new ArrayList<>();

        String domain = _getDomain(docUrl);
        String scheme = _getScheme(docUrl);
        Document document = JsoupUtils.htmlStringToDoc(doc);
        SpiderResultUrlsPo spiderResultUrlsPo = null;
        List<Element> links = JsoupUtils.selectList("a[href]",document);
        // 链接
        if (links != null && !links.isEmpty()) {
            urls = new HashSet<>(links.size());
            for (Element link : links) {
                String _url = link.attr("href");
                if(StringUtils.isEmpty(_url) || StringUtils.isEmpty(domain)) continue;
                String _urlFull = _url;
                if(!_urlFull.startsWith("http")){
                    if(_urlFull.startsWith(FileUtils.slash_double)){
                        _urlFull = scheme +":" + _urlFull;

                    }else {
                        _urlFull = domain + _urlFull;
                    }
                }
                if(!_urlFull.startsWith("https://www.toutiao.com")) continue;
                String _text = link.ownText();
                spiderResultUrlsPo = new SpiderResultUrlsPo();
                spiderResultUrlsPo.setConfigSourceId(spiderResultPo.getConfigSourceId());
                spiderResultUrlsPo.setResultId(spiderResultPo.getId());
                spiderResultUrlsPo.setUrl(_url);
                spiderResultUrlsPo.setType("a");
                spiderResultUrlsPo.setUrlText(_text);

                spiderResultUrlsPo.setUrlFull(_urlFull);
                spiderResultUrlsPo = apiSpiderResultUrlsService.preInsert(spiderResultUrlsPo,BasePo.DEFAULT_USER_ID);
                toBeInsert.add(spiderResultUrlsPo);
                urls.add(_urlFull);
            }
        }

        //保存
        if (toBeInsert != null && !toBeInsert.isEmpty()) {
            apiSpiderResultUrlsService.insertBatch(toBeInsert);
        }
        return urls;
    }
    private SpiderResultPo addResult(String url,String doc,String configSourceId,String parentResultId){
        // 如果已存在不操作
        if(apiSpiderResultService.selectByUrl(url) != null){
            logger.info("addResult url exist. url=" + url);

            return null;
        };
        SpiderResultPo spiderResultPo = new SpiderResultPo();
        spiderResultPo.setUrl(url);
        Document document = JsoupUtils.htmlStringToDoc(doc);

        spiderResultPo.setTitle(document.title());
        spiderResultPo.setDocument(doc);
        spiderResultPo.setConfigSourceId(configSourceId);
        spiderResultPo.setParentId(parentResultId);
        apiSpiderResultService.preInsert(spiderResultPo,BasePo.DEFAULT_USER_ID);
        spiderResultPo = apiSpiderResultService.insertSimple(spiderResultPo);
        logger.info("addResult title=" + document.title());
        logger.info("addResult url=" + url);
        logger.info("addResult configSourceId=" + configSourceId);
        return spiderResultPo;
    }
    @Override
    public void spide(List<SpiderResultUrlsPo> spiderResultUrlsPos) {
        if (spiderResultUrlsPos != null) {
            WebDriver webDriver = null;
            try {
                webDriver = webDriverFactory.newWebDrive(driverType_firefox);
            } catch (Exception e) {
                logger.error(e.getMessage(),e);
                return;
            }
            _spide(spiderResultUrlsPos,1,null,webDriver);
            webDriver.quit();
        }
    }
    public void _spide(List<SpiderResultUrlsPo> spiderResultUrlsPos,int deep,String parentResultId,WebDriver webDriver){
        if (spiderResultUrlsPos == null || spiderResultUrlsPos.isEmpty()) {
            return;
        }
        Set<String> urls = new HashSet<>(spiderResultUrlsPos.size());
        for (SpiderResultUrlsPo spiderResultUrlsPo : spiderResultUrlsPos) {
            urls.add(spiderResultUrlsPo.getUrlFull());
        }
        _spide(urls,deep,parentResultId,webDriver);
    }
}
