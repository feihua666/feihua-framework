package com.feihua.framework.cms.front.web.mvc;

import com.feihua.exception.PageNotFoundException;
import com.feihua.framework.cms.api.ApiCmsSitePoService;
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

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.List;

import static com.feihua.framework.cms.front.web.mvc.DynamicBaseController.requestPathPrefix;

/**
 * cms前端页面首页访问入口
 * Created by yangwei
 */
@Controller
@RequestMapping(requestPathPrefix)
public class DynamicIndexPageController extends DynamicBaseController {

    private static Logger logger = LoggerFactory.getLogger(DynamicIndexPageController.class);


    @Autowired
    private ApiCmsSitePoService apiCmsSitePoService;
    /**
     * 首页
     * @return
     */
    @RequestMapping(value = {""},method = RequestMethod.GET)
    public String indexEmpty(Model model){
        return indexHtml(model);
    }
    /**
     * 首页
     * @return
     */
    @RequestMapping(value = {"/"},method = RequestMethod.GET)
    public String indexRoot(Model model){
        return indexHtml(model);
    }

    /**
     * 以动态方式访问静态首页，如果不存在静态页,跳转到动态首页访问
     * @return
     */
    @RequestMapping(value = {"/index.html"},method = RequestMethod.GET)
    public String indexHtml(Model model){

        CmsSitePo cmsSitePo = getMainSiteByDomain();
        HttpServletRequest request = RequestUtils.getRequest();

        return indexStatic(cmsSitePo,request,model);
    }

    /**
     * 动态首页
     * @return
     */
    @RequestMapping(value = {"/index.htm"},method = RequestMethod.GET)
    public String indexHtm(Model model){
        HttpServletRequest request = RequestUtils.getRequest();
        CmsSitePo cmsSitePo = getMainSiteByDomain();
        return indexDynamic(cmsSitePo,model);
    }
    /**
     * 上下文首页
     * @return
     */
    @RequestMapping(value = {"/{siteContextPath}"},method = RequestMethod.GET)
    public String indexEmptyContext(@PathVariable  String siteContextPath,Model model){
        return indexHtmlContext(siteContextPath,model);
    }
    /**
     * 上下文首页
     * @return
     */
    @RequestMapping(value = {"/{siteContextPath}/"},method = RequestMethod.GET)
    public String indexRootContext(@PathVariable  String siteContextPath,Model model){
        return indexHtmlContext(siteContextPath,model);
    }

    /**
     * 上下文静态首页
     * @return
     */
    @RequestMapping(value = {"/{siteContextPath}/index.html"},method = RequestMethod.GET)
    public String indexHtmlContext(@PathVariable  String siteContextPath,Model model){

        HttpServletRequest request = RequestUtils.getRequest();
        //根据上下文查找站点
        CmsSitePo cmsSitePo = getSiteByPath(RequestUtils.wrapStartSlash(siteContextPath));
        return indexStatic(cmsSitePo,request,model);
    }

    /**
     * 上下文动态首页
     * @return
     */
    @RequestMapping(value = {"/{siteContextPath}/index.htm"},method = RequestMethod.GET)
    public String indexHtmContext(@PathVariable  String siteContextPath,Model model){
        //根据上下文查找站点
        CmsSitePo cmsSitePo = getSiteByPath(RequestUtils.wrapStartSlash(siteContextPath));
        return indexDynamic(cmsSitePo,model);
    }
    private String indexStatic(CmsSitePo cmsSitePo ,HttpServletRequest request,Model model){
        if (cmsSitePo != null) {
            //检查是否存在静态页，如果存在静态页
            String webAppRealPath = RequestUtils.getWebappRealPath(request);
            String indexRealPath = webAppRealPath + File.separator + FileUtils.wrapStartFileSeparator(cmsSitePo.getStaticPath()) + File.separator + indexHtml;
            File indexFile = FileUtils.getFile(indexRealPath);
            // 如果存在静态首页，直接重定向到静态首页
            if (indexFile.exists()) {
                return "redirect:" + RequestUtils.wrapStartSlash(cmsSitePo.getStaticPath()) + RequestUtils.wrapStartSlash(indexHtml);
            }
            //如果静态页不存在，返回动态页内容
            return indexDynamic(cmsSitePo,model);
        }else {
            // 不存在站点，直接抛出异常
            throw new PageNotFoundException("");
        }
    }
    private String indexDynamic(CmsSitePo cmsSitePo ,Model model){
        if (cmsSitePo != null) {
            //存在站点
            // 查找模板
            model.addAttribute("site",cmsSitePo);
            String template = indexHtml;
            if(!StringUtils.isEmpty(cmsSitePo.getTemplate())){
                template = cmsSitePo.getTemplate();
            }
            String templatePath = getTemplatePathForViewResolver(cmsSitePo) + FileUtils.wrapStartFileSeparator(template);
            return templatePath;

        }else {
            // 不存在站点，直接抛出异常
            throw new PageNotFoundException("");
        }
    }
}
