package com.feihua.framework.cms.front.web.mvc;

import com.feihua.framework.cms.api.ApiCmsContentPageViewPoService;
import com.feihua.framework.cms.api.ApiCmsContentPoService;
import com.feihua.framework.cms.po.CmsContentPageViewPo;
import com.feihua.framework.cms.po.CmsContentPo;
import com.feihua.framework.rest.ResponseJsonRender;
import com.feihua.utils.http.httpServletRequest.RequestUtils;
import com.feihua.utils.http.httpServletResponse.ResponseCode;
import feihua.jdbc.api.pojo.BasePo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * cms内容前端页面访问入口
 * Created by yangwei
 */
@Controller
@RequestMapping("/cms/front")
public class FrontContentController extends FrontBaseController {

    private static Logger logger = LoggerFactory.getLogger(FrontContentController.class);

    @Autowired
    private ApiCmsContentPageViewPoService apiCmsContentPageViewPoService;
    @Autowired
    private ApiCmsContentPoService apiCmsContentPoService;

    /**
     * 单资源，更新访问数量
     * @param id
     * @return
     */
    @RequestMapping(value = "/content/{id}/count",method = RequestMethod.PUT)
    public ResponseEntity contentCount(@PathVariable String id, HttpServletRequest request){
        logger.info("更新内容访问量开始");
        logger.info("内容id:{}",id);
        ResponseJsonRender resultData=new ResponseJsonRender();

        CmsContentPo cmsContentPo = apiCmsContentPoService.selectByPrimaryKeySimple(id,false);
        if (cmsContentPo == null) {
            resultData.setCode(ResponseCode.E404_100001.getCode());
            resultData.setMsg(ResponseCode.E404_100001.getMsg());
            logger.info("code:{},msg:{}",resultData.getCode(),resultData.getMsg());
            logger.info("更新内容访问量结束，失败");
            return new ResponseEntity(resultData, HttpStatus.NOT_FOUND);
        }

        Date today = new Date();
        String host = RequestUtils.getRemoteAddr(request);
        int existHost = apiCmsContentPageViewPoService.existHost(host,today);
        Cookie cookie = RequestUtils.getCookieByName(self_session_id_name,request);

        int existCookie = 0;
        if (cookie != null) {
            String cookieValue = cookie.getValue();
            existCookie = apiCmsContentPageViewPoService.existCookie(cookieValue,today);
        }


        //添加访问记录
        CmsContentPageViewPo cmsContentPageViewPo = new CmsContentPageViewPo();
        cmsContentPageViewPo.setContentId(id);
        cmsContentPageViewPo.setSiteId(cmsContentPo.getSiteId());
        cmsContentPageViewPo.setHost(RequestUtils.getRemoteAddr(request));
        if (cookie != null) {
            cmsContentPageViewPo.setCookie(cookie.getValue());
        }
        String userId = getLoginUserId();
        if(StringUtils.isEmpty(userId)){
            userId = BasePo.DEFAULT_USER_ID;
        }
        cmsContentPageViewPo = apiCmsContentPageViewPoService.preInsert(cmsContentPageViewPo,userId);
        apiCmsContentPageViewPoService.insert(cmsContentPageViewPo);

        // 表单值设置
        CmsContentPo basePo = new CmsContentPo();
        // id
        basePo.setId(id);

        basePo.setPv(cmsContentPo.getPv().intValue() + 1);
        if(existHost == 0){
            basePo.setIv(cmsContentPo.getIv().intValue() + 1);
        }
        if(existCookie == 0){
            basePo.setUv(cmsContentPo.getUv().intValue() + 1);
        }

        int r = apiCmsContentPoService.updateByPrimaryKeySelective(basePo);
        // 更新成功，已被成功创建
        logger.info("更新的内容id:{}",id);
        logger.info("更新内容访问量结束，成功");
        return new ResponseEntity(resultData, HttpStatus.CREATED);
    }
}
