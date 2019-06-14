package com.feihua.framework.cms.admin.rest.mvc;

import com.feihua.framework.base.modules.postjob.dto.BasePostDto;
import com.feihua.framework.base.modules.role.dto.BaseRoleDto;
import com.feihua.framework.cms.CmsConstants;
import com.feihua.framework.cms.api.ApiCmsChannelPoService;
import com.feihua.framework.cms.api.ApiCmsContentPoService;
import com.feihua.framework.cms.api.ApiCmsSitePoService;
import com.feihua.framework.cms.dto.CmsTemplateModelContextDto;
import com.feihua.framework.cms.po.CmsChannelPo;
import com.feihua.framework.cms.po.CmsContentPo;
import com.feihua.framework.cms.po.CmsSitePo;
import com.feihua.framework.rest.mvc.SuperController;
import com.feihua.framework.shiro.pojo.ShiroUser;
import com.feihua.utils.http.httpServletRequest.RequestUtils;
import com.feihua.utils.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * cms前端页面访问入口
 * Created by yangwei
 */
@Controller
public class BaseController extends SuperController {
    private static Logger logger = LoggerFactory.getLogger(BaseController.class);


    public BaseRoleDto getLoginUserRole(){
        ShiroUser su = super.getLoginUser();
        if(su != null){
            return (BaseRoleDto) su.getRole();
        }
        return null;
    }
    public BasePostDto getLoginUserPost(){
        ShiroUser su = super.getLoginUser();
        if(su != null){
            return (BasePostDto) su.getPost();
        }
        return null;
    }
    public String getLoginUserRoleId(){
        BaseRoleDto roleDto = getLoginUserRole();
        if(roleDto != null){
            return roleDto.getId();
        }
        return null;
    }
    public String getLoginUserPostId(){
        BasePostDto postDto = getLoginUserPost();
        if(postDto != null){
            return postDto.getId();
        }
        return null;
    }

    /**
     * 获取path路径下的文件夹名或文件名
     * @param path
     * @param folder
     * @return
     */
    public List<String> getFileNames(String path,boolean  folder){
        HttpServletRequest request = RequestUtils.getRequest();

        String webappRealPath = RequestUtils.getWebappRealPath(request) + CmsConstants.webinfPath + CmsConstants.templateRootPath;
        List<File> allFile = null;

        if (StringUtils.isNotEmpty(path)){
            webappRealPath = webappRealPath + RequestUtils.wrapStartSlash(path);
        }
        if(folder){
            allFile = FileUtils.getAllFolder(webappRealPath);
        }else{
            allFile = FileUtils.getAllFile(webappRealPath);
        }

        if(allFile != null && !allFile.isEmpty()) {
            List<String> templatePathStr = new ArrayList<>(allFile.size());
            for (File file : allFile) {
                templatePathStr.add(file.getName());
            }
            return templatePathStr;
        }

        return null;
    }
}
